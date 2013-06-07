package com.aeenery.aebicycle.bms.models;

import java.util.Calendar;

import com.aeenery.aebicycle.bms.BMSUtil;

/**
 * 0082,0083
 * @author Jianxing
 *
 */
public class BatteryChargeTimePacket extends BMSPacket{
	
	private byte[] serialNumber;
	private byte[] year;
	private byte month;
	private byte day;
	private byte hour;
	private byte min;
	private byte sec;
	private byte[] chargeTime;
	private byte socBefore;
	private byte socAfter;
	
	public BatteryChargeTimePacket(long _serialNumber){
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
	
	public BatteryChargeTimePacket(BMSPacket packet){
		super(packet);
		serialNumber = BMSUtil.extractByteArray(body, 0, 6);
		year = new byte[]{body[6],body[7]};
		month = body[8];
		day = body[9];
		hour = body[10];
		min = body[11];
		sec = body[12];
		chargeTime = new byte[]{body[13],body[14]};
		socBefore = body[15];
		socAfter = body[16];
	}
	
	public long getSrialNumber(){
		return BMSUtil.convertBytesToLong(serialNumber);
	}
	
	public Calendar getCalendar(){
		Calendar can = Calendar.getInstance();
		can.set(getYear(), getMonth(), getDay(), getHour(), getMin(), getSec());
		return can;
	}
	
	public int getChargeTime(){
		return BMSUtil.convertTwoBytesToInt2(chargeTime[0], chargeTime[1]);
	}
	
	
	public int getSocBefore(){
		return BMSUtil.convertByteToInt(socBefore);
	}
	
	public int getSocAfter(){
		return BMSUtil.convertByteToInt(socAfter);
	}
	
	public int getYear(){
		return BMSUtil.convertTwoBytesToInt2(year[0], year[1]);
	}
	
	public int getMonth(){
		return BMSUtil.convertByteToInt(month);
	}
	
	public int getDay(){
		return BMSUtil.convertByteToInt(day);
	}
	
	public int getHour(){
		return BMSUtil.convertByteToInt(hour);
	}
	
	public int getMin(){
		return BMSUtil.convertByteToInt(min);
	}
	
	public int getSec(){
		return BMSUtil.convertByteToInt(sec);
	}
}
