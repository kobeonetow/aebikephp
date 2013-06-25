package com.aeenery.aebicycle.model;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.aeenery.aebicycle.LoginActivity;
import com.aeenery.aebicycle.R;
import com.aeenery.aebicycle.RegisterActivity;
import com.aeenery.aebicycle.challenge.JoinPlansActivity;
import com.aeenery.aebicycle.challenge.MyPlansActivity;
import com.aeenery.aebicycle.challenge.PlanDetailActivity;
import com.aeenery.aebicycle.challenge.QuickPlanActivity;
import com.aeenery.aebicycle.challenge.ViewPlanActivity;
import com.aeenery.aebicycle.entry.BicycleUtil;
import com.aeenery.aebicycle.friend.FriendListActivity;

public class ServerAPI {
	
	private HttpRestfulClient httpClient = null; 
	private Useraccount user = null;
	
	public ServerAPI(Context context, Useraccount user){
		httpClient = new HttpRestfulClient(context.getString(R.string.servername), context.getString(R.string.serverkey));
		this.user = user;
	}
	
	public Useraccount getUser(){
		return this.user;
	}
	
	public void setUser(Useraccount user){
		this.user = user;
	}

	protected JSONObject callServer(String url){
		try {
			if(this.user.getId() == null){
				httpClient.addNameValuePair("userid", "");
			}else{
				httpClient.addNameValuePair("userid", this.user.getId());
			}
			JSONObject json = httpClient.callUrl(url);
			return json;
		} catch (JSONException e) {
			Log.e("Json Error","Return type is not a json object");
			return null;
		} 
	}
	
	protected boolean checkResult(AsyncTask<String,String,JSONObject> asynctask,JSONObject json, Context context, String msg){
		if(json == null){
			Log.i("Response Error","Json object returned is null");
			asynctask.cancel(true);
			return false;
		}
		String result;
		try {
			result = json.getString("result");
			if(result == null || result.equals("0")){
				Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
				return false;
			}else if(result.equals("2")){
				Toast.makeText(context, "没有新信息", Toast.LENGTH_SHORT).show();
				return false;
			}
		} catch (JSONException e) {
			Log.i("Response Error","Json object returned is not a json object");
			e.printStackTrace();
		}
		
		return true;
	}
	
	public void useSharedPreferencesInfoLogin(Context context){
		SharedPreferences sharedPreferences = context.getSharedPreferences("aebike", Context.MODE_PRIVATE);
		if(user == null) user = new Useraccount();
		user.setUsername(sharedPreferences.getString("username",""));
		user.setPassword(sharedPreferences.getString("password",""));
		user.setId(sharedPreferences.getString("userid", null));
		if(user.getUsername().equals("") || user.getPassword().equals("")){
			Log.i("Login","cannot login with empty username or password ");
			if(context instanceof LoginActivity){
				return;
			}else{
				Intent intent = new Intent();
				intent.setClass(context, LoginActivity.class);
				((Activity) context).startActivityForResult(intent, BicycleUtil.RequireLogin);
				return;
			}
		}
		this.login(context);
    }
	
