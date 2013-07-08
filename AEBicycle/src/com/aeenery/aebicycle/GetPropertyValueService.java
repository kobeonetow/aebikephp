package com.aeenery.aebicycle;

import com.aeenery.aebicycle.battery.BluetoothService;
import com.aeenery.aebicycle.battery.BluetoothService.MyBinder;
import com.aeenery.aebicycle.bms.BMSController;
import com.aeenery.aebicycle.entry.UtilFunction;
import com.aeenery.aebicycle.map.MapActivity;
import com.aeenery.aebicycle.map.MapActivity.MyLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.LocationData;
import com.baidu.mapapi.map.MyLocationOverlay;
import com.baidu.mapapi.utils.DistanceUtil;
import com.baidu.platform.comapi.basestruct.GeoPoint;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class GetPropertyValueService extends Service{
	/**
	 * Debug settings
	 */
	private static final boolean D = true;
	private static final String TAG = "GetPropertyValueService";
	private final IBinder mBinder = new MyBinder();
	
	//定位系统
	protected LocationClient mLocationClient = null;
	protected MyLocationOverlay myLocationOverlay = null;
	protected BDLocationListener myListener;
	protected LocationData myLocNew = null;
	protected LocationData myLocOld = null;
	
	//Temp
	double distancePlus;
	double speedAverage;
	double speedCount= 0;
	double speedSum;
	
	@Override
	public IBinder onBind(Intent intent) {
		if (D)
			Log.e(TAG, "+++ ON BIND +++");
		return mBinder;
	}
	
	public class MyBinder extends Binder {
		public GetPropertyValueService getService() {
			return GetPropertyValueService.this;
		}
	}
	
	@Override
	public void onCreate() {
		if (D)
			Log.e(TAG, "+++ ON CREATE +++");
		super.onCreate();
		initGPSLoc();
	}
	

	@Override
	public void onDestroy() {
		if (D)
			Log.e(TAG, "--- ON DESTROY ---");
		if(mLocationClient.isStarted()){
			mLocationClient.stop();
			mLocationClient.unRegisterLocationListener(myListener);
		}
		super.onDestroy();
	}

	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		requestLocation();
		return START_NOT_STICKY; // run until explicitly stopped.
	}
	
	//注册定位器
	private void initGPSLoc(){
		if(mLocationClient == null)
			mLocationClient = new LocationClient(getApplicationContext());
		if(myListener == null)
			 myListener = new MyLocationListener();
		mLocationClient.registerLocationListener( myListener );    
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);
		option.setAddrType("all");//返回的定位结果包含地址信息
		option.setCoorType("bd09ll");//返回的定位结果是百度经纬度,默认值gcj02
		option.setScanSpan(2500);//设置发起定位请求的间隔时间为5000ms
		option.disableCache(true);//禁止启用缓存定位
		mLocationClient.setLocOption(option);
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
	
	protected synchronized void calculateDistanceParams(BDLocation location){
		if(myLocNew == null)
			myLocNew = new LocationData();
		myLocNew.latitude = location.getLatitude();
		myLocNew.longitude = location.getLongitude();
		myLocNew.accuracy = location.getRadius();
		myLocNew.direction = location.getDerect();
		myLocNew.speed = location.getSpeed();
		
		if(myLocOld != null){
//			GeoPoint gp1 = new GeoPoint((int)(myLocNew.latitude*1E6),(int)(myLocNew.longitude*1E6));
//			GeoPoint gp2 = new GeoPoint((int)(myLocOld.latitude*1E6),(int)(myLocOld.longitude*1E6));
//			double distance = DistanceUtil.getDistance(gp1, gp2);
			distancePlus += UtilFunction.GetShortDistance(myLocNew.longitude, myLocNew.latitude, myLocOld.longitude, myLocOld.latitude)/1000.0;
			
			if(myLocNew.speed > 2){
				speedCount++;
				speedSum += myLocNew.speed;
				speedAverage = speedSum/speedCount;
			}
			
			Toast.makeText(this, "Distance is:"+distancePlus+"\n精准:"+myLocNew.accuracy+"速度:"+speedAverage, Toast.LENGTH_SHORT).show();
			
			
		}
		copyToOldLocation(myLocNew);
	}
	
	protected void copyToOldLocation(LocationData data){
		if(myLocOld == null)
			myLocOld = new LocationData();
		myLocOld.latitude = data.latitude;
		myLocOld.longitude = data.longitude;
		myLocOld.accuracy = data.accuracy;
		myLocOld.direction = data.direction;
		myLocOld.speed = data.speed;
	}

	class MyLocationListener implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null)
				return ;
			calculateDistanceParams(location);
        }
		@Override
		public void onReceivePoi(BDLocation poiLocation) {
				if (poiLocation == null){
					return ;
				}
			}
	}
}
