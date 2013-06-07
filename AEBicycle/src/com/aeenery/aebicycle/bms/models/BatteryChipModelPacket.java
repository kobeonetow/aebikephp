package com.aeenery.aebicycle.bms.models;

import com.aeenery.aebicycle.bms.BMSUtil;

/**
 * command 0032 0033
 * @author Jianxing
 *
 */
public class BatteryChipModelPacket extends BMSPacket{

	
	private String model;
	public BatteryChipModelPacket(BMSPacket packet){
		super(packet);
		model = BMSUtil.convertByteArrayToString(body, 0, body.length-3);
	}
	
	public String getModel(){
		return model;
	}
}
