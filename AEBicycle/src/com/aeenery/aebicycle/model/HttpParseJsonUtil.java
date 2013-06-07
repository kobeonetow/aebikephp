package com.aeenery.aebicycle.model;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.DisplayMetrics;

import com.aeenery.aebicycle.entry.WeatherInfo;

public class HttpParseJsonUtil {
	private static String weatherUrl = "http://m.weather.com.cn/data/";
	private static String weatherImg = "http://m.weather.com.cn/img/";
	
	public static WeatherInfo getWeatherInfo(String cityCode){
		String uri = weatherUrl + cityCode + ".html";
		String result = new String();	
		try {
			HttpGet httpReq = new HttpGet(uri);
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpResponse httpResponse = httpClient.execute(httpReq);
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				//取得返回数据
				result = EntityUtils.toString(httpResponse.getEntity());
			}else {
				return null;
			}
			JSONObject jsonObj = new JSONObject(result.toString()).getJSONObject("weatherinfo");
			WeatherInfo info = new WeatherInfo();
			info.setCity(jsonObj.getString("city"));
			info.setDate(jsonObj.getString("date_y"));
			info.setWeek(jsonObj.getString("week"));
			info.setLunarDate(jsonObj.getString("date"));
			info.setFctime(jsonObj.getString("fchh"));
			info.setTemp(jsonObj.getString("temp1"));
			info.setTemp2(jsonObj.getString("temp2"));
			info.setTemp3(jsonObj.getString("temp3"));
			info.setWeather(jsonObj.getString("weather1"));
			info.setWeather2(jsonObj.getString("weather2"));
			info.setWeather3(jsonObj.getString("weather3"));
			info.setWind(jsonObj.getString("wind1"));
			info.setImg(weatherImg + "b" + jsonObj.getString("img1") + ".gif");
			info.setImg2(weatherImg + "c" + jsonObj.getString("img3") + ".gif");
			info.setImg3(weatherImg + "c" + jsonObj.getString("img5") + ".gif");
			info.setTips(jsonObj.getString("index_d"));
			return info;
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public static Bitmap getNetImage(String url,DisplayMetrics dm){
		Bitmap bitmap = null;
		Bitmap dstBitmap = null;
		Matrix matrix;
		double h;
		float fw = 1,fh = 1;
		try {
			URL imageUri = new URL(url);
			URLConnection httpConn = imageUri.openConnection();
			httpConn.setDoInput(true);
			httpConn.setConnectTimeout(6 * 1000);// 6秒连接超时
			httpConn.setReadTimeout(6 * 1000); // 6秒读取数据超时
			httpConn.connect();
			// httpConn.get
			String responseCode = httpConn.getHeaderField(0);
			InputStream is = httpConn.getInputStream();
			BufferedInputStream bis = new BufferedInputStream(is);
			bitmap = BitmapFactory.decodeStream(bis);
			bis.close();
			is.close();
		
			//在的大屏幕像素中对图片进行调整
			h = dm.heightPixels;
			if (h >= 800) {
				fw = 1.5f;
				fh = 1.5f;
			}
			matrix=new Matrix();
	        matrix.postScale(fw, fh);
	        dstBitmap=Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),
	        		bitmap.getHeight(),matrix,true);
			return dstBitmap;
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}
}
