package com.ccdrive.newsstore.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.ccdrive.newsstore.R;
import com.ccdrive.newsstore.http.HttpUtil;

public class UpdateVersion implements Runnable {
	
	public static final String TAG = "version" ;
	private static Context mContext ;
	private static UpdateVersion version ;
	private static Handler mHandler ;
	private String mUrl ;
	private static boolean isApk =true;
	private String mName ;
	
	public static UpdateVersion instance(Context context, Handler handler,boolean boo) {
		mContext = context ;
		mHandler = handler ;
		isApk =boo;
		if(version==null) {
			version = new UpdateVersion() ;
		}
		return version ;
	}
	
	public UpdateVersion setUpdateUrl(String verUrl) {
		this.mUrl = verUrl ;
		return version ;
	}
	
	public UpdateVersion setLoadApkName(String name) {
		this.mName = name ;
		return version ;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){ 
			fileName = Environment.getExternalStorageDirectory().toString() ;
			if(fileIsExist(fileName)) {
				fileName = fileName + "/tvMarket" ;
				downLoad(mUrl);
			} else {
				makeException() ;
			}
		}else{
			if(Build.MODEL.equals("M3 media box board")) { //M3 media box board
				fileName = "/mnt/sda/sda1" ;
				if(fileIsExist(fileName)) {
					System.out.println("file is exist!!!!!!!!"+fileName);
					fileName = fileName + "/tvMarket" ;
					downLoad(mUrl);
				} else {
					makeException() ;
				}
			} else {
				mHandler.post(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						Toast.makeText(mContext, mContext.getResources().getString(R.string.nosdcard), Toast.LENGTH_LONG).show();
					}
				}) ;
			}
		}
	}
	
	ProgressDialog progressDialog  = null ;
	public void showprogressDialog() {
	    progressDialog = new ProgressDialog(mContext); 
	    progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL); 
	    progressDialog.setTitle(mContext.getResources().getString(R.string.start_download)); 
	    progressDialog.setIndeterminate(false);
	    progressDialog.setMax(100); 
	    progressDialog.setCancelable(true);
	    progressDialog.show(); 
	}
	
	private void disMissProgressBar() {
		if(progressDialog!=null) progressDialog.dismiss() ;
	}

    private long loadSize ;
    private String fileName ;
    private int percent ;
	private void downLoad(String url) {
		// TODO Auto-generated method stub
		mHandler.post(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				showprogressDialog() ;
			}
		}) ;
		InputStream is = null ;
		FileOutputStream fileOutputStream = null;
		try {
			is = HttpUtil.getInstance().getInputStreamFromUrl(url) ;
			if (is != null) {
				File file = new File(fileName, mName+".apk");
				if(!file.getParentFile().exists()) 
					file.getParentFile().mkdirs();
				fileOutputStream = new FileOutputStream(file);
				byte[] buf = new byte[1024];
				int ch = -1;
				int count = 0;
				while ((ch = is.read(buf)) != -1) {
					fileOutputStream.write(buf, 0, ch);
					count += ch;
					loadSize = count ;
					mHandler.post(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							long apkSize = HttpUtil.getInstance().getApkSize() ;
							if(progressDialog!=null&&progressDialog.isShowing()&&loadSize<apkSize) {
								double apkSizeD = (double) apkSize ;
								double loadSizeD = (double) loadSize ;
								Log.i(TAG, "load apkSize:"+apkSize) ;
								percent = (int)Math.ceil((loadSizeD / apkSizeD)* 100) ;
								progressDialog.setProgress(percent) ;
							} 
						}
					}) ;
				}
			}
			fileOutputStream.flush();
			if (fileOutputStream != null) {
				fileOutputStream.close();
			}
			complete() ;
		}  catch (Exception e) {
			e.printStackTrace();
			showToast(mContext.getResources().getString(R.string.downErro)) ;
		}  
	}
	
	private boolean fileIsExist(String path) {
		File file = new File(path) ;
		if(file.isDirectory()) {
			return true ;
		}
		return false ;
	}
	
	private void makeException() {
		try {
			int i = 1/0 ;
		} catch (Exception e) {
			showToast("没锟斤拷锟揭碉拷锟借备锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟截★拷") ;
		}
	}
	
	private void showToast(final String str) {
		mHandler.post(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Toast.makeText(mContext, str, Toast.LENGTH_LONG).show() ;
				disMissProgressBar() ;
			}
		}) ;
	}
	
	private void complete() {
		mHandler.post(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				disMissProgressBar() ;
				showToast(mContext.getResources().getString(R.string.downComplete)) ;
				if(isApk){
				install() ;
				}
			}
		}) ;
	}
	
	void install() {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		Uri uri = Uri.fromFile(new File(fileName, mName+".apk")) ;
		intent.setDataAndType(uri,"application/vnd.android.package-archive");
		mContext.startActivity(intent);
	}

}
