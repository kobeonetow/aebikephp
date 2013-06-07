package com.aeenery.aebicycle.bms;

import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.Log;

import com.aeenery.aebicycle.bms.models.BMSCommand;
import com.aeenery.aebicycle.bms.models.BMSPacket;

public class BMSUtil {
	
	//Debug
	public final static boolean D = true;
	
	//Timeout Action
	public final static String PACKET_TIMEOUT_ACTION = "com.aeenergy.aebicycle.bms.PACKET_TIME_OUT_ACTION";
	
	//Message Received Action
	public final static String BMS_PACKET_RECEIVED = "com.aeenergy.aebicycle.bms.BMS_PACKET_RECEIVED_ACTION";
	
	//Timeout over limits and send to activity that nothing can be updated by previous packet sent
	public final static String BATTERY_UPDATE_FAIL_OVER_TIMEOUT = "com.aeenergy.aebicycle.bms.BATTERY_UPDATE_FAIL_OVER_TIMEOUT";
	
	//Message what define
	public final static int COMMAND_UNIVERSAL = 0xFAAA;
	public final static int FLOW_NUMBER_UNIVERSAL = 0xAAAA;
	
	public final static int COMMAND_GET_SYSTEM_INFO = 0x0000;
	public final static int COMMAND_GET_SYSTEM_INFO_REPLY = 0x0001;
	public final static int COMMAND_GET_HARDWARE_VERSION = 0x0002;
	public final static int COMMAND_GET_HARDWARE_VERSION_REPLY = 0x0003;
	public final static int COMMAND_GET_SOFTWARE_VERSION = 0x0004;
	public final static int COMMAND_GET_SOFTWARE_VERSION_REPLY = 0x0005;
	public final static int COMMAND_GET_DEVICE_SERIAL_NUMBER = 0x0006;
	public final static int COMMAND_GET_DEVICE_SERIAL_NUMBER_REPLY = 0x0007;
	public final static int COMMAND_GET_SETTING_INFO = 0x0008;
	public final static int COMMAND_GET_SETTING_INFO_REPLY = 0x0009;
	public final static int COMMAND_EDIT_SETTING_INFO = 0x000A;
	public final static int COMMAND_EDIT_SETTING_INFO_REPLY = COMMAND_UNIVERSAL;
	public final static int COMMAND_EDIT_BLUETOOTH_DEVICE_NAME = 0x000C;
	public final static int COMMAND_EDIT_BLUETOOTH_DEVICE_NAME_REPLY = COMMAND_UNIVERSAL;
	public final static int COMMAND_GET_BATTERY_INFO = 0x0030;
	public final static int COMMAND_GET_BATTERY_INFO_REPLY = 0x0031;
	public final static int COMMAND_GET_BATTERY_CHIP_MODEL = 0x0032;
	public final static int COMMAND_GET_BATTERY_CHIP_MODEL_REPLY = 0x0033;
	public final static int COMMAND_GET_BATTERY_CHIP_SIZE = 0x0034;
	public final static int COMMAND_GET_BATTERY_CHIP_SIZE_REPLY = 0x0035;
	public final static int COMMAND_GET_BATTERY_CHIP_STANDARD_VOLTAGE_CAPACITY = 0x0036;
	public final static int COMMAND_GET_BATTERY_CHIP_STANDARD_VOLTAGE_CAPACITY_REPLY = 0x0037;
	public final static int COMMAND_GET_BATTERY_CHIP_PACKAGE_AND_TYPE = 0x0038;
	public final static int COMMAND_GET_BATTERY_CHIP_PACKAGE_AND_TYPE_REPLY = 0x0039;
	public final static int COMMAND_GET_BATTERY_CLOCK = 0x0060;
	public final static int COMMAND_GET_BATTERY_CLOCK_REPLY = 0x0061;
	public final static int COMMAND_EDIT_BATTERY_CLOCK = 0x0062;
	public final static int COMMAND_EDIT_BATTERY_CLOCK_REPLY = COMMAND_UNIVERSAL;
	public final static int COMMAND_GET_BATTERY_TEMPERATURE = 0x0080;
	public final static int COMMAND_GET_BATTERY_TEMPERATURE_REPLY = 0x0081;
	public final static int COMMAND_GET_BATTERY_CHARGE_TIME_ONE = 0x0082;
	public final static int COMMAND_GET_BATTERY_CHARGE_TIME_ONE_REPLY = 0x0083;
	public final static int COMMAND_GET_BATTERY_CHARGE_TIME_TWO = 0x0084;
	public final static int COMMAND_GET_BATTERY_CHARGE_TIME_TWO_REPLY = 0x0085;
	public final static int COMMAND_GET_BATTERY_VOLTAGE_CURRENT = 0x00A0;
	public final static int COMMAND_GET_BATTERY_VOLTAGE_CURRENT_REPLY = 0x00A1;
	public final static int COMMAND_GET_BATTERY_TEMPERATURE_NOW_DETAIL = 0x00A2;
	public final static int COMMAND_GET_BATTERY_TEMPERATURE_NOW_DETAIL_REPLY = 0x00A3;
	public final static int COMMAND_GET_BATTERY_CHIP_VOLTAGE = 0x00A4;
	public final static int COMMAND_GET_BATTERY_CHIP_VOLTAGE_REPLY = 0x00A5;
	public final static int COMMAND_GET_BATTERY_CAPACITY_AND_SOC_STATUS = 0x00A6;
	public final static int COMMAND_GET_BATTERY_CAPACITY_AND_SOC_STATUS_REPLY = 0x00A7;
	public final static int COMMAND_GET_BATTERY_LOOP_PERIOD_INFO = 0x00A8;
	public final static int COMMAND_GET_BATTERY_LOOP_PERIOD_INFO_REPLY = 0x00A9;
	public final static int COMMAND_GET_BATTERY_TIME_LEFT = 0x00AA;
	public final static int COMMAND_GET_BATTERY_TIME_LEFT_REPLY = 0x00AB;
	
	
	public final static int COMMAND_BMS_ALARM = 0x0101;
	
