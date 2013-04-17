package com.ccdrive.moviestore.download;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import com.ccdrive.moviestore.content.Constant;
import com.ccdrive.moviestore.http.FileCache;
import com.ccdrive.moviestore.util.JsonUtil;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class VideoDownload {
	Context con;
	private String downloadUrl;// ����·��
	File dFile1;
	FileOutputStream outs1;
	private long fileSize = 0;// ԭʼ�ļ���С
	Handler handler;

	/**
	 * ��ȡ�ļ���С
	 * 
	 * @return
	 */
	public long getFileSize() {
		return fileSize;
	}

	public VideoDownload(Context con, String url, Handler handler) {
		this.con = con;
		this.downloadUrl = url;
		this.handler = handler;
	}

	//���½����(������ͨ��getContentLength����ȡ�ļ����ȵ������)
	public void updateProgressBar(int readLen,long total) {
		Message msg = new Message();
		msg.what = 1;
		msg.getData().putInt("size", readLen);
		msg.getData().putLong("total", total);
		//ͨ�����̵߳�handler֪ͨ���̸߳���UI������Ľ��
		handler.sendMessage(msg);
	}
	
	public void finishProgressBar() {
		Message msg = new Message();
		msg.what = 2;
		//ͨ�����̵߳�handler֪ͨ���̸߳���UI������Ľ��
		handler.sendMessage(msg);
	}

	/* �Զ���setDataSource�����߳����� */
	public void setDataSource1() throws Exception {

		URL myURL = new URL("http://www.vocy.com/download/yichang.apk");
		//��SD��ʱ�ڴ�洢
		File myFileTemp = new File(con.getFilesDir(), getFileName(downloadUrl)+  
				 getFileExtension(downloadUrl));   
		downloadFile(myURL, myFileTemp);
        Constant.FILE_NAME = myFileTemp;
        finishProgressBar(); 
	} 
	
	public static void downloadFile(URL source, File destination) {
		try {
			FileUtils.copyURLToFile(source, destination, 6000,3000);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	// ȡ�ü����ļ��ĺ�׺��
	public static String getFileExtension(String strFileName) {
		File myFile = new File(strFileName);
		String strFileExtension = myFile.getName();
		strFileExtension = (strFileExtension.substring(strFileExtension
				.lastIndexOf(".") + 1)).toLowerCase();  

		if (strFileExtension == "") {
			/* ���޷�˳��ȡ����չ��Ĭ��Ϊ.dat */
			strFileExtension = "dat";
		}
		return strFileExtension;
	}
 
	// ȡ�ü����ļ���
	public static String getFileName(String strFileName) {  
			File myFile = new File(strFileName);
			String strFileExtension = myFile.getName();
			strFileExtension = (strFileExtension.substring(strFileExtension
					.lastIndexOf("\\") + 1,strFileExtension.lastIndexOf(".")+1)).toLowerCase();

			if (strFileExtension == "") {
				/* ���޷�˳��ȡ����չ��Ĭ��Ϊ.dat */
				strFileExtension = "dat";
			}
			return strFileExtension;
		}
}
