package com.aeenery.aebicycle.challenge;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.aeenery.aebicycle.BaseActivity;
import com.aeenery.aebicycle.LoginActivity;
import com.aeenery.aebicycle.R;
import com.aeenery.aebicycle.entry.BicycleUtil;
import com.aeenery.aebicycle.model.Plan;
import com.aeenery.aebicycle.model.ServerAPI;

public class ViewPlanActivity extends BaseActivity {
	
	private ServerAPI api = null;
	private ListView lv = null;
	private Plan[] plans;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.requiredLogin();
		api = new ServerAPI(this, LoginActivity.user);
		setContentView(R.layout.activity_view_plan);
		init();
	}

	private void init(){
		api.getCurrentPlanList(this, "1", "10");
		TextView title = (TextView)findViewById(R.id.view_plan_activity_title);
		title.setText("第一页");
		
		lv = (ListView)findViewById(R.id.view_plan_listview);
		lv.setOnItemClickListener(new PlanClickListener());
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_view_plan, menu);
		return true;
	}
	
	public void setPlansToView(JSONObject json){
		try{
		JSONArray json2 = json.getJSONArray("result");
		if(json2.length() <= 1){
				if(json2.length() == 1 && !((JSONObject)json2.get(0)).has("id")){
					Toast.makeText(this, "No plans found", Toast.LENGTH_LONG).show();
					return;
			}
		}
		plans = new Plan[json2.length()];
		for(int i=0; i<json2.length(); i++){
			JSONObject curIndex = ((JSONObject)json2.get(i));
			Plan p = new Plan();
			p.__setPlanFromJSONObject(curIndex);
			p.__setAssignStatus(curIndex.getString("assignstatus"));
			plans[i] = p;
			
		}
		lv.setAdapter(new PlanViewAdapter(ViewPlanActivity.this, R.layout.listview_plan_adapter, plans));
		}catch(Exception e){
			Log.i("ViewPlanActivity",e.getMessage());
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == resultCode){
//			Log.i("result","return success 1:"+requestCode + " 2:"+resultCode);
			Plan retPlan = (Plan)data.getSerializableExtra("plan");
			int position = data.getIntExtra("position", -1);
			if(position < 0 || position >= plans.length)
				return;
			plans[position] = retPlan;
			Log.i("Change", "status" + retPlan.__getAssignStatus());
			lv.setAdapter(new PlanViewAdapter(ViewPlanActivity.this, R.layout.listview_plan_adapter, plans));
		}
	}
	
	class PlanClickListener implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Plan plan = (Plan) lv.getItemAtPosition(position);
			Intent intent = new Intent();
			intent.setClass(ViewPlanActivity.this, PlanDetailActivity.class);
			Bundle bundle = new Bundle();
			bundle.putSerializable("plan", plan);
			intent.putExtra("position",position);
			intent.putExtras(bundle);
//			ViewPlanActivity.this.startActivity(intent);
			ViewPlanActivity.this.startActivityForResult(intent, BicycleUtil.BACK_TO_VIEW_PLAN);
		}
		
	}
	
	class PlanViewAdapter extends ArrayAdapter<Plan>{

		protected Context  context;
		protected int resourceId ;
		protected Plan[] plans;
		
		public PlanViewAdapter(Context context, int textViewResourceId,
				Plan[] plans) {
			super(context, textViewResourceId, plans);
			this.context = context;
			this.resourceId = textViewResourceId;
			this.plans = plans;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent){
			View row = convertView;
			if(row == null){
				LayoutInflater inflater = ((Activity)context).getLayoutInflater();
				row = inflater.inflate(resourceId, parent,false);
			}

			Plan p = plans[position];
			
			TextView name = (TextView)row.findViewById(R.id.view_plan_name);
			TextView distance = (TextView)row.findViewById(R.id.view_plan_distance);
			TextView started = (TextView)row.findViewById(R.id.view_plan_started);
			TextView startLocation = (TextView)row.findViewById(R.id.view_plan_start_location);
			TextView pplJoin = (TextView)row.findViewById(R.id.view_plan_members_count);
			
			
			name.setText(p.getName());
			distance.setText("路程:" + p.getDistance());
			if(p.getStarttime().substring(0, 1).equals("-"))
				started.setText("N");
			else
				started.setText("Y");
			startLocation.setText("起点:" + p.getStartlocation());
			pplJoin.setText("参加人数:" + p.getPplgoing());
			
			return row;
		}
		
	}
}
