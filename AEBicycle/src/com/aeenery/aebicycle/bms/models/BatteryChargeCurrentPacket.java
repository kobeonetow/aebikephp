package com.aeenery.aebicycle.bms.models;

import com.aeenery.aebicycle.bms.BMSUtil;

public class BatteryChargeCurrentPacket extends BMSPacket{
	
	private byte[] serialNumber;
	private byte[] highestChargeCurrent;
	private byte[] averageChargeCurrent;
	private byte[] highTemp;
	private byte[] averageTemp;
	
	public BatteryChargeCurrentPacket(BMSPacket packet){
		super(packet);
		serialNumber = BMSUtil.extractByteArray(body, 0, 6);
		highestChargeCurrent = BMSUtil.extractByteArray(body, 6, 4);
		averageChargeCurrent = BMSUtil.extractByteArray(body, 10, 4);
		highTemp = new byte[]{body[14],body[15]};
		averageTemp = new byte[]{body[16],body[17]};
	}
	
	public BatteryChargeCurrentPacket(long _serialNumber){
		serialNumber = new byte[]{
			(byte)(_serialNumber >>> 40),
			(byte)(_serialNumber >>> 32),
			(byte)(_serialNumber >>> 24),
			(byte)(_serialNumber >>> 16),
			(byte)(_serialNumber >>> 8),
			(byte)_serialNumber
		};
		body = serialNumber;
	}
	
	public long getSerialNumber(){
		return BMSUtil.convertBytesToLong(serialNumber);
	}
	
	public int getHighestChargeCurrent(){
		return (int)BMSUtil.convertBytesToLong(highestChargeCurrent);
	}
	
	public int getAverageChargeCurrent(){
		return (int)BMSUtil.convertBytesToLong(averageChargeCurrent);
	}
	
	public int getHighTemp(){
		return (int)(BMSUtil.calculateHalfByteBCDFromByteArrayAtBaseTen(highTemp) * getSign(highTemp[0]));
	}
	
	public int getAverageTemp(){
		return (int)(BMSUtil.calculateHalfByteBCDFromByteArrayAtBaseTen(averageTemp) * getSign(averageTemp[0]));
	}
	
	public int getSign(byte b){
		int sign = (int)((b >> 7) & 1);
		if(sign == 1)
			return 1;
		else
			return -1;
	}

}
