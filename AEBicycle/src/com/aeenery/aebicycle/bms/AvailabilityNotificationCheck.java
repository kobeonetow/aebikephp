package com.aeenery.aebicycle.bms;

import com.aeenery.aebicycle.bms.models.BMSPacket;

public interface AvailabilityNotificationCheck {
	
	/**
	 * 超时设置,如果超时成立，返回true,否则false
	 */
	public boolean responseTimeOut(String packetId);
	
	/**
	 * Handler packet receive
	 * @param packet
	 */
	public void handlerReceivePacket(BMSPacket packet);
}
