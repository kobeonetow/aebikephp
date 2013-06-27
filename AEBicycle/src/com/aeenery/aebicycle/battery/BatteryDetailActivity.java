package com.aeenery.aebicycle.battery;

import com.aeenery.aebicycle.R;
import com.aeenery.aebicycle.battery.BluetoothChat.StateReceiver;
import com.aeenery.aebicycle.bms.BMSUtil;
import com.aeenery.aebicycle.bms.RequestController;
import com.aeenery.aebicycle.bms.models.BMSCommand;
import com.aeenery.aebicycle.entry.BicycleUtil;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class BatteryDetailActivity extends Activity{
	
	 // Debugging
    private static final String TAG = "BatteryDetailActivity";
    private static final boolean D = true;


    private Button btnGetHardwareVersion;
    private Button btnGetSoftwareVersion;
    private Button btnGetSystemInfo;
    private Button btnEmptyByte;
    
    private Button btnGetDeviceSerialNumber;
    private Button btnGetConfigurationInfo;
    private Button btnGetBatteryGroupInfo;

//    private Button sendMessageBT;
//    private EditText sendMessageBTContain;
    
    private SharedPreferences sharedPreferences = null;
    private StateReceiver receiver = null;
    
    private TextView textHardwareVersion;
    private TextView textSoftwareVersion;
    private TextView textSystemInfo;
    private TextView textDeviceSerialNumber;
    private TextView textConfigurationInfo;
    private TextView textBatteryGroupInfo;
    
    // Local Bluetooth adapter
    private BluetoothAdapter mBluetoothAdapter = null;
    
    private RequestController controller = null;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(D) Log.e(TAG, "+++ ON CREATE +++");
        setContentView(R.layout.activity_battery_detail);

        // Get local Bluetooth adapter
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // If the adapter is null, then Bluetooth is not supported
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        
        init();
        sharedPreferences = this.getSharedPreferences("aebt", MODE_PRIVATE);
        registerStateReceiver();
    }

    
    private void registerStateReceiver(){
    	receiver = new StateReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(BicycleUtil.BATTERY_STATE_UPDATE);
        filter.addAction(BMSUtil.BATTERY_UPDATE_FAIL_OVER_TIMEOUT);
        this.registerReceiver(receiver, filter);
    }
    
    private void init(){
    	controller = RequestController.getRequestController(this);
    	btnGetHardwareVersion = (Button)findViewById(R.id.btnGetHardwareVersion);
    	btnGetSoftwareVersion = (Button)findViewById(R.id.btnGetSoftwareVersion);
    	btnGetSystemInfo = (Button)findViewById(R.id.btnGetSystemInfo);
    	btnEmptyByte = (Button)findViewById(R.id.btnEmptyByte);
    	btnGetDeviceSerialNumber = (Button)findViewById(R.id.btnGetDeviceSerialNumber);
    	btnGetConfigurationInfo = (Button)findViewById(R.id.btnGetConfigurationInfo);
    	btnGetBatteryGroupInfo = (Button)findViewById(R.id.btnGetBatteryGroupInfo);
    	
    	textHardwareVersion = (TextView)findViewById(R.id.textHardwareVersion);
    	textSoftwareVersion = (TextView)findViewById(R.id.textSoftwareVersion);
    	textSystemInfo = (TextView)findViewById(R.id.textSystemInfo);
    	textDeviceSerialNumber = (TextView)findViewById(R.id.textDeviceSerialNumber);
    	textConfigurationInfo = (TextView)findViewById(R.id.textConfigurationInfo);
    	textBatteryGroupInfo = (TextView)findViewById(R.id.textBatteryGroupInfo);
    	
//    	sendMessageBTContain = (EditText)findViewById(R.id.sendMessageBTContain);
    	
    	setAdapter();
    }
    
    private void setAdapter(){
    	OnClickListener click = new OnClickListener(){
			@Override
			public void onClick(View v) {
				int id = v.getId();
				int commandId = 0xFFFFFF;
				switch(id){
				case R.id.btnGetHardwareVersion:
					commandId = BMSUtil.COMMAND_GET_HARDWARE_VERSION;
					controller.sendRequestPacket(new BMSCommand(commandId,BMSUtil.COMMAND_GET_HARDWARE_VERSION_REPLY),false);
					break;
				case R.id.btnGetSoftwareVersion:
					commandId = BMSUtil.COMMAND_GET_SOFTWARE_VERSION;
					controller.sendRequestPacket(new BMSCommand(commandId, BMSUtil.COMMAND_GET_SOFTWARE_VERSION_REPLY),false);
					break;
				case R.id.btnGetSystemInfo:
					commandId = BMSUtil.COMMAND_GET_SYSTEM_INFO;
					controller.sendRequestPacket(new BMSCommand(commandId, BMSUtil.COMMAND_GET_SYSTEM_INFO_REPLY),false);
					break;
				case R.id.btnEmptyByte:
					controller.sendEmptyByte();
					break;
				case R.id.btnGetDeviceSerialNumber:
					controller.sendRequestPacket(new BMSCommand(BMSUtil.COMMAND_GET_DEVICE_SERIAL_NUMBER, BMSUtil.COMMAND_GET_DEVICE_SERIAL_NUMBER_REPLY),false);
					break;
				case R.id.btnGetConfigurationInfo:
					controller.sendRequestPacket(new BMSCommand(BMSUtil.COMMAND_GET_SETTING_INFO, BMSUtil.COMMAND_GET_SETTING_INFO_REPLY),false);
					break;
				case R.id.btnGetBatteryGroupInfo:
					controller.sendRequestPacket(new BMSCommand(BMSUtil.COMMAND_GET_BATTERY_INFO, BMSUtil.COMMAND_GET_BATTERY_INFO_REPLY),false);
					break;
				default:
					Toast.makeText(BatteryDetailActivity.this, "Cannot update information. Resources not found", Toast.LENGTH_LONG).show();
				}
					
			}
    		
    	};
    	btnGetHardwareVersion.setOnClickListener(click);
    	btnGetSoftwareVersion.setOnClickListener(click);
    	btnGetSystemInfo.setOnClickListener(click);
    	btnEmptyByte.setOnClickListener(click);
    	btnGetDeviceSerialNumber.setOnClickListener(click);
    	btnGetConfigurationInfo.setOnClickListener(click);
    	btnGetBatteryGroupInfo.setOnClickListener(click);
    }
    
    @Override
    public void onStart() {
        super.onStart();
        if(D) Log.e(TAG, "++ ON START ++");
    }

    @Override
    public synchronized void onPause() {
        super.onPause();
        if(D) Log.e(TAG, "- ON PAUSE -");
    }

    @Override
    public void onStop() {
        super.onStop();
        if(D) Log.e(TAG, "-- ON STOP --");
    }

    @Override
    public void onDestroy() {
    	if(receiver != null)
    		this.unregisterReceiver(receiver);
        super.onDestroy();
        // Stop the Bluetooth chat services
//        if (mChatService != null) mChatService.stop();
//        if(s != null) s.stopSelf();
        
        if(D) Log.e(TAG, "--- ON DESTROY ---");
    }

    private void ensureDiscoverable() {
        if(D) Log.d(TAG, "ensure discoverable");
        if (mBluetoothAdapter.getScanMode() !=
            BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            startActivity(discoverableIntent);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(D) Log.d(TAG, "onActivityResult " + resultCode);
        switch (requestCode) {
        case BicycleUtil.REQUEST_CONNECT_DEVICE:
            // When DeviceListActivity returns with a device to connect
            if (resultCode == Activity.RESULT_OK) {
                // Get the device MAC address
                String address = data.getExtras()
                                     .getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
                // Get the BLuetoothDevice object
//                BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
                // Attempt to connect to the device
                Intent intent = new Intent(BicycleUtil.CONNECT_DEVICE);
                intent.putExtra("address", address);
                sendBroadcast(intent);
            }
            break;
        }
    }

    
    private void discoverDevices(){
    	 // Launch the DeviceListActivity to see devices and do scan
        Intent serverIntent = new Intent(this, DeviceListActivity.class);
        startActivityForResult(serverIntent, BicycleUtil.REQUEST_CONNECT_DEVICE);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.scan:
        	Intent stopConnecting = new Intent(BicycleUtil.STOP_CONNECT_DEVICE);
        	BatteryDetailActivity.this.sendBroadcast(stopConnecting);
        	discoverDevices();
            return true;
        case R.id.discoverable:
            // Ensure this device is discoverable by others
            ensureDiscoverable();
            return true;
        }
        return false;
    }

    class StateReceiver extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if(D) Log.i(TAG, "Action received:"+action);
			if(action.equals(BMSUtil.BATTERY_UPDATE_FAIL_OVER_TIMEOUT)){
				if(D) Log.e(TAG, "Timeout over limits, continue to next packet");
			}else if(action.equals(BicycleUtil.BATTERY_STATE_UPDATE)){
				textHardwareVersion.setText(sharedPreferences.getString("0003-1", "h") + " " 
						+ sharedPreferences.getString("0003-2", "v") + " "
						+ sharedPreferences.getString("0003-3", "e") + " "
						+ sharedPreferences.getString("0003-4", "n"));
				textSoftwareVersion.setText(sharedPreferences.getString("0004-1", "h") + " " 
						+ sharedPreferences.getString("0004-2", "v") + " "
						+ sharedPreferences.getString("0004-3", "e") + " "
						+ sharedPreferences.getString("0004-4", "n"));
				textSystemInfo.setText(sharedPreferences.getString("0001-1", ""));
				
				textDeviceSerialNumber.setText(sharedPreferences.getString("0007-1", " "));
				
				textConfigurationInfo.setText(sharedPreferences.getString("0009-1", " "));
				textBatteryGroupInfo.setText(sharedPreferences.getString("0030-1", "g") + " "
						+ sharedPreferences.getString("0030-2", "g") + " "
						+sharedPreferences.getString("0030-3", "g") + " ");
			}
		}
    	
    }
}
