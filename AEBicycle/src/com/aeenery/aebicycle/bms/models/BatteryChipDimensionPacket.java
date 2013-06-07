package com.aeenery.aebicycle.bms.models;

import com.aeenery.aebicycle.bms.BMSUtil;

/**
 * 0034 0035
 * @author Jianxing
 *
 */
public class BatteryChipDimensionPacket extends BMSPacket{

	public int length;
	public int width;
	public int height;
	public BatteryChipDimensionPacket(BMSPacket packet){
		super(packet);
		length = (int) BMSUtil.convertBytesToLong(BMSUtil.extractByteArray(body, 0, 3));
		width = (int)BMSUtil.convertBytesToLong(BMSUtil.extractByteArray(body, 3, 3));
		height = (int)BMSUtil.convertBytesToLong(BMSUtil.extractByteArray(body, 6, 2));
		
	}
	
	public int getLength(){
		return length;
	}
	
	public int getWidth(){
		return width;
	}
	
	public int getHeight(){
		return height;
	}
}
