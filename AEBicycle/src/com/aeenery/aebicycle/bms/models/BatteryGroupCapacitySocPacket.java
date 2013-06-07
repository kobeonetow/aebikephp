package com.aeenery.aebicycle.bms.models;

import com.aeenery.aebicycle.bms.BMSUtil;


/**
 * 00A6 00A7
 * @author Jianxing
 *
 */
public class BatteryGroupCapacitySocPacket  extends BMSPacket{

	private byte absoluteSOC; //HEX
	private byte relativeSOC; //HEX
	private byte[] capacityleft;
	private byte[] totalcapacity;
	public BatteryGroupCapacitySocPacket(BMSPacket packet){
		super(packet);
		absoluteSOC = body[0];
		relativeSOC = body[1];
		capacityleft = BMSUtil.extractByteArray(body, 2, 5);
		totalcapacity = BMSUtil.extractByteArray(body, 7, 5);
	}
	
	public int getAbsoluteSOC(){
		return (int)(absoluteSOC & (int)0xFF);
	}
	
	public int getRelativeSOC(){
		return (int)(relativeSOC & (int)0xFF);
	}
	
	public float getCapacityleft(){
		float byte0 = (float)BMSUtil.convertByteToBCD(capacityleft[0]) * 10000f; //10kAh to Ah
		float byte1 = (float)BMSUtil.convertByteToBCD(capacityleft[1]) * 100f; //100Ah to Ah
		float byte2 = (float)BMSUtil.convertByteToBCD(capacityleft[2]); 
		float byte3 = (float)BMSUtil.convertByteToBCD(capacityleft[3])/100f; //10mAh to Ah
		float byte4 = (float)BMSUtil.convertByteToBCD(capacityleft[4])/10000f;//100uAh to Ah
		return (byte0+byte1+byte2+byte3+byte4);
	}
	
	public float getTotalcapacity(){
		float byte0 = (float)BMSUtil.convertByteToBCD(totalcapacity[0]) * 10000f; //10kAh to Ah
		float byte1 = (float)BMSUtil.convertByteToBCD(totalcapacity[1]) * 100f; //100Ah to Ah
		float byte2 = (float)BMSUtil.convertByteToBCD(totalcapacity[2]); 
		float byte3 = (float)BMSUtil.convertByteToBCD(totalcapacity[3])/100f; //10mAh to Ah
		float byte4 = (float)BMSUtil.convertByteToBCD(totalcapacity[4])/10000f;//100uAh to Ah
		return (byte0+byte1+byte2+byte3+byte4);
	}
	
}
