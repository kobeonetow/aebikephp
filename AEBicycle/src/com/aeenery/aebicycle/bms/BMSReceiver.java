package com.aeenery.aebicycle.bms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class BMSReceiver extends BroadcastReceiver{

	private final Handler mHandler;
	
	public BMSReceiver(Handler _handler){
		this.mHandler = _handler;
	}
	
	@Override
	public void onReceive(Context context, Intent _action) {
		String action = _action.getAction();
//		if(BMSUtil.BMS_PACKET_RECEIVED.equals(action)){
			//get message
			//check message
			//get command id
			//distribute to appropriate handler
//			mHandler.sendMessage(this.buildMessage(BMSUtil.COMMAND1, ""));
//		}else if(BMSUtil.PACKET_TIMEOUT_ACTION.equals(action)){
//			
//		}
	}
	
	public Message buildMessage(int what, String data){
		Message msg = new Message();
		msg.what = what;
		Bundle bundle = new Bundle();
		bundle.putString("data", data);
		msg.setData(bundle);
		return msg;
	}

}
