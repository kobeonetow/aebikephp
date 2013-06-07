package com.aeenery.aebicycle.model;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public class NameValuePairReflect {
	public static List<NameValuePair> __getNameValuePair(String _className, Object _obj) {
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		try {
			Class c = Class.forName(_className);
			Method m[] = c.getDeclaredMethods();
			for(Method method : m){
				String mName = method.getName().substring(0, 3);
				if(mName.equals("get")){
					String name = method.getName().substring(3).toLowerCase();
					String value = (String)method.invoke(_obj);
					if(value != null && !value.trim().equals("")){
						NameValuePair pair = new BasicNameValuePair(name,value);
						list.add(pair);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public static void __setNameValuePair(String _className, Object _obj, List<NameValuePair> pairs){
		try {
			Class c = Class.forName(_className);
			for(NameValuePair pair : pairs){
				String methodName = "set" + Character.toString(pair.getName().charAt(0)).toUpperCase() + pair.getName().substring(1);
				Method m = c.getMethod(methodName, String.class);
				m.invoke(_obj, pair.getValue());
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
