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

	
	public static final int JOIN_PLAN = 1;
	public static final int QUIT_PLAN = 2;
	public static final int UPDATE_PLAN = 3;
	public static final int DELETE_PLAN = 4;
	
	private Plan p;
	private int position;
	private TextView tvStartTime;
	private TextView tvExpectedDistance;
	private TextView tvExpectedTime;
	private TextView tvExpectedPpl;
	private TextView tvRemark;
	private TextView tvPlanId;
	private TextView tvStatus;
	private TextView tvUserid;
	private Button btnJoin;
	private Button btnQuit;
	private Button btnDelete;
	private Button btnUpdate;
	
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
		tvStartTime = (TextView)findViewById(R.id.plan_detail_start_time);
		
		tvPlanId = (TextView)findViewById(R.id.plan_detail_id); //hide
		
		
		btnJoin = (Button)findViewById(R.id.plan_detail_join_plan);
		btnQuit = (Button)findViewById(R.id.plan_detail_quit_plan);
		btnDelete = (Button)findViewById(R.id.plan_detail_cancel_plan);
		btnUpdate = (Button)findViewById(R.id.plan_detail_update_plan);
		
		btnJoin.setOnClickListener(new PlanDetailButtonClickListener());
		btnQuit.setOnClickListener(new PlanDetailButtonClickListener());
		btnDelete.setOnClickListener(new PlanDetailButtonClickListener());
		btnUpdate.setOnClickListener(new PlanDetailButtonClickListener());
		
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
		
		tvExpectedPpl.setText((Integer.parseInt(p.getPplgoing())+1) +"/"+p.getPplexpected());
		tvRemark.setText(p.getDescription());
		tvPlanId.setText(p.getId());
		tvStartTime.setText(p.getPlandate());
		tvStatus.setText(p.getStatus());
		tvUserid.setText(p.getUserid());
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_plan_detail, menu);
		return true;
	}
	
	
	public void displayUserView(){
		//Enable update button
		btnUpdate.setVisibility(View.VISIBLE);
		btnUpdate.setEnabled(true);
		
		//First make all invisible then filter out step by step
		btnJoin.setVisibility(View.GONE);
		btnQuit.setVisibility(View.GONE);
		btnDelete.setVisibility(View.GONE);
//		btnStartPlan.setVisibility(View.GONE);
//		btnEndPlan.setVisibility(View.GONE);
		
		//If viewing own plan
		if(p.getUserid() != null && p.getUserid().equals(LoginActivity.user.getId())){
			btnDelete.setVisibility(View.VISIBLE);
			return;
		}
		
		switch(Integer.parseInt(p.__getAssignStatus())){
		case BicycleUtil.STATUS_PLAN_NOT_ASSIGN:
			btnJoin.setVisibility(View.VISIBLE);
			break;
		case BicycleUtil.STATUS_PLAN_ACCEPT:
			btnQuit.setVisibility(View.VISIBLE);
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
		}
		
	}
			
	public void callBackAfterClick(JSONObject json, String id, int request){
		try{
		switch(request){
		case JOIN_PLAN:
			if(id.equals(this.p.getId())){
				p.__setAssignStatus(BicycleUtil.STATUS_PLAN_ACCEPT +"");
				btnJoin.setEnabled(true);
				btnJoin.setVisibility(View.GONE);
				btnQuit.setVisibility(View.VISIBLE);
				String members  = json.getJSONObject("data").getString("memebers");
				p.setPplgoing(members);
				setContents();
			}
			break;
		case QUIT_PLAN:
			if(id.equals(this.p.getId())){
				p.__setAssignStatus(BicycleUtil.STATUS_PLAN_NOT_ASSIGN +"");
				btnQuit.setEnabled(true);
				btnQuit.setVisibility(View.GONE);
				btnJoin.setVisibility(View.VISIBLE);
				String members  = json.getJSONObject("data").getString("memebers");
				p.setPplgoing(members);
				setContents();
			}
			break;
		case UPDATE_PLAN:
			if(id.equals(this.p.getId())){
				JSONObject result = json.optJSONObject("data");
					if(result != null){
						if(result.has("id")){
							p.__setPlanFromJSONObject(result);
						setContents();
					}
				}
				btnUpdate.setEnabled(true);
			}
			break;
		case DELETE_PLAN:
			Intent ret = new Intent();
			Bundle bundle = new Bundle();
			bundle.putInt("position", position);
			bundle.putString("planid", p.getId());
			ret.putExtras(bundle);
			this.setResult(BicycleUtil.VIEW_PLAN_DELETE, ret);
			this.finish();
			break;
		}
		}catch(Exception e){
			Log.e("PlanDetailActivity","Json return has error."+e.getMessage());
			return;
		}
	}
	
	@Override
	public void onBackPressed(){
		Intent ret = new Intent();
		Bundle bundle = new Bundle();
		bundle.putInt("position", position);
		bundle.putSerializable("plan", p);
		ret.putExtras(bundle);
		this.setResult(BicycleUtil.VIEW_PLAN_FINISH, ret);
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
	
	class PlanDetailButtonClickListener implements OnClickListener{
		@Override
		public void onClick(View v) {
			switch(v.getId()){
			case R.id.plan_detail_join_plan:
				btnJoin.setEnabled(false);
				api.joinPlan(PlanDetailActivity.this, p);
				break;
			case R.id.plan_detail_quit_plan:
				btnQuit.setEnabled(false);
				api.quitPlan(PlanDetailActivity.this, p);
				break;
			case R.id.plan_detail_update_plan:
				btnUpdate.setEnabled(false);
				api.updatePlan(PlanDetailActivity.this, p);
				break;
			case R.id.plan_detail_cancel_plan:
				btnDelete.setEnabled(false);
				api.detelePlan(PlanDetailActivity.this, p);
				break;
			}
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
