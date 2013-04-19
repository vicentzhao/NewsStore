package com.ccdrive.newsstore.util;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.ccdrive.newsstore.bean.News;
import com.ccdrive.newsstore.bean.OrderBean;
import com.ccdrive.newsstore.bean.Paper;
import com.ccdrive.newsstore.bean.PayOrderBean;
import com.ccdrive.newsstore.bean.PostMent;
import com.ccdrive.newsstore.bean.SoftwareBean;
import com.ccdrive.newsstore.suffix.CryptUtil;

public class JsonUtil {
	
//	 private String id;  //music id
//	 private String pName;  // music title
//	 private String ALBUM;  //music  
//	 private String PIC;    //imagepath
//	 private String QNAME;  //quity
//	 private String pcname;  //request
//	 private String pnname;   //way
//	 private String setname;  //set
	
	public static ArrayList<News> getNewsList(String str) {
		ArrayList<News> list  = new ArrayList<News>();
		if(str == null)return list;
		News bean = null;
		try {
			JSONObject jsonObject =  new JSONObject(str);
			JSONArray jsonA = jsonObject.getJSONArray("data");
			for(int i =0;i<jsonA.length();i++){
				JSONObject jsonO = jsonA.getJSONObject(i);
				bean = new News();
				bean.setImage_path(jsonO.getString("PIC"));
				bean.setName(jsonO.getString("NAME"));
				bean.setContent(jsonO.getString("CONTENT"));
				bean.setLink(jsonO.getString("LINK"));
				bean.setId(jsonO.getString("ID"));
				list.add(bean);
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list; 
	}
	
	
	public static ArrayList<Paper> getMusicList(String str) {
		ArrayList<Paper> list  = new ArrayList<Paper>();
		if(str == null)return list;
		Paper music = null;
		try {
			JSONObject jsonObject =  new JSONObject(str);
			JSONArray jsonA = jsonObject.getJSONArray("data");
			JSONObject jsonB = jsonObject.getJSONObject("page");
			for(int i =0;i<jsonA.length();i++){
				JSONObject jsonO = jsonA.getJSONObject(i);
				music = new Paper();
				music.setPubName(jsonO.getString("PUBNAME"));
				music.setImage_path(jsonO.getString("PIC"));
				music.setName(jsonO.getString("PNAME"));
				music.setInfo(jsonO.getString("PHID"));
				music.setVersion(jsonO.getString("QUA"));
				music.setId(jsonO.getString("ID"));
				if(!jsonO.isNull("artist")){
				music.setArtist(jsonO.getString("artist"));
				}
				if(!jsonO.isNull("company")){
				music.setCompany(jsonO.getString("company"));
				}
				if(!jsonO.isNull("language")){
				music.setLanguage(jsonO.getString("language"));
				}
				if(!jsonO.isNull("pubdate")){
				music.setPubdate(jsonO.getString("pubdate"));
				}
				if(!jsonO.isNull("TYPE")){
					music.setType(jsonO.getString("TYPE"));
				}
				list.add(music);
			}
			 
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 return list; 
	}
	/**
	 * 获取单个app的信息
	 * @param str
	 * @return
	 */
	public static SoftwareBean getSoftBean(String str){
		SoftwareBean bean = null;
		ArrayList<PostMent> postMentList  =new ArrayList<PostMent>();
		try {
         JSONObject jsonO =  new JSONObject(str);
				bean = new SoftwareBean();
				bean.setImage_path(jsonO.getString("PIC"));
				bean.setName(jsonO.getString("PNAME"));
				bean.setInfo(jsonO.getString("PNOTE"));
				bean.setId(jsonO.getString("ID"));
				bean.setAuthor(jsonO.getString("AUTHOR"));
				bean.setRelease(jsonO.getString("RELEASE"));
				bean.setVersion(jsonO.getString("VERSION"));
				bean.setEnvironment(jsonO.getString("ENVIRONMENT"));
				bean.setAddDate(jsonO.getString("ADDTIME"));
				JSONArray postOb = jsonO.getJSONArray("potype");
				for (int i = 0; i < postOb.length(); i++) {
					PostMent pm = new PostMent();
					pm.setType(postOb.getJSONObject(i)
							.getString("TYPE"));
					pm.setId(postOb.getJSONObject(i).getString("PUBID"));
					pm.setPrice(postOb.getJSONObject(i).getString(
							"PRICE"));
					postMentList.add(pm);
				}
				bean.setPostMentList(postMentList);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bean;
	}
	

	/**
	 *  获得applist 
	 * @param str
	 * @return
	 */
	public static ArrayList<SoftwareBean> getSoftBeanList(String str){
		SoftwareBean bean = null;
		ArrayList<SoftwareBean> list = new ArrayList<SoftwareBean>();
		ArrayList<PostMent> postMentList  =new ArrayList<PostMent>();
		if(str == null)return list;
		try {
			JSONObject jsonObject =  new JSONObject(str);
			JSONArray jsonA = jsonObject.getJSONArray("data");
			for(int i =0;i<jsonA.length();i++){
				JSONObject jsonO = jsonA.getJSONObject(i);
			bean = new SoftwareBean();
			bean.setImage_path(jsonO.getString("PIC"));
			bean.setName(jsonO.getString("PNAME"));
			if(!jsonO.isNull("PNOTE")){
			bean.setInfo(jsonO.getString("PNOTE"));
			}
			if(!jsonO.isNull("NOTE")){
				bean.setInfo(jsonO.getString("NOTE"));
			}
			bean.setAddDate(jsonO.getString("ADDTIME"));
			bean.setId(jsonO.getString("ID"));
			bean.setAuthor(jsonO.getString("AUTHOR"));
			bean.setRelease(jsonO.getString("RELEASE"));
			bean.setVersion(jsonO.getString("VERSION"));
			bean.setEnvironment(jsonO.getString("ENVIRONMENT"));
			if(!jsonO.isNull("potype")){
			JSONArray postOb = jsonO.getJSONArray("potype");
			for (int j = 0; j < postOb.length(); j++) {
				PostMent pm = new PostMent();
				pm.setType(postOb.getJSONObject(i)
						.getString("TYPE"));
				pm.setId(postOb.getJSONObject(i).getString("PUBID"));
				pm.setPrice(postOb.getJSONObject(i).getString(
						"PRICE"));
				postMentList.add(pm);
			}
			bean.setPostMentList(postMentList);
			}
			list.add(bean);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	
	
	public static ArrayList<OrderBean> getOrderList(String str){
		ArrayList<OrderBean> list = new ArrayList<OrderBean>();
		OrderBean bean = null;
		if(str == null)return list;
		try {
			JSONArray jsonA = new JSONArray(str);
			for(int i =0;i<jsonA.length();i++){
				JSONObject jsonO = jsonA.getJSONObject(i);
				bean = new OrderBean();
				String name = jsonO.getString("title");
				String version = jsonO.getString("price");
				bean.setName(name);	  
				bean.setVersion(version);	
				bean.setSid(jsonO.getString("sid"));
				list.add(bean);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	
	public static boolean getPayResult(String str){
		boolean result = false;
		if(str == null)return result;
		try {
				JSONObject jsonO = new JSONObject(str);
				if(jsonO.getString("STATE") != null && jsonO.getString("STATE").equals("0") ){
					result = true;
				}else{
					result = false;
				}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	//获得要支付的订单的信息
	
	public static ArrayList<PayOrderBean> getOrderNum(String str){
		ArrayList<PayOrderBean> list = new ArrayList<PayOrderBean>();
		PayOrderBean bean = null;
		if(str == null)return list;
		try {
			JSONObject json = new JSONObject(str);
			JSONArray jsonA = json.getJSONArray("list");
			for(int i =0;i<jsonA.length();i++){
				JSONObject jsonO = jsonA.getJSONObject(i);
				bean = new PayOrderBean();
				String name = jsonO.getString("pname");
				String price = jsonO.getString("PRICE");
				String num =jsonO.getString("NUM");
				String kind=jsonO.getString("TYPE");
				String olid =jsonO.getString("OLID");
				String oorderDate =jsonO.getString("ORDERDATE");
				String orderDate=oorderDate.substring(0, oorderDate.lastIndexOf(" "));
				bean.setName(name);	  
				bean.setPrice(price);	
				bean.setKind(kind);
				bean.setOlid(olid);
				bean.setNum(num);
				bean.setOrderDate(orderDate);
				list.add(bean);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	
	public static void setLogin(String str,Context con){
		try {
			JSONObject json = new JSONObject(str);
			String token = json.getString("TOKEN");
			if(token != null){
				AuthoSharePreference.putToken(con, token);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static String subStr(String str,String key){
		   String temp =str.toLowerCase();
		   key=key.toLowerCase();
		   if(temp.indexOf(key) == -1){
	    	  return "-1";
		   }
	       temp=temp.substring(temp.indexOf(key)+key.length());
    	   temp=temp.substring(0,temp.indexOf(","));
    	   temp=temp.replace("=", "").trim();
    	   temp=temp.replace("[", "").trim();
    	   temp=temp.replace("]", "").trim();
    	   return temp;
	}

}
