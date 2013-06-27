package com.aeenery.aebicycle.bms;

import com.aeenery.aebicycle.bms.models.BMSCommand;
import com.aeenery.aebicycle.bms.models.BMSPacket;
import com.aeenery.aebicycle.bms.models.ChangeDeviceNamePacket;
import com.aeenery.aebicycle.entry.BicycleUtil;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class ConfigController{

	private static final String TAG = "RequestController";
	private static final boolean D = true;
	private static ConfigController configController;
	private static BMSController controller;
	
	public static ConfigController getConfigController(Context _context){
		if(configController == null){
			configController = new ConfigController();
			controller = BMSController.getController(_context);
		}
		return configController;
	}

	public void sendConfigNamePacket(BMSCommand commandId,String name){
		BMSPacket packet = null;
		packet = controller.getBuilder().buildConfigPacket(commandId);
		ChangeDeviceNamePacket configPacket = new ChangeDeviceNamePacket(packet,"AEE:"+name);
		if(D) Log.i(TAG,"Packet build:packetid = "+configPacket.getPacketId());
		controller.sendEmptyByte();
		controller.sendPacket(configPacket);
	}
	
	public void sendEmptyByte() {
		controller.sendEmptyByte();
	}
	
//	private Handler mHandler = new Handler(){
//		@Override
//		public void handleMessage(Message msg) {
//			switch (msg.what) {
//			case BicycleUtil.MESSAGE_STATE_CHANGE:
//				break;
//			}
//		}
//	};
}
