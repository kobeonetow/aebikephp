package com.aeenery.aebicycle.challenge;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.app.DatePickerDialog;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.aeenery.aebicycle.BaseActivity;
import com.aeenery.aebicycle.LoginActivity;
import com.aeenery.aebicycle.R;
import com.aeenery.aebicycle.entry.BicycleUtil;
import com.aeenery.aebicycle.entry.UtilFunction;
import com.aeenery.aebicycle.map.MapActivity;
import com.aeenery.aebicycle.model.Plan;
import com.aeenery.aebicycle.model.ServerAPI;
import com.baidu.mapapi.map.LocationData;

public class QuickPlanActivity extends FragmentActivity {
	
	//Attributes
	private Button btnDatePicker;
	private Button btnTimePicker;
	
	private TextView tvEstimateDistance = null;
	private EditText etPlanName = null;
	private TextView tvExpectedTime;
	private Button btPplExpected;
	private EditText etRemark;
	private Button  btnSubmit = null;
	private Button btnSelectLoc = null;
	
	private ServerAPI api = null;
	
	
	//start and end location
	protected LocationData start, end;
	protected int selectLocStatus = 0x00; //0=not set,1=start set, 2=end set, 3=all set
	protected String selectLocString = "选择地点(未设置)";
	
	//Some attributes to store temp parameters
	protected int distance;
	protected int time;
	
