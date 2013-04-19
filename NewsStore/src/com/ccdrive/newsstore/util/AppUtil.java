package com.ccdrive.newsstore.util;

import java.util.List;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

public class AppUtil {
	
	
	 private Context mContext;
	
	 public AppUtil(Context mContext) {
		this.mContext = mContext;
	}

	public boolean  isInstall(String appName){
		  int count; 
		  boolean  isInstall =false;
          PackageManager pckMan = mContext.getPackageManager(); 
          List<PackageInfo> packs = pckMan.getInstalledPackages(0); 
          count = packs.size(); 
          String name; 
          int installedNum = 0; 
          for (int i = 0; i < count; i++ ) { 
              PackageInfo p = packs.get(i); 
              if ( p.versionName == null){ 
                  continue; 
              } 
              //判断该软件包是否在/data/app目录下 
              ApplicationInfo appInfo = p.applicationInfo; 
              name = p.applicationInfo.loadLabel(pckMan).toString(); 
              if(appName.equals(name)){
            	  isInstall=true;
            	  break;
              }
          } 
            
		 
		 return isInstall;
	 }
	  

}
