package com.aeenery.aebicycle.battery;

import com.aeenery.aebicycle.R;
import com.aeenery.aebicycle.R.layout;
import com.aeenery.aebicycle.R.menu;
import com.aeenery.aebicycle.battery.BatteryMainActivity.StateReceiver;
import com.aeenery.aebicycle.bms.BMSUtil;
import com.aeenery.aebicycle.bms.RequestController;
import com.aeenery.aebicycle.bms.SendPacketThread;
import com.aeenery.aebicycle.bms.SenderContext;
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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class BatteryDescriptionActivity extends Activity implements SenderContext{
	
	private final static boolean D = true;
	public final static String TAG = "BatteryDescriptionActivity";
	private final int PERIOD = 5000;

	private TextView tvsoc;
	private TextView tvpowerremain;
	private TextView tvcurrentnow;
	private TextView tvtotalvoltage;
	private TextView tvtempnow;
	private TextView tvtemphistory;
	private TextView tvperiod;
	private Button btnDescriptionUpdate;
	private SendPacketThread thread = null;
	
	private StateReceiver receiver = null;
	private RequestController controller = null;
	private SharedPreferences sharedPreferences = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_battery_description);
		
		registerReceiver();
		loadUIObjects();
		restartThread();
	}

	private void restartThread() {
		if(thread != null){
			thread.cancel();
			thread = null;
		}
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
	
	private void loadUIObjects() {
		tvsoc = (TextView)findViewById(R.id.tvsoc);
		tvpowerremain = (TextView)findViewById(R.id.tvpowerremain);
		tvcurrentnow = (TextView)findViewById(R.id.tvcurrentnow);
		tvtotalvoltage = (TextView)findViewById(R.id.tvtotalvoltage);
		tvtempnow = (TextView)findViewById(R.id.tvtempnow);
		tvtemphistory = (TextView)findViewById(R.id.tvtemphistory);
		tvperiod = (TextView)findViewById(R.id.tvperiod);
		btnDescriptionUpdate = (Button)findViewById(R.id.btn_description_update);

		controller = RequestController.getRequestController(this);
		sharedPreferences = this.getSharedPreferences("aebt", MODE_PRIVATE);
		
		updateAll();
	}

	public void updateAll(){
		btnDescriptionUpdate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				controller.sendRequestPacket(new BMSCommand(BMSUtil.COMMAND_GET_BATTERY_CAPACITY_AND_SOC_STATUS,
						BMSUtil.COMMAND_GET_BATTERY_CAPACITY_AND_SOC_STATUS_REPLY),false);
				controller.sendRequestPacket(new BMSCommand(BMSUtil.COMMAND_GET_BATTERY_LOOP_PERIOD_INFO,
						BMSUtil.COMMAND_GET_BATTERY_LOOP_PERIOD_INFO_REPLY), false);
				controller.sendRequestPacket(new BMSCommand(BMSUtil.COMMAND_GET_BATTERY_VOLTAGE_CURRENT,
						BMSUtil.COMMAND_GET_BATTERY_VOLTAGE_CURRENT_REPLY),false);
				controller.sendRequestPacket(new BMSCommand(BMSUtil.COMMAND_GET_BATTERY_TEMPERATURE_NOW_DETAIL,
						BMSUtil.COMMAND_GET_BATTERY_TEMPERATURE_NOW_DETAIL_REPLY),true);
				controller.sendRequestPacket(new BMSCommand(BMSUtil.COMMAND_GET_BATTERY_TEMPERATURE,
						BMSUtil.COMMAND_GET_BATTERY_TEMPERATURE_REPLY), false);
			}
		});
		
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
				setSoc();
				setPeriodic();
				setVoltageAndCurrent();
				setTempNow();
				setTempHistory();
			}
		}
    	
    }

	public void setSoc() {
		String socAbsolute = sharedPreferences.getString("00A6-1", "");
		String socRelative = sharedPreferences.getString("00A6-2", "");
		double power_now = Double.parseDouble(sharedPreferences.getString("00A6-3", ""));
		double power_max = Double.parseDouble(sharedPreferences.getString("00A6-4", ""));
		
		tvsoc.setText("缁濆SOC:" + socAbsolute+",鐩稿SOC:" + socRelative);
		double d = power_now/power_max * 100;
		tvpowerremain.setText(power_now + " / " + power_max + " = " + (int)d + "%");
	}

	public void setTempHistory() {
		String highest = sharedPreferences.getString("0080-1", "0 掳C");
		String lowest = sharedPreferences.getString("0080-2", "0 掳C");
		tvtemphistory.setText("鏈�珮娓╁害:"+highest+"掳C" + " , 鏈�綆娓╁害:"+lowest+"掳C");
	}

	public void setTempNow() {
		String temp = sharedPreferences.getString("00A2-5", "0掳C");
		String[] sa = temp.split(",");
		if(sa.length > 0){
			tvtempnow.setText(sa[0] + "掳C");
		}
	}

	public void setVoltageAndCurrent() {
		String voltage  = sharedPreferences.getString("00A0-1", "0 V");
		String current = sharedPreferences.getString("00A0-2", "0 A");
		tvtotalvoltage.setText(voltage + " V");
		tvcurrentnow.setText(current + " A");
	}

	public void setPeriodic() {
		Long period  = sharedPreferences.getLong("00A8-1", 0L);
		tvperiod.setText(period + "次");
	}
	
	public synchronized void sendPackets(){
		try{
			controller.sendRequestPacket(new BMSCommand(BMSUtil.COMMAND_GET_BATTERY_CAPACITY_AND_SOC_STATUS,
					BMSUtil.COMMAND_GET_BATTERY_CAPACITY_AND_SOC_STATUS_REPLY),false);
			wait();
			controller.sendRequestPacket(new BMSCommand(BMSUtil.COMMAND_GET_BATTERY_LOOP_PERIOD_INFO,
					BMSUtil.COMMAND_GET_BATTERY_LOOP_PERIOD_INFO_REPLY), false);
			wait();
			controller.sendRequestPacket(new BMSCommand(BMSUtil.COMMAND_GET_BATTERY_VOLTAGE_CURRENT,
					BMSUtil.COMMAND_GET_BATTERY_VOLTAGE_CURRENT_REPLY),false);
			wait();
			controller.sendRequestPacket(new BMSCommand(BMSUtil.COMMAND_GET_BATTERY_TEMPERATURE_NOW_DETAIL,
					BMSUtil.COMMAND_GET_BATTERY_TEMPERATURE_NOW_DETAIL_REPLY),true);
			wait();
			controller.sendRequestPacket(new BMSCommand(BMSUtil.COMMAND_GET_BATTERY_TEMPERATURE,
					BMSUtil.COMMAND_GET_BATTERY_TEMPERATURE_REPLY), false);
			wait();
		}catch (Exception e) {
			Log.e("TAG","Error:"+e.getMessage()+ " "+e.getStackTrace()[1]);
		}
	}
	
	public synchronized void receivedPackets(){
		notify();
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
			thread.cancel();
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
}
