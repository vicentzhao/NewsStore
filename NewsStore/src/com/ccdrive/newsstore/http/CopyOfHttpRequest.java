package com.ccdrive.newsstore.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

public class CopyOfHttpRequest {
     
	public static  String WEB_ROOT = "http://192.168.1.32:8080/";  //外网
//	http://192.168.1.32:8080
//	public static  String WEB_ROOT = "http://42.121.6.154/"; //内网
	
	static HttpClient mHttpClient = null;
	
	public static String URL_LOGIN = WEB_ROOT + "login.action?username=";
	public static String URL ="http://www.baidu.com/images/1.gif";
	public static String URL_QUERY_ALL_RECORD = WEB_ROOT + "index/tapeshop.action?resultType=json";
	public static String URL_QUERY_SINGLE_RECORD = WEB_ROOT + "index/tapeshop!content.action?resultType=json&&id=";
	public static String URL_QUERY_SINGLE_IMAGE = WEB_ROOT + "download.action?inputPath=";
	public static String URL_QUERY_DOWNLOAD_URL = WEB_ROOT + "index/download.action?inputPath=";//下载文件
	public static String URL_QUERY_DOWNLOAD_CHECK = WEB_ROOT + "index/softshop!getFile.action?";//下载验证
	public static String URL_QUERY_SINGLE_IMAGE_TEST = WEB_ROOT + "download.action?";
	public static String URL_QUERY_ALL_BOOK = WEB_ROOT + "index/bookshop.action?resultType=json";
	public static String URL_QUERY_SINGLE_BOOK = WEB_ROOT + "index/bookshop!content.action?resultType=json&&id=";
	public static String URL_QUERY_STROE_ALL_MOVIE = WEB_ROOT + "index/movieshop.action?resultType=json";
	public static String URL_QUERY_SINGLE_MOVIE = WEB_ROOT + "index/movieshop!content.action?resultType=json&&id=";
	public static String URL_QUERY_STROE_ALL_PRINT = WEB_ROOT + "index/journalshop.action?resultType=json";
	public static String URL_QUERY_SINGLE_PRINT = WEB_ROOT + "index/journalshop!content.action?resultType=json&&id=";
	public static String URL_QUERY_STROE_ALL_NEWSPAPER = WEB_ROOT + "index/papershop.action?resultType=json";
	public static String URL_QUERY_SINGLE_NEWSPAPER = WEB_ROOT + "index/papershop!content.action?resultType=json&&id=";
	public static String URL_QUERY_STROE_ALL_MUSIC = WEB_ROOT + "index/musicshop.action?resultType=json";
	public static String URL_QUERY_SINGLE_MUSIC = WEB_ROOT + "index/musicshop!content.action?resultType=json&&id=";
	public static String URL_QUERY_STROE_ALL_TV = WEB_ROOT + "index/tvplayshop.action?resultType=json";
	public static String URL_QUERY_SINGLE_TV = WEB_ROOT + "index/tvplayshop!content.action?resultType=json&&id=";
	public static String URL_QUERY_STROE_ALL_ANIME = WEB_ROOT + "index/cartoonshop.action?resultType=json";
	public static String URL_QUERY_SINGLE_ANIME = WEB_ROOT + "index/cartoonshop!content.action?resultType=json&&id=";
	public static String URL_QUERY_STROE_ALL_SOFT = WEB_ROOT + "index/softshop.action?resultType=json";
	public static String URL_QUERY_SINGLE_SOFT = WEB_ROOT + "index/softshop!content.action?resultType=json&&id=";
	
