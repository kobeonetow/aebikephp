package com.aeenery.aebicycle.bms.models;

import com.aeenery.aebicycle.bms.BMSUtil;

public class BMSGeneralReplyPacket {

	public BMSCommand command;
	public byte[] flowNumber = new byte[]{(byte)0xAA,(byte)0xAA};
//	private String replyStatus="";
//	private String replyReason="";
	
	private byte body[];
	
	public BMSGeneralReplyPacket(byte[] data){
//		BMSUtil.printByteArrayAsInt("General packet", data);
		byte commandHead = data[0];
		byte commandTail = data[1];
		command = new BMSCommand(commandHead,commandTail, 0);
		
		if(data.length > 7){
			body = new byte[data.length - 5];
			System.arraycopy(data, 4, body, 0, body.length);
		}
	}
	
	public String getReplyStatus(){
		if(body == null)return null;
		int baindex = BMSUtil.indexOfBAIndex(body);
		byte[] displayStr = new byte[baindex];
		System.arraycopy(body, 0, displayStr, 0, baindex);
		String converted = "";
		try{
//			converted = new String(displayStr,"US-ASCII");
			converted = BMSUtil.convertByteArrayToString(displayStr, 0);
		}catch(Exception e){
			converted = "Covert error";
		}
		return converted;
	}
	
	public String getReplyReason(){
		if(body == null)return null;
		int baindex = BMSUtil.indexOfBAIndex(body);
		if((baindex+2)>body.length)
			return "";
		byte[] next = BMSUtil.extractByteArray(body, baindex+2, body.length-baindex-4);
//		BMSUtil.printByteArrayAsInt("status", next);
		String converted = "";
		try{
//			converted = new String(next,"US-ASCII");
			converted = BMSUtil.convertByteArrayToString(next, 0);
		}catch(Exception e){
			converted = "Covert error";
		}
		return converted;
	}
}
