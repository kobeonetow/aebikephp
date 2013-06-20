package com.aeenery.aebicycle.map;

import com.aeenery.aebicycle.AEApplication;
import com.aeenery.aebicycle.R;
import com.aeenery.aebicycle.entry.BicycleUtil;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.LocationData;
import com.baidu.mapapi.map.MKEvent;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationOverlay;
import com.baidu.mapapi.map.PoiOverlay;
import com.baidu.mapapi.search.MKAddrInfo;
import com.baidu.mapapi.search.MKBusLineResult;
import com.baidu.mapapi.search.MKDrivingRouteResult;
import com.baidu.mapapi.search.MKPoiInfo;
import com.baidu.mapapi.search.MKPoiResult;
import com.baidu.mapapi.search.MKSearch;
import com.baidu.mapapi.search.MKSearchListener;
import com.baidu.mapapi.search.MKSuggestionResult;
import com.baidu.mapapi.search.MKTransitRouteResult;
import com.baidu.mapapi.search.MKWalkingRouteResult;
import com.baidu.platform.comapi.basestruct.GeoPoint;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class MapActivity extends Activity {

	//必须
	private AEApplication app = null;
	protected BMapManager mBMapMan = null;
	protected MapView mMapView = null;
	
	//To save the my location cache
	protected final double defaultLat = 22.770096;
	protected final double defaultLong = 113.357397;
	protected SharedPreferences sp = null;
	
	//查找类
	protected MKSearch mMKSearch = null;
	
	//ImageButtons,Edittext
	protected ImageButton ibMapMyLoc,ibMapSearch;
	protected EditText etMapDst;
	
	//My Location
	LocationData myLocPoint = null;
	
	//RequestCode
	private int requestCode = 0;
	
	//定位系统
	public LocationClient mLocationClient = null;
	MyLocationOverlay myLocationOverlay = null;
	public BDLocationListener myListener = new MyLocationListener();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		app = (AEApplication) this.getApplication();
		mBMapMan = app.getBMapManager();
		sp = this.getSharedPreferences("mylocation", MODE_PRIVATE);
		
		//注意：请在试用setContentView前初始化BMapManager对象，否则会报错
		setContentView(R.layout.activity_map);
		mMapView=(MapView)findViewById(R.id.bmapsView);
		mMapView.setBuiltInZoomControls(true);
		mMapView.setLongClickable(true);
		mMapView.setOnLongClickListener(new ButtonLongClickListener());
		
		//设置启用内置的缩放控件
		MapController mMapController=mMapView.getController();
		// 得到mMapView的控制权,可以用它控制和驱动平移和缩放
		GeoPoint point =new GeoPoint((int)(22.770096* 1E6),(int)(113.357397* 1E6));
		//用给定的经纬度构造一个GeoPoint，单位是微度 (度 * 1E6)
		mMapController.setCenter(point);//设置地图中心点
		mMapController.setZoom(12);//设置地图zoom级别

		//Load last location
		loadLastMyLocation();
		
		//Register buttons
		registerButtons();
		
		//注册定位类
		initGPSLoc();
		requestLocation();
		
		//初始化搜索类
		mMKSearch = new MKSearch();
		mMKSearch.init(mBMapMan, new AESearchListener());
		
		
		//See which activity calls the api and works differently
		this.requestCode = this.getIntent().getIntExtra("requestCode",0);
		reactToDifferentActivity();
	}

	/**
	 * Load last my location if any
	 */
	private void loadLastMyLocation() {
		if(myLocPoint == null){
			myLocPoint = new LocationData();
		}
		myLocPoint.latitude = sp.getFloat("latitude", (float) defaultLat);
		myLocPoint.longitude = sp.getFloat("longtitude", (float) defaultLong);
	}

	/**
	 * React differently according to require functions and return result
	 */
	private void reactToDifferentActivity() {
		switch(requestCode){
		case BicycleUtil.RequestMapView:
			break;
		default:
			break;
		}
	}
	
	@Override
	public void finish(){
		switch(requestCode){
		case BicycleUtil.RequestMapView:
			this.setResult(BicycleUtil.ResultMapView);
			break;
		default:
			break;
		}
		super.finish();
	}

	//注册定位器
	private void initGPSLoc(){
		if(mLocationClient == null)
			mLocationClient = new LocationClient(getApplicationContext());     
		mLocationClient.registerLocationListener( myListener );    
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);
		option.setAddrType("all");//返回的定位结果包含地址信息
		option.setCoorType("bd09ll");//返回的定位结果是百度经纬度,默认值gcj02
		option.setScanSpan(5000);//设置发起定位请求的间隔时间为5000ms
		option.disableCache(true);//禁止启用缓存定位
		option.setPoiNumber(5);	//最多返回POI个数	
		option.setPoiDistance(1000); //poi查询距离		
		option.setPoiExtraInfo(true); //是否需要POI的电话和地址等详细信息		
		mLocationClient.setLocOption(option);
		
		myLocationOverlay = new MyLocationOverlay(mMapView);
		myLocationOverlay.enableCompass();
		mMapView.getOverlays().add(myLocationOverlay);
	}
	
	/**
	 * Request my location information
	 */
	private void requestLocation(){
		if (mLocationClient != null && mLocationClient.isStarted())
			mLocationClient.requestLocation();
		else if(mLocationClient != null && !mLocationClient.isStarted()){
			mLocationClient.start();
			mLocationClient.requestLocation();
		}
		else
			Log.d("LocSDK3", "locClient is null or not started");
	}
	
	/**
	 * Animate to my location geopoint
	 */
	protected void setMyLocationToCenter(){
		if(myLocPoint != null){
			myLocationOverlay.setData(myLocPoint);
			mMapView.refresh();
			mMapView.getController().animateTo(new GeoPoint((int)(myLocPoint.latitude*1E6),(int)(myLocPoint.longitude*1E6)));
		}else{
			Toast.makeText(this, "获取现在位置失败", Toast.LENGTH_SHORT).show();
		}
	}
	
	/**
	 * Register image buttons
	 */
	private void registerButtons(){
		ibMapMyLoc = (ImageButton)findViewById(R.id.ibMapMyLoc);
		ibMapSearch = (ImageButton)findViewById(R.id.ibMapSearch);
		etMapDst = (EditText)findViewById(R.id.etMapDst);
		
		ibMapMyLoc.setOnClickListener(new ButtonClickListener());
		ibMapSearch.setOnClickListener(new ButtonClickListener());
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.map, menu);
		return true;
	}

	@Override
	protected void onDestroy(){
		mLocationClient.unRegisterLocationListener(myListener);	
		if(mLocationClient != null && mLocationClient.isStarted())
			mLocationClient.stop();
	    mMapView.destroy();
	    if(mBMapMan!=null){
	        mBMapMan=null;
	    }
	    super.onDestroy();
	}
	@Override
	protected void onPause(){
	        mMapView.onPause();
	        if(mBMapMan!=null){
	                mBMapMan.stop();
	        }
	        super.onPause();
	}
	@Override
	protected void onResume(){
	        mMapView.onResume();
	        if(mBMapMan!=null){
	                mBMapMan.start();
	        }
	        super.onResume();
	}
	
	public class MyLocationListener implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null)
				return ;
			if(myLocPoint == null)
				myLocPoint = new LocationData();
			myLocPoint.latitude = location.getLatitude();
			myLocPoint.longitude = location.getLongitude();
			myLocPoint.accuracy = location.getRadius();
			myLocPoint.direction = location.getDerect();
			myLocPoint.speed = location.getSpeed();
			
			Editor edit = sp.edit();
			edit.putFloat("latitude", (float) location.getLatitude());
			edit.putFloat("longtitude", (float) location.getLongitude());
			edit.commit();
        }
		@Override
		public void onReceivePoi(BDLocation poiLocation) {
				if (poiLocation == null){
					return ;
				}
			}
	}
	
	public class AESearchListener implements MKSearchListener {

		@Override
		public void onGetAddrResult(MKAddrInfo arg0, int arg1) {
			// TODO Auto-generated method stub
			
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
		public void onGetPoiResult(MKPoiResult result, int type, int error) {
			// 错误号可参考MKEvent中的定义
			if ( error == MKEvent.ERROR_RESULT_NOT_FOUND){
				Toast.makeText(MapActivity.this, "抱歉，未找到结果",Toast.LENGTH_LONG).show();
				return;
			}else if (error != 0 || result == null) {
				Toast.makeText(MapActivity.this, "搜索出错啦..", Toast.LENGTH_LONG).show();
				return;
			}

			// 将poi结果显示到地图上
			PoiOverlay poiOverlay = new PoiOverlay(MapActivity.this, mMapView);
			poiOverlay.setData(result.getAllPoi());
//			mMapView.getOverlays().clear();
			mMapView.getOverlays().add(poiOverlay);
			mMapView.refresh();

			//当ePoiType为2（公交线路）或4（地铁线路）时， poi坐标为空
			for(MKPoiInfo info : result.getAllPoi() ){
				if(info.pt != null){
					mMapView.getController().animateTo(info.pt);
					break;
				}
			}
		}

		@Override
		public void onGetSuggestionResult(final MKSuggestionResult result, int error) {
			if (error!= 0 || result == null) {
				 Toast.makeText(MapActivity.this, "抱歉，未找到结果", Toast.LENGTH_LONG).show(); 
				 return;
			 }
			int nSize = result.getSuggestionNum();
	        final String[] mStrSuggestions = new String[nSize];

	        if(nSize == 1){
	        	mMKSearch.poiSearchInCity(result.getSuggestion(0).city ,result.getSuggestion(0).key);
	        }else if(nSize == 0){
	        	mMKSearch.poiSearchInCity(null,etMapDst.getText().toString());
	        }
	        
	        for (int i = 0; i <nSize; i++){
	        	mStrSuggestions[i] = result.getSuggestion(i).city + result.getSuggestion(i).key;
	        }

	        
	        new AlertDialog.Builder(MapActivity.this)
            .setTitle("请选择地点")
            .setItems(mStrSuggestions, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    /* User clicked so do some stuff */
                	mMKSearch.poiSearchInCity(result.getSuggestion(which).city ,result.getSuggestion(which).key);
                }
            })
            .create().show();

		}

		@Override
		public void onGetTransitRouteResult(MKTransitRouteResult arg0, int arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onGetWalkingRouteResult(MKWalkingRouteResult arg0, int arg1) {
			// TODO Auto-generated method stub
			
		}

	}
	
	public class ButtonClickListener implements OnClickListener{
		@Override
		public void onClick(View v) {
			switch(v.getId()){
			case R.id.ibMapMyLoc:
				setMyLocationToCenter();
				break;
			case R.id.ibMapSearch:
				String searchStr = etMapDst.getText().toString();
				final String[] arrStr = searchStr.split(" ");// 通过空格间断字符串
				if (arrStr.length > 1)
					mMKSearch.poiSearchInCity(arrStr[0], arrStr[1]);
				else if (arrStr.length == 1) // 当前城市搜索
					mMKSearch.suggestionSearch(arrStr[0]);
//				mMKSearch.poiSearchNearBy(searchStr, new GeoPoint((int) (myLocPoint.latitude * 1E6), (int) (myLocPoint.longitude * 1E6)), 50000);
				break;
			default:
				Log.i("MapActivity","Unknow button clicked");
			}
		}
		
	}
	
	public class ButtonLongClickListener implements OnLongClickListener{
		@Override
		public boolean onLongClick(View v) {
			switch(v.getId()){
			case R.id.bmapsView:
				Toast.makeText(MapActivity.this, "long touch", Toast.LENGTH_SHORT).show();
				break;
			default:
				Toast.makeText(MapActivity.this, "long touch but not view detected", Toast.LENGTH_SHORT).show();
				break;
			}
			return true;
		}
	}
	
	
}