	public static String URL_QUERY_LIST_RECORD = WEB_ROOT + "index/tapeshop!getworks.action?id=";
	public static String URL_QUERY_LIST_BOOK = WEB_ROOT + "index/bookshop!getworks.action?id=";
	public static String URL_QUERY_LIST_MOVIE = WEB_ROOT + "index/movieshop!getworks.action?id=";
	public static String URL_QUERY_LIST_PRINT = WEB_ROOT + "index/journalshop!getworks.action?id=";
	public static String URL_QUERY_LIST_NEWSPAPER = WEB_ROOT + "index/papershop!getworks.action?id=";
	public static String URL_QUERY_LIST_MUSIC = WEB_ROOT + "index/musicshop!getworks.action?id=";
	public static String URL_QUERY_LIST_TV = WEB_ROOT + "index/tvplayshop!getworks.action?id=";
	public static String URL_QUERY_LIST_ANIME = WEB_ROOT + "index/cartoonshop!getworks.action?id=";  
	public static String URL_QUERY_LIST_SOFT = WEB_ROOT + "index/softshop!getworks.action?id=";
	
	public static String URL_QUERY_LIST_ORDER_SOFT = WEB_ROOT + "index/softshop!getworks.action?resultType=json&id=";//软件订购列表
	public static String URL_QUERY_SINGLE_ORDER_SOFT = WEB_ROOT + "index/softshop!singlebuy.action?type=soft&id=";//订购单个音乐商品
	public static String URL_QUERY_LIST_ORDER_MUSIC = WEB_ROOT + "index/musicshop!getworks.action?resultType=json&id=";//音乐订购列表
	public static String URL_QUERY_SINGLE_ORDER_MUSIC = WEB_ROOT + "index/musicshop!singlebuy.action?type=music&id=";//订购单个音乐商品
	public static String URL_QUERY_LIST_ORDER_BOOK = WEB_ROOT + "index/bookshop!getworks.action?resultType=json&id=";
	public static String URL_QUERY_SINGLE_ORDER_BOOK = WEB_ROOT + "index/bookshop!singlebuy.action?type=book&id=";
	public static String URL_QUERY_LIST_ORDER_PAPER = WEB_ROOT + "index/papershop!getworks.action?resultType=json&id=";
	public static String URL_QUERY_SINGLE_ORDER_PAPER = WEB_ROOT + "index/papershop!singlebuy.action?type=paper&id=";
	public static String URL_QUERY_LIST_ORDER_PRINT = WEB_ROOT + "index/journalshop!getworks.action?resultType=json&id=";
	public static String URL_QUERY_SINGLE_ORDER_PRINT = WEB_ROOT + "index/journalshop!singlebuy.action?type=journal&id=";
	public static String URL_QUERY_LIST_ORDER_ANIM = WEB_ROOT + "index/cartoonshop!getworks.action?resultType=json&id=";
	public static String URL_QUERY_SINGLE_ORDER_ANIM = WEB_ROOT + "index/cartoonshop!singlebuy.action?type=cartoon&id=";
	public static String URL_QUERY_LIST_ORDER_MOVIE = WEB_ROOT + "index/movieshop!getworks.action?resultType=json&id=";
	public static String URL_QUERY_SINGLE_ORDER_MOVIE = WEB_ROOT + "index/movieshop!singlebuy.action?type=movie&id=";
	public static String URL_QUERY_LIST_ORDER_TV = WEB_ROOT + "index/tvplayshop!getworks.action?resultType=json&id=";
	public static String URL_QUERY_SINGLE_ORDER_TV = WEB_ROOT + "index/tvplayshop!singlebuy.action?type=tvplay&id=";
	public static String URL_QUERY_LIST_ORDER_RECORD = WEB_ROOT + "index/tapeshop!getworks.action?resultType=json&id=";
	public static String URL_QUERY_SINGLE_ORDER_RECORD = WEB_ROOT + "index/tapeshop!singlebuy.action?type=tape&id=";
	public static String URL_QUERY_LIST_ORDER = WEB_ROOT + "index/order.action?resultType=json";//订购列表  
	public static String URL_QUERY_LIST_ORDER_NUM = WEB_ROOT + "index/order!pay.action?resultType=json";//支付获取订单号
	public static String URL_QUERY_LIST_PAY_ID = WEB_ROOT + "index/pay.action?resultType=json&num=";//根据订单号得订单id   
	public static String URL_QUERY_LIST_PAY = WEB_ROOT + "index/pay!pay.action?resultType=json&id=";//确认支付
	
	
	public static String DOWNLOAD_ID = null;
	public static String SID = null;//验证ID
	
