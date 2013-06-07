package com.aeenery.aebicycle.bms;


import com.aeenery.aebicycle.bms.models.BMSCommand;
import com.aeenery.aebicycle.bms.models.BMSPacket;
import com.aeenery.aebicycle.bms.models.BatteryGroupTemperaturePacket;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class RequestController{
	
	private static final String TAG = "RequestController";
	private static final boolean D = true;
	private static RequestController requestController;
	private BMSController controller;
	
	public RequestController(){
		controller = BMSController.getController();
	}

	public static RequestController getRequestController(){
		if(requestController == null){
			requestController = new RequestController();
		}
		return requestController;
	}
	
	public void sendRequestPacket(BMSCommand command, boolean special){
		BMSPacket packet = null;
		if(!special)
			packet = controller.getBuilder().buildRequestPacket(command);
		else
			packet = getSpecialRequestPacket(command);
		if(D) Log.i(TAG,"Packet build:packetid = "+packet.getPacketId());
		controller.sendPacket(packet);
	}
	
	private BMSPacket getSpecialRequestPacket(BMSCommand command) {
		BMSPacket packet = controller.getBuilder().buildRequestPacket(command);
		switch(command.getCommandAsByteInt()){
			case BMSUtil.COMMAND_GET_BATTERY_TEMPERATURE_NOW_DETAIL:
				BatteryGroupTemperaturePacket temPacket = new BatteryGroupTemperaturePacket(packet,1,1,1);
				return temPacket;
			default:
				return packet;
		}
	}

//	private Handler mHandler = new Handler(){
//		@Override
//		public void handleMessage(Message msg) {
//			switch (msg.what) {
//			case BMSUtil.COMMAND_GET_DEVICE_SERIAL_NUMBER:
//				//if request approved
//				//or not, do something
//				break;
//			}
//		}
//	};

	public void sendEmptyByte() {
		controller.sendEmptyByte();
	}
	
}
