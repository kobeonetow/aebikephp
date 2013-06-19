package com.aeenery.aebicycle.challenge;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.aeenery.aebicycle.BaseListActivity;
import com.aeenery.aebicycle.DevelopingActivity;
import com.aeenery.aebicycle.R;
import com.aeenery.aebicycle.entry.BicycleUtil;
import com.aeenery.aebicycle.entry.UtilFunction;

public class ChallengeActivity extends BaseListActivity{
	
	protected AlertDialog.Builder dialogBuilder;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_challenge);
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
		switch(position){
		case BicycleUtil.SetupPlan:
			setupPlan();
			break;
		case BicycleUtil.ViewPlan:
			viewPlan();
			break;
		case BicycleUtil.InviteFriend:
			inviteFriend();
			break;
		case BicycleUtil.ViewHistory:
			viewHistory();
			break;
		default:
			break;
		}
	}
	
//	public void setupPlan(){
//		String[] options = {"快速私人","组队","挑战","路线查询","微博"};
//		dialogBuilder.setTitle("").setItems(options, new DialogInterface.OnClickListener() {
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				Intent intent = new Intent();
//				switch(which){
//				case 0:
//					intent.setClass(ChallengeActivity.this, QuickPlanActivity.class);
//					ChallengeActivity.this.startActivity(intent);
//					break;
//				case 1:
//					intent.setClass(ChallengeActivity.this, NormalPlanActivity.class);
//					ChallengeActivity.this.startActivity(intent);
//					break;
//				case 2:
//					intent.setClass(ChallengeActivity.this, ChallengePlanActivity.class);
//					ChallengeActivity.this.startActivity(intent);
//					break;
//				case 3:
//					Bundle bundle = new Bundle();
//					//传递起点经纬度
//					bundle.putInt("srcLatitudeE6", 23203189);
//					bundle.putInt("srcLongitudeE6",113332441);
//					//传递终点经纬度
//					bundle.putInt("dstLatitudeE6", 23137897);
//					bundle.putInt("dstLongitudeE6",113368286);
//					//是否拥有分享按钮
//					bundle.putBoolean("ifShareBt", false);
//					intent.setClass(ChallengeActivity.this, RouteMapActivity.class);
//					intent.putExtras(bundle);
//					ChallengeActivity.this.startActivity(intent);
//					break;
//				case 4:
//					
//				default:
//					intent.setClass(ChallengeActivity.this, QuickPlanActivity.class);
//					ChallengeActivity.this.startActivity(intent);
//					break;
//				}
//			}
//		});
//		dialogBuilder.create();
//		dialogBuilder.show();
//	}
	
	public void setupPlan(){
		Intent intent = new Intent();
		intent.setClass(ChallengeActivity.this, QuickPlanActivity.class);
		ChallengeActivity.this.startActivityForResult(intent,BicycleUtil.CREATE_PLAN_SUCCESS);
	}

	public void viewPlan() {
		Intent intent  = new Intent();
		intent.setClass(ChallengeActivity.this, ViewPlanActivity.class);
		ChallengeActivity.this.startActivity(intent);
	}

	public void inviteFriend() {
		Intent intent  = new Intent();
		intent.setClass(ChallengeActivity.this, DevelopingActivity.class);
		ChallengeActivity.this.startActivity(intent);
	}

	public void viewHistory() {
		Intent intent  = new Intent();
		intent.setClass(ChallengeActivity.this, DevelopingActivity.class);
		ChallengeActivity.this.startActivity(intent);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		switch(requestCode){
		case BicycleUtil.CREATE_PLAN_SUCCESS:
			if(resultCode == BicycleUtil.CREATE_PLAN_SUCCESS)
				//do nothing
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
