package com.aeenery.aebicycle.bms.models;

import com.aeenery.aebicycle.bms.BMSUtil;

/**
 * 00A8 00A9
 * @author Jianxing
 *
 */
public class BatteryPeriodicPacket  extends BMSPacket{

	private byte[] periodic;
	public BatteryPeriodicPacket(BMSPacket packet){
		super(packet);
		periodic = body;
	}
	
	public long getPeriodic(){
		long byte0 = (long)BMSUtil.convertByteToBCD(periodic[0]) * 1000000L;
		long byte1 = (long)BMSUtil.convertByteToBCD(periodic[1]) * 10000L; 
		long byte2 = (long)BMSUtil.convertByteToBCD(periodic[2]) * 100L; 
		long byte3 = (long)BMSUtil.convertByteToBCD(periodic[3]);
		return (byte0+byte1+byte2+byte3);
	}
}
