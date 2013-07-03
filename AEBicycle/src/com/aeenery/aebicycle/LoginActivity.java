package com.aeenery.aebicycle;

import org.json.JSONObject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.aeenery.aebicycle.entry.BicycleUtil;
import com.aeenery.aebicycle.model.ServerAPI;
import com.aeenery.aebicycle.model.Useraccount;
import com.aeenery.aebicycle.pushservice.BaeModel;

public class LoginActivity extends BaseActivity {

	public static Useraccount user = new Useraccount(); 
	public static boolean login = false;
	private Button btnLogin;
	private Button btnRegister;
	private EditText etUsername;
	private EditText etPassword;
	private SharedPreferences sharedPreferences; 
	private Editor editor; 
	private ServerAPI api;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
    }

    //
    protected void init(){
    	sharedPreferences = this.getSharedPreferences("aeuser", MODE_PRIVATE);
    	btnLogin = (Button)findViewById(R.id.button_login);
    	btnRegister = (Button)findViewById(R.id.button_register);
    	etUsername = (EditText)findViewById(R.id.username);
    	etPassword = (EditText)findViewById(R.id.password);
    	btnLogin.setOnClickListener(new LoginListener());
    	btnRegister.setOnClickListener(new RegisterListener());
    	
    	api = new ServerAPI(LoginActivity.this,user);
    	if(LoginActivity.user != null && LoginActivity.user.getId() != null && LoginActivity.login == true){
    		//this.toMainActivity();
    		this.setResult(BicycleUtil.LoginSuccess);
    		this.finish();
    	}else{
    		api.useSharedPreferencesInfoLogin(this);
    	}
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
   
    //
    protected void clearSharedPreferences(){
    	sharedPreferences.edit().clear().commit();
    }
    
    public void writeToSharedPreferences(){
    	if(sharedPreferences == null){
    		sharedPreferences = getSharedPreferences("aebike", MODE_PRIVATE);
    	}
    	if(editor == null){
    		editor = sharedPreferences.edit();
    	}
    	editor.putString("username", user.getUsername());
		editor.putString("password",user.getPassword());
		editor.putString("userid", user.getId());
		editor.putString("baeuserId", BaeModel.getUserId());
		editor.putString("channel_id", BaeModel.getChannelId());
		editor.commit();
    }
    
    //��¼��ť������
    class LoginListener implements OnClickListener {
		@Override
		public void onClick(View arg0) {
			//����û���������Ƿ��Ѿ�����
			
			boolean valid = validation();
			if(valid){
				Log.i("Login"," logging ");
				if(LoginActivity.login){
					writeToSharedPreferences();
				}else{
					showDialog("","正在登陆...");
					api.login(LoginActivity.this);
				}
			}
		}
    	
		public boolean validation(){
			String username = etUsername.getText().toString().trim();
			if(username.equals("")){
				Toast.makeText(LoginActivity.this, getString(R.string.enter_username), Toast.LENGTH_LONG).show();
				return false;
			}
			
			String password = etPassword.getText().toString().trim();
			if(password.equals("")){
				Toast.makeText(LoginActivity.this, getString(R.string.enter_password), Toast.LENGTH_LONG).show();
				return false;
			}
			
			user.setUsername(username);
			user.setPassword(password);
			return true;
		}
    }

    public void checkLogin(JSONObject json){
    	closeDialog();
    	if(json == null) return;     	
		LoginActivity.user.__setUserFromJSONObject(json);
		Log.i("Login","Login success with user "+user.getUsername());
		LoginActivity.login = true;
		writeToSharedPreferences();
		toMainActivity();
    }
    
    public void toMainActivity(){
    	if(LoginActivity.login){
    		this.setResult(BicycleUtil.LoginSuccess);
    		this.finish();
    	}
    }
    
    class RegisterListener implements OnClickListener{
		@Override
		public void onClick(View v) {
			Intent intentRegister = new Intent();
			intentRegister.setClass(LoginActivity.this, RegisterActivity.class);
			LoginActivity.this.startActivity(intentRegister);
		}
    	
    }
}
