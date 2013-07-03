package com.aeenery.aebicycle.friend;

import org.json.JSONArray;
import org.json.JSONObject;

import com.aeenery.aebicycle.BaseActivity;
import com.aeenery.aebicycle.LoginActivity;
import com.aeenery.aebicycle.R;
import com.aeenery.aebicycle.model.Plan;
import com.aeenery.aebicycle.model.ServerAPI;
import com.aeenery.aebicycle.model.Useraccount;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class FriendListActivity extends BaseActivity {

	private ServerAPI api = null;
	private ListView lv = null;
	private Useraccount[] friends = null;
	private Button btnSend = null;
	private String planId = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		api = new ServerAPI(this, LoginActivity.user);
		setContentView(R.layout.activity_friend_list);

		initFriendListView();
	}

	// Get the friend list for user
	private void initFriendListView() {
		api.getFriendList(this, "0");
		lv = (ListView) findViewById(R.id.view_friend_listview);
		lv.setOnItemClickListener(new FriendClickListener());

		btnSend = (Button) findViewById(R.id.btnSendInvite);
		btnSend.setOnClickListener(sendButtonClick());
		
		getPlanIdFromIntent();
	}

	private void getPlanIdFromIntent() {
		planId = getIntent().getExtras().getString("planid");
	}

	/**
	 * Set friends to the view
	 * 
	 * @param json
	 */
	public void setFriendsToView(JSONObject json) {
		try {
			JSONArray json2 = json.getJSONArray("data");
			friends = new Useraccount[json2.length()];
			for (int i = 0; i < json2.length(); i++) {
				JSONObject curIndex = ((JSONObject) json2.get(i));
				Useraccount user = new Useraccount();
				user.__setUserFromJSONObject(curIndex);
				friends[i] = user;

			}
			lv.setAdapter(new FriendViewAdapter(FriendListActivity.this,
					R.layout.listview_friend_adapter, friends));
		} catch (Exception e) {
			Log.i("FriendListActivity", e.getMessage());
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.friend_list, menu);
		return true;
	}

	class FriendClickListener implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// Do nothing at the moment
		}
	}

	protected OnClickListener sendButtonClick() {
		return new OnClickListener() {
			@Override
			public void onClick(View v) {
				 String inviteList = "";
				for(int i=0;i<friends.length;i++){
					 View child = lv.getChildAt(i);
					 Useraccount ac = friends[i];
					 CheckBox cb = (CheckBox)child.findViewById(R.id.is_invite_friend);
					 if(cb.isChecked()){
						 inviteList += ac.getId()+",";
					 }
				}
				if(inviteList.length()>0){
					inviteList = inviteList.substring(0, inviteList.length()-1);
					sendInvite(inviteList);
				}
			}
		};
	}

	protected void sendInvite(String inviteList) {
		if(inviteList==null || planId == null)
			Toast.makeText(this, "请选择要邀请的人", Toast.LENGTH_SHORT).show();
		else
			api.sendPlanInvite(this,planId,inviteList);
	}

	class FriendViewAdapter extends ArrayAdapter<Useraccount> {

		protected Context context;
		protected int resourceId;
		protected Useraccount[] friends;

		public FriendViewAdapter(Context context, int textViewResourceId,
				Useraccount[] friends) {
			super(context, textViewResourceId, friends);
			this.context = context;
			this.resourceId = textViewResourceId;
			this.friends = friends;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View row = convertView;
			if (row == null) {
				LayoutInflater inflater = ((Activity) context)
						.getLayoutInflater();
				row = inflater.inflate(resourceId, parent, false);
			}
			Useraccount f = friends[position];
			TextView name = (TextView) row.findViewById(R.id.friend_name);
			CheckBox checkBox = (CheckBox) row
					.findViewById(R.id.is_invite_friend);
			name.setText(f.getName());
			return row;
		}

	}

	//Call back function after success
	public void sentInviteSuccess(JSONObject json) {
		
	}

}