	public interface OnHttpResponseListener {
		/**
		 * @param responseCode 响应编码
		 * @param what 异步返回执行�?
		 * @param value 返回�?
		 */
		void response(int responseCode, int what, String value,Object object);
		
	}
	
	public interface OnBitmapHttpResponseListener {
		/**
		 * @param responseCode 响应编码
		 * @param what 异步返回执行�?
		 * @param value 返回�?
		 */
		void response(int responseCode, int what, Bitmap bm);
	}
	
    OnHttpResponseListener httpResponseListener;
	
	OnBitmapHttpResponseListener bitMaphttpResponseListener;
	
	public void setHttpResponseListener(OnHttpResponseListener httpResponseListener) {
		this.httpResponseListener = httpResponseListener;
	}
	
	public void setbitmapHttpResponseListener(OnBitmapHttpResponseListener bitMaphttpResponseListener) {
		this.bitMaphttpResponseListener = bitMaphttpResponseListener;
	}
	
	/**
	 * 用户登录
	 */
	public void userLogin(String userNmae,int what,Object obj){
		List<NameValuePair> parameList = new ArrayList<NameValuePair>();
		new HttpRequestTask(what, URL_LOGIN+userNmae,obj).execute(parameList);
	}
	
	/**
	 * 获取所有电影
	 */
	public void queryAllMovies(int what, Object obj){
		List<NameValuePair> parameList = new ArrayList<NameValuePair>();
		new HttpRequestTask(what, URL_QUERY_STROE_ALL_MOVIE,obj).execute(parameList);
	}
	
	/**
	 * 获取所有电视剧
	 */
	public void queryAllTv(int what, Object obj){
		List<NameValuePair> parameList = new ArrayList<NameValuePair>();
		new HttpRequestTask(what, URL_QUERY_STROE_ALL_TV,obj).execute(parameList);
	}
	
	/**
	 * 获取所有动漫
	 */
	public void queryAllAnime(int what, Object obj){ 
		List<NameValuePair> parameList = new ArrayList<NameValuePair>();
		new HttpRequestTask(what, URL_QUERY_STROE_ALL_ANIME,obj).execute(parameList);
	}
	
	/**
	 * 获取所有音乐
	 */
	public void queryAllMusic(int what, Object obj){ 
		List<NameValuePair> parameList = new ArrayList<NameValuePair>();
		new HttpRequestTask(what, URL_QUERY_STROE_ALL_MUSIC,obj).execute(parameList);
	}
	
	/**
	 * 获取所有录音
	 * @author Administrator
	 *
	 */
	public void queryAllRecord(int what, Object obj){
		List<NameValuePair> parameList = new ArrayList<NameValuePair>();
		new HttpRequestTask(what, URL_QUERY_ALL_RECORD,obj).execute(parameList);
	}
	
	/**
	 * 获取所有书
	 * @author Administrator
	 *
	 */
	public void queryAllBook(int what, Object obj){
		List<NameValuePair> parameList = new ArrayList<NameValuePair>();
		new HttpRequestTask(what, URL_QUERY_ALL_BOOK,obj).execute(parameList);
	}
	
	/**
	 * 获取所有刊
	 * @author Administrator
	 *
	 */
	public void queryAllPrint(int what, Object obj){
		List<NameValuePair> parameList = new ArrayList<NameValuePair>();
		new HttpRequestTask(what, URL_QUERY_STROE_ALL_PRINT,obj).execute(parameList);
	}
	
	/**
	 * 获取所有报
	 * @author Administrator
	 *
	 */
	public void queryAllNewspaper(int what, Object obj){
		List<NameValuePair> parameList = new ArrayList<NameValuePair>();
		new HttpRequestTask(what, URL_QUERY_STROE_ALL_NEWSPAPER,obj).execute(parameList);
	}
	
