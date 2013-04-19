package com.ccdrive.newsstore.main;


import java.io.Serializable;
import java.util.ArrayList;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.ccdrive.newsstore.R;
import com.ccdrive.newsstore.bean.News;
import com.ccdrive.newsstore.content.Constant;
import com.ccdrive.newsstore.http.HttpRequest;
import com.ccdrive.newsstore.http.ImageDownloader;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class NewsDetailActivity extends Activity {
	
	private static int[] horItems = { R.id.item_hor_01, R.id.item_hor_02,
		R.id.item_hor_03, R.id.item_hor_04, R.id.item_hor_05,
		R.id.item_hor_06, R.id.item_hor_07, R.id.item_hor_08,
		R.id.item_hor_09, R.id.item_hor_10, R.id.item_hor_11,
		R.id.item_hor_12, R.id.item_hor_13, R.id.item_hor_14,
		R.id.item_hor_15 };
	
	private static int[] tvlistItem = { R.id.i_text_episode_text1,
		R.id.i_text_episode_text2, R.id.i_text_episode_text3,
		R.id.i_text_episode_text4, R.id.i_text_episode_text5,
		R.id.i_text_episode_text6, R.id.i_text_episode_text7,
		R.id.i_text_episode_text8, R.id.i_text_episode_text9,
		R.id.i_text_episode_text10 };
	
	private AQuery aQuery;
	   @Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tv_detail);
		aQuery=new AQuery(NewsDetailActivity.this);
		Intent intent = getIntent();
		String path = intent.getStringExtra("path");
		int isWhatRight =intent.getIntExtra("isWhatRight", 0);
	     News news = (News) intent.getSerializableExtra("news");
		
		final Button btn_orderall = (Button) (findViewById(R.id.btn_order_allmusic));
		if(isWhatRight==Constant.MYMUSIC){
			btn_orderall.setVisibility(View.INVISIBLE);
		}
		else{
			btn_orderall.setVisibility(View.VISIBLE);
		}
		for (int i = 0; i < tvlistItem.length; i++) {
			findViewById(tvlistItem[i]).setVisibility(View.GONE);
			TextView tv =(TextView)(findViewById(tvlistItem[i]));
			tv.setWidth(LayoutParams.WRAP_CONTENT);
		}
		 
			  String name = news.getName();
			  String content =news.getContent();
			  String image_path_boot =news.getImage_path();
			  final String link = news.getLink();
			  TextView tv =(TextView)(findViewById(tvlistItem[0]));
				tv.setWidth(LayoutParams.WRAP_CONTENT);
				tv.setVisibility(View.VISIBLE);
				tv.setText(name);
				((TextView)findViewById(R.id.musicdetail_text))
						.setText(name);
				((TextView) findViewById(R.id.albumname)).setText(name);
				((TextView)findViewById(R.id.albuminfo)).setText(content);
				tv.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if(link!=null){
						Uri uri = Uri.parse(link);
						Intent it = new Intent(Intent.ACTION_VIEW,uri);
					       startActivity(it);
						}
					}
				});
				String image_path = HttpRequest.URL_QUERY_SINGLE_IMAGE
				+ image_path_boot;
		ImageView imageView = (ImageView)findViewById(R.id.albumimage);
		ImageDownloader downloader = new ImageDownloader(aQuery
				.getContext());
		downloader.download(image_path, imageView);
		final String myId =news.getId();
		findViewById(R.id.btn_order_allmusic).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String path = HttpRequest.URL_QUERY_LIST_ORDER_NEWS_SINGLE+myId;
				final ProgressDialog dialog = ProgressDialog.show(aQuery.getContext(),
						"缓冲中。。", "正在缓冲请稍后。。");
				aQuery.ajax(path, String.class, new AjaxCallback<String>(){

					@Override
					public void callback(String url, String json,
							AjaxStatus status) {
						
						if(json!=null){
							System.out.println("返回的内容"+json);
							dialog.dismiss();
							Toast.makeText(aQuery.getContext(), "订阅成功", 1).show();
						}
					}
					
				});
					
				
			}
		});
	
	   
	   }

}
