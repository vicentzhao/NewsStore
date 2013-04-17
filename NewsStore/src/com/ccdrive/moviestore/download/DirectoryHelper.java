package com.ccdrive.moviestore.download;

import java.io.File;

import com.ccdrive.moviestore.content.Constant;
import com.ccdrive.moviestore.main.MainActivity1;

import android.os.Environment;

public class DirectoryHelper{
	public void getSdCardPath(){
		File sdcardDir = Environment.getExternalStorageDirectory(); 
		String path = sdcardDir.getParent() + sdcardDir.getName();
		Constant.filePath = "/data/data/"+MainActivity1.FILE_PATH+"/shuzifaxing/";
		createFile();
	}

	public void createFile(){
		try{
			// 1.�ж��Ƿ����sdcard
			if (Environment.MEDIA_MOUNTED.equals(Environment
					.getExternalStorageState())){
				// Ŀ¼
				File path = new File(Constant.filePath);
				if (!path.exists()){
					// 2.����Ŀ¼��������Ӧ��������ʱ�򴴽�
					path.mkdirs();
				}

			}
		} catch (Exception e){
			e.printStackTrace();
		}
	}
}
