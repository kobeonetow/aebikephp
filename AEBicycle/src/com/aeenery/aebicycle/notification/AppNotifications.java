package com.aeenery.aebicycle.notification;

import com.aeenery.aebicycle.R;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;

public class AppNotifications {
	
	public static AppNotifications instance = null;
	public long[] viratePatten = new long[]{0,400,200,400};
	
	public static AppNotifications getInstance(){
		if(instance == null)
			instance = new AppNotifications();
		return instance;
	}
	
	
	public NotificationCompat.Builder getBatteryLowNotification(Context context, String content){
		NotificationCompat.Builder noti = new NotificationCompat.Builder(context.getApplicationContext());
        noti.setContentTitle("电量底下，请尽快充电")
        .setContentText(content)
        .setSmallIcon(R.drawable.noti_charge);
        noti.setAutoCancel(false);
        
        noti.setVibrate(viratePatten);
        noti.setLights(0xA8C7A6, 200, 1500);
        
        noti.setOnlyAlertOnce(true);
        return noti;
	}

	
	public NotificationCompat.Builder getBatteryErrorNotification(Context context, String content){
		NotificationCompat.Builder noti = new NotificationCompat.Builder(context.getApplicationContext());
        noti.setContentTitle("电池出错，请尝试关掉电池重启并联系厂商")
        .setContentText(content)
        .setSmallIcon(R.drawable.icon_battery_error);
        noti.setAutoCancel(true);
        noti.setVibrate(viratePatten);
        noti.setLights(0xA8C7A6, 200, 1500);
        noti.setOnlyAlertOnce(true);
        return noti;
	}
	
	public NotificationCompat.Builder getPlanInviteNotification(Intent _intent, Context context, String content){
		_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		PendingIntent pIntent = PendingIntent.getActivity(context,0,_intent,PendingIntent.FLAG_UPDATE_CURRENT);
		NotificationCompat.Builder noti = new NotificationCompat.Builder(context.getApplicationContext());
        noti.setContentTitle("收到参加邀请")
        .setContentText(content)
        .setSmallIcon(R.drawable.icon_join_plan)
        .setContentIntent(pIntent);
        
        noti.setAutoCancel(true);
        noti.setVibrate(viratePatten);
        noti.setLights(0xA8C7A6, 200, 1500);
        noti.setOnlyAlertOnce(true);
        return noti;
	}
	
	public NotificationCompat.Builder getFriendAddNotification(Intent _intent,Context context, String content){
		_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		PendingIntent pIntent = PendingIntent.getActivity(context,0,_intent,PendingIntent.FLAG_UPDATE_CURRENT);
		
		NotificationCompat.Builder noti = new NotificationCompat.Builder(context.getApplicationContext());
        noti.setContentTitle("添加好友")
        .setContentText(content)
        .setSmallIcon(R.drawable.icon_friend_request)
        .setContentIntent(pIntent);
        
        noti.setAutoCancel(true);
        noti.setVibrate(viratePatten);
        noti.setLights(0xA8C7A6, 200, 1500);
        noti.setOnlyAlertOnce(true);
        return noti;
	}
	
	public NotificationCompat.Builder getJoinPlanNotification(Intent _intent, Context context, String content){
		_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		PendingIntent pIntent = PendingIntent.getActivity(context,0,_intent,PendingIntent.FLAG_UPDATE_CURRENT);
		
		NotificationCompat.Builder noti = new NotificationCompat.Builder(context.getApplicationContext());
        noti.setContentTitle("新成员加入了计划")
        .setContentText(content)
        .setSmallIcon(R.drawable.icon_join_plan)
        .setContentIntent(pIntent);
        
        noti.setAutoCancel(true);
        noti.setVibrate(viratePatten);
        noti.setLights(0xA8C7A6, 200, 1500);
        noti.setOnlyAlertOnce(true);
        return noti;
	}
	
}
