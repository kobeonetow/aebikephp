package com.aeenery.aebicycle.challenge;

import android.content.Intent;
import android.os.Bundle;

import com.aeenery.aebicycle.R;
import com.aeenery.aebicycle.entry.BicycleUtil;
import com.aeenery.aebicycle.model.Plan;

public class FinishedPlansActivity extends PlanListActivity{

	@Override
	protected void loadPlans(){
		api.getFinishedPlans(this);
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
				lv.setAdapter(new PlanViewAdapter(FinishedPlansActivity.this, R.layout.listview_plan_adapter, plans));
			}
		}
	}
}
