package com.aeenery.aebicycle.battery;

import java.lang.reflect.Array;

import com.aeenery.aebicycle.bms.BMSController;
import com.aeenery.aebicycle.bms.BMSUtil;
import com.aeenery.aebicycle.bms.TimeOutThread;
import com.aeenery.aebicycle.bms.models.BMSGeneralReplyPacket;
import com.aeenery.aebicycle.bms.models.BMSPacket;
import com.aeenery.aebicycle.entry.BicycleUtil;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

public class BluetoothService extends Service implements BluetoothServiceStatus{

	public final static String ServiceAction = "com.aeenery.aebicycle.battery.Bluetooth_Service";
	public static int state = SERVICE_NOT_STARTED;
	
	// Debugging
	private static final String TAG = "BluetoothService";
	private static final boolean D = true;

	// Name of the connected device
	private String mConnectedDeviceName = null;

	// Local Bluetooth adapter
	private static BluetoothAdapter mBluetoothAdapter = null;
	
	// Member object for the chat services
	private BluetoothChatService mChatService = null;
	private BluetoothDevice device = null;

	private final IBinder mBinder = new MyBinder();

	private SharedPreferences sharedPreferences;

	public static boolean connected = false;
	private boolean reconnect = true;
	
	private TimeOutThread timeoutThread = null;
	private BMSController controller;
	
	protected short AFIndex = 0;
	protected byte[] receiveByteArray;
	protected BMSPacket receivedPacket;
	
	// Receiver
	BTSReceiver receiver = null;
	
	// Attemp to Connect thread
	public AttempConnectThread act= null; 
	public BluetoothDevice currentDevice = null;
	
