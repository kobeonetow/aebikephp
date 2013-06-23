package com.aeenery.aebicycle.challenge;

import org.json.JSONException;
import org.json.JSONObject;


import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.aeenery.aebicycle.map.MapActivity;
import com.aeenery.aebicycle.map.MapActivity.AESearchListener;
import com.aeenery.aebicycle.model.Plan;
import com.aeenery.aebicycle.model.ServerAPI;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.MKEvent;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.PoiOverlay;
import com.baidu.mapapi.map.RouteOverlay;
import com.baidu.mapapi.search.MKAddrInfo;
import com.baidu.mapapi.search.MKBusLineResult;
import com.baidu.mapapi.search.MKDrivingRouteResult;
import com.baidu.mapapi.search.MKPlanNode;
import com.baidu.mapapi.search.MKPoiInfo;
import com.baidu.mapapi.search.MKPoiResult;
import com.baidu.mapapi.search.MKSearch;
import com.baidu.mapapi.search.MKSearchListener;
import com.baidu.mapapi.search.MKSuggestionResult;
import com.baidu.mapapi.search.MKTransitRouteResult;
import com.baidu.mapapi.search.MKWalkingRouteResult;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.aeenery.aebicycle.AEApplication;

public class PlanDetailActivity extends BaseActivity {

	private Plan p;
	private int position;
//	private TextView tvName;s
	private TextView tvExpectedDistance;
	private TextView tvExpectedTime;
	private TextView tvExpectedPpl;
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
	private Button btnStartPlan;
	private Button btnEndPlan;
	
	private ServerAPI api;
	
	protected BMapManager mBMapMan = null;
	protected MapView mMapView;
	protected MKSearch mMKSearch = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_plan_detail);
		api = new ServerAPI(PlanDetailActivity.this, LoginActivity.user);
		//Initalise map main class
		mBMapMan = ((AEApplication)getApplication()).getBMapManager();
		mMKSearch = new MKSearch();
		mMKSearch.init(mBMapMan, new AESearchListener());
		
		//Initialise parameters
		init();
		findViewsAndBindViews();
		setContents();
	}

	protected void init(){
		p = (Plan) getIntent().getSerializableExtra("plan");
		position = getIntent().getExtras().getInt("position", -1);
		
		if(p == null){
			Toast.makeText(this, "Plan Not Loaded", Toast.LENGTH_LONG).show();
			this.finish();
			return;
		}
	}
	
	protected void findViewsAndBindViews() {
//		tvName = (TextView)findViewById(R.id.plan_detail_name);
		
		tvUserid = (TextView)findViewById(R.id.plan_detail_creator);
		tvExpectedDistance = (TextView)findViewById(R.id.view_plan_estimate_distance);
		tvExpectedTime = (TextView)findViewById(R.id.view_plan_expected_time);
		tvExpectedPpl = (TextView)findViewById(R.id.view_pplexpected);
		tvRemark = (TextView)findViewById(R.id.view_planremark);
		tvStatus = (TextView)findViewById(R.id.plan_detail_status);
		
		tvPlanId = (TextView)findViewById(R.id.plan_detail_id); //hide
		
		
		btnJoin = (Button)findViewById(R.id.plan_detail_join_plan);
		btnQuit = (Button)findViewById(R.id.plan_detail_quit_plan);
		btnInterest = (Button)findViewById(R.id.plan_detail_interest_plan);
		btnDelete = (Button)findViewById(R.id.plan_detail_cancel_plan);
		btnUpdate = (Button)findViewById(R.id.plan_detail_update_plan);
		btnStartPlan = (Button)findViewById(R.id.plan_detail_start_plan);
		btnEndPlan = (Button)findViewById(R.id.plan_detail_end_plan);
		
		btnJoin.setOnClickListener(new JoinListener());
		btnQuit.setOnClickListener(new QuitListener());
		btnInterest.setOnClickListener(new InterestListener());
		btnDelete.setOnClickListener(new DeleteListener());
		btnUpdate.setOnClickListener(new UpdateListener());
		btnStartPlan.setOnClickListener(new StartPlanListener());
		btnEndPlan.setOnClickListener(new EndPlanListener());
		
		//Show route on map
		initailiseMapView();
		
		displayUserView();
	}

	private void initailiseMapView() {
		mMapView = (MapView)findViewById(R.id.bmapsViewNoneEditable);
		mMapView.setBuiltInZoomControls(true);
		MapController mMapController=mMapView.getController();
		
		//Get latitude and longtitude
		String startLocStr[] = p.getStartlocation().split("\\|");
		String endLocStr[] = p.getEndlocation().split("\\|");
		//Create two points
		GeoPoint startPoint =new GeoPoint(Integer.parseInt(startLocStr[0]),Integer.parseInt(startLocStr[1]));
		GeoPoint endPoint =new GeoPoint(Integer.parseInt(endLocStr[0]),Integer.parseInt(endLocStr[1]));
		
		//Create the nodes
		MKPlanNode startNode = new MKPlanNode();
		startNode.pt = startPoint;
		MKPlanNode endNode = new MKPlanNode();
		endNode.pt = endPoint;
		
		mMapController.setCenter(startPoint);//设置地图中心点
		mMapController.setZoom(12);//设置地图zoom级别
		
		mMKSearch.walkingSearch(null, startNode, null, endNode);
	}

	protected void setContents() {
//		tvName.setText(p.getName());
		setTitle(p.getName());
		tvExpectedDistance.setText(p.getDistance() + "米");
		
		int time = 0;
		if(p.getExpecttime() != null) time = Integer.parseInt(p.getExpecttime());
		tvExpectedTime.setText(time/3600 + "时" + (time % 3600) / 60 +"分"+ time % 60 +"秒");
		
		tvExpectedPpl.setText(p.getPplgoing() +"/"+p.getPplexpected());
		tvRemark.setText(p.getDescription());
		tvPlanId.setText(p.getId());
		
		tvStatus.setText(p.getStatus());
		tvUserid.setText(p.getUserid());
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_plan_detail, menu);
		return true;
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
					setContents();
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
		this.setResult(BicycleUtil.VIEW_PLAN_FINISH, ret);
