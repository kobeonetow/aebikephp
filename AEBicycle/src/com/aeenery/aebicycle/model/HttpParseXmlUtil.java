package com.aeenery.aebicycle.model;


import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import android.content.Context;

import com.aeenery.aebicycle.R;


public class HttpParseXmlUtil {
	
	public static String getWeatherCityCode(Context context,String city){
		InputStream is = context.getResources().openRawResource(R.raw.placecode);
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		String result = null;
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(is);
			Element root = document.getDocumentElement();
			NodeList list = root.getElementsByTagName("string");
			for (int i = 0; i < list.getLength(); i++) {
				Element elen = (Element) list.item(i);
				if (elen.getAttribute("name").equals(city)) {
					result = elen.getTextContent();
					break;
				}
			}
			is.close();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
}
