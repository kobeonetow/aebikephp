package com.aeenery.aebicycle.entry;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

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
	
	public static Date convertDateString(String date){
		try{
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd",Locale.CHINA);
			return format.parse(date);
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * String passed must in format MM:SS
	 * @param time
	 * @return
	 */
	public static Calendar convertStringToHourMinute(String time){
		Calendar c = Calendar.getInstance();
		if(time == null || !time.contains(":"))
			return null;
		String[] nums = time.split(":");
		if(nums.length != 2)
			return null;
		int hour = Integer.parseInt(nums[0]);
		int min = Integer.parseInt(nums[1]);
		c.set(Calendar.HOUR_OF_DAY, hour);
		c.set(Calendar.MINUTE, min);
		return c;
	}
}
