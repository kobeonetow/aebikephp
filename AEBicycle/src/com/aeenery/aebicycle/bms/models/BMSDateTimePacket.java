package com.aeenery.aebicycle.bms.models;

import java.util.Calendar;

import com.aeenery.aebicycle.bms.BMSUtil;

/**
 * 0060,0061,0062,FAAA
 * @author Jianxing
 *
 */
public class BMSDateTimePacket extends BMSPacket{
	
	private byte[] year;
	private byte month;
	private byte day;
	private byte hour;
	private byte min;
	private byte sec;
	public BMSDateTimePacket(BMSPacket packet){
		super(packet);
		year = new byte[]{body[0], body[1]};
		month = body[2];
		day = body[3];
		hour = body[4];
		min = body[5];
		sec = body[6];
	}

	public BMSDateTimePacket(){
		
	}
	
	public Calendar getCalendar(){
		Calendar can = Calendar.getInstance();
		can.set(getYear(), getMonth(), getDay(), getHour(), getMin(), getSec());
		return can;
	}
	
	public void setDateTime(Calendar can){
		this.year =  new byte[] {
	            (byte)(can.get(Calendar.YEAR) >>> 8),
	            (byte)can.get(Calendar.YEAR)};
		this.month = (byte)can.get(Calendar.MONTH);
		this.day = (byte)can.get(Calendar.DAY_OF_MONTH);
		this.hour = (byte)can.get(Calendar.HOUR_OF_DAY);
		this.min = (byte)can.get(Calendar.MINUTE);
		this.sec = (byte)can.get(Calendar.SECOND);
		this.body = new byte[]{year[0],year[1],month,day,hour,min,sec};
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
