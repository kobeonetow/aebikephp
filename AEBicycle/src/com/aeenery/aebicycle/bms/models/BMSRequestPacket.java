package com.aeenery.aebicycle.bms.models;

import com.aeenery.aebicycle.bms.BMSUtil;

public class BMSRequestPacket extends BMSPacket{
	
	public BMSRequestPacket(){
		super();
	}
	
	public BMSRequestPacket(BMSCommand commandId){
		BMSPacketHeader header = new BMSPacketHeader();
		header.setCommandId(commandId);
		header.setFlowNumber(new FlowNumber(0x0000));
		header.setDataSize((byte)0x00);
		header.setPacketIndex((byte)0x00);
		super.setHeader(header);
		super.setBody(null);
		setCheckCode(BMSUtil.calculatePacketCheckCode(header.getHeaderAsByte()));
	}

	@Override
	public byte[] getPacketAsByteArray() {
		byte[] headerBytes = this.header.getHeaderAsByte();
//		this.totalSize = 5 + headerBytes.length + this.body.length;
		this.totalSize = 5 + headerBytes.length;
		byte[] ret = new byte[this.totalSize];
		ret[0] = this.AEIndex;
		ret[1] = this.AEIndex;
		System.arraycopy(headerBytes, 0, ret, 2, headerBytes.length);
//		System.arraycopy(this.body, 0, ret, 2+headerBytes.length, body.length);
		ret[this.totalSize-3] = this.checkCode;
		ret[this.totalSize-2] = this.AFIndex;
		ret[this.totalSize-1] = this.AFIndex;
		return ret;
	}
}
