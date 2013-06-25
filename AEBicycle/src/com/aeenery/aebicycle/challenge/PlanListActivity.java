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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.aeenery.aebicycle.BaseActivity;
import com.aeenery.aebicycle.LoginActivity;
import com.aeenery.aebicycle.R;
import com.aeenery.aebicycle.entry.BicycleUtil;
import com.aeenery.aebicycle.model.Plan;
import com.aeenery.aebicycle.model.ServerAPI;

public class PlanListActivity extends BaseActivity {
	
	protected ServerAPI api = null;
	protected ListView lv = null;
	protected Plan[] plans;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		api = new ServerAPI(this, LoginActivity.user);
		setContentView(R.layout.activity_view_plan);
		lv = (ListView)findViewById(R.id.view_plan_listview);
		lv.setOnItemClickListener(new PlanClickListener());
		
		loadPlans();
	}
	
	/**
	 * Need to be overwritten to load content from database
	 */
	protected void loadPlans() {
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_view_plan, menu);
		return true;
	}
	
	public void setPlansToView(JSONObject json){
		try{
			JSONArray json2 = json.getJSONArray("data");
			if(json.getString("result").equals("2")){
				Toast.makeText(this, "No plans found", Toast.LENGTH_LONG).show();
				return;
			}
			plans = new Plan[json2.length()];
			for(int i=0; i<json2.length(); i++){
				JSONObject curIndex = ((JSONObject)json2.get(i));
				Plan p = new Plan();
				p.__setPlanFromJSONObject(curIndex);
				plans[i] = p;
			}
			lv.setAdapter(new PlanViewAdapter(PlanListActivity.this, R.layout.listview_plan_adapter, plans));
		}catch(Exception e){
			Log.i("PlanListActivity",e.getMessage());
		}
	}
	
	class PlanClickListener implements OnItemClickListener{
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Plan plan = (Plan) lv.getItemAtPosition(position);
			Intent intent = new Intent();
			intent.setClass(PlanListActivity.this, PlanDetailActivity.class);
			Bundle bundle = new Bundle();
			bundle.putSerializable("plan", plan);
			bundle.putInt("position",position);
			intent.putExtras(bundle);
			PlanListActivity.this.startActivityForResult(intent, BicycleUtil.VIEW_PLAN);
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
			if(p.getPlandate().equals(""))
				started.setText("N");
			else
				started.setText("Y");
			startLocation.setText("起点:" + p.getStartlocation());
			pplJoin.setText("参加人数:" + p.getPplgoing());
			
			return row;
		}
		
	}
}