	/**
	 * 获取所有软件
	 * @author Administrator
	 *
	 */
	public void queryAllSoft(int what, Object obj){
		List<NameValuePair> parameList = new ArrayList<NameValuePair>();
		new HttpRequestTask(what, URL_QUERY_STROE_ALL_SOFT,obj).execute(parameList);
	}
	
	
	/**
	 * 查询书籍
	 * @author Administrator
	 *
	 */
	public void querySingleBook(String id,int what, Object obj){
		List<NameValuePair> parameList = new ArrayList<NameValuePair>();
		new HttpRequestTask(what, URL_QUERY_SINGLE_BOOK+id,obj).execute(parameList);
	}
	
	/**
	 * 查询电影
	 * @author Administrator
	 *
	 */
	public void querySingleMovie(String id,int what, Object obj){
		List<NameValuePair> parameList = new ArrayList<NameValuePair>();
		new HttpRequestTask(what, URL_QUERY_SINGLE_MOVIE+id,obj).execute(parameList);
	}
	
	/**
	 * 查询报
	 * @author Administrator
	 *
	 */
	public void querySingleNewspaper(String id,int what, Object obj){
		List<NameValuePair> parameList = new ArrayList<NameValuePair>();
		new HttpRequestTask(what, URL_QUERY_SINGLE_NEWSPAPER+id,obj).execute(parameList);
	}
	
	/**
	 * 查询刊
	 * @author Administrator
	 *
	 */
	public void querySinglePrint(String id,int what, Object obj){
		List<NameValuePair> parameList = new ArrayList<NameValuePair>();
		new HttpRequestTask(what, URL_QUERY_SINGLE_PRINT+id,obj).execute(parameList);
	}
	
	/**
	 * 查询音乐
	 * @author Administrator
	 *
	 */
	public void querySingleMusic(String id,int what, Object obj){
		List<NameValuePair> parameList = new ArrayList<NameValuePair>();
		new HttpRequestTask(what, URL_QUERY_SINGLE_MUSIC+id,obj).execute(parameList);
	}
	
	/**
	 * 查询电视剧
	 * @author Administrator
	 *
	 */
	public void querySingleTv(String id,int what, Object obj){
		List<NameValuePair> parameList = new ArrayList<NameValuePair>();
		new HttpRequestTask(what, URL_QUERY_SINGLE_TV+id,obj).execute(parameList);
	}
	
	
	/**
	 * 查询动漫
	 * @author Administrator
	 *
	 */
	public void querySingleANIME(String id,int what, Object obj){
		List<NameValuePair> parameList = new ArrayList<NameValuePair>();
		new HttpRequestTask(what, URL_QUERY_SINGLE_ANIME+id,obj).execute(parameList);
	}
	
	/**
	 * 查询录音文件
	 * @author Administrator
	 *
	 */
	public void querySingleRecord(String id,int what, Object obj){
		List<NameValuePair> parameList = new ArrayList<NameValuePair>();
		new HttpRequestTask(what, URL_QUERY_SINGLE_RECORD+id,obj).execute(parameList);
	}
	
	/**
	 * 查询软件
	 * @author Administrator
	 *
	 */
	public void querySingleSoft(String id,int what, Object obj){
		List<NameValuePair> parameList = new ArrayList<NameValuePair>();
		new HttpRequestTask(what, URL_QUERY_SINGLE_SOFT+id,obj).execute(parameList);
	}
	
	/**
	 * 查询软件下载
	 * @author Administrator
	 *
	 */
	public void queryListDowanloadSoft(String id,String url,int what, Object obj){
		List<NameValuePair> parameList = new ArrayList<NameValuePair>();
		new HttpRequestTask(what,url+id,obj).execute(parameList);
	}
	
	/**
	 * 查询软件订购列表
	 * @param param
	 * @param eventListener 
	 */
	public void queryOrderSoftList(String URL,String id,int what) {
		List<NameValuePair> valuePairs = new ArrayList<NameValuePair>();
//		valuePairs.add(new BasicNameValuePair("sid", id));
//		valuePairs.add(new BasicNameValuePair("type", type));
		new HttpRequestTask(what,URL+id).execute(valuePairs);
	}
	
	
	
