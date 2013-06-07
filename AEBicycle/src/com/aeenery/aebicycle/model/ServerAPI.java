package com.aeenery.aebicycle.model;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.aeenery.aebicycle.LoginActivity;
import com.aeenery.aebicycle.R;
import com.aeenery.aebicycle.RegisterActivity;
import com.aeenery.aebicycle.challenge.PlanDetailActivity;
import com.aeenery.aebicycle.challenge.QuickPlanActivity;
import com.aeenery.aebicycle.challenge.ViewPlanActivity;

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
//		catch (IOException e) {
//			e.printStackTrace();
//			Log.i("Output Stream Error","The output is not a proper http request :"+e.getCause().getMessage());
//			return null;
//		}
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
				context.startActivity(intent);
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
//				httpClient.addNameValuePair("userid", user.getId());
				httpClient.addNameValuePair("plantype", "C");
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
	
	public void getCurrentPlanList(final Context context, final String pagenumber, final String lotsize){
		new AsyncTask<String,String,JSONObject>(){
			@Override
			protected JSONObject doInBackground(String... params) {
				httpClient.addNameValuePair("pagenumber", pagenumber);
				httpClient.addNameValuePair("lotsize", lotsize);
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
	
	public void joinPlan(final Context context, final Plan p){
		new AsyncTask<String,String,JSONObject>(){
			@Override
			protected JSONObject doInBackground(String... params) {
				httpClient.addNameValuePair("planid", p.getId());
//				httpClient.addNameValuePair("userid", user.getId());
				return callServer("index/acceptplan");
			}
			
			@Override
			protected void onPostExecute(JSONObject json){
				if(checkResult(this,json,context, "����ʧ�ܣ����Ժ�����")){
					((PlanDetailActivity)context).joinAPlan(p.getId());
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
				if(checkResult(this,json,context, "ɾ��ʧ�ܣ����Ժ�����")){
					((PlanDetailActivity)context).close(json);
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
				if(checkResult(this,json,context, "�˳�ʧ�ܣ����Ժ�����")){
					((PlanDetailActivity)context).quitAPlan(p.getId(),json);
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
				if(checkResult(this,json,context, "����ʧ�ܣ����Ժ�����")){
					((PlanDetailActivity)context).updateAPlan(p.getId(),json);
				}
			}
			
		}.execute("");
	}
	
	public void startPlan(final Context context, final Plan p){
		new AsyncTask<String,String,JSONObject>(){
			@Override
			protected JSONObject doInBackground(String... params) {
				httpClient.addNameValuePair("planid", p.getId());
				return callServer("index/startplan");
			}
			
			@Override
			protected void onPostExecute(JSONObject json){
				if(checkResult(this,json,context, "开始失败,请稍后尝试")){
					((PlanDetailActivity)context).updateAPlan(p.getId(),json);
					((PlanDetailActivity)context).hideStartPlanBtn();
					((PlanDetailActivity)context).runPlanActivity();
				}
			}
			
		}.execute("");
	}
	
	public void endPlan(final Context context, final Plan p){
		new AsyncTask<String,String,JSONObject>(){
			@Override
			protected JSONObject doInBackground(String... params) {
				httpClient.addNameValuePair("planid", p.getId());
				return callServer("index/submitsummary");
			}
			
			@Override
			protected void onPostExecute(JSONObject json){
				if(checkResult(this,json,context, "开始失败,请稍后尝试")){
					((PlanDetailActivity)context).updateAPlan(p.getId(),json);
					((PlanDetailActivity)context).hideEndPlanBtn();
				}
			}
			
		}.execute("");
	}
}