	public void login(final Context context){
		new AsyncTask<String , String, JSONObject>() {
			@Override
			protected JSONObject doInBackground(String... params) {
				httpClient.setNameValuePair(NameValuePairReflect.__getNameValuePair("com.aeenery.aebicycle.model.Useraccount", user),true);
				return callServer("index/login");
			}
			
			@Override
			protected void onPostExecute(JSONObject json){
				boolean ret = checkResult(this,json, context, "登陆失败");
				try {
					if(!ret){
						LoginActivity.login = false;
						((LoginActivity)context).checkLogin(null);
						return;
					}else{
						JSONObject json2 = json.getJSONObject("result");
						((LoginActivity)context).checkLogin(json2);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			
		}.execute("");
	}
	
	public void register(final Context context){
		new AsyncTask<String, String, JSONObject>(){
			@Override
			protected JSONObject doInBackground(String... params) {
				httpClient.setNameValuePair(NameValuePairReflect.__getNameValuePair("com.aeenery.aebicycle.model.Useraccount", user),true);
				return callServer("index/createuser");
			}
			
			@Override
			protected void onPostExecute(JSONObject json){
				if(checkResult(this,json,context,context.getString(R.string.server_busy))){
						Toast.makeText(context, context.getString(R.string.user_register_success), Toast.LENGTH_SHORT).show();
						((RegisterActivity)context).finish();
				}
				
			}
		}.execute("");
	}
	
	public void createplan(final Context context, final Plan plan){
		new AsyncTask<String,String,JSONObject>(){
			@Override
			protected JSONObject doInBackground(String... params) {
				httpClient.setNameValuePair(NameValuePairReflect.__getNameValuePair("com.aeenery.aebicycle.model.Plan",plan), true);
				httpClient.addNameValuePair("plantype", BicycleUtil.PLAN_TYPE_CHALLENGE);
				return callServer("index/createplan");
			}
			
			@Override
			protected void onPostExecute(JSONObject json){
				if(checkResult(this,json,context, context.getString(R.string.plan_create_success))){
					((QuickPlanActivity)context).planCreated();
				}
				
			}
			
		}.execute("");
	}
	
	public void getCurrentPlanList(final Context context, final String startRow){
		new AsyncTask<String,String,JSONObject>(){
			@Override
			protected JSONObject doInBackground(String... params) {
				httpClient.addNameValuePair("start", startRow);
				return callServer("index/getcurrentplanlist");
			}
			
			@Override
			protected void onPostExecute(JSONObject json){
				if (checkResult(this, json, context,
						context.getString(R.string.server_busy))) {
					((ViewPlanActivity) context).setPlansToView(json);
				}
			}
			
		}.execute("");
	} 
	
	/**
	 * Get all plans that created by user
	 * @param myPlansActivity
	 */
	public void getMyOwnPlans(final MyPlansActivity myPlansActivity) {
		new AsyncTask<String,String,JSONObject>(){
			@Override
			protected JSONObject doInBackground(String... params) {
				return callServer("index/getuserplanlist");
			}
			
			@Override
			protected void onPostExecute(JSONObject json){
				if (checkResult(this, json, myPlansActivity,
						myPlansActivity.getString(R.string.server_busy))) {
					myPlansActivity.setPlansToView(json);
				}
			}
			
		}.execute("");
	}
	
	public void joinPlan(final Context context, final Plan p){
		new AsyncTask<String,String,JSONObject>(){
			@Override
			protected JSONObject doInBackground(String... params) {
				httpClient.addNameValuePair("planid", p.getId());
				return callServer("index/acceptplan");
			}
			
			@Override
			protected void onPostExecute(JSONObject json){
				if(checkResult(this,json,context, "参加失败，稍后再试")){
					((PlanDetailActivity)context).callBackAfterClick(json , p.getId(),PlanDetailActivity.JOIN_PLAN);
				}
			}
			
		}.execute("");
	}
	
	public void detelePlan(final Context context, final Plan p){
		new AsyncTask<String,String,JSONObject>(){
			@Override
			protected JSONObject doInBackground(String... params) {
				httpClient.addNameValuePair("planid", p.getId());
				return callServer("index/deleteplan");
			}
			
			@Override
			protected void onPostExecute(JSONObject json){
				if(checkResult(this,json,context, "取消计划失败，请稍后再试")){
					((PlanDetailActivity)context).callBackAfterClick(json, p.getId(),PlanDetailActivity.DELETE_PLAN);
				}
			}
			
		}.execute("");
	}
	
	public void quitPlan(final Context context, final Plan p){
		new AsyncTask<String,String,JSONObject>(){
			@Override
			protected JSONObject doInBackground(String... params) {
				httpClient.addNameValuePair("planid", p.getId());
				return callServer("index/quitplan");
			}
			
			@Override
			protected void onPostExecute(JSONObject json){
				if(checkResult(this,json,context, "退出失败，请稍后再试")){
					((PlanDetailActivity)context).callBackAfterClick(json, p.getId(),PlanDetailActivity.QUIT_PLAN);
				}
			}
			
		}.execute("");
	}
	
	public void updatePlan(final Context context, final Plan p){
		new AsyncTask<String,String,JSONObject>(){
			@Override
			protected JSONObject doInBackground(String... params) {
				httpClient.addNameValuePair("planid", p.getId());
				return callServer("index/getplanbyid");
			}
			
			@Override
			protected void onPostExecute(JSONObject json){
				if(checkResult(this,json,context, "更新失败，请稍后再试")){
					((PlanDetailActivity)context).callBackAfterClick(json,p.getId(),PlanDetailActivity.UPDATE_PLAN);
				}
			}
			
		}.execute("");
	}
	
	
	/**
	 * Get friend starting from number index
	 * Each time load maximum 30 friends
	 * @param context
	 * @param number
	 */
	public void getFriendList(final Context context, final String number){
		new AsyncTask<String,String,JSONObject>(){
			@Override
			protected JSONObject doInBackground(String... params) {
				httpClient.addNameValuePair("startRow", number);
				return callServer("index/getfriendlist");
			}
			
			@Override
			protected void onPostExecute(JSONObject json){
				if (checkResult(this, json, context,
						context.getString(R.string.server_busy))) {
					((FriendListActivity) context).setFriendsToView(json);
				}
			}
			
		}.execute("");
	}

	/**
	 * Get joined plans by user
	 * @param joinPlansActivity
	 * @param string
	 */
	public void getJoinedPlans(final JoinPlansActivity joinPlansActivity,
			final String startRow) {
		new AsyncTask<String,String,JSONObject>(){
			@Override
			protected JSONObject doInBackground(String... params) {
				httpClient.addNameValuePair("start", startRow);
				return callServer("index/getjoinedplanlist");
			}
			
			@Override
			protected void onPostExecute(JSONObject json){
				if (checkResult(this, json, joinPlansActivity,
						joinPlansActivity.getString(R.string.server_busy))) {
					joinPlansActivity.setPlansToView(json);
				}
			}
			
		}.execute("");
	}

}
