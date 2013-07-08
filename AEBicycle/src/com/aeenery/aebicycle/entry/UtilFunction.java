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
	
	static double DEF_PI = 3.14159265359; // PI
	static double DEF_2PI= 6.28318530712; // 2*PI
	static double DEF_PI180= 0.01745329252; // PI/180.0
	static double DEF_R =6370693.5; // radius of earth
	public static double GetShortDistance(double lon1, double lat1, double lon2, double lat2)
	{
		double ew1, ns1, ew2, ns2;
		double dx, dy, dew;
		double distance;
		// 角度转换为弧度
			ew1 = lon1 * DEF_PI180;
		ns1 = lat1 * DEF_PI180;
		ew2 = lon2 * DEF_PI180;
		ns2 = lat2 * DEF_PI180;
		// 经度差
		dew = ew1 - ew2;
		// 若跨东经和西经180 度，进行调整
		if (dew > DEF_PI)
		dew = DEF_2PI - dew;
		else if (dew < -DEF_PI)
		dew = DEF_2PI + dew;
		dx = DEF_R * Math.cos(ns1) * dew; // 东西方向长度(在纬度圈上的投影长度)
		dy = DEF_R * (ns1 - ns2); // 南北方向长度(在经度圈上的投影长度)
		// 勾股定理求斜边长
		distance = Math.sqrt(dx * dx + dy * dy);
		return distance;
	}
}
