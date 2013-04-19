package com.ccdrive.newsstore.util;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import com.ccdrive.newsstore.bean.VersionInfo;

public class XmlParse {
	
	ArrayList<VersionInfo> versionInfoList = null;
	   public ArrayList<VersionInfo>  getVersionInfo(InputStream is ){
		   
		   try {
		   XmlPullParserFactory pullParseFac = XmlPullParserFactory.newInstance();
           XmlPullParser pullParser = pullParseFac.newPullParser();
           //解析xml
           pullParser.setInput(is, "utf-8");
           //开始
           int eventType = pullParser.getEventType();
           
           VersionInfo versionInfo = null;
           while(eventType!=XmlPullParser.END_DOCUMENT){
                   String nodeName = pullParser.getName();
                   switch (eventType) {
                   //文档开始
                   case XmlPullParser.START_DOCUMENT:
                	   versionInfoList = new ArrayList<VersionInfo>();
                           break;
                   //节点开始        
                   case XmlPullParser.START_TAG:
                           if("apk".equals(nodeName)){
                        	   versionInfo = new VersionInfo();
                        	   versionInfo.setName(pullParser.getAttributeValue(0));
                           }else if("package".equals(nodeName)){
                        	   versionInfo.setPackageName(pullParser.nextText().toString().trim());
                                   
                           }else if("filepath".equals(nodeName)){
                        	   versionInfo.setFilepath(pullParser.nextText().toString().trim());
                           }else if("version".equals(nodeName)){
                        	   versionInfo.setVersion(pullParser.nextText().toString().trim());
                           }
                   else if("description".equals(nodeName)){
                	   versionInfo.setDescription(pullParser.nextText().toString().trim());
                   }
                           break;
                   //节点结束
                   case XmlPullParser.END_TAG:
                           if("apk".equals(nodeName)){
                        	   versionInfoList.add(versionInfo);
                        	   versionInfo=null;
                           }
                           break;
                   }
                   eventType = pullParser.next();
           }
		   } catch (Exception e) {
				e.printStackTrace();
				return  null;
			}
		return versionInfoList;
	   } 

}
