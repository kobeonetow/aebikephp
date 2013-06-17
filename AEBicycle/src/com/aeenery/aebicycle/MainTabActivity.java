package com.aeenery.aebicycle;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TabHost;

import com.aeenery.aebicycle.challenge.ViewPlanActivity;
import com.aeenery.aebicycle.entry.BicycleUtil;
import com.aeenery.aebicycle.model.Plan;
import com.aeenery.aebicycle.model.netManager;

public class MainTabActivity extends TabActivity {
	public TabHost mTabHost;
	private RadioGroup tabGroup;
	private netManager wifimgr;
	private int tabIdx = BicycleUtil.tabCommunity;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		isNetwork();
		login();
		setContentView(R.layout.activity_maintab);
		
		//��ȡtabIdx
		Bundle bundle = getIntent().getExtras();
		
		//���Ϊ�գ���ȡһ��Ĭ��ֵ
		if (bundle != null) {
			tabIdx = bundle.getInt(BicycleUtil.curTab,BicycleUtil.tabCommunity);
		}
		
		//��ȡTabHost����
		mTabHost = getTabHost();
		tabGroup = (RadioGroup)findViewById(R.id.tab_group);
		
		//�������ǩ��ͼ��setIndicator
		//��������setContent
		//��Ϊ��xml���Ѿ���TabWidget�����ˣ���ʾ����Ч��,������Ҫ����,ͨ��setOnCheckedChangeListener
		//��ΪŦ��
		mTabHost.addTab(mTabHost.newTabSpec("tab_community")
				.setIndicator(getResources().getText(R.string.maintab_community))
				.setContent(new Intent(MainTabActivity.this,DevelopingActivity.class)));
		mTabHost.addTab(mTabHost.newTabSpec("tab_challenge")
				.setIndicator(getResources().getText(R.string.maintab_challenge))
				.setContent(new Intent(MainTabActivity.this,DevelopingActivity.class)));
		mTabHost.addTab(mTabHost.newTabSpec("tab_manage")
				.setIndicator(getResources().getText(R.string.maintab_manage))
				.setContent(new Intent(MainTabActivity.this,DevelopingActivity.class)));
		mTabHost.addTab(mTabHost.newTabSpec("tab_weather")
				.setIndicator(getResources().getText(R.string.maintab_weather))
				.setContent(new Intent(MainTabActivity.this,DevelopingActivity.class)));
		mTabHost.addTab(mTabHost.newTabSpec("tab_more")
				.setIndicator(getResources().getText(R.string.maintab_more))
				.setContent(new Intent(MainTabActivity.this,DevelopingActivity.class)));
		mTabHost.setCurrentTab(tabIdx);
		
		tabGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				switch(checkedId){
				case R.id.tab_community:
					mTabHost.setCurrentTabByTag("tab_community");
					break;
				case R.id.tab_rules:
					mTabHost.setCurrentTabByTag("tab_rules");
					break;
				case R.id.tab_training:
					mTabHost.setCurrentTabByTag("tab_training");
					break;
				case R.id.tab_annoucement:
					mTabHost.setCurrentTabByTag("tab_annoucement");
					break;
				case R.id.tab_enquiry:
					mTabHost.setCurrentTabByTag("tab_enquiry");
					break;
				}
			}
		});
		
	}
	
	private void login(){
		if(LoginActivity.login == false){
			this.startActivityForResult(new Intent(this, LoginActivity.class), BicycleUtil.RequireLogin);
		}
	}
	
	private void isNetwork(){
		if(wifimgr == null){
			wifimgr = new netManager(this);
		}
		if (!wifimgr.checkConnect()) {
			Builder dialog = new AlertDialog.Builder(this);
			dialog.setTitle("֪ͨ");
			dialog.setMessage("�Ƿ������������,��������,������ܲ��ܷ������ݲ�ǿ���˳�");
			dialog.setPositiveButton("��������", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
					dialog.dismiss();
				}
			});
			dialog.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					dialog.dismiss();
					finish();
				}
			});
			dialog.show();
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == BicycleUtil.RequireLogin){
			if(resultCode != BicycleUtil.LoginSuccess){
				this.finish();
			}
		}
	}
}
