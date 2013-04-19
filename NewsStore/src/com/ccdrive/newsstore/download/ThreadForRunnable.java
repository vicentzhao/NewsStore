package com.ccdrive.newsstore.download;

import com.ccdrive.newsstore.http.HttpRequest;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ProgressBar;

public class ThreadForRunnable implements Runnable {
	String TAG = "***********************";

	Context con;
	ProgressBar myProgressBar;
	Handler handler;

	public ThreadForRunnable(Context con, ProgressBar myProgressBar,
			Handler handler) {
		this.con = con;
		this.myProgressBar = myProgressBar;
		this.handler = handler;
	}

	@Override
	public void run() {

		String url = HttpRequest.URL_QUERY_DOWNLOAD_URL+HttpRequest.DOWNLOAD_ID;
		try {
			// ���߳������У������Զ��庯��ץ���ļ�
			VideoDownload vd = new VideoDownload(con, url, handler); 
			vd.setDataSource1();
			//vd.setDataSource1();// ���ض���Ƶ�ļ�
			//vd.setDataSource2();//����exe�ļ�
			//vd.setDataSource();// ����apk�ļ�
			//vd.videoGet();
			myProgressBar.setMax((int) vd.getFileSize());// ���ý���������̶�
		} catch (Exception e) {
			Message msg = new Message();
			msg.what = -1;
			msg.getData().putString("error", "����ʧ��");
			handler.sendMessage(msg);
			Log.e(TAG, e.toString());
		}

	}

}
