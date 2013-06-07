package com.aeenery.aebicycle.bms;

import com.aeenery.aebicycle.entry.BicycleUtil;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * 提供的毫秒数完结的时候，会发出PACKET_TIME_OUT_ACTION
 * @author Jianxing
 *
 */
public class TimeOutThread extends Thread{

	private final static String TAG = "TimeOutThread";
	private final static boolean D = true;
	private Context context;
	private int waitTime= 1000;
	public boolean broadcast = true;
	private String packetId;
	
	public TimeOutThread(final Context _context, String _packetId){
		this.context = _context;
		this.packetId = _packetId;
	}

	public void run(){
		try {
			if(D) Log.i(TAG,"Time out thread started for " + this.waitTime + " ms.");
			broadcast = true;
			sleep(waitTime);
			if(broadcast){
				Intent intent = new Intent(BMSUtil.PACKET_TIMEOUT_ACTION);
//				intent.putExtra("timeoutId", timeoutId);
				intent.putExtra(BicycleUtil.BT_SEND_MSG_ID,packetId);
				this.context.sendBroadcast(intent);
				if(D) Log.i(TAG,"Time out thread broadcast.");
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	public void cancel(){
//		if(_packetId.equals(this.packetId)){
			this.broadcast = false;
//		}
	}
}
