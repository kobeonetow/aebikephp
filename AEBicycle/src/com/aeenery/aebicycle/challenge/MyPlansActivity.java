package com.aeenery.aebicycle.challenge;

public class MyPlansActivity extends PlanListActivity{

	@Override
	protected void loadPlans(){
		api.getMyOwnPlans(this);
	}
}
