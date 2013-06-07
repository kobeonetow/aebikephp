package com.aeenery.aebicycle.bms.models;

import com.aeenery.aebicycle.bms.BMSUtil;


/**
 * 00A0 00A1
 * @author Jianxing
 *
 */
public class BatteryVoltageCurrentPacket extends BMSPacket{
	
	
	private byte[] voltage;
	private byte[] current;
	public BatteryVoltageCurrentPacket(BMSPacket packet){
		super(packet);
		voltage = BMSUtil.extractByteArray(body, 0, 4);
		current = BMSUtil.extractByteArray(body, 4, 4);
	}

	public float getVoltage(){
		int sign = BMSUtil.getSign(voltage[0]);
		float byte0 = (float)BMSUtil.convertByteToBCD(BMSUtil.hideFirstSignBit(voltage[0])) * 100f; //100V to V
		float byte1 = (float)BMSUtil.convertByteToBCD(voltage[1]); //V to V
		float byte2 = (float)BMSUtil.convertByteToBCD(voltage[2]) / 100f; //10mV to V
		float byte3 = (float)BMSUtil.convertByteToBCD(voltage[3]) / 10000f; //100uV to V
		return (byte0 + byte1+byte2+byte3)*(float)sign;
	}
	
	public float getCurrent(){
		int sign = BMSUtil.getSign(current[0]);
		float byte0 = (float)BMSUtil.convertByteToBCD(BMSUtil.hideFirstSignBit(current[0])) * 100f; //100A to A
		float byte1 = (float)BMSUtil.convertByteToBCD(current[1]); //A to A
		float byte2 = (float)BMSUtil.convertByteToBCD(current[2]) / 100f; //10mA to A
		float byte3 = (float)BMSUtil.convertByteToBCD(current[3]) / 10000f; //100uA to A
		return (byte0 + byte1+byte2+byte3)*(float)sign;
	}
}
