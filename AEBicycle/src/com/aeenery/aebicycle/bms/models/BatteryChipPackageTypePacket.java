package com.aeenery.aebicycle.bms.models;

import com.aeenery.aebicycle.bms.BMSUtil;

public class BatteryChipPackageTypePacket extends BMSPacket{
	
	public byte batteryChipType;
	public byte batteryPackage;
	public BatteryChipPackageTypePacket(BMSPacket packet){
		super(packet);
		batteryChipType = body[0];
		batteryPackage = body[1];
	}

	public int getBatteryChipType(){
		return BMSUtil.convertByteToInt(batteryChipType);
	}
	
	public int getBatteryPackage(){
		return BMSUtil.convertByteToInt(batteryPackage);
	}
}