	/**
	 * 软件订购
	 * @param param
	 * @param eventListener 
	 */
	public void OrderSoftSingle(String URL,String id,int what) {
		List<NameValuePair> valuePairs = new ArrayList<NameValuePair>();
		new HttpRequestTask(what,URL+id).execute(valuePairs);
	}
	
	
	/**
	 * 异步请求图片
	 * http://192.168.16.109:9090/sms_web/HeadDownLoadServlet.do?path=
	 * @param param
	 * @param eventListener 
	 */
	public void searchSingleImage(String path, int what) {
		List<NameValuePair> valuePairs = new ArrayList<NameValuePair>();
		valuePairs.add(new BasicNameValuePair("inputPath", path));
		new HttpRequestBitmapTask(what,URL_QUERY_SINGLE_IMAGE_TEST).execute(valuePairs);
	}
	
	
	/**
	 * 下载验证
	 * @param param
	 * @param eventListener 
	 */
	public void DownCheck(String id,String type, int what) {
		List<NameValuePair> valuePairs = new ArrayList<NameValuePair>();
		new HttpRequestTask(what,URL_QUERY_DOWNLOAD_CHECK+"sid="+id+"&type="+type).execute(valuePairs);
	}
	
	/**
	 * 订单列表
	 * @param param
	 * @param eventListener 
	 */
	public void getOrderList(int what) {
		List<NameValuePair> valuePairs = new ArrayList<NameValuePair>();
		new HttpRequestTask(what,URL_QUERY_LIST_ORDER).execute(valuePairs);
	}
	
	/**
	 * 订单列表
	 * @param param
	 * @param eventListener 
	 */
	public void getOrderListNum(int what) {
		List<NameValuePair> valuePairs = new ArrayList<NameValuePair>();
		new HttpRequestTask(what,URL_QUERY_LIST_ORDER_NUM).execute(valuePairs);
	}
	
	/**
	 * 得订单ID
	 * @param param
	 * @param eventListener         
	 */
	public void getOrderListID(String num,int what) {
		List<NameValuePair> valuePairs = new ArrayList<NameValuePair>();
		new HttpRequestTask(what,URL_QUERY_LIST_PAY_ID+num).execute(valuePairs);
	}
	
	/**
	 * 支付
	 * @param param
	 * @param eventListener 
	 */
	public void doPayOrder(String id,int what) {  
		List<NameValuePair> valuePairs = new ArrayList<NameValuePair>();  
		new HttpRequestTask(what,URL_QUERY_LIST_PAY+id).execute(valuePairs);
	}
	
	/**
	 * 下载文件
	 * @param param
	 * @param eventListener 
	 */
	public void Down(String path, int what) {
		List<NameValuePair> valuePairs = new ArrayList<NameValuePair>();
		valuePairs.add(new BasicNameValuePair("inputPath", path));
		new HttpRequestBitmapTask(what,URL_QUERY_SINGLE_IMAGE_TEST).execute(valuePairs);
	}
	
	 
	public static InputStream getImageStream(String urlpath) throws Exception {
	  URL url = new URL(URL_QUERY_SINGLE_IMAGE+urlpath);
	  HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	  conn.setRequestMethod("GET");
	  conn.setConnectTimeout(6 * 1000);
	  if(conn.getResponseCode()==200){
	   InputStream inputStream=conn.getInputStream();
	   return inputStream;
	  }
	  return null;
	}
	
	private class HttpRequestTask extends AsyncTask<List<NameValuePair>, String, String> {
		private int responseCode = -1;
		private int what = 0;
		String mURL = null;
		Object mObject = null;
		
		public HttpRequestTask(int what, String url,Object object){
			this.what = what;
			mURL = url;
			mObject = object;
		}
		
		public HttpRequestTask(int what, String url) {
			this.what = what;
			mURL = url;
		}    
		
