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
	
//	private Button btn_temp; 
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
	
	private MenuItem menu_detail;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_battery_main);
		setup();
	}

	private void setup() {
		controller = RequestController.getRequestController();
		sharedPreferences = this.getSharedPreferences("aebt", MODE_PRIVATE);
//		btn_temp = (Button)findViewById(R.id.buttonTemp);
		thermo = (Thermometer)findViewById(R.id.thermometer);
//		thermo.setLayoutParams(new ViewGroup.LayoutParams((int)BMSUtil.convertPixelsToDp(100, this), (int)BMSUtil.convertPixelsToDp(700, this)));
		
		battery = (BatteryContainer)findViewById(R.id.batterycontainer);
		
		tach = (Tachometer)findViewById(R.id.tachometer);
		power = (TextView)findViewById(R.id.bpowernow);
		tvCurrentNow = (TextView)findViewById(R.id.currentnow);
		tvVoltageNow = (TextView)findViewById(R.id.voltagenow);
		tvTimeLeft = (TextView)findViewById(R.id.batterytimeremain);
		tvTemperature = (TextView)findViewById(R.id.batterytemperature);
		
		menu_detail = (MenuItem)findViewById(R.id.menu_batteryDetail);
		
		setAdapters();
		registerReceiver();
		
		thread = new SendPacketThread(this,PERIOD);
		thread.start();
	}

	private void registerReceiver() {
		receiver = new StateReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(BicycleUtil.BATTERY_STATE_UPDATE);
		filter.addAction(BMSUtil.BATTERY_UPDATE_FAIL_OVER_TIMEOUT);
		this.registerReceiver(receiver, filter);
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
	
	private void setAdapters() {
//		btn_temp.setOnClickListener(new OnClickListener(){
//			@Override
//			public void onClick(View v) {
//				controller.sendRequestPacket(new BMSCommand(BMSUtil.COMMAND_GET_BATTERY_CAPACITY_AND_SOC_STATUS,
//						BMSUtil.COMMAND_GET_BATTERY_CAPACITY_AND_SOC_STATUS_REPLY),false);
//				controller.sendRequestPacket(new BMSCommand(BMSUtil.COMMAND_GET_BATTERY_VOLTAGE_CURRENT,
//						BMSUtil.COMMAND_GET_BATTERY_VOLTAGE_CURRENT_REPLY),false);
//				controller.sendRequestPacket(new BMSCommand(BMSUtil.COMMAND_GET_BATTERY_TEMPERATURE_NOW_DETAIL,
//						BMSUtil.COMMAND_GET_BATTERY_TEMPERATURE_NOW_DETAIL_REPLY),true);
//			}
//		});
		
//		menu_detail.setOnMenuItemClickListener(new OnMenuItemClickListener(){
//			@Override
//			public boolean onMenuItemClick(MenuItem item) {
//				Intent intent = new Intent(BatteryMainActivity.this, BatteryDescriptionActivity.class);
//				BatteryMainActivity.this.startActivity(intent);
//				return true;
//			}
//		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		 MenuInflater inflater = getMenuInflater();
	     inflater.inflate(R.menu.activity_battery_main, menu);
	     return true;
	}

	 @Override
	    public boolean onOptionsItemSelected(MenuItem item) {
		 	Intent intent;
	        switch (item.getItemId()) {
	        case R.id.menu_batteryDetail:
	        	intent = new Intent(BatteryMainActivity.this, BatteryDescriptionActivity.class);
	        	this.startActivity(intent);
	        	return true;
	        case R.id.menu_batteryversion:
	        	intent  = new Intent(BatteryMainActivity.this, BatteryVersionActivity.class);
	        	this.startActivity(intent);
	        	return true;
	        }
	        return false;
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
			if(action.equals(BMSUtil.BATTERY_UPDATE_FAIL_OVER_TIMEOUT)){
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
	
}
