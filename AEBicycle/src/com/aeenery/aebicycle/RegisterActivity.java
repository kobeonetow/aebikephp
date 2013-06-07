package com.aeenery.aebicycle;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.aeenery.aebicycle.entry.BicycleUtil;
import com.aeenery.aebicycle.model.ServerAPI;
import com.aeenery.aebicycle.model.Useraccount;

public class RegisterActivity extends Activity {

	private Useraccount user;  //��ȡ�û�����
	private Button btnRegister;
	private EditText etUsername;
	private EditText etPassword;
	private EditText etPassword2;
	private EditText etEmail;
	private EditText etDisplayName;
	private Intent intent;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		init();
	}

	public void init() {
		user = new Useraccount();
		etUsername = (EditText)findViewById(R.id.register_username);
		etPassword = (EditText)findViewById(R.id.register_password);
		etPassword2 = (EditText)findViewById(R.id.register_password2);
		etEmail = (EditText)findViewById(R.id.register_email);
		etDisplayName = (EditText)findViewById(R.id.register_display_name);
		btnRegister = (Button)findViewById(R.id.btn_register_submit);
		
		btnRegister.setOnClickListener(new RegisterListener());
	}

	//��ע�ᰴť����ʱ
	class RegisterListener implements OnClickListener{
		@Override
		public void onClick(View v) {
			switch (validate()) {
			case BicycleUtil.RequireUsername:
				Toast.makeText(RegisterActivity.this, getString(R.string.enter_username), Toast.LENGTH_SHORT).show();
				break;
			case BicycleUtil.RequirePassword:
				Toast.makeText(RegisterActivity.this, getString(R.string.enter_password), Toast.LENGTH_SHORT).show();
				break;
			case BicycleUtil.PasswordNotEqual:
				Toast.makeText(RegisterActivity.this, getString(R.string.password_not_the_same), Toast.LENGTH_SHORT).show();
				break;
			case BicycleUtil.RequireEmail:
				Toast.makeText(RegisterActivity.this, getString(R.string.enter_emailaddress), Toast.LENGTH_SHORT).show();
				break;
			case BicycleUtil.RequireName:
				Toast.makeText(RegisterActivity.this, getString(R.string.enter_display_name), Toast.LENGTH_SHORT).show();
				break;
			case BicycleUtil.Valid:
				ServerAPI api = new ServerAPI(RegisterActivity.this, user);
				api.register(RegisterActivity.this);
				break;
			default:
				Toast.makeText(RegisterActivity.this, getString(R.string.check_typo), Toast.LENGTH_SHORT).show();
			}
		}
		
		public int validate(){
			String username = etUsername.getText().toString().trim();
			if(username == null || username.equals("")){
				return BicycleUtil.RequireUsername;
			}
			
			String password  = etPassword.getText().toString().trim();
			String password2  = etPassword2.getText().toString().trim();
			if(password == null || password.equals("")){
				return BicycleUtil.RequirePassword;
			}
			if(!password.equals(password2)){
				return BicycleUtil.PasswordNotEqual;
			}
			
			String email = etEmail.getText().toString().trim();
			if(email == null || email.equals("")){
				return BicycleUtil.RequireEmail;
			}
			
			String name = etDisplayName.getText().toString().trim();
			if(name == null || name.equals("")){
				return BicycleUtil.RequireName;
			}
			
			user.setName(name);
			user.setEmailaddress(email);
			user.setPassword(password);
			user.setUsername(username);
			return BicycleUtil.Valid;
		}
	}
}
