package com.aeenery.aebicycle.bms.models;

import java.util.ArrayList;

import android.util.Log;

import com.aeenery.aebicycle.bms.BMSUtil;

/**
 * 0008,0009,000A
 * @author Jianxing
 *
 */
public class ConfigurationInfoPacket extends BMSPacket{

	private byte switchByte;
	private ArrayList<Byte> bytes =  new ArrayList<Byte>();
	public int numOfSettingBytes = 0;
	public ConfigurationInfoPacket(BMSPacket packet){
		super(packet);
		switchByte = body[0];
		numOfSettingBytes = BMSUtil.convertByteToInt((byte)(switchByte & 0x0F));
		for(int i=0; i<body.length;i++){
			bytes.add(body[i]);
		}
	}
	
	public int getSwitchByte(int position) {
		return BMSUtil.getBitValueForByte(switchByte, position);
	}
	
	public int getSwitchOnByteAndBit(int bytePosition, int bitPosition){
		return BMSUtil.getBitValueForByte(bytes.get(bytePosition),bitPosition);
	}
	
	@Override
	public String toString(){
		String str ="";
		for(Byte b: bytes){
			str += "" +  BMSUtil.convertByteToInt(b);
			str += ",";
		}
		return str.substring(0, str.length()-1);
	}
}
