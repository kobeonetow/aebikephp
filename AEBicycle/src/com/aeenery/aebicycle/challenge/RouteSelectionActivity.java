package com.aeenery.aebicycle.challenge;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.aeenery.aebicycle.R;
import com.aeenery.aebicycle.entry.BicycleUtil;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.GeoPoint;
import com.baidu.mapapi.LocationListener;
import com.baidu.mapapi.MKAddrInfo;
import com.baidu.mapapi.MKBusLineResult;
import com.baidu.mapapi.MKDrivingRouteResult;
import com.baidu.mapapi.MKLocationManager;
import com.baidu.mapapi.MKPlanNode;
import com.baidu.mapapi.MKPoiInfo;
import com.baidu.mapapi.MKPoiResult;
import com.baidu.mapapi.MKRoute;
import com.baidu.mapapi.MKSearch;
import com.baidu.mapapi.MKSearchListener;
import com.baidu.mapapi.MKSuggestionResult;
import com.baidu.mapapi.MKTransitRouteResult;
import com.baidu.mapapi.MKWalkingRouteResult;
import com.baidu.mapapi.MapActivity;
import com.baidu.mapapi.MapController;
import com.baidu.mapapi.MapView;
import com.baidu.mapapi.MyLocationOverlay;
import com.baidu.mapapi.PoiOverlay;
import com.baidu.mapapi.RouteOverlay;

public class RouteSelectionActivity extends MapActivity {

	private BMapManager mBMapMan = null;
	private MapView mMapView;
	private MapController mMapController;
	private MyLocationOverlay myLocOverlay;
	private MKSearch mMkSearch;
	private OnClickListener ibListener;
	private boolean bSearch;
	private ProgressDialog pd;
	private int selFlag;//选择定位详细地址标记
	private MKPlanNode startNode, terminateNode, focusNode;	
	public ArrayList<MKPoiInfo> displayPositionList;
	
