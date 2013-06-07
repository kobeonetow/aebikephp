package com.aeenery.aebicycle.bms.models;

import com.aeenery.aebicycle.bms.BMSUtil;

/**
 * 0x00AA  and 0x00AB
 * @author Jianxing
 *
 */
public class BatteryTimeLeftPacket extends BMSPacket {

	private byte[] hour;
	private byte minute;
	
	public BatteryTimeLeftPacket(BMSPacket packet){
		super(packet);
		hour  = new byte[]{body[0],body[1],body[2]};
		minute = body[3];
	}

	public long getHour(){
		int last = BMSUtil.convertByteToBCD(hour[2]);
		int middle = BMSUtil.convertByteToBCD(hour[1]);
		int first = BMSUtil.convertByteToBCD(hour[0]);
		long ret = (long)first*10000 + (long)middle*100 + (long)last;
		return ret;
	}
	
	public int getMinute(){
		return BMSUtil.convertByteToBCD(minute);
	}
}
