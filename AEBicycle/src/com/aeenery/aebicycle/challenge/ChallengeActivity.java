package com.aeenery.aebicycle.challenge;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.aeenery.aebicycle.BaseListActivity;
import com.aeenery.aebicycle.DevelopingActivity;
import com.aeenery.aebicycle.R;
import com.aeenery.aebicycle.entry.BicycleUtil;
import com.aeenery.aebicycle.entry.UtilFunction;
import com.aeenery.aebicycle.friend.FriendListActivity;

public class ChallengeActivity extends BaseListActivity{
	
	protected AlertDialog.Builder dialogBuilder;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		UtilFunction.login(this);
		init();
		
	}
	
	//Style listview和它的内容
	public void init(){
		Resources res = getResources();
		String[] challengeList = res.getStringArray(R.array.challengemainlist);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,android.R.id.text1,challengeList);
		setListAdapter(adapter);	
		dialogBuilder = new AlertDialog.Builder(this);
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, final int position, final long id){
		super.onListItemClick(l, v, position, id);
		Intent intent = new Intent();
		switch(position){
		case BicycleUtil.SetupPlan:
			intent.setClass(ChallengeActivity.this, QuickPlanActivity.class);
			ChallengeActivity.this.startActivityForResult(intent,BicycleUtil.CREATE_PLAN);
			break;
		case BicycleUtil.ViewPlan:
			intent.setClass(ChallengeActivity.this, ViewPlanActivity.class);
			ChallengeActivity.this.startActivity(intent);
			break;
		case BicycleUtil.MyPlans:
			intent.setClass(ChallengeActivity.this, );
			ChallengeActivity.this.startActivity(intent);
			break;
		case BicycleUtil.JoinedPlans:
			ChallengeActivity.this.startActivity(intent);
			break;
		case BicycleUtil.FinishedPlans:
			ChallengeActivity.this.startActivity(intent);
			break;
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		switch(requestCode){
		case BicycleUtil.CREATE_PLAN:
			if(resultCode == BicycleUtil.CREATE_PLAN_SUCCESS)
				Toast.makeText(this, this.getString(R.string.plan_create_success), Toast.LENGTH_LONG).show();
			break;
		case BicycleUtil.RequireLogin:
			if(resultCode != BicycleUtil.LoginSuccess)
				this.finish();
			break;
		default:
			break;
		}
	}
}
