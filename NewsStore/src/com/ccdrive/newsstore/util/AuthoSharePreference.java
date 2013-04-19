package com.ccdrive.newsstore.util;

import java.util.ArrayList;
import java.util.HashMap;

import com.ccdrive.newsstore.bean.AppBean;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class AuthoSharePreference {

	public static ArrayList<AppBean> appList = new ArrayList<AppBean>();
	public static ArrayList<HashMap<String,String>> appInstallList = new ArrayList<HashMap<String,String>>();
	private final static String SHAREPREFERENCE_NAME = "AuthoSharePreference";
	
	private final static String KEY_TOKEN = "token";
	private final static String KEY_INSTALL = "apk_install";
	
	public AuthoSharePreference(){
		
	}
	
	public static boolean putToken(Context context, String token)
	{
		SharedPreferences sharedPreferences = context.getSharedPreferences(SHAREPREFERENCE_NAME, 0);
		Editor editor = sharedPreferences.edit();
		editor.putString(KEY_TOKEN, token);
		
		return editor.commit();
	}
	
	public static String getToken(Context context)
	{
		SharedPreferences sharedPreferences = context.getSharedPreferences(SHAREPREFERENCE_NAME, 0);
		return sharedPreferences.getString(KEY_TOKEN, "");
	}
	
	public static boolean putAPKInstall(Context context, String appInstllList){
		SharedPreferences sharedPreferences = context.getSharedPreferences(SHAREPREFERENCE_NAME, 0);
		Editor editor = sharedPreferences.edit();
		editor.putString(KEY_INSTALL, appInstllList);
		
		return editor.commit();
	}
	
	public static String getAPKInstall(Context context){
		SharedPreferences sharedPreferences = context.getSharedPreferences(SHAREPREFERENCE_NAME, 0);
		return sharedPreferences.getString(KEY_INSTALL, "");
	}
	
	public static void clear(Context context) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(SHAREPREFERENCE_NAME, 0);
		sharedPreferences.edit().clear().commit();
	}
}