	public static byte calculatePacketCheckCode(byte[] headerAndBody){
		byte ret = headerAndBody[0];
		for(int i=1; i<headerAndBody.length;i++){
			ret = (byte)(ret & headerAndBody[i]);
		}
		return ret;
	}

	public static int convertByteToInt(byte b){
		return (int)(b & 0xFF);
	}
	
	public static byte hideFirstSignBit(byte b){
		return (byte) (b & (byte)0x7F);
	}
	
	public static int convertTwoBytesToInt2(byte b1, byte b2) {
	    return (int) (( (b1 & 0xFF) << 8) | (b2 & 0xFF));
	}
	
	public static long convertBytesToLong(byte[] b){
		long value = 0;
		for (int i = 0; i < b.length; i++)
		{
		   value = (value << 8) + (b[i] & 0xff);
		}
		return value;
	}
	
	public static String convertByteArrayToString(byte[] body, int startIndex){
		StringBuffer buf = new StringBuffer();
		for(int i=startIndex; i<body.length;i++){
			buf.append((char) (body[i] & 0xff));
		}
		return buf.toString();
	}
	
	public static String convertByteArrayToString(byte[] body, int startIndex, int lastIndex){
		StringBuffer buf = new StringBuffer();
		for(int i=startIndex; i<= lastIndex;i++){
			buf.append((char) (body[i] & 0xff));
		}
		return buf.toString();
	}
	
	public static byte[] extractByteArray(byte[] body, int start, int length){
		byte[] a = new byte[length];
		System.arraycopy(body, start, a, 0, length);
		return a;
	}
	
	public static double calculateHalfByteBCDFromByteArrayAtBaseTen(byte[] b){
		int power = 0;
		double result = 0.0;
		for(int i=b.length-1;i>=0;i--){
			byte last = b[i];
			double  l  = (int)(last & 0x0F) * 0.1 * Math.pow(10, power);
			double l2 = (int)(last & 0xF0) * 0.1 * Math.pow(10,  power+1);
			result += l2 + l;
			power = power + 2;
		}
		return result;
	}
	
	public static int getCommandFromPacket(BMSPacket packetSending) {
		if(packetSending == null){
			return -1;
		}else if(packetSending.getHeader() == null){
			return -1;
		}else if(packetSending.getHeader().getCommandId() == null){
			return -1;
		}else{
			BMSCommand c = packetSending.getHeader().getCommandId();
			byte[] command = c.getCommand();
			long result = ((command[0] << 8) + (command[1] & 0xff)) & 0xffffffff;
			Integer intresult = (int)result;
			return intresult;
		}
	}
	
