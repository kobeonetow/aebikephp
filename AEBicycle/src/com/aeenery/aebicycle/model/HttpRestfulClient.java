package com.aeenery.aebicycle.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

/**

 * 
 * @author J-Liang
 *
 */
public class HttpRestfulClient {
	
	//
	private String uri;
	//
	private String key;
	
	//
	private List<NameValuePair> postdata;
	
	/**
	 * ��ʼ����������ַ���ܳ�
	 * @param uri
	 * ��������ַ������Ǳ��ص�ַ��10.0.2.2����localhost
	 * Android ����
	 * @param key
	 * �������ܳף��������web service�ṩ����ȡ
	 */
	public HttpRestfulClient(String uri, String key){
		this.uri = uri;
		this.key = key;
		this.postdata = new ArrayList<NameValuePair>();
	}
	

	/**
	 * Test call httpclient
	 * @throws IOException 
	 * @throws JSONException 
	 */
	public JSONObject callUrl(String suburi) throws JSONException{
		HttpURLConnection  connection = null;
		try{
//		URL url = new URL("http://webservice.bike/"+suburi);
		URL url = new URL("http://10.0.2.2/"+suburi);
//		URL url = new URL("http://aebike.alienpig.org/"+suburi);
		URLConnection urlConnection = url.openConnection();
		connection = (HttpURLConnection)urlConnection;
		Log.i("Calling url", url.getHost() + url.getPath());
		
		//���ó�ʱ
		connection.setConnectTimeout(30000);
		connection.setReadTimeout(30000);
		
		//����post������
		connection.setDoOutput(true);
		connection.setDoInput(true);
		connection.setUseCaches(false);
		connection.setRequestMethod("POST");
		
		//����http�ļ�ͷ
		String signature  = signatureMD5();
		connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		connection.setRequestProperty("X-CapDyn-AppName", "aebike");
		connection.setRequestProperty("X-CapDyn-MACHASH", signature);
		
		//д��http�ļ�����
		OutputStream outStrm = connection.getOutputStream();
		ObjectOutputStream objOutputStrm = new ObjectOutputStream(outStrm);
		objOutputStrm.reset();
		
		String strbud = "";
		for(NameValuePair p : this.postdata){
			if(strbud.equals(""))
				strbud += URLEncoder.encode("empty=empty") + "&";
			else{
				strbud += "&";
			}
			Log.i("NameValuePait",p.getName()+" = "+p.getValue());
			strbud += p.getName() + "=" + URLEncoder.encode(p.getValue());
		}
		Log.i("Sending", "value "+ strbud);
		objOutputStrm.writeObject(strbud);
		objOutputStrm.flush(); 
		objOutputStrm.close(); 
		
		//����http����
		InputStream inStrm = connection.getInputStream();
		
		//���Output Stream��������ݣ������´κ���
		this.clearNameValuePair();
		
		//ת��ΪjsonObject
		String bufferString = null;
		StringBuffer buffer = new StringBuffer();
		BufferedReader br = new BufferedReader(new InputStreamReader(inStrm));
		while ((bufferString = br.readLine()) != null) {
			buffer.append(bufferString);
		}
//		jsonParam = (JSONObject) xmlSerializer.read(buffer.toString());
		Log.i("responce from server",buffer.toString());
		JSONObject jsonObj = new JSONObject(buffer.toString());
		return jsonObj;
		}catch(IOException e){
			try {
				e.printStackTrace();
				Log.i(this.getClass().getName(), ""+connection.getResponseCode());
				Log.i(this.getClass().getName(), ""+connection.getResponseMessage());
				Log.i(this.getClass().getName(),"The output is not a proper http request :"+e.getCause().getMessage());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			return null;
		}
	}
	/**
	 * ��ȡ��ϣֵ 32λ
	 * @return
	 */
	private String signatureMD5(){
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.reset();
			String poststring = "";
			for(NameValuePair pair: this.postdata){
				poststring += pair.getName() + pair.getValue();
			}
			poststring = this.key + poststring + this.key;
			md.update(poststring.getBytes());
			byte[] digest = md.digest();
			BigInteger in = new BigInteger(1, digest);
			String hashtext = in.toString(16);
			while (hashtext.length() < 32) {
				hashtext = "0" + hashtext;
			}
			return hashtext;
		} catch (Exception e) {
			Log.i("Get signature error", e.getMessage());
			return null;
		}
	}
	
	/**
	 * To convert the InputStream to String we use the BufferedReader.readLine()
     * method. We iterate until the BufferedReader return null which means
     * there's no more data to read. Each line will appended to a StringBuilder
     * and returned as String. 
	 * @param is
	 * @return
	 */
	public static String convertStreamToString(InputStream is) {
        /*
         *
         */
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
 
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
	
	/**
	 * ���һ�����post
	 * @param name
	 * @param value
	 */
	public void addNameValuePair(String name, String value){
		this.postdata.add(new BasicNameValuePair(name,value));
	}
	
	/**
	 * ��post�Ƴ�һ�����
	 * @param name
	 */
	public void removeNameValuePair(String name){
		int counter = 0;
		for(NameValuePair pair : this.postdata){
			if(name.equals(pair.getName())){
				break;
			}
			counter++;
		}
		this.postdata.remove(counter);
	}
	
	public void setNameValuePair(List<NameValuePair> pairs, boolean clear){
		if(this.postdata != null && clear){
			this.postdata.clear();
		}else{
			this.postdata = new ArrayList<NameValuePair>();
		}
		for(NameValuePair pair: pairs){
			this.postdata.add(pair);
//			Log.i(pair.getName(), pair.getValue());
		}
	}
	
	/**
	 * ���post����
	 */
	public void clearNameValuePair(){
		this.postdata.clear();
	}
	
	/**
	 * ���÷���������ַ
	 * @param uri
	 */
	public void setWebServiceBaseUrl(String uri){
		this.uri = uri;
	}
}
