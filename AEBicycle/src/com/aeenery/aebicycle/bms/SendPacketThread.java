package com.aeenery.aebicycle.bms;

import android.content.Context;

public class SendPacketThread extends Thread{
	public int PERIOD = 10000;
	SenderContext context;
	boolean run = true;
	
	public SendPacketThread(SenderContext _context, int period){
		this.context = _context;
		this.PERIOD = period;
	}
	
	public void run(){
		while(run){
			context.sendPackets();
			try {
				this.sleep(PERIOD);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void cancel(){
		run = false;
	}
	
	public void reRun(){
		run = true;
	}
}
