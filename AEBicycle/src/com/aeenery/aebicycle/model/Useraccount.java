package com.aeenery.aebicycle.model;

import java.io.Serializable;

import org.json.JSONObject;

import android.util.Log;

public class Useraccount implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1114840790364921402L;
	
	private String id;
	private String username;
	private String password;
	private String name;
	private String status;
	private String onlinestatus;
	private String phonenumber;
	private String addressline;
	private String city;
	private String province;
	private String postcode;
	private String age;
	private String sex;
	private String weight;
	private String height;
	private String emailaddress;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getOnlinestatus() {
		return onlinestatus;
	}

	public void setOnlinestatus(String onlinestatus) {
		this.onlinestatus = onlinestatus;
	}

	public String getPhonenumber() {
		return phonenumber;
	}

	public void setPhonenumber(String phonenumber) {
		this.phonenumber = phonenumber;
	}

	public String getAddressline() {
		return addressline;
	}

	public void setAddressline(String addressline) {
		this.addressline = addressline;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public String getEmailaddress() {
		return emailaddress;
	}

	public void setEmailaddress(String emailaddress) {
		this.emailaddress = emailaddress;
	}

	public void __setUserFromJSONObject(JSONObject json) {
		try {
			if (json.has("id")) {
				this.setId(json.getString("id"));
			}
			if (json.has("username")) {
				this.setUsername(json.getString("username"));
			}
			if (json.has("name")) {
				this.setName(json.getString("name"));
			}
			if (json.has("status")) {
				this.setStatus(json.getString("status"));
			}
			if (json.has("onlinestatus")) {
				this.setOnlinestatus(json.getString("onlinestatus"));
			}
			if (json.has("phonenumber")) {
				this.setPhonenumber(json.getString("phonenumber"));
			}
			if (json.has("addressline")) {
				this.setAddressline(json.getString("addressline"));
			}
			if (json.has("city")) {
				this.setCity(json.getString("city"));
			}
			if (json.has("province")) {
				this.setProvince(json.getString("province"));
			}
			if (json.has("postcode")) {
				this.setPostcode(json.getString("postcode"));
			}
			if (json.has("age")) {
				this.setAge(json.getString("age"));
			}
			if (json.has("sex")) {
				this.setSex(json.getString("sex"));
			}
			if (json.has("weight")) {
				this.setWeight(json.getString("weight"));
			}
			if (json.has("height")) {
				this.setHeight(json.getString("height"));
			}
			if (json.has("emailaddress")) {
				this.setEmailaddress(json.getString("emailaddress"));
			}
		} catch (Exception e) {
			Log.i("Useraccount Object",
					"Cannot parse from json object to Useraccount Strings");
			e.printStackTrace();
		}
	}
}