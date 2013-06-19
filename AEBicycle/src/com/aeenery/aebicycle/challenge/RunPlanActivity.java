//package com.aeenery.aebicycle.challenge;
//
//import android.app.AlertDialog;
//import android.app.ProgressDialog;
//import android.content.DialogInterface;
//import android.location.Location;
//import android.os.Bundle;
//import android.view.KeyEvent;
//import android.view.Menu;
//
//import com.aeenery.aebicycle.R;
//import com.aeenery.aebicycle.entry.BicycleUtil;
//import com.baidu.mapapi.BMapManager;
//import com.baidu.mapapi.LocationListener;
//import com.baidu.mapapi.MKAddrInfo;
//import com.baidu.mapapi.MKBusLineResult;
//import com.baidu.mapapi.MKDrivingRouteResult;
//import com.baidu.mapapi.MKLocationManager;
//import com.baidu.mapapi.MKPoiResult;
//import com.baidu.mapapi.MKSearch;
//import com.baidu.mapapi.MKSearchListener;
//import com.baidu.mapapi.MKSuggestionResult;
//import com.baidu.mapapi.MKTransitRouteResult;
//import com.baidu.mapapi.MKWalkingRouteResult;
//import com.baidu.mapapi.MapActivity;
//import com.baidu.mapapi.MapController;
//import com.baidu.mapapi.MapView;
//import com.baidu.mapapi.MyLocationOverlay;
//
//public class RunPlanActivity extends MapActivity {
//
//	private BMapManager mBMapMan = null;
//	private MapView mMapView;
//	private MapController mMapController;
//	private MyLocationOverlay myLocOverlay;
//	private MKSearch mMkSearch;
//	private ProgressDialog pd;
//	
//	
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_run_plan);
//		init();
//	}
//
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.activity_run_plan, menu);
//		return true;
//	}
//	
//
//	private void init() {
//		//初始化百度map 类
//		mBMapMan = new BMapManager(getApplication());
//		mBMapMan.init(BicycleUtil.mapKey, null);
//		super.initMapActivity(mBMapMan);
//		mMapView = (MapView)findViewById(R.id.selection_bmapView);
//		mMapView.setBuiltInZoomControls(true);//设置内置缩放控件
//		mMapView.setDrawOverlayWhenZooming(true);
//		mMapController = mMapView.getController();
//		mMkSearch = new MKSearch();
//		mMkSearch.init(mBMapMan, new RouteMapSearchListener());
//		
//		
//		// 初始化Location模块
//		MKLocationManager myLocMgr = mBMapMan.getLocationManager();
//		
//		//通过enableProvider和disableProvider方法，选择定位的Provider
//		myLocMgr.enableProvider(MKLocationManager.MK_NETWORK_PROVIDER);
//		myLocMgr.enableProvider(MKLocationManager.MK_GPS_PROVIDER);
//		myLocOverlay = new MyLocationOverlay(this, mMapView);
//		myLocOverlay.enableMyLocation();//用户定位
//		myLocOverlay.enableCompass();//用户指南针
//		mMapView.getOverlays().add(myLocOverlay);
//		//1 runOnFirstFix，在第一次获取坐标时就启动一个线程。
//		//2 mMapController.animateTo(myLocationOverlay.getMyLocation()); 
//		//  这个线程的作用是把地图中心移动到用户的位置。
//		{
//			myLocOverlay.runOnFirstFix(new Runnable() {
//				@Override
//				public void run() {
//					// 初始化路径选择源
//					{
//						mMapController.animateTo(myLocOverlay.getMyLocation());
//					}
//				}
//			});
//
//			// 定位
//			myLocMgr.requestLocationUpdates(new LocationListener() {
//				@Override
//				public void onLocationChanged(Location arg0) {
//					{
//						mMapController.animateTo(myLocOverlay.getMyLocation());
//					}
//				}
//			});
//
//		}
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
//		}
//
//
//		@Override
//		public void onGetBusDetailResult(MKBusLineResult arg0, int arg1) {
//			// TODO Auto-generated method stub
//			
//		}
//		@Override
//		public void onGetDrivingRouteResult(MKDrivingRouteResult arg0, int arg1) {
//			// TODO Auto-generated method stub
//			
//		}
//		@Override
//		public void onGetPoiDetailSearchResult(int arg0, int arg1) {
//			// TODO Auto-generated method stub
//			
//		}
//		@Override
//		public void onGetRGCShareUrlResult(String arg0, int arg1) {
//			// TODO Auto-generated method stub
//			
//		}
//		@Override
//		public void onGetSuggestionResult(MKSuggestionResult arg0, int arg1) {
//			// TODO Auto-generated method stub
//			
//		}
//		@Override
//		public void onGetTransitRouteResult(MKTransitRouteResult arg0, int arg1) {
//			// TODO Auto-generated method stub
//			
//		}
//
//
//		@Override
//		public void onGetPoiResult(MKPoiResult arg0, int arg1, int arg2) {
//			// TODO Auto-generated method stub
//			
//		}
//
//
//		@Override
//		public void onGetWalkingRouteResult(MKWalkingRouteResult arg0, int arg1) {
//			// TODO Auto-generated method stub
//			
//		}
//		
//	}
//	
//	protected void doExit(){
//		new AlertDialog.Builder(RunPlanActivity.this)
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
//	public void showDialog(){
//		pd = ProgressDialog.show(RunPlanActivity.this, "提示", "数据加载中,请稍后...");
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
//		pd = ProgressDialog.show(RunPlanActivity.this, title, msg);
//	}
//	
//	public void closeDialog(){
//		pd.cancel();
//	}
//	
//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		// TODO Auto-generated method stub
//		{
//			if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
//				doExit();
//				return true;	
//			}
//		}
//		return super.onKeyDown(keyCode, event);
//	}
//	
//}
