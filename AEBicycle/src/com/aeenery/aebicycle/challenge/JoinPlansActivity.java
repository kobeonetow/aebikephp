package com.aeenery.aebicycle.challenge;

import android.content.Intent;
import android.os.Bundle;

import com.aeenery.aebicycle.R;
import com.aeenery.aebicycle.challenge.PlanListActivity.PlanViewAdapter;
import com.aeenery.aebicycle.entry.BicycleUtil;
import com.aeenery.aebicycle.model.Plan;

public class JoinPlansActivity extends PlanListActivity{
	
	@Override
	protected void loadPlans(){
		if(plans == null)
			api.getJoinedPlans(this, "0");
		else
			api.getJoinedPlans(this, plans.length+"");
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		super.onActivityResult(requestCode, resultCode, data);
		Bundle bundle = data.getExtras();
		int position = bundle.getInt("position", -1);
		if(plans == null || position < 0 ||position >= plans.length)
			return;
		if(requestCode == BicycleUtil.VIEW_PLAN){
			if(resultCode == BicycleUtil.VIEW_PLAN_FINISH){
				Plan retPlan = (Plan)bundle.getSerializable("plan");
				plans[position] = retPlan;
				lv.setAdapter(new PlanViewAdapter(JoinPlansActivity.this, R.layout.listview_plan_adapter, plans));
			}else if(resultCode == BicycleUtil.VIEW_PLAN_QUIT){
				Plan[] newplans = new Plan[plans.length-1];
				int index = 0;
				for(int i=0;i<plans.length-1;i++){
					if(i == position)
						index = 1;
					newplans[i] = plans[i+index];
				}
				plans = newplans;
				lv.setAdapter(new PlanViewAdapter(JoinPlansActivity.this, R.layout.listview_plan_adapter, plans));
			}
		}
	}
}
