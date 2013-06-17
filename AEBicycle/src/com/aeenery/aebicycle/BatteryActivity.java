package com.aeenery.aebicycle;

//import com.aeenery.aebicycle.battery.BlueToothConnectionThread;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.aeenery.aebicycle.battery.BluetoothChatService;
import com.aeenery.aebicycle.battery.DeviceListActivity;
import com.aeenery.aebicycle.entry.BicycleUtil;

public class BatteryActivity extends BaseActivity {

	public BluetoothAdapter mBluetoothAdapter;
//	private BlueToothConnectionThread blue2connThread;
	private BluetoothChatService btThread;
	private Button btnFindDevice;
	private Button btnUpdateAll;
	
	// Create a BroadcastReceiver for ACTION_FOUND
//	private BroadcastReceiver mReceiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_battery);
		btnFindDevice = (Button)findViewById(R.id.btnFindDevice);
		btnUpdateAll = (Button)findViewById(R.id.btnUpdateAll);
		setAdapter();		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_battery, menu);
		return true;
	}
	
	private void setAdapter(){
		btnFindDevice.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(BatteryActivity.this, DeviceListActivity.class);
				BatteryActivity.this.startActivityForResult(intent, BicycleUtil.RESULT_SCAN_DEVICE);
				return;
			}
		});
		btnUpdateAll.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
			}
		});
	}

	
	
    // The Handler that gets information back from the BluetoothChatService
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case BicycleUtil.MESSAGE_STATE_CHANGE:
                switch (msg.arg1) {
                case BluetoothChatService.STATE_CONNECTED:
//                    mTitle.setText(R.string.title_connected_to);
//                    mTitle.append(mConnectedDeviceName);
//                    mConversationArrayAdapter.clear();
                    break;
                case BluetoothChatService.STATE_CONNECTING:
//                    mTitle.setText(R.string.title_connecting);
                    break;
                case BluetoothChatService.STATE_NONE:
//                    mTitle.setText(R.string.title_not_connected);
                    break;
                }
                break;
            case BicycleUtil.MESSAGE_WRITE:
                byte[] writeBuf = (byte[]) msg.obj;
                // construct a string from the buffer
                String writeMessage = new String(writeBuf);
//                mConversationArrayAdapter.add("Me:  " + writeMessage);
                break;
            case BicycleUtil.MESSAGE_READ:
                byte[] readBuf = (byte[]) msg.obj;
                // construct a string from the valid bytes in the buffer
                String readMessage = new String(readBuf, 0, msg.arg1);
//                mConversationArrayAdapter.add(mConnectedDeviceName+":  " + readMessage);
                break;
            case BicycleUtil.MESSAGE_DEVICE_NAME:
                // save the connected device's name
//                mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
//                Toast.makeText(getApplicationContext(), "Connected to "
//                               + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                break;
            case BicycleUtil.MESSAGE_TOAST:
                Toast.makeText(getApplicationContext(), msg.getData().getString(BicycleUtil.TOAST),
                               Toast.LENGTH_SHORT).show();
                break;
            }
        }
    };
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		switch(requestCode){
		case BicycleUtil.REQUEST_ENABLE_BT:
			if(resultCode == RESULT_OK){
				Toast.makeText(this,"qiyong",Toast.LENGTH_LONG);
				setupService();
			}else if(resultCode == RESULT_CANCELED){
				Toast.makeText(this, "钃濈墮鏈惎鐢紝椤甸潰鏃犳硶姝ｅ父鏄剧ず", Toast.LENGTH_LONG).show();
				this.finish();
				return;
			}
			break;
		case BicycleUtil.RESULT_SCAN_DEVICE:
			 if (resultCode == Activity.RESULT_OK) {
	                // Get the device MAC address
	                String address = data.getExtras()
	                                     .getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
	                // Get the BLuetoothDevice object
	                BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
	                // Attempt to connect to the device
	                btThread.connect(device);
	            }
			break;
		default:
			break;
		}
	}
	
	private void setupService(){
		if (btThread == null) {
    		// Initialize the BluetoothChatService to perform bluetooth connections
    		btThread = new BluetoothChatService(this, mHandler);
//    		btThread.start();
    	}
	}
	 @Override
	    public void onStart() {
	        super.onStart();
	        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
	        // If BT is not on, request that it be enabled.
	        // setupChat() will then be called during onActivityResult
	        if (!mBluetoothAdapter.isEnabled()) {
	            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
	            startActivityForResult(enableIntent, BicycleUtil.REQUEST_ENABLE_BT);
	        // Otherwise, setup the chat session
	        }else {
	        	setupService();
	        }
	        
	    }

	    @Override
	    public synchronized void onResume() {
	        super.onResume();
	        // Performing this check in onResume() covers the case in which BT was
	        // not enabled during onStart(), so we were paused to enable it...
	        // onResume() will be called when ACTION_REQUEST_ENABLE activity returns.
	        if (btThread != null) {
	            // Only if the state is STATE_NONE, do we know that we haven't started already
	            if (btThread.getState() == BluetoothChatService.STATE_NONE) {
	              // Start the Bluetooth chat services
//	            	btThread.start();
	            }
	        }
	    }
	
	@Override
	protected void onDestroy(){
		if(mBluetoothAdapter != null){
			if(mBluetoothAdapter.isDiscovering()){
				mBluetoothAdapter.cancelDiscovery();
			}
			if(mBluetoothAdapter.isEnabled()){
				mBluetoothAdapter.disable();
			}
		}
		// Stop the Bluetooth chat services
        if (btThread != null) 
        	btThread.stop();
//		unregisterReceiver(mReceiver);
		super.onDestroy();
	}
	
	@Override
	public void onBackPressed(){
		super.onBackPressed();
	}
}
