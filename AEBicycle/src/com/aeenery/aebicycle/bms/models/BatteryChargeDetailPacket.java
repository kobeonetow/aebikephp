package com.aeenery.aebicycle.bms.models;
import com.aeenery.aebicycle.bms.BMSUtil;


/**
 * 0084, 0085
 * @author Jianxing
 *
 */
public class BatteryChargeDetailPacket extends BMSPacket{
	
	public byte[] serialNumber;
	public byte[] largestCurrent;
	public byte[] averageCurrent;
	public byte[] highestTemp;
	public byte[] averageTemp;
	

	public BatteryChargeDetailPacket(long _serialNumber){
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

	public BatteryChargeDetailPacket(BMSPacket packet){
		super(packet);
		serialNumber = BMSUtil.extractByteArray(body, 0, 6);
		largestCurrent = BMSUtil.extractByteArray(body, 6, 4);
		averageCurrent = BMSUtil.extractByteArray(body, 10, 4);
		highestTemp = new byte[]{body[14],body[15]};
		averageTemp = new byte[]{body[16],body[17]};
	}
	
	public long getSrialNumber(){
		return BMSUtil.convertBytesToLong(serialNumber);
	}
	
	public int getLargestCurrent(){
		return (int)(BMSUtil.convertBytesToLong(largestCurrent) * getSign(largestCurrent[0]));
	}

	public int getAverageCurrent(){
		return (int)(BMSUtil.convertBytesToLong(averageCurrent) * getSign(averageCurrent[0]));
	}
	
	public int getLargestTemp(){
		return BMSUtil.convertTwoBytesToInt2(highestTemp[0], highestTemp[1]);
	}
	
	public int getAverageTemp(){
		return BMSUtil.convertTwoBytesToInt2(averageTemp[0], averageTemp[1]);
	}
	
	public int getSign(byte b){
		int sign = (int)((b >> 7) & 1);
		if(sign == 1)
			return 1;
		else
			return -1;
	}

}
