package com.aeenery.aebicycle.challenge;

import com.aeenery.aebicycle.R;
import com.aeenery.aebicycle.entry.BicycleUtil;
import com.aeenery.aebicycle.entry.UtilFunction;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.TextView;
import android.widget.Toast;

public class ShowPropertyActivity extends Activity{

	/**
	 * view elements
	 */
	protected TextView tvTextLocation;
	protected TextView tvWeatherLocation;
	
	protected TextView tvRideDistance;
	protected TextView tvRideTime;
	protected TextView tvRideSpeed;
	
	protected TextView tvPropertyPhoneBatteryCap;
	protected TextView tvPropertyPhoneBatteryTime;
	protected TextView tvPropertyPhoneBatteryTemp;
	
	protected TextView tvPropertyBikeBatteryCap;
	protected TextView tvPropertyBikeBatteryTime;
	protected TextView tvPropertyBikeBatteryTemp;
	
	/**
	 * Shareoreferences data
	 */
	protected SharedPreferences sp;
	
	/**
	 * Variable Names
	 */
	public static final String Location = "currentLocation";
	public static final String Weather = "weather";
	public static final String RideDistance = "rideDistance";
	public static final String RideTime ="rideTime";
	public static final String RideSpeed ="rideSpeed";
	public static final String BikeBCap ="bikeBatteryCap";
	public static final String BikeBTemp ="bikeBatteryTemp";
	public static final String BikeBTime ="bikeBatteryTime";
	public static final String PhoneBCap = "phoneBatteryCap";
	public static final String PhoneBTemp = "phoneBatteryTemp";
	public static final String PhoneBTime = "phoneBatteryTime";
	
	
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_property);
		loadViews();
		setContents();
		this.startService(new Intent(BicycleUtil.ACTION_RUN_PROPERTY_SERVICE));
	}
	
	protected void loadViews(){
		tvTextLocation = (TextView)findViewById(R.id.tvTextLocation);
		tvWeatherLocation = (TextView)findViewById(R.id.tvWeatherLocation);
		tvRideDistance  = (TextView)findViewById(R.id.tvRideDistance);
		tvRideTime = (TextView)findViewById(R.id.tvRideTime);
		tvRideSpeed = (TextView)findViewById(R.id.tvRideSpeed);
		tvPropertyBikeBatteryCap = (TextView)findViewById(R.id.tvPropertyBikeBatteryCapacity);
		tvPropertyBikeBatteryTemp = (TextView)findViewById(R.id.tvPropertyBikeBatteryTemp);
		tvPropertyBikeBatteryTime = (TextView)findViewById(R.id.tvPropertyBikeBatteryTime);
		tvPropertyPhoneBatteryCap = (TextView)findViewById(R.id.tvPropertyPhoneBatteryCapacity);
		tvPropertyPhoneBatteryTemp = (TextView)findViewById(R.id.tvPropertyPhoneBatteryTemp);
		tvPropertyPhoneBatteryTime = (TextView)findViewById(R.id.tvPropertyPhoneBatteryTime);
	}
	
	protected void setContents(){
		if(sp == null){
			sp = PreferenceManager.getDefaultSharedPreferences(this);
		}
		//Get contents from sharePreference file
		String location = sp.getString(Location, "-");
		String weatherStr = sp.getString(Weather, "-");
		int distanceInt = sp.getInt(RideDistance, 0);
		int timeInt = sp.getInt(RideTime, 0);
		float speedF = sp.getFloat(RideSpeed, 0);
		int bbCap = sp.getInt(BikeBCap, 0);
		float bbTemp = sp.getFloat(BikeBTemp,0);
		int bbTime = sp.getInt(BikeBTime, 0); 
		int pbCap = sp.getInt(PhoneBCap, 0);
		float pbTemp = sp.getFloat(PhoneBTemp,0);
		int pbTime = sp.getInt(PhoneBTime, 0); 
		
		tvTextLocation.setText(location+":");
		tvWeatherLocation.setText(weatherStr);
		tvRideDistance.setText(distanceInt+"米");
		tvRideTime.setText(timeInt+"秒");
		tvRideSpeed.setText(speedF+"米/秒");
		
		tvPropertyBikeBatteryCap.setText(bbCap+"%");
		tvPropertyBikeBatteryTemp.setText(bbTemp+"℃");
		tvPropertyBikeBatteryTime.setText(bbTime+"秒");
		
		tvPropertyPhoneBatteryCap.setText(pbCap+"%");
		tvPropertyPhoneBatteryTemp.setText(pbTemp+"℃");
		tvPropertyPhoneBatteryTime.setText(pbTime+"秒");
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		
	}
	
	public BroadcastReceiver receiver = new BroadcastReceiver(){
		@Override
		public void onReceive(Context context, Intent intent) {
			String action  = intent.getAction();
			if(action.equals(BicycleUtil.ACTION_SHOW_PROPERTY)){
				
			}
		}
	};
}
