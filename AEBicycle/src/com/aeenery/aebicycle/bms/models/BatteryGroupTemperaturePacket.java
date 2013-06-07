package com.aeenery.aebicycle.bms.models;

import com.aeenery.aebicycle.bms.BMSUtil;

/**
 * 00A2 00A3
 * @author Jianxing
 *
 */
public class BatteryGroupTemperaturePacket extends BMSPacket{
	
	private byte[] groupIndex;
	private byte groupMemberIndex;
	private byte requestNumber;
	private byte status;
	private byte[] tempGroup;
	
	public BatteryGroupTemperaturePacket(BMSPacket packet, int gIndex, int gMemberIndex, int request){
		super(packet);
		requestNumber = BMSUtil.convertIntToBCDBytes(request)[0];
		byte[] convert = BMSUtil.convertIntToBCDBytes(gIndex);
		if(convert.length <2){
			groupIndex = new byte[]{0x00,convert[0]};
		}else{
			groupIndex = new byte[]{convert[0],convert[1]};
		}
		
		groupMemberIndex = BMSUtil.convertIntToBCDBytes(gMemberIndex)[0];
		
		body = new byte[]{groupIndex[0],groupIndex[1],groupMemberIndex,requestNumber};
		this.getHeader().setDataSize(((byte)this.body.length));
	}
	public  BatteryGroupTemperaturePacket(BMSPacket packet){
		super(packet);
		status = body[0];
		groupIndex = new byte[]{body[1],body[2]};
		groupMemberIndex = body[3];
		requestNumber = body[4];
		tempGroup = BMSUtil.extractByteArray(body, 5, body.length-5);
		
	}

	public byte getStatus(){
		return status;
	}
	
	public int getGoupIndex(){
		int byte0 = BMSUtil.convertByteToBCD(groupIndex[0])*100;
		int byte1 = BMSUtil.convertByteToBCD(groupIndex[1]);
		return byte0+byte1;
	}
	
	public int getGroupMemberIndex(){
		return BMSUtil.convertByteToBCD(groupMemberIndex);
	}
	
	public int getRequestNumber(){
		return BMSUtil.convertByteToBCD(requestNumber);
	}
	
	/**
	 * Index from 0
	 * @param position
	 * @return
	 */
	public float getTemperature(int position){
		if(position < getRequestNumber()){
			int pos = 2*position;
			byte[] array  = new byte[]{tempGroup[pos],tempGroup[pos+1]};
			int sign = BMSUtil.getSign(array[0]);
			float byte0  = (float)BMSUtil.convertByteToBCD(BMSUtil.hideFirstSignBit(array[0]))*10f;//10C to C
			float byte1 = (float)BMSUtil.convertByteToBCD(array[1])/10f; //100 mC to C
			return (byte0+byte1)*(float)sign;
		}else
			return 0;
	}
}
