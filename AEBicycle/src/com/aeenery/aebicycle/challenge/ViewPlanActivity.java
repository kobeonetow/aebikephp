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

public class ViewPlanActivity extends PlanListActivity {

	@Override
	protected void loadPlans(){
		api.getCurrentPlanList(this, "1", "10");
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		super.onActivityResult(requestCode, resultCode, data);
		Bundle bundle = data.getExtras();
		int position = bundle.getInt("position", -1);
		if(position < 0 ||position >= plans.length)
			return;
		if(requestCode == BicycleUtil.VIEW_PLAN){
			if(resultCode == BicycleUtil.VIEW_PLAN_FINISH){
				Plan retPlan = (Plan)bundle.getSerializable("plan");
				plans[position] = retPlan;
				lv.setAdapter(new PlanViewAdapter(ViewPlanActivity.this, R.layout.listview_plan_adapter, plans));
			}else if(resultCode == BicycleUtil.VIEW_PLAN_DELETE){
				Plan[] newplans = new Plan[plans.length-1];
				int index = 0;
				for(int i=0;i<plans.length-1;i++){
					if(i == position)
						index = 1;
					newplans[i] = plans[i+index];
				}
				plans = newplans;
				lv.setAdapter(new PlanViewAdapter(ViewPlanActivity.this, R.layout.listview_plan_adapter, plans));
			}
		}
	}
	
}