	@Override
	public IBinder onBind(Intent arg0) {
		if (D)
			Log.e(TAG, "+++ ON BIND +++");
		return mBinder;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		if (D)
			Log.e(TAG, "+++ ON CREATE +++");
		// Get local Bluetooth adapter
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

		// Initialise sharedPreferences
		sharedPreferences = this.getSharedPreferences("aebt", MODE_PRIVATE);

		// Initiailise the service model
		setupChat();
		registerActionToReceiver();
		controller = BMSController.getInitiliseController(this);
		
		// If the adapter is null, then Bluetooth is not supported
		if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
			Toast.makeText(this,"蓝牙未启用",Toast.LENGTH_LONG).show();
			return;
		}
	}

	private void setupChat() {
		Log.d(TAG, "setupChat()");
		// Initialize the BluetoothChatService to perform bluetooth connections
		mChatService = new BluetoothChatService(this, mHandler);
	}
	
	private void registerActionToReceiver(){
		if(receiver == null){
			receiver = new BTSReceiver();
		}
		IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
		filter.addAction(BicycleUtil.CONNECT_DEVICE);
		filter.addAction(BicycleUtil.STOP_BT_SERVICE);
		filter.addAction(BicycleUtil.BT_SEND_MSG);
		filter.addAction(BicycleUtil.RECONNECT_BT);
		filter.addAction(BicycleUtil.STOP_CONNECT_DEVICE);
		filter.addAction(BMSUtil.PACKET_TIMEOUT_ACTION);
		this.registerReceiver(receiver, filter);
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (D)	Log.e(TAG, "+++ ON START COMMAND +++");
		if(mBluetoothAdapter != null && mBluetoothAdapter.isEnabled() && !connected){
			getDevice(); //Get the stored battery Mac address from sharePreferences to current device
			startConnectionToDevice(currentDevice); 
		}
		String action = intent.getAction();
		if(action == null){
			//Starting with no action, purpose to just start the service
		}else if(action.equals(BicycleUtil.BT_SEND_MSG)){
			byte[] messages = intent.getByteArrayExtra(BicycleUtil.BT_SEND_MSG);
			String packetId = intent.getStringExtra(BicycleUtil.BT_SEND_MSG_ID);
			sendMessage(messages, packetId);
		}else if(action.equals(BicycleUtil.BT_SEND_EMPTY_BYTES)){
			byte[] messages = {0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00
					,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00
					,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00
					,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00
					,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00};
			sendMessage(messages, null);
		}
		
		else{
			//Do nothing
		}
		return START_NOT_STICKY; // run until explicitly stopped.
	}

	private BluetoothDevice getDevice() {
		if(currentDevice == null){
			String address = sharedPreferences.getString("deviceAddress", "");
			if (address.equals("")){
				Toast.makeText(this, "No device available in storge",  Toast.LENGTH_LONG).show();
				if(D) Log.i(TAG,"Device not in saved");
				return null;
			}
			else
				currentDevice = mBluetoothAdapter.getRemoteDevice(address);
			if(currentDevice == null){
				Toast.makeText(this, "No device found with address :"+address,  Toast.LENGTH_LONG).show();
			}
		}
		if(D) Log.i(TAG,"Device:"+currentDevice.getAddress() + " ,"+currentDevice.getName());
		return currentDevice;
	}
	
	public synchronized void startConnectionToDevice(BluetoothDevice _device) {
		if(D) Log.i(TAG, "START CONNECTION TO DEVICE");

		if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
			Toast.makeText(this, "蓝牙未启用", Toast.LENGTH_LONG).show();
			return;
		}
		if(currentDevice == null){
			Toast.makeText(this, "未找到设备", Toast.LENGTH_LONG).show();
			return;
		}
		//If previously connected before service start, is should be accident and now reconnect to make sure correcly connected
		if(connected){
			mChatService.stop();
		}
		final BluetoothDevice deviceReal = currentDevice;
		if(act != null){
			act.cancel();
			act = null;
		}
		act = new AttempConnectThread(deviceReal);
		act.start();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		// Stop the Bluetooth chat services
		if (mChatService != null)
			mChatService.stop();
		if(act != null){
			act.cancel();
			act = null;
		}
		if(receiver != null)
			unregisterReceiver(receiver);
		if (D)
			Log.e(TAG, "--- ON DESTROY ---");
	}

	/**
	 * Sends a message.
	 * 
	 * @param message
	 *            A string of text to send.
	 */
	public void sendMessage(byte[] message, String msgId) {
		// Check that we're actually connected before trying anything
		if (mChatService.getState() != BluetoothChatService.STATE_CONNECTED) {
			Toast.makeText(this, "Not connected", Toast.LENGTH_SHORT).show();
			return;
		}
		// Check that there's actually something to send
		if (message != null) {
			// Get the message bytes and tell the BluetoothChatService to write
			byte[] send = message;
			mChatService.write(send);
			if(msgId != null){
				timeoutThread = new TimeOutThread(this, msgId);
				timeoutThread.start();
			}
		}
	}

	public void checkReceivedData(byte[] data){
		if(receiveByteArray == null){
			receiveByteArray = new byte[data.length];
			System.arraycopy(data, 0, receiveByteArray, 0, data.length);
		}else{
			byte[] newarray = new byte[receiveByteArray.length + data.length];
			System.arraycopy(receiveByteArray, 0, newarray, 0, receiveByteArray.length);
			System.arraycopy(data, 0, newarray, receiveByteArray.length, data.length);
			receiveByteArray = newarray;
		}
		AFIndex += BMSUtil.hasAFEndIndex(data);
		if(AFIndex >= 2){
			byte[] packet = BMSUtil.extractData(receiveByteArray);
//			BMSUtil.printByteArrayAsInt(TAG, receiveByteArray);
			if(packet == null){
				if(D) Log.e(TAG,"Invalid packet, ignore and return back to service");
			}else{
				BMSGeneralReplyPacket generalPacket = new BMSGeneralReplyPacket(packet);
				if(D) Log.e(TAG,"Universal packet received :"+(int)BMSUtil.COMMAND_UNIVERSAL + " matching "+ generalPacket.command.getCommandAsByteInt());
				if((int)BMSUtil.COMMAND_UNIVERSAL == generalPacket.command.getCommandAsByteInt()){
					if(D) Log.e(TAG,"Universal packet received");
					if(controller.isCorrectResponce(generalPacket)){
						timeoutThread.cancel();
						controller.handlerReceivePacket(generalPacket);
					}else{
						if(D) Log.e(TAG,"Invalid general reply packet, is not a correct response");
					}
				}else{
					BMSPacket bmsPacket = new BMSPacket(packet);
					if(controller.isCorrectResponce(bmsPacket)){
						timeoutThread.cancel();
						controller.handlerReceivePacket(bmsPacket);
					}else{
						if(D) Log.e(TAG,"Invalid packet, is not a correct response");
					}
				}
			}
			resetReceiveBuffer();
		}
	}
	
	// The Handler that gets information back from the BluetoothChatService
	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case BicycleUtil.MESSAGE_STATE_CHANGE:
				if (D)
					Log.i(TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);
				switch (msg.arg1) {
				case BluetoothChatService.STATE_CONNECTED:
					setConnected(true);
					sendBroadcast(new Intent(BicycleUtil.DEVICE_CONNECTED));
					if(act != null)
						act.cancel();
					break;
				case BluetoothChatService.STATE_DISCONNECT:
					setConnected(false);
					sendBroadcast(new Intent(BicycleUtil.DEVICE_DISCONNECTED));
					Log.i(TAG,"Reconnect is:"+reconnect);
					if(reconnect)
						startConnectionToDevice(currentDevice);
					else
						reconnect = true;
					break;
				case BluetoothChatService.STATE_CONNECTING:
					break;
				case BluetoothChatService.STATE_NONE:
					setConnected(false);
					sendBroadcast(new Intent(BicycleUtil.DEVICE_DISCONNECTED));
					break;
				}
				break;
			case BicycleUtil.MESSAGE_WRITE:
				byte[] writeBuf = (byte[]) msg.obj;
				// construct a string from the buffer
				String writeMessage = new String(writeBuf);
				Log.i(TAG,"Write:--" +writeMessage);
