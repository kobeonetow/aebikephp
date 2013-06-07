package com.aeenery.aebicycle.entry;

public class WeatherInfo {
	private String city; //城市名
	private String date; //日期
	private String lunarDate; //农历日期
	private String week; //星期
	private String fcTime; //预报时间
	private String temp; //当日气温
	private String temp2; //明日气温
	private String temp3; //后日气温
	private String weather; //天气
	private String weather2; //天气
	private String weather3; //天气
	private String wind; //风力
	private String tips; //提示
	private String imgUrl;//天气图片
	private String img2Url;
	private String img3Url;
	
	public void setCity(String city){
		this.city = city;
	}
	
	public String getCity(){
		return this.city;
	}
	
	public void setDate(String date){
		this.date = date;
	}
	
	public String getDate(){
		return this.date;
	}
	
	public void setLunarDate(String lunarDate){
		this.lunarDate = lunarDate;
	}
	
	public String getLunarDate(){
		return this.lunarDate;
	}
	
	public void setWeek(String week){
		this.week = week;
	}
	
	public String getWeek(){
		return this.week;
	}
	
	public void setWind(String wind){
		this.wind = wind;
	}
	
	public String getWind(){
		return this.wind;
	}
	
	public void setFctime(String fcTime){
		this.fcTime = fcTime;
	}
	
	public String getFctime(){
		return this.fcTime;
	}
	
	public void setTemp(String temp){
		this.temp = temp;
	}
	
	public String getTemp(){
		return this.temp;
	}
	
	public void setTemp2(String temp2){
		this.temp2 = temp2;
	}
	
	public String getTemp2(){
		return this.temp2;
	}
	
	public void setTemp3(String temp3){
		this.temp3 = temp3;
	}
	
	public String getTemp3(){
		return this.temp3;
	}
	
	public void setWeather(String weather){
		this.weather = weather;
	}
	
	public String getWeather(){
		return this.weather;
	}
	
	public void setWeather2(String weather2){
		this.weather2 = weather2;
	}
	
	public String getWeather2(){
		return this.weather2;
	}
	
	public void setWeather3(String weather3){
		this.weather3 = weather3;
	}
	
	public String getWeather3(){
		return this.weather3;
	}
	
	public void setTips(String tips){
		this.tips = tips;
	}
	
	public String getTips(){
		return this.tips;
	}
	
	public void setImg(String img){
		this.imgUrl = img;
	}
	
	public String getImg(){
		return this.imgUrl;
	}
	
	public void setImg2(String img2){
		this.img2Url = img2;
	}
	
	public String getImg2(){
		return this.img2Url;
	}
	
	public void setImg3(String img3){
		this.img3Url = img3;
	}
	
	public String getImg3(){
		return this.img3Url;
	}
}
