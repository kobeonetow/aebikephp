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
		//获取WIFI管理器
		mWifiMgr = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
	}
	
	public boolean checkConnect(){
		ConnectivityManager connMgr = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		//获取网络状态
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		//获取当前网络是否可用
		if (networkInfo == null) {
			return false;
		}
		return true;
	}
	
	//是否连接了WIFI
	public boolean isWifiConnect(){
		return mWifiMgr.isWifiEnabled();
	}
	
	//请求WIFI连接
	public boolean aquireWifi(){
		if (!mWifiMgr.isWifiEnabled()) {
			return mWifiMgr.setWifiEnabled(true);
		}
		else {
			return true;
		}
	}
}
