package com.ccdrive.newsstore.main;
//package com.ccdrive.moviestore.main;
//
//import java.util.ArrayList;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import android.app.Activity;
//import android.app.Dialog;
//import android.app.ProgressDialog;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.graphics.Rect;
//import android.util.DisplayMetrics;
//import android.view.Gravity;
//import android.view.KeyEvent;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.ViewGroup.LayoutParams;
//import android.view.Window;
//import android.view.WindowManager;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.androidquery.AQuery;
//import com.androidquery.callback.AjaxCallback;
//import com.androidquery.callback.AjaxStatus;
//import com.ccdrive.moviestore.R;
//import com.ccdrive.moviestore.bean.Movie;
//import com.ccdrive.moviestore.bean.PostMent;
//import com.ccdrive.moviestore.content.Constant;
//import com.ccdrive.moviestore.http.HttpRequest;
//import com.ccdrive.moviestore.http.ImageDownloader;
//
//public class MusicDetail extends Activity{
//	
//	int id;  ArrayList<Movie> list;
//	 View view; String orderId;  int bo,isWhatRight,isWhatLeft;
//	 Context mContext;
//	 Dialog builder;
//	 AQuery aQuery ;
//	public MusicDetail(int id, ArrayList<Movie> list, View view,
//			String orderId, int bo,int isWhatRight,int isWhatLeft,Context mContext) {
//		this.id = id;
//		this.list = list;
//		this.view = view;
//		this.orderId = orderId;
//		this.bo = bo;
//		this.isWhatLeft =isWhatLeft;
//		this.isWhatRight =isWhatRight;
//		this.mContext =mContext;
//		builder= new Dialog(mContext);
//		aQuery =new AQuery(mContext);
//	}
//
//
//
//	private static int[] horItems = { R.id.item_hor_01, R.id.item_hor_02,
//		R.id.item_hor_03, R.id.item_hor_04, R.id.item_hor_05,
//		R.id.item_hor_06, R.id.item_hor_07, R.id.item_hor_08,
//		R.id.item_hor_09, R.id.item_hor_10, R.id.item_hor_11,
//		R.id.item_hor_12, R.id.item_hor_13, R.id.item_hor_14,
//		R.id.item_hor_15 };
//
//	private static int[] tvlistItem = { R.id.i_text_episode_text1,
//		R.id.i_text_episode_text2, R.id.i_text_episode_text3,
//		R.id.i_text_episode_text4, R.id.i_text_episode_text5,
//		R.id.i_text_episode_text6, R.id.i_text_episode_text7,
//		R.id.i_text_episode_text8, R.id.i_text_episode_text9,
//		R.id.i_text_episode_text10 };
//	
//	protected void setMusicDetail() {
//		int j = 0;
//		for (int i = 0; i < horItems.length; i++) {
//			if (id == horItems[i]) {
//				j = i;
//			}
//		}
//		
//		final Button btn_orderall = (Button) (view
//				.findViewById(R.id.btn_order_allmusic));
//		if(isWhatRight==Constant.MYMUSIC){
//			btn_orderall.setVisibility(View.INVISIBLE);
//		}
//		else{
//			btn_orderall.setVisibility(view.VISIBLE);
//		}
//		builder.setContentView(view);
//		Window dialogWindow = builder.getWindow();
//		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
//		dialogWindow.setGravity(Gravity.CENTER);
//		lp.width = 1000;
//		lp.height = 640;
//		dialogWindow.setAttributes(lp);
//		if(isWhatLeft==Constant.MUSICCHAPTER){
//		for (int i = 0; i < tvlistItem.length; i++) {
//			view.findViewById(tvlistItem[i]).setVisibility(View.GONE);
//			TextView tv =(TextView)(view.findViewById(tvlistItem[i]));
//			tv.setWidth(LayoutParams.WRAP_CONTENT);
//		}
//		}else{
//			for (int i = 0; i < tvlistItem.length; i++) {
//				view.findViewById(tvlistItem[i]).setVisibility(View.INVISIBLE);
//			}
//		}
//		final ProgressDialog Dialog = ProgressDialog.show(aQuery.getContext(),
//				"缓冲中。。", "正在缓冲请稍后。。");
//		ProDiaglogDimiss(Dialog);
//		String web_url ="";
//		final String musicId = list.get(j).getId();
//		if(bo==isFilm){
//			web_url= HttpRequest.URL_QUERY_LIST_MOVIE + musicId;
//		}else if(bo==isTv){
//			web_url=HttpRequest.URL_QUERY_LIST_TV+musicId;
//		}
//		aQuery.ajax(web_url, String.class, new AjaxCallback<String>() {// 这里的函数是一个内嵌函数如果是函数体比较复杂的话这种方法就不太合适了
//					@Override
//					public void callback(String url, String json,
//							AjaxStatus status) {
//						if (json != null) {
//							ArrayList<Movie> musicDetialList = new ArrayList<Movie>();
//							System.out.println("下载的数据" + "====" + json);
//							Dialog.dismiss();
//							try {
//								JSONArray ja = new JSONArray(json);
//								for (int i = 0; i < ja.length(); i++) {
//									Movie music = new Movie();
//									JSONObject jb = ja.getJSONObject(i);
//									String sid = jb.getString("sid");
//									String musicpath = jb.getString("filepath");
//									String title = jb.getString("title");
//									String seq = null;
//									if(!jb.isNull("seq")){
//									seq =jb.getString("seq");
//									}
//									music.setSeq(seq);
//									music.setDownload_path(musicpath);
//									music.setId(sid);
//									music.setName(title);
//									musicDetialList.add(music);
//									
//								}
//
//								// MusicAdapter myMusicAdapter = new
//								// MusicAdapter(aQuery.getContext(),
//								// musicDetialList,isWhatRight);
//								// listview.setAdapter(myMusicAdapter);
//								int temp = 0;//级数常量
//								for (int i = 0; i < musicDetialList.size(); i++) {
//									final Movie music =musicDetialList.get(i);
//									if(isWhatLeft==Constant.MUSICCHAPTER){
//									TextView tv =(TextView)(view.findViewById(tvlistItem[i]));
//									tv.setWidth(LayoutParams.WRAP_CONTENT);
//									tv.setVisibility(View.VISIBLE);
//									tv.setText(music.getName());
//									}else{
//										String seq = music.getSeq();
//										if(seq.length()!=0){
//											TextView tv =(TextView)(view.findViewById(tvlistItem[i]));
//											tv.setWidth(LayoutParams.WRAP_CONTENT);
//											view.findViewById(tvlistItem[i])
//													.setVisibility(View.VISIBLE);
//											tv.setText(seq);
//											temp =Integer.parseInt(seq);
//										}else{
//											TextView tv =(TextView)(view.findViewById(tvlistItem[i]));
//											tv.setWidth(LayoutParams.WRAP_CONTENT);
//											view.findViewById(tvlistItem[i])
//													.setVisibility(View.VISIBLE);
//											temp =temp+1;
//											tv.setText(temp+"");
//										}
//									}
//									view.findViewById(tvlistItem[i])
//											.setOnClickListener(
//													new OnClickListener() {
//														@Override
//														public void onClick(
//																View v) {
//																setMVPilot(music);
//														}
//													});
//								}
//							} catch (JSONException e) {
//								e.printStackTrace();
//							}
//						} else {
//							Dialog.dismiss();
//							Toast.makeText(aQuery.getContext(),
//									"Error:" + status.getCode(),
//									Toast.LENGTH_LONG).show();
//						}
//					}
//				});
//		String musicssPath ="";
//		if(bo==isFilm){
//			musicssPath= HttpRequest.URL_QUERY_SINGLE_MOVIE + musicId;
//		}else if(bo==isTv){
//			musicssPath=HttpRequest.URL_QUERY_SINGLE_TV+musicId;
//		}
//		aQuery.ajax(musicssPath, String.class, new AjaxCallback<String>() {
//			@Override
//			public void callback(String url, String json, AjaxStatus status) {
//				if (json != null) {
//					System.out.println("下载的数据" + "=======" + json);
//					final String image_path_boot;
//					try {
//						final ArrayList<String> pathList = new ArrayList<String>();
//						ArrayList<String> nameList = new ArrayList<String>();
//						final ArrayList<PostMent> postMentList = new ArrayList<PostMent>();
//						JSONObject jb = new JSONObject(json);
//						image_path_boot = jb.getString("PIC");
//						String name = jb.getString("PNAME");
//						String note = jb.getString("PNOTE");
//						String director = "";
//						String kind = "";
//						String rose = "";
//						String pubdate = "";
//						if (!jb.isNull("DIRECTOR")) {
//							director = jb.getString("DIRECTOR");
//						}
//						if (!jb.isNull("KIND")) {
//							kind = jb.getString("KIND");
//						}
//						if (!jb.isNull("ROSE")) {
//							rose = jb.getString("ROSE");
//						}
//						if (!jb.isNull("RELEASE")) {
//						String 	mypubdate = jb.getString("RELEASE");
//						pubdate =mypubdate.substring(0, mypubdate.lastIndexOf(" "));
//						   
//						}
//						JSONArray postOb = jb.getJSONArray("potype");
//						for (int i = 0; i < postOb.length(); i++) {
//
//							PostMent pm = new PostMent();
//							pm.setType(postOb.getJSONObject(i)
//									.getString("TYPE"));
//							pm.setId(postOb.getJSONObject(i).getString("PUBID"));
//							pm.setPrice(postOb.getJSONObject(i).getString(
//									"PRICE"));
//							postMentList.add(pm);
//						}
//						btn_orderall.setOnClickListener(new OnClickListener() {
//							@Override
//							public void onClick(View v) {
//								setOrder(postMentList, orderId);
//							}
//						});
//						builder.show();
//						((TextView) viewFormusicdetail
//								.findViewById(R.id.musicdetail_text))
//								.setText(name);
//						((TextView) viewFormusicdetail
//								.findViewById(R.id.albumname)).setText(name);
//						((TextView) viewFormusicdetail
//								.findViewById(R.id.albuminfo)).setText(note);
//						((TextView) viewFormusicdetail
//								.findViewById(R.id.artist)).setText(aQuery
//								.getContext().getResources()
//								.getString(R.string.director)
//								+ " : " + director);
//						((TextView) viewFormusicdetail
//								.findViewById(R.id.Issuedate)).setText(aQuery
//								.getContext().getResources()
//								.getString(R.string.pubdate)
//								+ " ： " + pubdate);
//						((TextView) viewFormusicdetail
//								.findViewById(R.id.language)).setText(aQuery
//								.getContext().getResources()
//								.getString(R.string.rose)
//								+ " ： " + rose);
//						((TextView) viewFormusicdetail
//								.findViewById(R.id.company)).setText(aQuery
//								.getContext().getResources()
//								.getString(R.string.kind)
//								+ " ： " + kind);
//						setMusicrecommend(list, viewFormusicdetail,bo);
//						String path = HttpRequest.URL_QUERY_SINGLE_IMAGE
//								+ image_path_boot;
//						ImageView imageView = (ImageView) viewFormusicdetail
//								.findViewById(R.id.albumimage);
//						ImageDownloader downloader = new ImageDownloader(aQuery
//								.getContext());
//						downloader.download(path, imageView);
//					} catch (JSONException e) {
//						e.printStackTrace();
//					}
//					Dialog.dismiss();
//				} else {
//					Dialog.dismiss();
//					Toast.makeText(aQuery.getContext(),
//							"Error:" + status.getCode(), Toast.LENGTH_LONG)
//							.show();
//				}
//			}
//		});
//	
//	}
//	
//	private void setParams(LayoutParams lay) {
//		  DisplayMetrics dm = new DisplayMetrics();
//		  getWindowManager().getDefaultDisplay().getMetrics(dm);
//		  Rect rect = new Rect();
//		  View view = getWindow().getDecorView();
//		  view.getWindowVisibleDisplayFrame(rect);
//		  lay.height = dm.heightPixels - rect.top;
//		  lay.width = dm.widthPixels;
//		 }
//	  public  void DiaglogDimiss(Dialog dialog){
//		  dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
//				@Override
//				public boolean onKey(DialogInterface dialog, int keyCode,
//						KeyEvent event) {
//					 if (keyCode == KeyEvent.KEYCODE_BACK){
//					          // Perform action on key press
//						 dialog.dismiss();
//					          return true;
//					        }
//					return false;
//				}
//			});
//	  }
//	  public  void ProDiaglogDimiss(ProgressDialog dialog){
//		  dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
//			  @Override
//			  public boolean onKey(DialogInterface dialog, int keyCode,
//					  KeyEvent event) {
//				  if (keyCode == KeyEvent.KEYCODE_BACK){
//					  // Perform action on key press
//					  dialog.dismiss();
//					  return true;
//				  }
//				  return false;
//			  }
//		  });
//	  }
//}
