package com.aeenery.aebicycle.bms.models;

import java.io.UnsupportedEncodingException;

/**
 * 000C
 * @author Jianxing
 *
 */
public class EditBluetoothDevicePacket extends BMSPacket{
	
	public EditBluetoothDevicePacket(String name){
		try {
			byte[] b = name.getBytes("US-ASCII");
			body = new byte[b.length+2];
			System.arraycopy(b, 0, body, 0, b.length);
			body[b.length] = (byte)0xBA;
			body[b.length+1] = (byte)0XBA;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public EditBluetoothDevicePacket(BMSPacket packet){
		super(packet);
	}

}