//				Toast.makeText(BluetoothService.this, writeMessage, Toast.LENGTH_SHORT).show();
				break;
			case BicycleUtil.MESSAGE_READ:
				byte[] readBuf = (byte[]) msg.obj;
				// construct a string from the valid bytes in the buffer
				String readMessage = new String(readBuf, 0, msg.arg1);
				byte[] data = new byte[msg.arg1];
				System.arraycopy(readBuf, 0, data, 0, msg.arg1);
				Log.i(TAG, "Byte count : "+ msg.arg1);
//				Toast.makeText(BluetoothService.this, "Receive msg:"+readMessage, Toast.LENGTH_LONG).show();
				String displaydata = BMSUtil.printByteArrayAsInt(TAG, data);
//				Toast.makeText(BluetoothService.this, "Receive msg:"+displaydata, Toast.LENGTH_LONG).show();
				checkReceivedData(data);
				break;
			case BicycleUtil.MESSAGE_DEVICE_NAME:
				// save the connected device's name
				mConnectedDeviceName = msg.getData().getString(
						BicycleUtil.DEVICE_NAME);
				String address = msg.getData().getString("batteryAddress");
				writeToSharedPreferences("batteryAddress", address);
				Toast.makeText(getApplicationContext(),
						"Connected to " + mConnectedDeviceName + " ("+address+")",
						Toast.LENGTH_SHORT).show();
				break;
			case BicycleUtil.MESSAGE_TOAST:
				Log.i(TAG,"Msg:--" +msg.getData().getString(BicycleUtil.TOAST));
//				Toast.makeText(getApplicationContext(),
//						msg.getData().getString(BicycleUtil.TOAST),
//						Toast.LENGTH_SHORT).show();
				break;
			}
		}
	};

	private synchronized void broadcastAction(String action){
		Intent b_action  = new Intent();
		b_action.setAction(action);
		sendBroadcast(b_action);
	}
	

	private void writeToSharedPreferences(String name, String value) {
		Editor editor = sharedPreferences.edit();
		editor.putString(name, value);
		editor.commit();
	}

	private synchronized static void setConnected(boolean value){
		if(D) Log.i(TAG,"Set CONNECTED:"+value);
		connected = value;
	}
	
	public class MyBinder extends Binder {
		public BluetoothService getService() {
			return BluetoothService.this;
		}
	}
	
	//Attemp to connect device every 5 seconds if not connected
	class AttempConnectThread extends Thread{
		BluetoothDevice device = null;
		boolean connect = false;
		
		public AttempConnectThread(BluetoothDevice _device){
			connect = true;
			this.device = _device;
		}
		
		public void run() {
			while (!connected && connect) {
				mChatService.connect(device);
				try {
					sleep(5000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		public void cancel(){
			connect = false;
		}
	}
	
	class BTSReceiver extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if(D) Log.i(TAG,"Action:"+action);
			if(action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)){
				switch(mBluetoothAdapter.getState()){
				case BluetoothAdapter.STATE_OFF:
					BluetoothService.connected = false;
					break;
				case BluetoothAdapter.STATE_ON:
					startConnectionToDevice(currentDevice);
					break;
				}
			}else if(action.equals(BicycleUtil.CONNECT_DEVICE)){
				String address = intent.getStringExtra("deviceAddress");
				writeToSharedPreferences("deviceAddress", address);
				currentDevice = mBluetoothAdapter.getRemoteDevice(address);
				startConnectionToDevice(currentDevice);
			}else if(action.equals(BicycleUtil.STOP_CONNECT_DEVICE)){
				if(act != null && act.isAlive()){
					act.cancel();
//					act = null;
				}
				if(connected){
					mChatService.stop();
					connected  = false;
				}
			}else if(action.equals(BicycleUtil.RECONNECT_BT)){
				if(act != null && act.isAlive()){
					act.cancel();
//					act = null;
				}
				if(connected){
					mChatService.stop();
					connected  = false;
				}
//				startConnectionToDevice(currentDevice);
			}else if(action.equals(BicycleUtil.BT_SEND_MSG)){
				byte[] msg = intent.getByteArrayExtra(BicycleUtil.BT_SEND_MSG);
				String id = intent.getStringExtra(BicycleUtil.BT_SEND_MSG_ID);
				sendMessage(msg, id);
				if(D) Log.i(TAG,"Message sent as byte:"+ msg);
			}else if(action.equals(BicycleUtil.STOP_BT_SERVICE)){
				if(act != null && act.isAlive()){
					act.cancel();
//					act = null;
				}
				if(connected){
					reconnect = false;
					mChatService.stop();
					connected  = false;
				}
			}else if(action.equals(BMSUtil.PACKET_TIMEOUT_ACTION)){
				String timeoutPID = intent.getStringExtra(BicycleUtil.BT_SEND_MSG_ID);
				if(controller.responseTimeOut(timeoutPID)){
					resetReceiveBuffer();
				}
			}
		}
		
	}
	
	/**
	 * Reset the receive buffer to receive new packet
	 */
	private void resetReceiveBuffer(){
		if(D) Log.e(TAG,"Reset received bufferred");
		AFIndex = 0;
		receiveByteArray = null;
	}
}
