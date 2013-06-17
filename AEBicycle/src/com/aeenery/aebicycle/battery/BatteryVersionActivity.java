package com.aeenery.aebicycle.battery;

import com.aeenery.aebicycle.R;
import com.aeenery.aebicycle.battery.BatteryDescriptionActivity.StateReceiver;
import com.aeenery.aebicycle.bms.BMSUtil;
import com.aeenery.aebicycle.bms.ConfigController;
import com.aeenery.aebicycle.bms.RequestController;
import com.aeenery.aebicycle.bms.SendPacketThread;
import com.aeenery.aebicycle.bms.models.BMSCommand;
import com.aeenery.aebicycle.entry.BicycleUtil;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class BatteryVersionActivity extends Activity{

	private final static boolean D = true;
	public final static String TAG = "BatteryDescriptionActivity";
	private final int PERIOD = 5000;

    private static final int DIALOG_TEXT_ENTRY = 7;
    
	private TextView tvHardware;
	private TextView tvSoftware;
	private TextView tvDeviceSerial;
	private TextView tvBatteryInfo;
	private Button btn_version;
	private Button btnDevicename;
	
	private StateReceiver receiver = null;
	private RequestController controller = null;
	private ConfigController configController = null;
	private SharedPreferences sharedPreferences = null;
	
	 @Override
	 protected Dialog onCreateDialog(int id, Bundle b) {
	        switch (id) {
	               case DIALOG_TEXT_ENTRY:
	            // This example shows how to add a custom layout to an AlertDialog
	            LayoutInflater factory = LayoutInflater.from(this);
	            final View textEntryView = factory.inflate(R.layout.alert_dialog_text_entry, null);
	            final EditText etDevicename = (EditText)textEntryView.findViewById(R.id.etdevicename);
	            return new AlertDialog.Builder(BatteryVersionActivity.this)
	                .setIcon(R.drawable.alert_dialog_icon)
	                .setTitle("设备名称")
	                .setView(textEntryView)
	                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
	                    public void onClick(DialogInterface dialog, int whichButton) {
	                    	String name = etDevicename.getText().toString();
	                    	if(D) Log.i(TAG,"Setting device name to "+name);
	                    	configController.sendConfigNamePacket(new BMSCommand(
	                    			BMSUtil.COMMAND_EDIT_BLUETOOTH_DEVICE_NAME,BMSUtil.COMMAND_EDIT_BLUETOOTH_DEVICE_NAME_REPLY), name);
	                    }
	                })
	                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
	                    public void onClick(DialogInterface dialog, int whichButton) {
	                    	
	                    }
	                })
	                .create();
	        }
	        return null;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_battery_version);
		
		registerReceiver();
		loadUIObjects();
	}

	private void registerReceiver() {
		receiver = new StateReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(BicycleUtil.BATTERY_STATE_UPDATE);
		this.registerReceiver(receiver, filter);
	}
	
	private void loadUIObjects() {
		tvHardware = (TextView)findViewById(R.id.tvhardware);
		tvSoftware = (TextView)findViewById(R.id.tvsoftware);
		tvDeviceSerial = (TextView)findViewById(R.id.tvdeviceserial);
		tvBatteryInfo = (TextView)findViewById(R.id.tvbatterygroupinfo);
		btn_version = (Button)findViewById(R.id.btn_version_update);
		btnDevicename = (Button)findViewById(R.id.btndevicename);
		
		controller = RequestController.getRequestController();
		configController = ConfigController.getConfigController();
		sharedPreferences = this.getSharedPreferences("aebt", MODE_PRIVATE);
		
		updateAll();
		setAdapters();
	}

	private void setAdapters() {
		btnDevicename.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showDialog(DIALOG_TEXT_ENTRY);
			}
		});
	}

	public void updateAll(){
		btn_version.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				controller.sendRequestPacket(new BMSCommand(BMSUtil.COMMAND_GET_HARDWARE_VERSION,
						BMSUtil.COMMAND_GET_HARDWARE_VERSION_REPLY), false);
				controller.sendRequestPacket(new BMSCommand(BMSUtil.COMMAND_GET_SOFTWARE_VERSION,
						BMSUtil.COMMAND_GET_SOFTWARE_VERSION_REPLY), false);
				controller.sendRequestPacket(new BMSCommand(BMSUtil.COMMAND_GET_DEVICE_SERIAL_NUMBER,
						BMSUtil.COMMAND_GET_DEVICE_SERIAL_NUMBER_REPLY), false);
				controller.sendRequestPacket(new BMSCommand(BMSUtil.COMMAND_GET_BATTERY_INFO,
						BMSUtil.COMMAND_GET_BATTERY_INFO_REPLY), false);
			}
		});
		
	}
	

	class StateReceiver extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if(D) Log.i(TAG, "Action received:"+action);
			if(action.equals(BicycleUtil.BATTERY_STATE_UPDATE)){
				setHardware();
				setSoftware();
				setDeviceSerialNumber();
				setBatteryInfo();
			}
		}
    	
    }

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}
	
	
	
	public void setBatteryInfo() {
		String capacity = sharedPreferences.getString("0030-1", "0");
		String totalvoltage = sharedPreferences.getString("0030-2", "0mv");
		String numbattery = sharedPreferences.getString("0030-3", "0个电池");
		tvBatteryInfo.setText("电池容量:"+capacity + "A." + "总电压:"+totalvoltage+"V."+"电池:"+numbattery+"节.");
	}

	public void setDeviceSerialNumber() {
		String deviceserial = sharedPreferences.getString("0007-1", "N/A");
		tvDeviceSerial.setText(deviceserial);
	}

	public void setSoftware() {
		String mainversion = sharedPreferences.getString("0004-1", "N/A");
		String subversion = sharedPreferences.getString("0004-2", "N/A");
		String numEdit = sharedPreferences.getString("0004-3","N/A");
		String name = sharedPreferences.getString("0004-4", "N/A");
		
		tvSoftware.setText("版本:"+mainversion+"-"+subversion+"." + "修改:"+numEdit+"次 ." + "设备:["+name+"]");
	}

	public void setHardware() {
		String mainversion = sharedPreferences.getString("0003-1", "N/A");
		String subversion = sharedPreferences.getString("0003-2", "N/A");
		String numEdit = sharedPreferences.getString("0003-3","N/A");
		String name = sharedPreferences.getString("0003-4", "N/A");
		
		tvHardware.setText("版本:"+mainversion+"-"+subversion+"." + "修改:"+numEdit+"次 ." + "设备:["+name+"]");
	}

	@Override
	public void onStop(){
		super.onStop();
		 if(D) Log.e(TAG, "--- ON STOP ---");
	}
	
	@Override
	public void onResume(){
		super.onResume();
		 if(D) Log.e(TAG, "--- ON RESUME ---");
	}
	
	@Override
    public void onDestroy() {
    	if(receiver != null){
    		this.unregisterReceiver(receiver);
    	}
        super.onDestroy();
        if(D) Log.e(TAG, "--- ON DESTROY ---");
    }
}
