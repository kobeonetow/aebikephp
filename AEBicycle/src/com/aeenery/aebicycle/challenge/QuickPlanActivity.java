package com.aeenery.aebicycle.challenge;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.aeenery.aebicycle.BaseActivity;
import com.aeenery.aebicycle.LoginActivity;
import com.aeenery.aebicycle.R;
import com.aeenery.aebicycle.entry.BicycleUtil;
import com.aeenery.aebicycle.model.Plan;
import com.aeenery.aebicycle.model.ServerAPI;

public class QuickPlanActivity extends BaseActivity {
	
	private TextView tvEstimateDistance = null;
	private EditText etPlanName = null;
	private EditText etStartLocation = null;
	private EditText etTerminateLocation = null;
	private TextView tvExpectedTime;
	private Button btPplExpected;
	private EditText etRemark;
	private EditText etSponsor;
	private EditText etPrize;
	private Button  btnSubmit = null;
	private Button btnSelectLoc = null;
	
	private ServerAPI api = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_quickplan);
		api = new ServerAPI(QuickPlanActivity.this, LoginActivity.user);
		if(!LoginActivity.login){
			api.useSharedPreferencesInfoLogin(QuickPlanActivity.this);
			api.setUser(LoginActivity.user);
		}
		init();
	}
	
	public void init(){
		etPlanName = (EditText)findViewById(R.id.plan_name);
		etStartLocation = (EditText)findViewById(R.id.plan_start_location);
		etTerminateLocation = (EditText)findViewById(R.id.plan_terminate_location);
		btnSubmit = (Button)findViewById(R.id.plan_submit);
		tvEstimateDistance = (TextView)findViewById(R.id.plan_estimate_distance);
		tvExpectedTime = (TextView)findViewById(R.id.plan_expected_time);
		btPplExpected = (Button)findViewById(R.id.pplexpected);
		etRemark = (EditText)findViewById(R.id.planremark);
		etSponsor = (EditText)findViewById(R.id.plansponsor);
		etPrize = (EditText)findViewById(R.id.planprize);
		
		btnSelectLoc = (Button)findViewById(R.id.plan_select_locations);
		
		btnSubmit.setOnClickListener(new SubmitListener());
		btnSelectLoc.setOnClickListener(new ClickToMapListener());
		etStartLocation.setOnClickListener(new ClickToMapListener());
		etTerminateLocation.setOnClickListener(new ClickToMapListener());
		
		btPplExpected.setOnClickListener(new ButtonClickListener());
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		super.onActivityResult(requestCode, resultCode, data);
		Log.i("getting", "get result");
		if (requestCode == resultCode && resultCode == BicycleUtil.PLAN_SELECT_ROUTE) {
			Intent intent = data;
//			Bundle bundle = intent.getBundleExtra("returnBundle");
//			if (bundle.getBoolean("hasRoute")) {
			if(intent.getBooleanExtra("hasRoute", false))	{
				String startPoiName = intent.getStringExtra("startPoiName");
				int startPoiLat = intent.getIntExtra("startPoiLat", 0);
				int startPoiLon = intent.getIntExtra("startPoiLon", 0);

				String terminatePoiName = intent
						.getStringExtra("terminatePoiName");
				int terminatePoiLat = intent.getIntExtra("terminatePoiLat", 0);
				int terminatePoiLon = intent.getIntExtra("terminatePoiLon", 0);

				int distance = intent.getIntExtra("distance", 0);
				
				etStartLocation.setText(startPoiName + "(" + startPoiLat + ","
						+ startPoiLon + ")");
				etTerminateLocation.setText(terminatePoiName + "("
						+ terminatePoiLat + "," + terminatePoiLon + ")");
				tvEstimateDistance.setText(distance + " meters");
			} else {
				Toast.makeText(QuickPlanActivity.this,
						"Cannot cast positions, try Again", Toast.LENGTH_LONG)
						.show();
			}
		}
	}
	
	public void planCreated(){
		Toast.makeText(this, this.getString(R.string.plan_create_success), Toast.LENGTH_LONG).show();
		Intent result = new Intent();
		result.putExtra("created", true);
		this.setResult(BicycleUtil.CREATE_PLAN_SUCCESS, result);
		this.finish();
	}
	
	class SubmitListener implements OnClickListener {
			Plan p = new Plan();
			
			@Override
			public void onClick(View v) {
				switch(checkValidatity()){
				case BicycleUtil.RequirePlanName:
					Toast.makeText(QuickPlanActivity.this, getString(R.string.plan_name_not_invalid), Toast.LENGTH_SHORT).show();
					break;
				case BicycleUtil.RequireStartLocation:
					Toast.makeText(QuickPlanActivity.this, getString(R.string.plan_start_not_invalid), Toast.LENGTH_SHORT).show();
					break;
				case BicycleUtil.RequireEndLocation:
					Toast.makeText(QuickPlanActivity.this, getString(R.string.plan_terminate_not_invalid), Toast.LENGTH_SHORT).show();
					break;
				case BicycleUtil.Valid:
					api.createplan(QuickPlanActivity.this,this.p);
					break;
				default:
					//Do nothing
				}
			}
			
			public int checkValidatity(){
				String name = etPlanName.getText().toString().trim();
				if(name.equals("")){  
					return BicycleUtil.RequirePlanName;
				}
				
				String startloc =  etStartLocation.getText().toString().trim();
				if(startloc.equals("")){
					return BicycleUtil.RequireStartLocation;
				}
				
				String terminateloc =  etTerminateLocation.getText().toString().trim();
				if(terminateloc.equals("")){
					return BicycleUtil.RequireEndLocation;
				}
				
				String distance = tvEstimateDistance.getText().toString().trim();
				String expectedTime = tvExpectedTime.getText().toString().trim();
				String pplExpected = btPplExpected.getText().toString().trim();
				String sponsor = etSponsor.getEditableText().toString().trim();
				String prize = etPrize.getEditableText().toString().trim();
				String remark = etRemark.getEditableText().toString().trim();
				
				p.setName(name);
				p.setStartlocation(startloc);
				p.setEndlocation(terminateloc);
				if(distance != null && !distance.equals(""))
					p.setDistance(distance);
				if(expectedTime != null && !expectedTime.equals(""))
					p.setExpecttime(expectedTime);
				if(pplExpected != null && !pplExpected.equals(""))
					p.setPplexpected(pplExpected);
				if(sponsor != null && !sponsor.equals(""))
					p.setSponsor(sponsor);
				if(remark != null && !remark.equals(""))
					p.setDescription(remark);
				
				return BicycleUtil.Valid;
			}
			
	};
	
	class ClickToMapListener implements OnClickListener {
		@Override
		public void onClick(View v) {
//			Intent intent  = new Intent(QuickPlanActivity.this, RouteSelectionActivity.class);
//			Bundle bundle = new Bundle();
//			String start = etStartLocation.getText().toString();
//			if(start.length() > 0){
//				bundle.putBoolean("hasStart", true);
//				bundle.putString("startPoiName", start.substring(0, start.indexOf("(")));
//				bundle.putInt("startPoiLat", Integer.parseInt(start.substring(start.indexOf("(")+1, start.indexOf(","))));
//				bundle.putInt("startPoiLon", Integer.parseInt(start.substring(start.indexOf(",")+1, start.indexOf(")"))));
//			}
//			String end = etTerminateLocation.getText().toString();
//			if(end.length() > 0){
//				bundle.putBoolean("hasTerminate", true);
//				bundle.putString("terminatePoiName", end.substring(0, end.indexOf("(")));
//				bundle.putInt("terminatePoiLat", Integer.parseInt(end.substring(end.indexOf("(")+1, end.indexOf(","))));
//				bundle.putInt("terminatePoiLon", Integer.parseInt(end.substring(end.indexOf(",")+1, end.indexOf(")"))));
//			}
//			intent.putExtras(bundle);
//			QuickPlanActivity.this.startActivityForResult(intent, BicycleUtil.PLAN_SELECT_ROUTE);
		}
	}
	
	class ButtonClickListener implements OnClickListener{
		@Override
		public void onClick(View v) {
			switch(v.getId()){
			case R.id.pplexpected:
				new AlertDialog.Builder(QuickPlanActivity.this)
                .setTitle("最少参加人数")
                .setItems(R.array.pplcount, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        /* User clicked so do some stuff */
                        String[] items = getResources().getStringArray(R.array.pplcount);
                        btPplExpected.setText(items[which]);
                    }
                })
                .create().show();
				break;
			default:
				break;
			}
		}
		
	}
}

