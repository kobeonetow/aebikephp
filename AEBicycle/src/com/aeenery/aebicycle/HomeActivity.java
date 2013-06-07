package com.aeenery.aebicycle;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
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
import com.aeenery.aebicycle.challenge.RouteMapActivity;
import com.aeenery.aebicycle.entry.BicycleUtil;
import com.aeenery.aebicycle.model.netManager;
import com.aeenery.aebicycle.weather.WeatherActivity;

public class HomeActivity extends BaseActivity {

	// Debugging
    private static final String TAG = "HomeActivity";
    private static final boolean D = true;
    
    private BluetoothService s;
	
	private netManager wifimgr;
	private ImageButton btnZone;
	private ImageButton btnWeather;
	private ImageButton btnMap;
	private ImageButton btnRide;
	private ImageButton btnNews;
	private ImageButton btnOthers;
	
	// Local Bluetooth adapter
    private BluetoothAdapter mBluetoothAdapter = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		init();
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if(mBluetoothAdapter != null)
			enableBluetoothAdapter();
	}

	@Override
	protected void onStart(){
		super.onStart();
		if(D) Log.i(TAG, "ON START");
		//Start the bluetooth service
		//Must assign a controller to service
		startBluetoothService();
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
		wifimgr = new netManager(this); 
		checkNetConnection();
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
					break;
				case R.id.home_image_button_2:
					intent = new Intent(HomeActivity.this,
							WeatherActivity.class);
					break;
				case R.id.home_image_button_3:
					intent = new Intent(HomeActivity.this,
							RouteMapActivity.class);
					break;
				case R.id.home_image_button_4:
					intent = new Intent(HomeActivity.this,
							ChallengeActivity.class);
					break;
				case R.id.home_image_button_5:
					intent = new Intent(HomeActivity.this,
							BatteryDetailActivity.class);
					break;
				case R.id.home_image_button_6:
//					intent = new Intent(HomeActivity.this,
//							BluetoothChat.class);
					intent = new Intent(HomeActivity.this,
							BatteryMainActivity.class);
					
					break;
				default:
					// do Nothing
					break;
				}
				HomeActivity.this.startActivity(intent);
			}
		};
		btnZone.setOnClickListener(click);
		btnWeather.setOnClickListener(click);
		btnMap.setOnClickListener(click);
		btnRide.setOnClickListener(click);
		btnNews.setOnClickListener(click);
		btnOthers.setOnClickListener(click);
	}

	public void checkNetConnection() {
		if (wifimgr == null) {
			wifimgr = new netManager(this);
		}
		if (!wifimgr.checkConnect()) {
			Builder dialog = new AlertDialog.Builder(this);
			dialog.setTitle("通知");
			dialog.setMessage("是否进行网络连接,若不连接,程序可能不能访问数据并强行退出");
			dialog.setPositiveButton("网络设置",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							startActivity(new Intent(
									android.provider.Settings.ACTION_WIRELESS_SETTINGS));
							dialog.dismiss();
						}
					});
			dialog.setNegativeButton("取消",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							dialog.dismiss();
							finish();
						}
					});
			dialog.show();
		}
	}
	
	private void enableBluetoothAdapter(){
		if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, BicycleUtil.REQUEST_ENABLE_BT);
        }
	}
	
	private void startBluetoothService(){
		 Intent service = new Intent(this, BluetoothService.class);
	     this.startService(service);
	     if(D) Log.e(TAG, "+ ON start Service +");
//	        bindService(new Intent(this, BluetoothService.class), mConnection,
//	            Context.BIND_AUTO_CREATE);
	}
    
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(D) Log.d(TAG, "onActivityResult " + resultCode);
        switch (requestCode) {
        case BicycleUtil.REQUEST_ENABLE_BT:
            // When the request to enable Bluetooth returns
            if (resultCode == Activity.RESULT_OK) {
                // Bluetooth is now enabled, so set up a chat session
//                broadcastAction(BicycleUtil.BT_ENABLE);
            } else {
                // User did not enable Bluetooth or an error occured
//            	broadcastAction(BicycleUtil.BT_NOT_ENABLE);
            }
        }
	}
	
	public void broadcastAction(String actionName){
		Intent intent = new Intent();
		intent.setAction(actionName);
		sendBroadcast(intent);
	}
}
