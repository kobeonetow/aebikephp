package com.aeenery.aebicycle;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.NotificationManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.Toast;

import com.aeenery.aebicycle.battery.BatteryDetailActivity;
import com.aeenery.aebicycle.battery.BatteryMainActivity;
import com.aeenery.aebicycle.battery.BluetoothChat;
import com.aeenery.aebicycle.battery.BluetoothService;
import com.aeenery.aebicycle.battery.DeviceListActivity;
import com.aeenery.aebicycle.bms.BMSController;
import com.aeenery.aebicycle.challenge.ChallengeActivity;
//import com.aeenery.aebicycle.challenge.RouteMapActivity;
import com.aeenery.aebicycle.entry.BicycleUtil;
import com.aeenery.aebicycle.entry.UIHelper;
import com.aeenery.aebicycle.map.MapActivity;
import com.aeenery.aebicycle.model.netManager;
import com.aeenery.aebicycle.notification.AppNotifications;
import com.aeenery.aebicycle.weather.WeatherActivity;
import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.baidu.android.pushservice.PushSettings;

public class HomeActivity extends Activity{

	// Debugging
    private static final String TAG = "HomeActivity";
    private static final boolean D = true;

	private ImageButton btnZone;
	private ImageButton btnWeather;
	private ImageButton btnMap;
	private ImageButton btnRide;
	private ImageButton btnNews;
	private ImageButton btnOthers;
	
	//Test
	AppNotifications notis = null;
	NotificationManager notiManager = null; 


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		
		//Set onclick event for buttons
		init();
		
		//test notifications
		notis = AppNotifications.getInstance();
		notiManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE); 
//		testNotifications();
		
		//Enable baidu push service
		startPushService();
	}

	/**
	 * Start the push service, will only start once on device
	 */
	private void startPushService() {
		if(D) Log.i(TAG,"++ In PUSH SERVICE ++");
//		if(!PushManager.isPushEnabled(getApplicationContext())){
//			PushSettings.enableDebugMode(getApplicationContext(), true);
			PushManager.startWork(getApplicationContext(), PushConstants.LOGIN_TYPE_API_KEY, BicycleUtil.PUSH_API_KEY);
//		}
	}

	private void testNotifications() {
		notiManager.notify(BicycleUtil.NOTI_BATTERY_LOW,notis.getBatteryLowNotification(this, "电池约剩下5秒").build());
		notiManager.notify(BicycleUtil.NOTI_BATTERY_ERROR,notis.getBatteryErrorNotification(this, "电池约剩下5秒").build());
	}

	@Override
	protected void onStart(){
		super.onStart();
		if(D) Log.i(TAG, "ON START");
	}
	
	@Override 
	protected void onPause(){
		super.onPause();
		if(D) Log.i(TAG, "ON PAUSE");
	}
	
	@Override
	protected void onDestroy(){
		super.onDestroy();
		if(D) Log.i(TAG, "ON DESTROY");
	}
	
	@Override
	protected void onResume(){
		super.onResume();
		if(D) Log.i(TAG, "ON RESUME");
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_home, menu);
		return true;
	}

	public void init() {
		btnZone = (ImageButton) findViewById(R.id.home_image_button_1);
		btnWeather = (ImageButton) findViewById(R.id.home_image_button_2);
		btnMap = (ImageButton) findViewById(R.id.home_image_button_3);
		btnRide = (ImageButton) findViewById(R.id.home_image_button_4);
		btnNews = (ImageButton) findViewById(R.id.home_image_button_5);
		btnOthers = (ImageButton) findViewById(R.id.home_image_button_6);
		setAdapters();
	}

	public void setAdapters() {
		OnClickListener click = new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = null;
				switch (v.getId()) {
				case R.id.home_image_button_1:
					intent = new Intent(HomeActivity.this,
							MainTabActivity.class);
					HomeActivity.this.startActivity(intent);
					break;
				case R.id.home_image_button_2:
					intent = new Intent(HomeActivity.this,
							WeatherActivity.class);
					HomeActivity.this.startActivity(intent);
					break;
				case R.id.home_image_button_3:
					intent = new Intent(HomeActivity.this,
							MapActivity.class);
					HomeActivity.this.startActivity(intent);
					break;
				case R.id.home_image_button_4:
					intent = new Intent(HomeActivity.this,
							ChallengeActivity.class);
					HomeActivity.this.startActivity(intent);
					break;
				case R.id.home_image_button_5:
					quitApp();
					break;
				case R.id.home_image_button_6:
					intent = new Intent(HomeActivity.this,
							BatteryMainActivity.class);
					HomeActivity.this.startActivity(intent);
					break;
				default:
					// do Nothing
					break;
				}
			}
		};
		btnZone.setOnClickListener(click);
		btnWeather.setOnClickListener(click);
		btnMap.setOnClickListener(click);
		btnRide.setOnClickListener(click);
		btnNews.setOnClickListener(click);
		btnOthers.setOnClickListener(click);
	}

	public void quitApp(){
		PushManager.stopWork(getApplicationContext());
		this.stopService(new Intent(BluetoothService.ServiceAction));
		UIHelper.killApp(true);
//		this.finish();
	}
}