	private Button btnSearchStart;
	private Button btnSearchTerminate;
	private Button btnSearchRiding;
	private Button btnConfirm;
	private EditText planStartLoc, planTerminateLoc;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_route_selection);
		init();
		setAdapter();
	}

	private void init() {
		//获取Widget
		btnSearchStart = (Button)findViewById(R.id.btn_selection_search_start_location);
		btnSearchTerminate = (Button)findViewById(R.id.btn_selection_search_terminate_location);
		btnSearchRiding = (Button)findViewById(R.id.btn_selection_ride_button);
		btnConfirm = (Button)findViewById(R.id.btn_return_coordinate_button);
		
		planStartLoc = (EditText)findViewById(R.id.selection_start_location);
		planTerminateLoc = (EditText)findViewById(R.id.select_end_location);
		
		displayPositionList = new ArrayList<MKPoiInfo>();
		//初始化百度map 类
		mBMapMan = new BMapManager(getApplication());
		mBMapMan.init(BicycleUtil.mapKey, null);
		super.initMapActivity(mBMapMan);
		mMapView = (MapView)findViewById(R.id.selection_bmapView);
		mMapView.setBuiltInZoomControls(true);//设置内置缩放控件
		mMapView.setDrawOverlayWhenZooming(true);
		mMapController = mMapView.getController();
		mMkSearch = new MKSearch();
		mMkSearch.init(mBMapMan, new RouteMapSearchListener());
		
		//Start and Terminate Nodes
		startNode = new MKPlanNode();
		terminateNode = new MKPlanNode();
		
		Intent intent = getIntent();
		if(intent.getBooleanExtra("hasStart", false)){
			startNode.name = intent.getStringExtra("startPoiName");
			startNode.pt = new GeoPoint(intent.getIntExtra("startPoiLat", 0),intent.getIntExtra("startPoiLon", 0));
			planStartLoc.setText(startNode.name);
			Log.i("StartPoi","Name:"+startNode.name + " Lat:"+startNode.pt.getLatitudeE6()/1e6 + " Lon:" +startNode.pt.getLongitudeE6()/1e6);
		}
		if(intent.getBooleanExtra("hasTerminate", false)){
			terminateNode.name = intent.getStringExtra("terminatePoiName");
			terminateNode.pt = new GeoPoint(intent.getIntExtra("terminatePoiLat", 0), intent.getIntExtra("terminatePoiLon", 0));
			planTerminateLoc.setText(terminateNode.name);
			Log.i("StartPoi","Name:"+terminateNode.name + " Lat:"+terminateNode.pt.getLatitudeE6()/1e6 + " Lon:" +terminateNode.pt.getLongitudeE6()/1e6);
		}
		if(startNode.pt != null && terminateNode.pt != null){
			selFlag = BicycleUtil.ibSearchDst;
			showDialog();
			SearchButtonProcess(btnSearchRiding);
		}
		if(intent.getBooleanExtra("checkRoute", false)){
			LinearLayout routeEditor = (LinearLayout)findViewById(R.id.route_information_editing);
			routeEditor.setVisibility(View.GONE);
		}
		
		// 初始化Location模块
		MKLocationManager myLocMgr = mBMapMan.getLocationManager();
		
		//通过enableProvider和disableProvider方法，选择定位的Provider
		myLocMgr.enableProvider(MKLocationManager.MK_NETWORK_PROVIDER);
		myLocMgr.enableProvider(MKLocationManager.MK_GPS_PROVIDER);
		myLocOverlay = new MyLocationOverlay(this, mMapView);
		myLocOverlay.enableMyLocation();//用户定位
		myLocOverlay.enableCompass();//用户指南针
		mMapView.getOverlays().add(myLocOverlay);
		//1 runOnFirstFix，在第一次获取坐标时就启动一个线程。
		//2 mMapController.animateTo(myLocationOverlay.getMyLocation()); 
		//  这个线程的作用是把地图中心移动到用户的位置。
		{
			myLocOverlay.runOnFirstFix(new Runnable() {
				@Override
				public void run() {
					// 初始化路径选择源
					{
						mMapController.animateTo(myLocOverlay.getMyLocation());
					}
				}
			});

			// 定位
			myLocMgr.requestLocationUpdates(new LocationListener() {
				@Override
				public void onLocationChanged(Location arg0) {
					{
						mMapController.animateTo(myLocOverlay.getMyLocation());
					}
				}
			});

		}
	}
	
	
	private void setAdapter() {
		// TODO Auto-generated method stub
		ibListener = new OnClickListener() {

			@Override
			public void onClick(final View v) {
				Log.i("OnClick","ID:"+v.getId());
				// TODO Auto-generated method stub
				switch (v.getId()) {
				case R.id.btn_selection_search_start_location: 
					selFlag = BicycleUtil.ibRouteSearchSrc;
					if(planStartLoc.getEditableText().toString().length() > 0)
						searchAddress(planStartLoc.getEditableText().toString());
					else
						Toast.makeText(RouteSelectionActivity.this, "请输起点地址", Toast.LENGTH_LONG).show();
					break;
				case R.id.btn_selection_search_terminate_location:
					selFlag = BicycleUtil.ibRouteSearchDst;
					if(planTerminateLoc.getEditableText().toString().length() > 0)
						searchAddress(planTerminateLoc.getEditableText().toString());
					else
						Toast.makeText(RouteSelectionActivity.this, "请输终点地址", Toast.LENGTH_LONG).show();
					break;
				case R.id.btn_selection_ride_button:
					selFlag = BicycleUtil.ibSearchDst;
					showDialog();
					SearchButtonProcess(v);
					break;
				case R.id.btn_return_coordinate_button:
					obtainRoutePois(v);
					Log.i("ReturnPress","Returned Pressed");
					break;
					
				}
			}
		};
		btnSearchRiding.setOnClickListener(ibListener);
		btnSearchStart.setOnClickListener(ibListener);
		btnSearchTerminate.setOnClickListener(ibListener);
		btnConfirm.setOnClickListener(ibListener);
	}

	public void obtainRoutePois(View v){
		Log.i("Route","Obtaining");
		if(!(planStartLoc.getEditableText().toString().length() > 0 && planTerminateLoc.getEditableText().toString().length() > 0)){
			Toast.makeText(RouteSelectionActivity.this, "请输入路程起点和终点", Toast.LENGTH_LONG).show();
		}
		selFlag = BicycleUtil.ibRouteSearchIgnoreView;
		Log.i("Route","Searching");
		SearchButtonProcess(v);
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		if (mBMapMan != null) {
	        mBMapMan.destroy();
	        mBMapMan = null;
	    }
		super.onDestroy();
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		if (mBMapMan != null) {
	        mBMapMan.stop();
	    }
		super.onPause();
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		if (mBMapMan != null) {
	        mBMapMan.start();
	    }
		super.onResume();
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
	
	//地图搜索监听接口
	public class RouteMapSearchListener implements MKSearchListener{

		@Override
		public void onGetAddrResult(MKAddrInfo result, int error) {
			// TODO Auto-generated method stub
			if(result == null){
				return;
			}
		}

		@Override
		public void onGetPoiResult(MKPoiResult result, int type, int iError) {
			// TODO Auto-generated method stub
			if(iError != 0 || result == null){
				closeDialog();
				Toast.makeText(RouteSelectionActivity.this, "抱歉，未找到结果", Toast.LENGTH_LONG).show();
				return;
			}
			if(bSearch && result.getCurrentNumPois() > 0)
			{
				closeDialog();
				getSearchListItemRoute(result);
				bSearch = false;
			}
		}

		@Override
		public void onGetWalkingRouteResult(MKWalkingRouteResult result, int iError) {
			// TODO Auto-generated method stub  
			if (result == null) {
				closeDialog();
		        return;
		    }
			
			MKRoute route = result.getPlan(0).getRoute(0);
			RouteOverlay routeOverlay = new RouteOverlay(RouteSelectionActivity.this, mMapView);
		    // 此处仅展示一个方案作为示例
		    mMapView.getOverlays().clear();
		    routeOverlay.setData(route);
		    mMapView.getOverlays().add(routeOverlay);
		    mMapView.invalidate();//手动刷新地图，否则会延迟显示
		    
		    startNode.pt = route.getStart();
		    terminateNode.pt = route.getEnd();
	
		    if(BicycleUtil.ibRouteSearchIgnoreView == selFlag){
		    	Bundle bundle = new Bundle();
		    	if(!(startNode.pt == null && terminateNode.pt == null)){
					bundle.putBoolean("hasRoute",true);
					bundle.putString("startPoiName", startNode.name);
					bundle.putInt("startPoiLon", startNode.pt.getLongitudeE6());
					bundle.putInt("startPoiLat", startNode.pt.getLatitudeE6());
					bundle.putString("terminatePoiName", terminateNode.name);
					bundle.putInt("terminatePoiLon", terminateNode.pt.getLongitudeE6());
					bundle.putInt("terminatePoiLat",terminateNode.pt.getLatitudeE6());
					bundle.putInt("distance", route.getDistance());
				}else{
					bundle.putBoolean("hasRoute",false);
				}
		    	returnDataToQucikPlanActivity(bundle);
		    }else{
		    	mMapView.getController().animateTo(startNode.pt);
		    }
		    
		    closeDialog();
		}

		private void returnDataToQucikPlanActivity(Bundle bundle) {
			Intent intent = new Intent();
//			intent.putExtra("returnBundle", bundle);
			intent.putExtras(bundle);
			Log.i("returnning", "set result");
			RouteSelectionActivity.this.setResult(BicycleUtil.PLAN_SELECT_ROUTE, intent);
			Log.i("returnning", "wait for finish");
			RouteSelectionActivity.this.finish();
		}

		@Override
		public void onGetBusDetailResult(MKBusLineResult arg0, int arg1) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void onGetDrivingRouteResult(MKDrivingRouteResult arg0, int arg1) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void onGetPoiDetailSearchResult(int arg0, int arg1) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void onGetRGCShareUrlResult(String arg0, int arg1) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void onGetSuggestionResult(MKSuggestionResult arg0, int arg1) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void onGetTransitRouteResult(MKTransitRouteResult arg0, int arg1) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	protected void doExit(){
		new AlertDialog.Builder(RouteSelectionActivity.this)
			.setTitle("提示")
			.setMessage("确定要退出吗?")
			.setPositiveButton("确定", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					finish();
				}
			})
			.setNegativeButton("取消", null).show();
	}
	
	//获取搜索列表项目
	public void getSearchListItemRoute(final MKPoiResult searchList){
		final ArrayList<String>  addrArr = new ArrayList<String> ();
		for(int i = 0; i < searchList.getCurrentNumPois(); i++){
			addrArr.add(searchList.getPoi(i).address);
		}
//		itemIdx = 0;
		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		dialog.setTitle("地址选择");
		dialog.setItems(addrArr.toArray(new String[searchList.getCurrentNumPois()]), new DialogInterface.OnClickListener() {
		
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				GeoPoint pt = searchList.getPoi(which).pt;
				displayPositionList.clear();
				displayPositionList.add(searchList.getAllPoi().get(which));
				switch(selFlag){
					case BicycleUtil.ibRouteSearchSrc:
						startNode.pt = pt;
//						startNode.name = planStartLoc.getEditableText().toString();
						startNode.name = searchList.getAllPoi().get(which).name;
						planStartLoc.setText(startNode.name);
						focusNode = startNode;
						Log.i("StartNode","Set focus Node");
						closeDialog();
						break;
					case BicycleUtil.ibRouteSearchDst:
						terminateNode.pt = pt;
//						terminateNode.name = planTerminateLoc.getEditableText().toString();
						terminateNode.name = searchList.getAllPoi().get(which).name;
						planTerminateLoc.setText(terminateNode.name);
						focusNode = terminateNode;
						closeDialog();
						break;
					default:
						Log.i("Flag","Flag neigher of all");
				}
				
			    // 将poi结果显示到地图上
				PoiOverlay poiOverlay = new PoiOverlay(RouteSelectionActivity.this, mMapView, mMkSearch);
				poiOverlay.setData(displayPositionList);
			    mMapView.getOverlays().clear();
			    mMapView.getOverlays().add(poiOverlay);
			    mMapView.invalidate();
			    if(focusNode != null && focusNode.pt != null){
			    	mMapView.getController().animateTo(focusNode.pt);
			    }
			}
		});
		dialog.create();
		dialog.show();
	}
	
	public void showDialog(){
		pd = ProgressDialog.show(RouteSelectionActivity.this, "提示", "数据加载中,请稍后...");
		pd.setCancelable(true);
	}
	
	public void showDialog(String title, String msg){
		if (title == null) {
			title = "提示";
		}
		if (msg == null) {
			msg = "数据加载中...";
		}
		pd = ProgressDialog.show(RouteSelectionActivity.this, title, msg);
	}
	
	public void closeDialog(){
		pd.cancel();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		{
			if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
				doExit();
				return true;	
			}
		}
		return super.onKeyDown(keyCode, event);
	}
	
	void SearchButtonProcess(View v) {
		// 对起点终点的name进行赋值，也可以直接对坐标赋值，赋值坐标则将根据坐标进行搜索
		String startName = planStartLoc.getText().toString();
		String terminateName = planTerminateLoc.getText().toString();
		if(!startName.equals(startNode.name)){
			startNode.name = startName;
			startNode.pt = null;
		}
		if(!terminateName.equals(terminateNode.name)){
			terminateNode.name = terminateName;
			terminateNode.pt = null;
		}
		showDialog();
		// 实际使用中请对起点终点城市进行正确的设定
		if (btnSearchRiding.equals(v) || btnConfirm.equals(v)) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				Log.i("Route","Obtaining Route");
				mMkSearch.walkingSearch(startNode.name, startNode, terminateNode.name, terminateNode);
			}
			
		}).start();
		}
		
	}
	
	public void searchAddress(String searchStrDst){
		final String[] arrStr = searchStrDst.split(" ");// 通过空格间断字符串
		bSearch = true;
		showDialog();
		new Thread(new Runnable() {
			@Override
			public void run() {
				Log.i("Address","Seaching address ");
				// TODO Auto-generated method stub
//				if (arrStr.length > 1)
//					mMkSearch.poiSearchInCity(arrStr[0], arrStr[1]);
//				else if (arrStr.length == 1) // 当前城市搜索
					mMkSearch.poiSearchInCity(null, arrStr[0]);
			}
		}).start();
	}
}
