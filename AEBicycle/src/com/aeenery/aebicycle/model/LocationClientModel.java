package com.aeenery.aebicycle.model;

import android.app.Activity;
import android.util.Log;

import com.aeenery.aebicycle.map.MyLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.BDNotifyListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

public class LocationClientModel {
	public LocationClient mLocClient = null;
	public BDLocationListener myListener = new MyLocationListener();
	
	public void onCreate() {
	    mLocClient = new LocationClient(new Activity());     //����LocationClient��
	    mLocClient.registerLocationListener( myListener );    //ע�������
	    
	    LocationClientOption option = new LocationClientOption();
	    option.setOpenGps(true);
	    option.setAddrType("detail");
	    option.setCoorType("gcj02");
	    option.setScanSpan(5000);
	    option.disableCache(true);//��ֹ���û��涨λ
	    option.setPoiNumber(5);	//��෵��POI����	
	    option.setPoiDistance(1000); //poi��ѯ����		
	    option.setPoiExtraInfo(true); //�Ƿ���ҪPOI�ĵ绰�͵�ַ����ϸ��Ϣ		
	    mLocClient.setLocOption(option);
	    
	    if (mLocClient != null && mLocClient.isStarted())
	    	mLocClient.requestLocation();
	    else 
	    	Log.d("LocSDK_2.0_Demo1", "locClient is null or not started");
	    
	    if (mLocClient != null && mLocClient.isStarted())
	    	mLocClient.requestPoi();
	    
	  //λ��������ش���
	    NotifyLister mNotifyer = new NotifyLister();
	    mNotifyer.SetNotifyLocation(42.03249652949337,113.3129895882556,3000,"gps");//4��������Ҫλ�����ѵĵ����꣬���庬������Ϊ��γ�ȣ����ȣ����뷶Χ�����ϵ����(gcj02,gps,bd09,bd09ll)
	    mLocClient.registerNotify(mNotifyer);
	    //ע��λ�����Ѽ����¼��󣬿���ͨ��SetNotifyLocation ���޸�λ���������ã��޸ĺ�������Ч��
	}
	
	//BDNotifyListnerʵ��
	public class NotifyLister extends BDNotifyListener{
	    @Override
		public void onNotify(BDLocation mlocation, float distance){
	    	Log.i("BDNOTIFY",""+distance);//�������ѵ��趨λ�ø���
	    }
	}
}
