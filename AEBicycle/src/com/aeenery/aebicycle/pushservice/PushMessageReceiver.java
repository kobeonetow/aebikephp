package com.aeenery.aebicycle.pushservice;

import org.json.JSONObject;

import com.aeenery.aebicycle.LoginActivity;
import com.aeenery.aebicycle.challenge.PlanDetailActivity;
import com.aeenery.aebicycle.entry.BicycleUtil;
import com.aeenery.aebicycle.notification.AppNotifications;
import com.baidu.android.pushservice.PushConstants;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class PushMessageReceiver extends BroadcastReceiver {
	public final static String TAG = "PushMessageReceiver";
	
	protected AppNotifications noti;
	protected NotificationManager notiManager;
	protected NotificationCompat.Builder notiBuilder; 
	
	
	
	public PushMessageReceiver(){
		noti = AppNotifications.getInstance();
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		try {
			if(noti == null)
				noti = AppNotifications.getInstance();
			if(notiManager == null)
				notiManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE); 
			if (intent.getAction().equals(PushConstants.ACTION_MESSAGE)) {
				// 处理push消息
				String message = intent.getExtras().getString(PushConstants.EXTRA_PUSH_MESSAGE_STRING);
				if (message == null) {
					return;
				}
				JSONObject jsonMsg = new JSONObject(message);
				switch(jsonMsg.getInt("msg_type")){
				case BaeModel.PUSH_MSG_TYPE_INVITE_PLAN:
					Log.i(TAG,"++ Receive invite ++");
					String planId = jsonMsg.getString("planId");
					Intent notiIntent1 = new Intent(context,PlanDetailActivity.class);
					Bundle b = new Bundle();
					b.putInt("requestCode", BicycleUtil.REQUEST_CODE_ViewPlanDetailPostLoad);
					b.putString("planId", planId);
					notiIntent1.putExtras(b);
					notiIntent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					notiBuilder = noti.getPlanInviteNotification(notiIntent1, context, jsonMsg.getString("title"), jsonMsg.getString("description"));
					notiManager.notify(BicycleUtil.NOTI_PLAN_INVITE, notiBuilder.build());
					break;
				default:
					break;
				}

			} else if (intent.getAction().equals(PushConstants.ACTION_RECEIVE)) {
				// 处理 bind、setTags等方法口的返回数据
				final String method = intent
						.getStringExtra(PushConstants.EXTRA_METHOD);
				final int errorCode = intent.getIntExtra(
						PushConstants.EXTRA_ERROR_CODE,
						PushConstants.ERROR_SUCCESS);
				final String content = new String(
						intent.getByteArrayExtra(PushConstants.EXTRA_CONTENT));
				Log.i(TAG, "onMessage: method: " + method);
				Log.i(TAG, "onMessage: result : " + errorCode);
				Log.i(TAG, "onMessage: content : " + content);
				if (method.equals(PushConstants.METHOD_BIND)
						&& errorCode == PushConstants.ERROR_SUCCESS) {
					// Save the user id
					JSONObject jsonObj = new JSONObject(content)
							.getJSONObject("response_params");
					BaeModel.setUserId(jsonObj.getString("user_id"));
					BaeModel.setAppId(jsonObj.getString("appid"));
					BaeModel.setChannelId(jsonObj.getString("channel_id"));
				}
				// 根据 method不同进行不同的处理。 errorCode 也需要处理，有可能成功，有可能失败，
				// 比如 access token过期
			} else if (intent.getAction().equals(
					PushConstants.ACTION_RECEIVER_NOTIFICATION_CLICK)) {
				// 通知标题
				String title = intent
						.getStringExtra(PushConstants.EXTRA_NOTIFICATION_TITLE);
				// 通知内容
				String content = intent
						.getStringExtra(PushConstants.EXTRA_NOTIFICATION_CONTENT);
				// PushConstants.EXTRA_EXTRA保存服务端推送下来的附加字段。这是个 JSON
				// 字符串。//对应管理控制台上的“自定义内容”
				String content2 = intent.getExtras().getString(
						PushConstants.EXTRA_EXTRA);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.e(TAG, e.getMessage());
		}
	}

}
