package com.aeenery.aebicycle.battery;


import com.aeenery.aebicycle.R;
import com.aeenery.aebicycle.R.layout;
import com.aeenery.aebicycle.R.menu;
import com.aeenery.aebicycle.battery.widget.BatteryContainer;
import com.aeenery.aebicycle.battery.widget.Tachometer;
import com.aeenery.aebicycle.battery.widget.Thermometer;
import com.aeenery.aebicycle.bms.BMSUtil;
import com.aeenery.aebicycle.bms.RequestController;
import com.aeenery.aebicycle.bms.SenderContext;
import com.aeenery.aebicycle.bms.SendPacketThread;
import com.aeenery.aebicycle.bms.models.BMSCommand;
import com.aeenery.aebicycle.entry.BicycleUtil;

import android.os.Bundle;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class BatteryMainActivity extends Activity implements SenderContext{

	private static String TAG = "BatteryMainActivity";
	private static boolean D = true;
	
	
	//Bluetooth hardwares
	public static BluetoothAdapter mBluetoothAdapter = null;
	public static boolean deviceConnected = false;
	
	//Notification textview
	private TextView tvNotify;
	
	private Thermometer thermo;
	private Tachometer tach;
	private BatteryContainer battery;
	private TextView power;
	private TextView tvCurrentNow;
	private TextView tvVoltageNow;
	private TextView tvTimeLeft;
	private TextView tvTemperature;
	private SendPacketThread thread = null;
	
	private StateReceiver receiver = null;
	private RequestController controller = null;
	private SharedPreferences sharedPreferences = null;
	
	private static final int PERIOD = 1000; //miliseconds
	
//	private MenuItem menu_detail;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_battery_main);
		
		//Load views
		startService(new Intent(BluetoothService.ServiceAction));
		loadAndGoneViews();
		if(openBluetooth()){
			if(connectDevice()){
				showBatteryInfo();
			}else{
				tvNotify.setVisibility(View.VISIBLE);
			}
		}else{
			tvNotify.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * Load all views and set to invisible
	 */
	protected void loadAndGoneViews(){
		controller = RequestController.getRequestController();
		sharedPreferences = this.getSharedPreferences("aebt", MODE_PRIVATE);
		
		tvNotify = (TextView)findViewById(R.id.notification_battery_main_activity);
		tvNotify.setVisibility(View.GONE);
		thermo = (Thermometer)findViewById(R.id.thermometer);
		thermo.setVisibility(View.GONE);
		battery = (BatteryContainer)findViewById(R.id.batterycontainer);
		battery.setVisibility(View.GONE);
		tach = (Tachometer)findViewById(R.id.tachometer);
		tach.setVisibility(View.GONE);
		power = (TextView)findViewById(R.id.bpowernow);
		power.setVisibility(View.GONE);
		tvCurrentNow = (TextView)findViewById(R.id.currentnow);
		tvCurrentNow.setVisibility(View.GONE);
		tvVoltageNow = (TextView)findViewById(R.id.voltagenow);
		tvVoltageNow.setVisibility(View.GONE);
		tvTimeLeft = (TextView)findViewById(R.id.batterytimeremain);
		tvTimeLeft.setVisibility(View.GONE);
		tvTemperature = (TextView)findViewById(R.id.batterytemperature);
		tvTemperature.setVisibility(View.GONE);
		
//		menu_detail = (MenuItem)findViewById(R.id.menu_batteryDetail);
		
		//Register receivers for broadcast actions
		IntentFilter filter = new IntentFilter();
		filter.addAction(BicycleUtil.BATTERY_STATE_UPDATE);
		filter.addAction(BMSUtil.BATTERY_UPDATE_FAIL_OVER_TIMEOUT);
		filter.addAction(BicycleUtil.DEVICE_CONNECTED);
		filter.addAction(BicycleUtil.DEVICE_DISCONNECTED);
		registerReceiver(new StateReceiver(), filter);
	}
	
	/**
	 * Open and enable bluetooth, if bluetooth was not on, reutrn false
	 * @return
	 */
	protected boolean openBluetooth(){
		if(mBluetoothAdapter == null)
			mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if(mBluetoothAdapter == null){
			tvNotify.setText("没找到本机的蓝牙设备,请确定本机支持蓝牙");
		}else{
			if(!mBluetoothAdapter.isEnabled())
				startActivityForResult(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE), BicycleUtil.REQUEST_ENABLE_BT);
			else
				return true;
		}
		return false;
	}
	
	/**
	 * Check whether device is connected
	 * @return
	 */
	protected boolean connectDevice(){
		if(deviceConnected)
			return true;
		else{
			tvNotify.setText("未连接上支持蓝牙的电池,请按菜单键进行连接.");
			return false;
		}
	}
	
	/**
	 * Re show all views and enable synchronization between devices
	 */
	protected void showBatteryInfo(){
		tvNotify.setVisibility(View.GONE);
		thermo.setVisibility(View.VISIBLE);
		battery.setVisibility(View.VISIBLE);
		tach.setVisibility(View.VISIBLE);
		power.setVisibility(View.VISIBLE);
		tvCurrentNow.setVisibility(View.VISIBLE);
		tvVoltageNow.setVisibility(View.VISIBLE);
		tvTimeLeft.setVisibility(View.VISIBLE);
		tvTemperature.setVisibility(View.VISIBLE);
		
		startSynchronize();
	}
	
	/**
	 * Start exchanging data vie bluetooth
	 */
	private void startSynchronize() {
		//Start the send packer theard for data exchange
		thread = new SendPacketThread(this,PERIOD);
		thread.start();
	}
	
	/**
	 * Stop exchanging data vie bluetooth
	 */
	private void stopSynchronize(){
		if(thread != null){
			thread.cancel();
		}
	}
	
	/**
	 * Get back the notification if connection lost
	 */
	protected void connectionLost(){
		tvNotify.setText("未连接上支持蓝牙的电池,请按菜单键进行连接.");
		tvNotify.setVisibility(View.VISIBLE);
		thermo.setVisibility(View.GONE);
		battery.setVisibility(View.GONE);
		tach.setVisibility(View.GONE);
		power.setVisibility(View.GONE);
		tvCurrentNow.setVisibility(View.GONE);
		tvVoltageNow.setVisibility(View.GONE);
		tvTimeLeft.setVisibility(View.GONE);
		tvTemperature.setVisibility(View.GONE);
	}
	
	public synchronized void sendPackets(){
			try{
				controller.sendRequestPacket(new BMSCommand(BMSUtil.COMMAND_GET_BATTERY_CAPACITY_AND_SOC_STATUS,
						BMSUtil.COMMAND_GET_BATTERY_CAPACITY_AND_SOC_STATUS_REPLY),false);
				wait();
				controller.sendRequestPacket(new BMSCommand(BMSUtil.COMMAND_GET_BATTERY_VOLTAGE_CURRENT,
						BMSUtil.COMMAND_GET_BATTERY_VOLTAGE_CURRENT_REPLY),false);
				wait();
				controller.sendRequestPacket(new BMSCommand(BMSUtil.COMMAND_GET_BATTERY_TEMPERATURE_NOW_DETAIL,
						BMSUtil.COMMAND_GET_BATTERY_TEMPERATURE_NOW_DETAIL_REPLY),true);
				wait();
				controller.sendRequestPacket(new BMSCommand(BMSUtil.COMMAND_GET_BATTERY_TIME_LEFT,
						BMSUtil.COMMAND_GET_BATTERY_TIME_LEFT_REPLY), false);
				wait();
			}catch (Exception e) {
				Log.e("TAG","Error:"+e.getMessage()+ " "+e.getStackTrace()[1]);
			}
	}
	
	public synchronized void receivedPackets(){
		notify();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		 MenuInflater inflater = getMenuInflater();
	     inflater.inflate(R.menu.activity_battery_main, menu);
	     return true;
	}


	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}
	
	@Override
	public void onStop(){
		if(thread != null && thread.isAlive()){
			thread.cancel();
			thread = null;
		}
		super.onStop();
		 if(D) Log.e(TAG, "--- ON STOP ---");
	}
	
	@Override
	public void onResume(){
		if(thread != null){
			thread.reRun();
		}else{
			thread = new SendPacketThread(this,PERIOD);
			thread.start();
		}
		super.onResume();
//		receivedPackets();
		 if(D) Log.e(TAG, "--- ON RESUME ---");
	}
	
	@Override
    public void onDestroy() {
    	if(receiver != null){
    		this.unregisterReceiver(receiver);
    	}
    	if(thread != null && thread.isAlive()){
    		thread.cancel();
    		thread = null;
    	}
        super.onDestroy();
        if(D) Log.e(TAG, "--- ON DESTROY ---");
    }

	
	class StateReceiver extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent) {
			receivedPackets();
			String action = intent.getAction();
			if(D) Log.i(TAG, "Action received:"+action);
			if(action.equals(BicycleUtil.DEVICE_CONNECTED)){
				showBatteryInfo();
			}else if(action.equals(BicycleUtil.DEVICE_DISCONNECTED)){
				connectionLost();
				stopSynchronize();
			}else if(action.equals(BMSUtil.BATTERY_UPDATE_FAIL_OVER_TIMEOUT)){
				if(D) Log.e(TAG, "Timeout over limits, continue to next packet");
			}else if(action.equals(BicycleUtil.BATTERY_STATE_UPDATE)){
				String temp= "";
				temp = sharedPreferences.getString("00A6-3", "");
				double power_now = 0;
				double power_max = 0;
				if(!temp.equals(""))
					power_now = Double.parseDouble(temp);
				temp = sharedPreferences.getString("00A6-4", "");
				if(!temp.equals(""))
					power_max = Double.parseDouble(temp);
				if(power_max != 0){
					power_now = power_now / power_max * 100;
					power.setText("电量:"+(int)power_now + "%");
					tach.update((float)power_now);
					battery.update((float)power_now);
				}else{
					power.setText("电量:N.A.");
					tach.update(0);
					battery.update((float)100);
				}
				
				String voltage =  sharedPreferences.getString("00A0-1", "0");
				tvVoltageNow.setText("电压:" + voltage + " V") ;
				String current =  sharedPreferences.getString("00A0-2", "0");
				tvCurrentNow.setText("电流:" + current + " A");
				
				temp =  sharedPreferences.getString("00A2-5", "");
				if(temp.equals("")){
//					btn_temp.setText("N/A");
					thermo.update(0.0f);
					tvTemperature.setText("0.0°C");
				}else{
					String[] temps = temp.split(",");
					int count = 0;
					double total = 0;
					for(int i=0;i<temps.length;i++){
						if(!temps[i].equals("")){
							total += Double.parseDouble(temps[i]);
							count++;
						}
					}
					total = total/(double)count;
//					btn_temp.setText(total + "C");
					thermo.update((float)total);
					tvTemperature.setText(total+"°C");
				}
				
				
				String timeleft =  sharedPreferences.getString("00AA-1", "0") + "小时" +  sharedPreferences.getString("00AA-2", "0")+ "分";
				tvTimeLeft.setText("剩余:"+timeleft);
			}
		}
    	
    }
	
	
	/********************** On menu item select to connect to bluetooth*********/
	@Override
	 public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent;
		if(item.getItemId() == R.id.scan){
			discoverDevices();
            return true;
		}
		if(deviceConnected == false){
			Toast.makeText(this, "请先连接上设备", Toast.LENGTH_SHORT).show();
			return false;
		}
	    switch (item.getItemId()) {
	    case R.id.menu_batteryDetail:
	       	intent = new Intent(BatteryMainActivity.this, BatteryDescriptionActivity.class);
	       	this.startActivity(intent);
	       	return true;
	    case R.id.menu_batteryversion:
	       	intent  = new Intent(BatteryMainActivity.this, BatteryVersionActivity.class);
	       	this.startActivity(intent);
	       	return true;
	    case R.id.stop_bluetooth:
	    	sendBroadcast(new Intent(BicycleUtil.STOP_BT_SERVICE));
	    	break;
	    }
	    return false;
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
                // Attempt to connect to the device
                Intent intent = new Intent(BicycleUtil.CONNECT_DEVICE);
                intent.putExtra("deviceAddress", address);
                sendBroadcast(intent);
            }
            break;
        case BicycleUtil.REQUEST_ENABLE_BT:
        	if(resultCode == RESULT_OK){
        		if(connectDevice()){
    				showBatteryInfo();
    			}else{
    				tvNotify.setVisibility(View.VISIBLE);
    			}
        	}
            break;
        }
    }
    
    private void discoverDevices(){
        Intent serverIntent = new Intent(this, DeviceListActivity.class);
        startActivityForResult(serverIntent, BicycleUtil.REQUEST_CONNECT_DEVICE);
    }
    
    /************************* finish connect to device *************************/
	
}
