package com.aeenery.aebicycle.weather;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager.LayoutParams;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.aeenery.aebicycle.BaseActivity;
import com.aeenery.aebicycle.R;
import com.aeenery.aebicycle.entry.BicycleUtil;
import com.aeenery.aebicycle.entry.WeatherInfo;
import com.aeenery.aebicycle.model.HttpParseJsonUtil;
import com.aeenery.aebicycle.model.HttpParseXmlUtil;

public class WeatherActivity extends BaseActivity {
	String cityCode;
	SharedPreferences defCode;
	WeatherInfo weatherInfo;
	ImageButton ibRefresh;
	ImageView ivCurWeather,ivTomWth,ivAfTomWth;
	Bitmap bmWeahter = null,
			bmTomWeather = null,
			bmAfTomWeather = null;
	TextView tvCurWeather,tvCurTemp,tvTips,tvWind,tvDate,tvTemp2,
				tvTomorrow,tvAfTomorrow,tvTemp3,tvCity;
	DisplayMetrics metric;
	EditText etCity;
	Button btSure;
	String[] city;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_weather);
		
		//获取默认城市
		defCode = getSharedPreferences(BicycleUtil.fileDefaultCode, Context.MODE_PRIVATE);
		cityCode = defCode.getString(BicycleUtil.deCode, "101010100");
		metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
		init();
		getWeatherInfo(cityCode);
	}
	
	private void init() {
		// TODO Auto-generated method stub
		//ImageButton refresh = (ImageButton)findViewById(R.id.ibRefresh);
		ivCurWeather = (ImageView)findViewById(R.id.ivCurWeather);
		ivTomWth = (ImageView)findViewById(R.id.imTomorrow);
		ivAfTomWth = (ImageView)findViewById(R.id.imAfTomorrow);
		tvCity = (TextView)findViewById(R.id.tvCity);
		tvCurTemp = (TextView)findViewById(R.id.tvCurTemp);
		tvCurWeather = (TextView)findViewById(R.id.tvCurWeather);
		tvDate = (TextView)findViewById(R.id.tvDate);
		tvTips = (TextView)findViewById(R.id.tvTips);
		tvWind = (TextView)findViewById(R.id.tvWind);
		tvTemp2 = (TextView)findViewById(R.id.tvTemp2);
		tvTemp3 = (TextView)findViewById(R.id.tvTemp3);
		tvTomorrow = (TextView)findViewById(R.id.tvTomorrow);
		tvAfTomorrow = (TextView)findViewById(R.id.tvAfTomorrow);
		ibRefresh = (ImageButton)findViewById(R.id.ibRefresh);
		//刷新键
		ibRefresh.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getWeatherInfo(cityCode);
			}
		});
		
		LayoutInflater inflater = LayoutInflater.from(this);
		//引入窗口配置文件
		View popLayout = inflater.inflate(R.layout.popwindow_more_city, null);
		//创建popupwindow对象
		final PopupWindow pop = new PopupWindow(popLayout,LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT,false);
		//设置一下参数,点击外边可消失
		pop.setBackgroundDrawable(new BitmapDrawable());
		pop.setOutsideTouchable(true);
		//获取焦点，否则无法点击
		pop.setFocusable(true);
		//动态添加TextView控件
		TableLayout tl = (TableLayout)popLayout.findViewById(R.id.talCity);
		city = WeatherActivity.this.getResources().getStringArray(R.array.quick_city);
		etCity = (EditText)popLayout.findViewById(R.id.etCity);
		etCity.setEnabled(true);
		btSure = (Button)popLayout.findViewById(R.id.btSure);
		TableRow tr = null;
		for (int i = 0; i < city.length; i++) {
			if (i%3 == 0) {
				if (tr != null) {
					tl.addView(tr,new TableLayout.LayoutParams());
				}
				tr = new TableRow(tl.getContext());
				tr.setLayoutParams(new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT,
						LayoutParams.WRAP_CONTENT));
				tr.setBaselineAligned(true);
			}
			TextView tv = new TextView(tr.getContext());
			tv.setText(city[i]);
			tv.setTextSize(19);
			tv.setId(i);
			tv.setPadding(5, 10, 5, 15);
			tv.setTextColor(Color.WHITE);
			tv.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					etCity.setText(city[v.getId()]);
				}
			});
			tr.addView(tv,new TableRow.LayoutParams());
		}
		
		btSure.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//SharedPreferences pSharedPreferences = getSharedPreferences(BicycleUtil.fileWeahter,0);
				
				//cityCode = pSharedPreferences.getString(etCity.getText().toString(), null);
				cityCode = HttpParseXmlUtil.getWeatherCityCode(getApplicationContext(), etCity.getText().toString());
				if (cityCode == null) {
					Toast.makeText(WeatherActivity.this, "没有搜到对应城市", 3000).show();
				}
				else {
					pop.dismiss();
					defCode.edit().putString(BicycleUtil.deCode, cityCode).commit();
					getWeatherInfo(cityCode);
				}
			}
		});
		
		tvCity.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!pop.isShowing()) {
					pop.showAsDropDown(v);
				}
			}
		});
	
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			doExit();
			return true;	
		}
		return super.onKeyDown(keyCode, event);
	}
	
	private void getWeatherInfo(final String cCode){
		new AsyncTask<Void, Void, WeatherInfo>(){
			@Override
			protected WeatherInfo doInBackground(Void... params) {
				// TODO Auto-generated method stub
				WeatherInfo info = HttpParseJsonUtil.getWeatherInfo(cCode);
				if (info != null) {
					bmWeahter = HttpParseJsonUtil.getNetImage(info.getImg(),metric);
					bmTomWeather = HttpParseJsonUtil.getNetImage(info.getImg2(),metric);
					bmAfTomWeather = HttpParseJsonUtil.getNetImage(info.getImg3(),metric);
				}
				return info;
			}
			
			@Override
			protected void onPreExecute(){
				super.onPreExecute();
				showDialog();
			};
			
			@Override
			protected void onPostExecute(WeatherInfo result){
				closeDialog();
				if (result != null) {					
			        ivCurWeather.setImageBitmap(bmWeahter);
					ivTomWth.setImageBitmap(bmTomWeather);
					ivAfTomWth.setImageBitmap(bmAfTomWeather);
					tvCity.setText(result.getCity());
					tvCurTemp.setText(result.getTemp());
					tvCurWeather.setText(result.getWeather());
					tvDate.setText(result.getDate());
					tvTips.setText(result.getTips());
					tvTips.setWidth((int) (metric.widthPixels*0.75));
					tvWind.setText("风力:" + result.getWind());
					tvTemp2.setText(result.getTemp2());
					tvTemp3.setText(result.getTemp3());
					tvTomorrow.setText("明天");
					tvAfTomorrow.setText("后天");
				}
				else {
					Toast.makeText(WeatherActivity.this, "数据加载失败，请尝试刷新", 3000).show();
				}
			};
		}.execute();
	}
}
