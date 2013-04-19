package com.ccdrive.newsstore.page;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.androidquery.AQuery;
import com.ccdrive.newsstore.R;

public class appPage {
	
	private Context mContext;
	private LayoutInflater inflater;
	private Dialog builder ;
	private AQuery aQuery;
	private ProgressDialog pd;
	private String mPath;
	private View appPageView;
	public appPage(Context context,String path) {
		this.mContext = context;
		this.mPath =path;
		inflater =LayoutInflater.from(context);
		builder=new Dialog(context);
		aQuery =new AQuery(context);
		appPageView = inflater.inflate(R.layout.soft_detail, null);
		  builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
			builder.setContentView(appPageView);
			Window dialogWindow = builder.getWindow();
			WindowManager.LayoutParams lp = dialogWindow.getAttributes();
			dialogWindow.setGravity(Gravity.CENTER);
			lp.width = 800;
			lp.height = 640;
			dialogWindow.setAttributes(lp);
			builder.show();
	}
	

}
