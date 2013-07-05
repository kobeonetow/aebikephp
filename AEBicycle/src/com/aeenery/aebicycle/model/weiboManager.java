package com.aeenery.aebicycle.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.widget.Toast;

import com.aeenery.aebicycle.entry.BicycleUtil;
import com.weibo.sdk.android.Oauth2AccessToken;
import com.weibo.sdk.android.Weibo;
import com.weibo.sdk.android.WeiboAuthListener;
import com.weibo.sdk.android.WeiboDialogError;
import com.weibo.sdk.android.WeiboException;


public class weiboManager {
	private Weibo mWeibo;
	public static Oauth2AccessToken accessToken;
	public static Context context;
	private boolean isAuthSucc;
	
	public boolean sinaWbInit(Context conx){
		context = conx;
		isAuthSucc = false;
		mWeibo = Weibo.getInstance(BicycleUtil.SINA_WEIBO_KEY, BicycleUtil.SINA_URL);
		//token = context.getSharedPreferences(BicycleUtil.preference_file, 0).getString(BicycleUtil.SINA_WB_TOKEN,"0");
		accessToken = readAccessToken(context);
		if(accessToken.isSessionValid())
		{
			isAuthSucc = true;
		}
		else {
			//��֤���ڻ�û�б���֤��������֤
			isAuthSucc = false;
			mWeibo.authorize(context, new AuthDialogListener());
		}
		return isAuthSucc;
	}
	
	class AuthDialogListener implements WeiboAuthListener {

		@Override
		public void onComplete(Bundle values) {
			String token = values.getString("access_token");
			String expires_in = values.getString("expires_in");
			weiboManager.accessToken = new Oauth2AccessToken(token, expires_in);
			if (weiboManager.accessToken.isSessionValid()) {
				isAuthSucc = true;
			}
			weiboManager.keepAccessToken(weiboManager.context, weiboManager.accessToken);
			Toast.makeText(context, "��֤�ɹ�", 3000);
		}

		@Override
		public void onError(WeiboDialogError e) {
			Toast.makeText(weiboManager.context, "Auth error : " + e.getMessage(),
					Toast.LENGTH_LONG).show();
		}

		@Override
		public void onCancel() {
			Toast.makeText(weiboManager.context, "Auth cancel", Toast.LENGTH_LONG).show();
		}

		@Override
		public void onWeiboException(WeiboException e) {
			Toast.makeText(weiboManager.context, "Auth exception : " + e.getMessage(),
					Toast.LENGTH_LONG).show();
		}
	}
	
	public static void keepAccessToken(Context context, Oauth2AccessToken token) {
		SharedPreferences pref = context.getSharedPreferences(BicycleUtil.SHAREPREFERENCE_FILE, Context.MODE_APPEND);
		Editor editor = pref.edit();
		editor.putString(BicycleUtil.SINA_WB_TOKEN, token.getToken());
		editor.putLong(BicycleUtil.SINA_WB_EXPIERS_TIME, token.getExpiresTime());
		editor.commit();
	}
	/**
	 * ���sharepreference
	 * @param context
	 */
	public static void clear(Context context){
	    SharedPreferences pref = context.getSharedPreferences(BicycleUtil.SHAREPREFERENCE_FILE, Context.MODE_APPEND);
	    Editor editor = pref.edit();
	    editor.clear();
	    editor.commit();
	}

	/**
	 * ��SharedPreferences��ȡaccessstoken
	 * @param context
	 * @return Oauth2AccessToken
	 */
	public static Oauth2AccessToken readAccessToken(Context context){
		Oauth2AccessToken token = new Oauth2AccessToken();
		SharedPreferences pref = context.getSharedPreferences(BicycleUtil.SHAREPREFERENCE_FILE, Context.MODE_APPEND);
		token.setToken(pref.getString(BicycleUtil.SINA_WB_TOKEN, ""));
		token.setExpiresTime(pref.getLong(BicycleUtil.SINA_WB_EXPIERS_TIME, 0));
		return token;
	}
}
