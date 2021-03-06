package com.aeenery.aebicycle.entry;

public interface BicycleUtil {
	//Baidu Push Service Constants
	public final static String PUSH_API_KEY = "7kGhBDp0chc3q9kXhPg6YVWT";
	
	//SharedPreferences保存常量
	public final static String SHAREPREFERENCE_FILE = "AEBData";
	//mainTab常量
	public final static String curTab = "currentTab";
	public final static int tabCommunity = 0;
	public final static int tabChallenge = 1;
	public final static int tabManage = 2;
	public final static int tabWeather = 3;
	public final static int tabMore = 4;
	//weather常量
	public final static String fileDefaultCode = "defcode";//记录默认城市的文件文件名
	public final static String deCode = "default";//记录默认城市代码的KEY
	public final static String fileWeahter = "placeCode";//记录所有城市的对应代码文件
	
	//Map常量
	public final static String mapKey = "94420D26D7D3D99ED1FCDFF07543A4049262EC73"; //百度地图API Key
	public final static int RequestMapView = 0xf1;
	public final static int ResultMapView = 0xf2;
	public final static int RequestSetStartEndLoc = 0xf3;
	public final static int ResultSetStartEndLoc = 0xf4;
	
	//验证信息
	public final static int RequireLogin = 0x01;
	public final static int LoginSuccess = 0x11;
	public final static int RequireUsername = 0x02;
	public final static int RequirePassword = 0x03;
	public final static int PasswordNotEqual = 0x04;
	public final static int PasswordWrong = 0x04;
	public final static int RequireEmail = 0x05;
	public final static int RequireName = 0x06;
	public final static int Valid = 0x07;
	
	//计划验证信息
	public final static int RequirePlanName = 1;
	public final static int RequireStartLocation = 2;
	public final static int RequireEndLocation = 3;
	public final static int PLAN_SELECT_ROUTE = 4;
	public final static int CREATE_PLAN_SUCCESS = 5;
	public final static int RUN_PLAN = 6;
	public final static int CREATE_PLAN_FAIL = 8;
	public final static int CREATE_PLAN = 7;
	public final static int REQUEST_CODE_ViewPlanDetailPostLoad = 0x0211;
	public final static int REQUEST_CODE_ViewPlanDetailPreLoad = 0x0212;
	
	//Plan Page Position
	public final static int SetupPlan = 0;
	public final static int ViewPlan = 1;
	public final static int MyPlans = 2;
	public final static int JoinedPlans = 3;
	public final static int FinishedPlans = 4;
	
	//View Plan
	public final static int VIEW_PLAN = 0xa1;
	public final static int VIEW_PLAN_FINISH = 0xa2;
	public final static int VIEW_PLAN_DELETE = 0xa3;
	public final static int VIEW_PLAN_QUIT = 0xa4;
	
	
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
	
	//微博信息
	public final static String SINA_WEIBO_KEY = "3594581444";
	public final static String SINA_URL = "http://www.sina.com";
	public final static String SINA_WB_TOKEN = "sina_token";
	public final static String SINA_WB_EXPIERS_TIME = "sina_expires_time";
	
	//蓝牙
	public final static int REQUEST_ENABLE_BT = 2;
	public final static int RESULT_SCAN_DEVICE = 1325;
	public final static int REQUEST_CONNECT_DEVICE = 1;
	public final static int Virtual_Battery_Function = 1111;
	public final static int CONNECTION_LOST = 5111;
	public final static int CONNECTION＿SUCCESS = 5112;
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
	public final static String DEVICE_CONNECTED = "com.aeenergy.aebicycle.battery.Device_CONNECTED";
	public final static String DEVICE_DISCONNECTED = "com.aeenergy.aebicycle.battery.DEVICE_DISCONNECTED";
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
    
    
    //Notifications
    public static final int NOTI_BATTERY_LOW = 0x1001;
    public static final int NOTI_BATTERY_ERROR = 0x1002;
    public static final int NOTI_PLAN_INVITE = 0x1003;
    public static final int NOTI_FRIEND_ADD = 0x1004;
    public static final int NOTI_JOIN_PLAN = 0x1005;
    
    //Activity Recever Actions
    public static final String ACTION_PLAN_DETAIL_GET = "com.aeenery.bicycle.PLAN_DETAIL_GET"; //For receiver to get plan detail from server
    
    //Activity Start Actons
    public static final String ACTION_SHOW_PROPERTY = "com.aeenery.bicycle.SHOW_PROPERTY_ACTIITY"; // For ShowPropertyActivity
    public static final String ACTION_RUN_PROPERTY_SERVICE = "com.aeenery.aebicycle.Property_Service"; //For property service
}
