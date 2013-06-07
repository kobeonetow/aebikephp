package com.aeenery.aebicycle.bms.models;

public class FlowNumber {

	private byte[] number = new byte[2];

	/**
	 * Must be a two byte integer
	 * will take value of 4 hex value 0xYYYY
	 * @param command
	 */
	public FlowNumber(int flownumber){
		byte tail = (byte)(flownumber & 0xFF);
		byte head = (byte)((flownumber >> 8 ) & 0xFF);
		this.number[0] = head;
		this.number[1] = tail;
	}
	
	public FlowNumber(byte head, byte tail){
		this.number[0] = head;
		this.number[1] = tail;
	}
	
	public byte[] getNumber() {
		return this.number;
	}

	public void setNumber(byte[] number) {
		this.number = number;
	}
}
