package com.aeenery.aebicycle.bms.models;



/**
 * 0101
 * @author Jianxing
 *
 */
public class BMSAlarmPacket extends BMSPacket{
	
	private byte alarm;
	public BMSAlarmPacket(BMSPacket packet){
		super(packet);
		alarm = body[0];
	}
	
	/**
	 * bit 3 for right to left order
	 * @return
	 */
	public boolean isOverTemperature(){
		int over = (int)((alarm >> 2) & 1);
		if(over == 1)
			return true;
		else
			return false;
	}
	
	/**
	 * bit 2 for right to left order
	 * @return
	 */
	public boolean isOverDischarge(){
		int over = (int)((alarm >> 1) & 1);
		if(over == 1)
			return true;
		else
			return false;
	}
	
	/**
	 * bit 1 order from right to left
	 * @return
	 */
	public boolean isOverCharge(){
		int over = (int)(alarm & 1);
		if(over == 1)
			return true;
		else
			return false;
	}

}