		@Override
		protected String doInBackground(List<NameValuePair>... params) {
			Log.d("log", "HTTP URL"+mURL);
			HttpPost post = new HttpPost(mURL);
			HttpParams httpParameters = new BasicHttpParams();   
			try {
				if (params!= null && params.length > 0) {
					post.setEntity(new UrlEncodedFormEntity(params[0],
							HTTP.UTF_8));
				}
//				 HttpConnectionParams.setConnectionTimeout(httpParameters, 1000*60);   
//		         HttpClient httpClient = new DefaultHttpClient(httpParameters); 
				 HttpResponse response = getHttpClient().execute(post);
				if ((responseCode = response.getStatusLine().getStatusCode()) != HttpStatus.SC_OK)
					return null;
				HttpEntity httpEntity = response.getEntity();
				return EntityUtils.toString(httpEntity);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}catch(HttpHostConnectException e){
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(String string) {
			if (httpResponseListener != null)
				httpResponseListener.response(responseCode, what,string,mObject);
		}
		
	}
	
	private synchronized HttpClient getHttpClient() {
		if (null != mHttpClient)return mHttpClient;
		
		HttpParams httpParams = new BasicHttpParams();
		HttpProtocolParams.setContentCharset(httpParams, HTTP.UTF_8);
		HttpProtocolParams.setVersion(httpParams, HttpVersion.HTTP_1_1);
		HttpProtocolParams.setUseExpectContinue(httpParams, false);

		// 从连接池中取连接的超时时�?
		ConnManagerParams.setTimeout(httpParams, 2000);
		
		// 连接超时
		HttpConnectionParams.setConnectionTimeout(httpParams,
				1000 * 50);

		// 读取超时,两个连续数据包的间隔时间
		HttpConnectionParams.setSoTimeout(httpParams, 1000 * 20);

		// 信任�?��的连�?
		SchemeRegistry schemeRegistry = new SchemeRegistry();
		schemeRegistry.register(new Scheme("http", PlainSocketFactory
				.getSocketFactory(), 80));
		schemeRegistry.register(new Scheme("https", SSLSocketFactory
				.getSocketFactory(), 443));

		ClientConnectionManager clientConnectionManager = new ThreadSafeClientConnManager(
				httpParams, schemeRegistry);
		
		DefaultHttpClient httpClient = new DefaultHttpClient(
				clientConnectionManager, httpParams);
		
		//请求超时
		httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 60000);
		//读取超时
		httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 60000);
		
		mHttpClient = httpClient;
		return httpClient;
	}
	
	/*
	 * 异步下载图片
	 */
	private class HttpRequestBitmapTask extends AsyncTask<List<NameValuePair>, String, Bitmap> {
		private int responseCode = -1;
		private int what = 0;
		String mURL = null;
		
		public HttpRequestBitmapTask(int what, String url) {
			this.what = what;
			mURL = url;
		}
		
		@Override
		protected Bitmap doInBackground(List<NameValuePair>... params) {
			HttpPost post = new HttpPost(mURL);
			try {
				if (params!= null && params.length > 0) {
					post.setEntity(new UrlEncodedFormEntity(params[0],
							HTTP.UTF_8));
				}
				HttpResponse response = getHttpClient().execute(post);
				if ((responseCode = response.getStatusLine().getStatusCode()) != HttpStatus.SC_OK){
					return null;
				}
				HttpEntity httpEntity = response.getEntity();
				InputStream inputStream = httpEntity.getContent();
				byte[] bytes = readInputStream(inputStream);   
				Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
				return bitmap;
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) { 
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
		
		public byte[] readInputStream(InputStream is) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int length = -1;
			try {
				while ((length = is.read(buffer)) != -1) {
					baos.write(buffer, 0, length);
				}
				baos.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
			byte[] data = baos.toByteArray();
			try {
				is.close();
				baos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return data;
		}
		
		protected void onPostExecute(Bitmap bitmap) {
			if (bitMaphttpResponseListener != null)
				bitMaphttpResponseListener.response(responseCode, what,bitmap);
			if(bitmap != null && bitmap.isRecycled()){
				bitmap.recycle();
				bitmap = null;
			}
			System.gc();
		}
		
	}
	
}
