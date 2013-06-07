package com.aeenery.aebicycle.bms.models;

import com.aeenery.aebicycle.bms.BMSUtil;

public class DeviceSerialNumberPacket extends BMSPacket{
	
	private byte[] deviceSerialNumber;
	public DeviceSerialNumberPacket(BMSPacket packet){
		super(packet);
		deviceSerialNumber = BMSUtil.extractByteArray(body, 0, body.length-2);
	}
	public String getDeviceSerialNumber() {
		return BMSUtil.convertByteArrayToString(deviceSerialNumber, 0);
	}
	public void setDeviceSerialNumber(byte[] deviceSerialNumber) {
		this.deviceSerialNumber = deviceSerialNumber;
	}
}
