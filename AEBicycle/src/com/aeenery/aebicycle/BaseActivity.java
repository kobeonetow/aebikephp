package com.aeenery.aebicycle;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;

import com.aeenery.aebicycle.model.ServerAPI;

//一个基础BaseActivity,方便写多个Acitivity之间一些共有的函数
public class BaseActivity extends Activity {
	ProgressDialog pd;
	
	protected void doExit(){
		new AlertDialog.Builder(BaseActivity.this)
			.setTitle("提示")
			.setMessage("确定要退出吗?")
			.setPositiveButton("确定", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					finish();
				}
			})
			.setNegativeButton("取消", null).show();
	}
	
	public void showDialog(){
		pd = ProgressDialog.show(BaseActivity.this, "提示", "数据加载中,请稍后...");
		pd.setCancelable(true);
	}
	
	public void showDialog(String title, String msg){
		if (title == null) {
			title = "提示";
		}
		if (msg == null) {
			msg = "数据加载中...";
		}
		if(pd != null && !pd.isShowing())
			pd = ProgressDialog.show(BaseActivity.this, title, msg);
	}
	
	public void closeDialog(){
		if(pd != null && pd.isShowing())
			pd.cancel();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		getMenuInflater().inflate(R.menu.activity_challenge, menu);
		return true;
	}
	
//	public void showProgressBar(){
//		//创建动画集，加入等待框显示动画
//		AnimationSet set = new AnimationSet(true);
//		Animation animation = new AlphaAnimation(0.0f, 1.0f);
//		animation.setDuration(500);
//		set.addAnimation(animation);
//		animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
//				Animation.RELATIVE_TO_SELF,0.0f,Animation.RELATIVE_TO_SELF,1.0f,
//				Animation.RELATIVE_TO_SELF,0.0f);
//		animation.setDuration(500);
//		set.addAnimation(animation);
//		LayoutAnimationController controller = new LayoutAnimationController(set,0.5f);
//		RelativeLayout loading = (RelativeLayout)findViewById(R.id.loading);
//		loading.setVisibility(View.VISIBLE);
//		loading.setLayoutAnimation(controller);
//	}
//	
//	public void showProgressBar(String title){
//		TextView loading = (TextView) findViewById(R.id.txt_loading);
//		loading.setText(title);
//		showProgressBar();
//	}
//	
//	public void closeProgressBar(){
//		AnimationSet set = new AnimationSet(true);
//		Animation animation = new AlphaAnimation(0.0f, 1.0f);
//		animation.setDuration(500);
//		set.addAnimation(animation);
//		animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
//				Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
//				0.0f, Animation.RELATIVE_TO_SELF, -1.0f);
//		animation.setDuration(500);
//		set.addAnimation(animation);
//		LayoutAnimationController controller = new LayoutAnimationController(
//				set, 0.5f);
//		RelativeLayout loading = (RelativeLayout) findViewById(R.id.loading);
//		loading.setLayoutAnimation(controller);
//		loading.setVisibility(View.INVISIBLE);
//	}
	
	protected void requiredLogin(){
		LoginActivity.login = true;
		if(!LoginActivity.login){
			ServerAPI api = new ServerAPI(this, LoginActivity.user);
			api.useSharedPreferencesInfoLogin(this);
			api.setUser(LoginActivity.user);
			Intent intent  = new Intent();
			intent.setClass(BaseActivity.this, LoginActivity.class);
			BaseActivity.this.startActivity(intent);
		}
	}
	
	@Override
	public void onBackPressed(){
		if(pd != null && pd.isShowing())
			this.closeDialog();
		this.finish();
	}
}
