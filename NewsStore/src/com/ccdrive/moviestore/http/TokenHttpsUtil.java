package com.ccdrive.moviestore.http;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.KeyStore;
import java.util.Properties;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.util.EntityUtils;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.content.res.AssetManager;

import com.ccdrive.moviestore.util.JsonUtilJava;

public class TokenHttpsUtil {
	    
	/**
	 * SSL Socket for json
	 * @param String token
	 * @return String jsontext
	 * */
	public static String getTokenResponseSSL(String token){
		String jsonText = "";
		HttpClient client = new DefaultHttpClient();  
		//获得�?
        FileInputStream instream = null; 
        InputStream is=new TokenHttpsUtil().getClass().getClassLoader().getResourceAsStream("tokenhttps.properties");
        Properties pro=new Properties();
        try {
			pro.load(is);
		} catch (IOException e) {
			System.err.println(e.getMessage());
			System.err.println("加载上传路径失败!!!");
		}
        String url =pro.getProperty("url");
        String keyStorePath =pro.getProperty("keyStorePath");
        String keyStorePassWord =pro.getProperty("keyStorePassWord");
        int port =Integer.parseInt(pro.getProperty("port"));
        //发�?https请求
        try {  
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());  
            instream = new FileInputStream(new File(keyStorePath));  
            keyStore.load(instream, keyStorePassWord.toCharArray());  
            SSLSocketFactory socketFactory = new SSLSocketFactory(keyStore);  
            Scheme sch = new Scheme("https", socketFactory, port);  
            client.getConnectionManager().getSchemeRegistry().register(sch);  
            System.out.println(url + "&token=" + token
					+ "&jsoncallback=?");
            HttpPost post = new HttpPost(url + "&token=" + token
					+ "&jsoncallback=?");
            HttpResponse response = client.execute(post);  
            jsonText = getStringFromHttp(response.getEntity());//getjsontext
        } catch (Exception e) {  
            e.printStackTrace();  
        } finally {  
            try {  
                instream.close();  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
            // 释放连接  
            client.getConnectionManager().shutdown();  
        }  
  
		return jsonText;
	}
	/**
	 * SSL Socket for json
	 * @param String username
	 * @param String password
	 * @param String usertype(user,units)
	 * @return String jsontext
	 * */
	public static String getResponseSSL(Context context,String username,String password,String usertype){
		String jsonText = "";
		HttpClient client = new DefaultHttpClient();  
		//获得�?
        InputStream instream = null; 
        InputStream is=new TokenHttpsUtil().getClass().getClassLoader().getResourceAsStream("tokenhttps.properties");
        Properties pro=new Properties();
        try {
			pro.load(is);
		} catch (IOException e) {
			System.err.println(e.getMessage());
			System.err.println("加载上传路径失败!!!");
		}
        String url =pro.getProperty("url");
        String keyStorePath =pro.getProperty("keyStorePath");
        String keyStorePassWord =pro.getProperty("keyStorePassWord");
        int port =Integer.parseInt(pro.getProperty("port"));
        //发�?https请求
        try {  
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());  
            System.out.println("keyStorePath=="+keyStorePath);
//            instream = new FileInputStream(new File(keyStorePath));  
            AssetManager assetManager = context.getAssets();
            instream = assetManager.open("tomcat.keystore");    
            System.out.println("instream=="+instream);
            keyStore.load(instream, keyStorePassWord.toCharArray());  
            SSLSocketFactory socketFactory = new SSLSocketFactory(keyStore);  
            Scheme sch = new Scheme("https", socketFactory, port);  
            client.getConnectionManager().getSchemeRegistry().register(sch);  
      
            HttpPost post = new HttpPost(url + "&username=" + username+"&password="+password+"&usertype="+usertype
					+ "&jsoncallback=?");
            HttpResponse response = client.execute(post);  
            jsonText = getStringFromHttp(response.getEntity());//getjsontext
        } catch (Exception e) {  
            e.printStackTrace();  
        } finally {  
            try {  
                instream.close();  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
            // 释放连接  
            client.getConnectionManager().shutdown();  
        }  
  
		return jsonText;
	}
	/**
	 * SSL Socket for json
	 * @param String token
	 * @return String jsontext
	 * */
	public static String getManagerTokenResponseSSL(Context context,String token){
		String jsonText = "";
		HttpClient client = new DefaultHttpClient();  
		//获得�?
        FileInputStream instream = null; 
        InputStream is=new TokenHttpsUtil().getClass().getClassLoader().getResourceAsStream("tokenhttps.properties");
        Properties pro=new Properties();
        try {
			pro.load(is);
		} catch (IOException e) {
			System.err.println(e.getMessage());
			System.err.println("加载上传路径失败!!!");
		}
        String url =pro.getProperty("managerUrl");
        String keyStorePath =pro.getProperty("managerKeyStorePath");
        String keyStorePassWord =pro.getProperty("managerKeyStorePassWord");
        int port =Integer.parseInt(pro.getProperty("managerPort"));
        //发�?https请求
        try {  
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType()); 
//            String path = context.getFilesDir()+"tokenhttps.properties";
//            File myFileTemp = new File(context.getFilesDir(), "tokenhttps.properties"); 
            File file = new File(context.getFilesDir(), "tokenhttps.properties");
            String cmd = "chmod 777 " +file;
			 try {
			 Runtime.getRuntime().exec(cmd);
			 } catch (Exception e) {
			e.printStackTrace();
			 } 
            instream = new FileInputStream(file);  
            AssetManager assetManager = context.getAssets();
//            instream = assetManager.open("tomcat.keystore");   
            System.out.println("instream=="+instream);
            keyStore.load(instream, keyStorePassWord.toCharArray());  
            SSLSocketFactory socketFactory = new SSLSocketFactory(keyStore);  
            Scheme sch = new Scheme("https", socketFactory, port);  
            client.getConnectionManager().getSchemeRegistry().register(sch);  
            System.out.println(url + "&token=" + token
					+ "&jsoncallback=?");
            HttpPost post = new HttpPost(url + "&token=" + token
					+ "&jsoncallback=?");
            HttpResponse response = client.execute(post);  
            jsonText = getStringFromHttp(response.getEntity());//getjsontext
        } catch (Exception e) {  
            e.printStackTrace();  
        } finally {  
            try {  
                instream.close();  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
            // 释放连接  
            client.getConnectionManager().shutdown();  
        }  
  
		return jsonText;
	}

