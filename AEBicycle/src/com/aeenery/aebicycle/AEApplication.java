package com.aeenery.aebicycle;


import com.aeenery.aebicycle.entry.BicycleUtil;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.MKGeneralListener;
import com.baidu.mapapi.map.MKEvent;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

public class AEApplication extends Application{
	
	private static AEApplication instance = null;
	private BMapManager mBMapManager = null;
	
	public static final String strBDApiKey = BicycleUtil.mapKey;
	
	@Override
    public void onCreate() {
	    super.onCreate();
		instance = this;
		initEngineManager(this);
	}

	public void initEngineManager(Context context) {
        if (mBMapManager == null) {
            mBMapManager = new BMapManager(context);
        }

        if (!mBMapManager.init(strBDApiKey,new MyGeneralListener())) {
            Toast.makeText(AEApplication.getInstance().getApplicationContext(), 
                    "BMapManager  初始化错误!", Toast.LENGTH_LONG).show();
        }
	}
	
	@Override
	//建议在您app的退出之前调用mapadpi的destroy()函数，避免重复初始化带来的时间消耗
	public void onTerminate() {
		// TODO Auto-generated method stub
	    if (mBMapManager != null) {
            mBMapManager.destroy();
            mBMapManager = null;
        }
		super.onTerminate();
	}
	
	public static AEApplication getInstance() {
		return instance;
	}
	
	public BMapManager getBMapManager(){
		return this.mBMapManager;
	}
	
	// 常用事件监听，用来处理通常的网络错误，授权验证错误等
    public static class MyGeneralListener implements MKGeneralListener {
        
        @Override
        public void onGetNetworkState(int iError) {
            if (iError == MKEvent.ERROR_NETWORK_CONNECT) {
                Toast.makeText(AEApplication.getInstance().getApplicationContext(), "您的网络出错啦！",
                    Toast.LENGTH_LONG).show();
            }
            else if (iError == MKEvent.ERROR_NETWORK_DATA) {
                Toast.makeText(AEApplication.getInstance().getApplicationContext(), "输入正确的检索条件！",
                        Toast.LENGTH_LONG).show();
            }
            // ...
        }

        @Override
        public void onGetPermissionState(int iError) {
            if (iError ==  MKEvent.ERROR_PERMISSION_DENIED) {
                //授权Key错误：
                Toast.makeText(AEApplication.getInstance().getApplicationContext(), 
                        "输入正确的授权Key！", Toast.LENGTH_LONG).show();
            }
        }
    }

	public void setBMapManager(BMapManager bMapManager) {
		this.mBMapManager = bMapManager;
	}
}
