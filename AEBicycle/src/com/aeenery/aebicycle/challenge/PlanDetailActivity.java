package com.aeenery.aebicycle.challenge;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.aeenery.aebicycle.BaseActivity;
import com.aeenery.aebicycle.LoginActivity;
import com.aeenery.aebicycle.R;
import com.aeenery.aebicycle.entry.BicycleUtil;
import com.aeenery.aebicycle.model.Plan;
import com.aeenery.aebicycle.model.ServerAPI;

public class PlanDetailActivity extends BaseActivity {

	private Plan p;
	private int position;
	private TextView tvName;
	private TextView tvStartLoc;
	private TextView tvTerminateLoc;
	private TextView tvExpectedDistance;
	private TextView tvExpectedTime;
	private TextView tvExpectedPpl;
	private TextView tvJoinPpl;
//	private TextView tvInterestPpl;
	private TextView tvSponspr;
	private TextView tvRemark;
	private TextView tvPlanId;
	private TextView tvStartTime;
	private TextView tvEndTime;
	private TextView tvStatus;
	private TextView tvUserid;
	private Button btnJoin;
	private Button btnQuit;
	private Button btnInterest;
	private Button btnDelete;
	private Button btnUpdate;
	private Button btnCheckRoute;
	private Button btnStartPlan;
	private Button btnEndPlan;
	
