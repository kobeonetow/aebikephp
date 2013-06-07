package com.aeenery.aebicycle.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import org.json.JSONObject;

import android.util.Log;

public class Plan implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 183801869253751540L;
	
	private String id;
	private String name;
	private String plantype;
	private String startlocation;
	private String endlocation;
	private String userid;
	private String distance;
	private String expecttime;
	private String pplgoing;
	private String pplexpected;
	private String description;
	private Timestamp starttime;
	private Timestamp endtime;
	private String sponsor;
	private String status;
	private String assignStatus;
	
	public String getId(){
		return id;
	}
	public void setId(String id){
		this.id = id;
	}
	public String getPlantype() {
		return plantype;
	}
	public void setPlantype(String plantype) {
		this.plantype = plantype;
	}
	public String getStartlocation() {
		return startlocation;
	}
	public void setStartlocation(String startlocation) {
		this.startlocation = startlocation;
	}
	public String getEndlocation() {
		return endlocation;
	}
	public void setEndlocation(String endlocation) {
		this.endlocation = endlocation;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getDistance() {
		return distance;
	}
	public void setDistance(String distance) {
		this.distance = distance;
	}
	public String getExpecttime() {
		return expecttime;
	}
	public void setExpecttime(String expecttime) {
		this.expecttime = expecttime;
	}
	public String getPplgoing() {
		return pplgoing;
	}
	public void setPplgoing(String pplgoing) {
		this.pplgoing = pplgoing;
	}
	public String getPplexpected() {
		return pplexpected;
	}
	public void setPplexpected(String pplexpected) {
		this.pplexpected = pplexpected;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getStarttime() {
		if(starttime == null) return "-";
		else
			return (starttime.getYear()+1900)+"/"+(starttime.getMonth()+1)+"/"+starttime.getDate() + " " + starttime.getHours() + ":"+ starttime.getMinutes()+":"+starttime.getSeconds();
	}
	public void setStarttime(String starttime) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.CHINA);
		try {
			Log.i("Starttime is",starttime);
			Log.i("Starttime subsrring is",starttime.substring(0, 4));
			if(starttime.substring(0, 4).equals("0000")) {
				this.starttime = null;
				return;
			}else
				this.starttime = new Timestamp(format.parse(starttime).getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	public String getEndtime() {
		if(endtime == null) return "-";
		else
			return (endtime.getYear()+1900)+"/"+(endtime.getMonth()+1)+"/"+endtime.getDate() + " " + endtime.getHours() + ":"+ endtime.getMinutes()+":"+endtime.getSeconds();
	}
	public void setEndtime(String endtime) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.CHINA);
		try {
			if(endtime.substring(0, 4).equals("0000")){
				this.endtime = null;
				return;
			}
			this.endtime = new Timestamp(format.parse(endtime).getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	public String getSponsor() {
		return sponsor;
	}
	public void setSponsor(String sponsor) {
		this.sponsor = sponsor;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getStatus(){
		return this.status;
	}
	public void setStatus(String status){
		this.status = status;
	}
	
	public void __setPlanFromJSONObject(JSONObject json){
		try {
			if (json.has("id")) {
				this.setId(json.getString("id"));
			}
			if (json.has("name")) {
				this.setName(json.getString("name"));
			}
			if (json.has("plantype")) {
				this.setName(json.getString("plantype"));
			}
			if (json.has("status")) {
				this.setStatus(json.getString("status"));
			}
			if (json.has("startlocation")) {
				this.setStartlocation(json.getString("startlocation"));
			}
			if (json.has("endlocation")) {
				this.setEndlocation(json.getString("endlocation"));
			}
			if (json.has("userid")) {
				this.setUserid(json.getString("userid"));
			}
			if (json.has("distance")) {
				this.setDistance(json.getString("distance"));
			}
			if (json.has("expecttime")) {
				this.setExpecttime(json.getString("expecttime"));
			}
			if (json.has("pplgoing")) {
				this.setPplgoing(json.getString("pplgoing"));
			}
			if (json.has("pplexpected")) {
				this.setPplexpected(json.getString("pplexpected"));
			}
			if (json.has("description")) {
				this.setDescription(json.getString("description"));
			}
			if (json.has("starttime")) {
				this.setStarttime(json.getString("starttime"));
			}
			if (json.has("endtime")) {
				this.setEndtime(json.getString("endtime"));
			}
			if (json.has("sponsor")) {
				this.setSponsor(json.getString("sponsor"));
			}
		} catch (Exception e) {
			Log.i("Plan Object",
					"Cannot parse from json object to Plan Strings");
			e.printStackTrace();
		}
	}
	public String __getAssignStatus() {
		return assignStatus;
	}
	public void __setAssignStatus(String assignStatus) {
		this.assignStatus = assignStatus;
	}
}
