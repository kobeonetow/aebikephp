package com.aeenery.aebicycle.bms.models;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import android.util.Log;

import com.aeenery.aebicycle.bms.BMSUtil;


public class BMSPacket {
	
	private static final Random r = new Random();
	private String packetId = BMSUtil.MD5(r.nextDouble() + "aeenergy");;
	protected final byte AEIndex = (byte)0xAE;
	protected final byte AFIndex = (byte)0xAF;
	protected BMSPacketHeader header;
	protected byte[] body;
	protected byte checkCode;
	protected int totalSize;
	
	public BMSPacket(byte[] data){
		header = new BMSPacketHeader(data);
		body = new byte[BMSUtil.convertByteToInt(header.getDataSize())];
		System.arraycopy(data, 9, body, 0, body.length);
		checkCode = data[data.length-1];
	}
	
	public BMSPacket(BMSPacket packet){
		header = packet.getHeader();
		body = packet.getBody();
		checkCode = packet.getCheckCode();
		packetId = packet.getPacketId();
	}
	
	public BMSPacket(){
//		double series = r.nextDouble();
//		packetId = BMSUtil.MD5(series + "aeenergy");
//		Log.e("##############","Packet build with r ID: "+ r + " - "+ packetId);
	}
	
	public String getPacketId(){
		return this.packetId;
	}
	
	public BMSPacketHeader getHeader() {
		return header;
	}
	public void setHeader(BMSPacketHeader header) {
		this.header = header;
	}
	public byte[] getBody() {
		return body;
	}
	public void setBody(byte[] body) {
		this.body = body;
	}
	public byte getCheckCode() {
		return checkCode;
	}
	public void setCheckCode(byte checkCode) {
		this.checkCode = checkCode;
	}
	public byte getAEIndex() {
		return AEIndex;
	}
	public byte getAFIndex() {
		return AFIndex;
	}
	public Integer getTotalSize(){
		return this.totalSize;
	}
	
	public byte[] getPacketAsByteArray(){
		byte[] headerBytes = this.header.getHeaderAsByte();
//		this.totalSize = 5 + headerBytes.length + this.body.length;
		this.totalSize = 5 + headerBytes.length + body.length;
		byte[] ret = new byte[this.totalSize];
		ret[0] = this.AEIndex;
		ret[1] = this.AEIndex;
		System.arraycopy(headerBytes, 0, ret, 2, headerBytes.length);
		System.arraycopy(this.body, 0, ret, 2+headerBytes.length, body.length);
		ret[this.totalSize-3] = this.checkCode;
		ret[this.totalSize-2] = this.AFIndex;
		ret[this.totalSize-1] = this.AFIndex;
		return ret;
	};
}
