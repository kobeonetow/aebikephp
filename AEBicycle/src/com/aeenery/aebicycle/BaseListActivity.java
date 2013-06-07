package com.aeenery.aebicycle;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.view.Menu;

public class BaseListActivity extends ListActivity {
ProgressDialog pd;
	
	protected void doExit(){
		new AlertDialog.Builder(BaseListActivity.this)
			.setTitle("��ʾ")
			.setMessage("ȷ��Ҫ�˳���?")
			.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					finish();
				}
			})
			.setNegativeButton("ȡ��", null).show();
	}
	
	public void showDialog(){
		pd = ProgressDialog.show(BaseListActivity.this, "��ʾ", "��ݼ�����,���Ժ�...");
		pd.setCancelable(true);
	}
	
	public void showDialog(String title, String msg){
		if (title == null) {
			title = "��ʾ";
		}
		if (msg == null) {
			msg = "��ݼ�����...";
		}
		pd = ProgressDialog.show(BaseListActivity.this, title, msg);
	}
	
	public void closeDialog(){
		pd.cancel();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		getMenuInflater().inflate(R.menu.activity_challenge, menu);
		return true;
	}
	
	
}
