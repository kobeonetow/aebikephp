package com.aeenery.aebicycle.bms.models;

import com.aeenery.aebicycle.bms.BMSUtil;

/**
 * 0036 0037
 * @author Jianxing
 *
 */
public class BatteryChipInfoPacket extends BMSPacket{
	
	public byte[] capacity;
	public byte[] voltage;
	
	public BatteryChipInfoPacket(BMSPacket packet){
		super(packet);
		capacity = BMSUtil.extractByteArray(body, 0, 4);
		voltage  = BMSUtil.extractByteArray(body, 4, 3);
	}
	
	public double getCapacity(){
		return BMSUtil.calculateHalfByteBCDFromByteArrayAtBaseTen(capacity);
	}
	
	public double getVoltage(){
		return BMSUtil.calculateHalfByteBCDFromByteArrayAtBaseTen(voltage);
	}
}
