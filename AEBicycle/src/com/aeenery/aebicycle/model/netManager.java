package com.aeenery.aebicycle.model;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;

public class netManager {
	private Context context;
	private WifiManager mWifiMgr;
	
	public netManager(Context context){
		this.context = context;
		//��ȡWIFI������
		mWifiMgr = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
	}
	
	public boolean checkConnect(){
		ConnectivityManager connMgr = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		//��ȡ����״̬
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		//��ȡ��ǰ�����Ƿ����
		if (networkInfo == null) {
			return false;
		}
		return true;
	}
	
	//�Ƿ�������WIFI
	public boolean isWifiConnect(){
		return mWifiMgr.isWifiEnabled();
	}
	
	//����WIFI����
	public boolean aquireWifi(){
		if (!mWifiMgr.isWifiEnabled()) {
			return mWifiMgr.setWifiEnabled(true);
		}
		else {
			return true;
		}
	}
}
