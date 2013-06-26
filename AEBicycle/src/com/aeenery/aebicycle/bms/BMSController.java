package com.aeenery.aebicycle.bms;

import com.aeenery.aebicycle.battery.BluetoothService;
import com.aeenery.aebicycle.bms.models.BMSGeneralReplyPacket;
import com.aeenery.aebicycle.bms.models.BMSPacket;
import com.aeenery.aebicycle.bms.models.BatteryGroupCapacitySocPacket;
import com.aeenery.aebicycle.bms.models.BatteryGroupInfoPacket;
import com.aeenery.aebicycle.bms.models.BatteryGroupTemperaturePacket;
import com.aeenery.aebicycle.bms.models.BatteryPeriodicPacket;
import com.aeenery.aebicycle.bms.models.BatteryTemperaturePacket;
import com.aeenery.aebicycle.bms.models.BatteryTimeLeftPacket;
import com.aeenery.aebicycle.bms.models.BatteryVoltageCurrentPacket;
import com.aeenery.aebicycle.bms.models.ConfigurationInfoPacket;
import com.aeenery.aebicycle.bms.models.DeviceSerialNumberPacket;
import com.aeenery.aebicycle.bms.models.HardwareVersionPacket;
import com.aeenery.aebicycle.bms.models.SoftwareVersionPacket;
import com.aeenery.aebicycle.entry.BicycleUtil;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
//import android.os.Handler;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class BMSController implements AvailabilityNotificationCheck{

	private final static String TAG = "BMSController";
	private final static boolean D = false;
	private final static int RETRY_IF_TIMEOUT = 3;
	
	private static BMSController controller;
	
	protected Context context;
	protected PacketBuilder builder = new PacketBuilder();
	protected BMSPacket[] queue = new BMSPacket[200];
	protected int deQueueIndex = 0;
	protected int enQueueIndex = 0;
	protected int trySending = 1;
	protected BMSPacket packetSending = null;
	protected boolean waitingForReply = false;
	protected Thread sendQueueThread = null;
	protected short AEIndex = 0;
	protected short AFIndex = 0;
	protected byte[] receiveByteArray;
	protected BMSPacket receivedPacket;
	protected SharedPreferences sp;
	
	
	public static BMSController getController(){
		return controller;
	}
	public static BMSController getInitiliseController(Context _context){
		if(controller == null)
			controller = new BMSController(_context);
		return controller;
	}
	public PacketBuilder getBuilder(){
		return this.builder;
	}
	
	public BMSController(Context _context){
		if(context == null)
			context = _context;
		if(sendQueueThread == null){
			sendQueueThread = new SendQueueThread();
		}
		if(!sendQueueThread.isAlive()){
			sendQueueThread.start();
		}
		sp = context.getSharedPreferences("aebt", 0);
	}
	
	protected synchronized void waitingForSend(){
		try {
			while(waitingForReply){
					if(D) Log.i(TAG,"Waiting for reply b4 wait is:"+waitingForReply);
					wait();
					if(D) Log.e(TAG,"waitingForSend() Notified");
			}
			waitingForReply = true;
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	protected synchronized void waitingForPacket(){
		try {
			while(packetSending == null){
				deQueue();
				if(packetSending == null){
					wait();
					if(D) Log.e(TAG,"waitingForPacket() Notified");
				}
				
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	protected synchronized void deQueue(){
		if(packetSending == null && queue[deQueueIndex] != null){
			if(D) Log.i(TAG,"deQueue");
			setPacketSending(queue[deQueueIndex]);
			queue[deQueueIndex] = null;
			deQueueIndex = (deQueueIndex + 1)%200;
		}else{
			if(D) Log.i(TAG,"Not deQueue");
		}
		if(D) Log.e(TAG,"Next dequeue index "+ deQueueIndex);
	}
	
	protected synchronized boolean enQueue(BMSPacket packet){
		if(queue[enQueueIndex] == null){
			if(D) Log.i(TAG,"enQueue");
			queue[enQueueIndex] = packet;
			enQueueIndex = (enQueueIndex + 1)%200;
			notifyAll();
//			if(D) Log.i(TAG,"enQueue() Notified All");
			if(D) Log.e(TAG,"Next enqueue index "+ enQueueIndex);
			return true;
		}else{
			Toast.makeText(context, "数据包队列已满，请稍后再继续尝试", Toast.LENGTH_LONG).show();
			return false;
		}
	}
	
	public synchronized boolean isCorrectResponce(BMSPacket packet){
		if(packetSending == null){
			if(D) Log.i(TAG,"Packet sending is null");
			return false;
		}
		if(packetSending.getHeader().getCommandId().getResponseID() == 0xFAAA){
			if(D) Log.i(TAG,"the response id is default");
			return false;
		}
		if(packetSending.getHeader().getCommandId().getResponseID() == packet.getHeader().getCommandId().getCommandAsByteInt()){
			if(D) Log.i(TAG,"the response id matched " + packetSending.getHeader().getCommandId().getResponseID() + " , "+packet.getHeader().getCommandId().getCommandAsByteInt());
			return true;
		}
		if(D) Log.i(TAG,"the response id not matched " + packetSending.getHeader().getCommandId().getResponseID() + " , "+packet.getHeader().getCommandId().getCommandAsByteInt());
		return false;
	}
	
	@Override
	public synchronized void handlerReceivePacket(BMSPacket packet) {
		setPacketSending(null);
		waitingForReply = false;
		resetTimeoutFrequency();
		if(D) Log.e(TAG,"ALREADY set packet sending to null and waiting for reply to false");
		notifyAll();
		updateSharePreferencesRecord(packet);
	}
	

	public synchronized boolean isCorrectResponce(BMSGeneralReplyPacket generalPacket) {
		if(packetSending == null){
			if(D) Log.i(TAG,"Packet sending is null");
			return false;
		}
		if(packetSending.getHeader().getCommandId().getResponseID() == generalPacket.command.getCommandAsByteInt()){
			if(D) Log.i(TAG,"the response id matched " + packetSending.getHeader().getCommandId().getResponseID() + " , "+generalPacket.command.getCommandAsByteInt());
			return true;
		}
		if(D) Log.i(TAG,"the response id not matched " + packetSending.getHeader().getCommandId().getResponseID() + " , "+generalPacket.command.getCommandAsByteInt());
		return false;
	}
	public synchronized void handlerReceivePacket(BMSGeneralReplyPacket generalPacket) {
		setPacketSending(null);
		waitingForReply = false;
		resetTimeoutFrequency();
		if(D) Log.e(TAG,"ALREADY set packet sending to null and waiting for reply to false");
		notifyAll();
//		if(D) Log.e(TAG,"General reply successfully receive and handle detail:"+ generalPacket.getReplyStatus()+"-"+generalPacket.getReplyReason());
		Toast.makeText(context, generalPacket.getReplyStatus()+"-"+generalPacket.getReplyReason(), Toast.LENGTH_SHORT).show();
	}
	
	private void resetTimeoutFrequency(){
		trySending = 0;
	}
	
	private void updateSharePreferencesRecord(BMSPacket packet){
		Editor edit = sp.edit();
		switch(packet.getHeader().getCommandId().getCommandAsByteInt()){
		case BMSUtil.COMMAND_GET_HARDWARE_VERSION_REPLY:
			HardwareVersionPacket hvp = (HardwareVersionPacket)builder.buildReceivedPacket(packet);
			edit.putString("0003-1", hvp.getMainVersionNumber()+"");
			edit.putString("0003-2", hvp.getSubVersionNumber()+"");
			edit.putString("0003-3", hvp.getNumberOfEdit()+"");
			edit.putString("0003-4", hvp.getName());
			break;
		case BMSUtil.COMMAND_GET_SOFTWARE_VERSION_REPLY:
			SoftwareVersionPacket svp = (SoftwareVersionPacket)builder.buildReceivedPacket(packet);
			edit.putString("0004-1", svp.getMainVersionNumber()+"");
			edit.putString("0004-2", svp.getSubVersionNumber()+"");
			edit.putString("0004-3", svp.getNumberOfEdit()+"");
			edit.putString("0004-4", svp.getName());
			break;
		case BMSUtil.COMMAND_GET_SYSTEM_INFO_REPLY:
			break;
		case BMSUtil.COMMAND_GET_DEVICE_SERIAL_NUMBER_REPLY:
			DeviceSerialNumberPacket dsnp= (DeviceSerialNumberPacket)builder.buildReceivedPacket(packet);
			if(D) Log.e(TAG,dsnp.getDeviceSerialNumber());
			edit.putString("0007-1", dsnp.getDeviceSerialNumber());
			break;
		case BMSUtil.COMMAND_GET_SETTING_INFO_REPLY:
			ConfigurationInfoPacket cip = (ConfigurationInfoPacket)builder.buildReceivedPacket(packet);
			edit.putString("0009-1", cip.toString());
			break;
		case BMSUtil.COMMAND_GET_BATTERY_INFO_REPLY:
			BatteryGroupInfoPacket bgip = (BatteryGroupInfoPacket)builder.buildReceivedPacket(packet);
			edit.putString("0030-1", "" + bgip.getBatteryGroupStandardCapacity()+"");
			edit.putString("0030-2", "" + bgip.getBatteryGroupStandardVoltage()+"");
			edit.putString("0030-3", "" + bgip.getBatteryGroupBatteryNumber()+"");
			break;
		case BMSUtil.COMMAND_GET_BATTERY_CAPACITY_AND_SOC_STATUS_REPLY:
			BatteryGroupCapacitySocPacket bgcsp = (BatteryGroupCapacitySocPacket)builder.buildReceivedPacket(packet);
			edit.putString("00A6-1", bgcsp.getAbsoluteSOC()+"");
			edit.putString("00A6-2", bgcsp.getRelativeSOC()+"");
			edit.putString("00A6-3", bgcsp.getCapacityleft()+"");
			edit.putString("00A6-4", bgcsp.getTotalcapacity()+"");
			break;
		case BMSUtil.COMMAND_GET_BATTERY_VOLTAGE_CURRENT_REPLY:
			BatteryVoltageCurrentPacket bvcp = (BatteryVoltageCurrentPacket)builder.buildReceivedPacket(packet);
			edit.putString("00A0-1", bvcp.getVoltage()+"");
			edit.putString("00A0-2", bvcp.getCurrent()+"");
			break;
		case BMSUtil.COMMAND_GET_BATTERY_TEMPERATURE_NOW_DETAIL_REPLY:
			BatteryGroupTemperaturePacket bgtp = (BatteryGroupTemperaturePacket)builder.buildReceivedPacket(packet);
			edit.putString("00A2-1", bgtp.getStatus()+"");
			edit.putString("00A2-2", bgtp.getGoupIndex()+"");
			edit.putString("00A2-3", bgtp.getGroupMemberIndex()+"");
			edit.putString("00A2-4", bgtp.getRequestNumber()+"");
			String bgtptemp = "";
			for(int i=0;i<bgtp.getRequestNumber();i++){
				bgtptemp += bgtp.getTemperature(i) + ",";
			}
			edit.putString("00A2-5", bgtptemp+"");
			break;
		case BMSUtil.COMMAND_GET_BATTERY_LOOP_PERIOD_INFO_REPLY:
			BatteryPeriodicPacket bpp = (BatteryPeriodicPacket)builder.buildReceivedPacket(packet);
			edit.putLong("00A8-1", bpp.getPeriodic());
			break;
		case BMSUtil.COMMAND_GET_BATTERY_TEMPERATURE_REPLY:
			BatteryTemperaturePacket btp = (BatteryTemperaturePacket)builder.buildReceivedPacket(packet);
			edit.putString("0080-1", btp.getHighest() +"");
			edit.putString("0080-2", btp.getLowest()+"");
			break;
		case BMSUtil.COMMAND_GET_BATTERY_TIME_LEFT_REPLY:
			BatteryTimeLeftPacket btlp = (BatteryTimeLeftPacket)builder.buildReceivedPacket(packet);
			edit.putString("00AA-1", btlp.getHour()+"");
			edit.putString("00AA-2", btlp.getMinute()+"");
			break;
		}
		edit.commit();
		Intent intent = new Intent(BicycleUtil.BATTERY_STATE_UPDATE);
		context.sendBroadcast(intent);
	}
	
//	protected Handler mHandler;
	
//	/**
//	 * 新建一个handler，并且赋值到this.mHandler 上
//	 */
//	abstract void createHandler();
	
	private void setPacketSending(BMSPacket packet){
		this.packetSending = packet;
		if(D) Log.e(TAG, "Set packetSending to "+ packet);
	}
	
	/**
	 * 发送数据包
	 */
	public void sendPacket(BMSPacket packet){
		if(!enQueue(packet)){
			Toast.makeText(context, "信息发送失败，可能信息队列过长，稍后再试",  Toast.LENGTH_LONG).show();
		}else{
			if(D) Log.i(TAG,"Inject packet to Queue success:"+packet.getPacketId());
		}
	}
	
	public void sendEmptyByte(){
		Intent intent = new Intent(context, BluetoothService.class);
		intent.setAction(BicycleUtil.BT_SEND_EMPTY_BYTES);
		context.startService(intent);
	}
	
	/**
	 * 接受数据包作出响应
	 */
	public void receivePacket(){
		
	}
	
	@Override
	public synchronized boolean responseTimeOut(String packetId){
//		this.sendEmptyByte();
		if(packetSending == null){
			if(D) Log.e(TAG,"Currently no packet on sender, no packet to compare");
			return true;
		}else if(!packetSending.getPacketId().equals(packetId)){
			if(D) Log.e(TAG,"timeout packetid different from current sending id:("+packetId+","+packetSending.getPacketId()+")");
			return false;
		}
		if(trySending >= RETRY_IF_TIMEOUT){
			Toast.makeText(context, "发送超时3次，放弃该数据包，命令id " + BMSUtil.getCommandFromPacket(packetSending), Toast.LENGTH_LONG).show();
			this.context.sendBroadcast( new Intent(BMSUtil.BATTERY_UPDATE_FAIL_OVER_TIMEOUT));
			setPacketSending(null);
			trySending = 0;
			deQueue();
		}else{
			trySending++;
		}
		waitingForReply = false;
		notifyAll();
		if(D) Log.e(TAG,"responseTimeOut("+(trySending-1)+") Notified All");
		return true;
	}
	
	class SendQueueThread extends Thread{
		@Override
		public void run(){
			while(true){
				waitingForSend();
				waitingForPacket();
				Intent intent = new Intent(context, BluetoothService.class);
				intent.setAction(BicycleUtil.BT_SEND_MSG);
				intent.putExtra(BicycleUtil.BT_SEND_MSG, packetSending.getPacketAsByteArray());
				intent.putExtra(BicycleUtil.BT_SEND_MSG_ID, packetSending.getPacketId());
				context.startService(intent);
				if(D) Log.e(TAG,"Message send to service to sendout thread packet Id:" + packetSending.getPacketId() + " -- " + packetSending.getPacketAsByteArray());
			}
		}
			
	}


}
