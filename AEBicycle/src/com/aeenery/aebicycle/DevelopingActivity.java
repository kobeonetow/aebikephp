package com.aeenery.aebicycle;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.aeenery.aebicycle.model.weiboManager;

public class DevelopingActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_developing);
		Button btWeibo = (Button)findViewById(R.id.btWeibo);
		btWeibo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				weiboManager wbMgr = new weiboManager();
				if(wbMgr.sinaWbInit(DevelopingActivity.this))
				{
//					Intent intent = new Intent(DevelopingActivity.this,WeiBoEditActivity.class);
//					DevelopingActivity.this.startActivity(intent);
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_developing, menu);
		return true;
	}

}
