package com.ccdrive.newsstore.http;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.util.Log;

public class FileCache {
	
	private static final String LOG_TAG = FileCache.class.getSimpleName();
	
	private File cacheDir;
//	private File cacheBrowse;//	SimpleDateFormat sDate;
	String dt;
	private SharedPreferences sp;
	private SharedPreferences.Editor editor;
	
	/**
	 * @param context
	 */
	public FileCache(Context context) {
		if(android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
			cacheDir = new File(android.os.Environment.getExternalStorageDirectory(), "shuzifaxing_huancun");
		} else {
			cacheDir = context.getCacheDir();
		}
		if(!cacheDir.exists()) {
			cacheDir.mkdir();
		}
	}
	
	/**
	 * 文件存储路径
	 * @param context
	 * @param name
	 * @param suffix
	 * @return
	 */
	public static File getFileName(Context context,String name,String suffix) {
		File store;
		if(android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
			File path = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/shuzifaxing/");
			if (!path.exists()){
				path.mkdirs();
			}
			store = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/shuzifaxing/",name+suffix);
		} else {
			store = context.getCacheDir();
		}
		return store;
	}
	/**
	 * 获取当前时间转化成string类型
	 * @return
	 */
	private String getTimeStr() {
		
		TimeZone tz = TimeZone.getDefault();
		 Calendar ca = Calendar.getInstance(tz);
		 String strYear = String.valueOf(ca.get(Calendar.YEAR));//获取当前年时间并转为字符串型
		 String strMonth = String.valueOf(ca.get(Calendar.MONTH)+1);//获取当前月时间并转为字符串型
		                String strDate = String.valueOf(ca.get(Calendar.DATE));//获取当前日时间并转为字符串型
		                String SysDate = null;
		                if((ca.get(Calendar.MONTH)+1)<=9&&ca.get(Calendar.DATE)<=9)
		                {
		                 SysDate=strYear+"0"+strMonth+"0"+strDate;
		                }else
		                 if((ca.get(Calendar.MONTH)+1)>9&&ca.get(Calendar.DATE)>9)
		                 {
		                  SysDate=strYear+strMonth+strDate;
		                 }else
		                  if((ca.get(Calendar.MONTH)+1)<=9&&ca.get(Calendar.DATE)>9)
		                  {
		                   SysDate=strYear+"0"+strMonth+strDate;
		                  }else
		                   if((ca.get(Calendar.MONTH)+1)>9&&ca.get(Calendar.DATE)<=9)
		                   {
		                    SysDate=strYear+strMonth+"0"+strDate;
		                   }
		
		
		return SysDate;
	}

	/**
	 * @param url
	 * @param bitmap
	 */
	public void addToFileCache(String url, InputStream inputStream) {
		OutputStream outputStream = null;
		try {
			outputStream = new FileOutputStream(getFromFileCache(url));
			copyStream(inputStream, outputStream);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if(outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	public File getFromFileCache(String url) {
		String fileName = urlToFileName(url);
		File file = new File(cacheDir, fileName);
		return file;
	}
	
	/**
	 * 清空文件缓存
	 */
	public void clearCache() {
		File[] files=cacheDir.listFiles();
        if(files==null)
            return;
        for(File f:files)
            f.delete();
	}
	
	
	/**
	 * 
	 * @param url
	 * @return
	 */
	private String urlToFileName(String url) {
		return String.valueOf(url.hashCode());
	}
	
	private void copyStream(InputStream is, OutputStream os){
        final int buffer_size=1024;
        try
        {
            byte[] bytes=new byte[buffer_size];
            for(;;)
            {
              int count=is.read(bytes, 0, buffer_size);
              if(count==-1)
                  break;
              os.write(bytes, 0, count);
            }
        }
        catch(Exception ex){}
    }


}
