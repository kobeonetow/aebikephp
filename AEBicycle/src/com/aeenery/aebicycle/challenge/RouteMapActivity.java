//package com.aeenery.aebicycle.challenge;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import android.app.AlertDialog;
//import android.app.ProgressDialog;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.graphics.Rect;
//import android.graphics.drawable.BitmapDrawable;
//import android.location.Location;
//import android.os.Bundle;
//import android.view.Gravity;
//import android.view.KeyEvent;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.Window;
//import android.widget.EditText;
//import android.widget.ImageButton;
//import android.widget.ListView;
//import android.widget.PopupWindow;
//import android.widget.SimpleAdapter;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.aeenery.aebicycle.R;
//import com.aeenery.aebicycle.entry.BicycleUtil;
//import com.baidu.mapapi.BMapManager;
//import com.baidu.mapapi.GeoPoint;
//import com.baidu.mapapi.LocationListener;
//import com.baidu.mapapi.MKAddrInfo;
//import com.baidu.mapapi.MKBusLineResult;
//import com.baidu.mapapi.MKDrivingRouteResult;
//import com.baidu.mapapi.MKLocationManager;
//import com.baidu.mapapi.MKPlanNode;
//import com.baidu.mapapi.MKPoiResult;
//import com.baidu.mapapi.MKRoute;
//import com.baidu.mapapi.MKRoutePlan;
//import com.baidu.mapapi.MKSearch;
//import com.baidu.mapapi.MKSearchListener;
//import com.baidu.mapapi.MKSuggestionResult;
//import com.baidu.mapapi.MKTransitRouteResult;
//import com.baidu.mapapi.MKWalkingRouteResult;
//import com.baidu.mapapi.MapActivity;
//import com.baidu.mapapi.MapController;
//import com.baidu.mapapi.MapView;
//import com.baidu.mapapi.MyLocationOverlay;
//import com.baidu.mapapi.RouteOverlay;
//
//public class RouteMapActivity extends MapActivity {
//	
//	private BMapManager mBMapMan = null;
//	private MapView mMapView;
//	private MapController mMapController;
//	private MyLocationOverlay myLocOverlay;
//	private MKSearch mMkSearch;
//	private ImageButton ibSearch,ibRoute,ibInfo,pwIbRouteSrc,pwIbRouteDst,pwIbRouteSearch;
//	private EditText etDst,pwEtRouteSrc,pwEtRouteDst;
//	private String strSrc,strDst;
//	private boolean isDst;//标记是否查找目的地址的地址
//	private boolean isSrc;//标记是否查找源地址的地址
//	private OnClickListener ibListener;
//	//private byte[] lock = new byte[0];
//	private boolean bSearch;
//	private ProgressDialog pd;
//	private int itemIdx,routeIdx;
//	private PopupWindow pwRouteInfo,pwRouteSel;
//	private ListView pwRiLv;
//	private TextView tvRiRouteName;
//	private MKWalkingRouteResult mkWalkRouteRs = null;
//	private List RouteInfoList = null;
//	private int selFlag;//选择定位详细地址标记
//	private MKPlanNode pnSrc,pnDst;
//	private boolean isShowRoute;//是否在显示路线状态
//	private boolean bShareBt;//是否拥有分享路线按钮
//	private int srcLatitudeE6;
//	private int srcLongitudeE6;
//	private int dstLatitudeE6;
//	private int dstLongitudeE6;
//	private MKPlanNode mkSrc,mkDst;
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		// TODO Auto-generated method stub
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_route_map);
//		Intent intent= getIntent();
//		srcLatitudeE6 = intent.getIntExtra("srcLatitudeE6", 0);
//		srcLongitudeE6 = intent.getIntExtra("srcLongitudeE6", 0);
//		dstLatitudeE6 = intent.getIntExtra("dstLatitudeE6", 0);
//		dstLongitudeE6 = intent.getIntExtra("dstLongitudeE6", 0);
//		bShareBt = intent.getBooleanExtra("ifShareBt", true);
//		if (srcLatitudeE6 != 0 || srcLongitudeE6 != 0 || dstLatitudeE6 != 0
//				|| dstLongitudeE6 != 0)
//			isShowRoute = true;
//		init();
//		setAdapter();
//		if (isShowRoute) {
//			mkSrc = new MKPlanNode();
//			isSrc = true;
//			mkSrc.pt = new GeoPoint(srcLatitudeE6, srcLongitudeE6);
//			mkDst = new MKPlanNode();
//			mkDst.pt = new GeoPoint(dstLatitudeE6, dstLongitudeE6);
//			showDialog();
//			//mMkSearch.walkingSearch(null, mkSrc, null, mkDst);
//			//查询路线的源地址的详细地址
//			mMkSearch.reverseGeocode(mkSrc.pt);
//		}
//	}
//
//	private void setAdapter() {
//		// TODO Auto-generated method stub
//		ibListener = new OnClickListener() {
//
//			@Override
//			public void onClick(final View v) {
//				// TODO Auto-generated method stub
//				switch (v.getId()) {
//				case R.id.ibMapSearch: {
//					selFlag = BicycleUtil.ibSearchDst;
//					if(etDst.getEditableText().toString().length() > 0)
//						searchAddress(etDst.getEditableText().toString());
//					else
//						Toast.makeText(RouteMapActivity.this, "请输入目的地址", Toast.LENGTH_LONG).show();
//				}
//				break;
//				case R.id.ibMapInfo:{
//					if (!pwRouteInfo.isShowing() && mkWalkRouteRs != null) {
//						StringBuffer strRouteName = new StringBuffer();
//						MKRoutePlan mkRoutePlan;
//						mkRoutePlan = mkWalkRouteRs.getPlan(routeIdx);
//						strRouteName.append(strSrc);
//						strRouteName.append(" - ");
//						strRouteName.append(strDst + "\n" + "共");
//						strRouteName.append(Integer.toString(mkRoutePlan.getDistance()) + "公里");
//						tvRiRouteName.setText(strRouteName);
//						MKRoute mkRoute = mkRoutePlan.getRoute(0);
//						if(RouteInfoList.size() > 0){
//							RouteInfoList.clear();
//						}
//						for(int i = 0; i < mkRoute.getNumSteps()-1; i++){
//							Map map = new HashMap();
//							map.put("title", mkRoute.getStep(i).getContent());
//							RouteInfoList.add(map);
//						}
//						pwRiLv.setAdapter(new SimpleAdapter(RouteMapActivity.this,RouteInfoList, R.layout.routeinfo_list_item,new String[]{"title"},new int[]{R.id.tvRouteInfoItem}));
//						pwRouteInfo.showAsDropDown(v);
//					}else {
//						Toast.makeText(RouteMapActivity.this, "当前没有路线信息", 3000).show();
//					}
//				}
//				break;
//				case R.id.ibMapRoute:{
//					if(!pwRouteSel.isShowing()){
//						Rect frame =  new  Rect();  
//						getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);  
//						int  statusBarHeight = frame.top;  
//						pwRouteSel.showAtLocation(v, Gravity.NO_GRAVITY, 0, getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop() + statusBarHeight);
//					}
//				}
//				break;
//				case R.id.ibRouteSrcSel:
//				case R.id.ibRouteDstSel:{
//					AlertDialog.Builder dialog = new AlertDialog.Builder(RouteMapActivity.this);
//					String[] options = {"我的位置","详细地址"};
//					dialog.setTitle("位置选择");
//					dialog.setItems(options, new DialogInterface.OnClickListener() {
//						
//						@Override
//						public void onClick(DialogInterface dialog, int which) {
//							// TODO Auto-generated method stub
//							switch(which){
//								case 0:{
//									if(v.getId() == R.id.ibRouteSrcSel)
//									{
//										pwEtRouteSrc.setText("我的位置");
//										strSrc = pwEtRouteSrc.getEditableText().toString();
//										pnSrc.pt = myLocOverlay.getMyLocation();
//										
//									}
//									else if(v.getId() == R.id.ibRouteDstSel)
//									{
//										pwEtRouteDst.setText("我的位置");
//										strDst = pwEtRouteDst.getEditableText().toString();
//										pnDst.pt = myLocOverlay.getMyLocation();
//									}
//								}
//									break;
//								case 1:
//									if(v.getId() == R.id.ibRouteSrcSel){
//										selFlag = BicycleUtil.ibRouteSearchSrc;
//										searchAddress(pwEtRouteSrc.getEditableText().toString());
//									}
//									else if(v.getId() == R.id.ibRouteDstSel){
//										selFlag = BicycleUtil.ibRouteSearchDst;
//										searchAddress(pwEtRouteDst.getEditableText().toString());
//									}
//									break;
//								
//							}
//						}
//					});
//					dialog.create();
//					dialog.show();
//				}
//					break;
//				case R.id.ibRouteSearch:
//					showDialog();
//					mMkSearch.walkingSearch(null, pnSrc, null, pnDst);
//					pwRouteSel.dismiss();
//				}
//			}
//		};
//
//		ibSearch.setOnClickListener(ibListener);
//		ibInfo.setOnClickListener(ibListener);
//		ibRoute.setOnClickListener(ibListener);
//		pwIbRouteSrc.setOnClickListener(ibListener);
//		pwIbRouteDst.setOnClickListener(ibListener);
//		pwIbRouteSearch.setOnClickListener(ibListener);
//	}
//
//	private void init() {
//		// TODO Auto-generated method stub
//		ibSearch = (ImageButton)findViewById(R.id.ibMapSearch);
//		ibRoute = (ImageButton)findViewById(R.id.ibMapRoute);
//		ibInfo = (ImageButton)findViewById(R.id.ibMapInfo);
//		etDst = (EditText)findViewById(R.id.etMapDst);
//		strSrc = new String("我的位置");
//		bSearch = false;
//		routeIdx = 0;//当前选择路线下标
//		//如果只是用作显示路线，则屏蔽掉搜索等按钮
//		if(isShowRoute)
//		{
//			ibSearch.setVisibility(View.GONE);
//			ibRoute.setVisibility(View.GONE);
//			etDst.setVisibility(View.GONE);
//		}
//		pnSrc = new MKPlanNode();
//		pnDst = new MKPlanNode();
//		RouteInfoList = new ArrayList<String>();
//		mBMapMan = new BMapManager(getApplication());
//		mBMapMan.init(BicycleUtil.mapKey, null);
//		super.initMapActivity(mBMapMan);
//		mMapView = (MapView)findViewById(R.id.bmapsView);
//		mMapView.setBuiltInZoomControls(true);//设置内置缩放控件
//		mMapController = mMapView.getController();
//		mMkSearch = new MKSearch();
//		mMkSearch.init(mBMapMan, new RouteMapSearchListener());
//		// 初始化Location模块
//		MKLocationManager myLocMgr = mBMapMan.getLocationManager();
//		 //通过enableProvider和disableProvider方法，选择定位的Provider
//		myLocMgr.enableProvider(MKLocationManager.MK_NETWORK_PROVIDER);
//		myLocMgr.enableProvider(MKLocationManager.MK_GPS_PROVIDER);
//		myLocOverlay = new MyLocationOverlay(this, mMapView);
//		myLocOverlay.enableMyLocation();//用户定位
//		myLocOverlay.enableCompass();//用户指南针
//		mMapView.getOverlays().add(myLocOverlay);
//		//1 runOnFirstFix，在第一次获取坐标时就启动一个线程。
//		//2 mMapController.animateTo(myLocationOverlay.getMyLocation()); 
//		//  这个线程的作用是把地图中心移动到用户的位置。
//		//if (!isShowRoute) 
//		{
//			myLocOverlay.runOnFirstFix(new Runnable() {
//				@Override
//				public void run() {
//					// TODO Auto-generated method stub
//					// 初始化路径选择源
//					// if(!isShowRoute)
//					{
//						pnSrc.pt = myLocOverlay.getMyLocation();
//						mMapController.animateTo(myLocOverlay.getMyLocation());
//					}
//				}
//			});
//
//			// 定位
//			myLocMgr.requestLocationUpdates(new LocationListener() {
//
//				@Override
//				public void onLocationChanged(Location arg0) {
//					// TODO Auto-generated method stub
//					// if(!isShowRoute)
//					{
//						pnSrc.pt = myLocOverlay.getMyLocation();
//						mMapController.animateTo(myLocOverlay.getMyLocation());
//					}
//				}
//			});
//		}
//		
//		//初始化listview
//		
//		//popWindow初始化
//		LayoutInflater inflater = LayoutInflater.from(this);
//		View popLayout = inflater.inflate(R.layout.popwindow_route_info, null);
//		pwRouteInfo = new PopupWindow(popLayout,android.view.ViewGroup.LayoutParams.FILL_PARENT,android.view.ViewGroup.LayoutParams.FILL_PARENT,false);
//		//点击窗外可消失
//		pwRouteInfo.setBackgroundDrawable(new BitmapDrawable());
//		pwRouteInfo.setOutsideTouchable(true);
//		pwRiLv = (ListView)popLayout.findViewById(R.id.lvRouteSteps);
//		tvRiRouteName = (TextView)popLayout.findViewById(R.id.tvRouteName);
//		
//		View pwRouteLayout = inflater.inflate(R.layout.popwindow_route_select, null);
//		pwRouteSel = new PopupWindow(pwRouteLayout,android.view.ViewGroup.LayoutParams.FILL_PARENT,android.view.ViewGroup.LayoutParams.WRAP_CONTENT,true);
//		pwRouteSel.setBackgroundDrawable(new BitmapDrawable());
//		pwRouteSel.setOutsideTouchable(true);
//		pwEtRouteSrc = (EditText)pwRouteLayout.findViewById(R.id.etRouteSrc);
//		pwEtRouteDst = (EditText)pwRouteLayout.findViewById(R.id.etRouteDst);
//		pwIbRouteSrc = (ImageButton)pwRouteLayout.findViewById(R.id.ibRouteSrcSel);
//		pwIbRouteDst = (ImageButton)pwRouteLayout.findViewById(R.id.ibRouteDstSel);
//		pwIbRouteSearch = (ImageButton)pwRouteLayout.findViewById(R.id.ibRouteSearch);
//		
//	}
//	
//	@Override
//	protected void onDestroy() {
//		// TODO Auto-generated method stub
//		if (mBMapMan != null) {
//	        mBMapMan.destroy();
//	        mBMapMan = null;
//	    }
//		super.onDestroy();
//	}
//	
//	@Override
//	protected void onPause() {
//		// TODO Auto-generated method stub
//		if (mBMapMan != null) {
//	        mBMapMan.stop();
//	    }
//		super.onPause();
//	}
//	
//	@Override
//	protected void onResume() {
//		// TODO Auto-generated method stub
//		if (mBMapMan != null) {
//	        mBMapMan.start();
//	    }
//		super.onResume();
//	}
//
//	@Override
//	protected boolean isRouteDisplayed() {
//		// TODO Auto-generated method stub
//		return false;
//	}
//	
//	//地图搜索监听接口
//	public class RouteMapSearchListener implements MKSearchListener{
//
//		@Override
//		public void onGetAddrResult(MKAddrInfo result, int error) {
//			// TODO Auto-generated method stub
//			if(result == null){
//				return;
//			}
//			if(isShowRoute){
//				if(isDst)
//				{
//					strDst = result.strAddr;
//					isDst = false;
//					//查询整条路线
//					mMkSearch.walkingSearch(null, mkSrc, null, mkDst);
//				}
//				if(isSrc)
//				{
//					strSrc = result.strAddr;
//					isSrc = false;
//					isDst = true;
//					//查询路线的目的地址的详细地址
//					mMkSearch.reverseGeocode(mkDst.pt);
//				}
//			}
//		}
//
//		@Override
//		public void onGetBusDetailResult(MKBusLineResult arg0, int arg1) {
//			// TODO Auto-generated method stub
//			
//		}
//
//		//驾车路线
//		@Override
//		public void onGetDrivingRouteResult(MKDrivingRouteResult result, int iError) {
//			// TODO Auto-generated method stub
//			if(result == null){
//				return;
//			}
//			RouteOverlay routeOverlay = new RouteOverlay(RouteMapActivity.this, mMapView);
//			routeOverlay.setData(result.getPlan(0).getRoute(0));
//		}
//
//		@Override
//		public void onGetPoiResult(MKPoiResult result, int type, int iError) {
//			// TODO Auto-generated method stub
//			if(iError != 0 || result == null){
//				closeDialog();
//				Toast.makeText(RouteMapActivity.this, "抱歉，未找到结果", Toast.LENGTH_LONG).show();
//				return;
//			}
//			if(bSearch && result.getCurrentNumPois() > 0)
//			{
//				getSearchListItemRoute(result);
//				bSearch = false;
//			}
//		}
//
//		@Override
//		public void onGetSuggestionResult(MKSuggestionResult arg0, int arg1) {
//			// TODO Auto-generated method stub
//			
//		}
//
//		@Override
//		public void onGetTransitRouteResult(MKTransitRouteResult arg0, int arg1) {
//			// TODO Auto-generated method stub
//			
//		}
//
//		@Override
//		public void onGetWalkingRouteResult(MKWalkingRouteResult result, int iError) {
//			// TODO Auto-generated method stub  
//			if (result == null) {
//				closeDialog();
//		        return;
//		    }
//			mkWalkRouteRs = result;
//		    RouteOverlay routeOverlay = new RouteOverlay(RouteMapActivity.this, mMapView);
//		    // 此处仅展示一个方案作为示例
//		    mMapView.getOverlays().clear();
//		    routeOverlay.setData(result.getPlan(0).getRoute(0));
//		    mMapView.getOverlays().add(routeOverlay);
//		    mMapView.invalidate();//手动刷新地图，否则会延迟显示
//		    closeDialog();
//		}
//
//		@Override
//		public void onGetPoiDetailSearchResult(int arg0, int arg1) {
//			// TODO Auto-generated method stub
//			
//		}
//
//		@Override
//		public void onGetRGCShareUrlResult(String arg0, int arg1) {
//			// TODO Auto-generated method stub
//			
//		}
//		
//	}
//	
//	protected void doExit(){
//		new AlertDialog.Builder(RouteMapActivity.this)
//			.setTitle("提示")
//			.setMessage("确定要退出吗?")
//			.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//				
//				@Override
//				public void onClick(DialogInterface dialog, int which) {
//					// TODO Auto-generated method stub
//					finish();
//				}
//			})
//			.setNegativeButton("取消", null).show();
//	}
//	
//	//获取搜索列表项目
//	public void getSearchListItemRoute(final MKPoiResult searchList){
//		final ArrayList<String>  addrArr = new ArrayList<String> ();
//		for(int i = 0; i < searchList.getCurrentNumPois(); i++){
//			addrArr.add(searchList.getPoi(i).address);
//		}
//		itemIdx = 0;
//		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
//		dialog.setTitle("地址选择");
//		dialog.setItems(addrArr.toArray(new String[searchList.getCurrentNumPois()]), new DialogInterface.OnClickListener() {
//			
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				// TODO Auto-generated method stub
//				switch(selFlag){
//					case BicycleUtil.ibSearchDst:
//					{
//						etDst.setText(addrArr.get(which));
//						GeoPoint pt = searchList.getPoi(which).pt;
//						MKPlanNode pn1 = new MKPlanNode();
//						MKPlanNode pn2 = new MKPlanNode();
//						pn1.pt = myLocOverlay.getMyLocation();
//						pn2.pt = pt;
//						mMkSearch.walkingSearch(null, pn1, null, pn2);
//					}
//					break;
//					case BicycleUtil.ibRouteSearchSrc:
//					{
//						pwEtRouteSrc.setText(addrArr.get(which));
//						strSrc = addrArr.get(which);
//						pnSrc.pt = searchList.getPoi(which).pt;
//						closeDialog();
//					}
//					break;
//					case BicycleUtil.ibRouteSearchDst:{
//						pwEtRouteDst.setText(addrArr.get(which));
//						strDst = addrArr.get(which);
//						pnDst.pt = searchList.getPoi(which).pt;
//						etDst.setText(addrArr.get(which));
//						closeDialog();
//					}
//					break;
//			}
//			}
//		});
//		dialog.create();
//		dialog.show();
//	}
//	
//	public void showDialog(){
//		pd = ProgressDialog.show(RouteMapActivity.this, "提示", "数据加载中,请稍后...");
//		pd.setCancelable(true);
//	}
//	
//	public void showDialog(String title, String msg){
//		if (title == null) {
//			title = "提示";
//		}
//		if (msg == null) {
//			msg = "数据加载中...";
//		}
//		pd = ProgressDialog.show(RouteMapActivity.this, title, msg);
//	}
//	
//	public void closeDialog(){
//		pd.cancel();
//	}
//	
//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		// TODO Auto-generated method stub
//		if(pwRouteInfo.isShowing()){
//			pwRouteInfo.dismiss();
//			return true;
//		}else if(pwRouteSel.isShowing()){
//			pwRouteSel.dismiss();
//			return true;
//		}
//		else
//		{
//			if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
//				doExit();
//				return true;	
//			}
//		}
//		return super.onKeyDown(keyCode, event);
//	}
//	
//	public void searchAddress(String searchStrDst){
//		final String[] arrStr = searchStrDst.split(" ");// 通过空格间断字符串
//		bSearch = true;
//		showDialog();
//		new Thread(new Runnable() {
//
//			@Override
//			public void run() {
//				// TODO Auto-generated method stub
//				if (arrStr.length > 1)
//					mMkSearch.poiSearchInCity(arrStr[0], arrStr[1]);
//				else if (arrStr.length == 1) // 当前城市搜索
//					mMkSearch.poiSearchInCity(null, arrStr[0]);
//			}
//		}).start();
//	}
//}
