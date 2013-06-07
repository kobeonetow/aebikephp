package com.aeenery.aebicycle.bms.models;

import com.aeenery.aebicycle.bms.BMSUtil;


/**
 * 0080 0081
 * @author Jianxing
 *
 */
public class BatteryTemperaturePacket extends BMSPacket{
	
	public byte[] highest;
	public byte[] lowest;
	
	public BatteryTemperaturePacket(BMSPacket packet){
		super(packet);
		highest = new byte[]{body[0],body[1]};
		lowest = new byte[]{body[2],body[3]};
	}
	
	public float getHighest(){
		int sign = BMSUtil.getSign(highest[0]);
		float firstb = BMSUtil.convertByteToBCD(BMSUtil.hideFirstSignBit(highest[0]))*10f;
		float secondb = (float)BMSUtil.convertByteToBCD(highest[1])/10f; 
		return (firstb+secondb)*sign;
	}
	
	public float getLowest(){
		int sign = BMSUtil.getSign(lowest[0]);
		float firstb = BMSUtil.convertByteToBCD(BMSUtil.hideFirstSignBit(lowest[0]))*10f;
		float secondb = (float)BMSUtil.convertByteToBCD(lowest[1])/10f; 
		return (firstb+secondb)*sign;
	}

}
