package com.ccdrive.newsstore.main;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.ccdrive.newsstore.R;
import com.ccdrive.newsstore.bean.News;
import com.ccdrive.newsstore.bean.SoftwareBean;
import com.ccdrive.newsstore.content.Constant;
import com.ccdrive.newsstore.http.HttpRequest;
import com.ccdrive.newsstore.http.ImageDownloader;
import com.ccdrive.newsstore.util.AppUtil;
import com.ccdrive.newsstore.util.JsonUtil;
import com.ccdrive.newsstore.util.UpdateVersion;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class AppDetailActivity extends Activity {
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
	private int id;
	private ArrayList<SoftwareBean> list;
	AQuery aQuery;
	   @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.soft_detail);
	   Intent t =getIntent();
	    id=t.getIntExtra("itemid",0);
	    list =(ArrayList<SoftwareBean>) t.getSerializableExtra("appList");
	    aQuery =new AQuery(AppDetailActivity.this);
		final Handler hdHandler = new Handler();
		int j = 0;
		for (int i = 0; i < horItems.length; i++) {
			if (id == horItems[i]) {
				j = i;
			}
		}
		final String appId = list.get(j).getId();
		
		String appPath = HttpRequest.URL_QUERY_SINGLE_SOFT + appId;
		final ProgressDialog Dialog = ProgressDialog.show(aQuery.getContext(),
				"缓冲中。。", "正在缓冲请稍后。。");
	    ProDiaglogDimiss(Dialog);
		Dialog.show();
		String web_url = HttpRequest.URL_QUERY_LIST_SOFT + appId;
		aQuery.ajax(web_url, String.class, new AjaxCallback<String>() {// 这里的函数是一个内嵌函数如果是函数体比较复杂的话这种方法就不太合适了
					@Override
					public void callback(String url, String json,
							AjaxStatus status) {
						// 得通过对一个url访问返回的数据存放在JSONObject json中
						// 可以通过json.getContext()得到
						if (json != null) {
							Dialog.dismiss();
							try {
								JSONArray ja = new JSONArray(json);
								for (int i = 0; i < ja.length(); i++) {
									JSONObject jb = ja.getJSONObject(i);
									final String appDownPaths = jb
											.getString("filepath");
									String version = jb.getString("title");
								final String 	appDownPath = HttpRequest.URL_QUERY_DOWNLOAD_URL
											+ appDownPaths + "&" + "多米";
								findViewById(R.id.install)
											.setOnClickListener(
													new OnClickListener() {
														@Override
														public void onClick(
																View v) {
															// TODO
															// Auto-generated
															// method stub
															System.out
																	.println("我已经被监听了");
															String web_url = HttpRequest.URL_QUERY_LIST_SOFT
																	+ appId;
															new AsyncTask<Void, Void, Void>() {
																@Override
																protected Void doInBackground(
																		Void... params) {
																	UpdateVersion updateVersion = UpdateVersion
																			.instance(
																					aQuery.getContext(),
																					hdHandler,
																					true);
																	updateVersion
																			.setUpdateUrl(appDownPath);
																	updateVersion
																			.run();
																	return null;
																}
															}.execute();
														}
													});
								}
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							// successful ajax call, show status code and json
							// content
						} else {
							Dialog.dismiss();
							Toast.makeText(aQuery.getContext(),
									"Error:" + status.getCode(),
									Toast.LENGTH_LONG).show();
						}
					}
				});
		aQuery.ajax(appPath, String.class, new AjaxCallback<String>() {
			@Override
			public void callback(String url, String json, AjaxStatus status) {
				// 得通过对一个url访问返回的数据存放在JSONObject json中 可以通过json.getContext()得到
				if (json != null) {
					System.out.println("下载的数据" + "====" + json);
					final String image_path_boot;
					try {
						SoftwareBean softBean = JsonUtil.getSoftBean(json);
						AppUtil appUtil = new AppUtil(aQuery.getContext());
						 boolean install = appUtil.isInstall(softBean.getName());
						 if(install){
							 findViewById(R.id.uninstall).setVisibility(View.VISIBLE);
							findViewById(R.id.uninstall).setFocusable(false);
							findViewById(R.id.install).setVisibility(View.GONE);
						 }else{
							 findViewById(R.id.uninstall).setVisibility(View.GONE);
							findViewById(R.id.install).setVisibility(View.VISIBLE);
						 }
						((TextView)findViewById(R.id.appname)).setText(softBean.getName());
						((TextView)findViewById(R.id.title_text)).setText(softBean.getName());
						((TextView)findViewById(R.id.appcompany)).setText(aQuery.getContext().getResources().getString(R.string.soft_company)+softBean.getAuthor());
						((TextView) findViewById(R.id.Issuedate)).setText(aQuery.getContext().getResources().getString(R.string.soft_addDate)+softBean.getRelease());
						((TextView)findViewById(R.id.versions)).setText(aQuery.getContext().getResources().getString(R.string.soft_version)+softBean.getVersion());
						((TextView) findViewById(R.id.publishdate)).setText(aQuery.getContext().getResources().getString(R.string.soft_reledate)+softBean.getRelease());
						((TextView) findViewById(R.id.platform)).setText(aQuery.getContext().getResources().getString(R.string.soft_evenment)+softBean.getEnvironment());
						setSoftrecommend(list);
						String path = HttpRequest.URL_QUERY_SINGLE_IMAGE
								+ softBean.getImage_path();
						ImageView imageView = (ImageView)findViewById(R.id.appimage);
						ImageDownloader downloader = new ImageDownloader(aQuery
								.getContext());
						downloader.download(path, imageView);
					} catch (Exception e) {
						e.printStackTrace();
					}
					Dialog.dismiss();
				} else {
					Dialog.dismiss();
					Toast.makeText(aQuery.getContext(),
							"Error:" + status.getCode(), Toast.LENGTH_LONG)
							.show();
				}
			}
		});
	   }
	   public  void DiaglogDimiss(Dialog dialog){
			  dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
					@Override
					public boolean onKey(DialogInterface dialog, int keyCode,
							KeyEvent event) {
						 if (keyCode == KeyEvent.KEYCODE_BACK){
						          // Perform action on key press
							 dialog.dismiss();
						          return true;
						        }
						return false;
					}
				});
		  }
		  public  void ProDiaglogDimiss(ProgressDialog dialog){
			  dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
				  @Override
				  public boolean onKey(DialogInterface dialog, int keyCode,
						  KeyEvent event) {
					  if (keyCode == KeyEvent.KEYCODE_BACK){
						  // Perform action on key press
						  dialog.dismiss();
						  return true;
					  }
					  return false;
				  }
			  });
		  }
		  private  void setSoftrecommend(ArrayList<SoftwareBean> list) {

				ImageDownloader Downloader = new ImageDownloader(aQuery.getContext());
				for (int i = 0; i < 5; i++) {
					findViewById(horItems[i]).setVisibility(View.VISIBLE);
				}
				int j = 0;
				if (list.size() < 5) {
					for (int s = 0; s < 5 - list.size(); s++) {
						findViewById(horItems[4 - s]).setVisibility(View.GONE);
					}
				}
				for (int i = 0; i < ((list.size() <= 5) ? list.size() : 5); i++) {
					final String image_path_boots = list.get(i).getImage_path();
					final int h = horItems[i];
					SoftwareBean sb = list.get(i);
					final String name = list.get(i).getName();
					final String path_root = list.get(i).getDownload_path();
					final String author = list.get(i).getAuthor();
					final String release = list.get(i).getRelease();
					final String addDate =list.get(i).getAddDate();
					final String version =list.get(i).getVersion();
					final String evenment=list.get(i).getEnvironment();
					final String image_path = sb.getImage_path();
					final String title = sb.getName();
					String turePath = HttpRequest.URL_QUERY_SINGLE_IMAGE + image_path;
					String appDownPath = HttpRequest.URL_QUERY_DOWNLOAD_URL
							+ path_root;
					Downloader.download(
							turePath,
							((ImageView) findViewById(horItems[i]).findViewById(
									R.id.ItemIcon)));
					((TextView) findViewById(horItems[i]).findViewById(
							R.id.ItemTitle)).setText(title);
					findViewById(horItems[i]).setOnClickListener(
							new OnClickListener() {
								@Override
								public void onClick(View v) {
									((TextView)findViewById(R.id.appname)).setText(name);
									String path = HttpRequest.URL_QUERY_SINGLE_IMAGE
											+ image_path;
									ImageView imageView = (ImageView) findViewById(R.id.appimage);
									ImageDownloader downloader = new ImageDownloader(
											aQuery.getContext());
									downloader.download(path, imageView);
									((TextView) findViewById(R.id.appname)).setText(name);
									((TextView)findViewById(R.id.title_text)).setText(name);
									((TextView)findViewById(R.id.appcompany)).setText(aQuery.getContext().getResources().getString(R.string.soft_company)+author);
									((TextView)findViewById(R.id.Issuedate)).setText(aQuery.getContext().getResources().getString(R.string.soft_addDate)+addDate);
									((TextView)findViewById(R.id.versions)).setText(aQuery.getContext().getResources().getString(R.string.soft_version)+version);
									((TextView)findViewById(R.id.publishdate)).setText(aQuery.getContext().getResources().getString(R.string.soft_reledate)+release);
									((TextView)findViewById(R.id.platform)).setText(aQuery.getContext().getResources().getString(R.string.soft_evenment)+evenment);
								}
							});
				}
			}

}