	public static String MD5(String text) {
		String original = text;
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("MD5");
			md.update(original.getBytes());
			byte[] digest = md.digest();
			StringBuffer sb = new StringBuffer();
			for (byte b : digest) {
				sb.append(Integer.toHexString((int) (b & 0xff)));
			}
			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	public static short hasAEStartIndex(byte[] data){
		short count = 0;
		for(int i=0; i<data.length; i++){
			if(data[i] == (byte)0xAE){
				count++;
			}
		}
		Log.e("BMSUtil", "contain the starting index header (count):" + count);
		return count;
	}
	
	public static short hasAFEndIndex(byte[] data){
		short count = 0;
		for(int i=0; i<data.length; i++){
			if(data[i] == (byte)0xAF){
				count++;
			}
		}
		Log.e("BMSUtil", "contain the ending index header (count):" + count);
		return count;
	}

	public static int indexOfBAIndex(byte[] data){
		int index = 0;
		for(int i=0; i<data.length; i++){
			if(data[i] == (byte)0xBA){
				if(i<(data.length-1)){
					if(data[i+1] == (byte)0xBA){
						index = i;
						break;
					}
				}
			}
		}
		Log.e("BMSUtil", "contain the BA indexAt:" + index);
		return index;
	}
	
	public static byte[] extractData(byte[] data){
			int startIndex = -1;
			int endIndex = -1;
			for(int i=0; i<data.length;i++){
				if((byte)0xAE == data[i]){
					if(i+1 < data.length && (byte)0xAE == data[i+1]){
						startIndex = i+2;
						break;
					}
				}
			}
			if(startIndex == -1){
				Log.e("PacketBuilder","Received packet not a valid packet, cannot identity the start Index");
				return null;
			}
			for(int i=startIndex; i<data.length;i++){
				if((byte)0xAF == data[i]){
					if(i+1 < data.length && (byte)0xAF == data[i+1]){
						endIndex = i;
						break;
					}
				}
			}
			if(endIndex == -1){
				Log.e("PacketBuilder","Received packet not a valid packet, cannot identity the end Index");
				return null;
			}
			int dataLength = endIndex - startIndex;
//			if(dataLength < 10){
//				Log.e("PacketBuilder","Received packet not a valid packet, packet too short less than 10 byte content");
//				return null;
//			}
			byte[] packet = new byte[dataLength];
			System.arraycopy(data, startIndex, packet, 0, dataLength);
			return packet;
	}
	
	public static String printByteArrayAsInt(String caller, byte[] data){
		String ret ="";
		for(int i=0;i<data.length;i++){
			ret += Integer.toHexString((int)(data[i] & 0xFF))+",";
			Log.i(caller, "Byte "+ i + " is " + Integer.toHexString((int)(data[i] & 0xFF)));
		}
		return ret;
	}
	
	public static int getBitValueForByte(byte b, int bitPosition){
		return (b >> bitPosition) & 1;
	}
	
	public static int getSign(byte b){
		int sign = (int)((b >> 7) & 1);
		if(sign == 1)
			return -1;
		else
			return 1;
	}
	
	public static int convertByteToBCD(byte b){
		byte one = (byte) ((b >> 4) & (byte)0x0F);
		byte two = (byte)(b&(byte)0x0F);
		return (int)one*10 + (int)two;
	}
	
	public static byte[] convertIntToBCDBytes(int value){
		int length = new String(value+"").length();
		if(length <= 2){
			int first = value/10;
			int second = value%10;
			byte[] b = new byte[]{(byte) (first << 4 | second)};
			return b;
		}else if(length <= 4){
			int first = (value/1000)%10;
			int second = (value/100)%10;
			int third = (value/10)%10;
			int fourth = value%10;
			byte[] b = new byte[]{(byte) (first << 4 | second), (byte) (third << 4 | fourth)};
			return b;
		}else{
			int prefirst = value/10000;
			int first = (value/1000)%10;
			int second = (value/100)%10;
			int third = (value/10)%10;
			int fourth = value%10;
			byte[] b = new byte[]{(byte)prefirst,(byte) (first << 4 | second), (byte) (third << 4 | fourth)};
			return b;
		}
	}
	
	/**
	 * This method converts dp unit to equivalent pixels, depending on device density. 
	 * 
	 * @param dp A value in dp (density independent pixels) unit. Which we need to convert into pixels
	 * @param context Context to get resources and device specific display metrics
	 * @return A float value to represent px equivalent to dp depending on device density
	 */
	public static float convertDpToPixel(float dp, Context context){
	    Resources resources = context.getResources();
	    DisplayMetrics metrics = resources.getDisplayMetrics();
	    float px = dp * (metrics.densityDpi / 160f);
	    return px;
	}

	/**
	 * This method converts device specific pixels to density independent pixels.
	 * 
	 * @param px A value in px (pixels) unit. Which we need to convert into db
	 * @param context Context to get resources and device specific display metrics
	 * @return A float value to represent dp equivalent to px value
	 */
	public static float convertPixelsToDp(float px, Context context){
	    Resources resources = context.getResources();
	    DisplayMetrics metrics = resources.getDisplayMetrics();
	    float dp = px / (metrics.densityDpi / 160f);
	    return dp;
	}
}
