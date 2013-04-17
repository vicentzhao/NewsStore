package com.ccdrive.moviestore.util;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.provider.Settings.Secure;
import android.util.Log;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.ccdrive.moviestore.R;

public class MachineInfo {
	
	private  String macAddress=null;
	private  Context mContext;
	
	public HashMap<String, String> MacInfo(){
		getWifiMacAddress(mContext);
		String deviceId = Secure.getString(mContext.getContentResolver(),Secure.ANDROID_ID);
		HashMap<String, String> macInfoHashMap = new HashMap<String, String>();
		macInfoHashMap.put("macAddress", macAddress);
		macInfoHashMap.put("deviceId", deviceId);
		return macInfoHashMap;
	}

	public MachineInfo(Context context) {
		mContext =context;
	}
	private void getWifiMacAddress(Context context) {
		final WifiManager wifi = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		if (wifi == null)
			return; 
		WifiInfo info = wifi.getConnectionInfo();
		this.macAddress = info.getMacAddress();
		if (this.macAddress == null && !wifi.isWifiEnabled()) {
			new Thread() {
				@Override
				public void run() {
					wifi.setWifiEnabled(true);
					for (int i = 0; i < 10; i++) {
						WifiInfo _info = wifi.getConnectionInfo();
						macAddress = _info.getMacAddress();
						if (macAddress != null)
							break;
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