//		Log.i("PLANDETAIL","BACK TO VIEW PLAN");
		this.finish();
	}
	
	@Override
	protected void onDestroy(){
	    mMapView.destroy();
	    if(mBMapMan!=null){
	        mBMapMan=null;
	    }
	    super.onDestroy();
	}
	@Override
	protected void onPause(){
	        mMapView.onPause();
	        if(mBMapMan!=null){
	                mBMapMan.stop();
	        }
	        super.onPause();
	}
	@Override
	protected void onResume(){
	        mMapView.onResume();
	        if(mBMapMan!=null){
	                mBMapMan.start();
	        }
	        super.onResume();
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
	class StartPlanListener implements OnClickListener{
		@Override
		public void onClick(View v) {
			api.startPlan(PlanDetailActivity.this,p);
		}
	}
	
	public void runPlanActivity(){
//		Intent intent = new Intent(PlanDetailActivity.this,RunPlanActivity.class);
//		this.startActivityForResult(intent, BicycleUtil.RUN_PLAN);
	}
	
	class EndPlanListener implements OnClickListener{
		@Override
		public void onClick(View v) {
			api.endPlan(PlanDetailActivity.this,p);
			
		}
	}
	
	public class AESearchListener implements MKSearchListener {

		@Override
		public void onGetAddrResult(MKAddrInfo arg0, int arg1) {
		}

		@Override
		public void onGetBusDetailResult(MKBusLineResult arg0, int arg1) {
		}

		@Override
		public void onGetDrivingRouteResult(MKDrivingRouteResult arg0, int arg1) {
		}

		@Override
		public void onGetPoiDetailSearchResult(int arg0, int arg1) {
		}

		@Override
		public void onGetPoiResult(MKPoiResult result, int type, int error) {
		}

		@Override
		public void onGetSuggestionResult(final MKSuggestionResult result, int error) {
		}

		@Override
		public void onGetTransitRouteResult(MKTransitRouteResult arg0, int arg1) {
		}

		@Override
		public void onGetWalkingRouteResult(MKWalkingRouteResult result, int error) {
			if (result == null) {  
                return;  
			}  
			RouteOverlay routeOverlay = new RouteOverlay(PlanDetailActivity.this, mMapView);
			routeOverlay.setData(result.getPlan(0).getRoute(0));  
	        mMapView.getOverlays().add(routeOverlay);  
	        mMapView.refresh();  
	        
		}

	}
}
