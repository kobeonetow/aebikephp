package com.aeenery.aebicycle.bms.models;

import android.util.Log;

import com.aeenery.aebicycle.bms.BMSUtil;

/**
 * 0x0030, 0x0031
 * @author Jianxing
 *
 */
public class BatteryGroupInfoPacket extends BMSPacket {

	private byte[] batteryGroupStandardCapacity;
	private byte[] batteryGroupStandardVoltage;
	private byte[] batteryGroupBatteryNumber;
	public BatteryGroupInfoPacket(BMSPacket packet){
		super(packet);
		setBatteryGroupStandardCapacity(BMSUtil.extractByteArray(body, 0, 5));
		setBatteryGroupStandardVoltage(BMSUtil.extractByteArray(body, 5, 5));
		setBatteryGroupBatteryNumber(new byte[]{body[10], body[11]});
	}
	
	/**
	 * At unit uAh  = 0.1mAh
	 * @return
	 */
	public double getBatteryGroupStandardCapacity() {
		return BMSUtil.calculateHalfByteBCDFromByteArrayAtBaseTen(batteryGroupStandardCapacity);
	}
	public void setBatteryGroupStandardCapacity(byte[] batteryGroupStandardCapacity) {
		this.batteryGroupStandardCapacity = batteryGroupStandardCapacity;
	}
	
	
	/**
	 * At Unit  100 uV = 0.1mV
	 * @return
	 */
	public double getBatteryGroupStandardVoltage() {
		return BMSUtil.calculateHalfByteBCDFromByteArrayAtBaseTen(batteryGroupStandardVoltage);
	}
	public void setBatteryGroupStandardVoltage(byte[] batteryGroupStandardVoltage) {
		this.batteryGroupStandardVoltage = batteryGroupStandardVoltage;
	}
	public int getBatteryGroupBatteryNumber() {
		if(batteryGroupBatteryNumber == null)
			return 0;
		int firstb = BMSUtil.convertByteToBCD(batteryGroupBatteryNumber[0]) * 100;
		int secondb = BMSUtil.convertByteToBCD(batteryGroupBatteryNumber[1]);
		return firstb+secondb;
	}
	public void setBatteryGroupBatteryNumber(byte[] batteryGroupBatteryNumber) {
		this.batteryGroupBatteryNumber = batteryGroupBatteryNumber;
	}
	
	
}
