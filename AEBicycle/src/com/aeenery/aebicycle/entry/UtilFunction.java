package com.aeenery.aebicycle.entry;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import com.aeenery.aebicycle.LoginActivity;
import com.aeenery.aebicycle.model.netManager;

public class UtilFunction {

	private static netManager wifimgr = null;
	
	public static  void login(Context context){
		if(LoginActivity.login == false){
			((Activity) context).startActivityForResult(new Intent(context, LoginActivity.class), BicycleUtil.RequireLogin);
		}
	}
	
	public static void checkNetConnection(final Context context) {
		if (wifimgr == null) {
			wifimgr = new netManager(context);
		}
		if (!wifimgr.checkConnect()) {
			Builder dialog = new AlertDialog.Builder(context);
			dialog.setTitle("通知");
			dialog.setMessage("设备没连接上网络，请配置好网络设置");
			dialog.setPositiveButton("网络设置",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							context.startActivity(new Intent(
									android.provider.Settings.ACTION_WIRELESS_SETTINGS));
							dialog.dismiss();
						}
					});
			dialog.setNegativeButton("取消",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							dialog.dismiss();
							((Activity) context).finish();
						}
					});
			dialog.show();
		}
	}
}
