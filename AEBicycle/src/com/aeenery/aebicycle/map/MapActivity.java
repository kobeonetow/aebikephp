package com.aeenery.aebicycle.map;

import java.io.IOException;
import java.util.List;

import com.aeenery.aebicycle.AEApplication;
import com.aeenery.aebicycle.R;
import com.aeenery.aebicycle.entry.BicycleUtil;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.ItemizedOverlay;
import com.baidu.mapapi.map.LocationData;
import com.baidu.mapapi.map.MKEvent;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationOverlay;
import com.baidu.mapapi.map.OverlayItem;
import com.baidu.mapapi.map.PoiOverlay;
import com.baidu.mapapi.map.PopupClickListener;
import com.baidu.mapapi.map.PopupOverlay;
import com.baidu.mapapi.map.RouteOverlay;
import com.baidu.mapapi.search.MKAddrInfo;
import com.baidu.mapapi.search.MKBusLineResult;
import com.baidu.mapapi.search.MKDrivingRouteResult;
import com.baidu.mapapi.search.MKPlanNode;
import com.baidu.mapapi.search.MKPoiInfo;
import com.baidu.mapapi.search.MKPoiResult;
import com.baidu.mapapi.search.MKRoute;
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
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
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
	protected ImageButton ibMapMyLoc,ibMapSearch, ibMarker, ibForwardResult, ibMapRoute;
	protected EditText etMapDst;
	protected TextView tvMapHint;
	protected boolean markerOn = false;
	protected String markerHint = "请选择一个位置标记";
	
	//My Location
	protected LocationData myLocPoint = null;
	protected LocationData startLoc, endLoc = null;
	protected MKRoute route = null;
	
	//RequestCode
	private int requestCode = BicycleUtil.RequestMapView;
	protected Intent  retData = null;
	
	//定位系统
	protected LocationClient mLocationClient = null;
	protected MyLocationOverlay myLocationOverlay = null;
	protected BDLocationListener myListener = new MyLocationListener();
	
	//interest overlay
	protected ItemOverlay itemizedOverlay = null;
	protected GeoPoint popUpPoint = null;
	
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
		mMapView.setOnTouchListener(new MapViewTouchListener());
		
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
		
		//初始化itemizedOverlay
		Drawable marker = getResources().getDrawable(R.drawable.icon_marker);
		itemizedOverlay = new ItemOverlay(marker,mMapView);
		mMapView.getOverlays().add(itemizedOverlay);
		
		
		//See which activity calls the api and works differently
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
		Intent it = this.getIntent();
		this.requestCode = it.getIntExtra("requestCode",BicycleUtil.RequestMapView);
		switch(requestCode){
		case BicycleUtil.RequestMapView: //Normal view
			if(ibForwardResult.getVisibility() != View.GONE)
				ibForwardResult.setVisibility(View.GONE);
			break;
		case BicycleUtil.RequestSetStartEndLoc: //View to select start and finish location, need return value
			//Enable the set route button to return start end location information
			ibForwardResult.setVisibility(View.VISIBLE);
			preLoadStartEndLocIfAny();
			break;
		default:
			break;
		}
	}
	
	/**
	 * Load the information given by caller
	 * This is for start and end position
	 * Set in bundle
	 * "startLocLat/Long && endLocLat/Long"
	 */
	private void preLoadStartEndLocIfAny() {
		Intent data = getIntent();
		if(data == null)
			return;
		Bundle b = data.getExtras();
		if(b == null)
			return;
		if(b.getDouble("startLocLat", 0) != 0){
			if(startLoc == null)
				startLoc = new LocationData();
			startLoc.latitude = b.getDouble("startLocLat");
			startLoc.longitude = b.getDouble("startLocLong");
			itemizedOverlay.addItem(getMarkerItem(1, startLoc.latitude, startLoc.longitude, "开始标记", ""));
		}
		if(b.getDouble("endLocLat",0) != 0){
			if(endLoc == null)
				endLoc  = new LocationData();
			endLoc.latitude = b.getDouble("endLocLat");
			endLoc.longitude = b.getDouble("endLocLong");
			itemizedOverlay.addItem(getMarkerItem(2, endLoc.latitude, endLoc.longitude, "结束标记", ""));
		}
		mMapView.refresh();
		
//		if(startLoc != null)
//			mMapController.animateTo(new GeoPoint((int)(startLoc.latitude),(int)(startLoc.longitude)));
	}

	/**
	 * Override the finish event so that can return different result code
	 * and data to caller activity
	 */
	@Override
	public void finish(){
		switch(requestCode){
		case BicycleUtil.RequestMapView:
			this.setResult(BicycleUtil.ResultMapView);
			break;
		case BicycleUtil.RequestSetStartEndLoc:
			MapActivity.this.setResult(BicycleUtil.ResultSetStartEndLoc, retData);
			break;
		default:
			break;
		}
		super.finish();
	}

	/**
	 * Back buttom pressed
	 */
	@Override
	public void onBackPressed() {
	   this.setResult(RESULT_CANCELED);
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
	 * Animate to my location geo point
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
		tvMapHint  = (TextView)findViewById(R.id.tvMapHint);
		ibMarker = (ImageButton)findViewById(R.id.ibMarker);
		ibForwardResult = (ImageButton)findViewById(R.id.ibForwardResult);
		ibMapRoute = (ImageButton)findViewById(R.id.ibMapRoute);
		
		ibMapMyLoc.setOnClickListener(new ButtonClickListener());
		ibMapSearch.setOnClickListener(new ButtonClickListener());
		ibMarker.setOnClickListener(new ButtonClickListener());
		ibForwardResult.setOnClickListener(new ButtonClickListener());
		ibMapRoute.setOnClickListener(new ButtonClickListener());
		
	}
	
	/**
	 * Get the route from start point to end point
	 * require startLoc and endLoc to be set.
	 * @param planNum
	 * @param routeNum
	 */
	protected void searchRoute(){
		MKPlanNode startNode = new MKPlanNode();
		MKPlanNode endNode = new MKPlanNode();
		startNode.pt = new GeoPoint((int)(startLoc.latitude),(int)(startLoc.longitude));
		endNode.pt = new GeoPoint((int)(endLoc.latitude),(int)(endLoc.longitude));
		mMKSearch.walkingSearch(null, startNode, null, endNode);
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
			Toast.makeText(MapActivity.this, location.getSpeed()+"", Toast.LENGTH_LONG).show();
			
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
		public void onGetWalkingRouteResult(MKWalkingRouteResult result, int error) {
			if (result == null) {  
                return;  
			}  
			RouteOverlay routeOverlay = new RouteOverlay(MapActivity.this, mMapView);
			Log.i("MapActivity","Plans found:"+result.getNumPlan() + " and Routes found for plan 1 is "+result.getPlan(0).getNumRoutes());
	        route = result.getPlan(0).getRoute(0);
			routeOverlay.setData(route);  
	        mMapView.getOverlays().add(routeOverlay);  
	        mMapView.refresh();  
	        
		}

	}
	
	public class ButtonClickListener implements OnClickListener{
		@Override
		public void onClick(View v) {
			switch(v.getId()){
			case R.id.ibMapMyLoc: //Set the view center to my location
				setMyLocationToCenter();
				break;
			case R.id.ibMapSearch: //Search a giving location string
				String searchStr = etMapDst.getText().toString();
				final String[] arrStr = searchStr.split(" ");// 通过空格间断字符串
				if (arrStr.length > 1)
					mMKSearch.poiSearchInCity(arrStr[0], arrStr[1]);
				else if (arrStr.length == 1) // 当前城市搜索
					mMKSearch.suggestionSearch(arrStr[0]);
				break;
			case R.id.ibMarker: //Put marker state, make the next click to put marker
				tvMapHint.setText(markerHint);
				if(tvMapHint.getVisibility() == View.INVISIBLE)
					tvMapHint.setVisibility(View.VISIBLE);
				markerOn = true;
				break;
			case R.id.ibForwardResult: //Forward result to plan activity with giving result code
				Bundle b = new Bundle();
				if(startLoc != null){
					b.putDouble("startLocLat", startLoc.latitude);
					b.putDouble("startLocLong", startLoc.longitude);
				}
				if(endLoc != null){
					b.putDouble("endLocLat", endLoc.latitude);
					b.putDouble("endLocLong", endLoc.longitude);
				}
				if(route != null){
					b.putInt("routeDistance", route.getDistance());
				}
				if(retData == null)
					retData = new Intent();
				retData.putExtras(b);
				MapActivity.this.finish();
				break;
			case R.id.ibMapRoute://Search the route from start point to end point
				if(startLoc == null || endLoc == null){
					Toast.makeText(MapActivity.this, "请先设置起点和终点", Toast.LENGTH_LONG).show();
				}else{
					searchRoute();
				}
				break;
			default:
				Log.i("MapActivity","Unknow button clicked");
			}
		}
		
	}
	
	public class MapViewTouchListener implements OnTouchListener{
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if(!markerOn){
				return false;
			}
			float x = event.getX();
			float y = event.getY();
			GeoPoint point = mMapView.getProjection().fromPixels((int)x, (int)y);
			itemizedOverlay.addItem(getMarkerItem(0,point.getLatitudeE6(),point.getLongitudeE6(),"自拟定添加",x+","+y));
			if(tvMapHint.getVisibility() == View.VISIBLE)
				tvMapHint.setVisibility(View.INVISIBLE);
			markerOn = false;
			mMapView.refresh();
			return true;
		}
	
	}
	
	/**
	 * Use to create overlay item, 0 for normal,
	 * 1 for start, 2 for end
	 * @return
	 */
	private OverlayItem getMarkerItem(int type, double latitude, double longtitude, String title, String content){
		OverlayItem item = null;
		if(type == 1){
			item = new OverlayItem(new GeoPoint((int)(latitude),(int)(longtitude)), title, content);
			Drawable start = MapActivity.this.getResources().getDrawable(R.drawable.letter_s);
			item.setMarker(start);
			return item;
		}else if(type == 2){
			item = new OverlayItem(new GeoPoint((int)(latitude),(int)(longtitude)), title, content);
			Drawable end = MapActivity.this.getResources().getDrawable(R.drawable.letter_e);
			item.setMarker(end);
			return item;
		}else{
			item = new OverlayItem(new GeoPoint((int)(latitude),(int)(longtitude)), title, content);
			return item;
		}
	}
	
	public class ItemOverlay extends ItemizedOverlay<OverlayItem>{
		//Popup overlay
		protected PopupOverlay popOverlay = null;
		protected Bitmap[] popMapUp = new Bitmap[3];
		protected int itemIndex = -1;
		
		public ItemOverlay(Drawable marker, MapView mapView) {
			super(marker, mapView);
			
			//Initialise popOverlay
			popOverlay = new PopupOverlay(mMapView, new PopupCustomClickListener());
			
			Options opt = new Options();
			opt.inScaled = true;
			popMapUp[0] = BitmapFactory.decodeResource(getResources(), R.drawable.icon_start,opt);
			popMapUp[1] = BitmapFactory.decodeResource(getResources(), R.drawable.icon_end_triangle,opt);  
//			popMapUp[2] = BitmapFactory.decodeResource(getResources(), R.drawable.icon_direct,opt);
			popMapUp[2] = BitmapFactory.decodeResource(getResources(), R.drawable.icon_close,opt);
		}
		
		protected boolean onTap(int index) {
			popOverlay.showPopup(popMapUp, getItem(index).getPoint(), 45);
			popUpPoint =  getItem(index).getPoint();
			itemIndex = index;
			return true;
			
		}
		
		public boolean onTap(GeoPoint pt, MapView mapView){
			if (popOverlay != null){
				popOverlay.hidePop();
			}
			super.onTap(pt,mapView);
			return false;
			
		}
		
		public class PopupCustomClickListener implements PopupClickListener{
			OverlayItem item = null;
			LocationData preData = null;
			@Override
			public void onClickedPopup(int index) {
				 switch(index){
				 case 0:
					 //Save previous starting point
					 if(startLoc != null){
						 preData = new LocationData();
						 preData.latitude = startLoc.latitude;
						 preData.longitude = startLoc.longitude;
					 }
					 
					 //Replace the marker to start marker and save current starting point
					 if(startLoc == null)
						 startLoc = new LocationData();
					 startLoc.latitude = getItem(itemIndex).getPoint().getLatitudeE6();
					 startLoc.longitude = getItem(itemIndex).getPoint().getLongitudeE6();
					 item = getMarkerItem(1, startLoc.latitude, startLoc.longitude, "开始标记","");
					 itemizedOverlay.removeItem(getItem(itemIndex));
					 itemizedOverlay.addItem(item);
					 item = null;
					 
					 //remove the previous starting point
					 if(preData != null){
						 int startIndex = getItemIdex(new GeoPoint((int)(preData.latitude), (int)(preData.longitude)));
						 if(startIndex != -1){
							 changeToNormalMarker(startIndex);
						 }
						 preData = null;
					 }
					 
					 popOverlay.hidePop();
					 break;
				 case 1:
					 //Save previous end location
					 if(endLoc != null){
						 preData = new LocationData();
						 preData.latitude = endLoc.latitude;
						 preData.longitude = endLoc.longitude;
					 }
					 
					 //set new end location
					 if(endLoc == null)
						 endLoc = new LocationData();
					 endLoc.latitude = getItem(itemIndex).getPoint().getLatitudeE6();
					 endLoc.longitude = getItem(itemIndex).getPoint().getLongitudeE6();
					 item = getMarkerItem(2, endLoc.latitude, endLoc.longitude, "结束标记","");
					 itemizedOverlay.removeItem(getItem(itemIndex));
					 itemizedOverlay.addItem(item);
					 item = null;
					 
					 //remove old end location
					 if(preData != null){
						 int endIndex = getItemIdex(new GeoPoint((int)(preData.latitude), (int)(preData.longitude)));
						 if(endIndex != -1){
							 changeToNormalMarker(endIndex);
						 }
						 preData = null;
					 }
					 
					 //Update route
					 searchRoute();
					 
					 popOverlay.hidePop();
					 break;
				 case 2:
					 GeoPoint point = getItem(itemIndex).getPoint();
					 itemizedOverlay.removeItem(getItem(itemIndex));
					 if(startLoc != null && point.getLatitudeE6() == startLoc.latitude && point.getLongitudeE6() == startLoc.longitude)
						 startLoc = null;
					 if(endLoc != null && point.getLatitudeE6() == endLoc.latitude && point.getLongitudeE6() == endLoc.longitude)
						 endLoc = null;
					 popOverlay.hidePop();
					 break;
				 }
				 mMapView.refresh();
			}
			
			private void changeToNormalMarker(int index) {
				OverlayItem itemb = getItem(index);
				OverlayItem itemc = getMarkerItem(0, itemb.getPoint().getLatitudeE6(), itemb.getPoint().getLongitudeE6(), "自拟定标记","");
				itemizedOverlay.removeItem(itemb);
				mMapView.refresh();
				itemizedOverlay.addItem(itemc);
				mMapView.refresh();
			}

			public int getItemIdex(GeoPoint point){
				List<OverlayItem> items = itemizedOverlay.getAllItem();
				for(int i = 0; i<items.size();i++){
					if(itemizedOverlay.getItem(i) == null)
						continue;
					OverlayItem itema = itemizedOverlay.getItem(i);
					if(point.getLatitudeE6() == itema.getPoint().getLatitudeE6() && point.getLongitudeE6() == itema.getPoint().getLongitudeE6())
						return i;
				}
				return -1;
			}
		}
		
	}
}



