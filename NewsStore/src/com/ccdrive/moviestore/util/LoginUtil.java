package com.ccdrive.moviestore.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.ccdrive.moviestore.http.HttpRequest;

public class LoginUtil {
	  private String macAddress;
	  private Context mContext;
	  private SharedPreferences sharedPreferences;
	  private AQuery aQuery;
	  private String path;
	  private String driveid;
	  private boolean isLogin;
	  
	 public LoginUtil(Context mContext) {
		 this.mContext = mContext;
	  }
     public boolean CheckUerInfo(){
    	 isLogin  =false;
    	 sharedPreferences =mContext. getSharedPreferences("userInfo", mContext.MODE_WORLD_READABLE);
    	 String userInfo =  sharedPreferences.getString("userInfo", "");
       if(userInfo.length()!=0){
     	String[] users = userInfo.split(",");
         for (String str : users)
         {
           String name = users[0];
           String password= users[1];
            path =HttpRequest.URL_QUERY_LOGIN+"username="+name+"&"+"password="+password+"&type=2"+"&driveid="+driveid+"&mac="+macAddress;
         }
         aQuery.ajax(path, String.class, new AjaxCallback<String>() {

				@Override
				public void callback(String url, String object,
						AjaxStatus status) {
					
					if(object!=null){
						String ss ="\"fasle\"";
					if(!ss.equals(object)){
						isLogin=true;
					}else{
						isLogin=false;
					}
				}
				else {
					Toast.makeText(aQuery.getContext(), "网络错误，请重试", 1).show();
				}
				}
				 
			});
     }
       return isLogin;
     }
     private  void getWifiMacAddress(Context context) {
     	final WifiManager wifi=(WifiManager)context.getSystemService(Context.WIFI_SERVICE);
     	if(wifi==null) return;
     	WifiInfo info=wifi.getConnectionInfo();
     	this.macAddress=info.getMacAddress();
     	if(this.macAddress==null && !wifi.isWifiEnabled()) {
     	 new Thread() {
     	@Override
     	public void run() {
     	wifi.setWifiEnabled(true);
     	for(int i=0;i<10;i++) {
     	WifiInfo _info=wifi.getConnectionInfo();
     	 macAddress=_info.getMacAddress();
     	if(macAddress!=null) break;
     	try {
     		Thread.sleep(500);
     	} catch (InterruptedException e) {
     		// TODO Auto-generated catch block
     		e.printStackTrace();
     	}
     	}
     	wifi.setWifiEnabled(false);
     	}
     	 }.start();
     	}
     	}
 
}