	/**
	 * buffered read text
	 * @param org.apache.http.HttpEntity
	 * @return String text
	 * @throws UnsupportedEncodingException 
	 * */
	private static String getStringFromHttp(HttpEntity entity) throws UnsupportedEncodingException,IOException {
		StringBuffer buffer = new StringBuffer();
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					entity.getContent(),"ISO-8859-1"));
			String temp = null;
			while ((temp = reader.readLine()) != null) {
				buffer.append(temp);
			}
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new String(buffer.toString().getBytes("ISO-8859-1"),"UTF-8");
	}
	
	public static void main(String [] args){
		/*String josn=getTokenResponseSSL("37e91b7a-6857-43e4-a5fd-ef91dec4c6a1");
		josn=josn.substring(1);
		System.out.println(josn);
		Auth_Token token =(Auth_Token)JsonUtil.jsonToObj(josn,Auth_Token.class);
		System.out.println(token.isTokenflag()+":"+token.getToken());*/
//		String josn=getManagerTokenResponseSSL("2fb0eb7c-bb83-4a9b-85b4-3a5cfe344b8c");
//		josn=josn.substring(1);
//		System.out.println(josn);
//		Manager_Token token =(Manager_Token)JsonUtilJava.jsonToObj(josn,Manager_Token.class);
//		System.out.println(token.isTokenflag()+":"+token.getToken());
	}
}
