/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.aeenery.aebicycle.battery;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.aeenery.aebicycle.DevelopingActivity;
import com.aeenery.aebicycle.R;
import com.aeenery.aebicycle.entry.BicycleUtil;

/**
 * This is the main Activity that displays the current chat session.
 */
public class BluetoothChat extends Activity {
    // Debugging
    private static final String TAG = "BluetoothChat";
    private static final boolean D = true;

//    // Message types sent from the BluetoothChatService Handler
//    public static final int MESSAGE_STATE_CHANGE = 1;
//    public static final int MESSAGE_READ = 2;
//    public static final int MESSAGE_WRITE = 3;
//    public static final int MESSAGE_DEVICE_NAME = 4;
//    public static final int MESSAGE_TOAST = 5;

//    // Key names received from the BluetoothChatService Handler
//    public static final String DEVICE_NAME = "device_name";
//    public static final String TOAST = "toast";

//    // Intent request codes
//    private static final int REQUEST_CONNECT_DEVICE = 1;
//    private static final int REQUEST_ENABLE_BT = 2;

//    // Layout Views
//    private TextView mTitle;
//    private ListView mConversationView;
//    private EditText mOutEditText;
//    private Button mSendButton;

    private Button btnFindDevice;
    private Button btnUpdateAll;
    private Button functions;
    private Button reconnectBT;
    private Button stopServices;
    
    private Button sendMessageBT;
    private EditText sendMessageBTContain;
    
    private SharedPreferences sharedPreferences = null;
    private StateReceiver receiver = null;
    
    private TextView b_name;
    private TextView b_voltage;
    private TextView b_time;
    private TextView b_status;
    private TextView b_conn;
    private TextView b_model;
    private TextView b_current;
    private TextView b_lvl;
    private TextView b_power;
    
    // Local Bluetooth adapter
    private BluetoothAdapter mBluetoothAdapter = null;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(D) Log.e(TAG, "+++ ON CREATE +++");

        // Set up the window layout
//        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_battery);
//        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.custom_title);

//        // Set up the custom title
//        mTitle = (TextView) findViewById(R.id.title_left_text);
//        mTitle.setText(R.string.app_name);
//        mTitle = (TextView) findViewById(R.id.title_right_text);

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
        this.registerReceiver(receiver, filter);
    }
    
    private void init(){
    	btnFindDevice = (Button)findViewById(R.id.btnFindDevice);
    	btnUpdateAll = (Button)findViewById(R.id.btnUpdateAll);
    	functions = (Button)findViewById(R.id.functions);
    	reconnectBT = (Button)findViewById(R.id.reconnectBT);
    	sendMessageBT = (Button)findViewById(R.id.sendMessageBT);
    	stopServices = (Button)findViewById(R.id.stopServices);
    	
    	b_name = (TextView)findViewById(R.id.b_name);
    	b_conn = (TextView)findViewById(R.id.b_conn);
    	b_current = (TextView)findViewById(R.id.b_current);
    	b_lvl = (TextView)findViewById(R.id.b_lvl);
    	b_model = (TextView)findViewById(R.id.b_model);
    	b_power = (TextView)findViewById(R.id.b_power);
    	b_status = (TextView)findViewById(R.id.b_status);
    	b_time = (TextView)findViewById(R.id.b_time);
    	b_voltage = (TextView)findViewById(R.id.b_voltage);
    	
    	sendMessageBTContain = (EditText)findViewById(R.id.sendMessageBTContain);
    	
    	setAdapter();
    }
    
    private void setAdapter(){
    	btnFindDevice.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent service = new Intent(BluetoothChat.this, BluetoothService.class);
			    BluetoothChat.this.startService(service);
			}
		});
    	
    	btnUpdateAll.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!mBluetoothAdapter.isEnabled()) {
		            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
		            startActivityForResult(enableIntent, BicycleUtil.REQUEST_ENABLE_BT);
		        // Otherwise, setup the chat session
		        }
			}
		});
    	
    	functions.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent service = new Intent(BluetoothChat.this, BluetoothService.class);
				BluetoothChat.this.stopService(service);
				if(BluetoothAdapter.getDefaultAdapter().isEnabled()){
					BluetoothAdapter.getDefaultAdapter().disable();
				}
			}
		});
    	
    	reconnectBT.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				 Intent intent = new Intent(BicycleUtil.RECONNECT_BT);
	             sendBroadcast(intent);
			}
		});
    	
    	sendMessageBT.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String text = sendMessageBTContain.getText().toString();
				sendMessageBTContain.setText("");
				Intent sendMsg = new Intent(BicycleUtil.BT_SEND_MSG);
				sendMsg.putExtra(BicycleUtil.BT_SEND_MSG, text);
				sendBroadcast(sendMsg);
			}
		});
    	
    	stopServices.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(BicycleUtil.STOP_BT_SERVICE);
	            sendBroadcast(intent);
			}
		});
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
    		unregisterReceiver(receiver);
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
        	BluetoothChat.this.sendBroadcast(stopConnecting);
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
			if(action.equals(BicycleUtil.BATTERY_STATE_UPDATE)){
			    b_name.setText(sharedPreferences.getString("b_name", ""));
			    b_voltage.setText(sharedPreferences.getString("b_voltage", ""));
			    b_time.setText(sharedPreferences.getString("b_time", ""));
			    b_status.setText(sharedPreferences.getString("b_status", ""));
			    b_conn.setText(sharedPreferences.getString("b_conn", ""));
			    b_model.setText(sharedPreferences.getString("b_model", ""));
			    b_current.setText(sharedPreferences.getString("b_current", ""));
			    b_lvl.setText(sharedPreferences.getString("b_lvl", ""));
			    b_power.setText(sharedPreferences.getString("b_power", ""));
			}
		}
    	
    }
}