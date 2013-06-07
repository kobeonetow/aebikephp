package com.aeenery.aebicycle.bms.models;

import com.aeenery.aebicycle.bms.BMSUtil;

import android.util.Log;

public class BMSPacketHeader {

	private BMSCommand commandId;
	private byte dataSize;
	private byte packetIndex;
	private FlowNumber flowNumber;
	private byte[] reserve;
	
	
	public BMSPacketHeader(byte[] data){
		commandId = new BMSCommand(data[0],data[1], 0xFFFA);
//		BMSUtil.printByteArrayAsInt("BMSHeader", data);
//		Log.e("BMSHeader",commandId.getCommandAsByteInt()+"");
		dataSize = data[2];
		packetIndex = data[3];
		flowNumber = new FlowNumber(data[4],data[5]);
		reserve = new byte[3];
		reserve[0] = data[6];
		reserve[1] = data[7];
		reserve[2] = data[8];
	}
	
	public BMSPacketHeader(){
		reserve = new byte[3];
	}
	
	public BMSCommand getCommandId(){
		return this.commandId;
	}
	public void setCommandId(BMSCommand command){
		this.commandId = command;
	}
	public byte getDataSize() {
		return dataSize;
	}
	public void setDataSize(byte dataSize) {
		this.dataSize = dataSize;
	}
	public byte getPacketIndex() {
		return packetIndex;
	}
	public void setPacketIndex(byte packetIndex) {
		this.packetIndex = packetIndex;
	}
	public byte[] getReserve() {
		return reserve;
	}
	public void setReserve(byte[] reserve, int position) {
		this.reserve[position] = reserve[position];
	}
	
	public byte[] getHeaderAsByte(){
		byte[] ret = new byte[9];
		System.arraycopy(this.commandId.getCommand(), 0, ret, 0, 2);
		ret[2] = this.dataSize;
		ret[3] = this.packetIndex;
		System.arraycopy(this.flowNumber.getNumber(), 0, ret, 4, 2);
		System.arraycopy(this.reserve, 0, ret, 6, 3);
		return ret;
	}

	public FlowNumber getFlowNumber() {
		return flowNumber;
	}

	public void setFlowNumber(FlowNumber flowNumber) {
		this.flowNumber = flowNumber;
	}
}