	private ServerAPI api;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_plan_detail);
		init();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_plan_detail, menu);
		return true;
	}
	
	public void init(){
		p = (Plan) getIntent().getSerializableExtra("plan");
		position = getIntent().getIntExtra("position", -1);
//		Log.i("New Activity","Name is :"+p.getName());
		if(p == null){
			Toast.makeText(this, "Plan Not Loaded", Toast.LENGTH_LONG).show();
			return;
		}
		api = new ServerAPI(PlanDetailActivity.this, LoginActivity.user);
		
		tvName = (TextView)findViewById(R.id.plan_detail_name);
		tvStartLoc = (TextView)findViewById(R.id.plan_detail_start_location);
		tvTerminateLoc = (TextView)findViewById(R.id.plan_detail_terminate_location);
		tvExpectedDistance = (TextView)findViewById(R.id.plan_detail_expected_distance);
		tvExpectedTime = (TextView)findViewById(R.id.plan_detail_expected_time);
		tvExpectedPpl = (TextView)findViewById(R.id.plan_detail_ppl_expected);
		tvJoinPpl = (TextView)findViewById(R.id.plan_detail_ppl_going);
//		tvInterestPpl = (TextView)findViewById(R.id.);
		tvSponspr = (TextView)findViewById(R.id.plan_detail_sponsor);
		tvRemark = (TextView)findViewById(R.id.plan_detail_remark);
		tvPlanId = (TextView)findViewById(R.id.plan_detail_id);
		tvStartTime = (TextView)findViewById(R.id.plan_detail_start_time);
		tvEndTime = (TextView)findViewById(R.id.plan_detail_end_time);
		tvStatus = (TextView)findViewById(R.id.plan_detail_status);
		tvUserid = (TextView)findViewById(R.id.plan_detail_creator);
		btnJoin = (Button)findViewById(R.id.plan_detail_join_plan);
		btnQuit = (Button)findViewById(R.id.plan_detail_quit_plan);
		btnInterest = (Button)findViewById(R.id.plan_detail_interest_plan);
		btnDelete = (Button)findViewById(R.id.plan_detail_cancel_plan);
		btnUpdate = (Button)findViewById(R.id.plan_detail_update_plan);
		btnCheckRoute = (Button)findViewById(R.id.plan_detail_check_route);
		btnStartPlan = (Button)findViewById(R.id.plan_detail_start_plan);
		btnEndPlan = (Button)findViewById(R.id.plan_detail_end_plan);
		
		this.updatePlanInformation();
		
		btnJoin.setOnClickListener(new JoinListener());
		btnQuit.setOnClickListener(new QuitListener());
		btnInterest.setOnClickListener(new InterestListener());
		btnDelete.setOnClickListener(new DeleteListener());
		btnUpdate.setOnClickListener(new UpdateListener());
		btnCheckRoute.setOnClickListener(new CheckRouteListener());
		btnStartPlan.setOnClickListener(new StartPlanListener());
		btnEndPlan.setOnClickListener(new EndPlanListener());
		
		displayUserView();
	}
	
	public void updatePlanInformation(){
		tvName.setText(p.getName());
		tvStartLoc.setText(p.getStartlocation());
		tvTerminateLoc.setText(p.getEndlocation());
		tvExpectedDistance.setText(p.getDistance() + "米");
		tvExpectedTime.setText(p.getExpecttime()+"秒");
		tvExpectedPpl.setText(p.getPplexpected());
		tvJoinPpl.setText(p.getPplgoing());
		tvSponspr.setText(p.getSponsor());
		tvRemark.setText(p.getDescription());
		tvPlanId.setText(p.getId());
		if(p.getStarttime().equals("-"))
			tvStartTime.setText("-");
		else
			tvStartTime.setText(p.getStarttime());
		if(p.getEndtime().equals("-"))
			tvEndTime.setText("-");
		else
			tvEndTime.setText(p.getEndtime());
		tvStatus.setText(p.getStatus());
		tvUserid.setText(p.getUserid());
	}
	
	public void setButtonsInvisibleAndDisable(){
		btnJoin.setVisibility(View.GONE);
		btnQuit.setVisibility(View.GONE);
		btnInterest.setVisibility(View.GONE);
		btnDelete.setVisibility(View.GONE);
		btnUpdate.setVisibility(View.VISIBLE);
		btnStartPlan.setVisibility(View.GONE);
		
		btnJoin.setEnabled(false);
		btnQuit.setEnabled(false);
//		btnInterest.setEnabled(false);
		btnDelete.setEnabled(false);
		btnUpdate.setEnabled(true);
	}
	
	public void hideStartPlanBtn(){
		btnStartPlan.setVisibility(View.GONE);
		btnStartPlan.setEnabled(false);
	}
	
	public void showEndPlanBtn(){
		btnEndPlan.setVisibility(View.VISIBLE);
		btnEndPlan.setEnabled(true);
	}
	
	public void hideEndPlanBtn(){
		btnEndPlan.setVisibility(View.GONE);
		btnEndPlan.setEnabled(false);
	}
	
	public void displayUserView(){
		setButtonsInvisibleAndDisable();
		if(p.getUserid() != null && p.getUserid().equals(LoginActivity.user.getId())){
			this.creatorView();
			return;
		}
		
		switch(Integer.parseInt(p.__getAssignStatus())){
		case BicycleUtil.STATUS_PLAN_NOT_ASSIGN:
			this.notJoinView();
			break;
		case BicycleUtil.STATUS_PLAN_ACCEPT:
			this.joinedView();
			break;
		case BicycleUtil.STATUS_PLAN_FINISH:
//			this.setButtonsInvisibleAndDisable();
			break;
		case BicycleUtil.STATUS_PLAN_TERMINATED:
//			this.setButtonsInvisibleAndDisable();
			break;
		case BicycleUtil.STATUS_PLAN_START:
//			this.setButtonsInvisibleAndDisable();
			break;
		case BicycleUtil.STATUS_PLAN_INTEREST:
			this.notJoinView();
			break;
		}
	}
	
	public void joinedView(){
		btnQuit.setEnabled(true);
		btnQuit.setVisibility(View.VISIBLE);
	}
	
	public void notJoinView(){
		btnJoin.setEnabled(true);
		btnJoin.setVisibility(View.VISIBLE);
	}
	
	public void creatorView(){
		btnDelete.setEnabled(true);
		btnDelete.setVisibility(View.VISIBLE);
		if(p.getStarttime().equals("-")){
			btnStartPlan.setEnabled(true);
			btnStartPlan.setVisibility(View.VISIBLE);
		}else if(p.getEndtime().equals("-")){
			this.showEndPlanBtn();
		}
	}
	
	public void close(JSONObject json){
		String result = null;
		try {
			result = json.getString("result");
			if(result.equals("1")){
				this.finish();
			}
		} catch (JSONException e) {
			Log.i("PlanDetailActivity","JSON result return fail:"+result);
			e.printStackTrace();
		}
	}
	
	public void joinAPlan(String planid){
		if(planid.equals(this.p.getId())){
			p.__setAssignStatus(BicycleUtil.STATUS_PLAN_ACCEPT +"");
			this.setButtonsInvisibleAndDisable();
			this.joinedView();
		}
	}
	
	public void quitAPlan(String planid,JSONObject json){
		if(planid.equals(this.p.getId())){
			String result = null;
			try {
				result = json.getString("result");
				if(result.equals("1")){
					p.__setAssignStatus(BicycleUtil.STATUS_PLAN_NOT_ASSIGN +"");
					this.setButtonsInvisibleAndDisable();
					this.notJoinView();
				}
			} catch (JSONException e) {
				Log.i("PlanDetailActivity","JSON result return fail:"+result);
				e.printStackTrace();
			}
		}
	}
	
	public void updateAPlan(String planid, JSONObject json){
		if(planid.equals(this.p.getId())){
			JSONObject result = null;
				result = json.optJSONObject("result");
				if(result != null){
				if(result.has("id")){
					p.__setPlanFromJSONObject(result);
					this.updatePlanInformation();
				}
			}
		}
	}
	
	@Override
	public void onBackPressed(){
		Intent ret = new Intent();
		Bundle bundle = new Bundle();
		bundle.putSerializable("plan", p);
		ret.putExtras(bundle);
		ret.putExtra("position", position);
		this.setResult(BicycleUtil.BACK_TO_VIEW_PLAN, ret);
//		Log.i("PLANDETAIL","BACK TO VIEW PLAN");
		this.finish();
	}
	
	class JoinListener implements OnClickListener{
		@Override
		public void onClick(View arg0) {
			api.joinPlan(PlanDetailActivity.this, p);
		}
	}
	class QuitListener implements OnClickListener{
		@Override
		public void onClick(View arg0) {
			api.quitPlan(PlanDetailActivity.this, p);
		}
	}
	class InterestListener implements OnClickListener{
		@Override
		public void onClick(View arg0) {
//			api.interestPlan(PlanDetailActivity.this, p);
		}
		
	}
	class DeleteListener implements OnClickListener{
		@Override
		public void onClick(View arg0) {
			api.detelePlan(PlanDetailActivity.this, p);
		}
	}
	class UpdateListener implements OnClickListener{
		@Override
		public void onClick(View arg0) {
			api.updatePlan(PlanDetailActivity.this, p);
		}
	}
	class CheckRouteListener implements OnClickListener{
		@Override
		public void onClick(View arg0) {
			Intent intent = new Intent(PlanDetailActivity.this, RouteSelectionActivity.class);
			Bundle bundle = new Bundle();
			bundle.putBoolean("checkRoute", true);
			String start = p.getStartlocation();
			String end = p.getEndlocation();
			if(start.length() > 0){
				bundle.putBoolean("hasStart", true);
				bundle.putString("startPoiName", start.substring(0, start.indexOf("(")));
				bundle.putInt("startPoiLat", Integer.parseInt(start.substring(start.indexOf("(")+1, start.indexOf(","))));
				bundle.putInt("startPoiLon", Integer.parseInt(start.substring(start.indexOf(",")+1, start.indexOf(")"))));
			}
			if(end.length() > 0){
				bundle.putBoolean("hasTerminate", true);
				bundle.putString("terminatePoiName", end.substring(0, end.indexOf("(")));
				bundle.putInt("terminatePoiLat", Integer.parseInt(end.substring(end.indexOf("(")+1, end.indexOf(","))));
				bundle.putInt("terminatePoiLon", Integer.parseInt(end.substring(end.indexOf(",")+1, end.indexOf(")"))));
			}
			intent.putExtras(bundle);
			PlanDetailActivity.this.startActivity(intent);
		}
	}
	
	class StartPlanListener implements OnClickListener{
		@Override
		public void onClick(View v) {
			api.startPlan(PlanDetailActivity.this,p);
		}
	}
	
	public void runPlanActivity(){
		Intent intent = new Intent(PlanDetailActivity.this,RunPlanActivity.class);
		this.startActivityForResult(intent, BicycleUtil.RUN_PLAN);
	}
	
	class EndPlanListener implements OnClickListener{
		@Override
		public void onClick(View v) {
			api.endPlan(PlanDetailActivity.this,p);
			
		}
	}
}
