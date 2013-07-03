package com.aeenery.aebicycle.pushservice;

public class BaeModel {
	
	public static final int PUSH_MSG_TYPE_INVITE_PLAN = 1;

	private static String appId = null;
	private static String channelId = null;
	private static String userId = null;
	
	public static String getAppId() {
		return appId;
	}
	public static void setAppId(String appId) {
		BaeModel.appId = appId;
	}
	public static String getChannelId() {
		return channelId;
	}
	public static void setChannelId(String channelId) {
		BaeModel.channelId = channelId;
	}
	public static String getUserId() {
		return userId;
	}
	public static void setUserId(String userId) {
		BaeModel.userId = userId;
	}
	
	
}
