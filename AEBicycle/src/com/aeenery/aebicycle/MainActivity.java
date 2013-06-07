package com.aeenery.aebicycle;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;


public class MainActivity extends Activity {
	
	private  Button btnLogin; 
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    //��ʼ������ʵ������¼��ע�ᰴť
    protected void init(){
    	btnLogin = (Button)findViewById(R.id.goto_login_from_main);
    	btnLogin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent  = new Intent();
		    	intent.setClass(MainActivity.this, LoginActivity.class);
		    	MainActivity.this.startActivity(intent);
			}
		});
    	
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
}
