package com.aeenery.aebicycle.entry;

public interface BicycleUtil {
	//SharedPreferences���泣��
	public final static String preference_file = "AEBDate";
	//mainTab����
	public final static String curTab = "currentTab";
	public final static int tabCommunity = 0;
	public final static int tabChallenge = 1;
	public final static int tabManage = 2;
	public final static int tabWeather = 3;
	public final static int tabMore = 4;
	//weather����
	public final static String fileDefaultCode = "defcode";//��¼Ĭ�ϳ��е��ļ��ļ���
	public final static String deCode = "default";//��¼Ĭ�ϳ��д����KEY
	public final static String fileWeahter = "placeCode";//��¼���г��еĶ�Ӧ�����ļ�
	//routeMap����
	public final static String mapKey = "2BD0C4C35EB180C542AF40EAFC12CA48CD0D797D"; //�ٶȵ�ͼAPI Key
	public final static int ibSearchDst = 0;
	public final static int ibRouteSearchSrc = 1;
	public final static int ibRouteSearchDst = 2;
	public final static int ibRouteSearchIgnoreView = 3;
	
	//��֤��Ϣ
	public final static int RequireUsername = 1;
	public final static int RequirePassword = 2;
	public final static int PasswordNotEqual = 3;
	public final static int PasswordWrong = 4;
	public final static int RequireEmail =5;
	public final static int RequireName = 6;
	public final static int Valid = 0;
	
	//�ƻ���֤��Ϣ
	public final static int RequirePlanName = 1;
	public final static int RequireStartLocation = 2;
	public final static int RequireEndLocation = 3;
	public final static int PLAN_SELECT_ROUTE = 4;
	public final static int CREATE_PLAN_SUCCESS = 5;
	public final static int RUN_PLAN = 6;
	
	//Plan Page Position
	public final static int SetupPlan = 0;
	public final static int ViewPlan = 1;
	public final static int InviteFriend = 2;
	public final static int ViewHistory = 3;
	
	//Activity return codes
	public final static int BACK_TO_VIEW_PLAN = 1;
	
	
	//Server Parameters
	public final static int STATUS_PLAN_NOT_ASSIGN = 0;
	public final static int STATUS_NEW = 1;
	public final static int STATUS_PLAN_FINISH = 2;
	public final static int STATUS_PLAN_ACCEPT = 3;
	public final static int STATUS_PLAN_INTEREST = 4;
	public final static int STATUS_PLAN_START = 5;
	public final static int STATUS_PLAN_TERMINATED = 6;
	public final static int STATUS_ACHIEVEMENT_GRAB = 7;
	public final static int STATUS_NEW_FRINED = 8;
	public final static int STATUS_INVITE_FRIEND = 9;
	public final static String STATUS_REQUEST_FRIEND = "A";
	
	//Define the plan type Q:QUICK, N:NORMAL, C:CHALLENGE
	public final static String PLAN_TYPE_QUICK = "Q";
	public final static String PLAN_TYPE_NORMAL = "N";
	public final static String PLAN_TYPE_CHALLENGE = "C";
	
	//΢����Ϣ
	public final static String SINA_WEIBO_KEY = "3594581444";
	public final static String SINA_URL = "http://www.sina.com";
	public final static String SINA_WB_TOKEN = "sina_token";
	public final static String SINA_WB_EXPIERS_TIME = "sina_expires_time";
	
	//����
	public final static int REQUEST_ENABLE_BT = 2;
	public final static int RESULT_SCAN_DEVICE = 1325;
	public final static int REQUEST_CONNECT_DEVICE = 1;
	public final static int Virtual_Battery_Function = 1111;
	public final static int CONNECTION_LOST = 5111;
	public final static int CONNECTION��SUCCESS = 5112;
	public final static int VB_Name = 911;
	public final static int VB_Voltage = 912;
	public final static int VB_Current = 913;
	public final static int VB_Status = 914;
	public final static int VB_Conn = 915;
	public final static int VB_Time = 916;
	public final static int VB_Model = 917;
	public final static int VB_Power = 918;
	public final static int VB_Lvl = 919;
	public final static String BATTERY_STATE_UPDATE = "com.aeenergy.aebicycle.battery.Update";
	public final static String STOP_CONNECT_DEVICE = "com.aeenergy.aebicycle.battery.STOP_CONNECTING";
	public final static String RECONNECT_BT = "com.aeenergy.aebicycle.battery.RECONNECT_BT";
	public final static String CONNECT_DEVICE = "com.aeenergy.aebicycle.battery.ConnectDevice";
	public final static String BT_SEND_MSG = "com.aeenergy.aebicycle.battery.BT_SEND_MSG";
	public final static String BT_SEND_MSG_ID = "com.aeenergy.aebicycle.battery.BT_SEND_MSG.ID";
	public final static String STOP_BT_SERVICE = "com.aeenergy.aebicycle.battery.STOP_BT_SERVICE";
	
	public final static String BT_SEND_EMPTY_BYTES = "com.aeenergy.aebicycle.battery.BT_SEND_EMPTY_BYTES";
	
	
	
	// Message types sent from the BluetoothChatService Handler
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;
    // Key names received from the BluetoothChatService Handler
    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";
    
}