	//Plan stores plan
	protected Plan p = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_quickplan);
		api = new ServerAPI(QuickPlanActivity.this, LoginActivity.user);
		init();
	}
	
	public void init(){
		etPlanName = (EditText)findViewById(R.id.plan_name);
		btnSubmit = (Button)findViewById(R.id.plan_submit);
		tvEstimateDistance = (TextView)findViewById(R.id.plan_estimate_distance);
		tvExpectedTime = (TextView)findViewById(R.id.plan_expected_time);
		btPplExpected = (Button)findViewById(R.id.pplexpected);
		etRemark = (EditText)findViewById(R.id.planremark);
		
		btnDatePicker = (Button)findViewById(R.id.btn_date_picker);
		btnTimePicker = (Button)findViewById(R.id.btn_time_picker);
		btnSelectLoc = (Button)findViewById(R.id.plan_select_locations);
		
		
		
		btnSubmit.setOnClickListener(new ButtonClickListener());
		btnSelectLoc.setOnClickListener(new ClickToMapListener());
		btnSelectLoc.setText(selectLocString);
		btnDatePicker.setOnClickListener(new ButtonClickListener());
		btnTimePicker.setOnClickListener(new ButtonClickListener());
		btPplExpected.setOnClickListener(new ButtonClickListener());
		
		
	}
	
	/**
	 * React according to the activity return state
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		super.onActivityResult(requestCode, resultCode, data);
		switch(requestCode){
		case BicycleUtil.RequestSetStartEndLoc:
			//CHeck whether return result is back button pressed
			if(resultCode != BicycleUtil.ResultSetStartEndLoc)
				break;
			int status = 0x00;
			Bundle b = data.getExtras();
			//Set start end point 
			if(b.getDouble("startLocLat", 0) != 0){
				if(start == null)
					start = new LocationData();
				start.latitude = b.getDouble("startLocLat");
				start.longitude = b.getDouble("startLocLong");
				status = status | 0x01;
			}else{
				start = null;
				status = status & 0x10;
			}
			if(b.getDouble("endLocLat",0) != 0){
				if(end == null)
					end  = new LocationData();
				end.latitude = b.getDouble("endLocLat");
				end.longitude = b.getDouble("endLocLong");
				status = status | 0x10;
			}else{
				end = null;
				status  = status & 0x01;
			}
			if(status == 0x00)
				selectLocString = "选择地点(未设置)";
			else if(status == 0x01)
				selectLocString = "选择地点(未设置终点)";
			else if(status == 0x10)
				selectLocString = "选择地点(未设置起点)";
			else if(status == 0x11)
				selectLocString = "选择地点(已设置)";
			btnSelectLoc.setText(selectLocString);
			this.selectLocStatus = (int)status;
			
			//Set the distance and estimated time
			if(b.getInt("routeDistance",0) != 0){
				distance  = b.getInt("routeDistance");
				time = (int)((double)distance/(double)4.2);
				tvEstimateDistance.setText(distance + "米");
				tvExpectedTime.setText(time/3600 + "时" + (time % 3600) / 60 +"分"+ time % 60 +"秒");
			}
			break;
		default:
			//do nothing
			break;
		}
	}
	
	/**
	 * Set the plan model detail so that can submit to 
	 * server
	 */
	protected void setPlanDetail(){
		if(p == null)
			p = new Plan();
		String name = etPlanName.getText().toString().trim();
		String pplExpected = btPplExpected.getText().toString().trim();
		String remark = etRemark.getEditableText().toString().trim();

		Date date = UtilFunction.convertDateString(btnDatePicker.getText().toString());
		Calendar timeHourMin = UtilFunction.convertStringToHourMinute(btnTimePicker.getText().toString());
		Calendar plandate = Calendar.getInstance();
		plandate.setTime(date);
		plandate.set(Calendar.HOUR_OF_DAY, timeHourMin.get(Calendar.HOUR_OF_DAY));
		plandate.set(Calendar.MINUTE, timeHourMin.get(Calendar.MINUTE));
		
		p.setName(name);
		p.setStartlocation((int)start.latitude+"|"+(int)start.longitude);
		p.setEndlocation((int)end.latitude + "|"+(int)end.longitude);
		p.setDistance(distance +"");
		p.setExpecttime(time+"");
		if(pplExpected != null)
			p.setPplexpected(pplExpected);
		if(remark != null)
			p.setDescription(remark);
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.CHINA);
		p.setPlandate(format.format(plandate.getTime()));
		
		p.setCreatedate(Calendar.getInstance(Locale.CHINA).getTime());
	}
	
	/**
	 * Call back api from submit plan
	 */
	public void planCreated(){
		this.setResult(BicycleUtil.CREATE_PLAN_SUCCESS);
		this.finish();
	}
	
	
	class ClickToMapListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			Intent intent  = new Intent(QuickPlanActivity.this, MapActivity.class);
			Bundle bundle = new Bundle();
			bundle.putInt("requestCode", BicycleUtil.RequestSetStartEndLoc);
			if(start != null){
				bundle.putDouble("startLocLat", start.latitude);
				bundle.putDouble("startLocLong", start.longitude);
			}
			if(end != null){
				bundle.putDouble("endLocLat", end.latitude);
				bundle.putDouble("endLocLong", end.longitude);
			}
			intent.putExtras(bundle);
			QuickPlanActivity.this.startActivityForResult(intent, BicycleUtil.RequestSetStartEndLoc);
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
                        String[] items = getResources().getStringArray(R.array.pplcount);
                        btPplExpected.setText(items[which]);
                    }
                })
                .create().show();
				break;
			case R.id.plan_submit:
				if(checkValidatity()){
					setPlanDetail();
					api.createplan(QuickPlanActivity.this,p);
				}
				break;
			case R.id.btn_date_picker:
				Calendar c = Calendar.getInstance();
				Date date = UtilFunction.convertDateString(btnDatePicker.getText().toString());
				if(date != null)
					c.setTime(date);
				DateSelectionDialogFragment datePicker = DateSelectionDialogFragment.getInstance(QuickPlanActivity.this,c, datePickerListener);
				datePicker.show(getSupportFragmentManager(), "日期选择");
				break;
			case R.id.btn_time_picker:
				Calendar time = UtilFunction.convertStringToHourMinute(btnTimePicker.getText().toString());
				if(time == null)
					time = Calendar.getInstance();
				TimeSelectionDialogFragment timePicker = TimeSelectionDialogFragment.getInstance(QuickPlanActivity.this, time, timePickerListener);
				timePicker.show(getSupportFragmentManager(), "时间选择");
				break;
			default:
				break;
			}
		}
		
		
		/**
		 * Check whether the plan detail entered is valid
		 * @return
		 */
		public boolean checkValidatity(){
			String name = etPlanName.getText().toString().trim();
			if(name.equals("")){  
				Toast.makeText(QuickPlanActivity.this, getString(R.string.plan_name_not_invalid), Toast.LENGTH_SHORT).show();
				return false;
			}
			if(start == null || end == null){
				Toast.makeText(QuickPlanActivity.this, "请设置开始和结束地点", Toast.LENGTH_SHORT).show();
				return false;
			}
			Date date = UtilFunction.convertDateString(btnDatePicker.getText().toString());
			Calendar time = UtilFunction.convertStringToHourMinute(btnTimePicker.getText().toString());
			if(date == null || time == null){
				Toast.makeText(QuickPlanActivity.this, "请设置出发时间", Toast.LENGTH_SHORT).show();
				return false;
			}
			return true;
		}
		
		
	}
	
	protected DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener(){
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			btnDatePicker.setText(year+"-"+monthOfYear+"-"+dayOfMonth);
		}
	};
	
	protected TimePickerDialog.OnTimeSetListener timePickerListener = new OnTimeSetListener(){
		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			btnTimePicker.setText(hourOfDay +":"+minute);
		}
	};
}

