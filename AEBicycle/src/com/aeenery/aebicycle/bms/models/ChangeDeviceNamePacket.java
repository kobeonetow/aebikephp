package com.aeenery.aebicycle.bms.models;

import android.util.Log;

import com.aeenery.aebicycle.bms.BMSUtil;

public class ChangeDeviceNamePacket extends BMSPacket{

	public ChangeDeviceNamePacket(BMSPacket packet, String name){
		super(packet);
		byte[] nameBytes = null;
		try{
			nameBytes = name.getBytes("UTF-8");
		}catch(Exception e){
			Log.e("DeviceName to utf8",e.getMessage());
			nameBytes = name.getBytes();
		}
		body = new byte[nameBytes.length+2];
		System.arraycopy(nameBytes, 0, body, 0, nameBytes.length);
		body[nameBytes.length] = (byte)0xBA;
		body[nameBytes.length+1] = (byte)0xBA;
		this.getHeader().setDataSize(((byte)this.body.length));
		setCheckCode();
	}
	
	public void setCheckCode(){
		byte[] check = new byte[header.getHeaderAsByte().length+body.length];
		System.arraycopy(header.getHeaderAsByte(), 0, check,0, header.getHeaderAsByte().length);
		System.arraycopy(body, 0, check, header.getHeaderAsByte().length, body.length);
		this.checkCode = BMSUtil.calculatePacketCheckCode(check);
	}
}
