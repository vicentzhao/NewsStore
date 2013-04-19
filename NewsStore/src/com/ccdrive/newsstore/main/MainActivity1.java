package com.ccdrive.newsstore.main;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.sax.StartElementListener;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.ccdrive.newsstore.R;
import com.ccdrive.newsstore.bean.News;
import com.ccdrive.newsstore.bean.OrderBean;
import com.ccdrive.newsstore.bean.PagenationBean;
import com.ccdrive.newsstore.bean.Paper;
import com.ccdrive.newsstore.bean.PayOrderBean;
import com.ccdrive.newsstore.bean.PostMent;
import com.ccdrive.newsstore.bean.SoftwareBean;
import com.ccdrive.newsstore.bean.VersionInfo;
import com.ccdrive.newsstore.content.CommUtil;
import com.ccdrive.newsstore.content.Constant;
import com.ccdrive.newsstore.http.HttpRequest;
import com.ccdrive.newsstore.http.HttpRequest.OnHttpResponseListener;
import com.ccdrive.newsstore.http.ImageDownloader;
import com.ccdrive.newsstore.page.OrderPage;
import com.ccdrive.newsstore.play.PlayerActivity;
import com.ccdrive.newsstore.play.StreamingMediaPlayer;
import com.ccdrive.newsstore.play.VitamioPlayer;
import com.ccdrive.newsstore.util.AppUtil;
import com.ccdrive.newsstore.util.JsonUtil;
import com.ccdrive.newsstore.util.UpdateVersion;
import com.ccdrive.newsstore.util.XmlParse;

public class MainActivity1 extends FragmentActivity implements
		LeftSelectedListener, RightSelectedListener, OnClickListener {

	private static int left_type = Constant.FLFG;
	// private static RightSecond rSecond;
	private static String TAG = "MusicStore";
	public static String DETAIL_ID = null;
	private static RightFragment rFragment;
	private static RelativeLayout leftlayout;
	private static HttpRequest http;
	public static ArrayList<SoftwareBean> softlist;
	public static ArrayList<OrderBean> softOrderlist;// 当前可订购列表
	public static ArrayList<PayOrderBean> payOrderlist;// 订单
	private static String right_grid_id = null;
	public static String FILE_PATH = null;
	public static View mainView;
	public static View itemView;
	public static Button myMusic, store, serch;
	public static ArrayList<HashMap<String, String>> musicTestArrayList;
	private static AQuery aQuery;
	private static SharedPreferences.Editor editor;
	public static boolean isFragment = true;
	private boolean isfirst = true;
	private static MediaPlayer mp;
	static ArrayList<Paper> musicList;
	static ArrayList<SoftwareBean> musicAppList;
	static Dialog builder;
	private static View viewForsoftDetail;
	private static View viewFormusicdetail;
	private static boolean isFristInit;
	private static boolean isplay = false;
	private static boolean needResume;
	boolean isUpdate = false;;
	public static String appDownPath;
	private static ArrayList<SoftwareBean> musicDetailList;
	//判断左右
	private static int isWhatLeft = 100013;
	private static int isWhatRight = 100011;
	static LayoutInflater inflater;
	static ArrayList<News> music_chapterList;
	static ArrayList<News> mvlist;
	private static musicTryplayAsyncTask musicTryTask;

	static int whatisplay = 1; // 判断哪个音乐播放。用来暂停或者
	static StreamingMediaPlayer audioStreamer; // 播放器
	static int isFilm = 000011; // 判断是否为music 还是mv
	static int isTv = 000012;

	static String currentPath; // 搜索判断

	static String whatTrueOrder =""; //订阅的内容
	
	//设备的宽高
	private static int DeviceWidth ;
	private static int DeviceHeight;
	
	
	static ProgressDialog pd;
	
	private static int pageCount = 1;

	private static boolean isSearch = false;// 判断是否为search

	// 分页
	private static PagenationBean page = new PagenationBean();
	private EditText searchContentEdit;
	
	
	//
	
	private static Button classify, the_news, recommend, movie, teleplay,
			anime, softInstallButton, music, record, soft;
	SharedPreferences sp;
	
	
	private static int[] horItems = { R.id.item_hor_01, R.id.item_hor_02,
			R.id.item_hor_03, R.id.item_hor_04, R.id.item_hor_05,
			R.id.item_hor_06, R.id.item_hor_07, R.id.item_hor_08,
			R.id.item_hor_09, R.id.item_hor_10, R.id.item_hor_11,
			R.id.item_hor_12, R.id.item_hor_13, R.id.item_hor_14,
			R.id.item_hor_15 };

	
	private static int[] musiclistItem = { R.id.music_item_hor_01,
			R.id.music_item_hor_02, R.id.music_item_hor_03,
			R.id.music_item_hor_04, R.id.music_item_hor_05,
			R.id.music_item_hor_06, R.id.music_item_hor_07,
			R.id.music_item_hor_08, R.id.music_item_hor_09,
			R.id.music_item_hor_10 };
	
	//tv item
	private static int[] tvlistItem = { R.id.i_text_episode_text1,
		R.id.i_text_episode_text2, R.id.i_text_episode_text3,
		R.id.i_text_episode_text4, R.id.i_text_episode_text5,
		R.id.i_text_episode_text6, R.id.i_text_episode_text7,
		R.id.i_text_episode_text8, R.id.i_text_episode_text9,
		R.id.i_text_episode_text10 };
	// private static int[] recommendItem =
	// {R.id.music1,R.id.music2,R.id.music3,
	// R.id.music4,R.id.music5,R.id.music6,
	// R.id.music7,R.id.music8,R.id.music9,
	// R.id.music10};
	private static int[] orderRadioItem = { R.id.rad1, R.id.rad2, R.id.rad3,
			R.id.rad4 };

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		setContentView(R.layout.activity_main_1920);
		FILE_PATH = getPackageName();
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
				.detectDiskReads().detectDiskWrites().detectNetwork()
				.penaltyLog().build());
		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
				.detectLeakedSqlLiteObjects().penaltyLog().penaltyDeath()
				.build());
		sp = getPreferences(MODE_PRIVATE);
		aQuery = new AQuery(MainActivity1.this);
		initView();
		setParams();
		editor = sp.edit();
		checkVersion();
	}

	public void initView() {
		myMusic = (Button) findViewById(R.id.myMusic);
		store = (Button) findViewById(R.id.store);
		serch = (Button) findViewById(R.id.search_button);
		searchContentEdit = (EditText) findViewById(R.id.search_input);
		myMusic.setOnClickListener(this);
		store.setOnClickListener(this);
		serch.setOnClickListener(this);
		inflater = LayoutInflater.from(this);
		builder = new Dialog(MainActivity1.this);
		DiaglogDimiss(builder);
		builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
		int h;
		View softDetail = inflater.inflate(R.layout.soft_detail, null);
		softInstallButton = (Button) softDetail.findViewById(R.id.install);
		softInstallButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(MainActivity1.this, "开始下载", 1).show();
				System.out.println("我已经开始下载");
			}
		});
		myMusic.setText(getResources().getString(R.string.my_music));
		myMusic.setSelected(true);
		myMusic.setTextColor(Color.YELLOW);
		store.setText(getResources().getString(R.string.music_store));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.myMusic:
			isSearch = false;
			myMusic.setSelected(true);
			store.setTextColor(Color.WHITE);
			myMusic.setTextColor(Color.YELLOW);
			store.setSelected(false);
			isWhatRight = Constant.MYMUSIC;
			if (isWhatLeft == Constant.MUSICAPP) {
				setAppStoreList(Constant.MYMUSIC_APP + pageCount);
			} else if (isWhatLeft == Constant.MUSICCHAPTER) {
				setMusicChapterList(Constant.MYMUSIC_CHAPTER + pageCount);
			} else if (isWhatLeft == Constant.MUSICMV) {
				setTVList(Constant.MYMUSIC_MV + pageCount);
			}
			break;
		case R.id.store:
			isSearch = false;
			myMusic.setSelected(true);
			store.setTextColor(Color.YELLOW);
			myMusic.setTextColor(Color.WHITE);
			store.setSelected(false);
			isWhatRight = Constant.MUSICSTORE;
			if (isWhatLeft == Constant.MUSICAPP) {
				setAppStoreList(Constant.MUSICSTORE_APP);
			} else if (isWhatLeft == Constant.MUSICCHAPTER) {
				setMusicChapterList(Constant.MUSICSTORE_CHAPTER);
			} else if (isWhatLeft == Constant.MUSICMV) {
				setTVList(Constant.MUSICSTORE_MV);
			}
			myMusic.setSelected(false);
			store.setSelected(true);
			break;
		case R.id.search_button:
			isSearch = true;
			String searchContent = searchContentEdit.getText().toString()
					.trim();
			if ("".equals(searchContent)) {
				Toast.makeText(
						aQuery.getContext(),
						aQuery.getContext().getResources()
								.getString(R.string.searchkey), 1).show();
			} else {
				pageCount = 1;
				String friString = URLEncoder.encode(searchContent);
				String currPath = currentPath + "&pname="
						+ URLEncoder.encode(friString) + "&currentPage="
						+ pageCount;
				System.out.println("currpath搜索的地址为===========" + currPath);
				if (isWhatLeft == Constant.MUSICAPP) {
					setAppStoreList(currPath);
				} else if (isWhatLeft == Constant.MUSICCHAPTER) {
					setMusicChapterList(currPath);
				} else if (isWhatLeft == Constant.MUSICMV) {
					setTVList(currPath);
				}
			}
			break;
		default:
			break;
		}
	}

	// 左侧栏碎片
	public  static class LeftFragment extends Fragment implements
			OnClickListener {
		OnLeftSelectedListener oLeftSelectedListener;
		View view;
		FragmentTransaction fragmentTransaction;

		// ！！！可恢复状态时用
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setHasOptionsMenu(true);
		}

		public interface OnLeftSelectedListener {
			public void onLeftSelected(int left_type);
		}

		@Override
		public void onAttach(Activity activity) {
			super.onAttach(activity);
			try {
				oLeftSelectedListener = (OnLeftSelectedListener) activity;
			} catch (Exception e) {
				// 抛一个自己定义 的异常
				throw new ClassCastException(activity.toString()
						+ "must implement OnLeftSelectedListener");
			}
		}

		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			super.onActivityCreated(savedInstanceState);
			// 用getActivity找到对应控件
			classify = (Button) getActivity().findViewById(R.id.flfg);
			the_news = (Button) getActivity().findViewById(R.id.al);
			recommend = (Button) getActivity().findViewById(R.id.flwsys);
			movie = (Button) getActivity().findViewById(R.id.movie);
			teleplay = (Button) getActivity().findViewById(R.id.teleplay);
			anime = (Button) getActivity().findViewById(R.id.anime);
			record = (Button) getActivity().findViewById(R.id.record);
			soft = (Button) getActivity().findViewById(R.id.soft);
			classify.setOnClickListener(this);
			the_news.setOnClickListener(this);
			recommend.setOnClickListener(this);
//			movie.setOnClickListener(this);
			teleplay.setOnClickListener(this);
			anime.setOnClickListener(this);
			// music.setOnClickListener(this);
			record.setOnClickListener(this);
			soft.setOnClickListener(this);
			classify.setSelected(true);
		}

		// 将fragment加入activity
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			FragmentManager fragmentManager = getFragmentManager();
			if (fragmentTransaction == null) {
				fragmentTransaction = fragmentManager.beginTransaction();
			}
			if (rFragment == null ) {
				rFragment = new RightFragment();
				fragmentTransaction.add(R.id.right, rFragment);
			} else {
				fragmentManager.popBackStack();
				fragmentTransaction.replace(R.id.right, rFragment);
			}
			fragmentTransaction.commit();
			if (container == null) {
				// return null;
			}
			leftlayout = new RelativeLayout(getActivity());
			view = inflater.inflate(R.layout.fragment_left_title1920,
					container, false);
			leftlayout.addView(view);
			return leftlayout;
		}

		// !!!!保存状态
		@Override
		public void onPause() {
			super.onPause();
		}

		@Override
		public void onClick(View v) {
			if (v == classify) {
				classify.setSelected(true);
				the_news.setSelected(false);
				recommend.setSelected(false);
				movie.setSelected(false);
				teleplay.setSelected(false);
				anime.setSelected(false);
				// music.setSelected(false);
				record.setSelected(false);
				soft.setSelected(false);
				left_type = Constant.FLFG;
			} else if (v == the_news) {
				classify.setSelected(false);
				the_news.setSelected(true);
				recommend.setSelected(false);
				movie.setSelected(false);
				teleplay.setSelected(false);
				anime.setSelected(false);
				// music.setSelected(false);
				record.setSelected(false);
				soft.setSelected(false);
				left_type = Constant.AL;
			} else if (v == recommend) {
				classify.setSelected(false);
				the_news.setSelected(false);
				recommend.setSelected(true);
				movie.setSelected(false);
				teleplay.setSelected(false);
				anime.setSelected(false);
				// music.setSelected(false);
				record.setSelected(false);
				soft.setSelected(false);
				left_type = Constant.FLWSYS;
			}else if (v == movie) {
			classify.setSelected(false);
			the_news.setSelected(false);
			recommend.setSelected(false);
			movie.setSelected(true);
			teleplay.setSelected(false);
			anime.setSelected(false);
			// music.setSelected(false);
			record.setSelected(false);
			soft.setSelected(false);
			left_type = Constant.MOVIE;
		}
			oLeftSelectedListener.onLeftSelected(left_type);
		}

	}

	/**
	 * 右侧栏碎片
	 * 
	 * @author sl
	 * 
	 */
	public static class RightFragment extends Fragment implements
			OnItemClickListener, OnHttpResponseListener {
		private CommUtil commUtil;
		ArrayList<String> a = new ArrayList<String>();
		OnRightSelectedListener oRightSelectedListener;
		private MediaPlayer mMediaPlayer;

		public interface OnRightSelectedListener {
			public void oRightSelected(int left_type);
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			http = new HttpRequest();
			http.setHttpResponseListener(this);
			RelativeLayout layout = new RelativeLayout(getActivity());
			itemView = inflater.inflate(R.layout.design_sketch_hor, container,
					false);
			initHorizontalView();
			layout.addView(itemView);
			setDefalutView();
			return layout;
		}

		public void update() {

		}


		public void updateRightFragmentBaseLeft(int left_type) {
			myMusic.setSelected(true);
			store.setSelected(false);
			HashMap<String, String> hashMap;
			if (left_type == Constant.FLFG) {
				isWhatLeft = Constant.MUSICAPP;
				if (isWhatRight == Constant.MYMUSIC) {
					setAppStoreList(Constant.MYMUSIC_APP + pageCount);
				} else if (isWhatRight == Constant.MUSICSTORE) {
					setAppStoreList(Constant.MUSICSTORE_APP + pageCount);
				}
			} else if (left_type == Constant.AL) {

				isWhatLeft = Constant.MUSICCHAPTER;
				if (isWhatRight == Constant.MYMUSIC) {
					setMusicChapterList(Constant.MYMUSIC_CHAPTER + pageCount);
				} else if (isWhatRight == Constant.MUSICSTORE) {
					setMusicChapterList(Constant.MUSICSTORE_CHAPTER);
//					setMusicChapterList(Constant.MUSICSTORE_CHAPTER + pageCount);
				}
			} else if (left_type == Constant.FLWSYS) {

				isWhatLeft = Constant.MUSICMV;
				if (isWhatRight == Constant.MYMUSIC) {
					setTVList(Constant.MYMUSIC_MV+ pageCount);
				} else if (isWhatRight == Constant.MUSICSTORE) {
					setTVList(Constant.MUSICSTORE_MV+ pageCount);
				}
			}else if(left_type==Constant.MOVIE){
				OrderPage  order = new OrderPage(aQuery.getContext(),DeviceHeight,DeviceWidth);
				String orderPathBegin =HttpRequest.URL_QUERY_LIST_ORDER+"1";
				order.setOrderPage(orderPathBegin);
			}
		}

		@Override
		public void response(int responseCode, int what, String value,
				Object object) {
		}

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			
		}

	}

	// 右边第二级碎片

	// 实现activity基于left传值更新rightFragment
	@Override
	public void onLeftSelected(int left_type) {
		if (rFragment == null) {
			rFragment = (RightFragment)
			// getSupportFragmentManager().findFragmentById(R.id.right);
			getSupportFragmentManager().findFragmentByTag("rightF");
		}
		FragmentManager fragmentManager = rFragment.getFragmentManager();
		fragmentManager.popBackStack();
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		fragmentTransaction.replace(R.id.right, rFragment);
		fragmentTransaction.addToBackStack(null);
		fragmentTransaction.commit();
		if (rFragment != null) {
			rFragment.updateRightFragmentBaseLeft(left_type);
		}
	}

	@Override
	public void oRightSelected(int left_type) {
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (isfirst) {
			if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
				myMusic.setFocusable(true);
				myMusic.requestFocus();
				myMusic.setSelected(true);
			}
			if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
				myMusic.setFocusable(true);
				myMusic.requestFocus();
				myMusic.setSelected(true);

			}
			if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
				myMusic.setFocusable(true);
				myMusic.requestFocus();
				myMusic.setSelected(true);
			}
			if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
				myMusic.setFocusable(true);
				myMusic.requestFocus();
				myMusic.setSelected(true);
			}
			isfirst = false;
		}

		if (!recommend.isFocused() & !classify.isFocused()
				& !the_news.isFocused()) {
			isFragment = false;
		}
		if (recommend.isFocused() || classify.isFocused()
				|| the_news.isFocused()) {
			isFragment = true;
		}
		if (classify.isFocused()) {
			editor.clear();
			editor.putString("from", "one");
			editor.commit();
		}
		if (the_news.isFocused()) {
			editor.clear();
			editor.putString("from", "two");
			editor.commit();
		}
		if (recommend.isFocused()) {
			SharedPreferences.Editor edit = sp.edit();
			editor.putString("from", "three");
			editor.commit();
		}
		if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
			if (itemView.findViewById(R.id.item_hor_01).isFocused()
					|| itemView.findViewById(R.id.item_hor_06).isFocused()
					|| itemView.findViewById(R.id.item_hor_11).isFocused()) {
				String focuse = sp.getString("from", "none");
				if (focuse.equals("one")) {
					classify.setFocusable(true);
					classify.requestFocus();
				}
				if (focuse.equals("three")) {
					recommend.setFocusable(true);
					recommend.requestFocus();
				}
				if (focuse.equals("two")) {
					the_news.setFocusable(true);
					the_news.requestFocus();
				}
			}
		}
		 if(keyCode == KeyEvent.KEYCODE_BACK){
			 Dialog dialog = new AlertDialog.Builder(aQuery.getContext())
				.setTitle(getResources().getString(R.string.exit))
				.setMessage(
						getResources().getString(R.string.exitContent)
								)
				.setPositiveButton(getResources().getString(R.string.confirm),
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
							 
								 MainActivity1.this.finish();
								
							}
					
				})
				.setNegativeButton(getResources().getString(R.string.cancle),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								dialog.dismiss();
							}
						}).create();
			 dialog.show();
			 }
		return super.onKeyDown(keyCode, event);
	}

	private static void initHorizontalView() {
		// initInfoView();
		itemView.findViewById(R.id.item_hor_05).setNextFocusRightId(
				R.id.item_hor_06);
		itemView.findViewById(R.id.item_hor_10).setNextFocusRightId(
				R.id.page_pre);
		itemView.findViewById(R.id.item_hor_11).setNextFocusDownId(
				R.id.page_pre);
		itemView.findViewById(R.id.item_hor_12).setNextFocusDownId(
				R.id.page_pre);
		itemView.findViewById(R.id.item_hor_13).setNextFocusDownId(
				R.id.item_hor_13);
		itemView.findViewById(R.id.item_hor_14).setNextFocusDownId(
				R.id.page_next);
		itemView.findViewById(R.id.item_hor_15).setNextFocusDownId(
				R.id. page_next);

	}

	// public void initItems(int[] itemIds) {
	// // AQuery aq = new AQuery(contentView);
	// for(int i=0;i<itemIds.length;i++){
	// aq.find(itemIds[i]).clicked(new OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	// MovieBrief movie = (MovieBrief) v.getTag();
	// Intent i = new Intent(context,ItemDetailPage.class);
	// i.putExtra("id",movie.getId());
	// context.startActivity(i);
	// }
	// }).focusChanged(new OnFocusChangeListener() {
	//
	// @Override
	// public void onFocusChange(View v, boolean hasFocus) {
	// if(hasFocus){
	// focusViewId = v.getId();
	// MovieBrief movie = (MovieBrief) v.getTag();
	// updateInfomation(movie);
	// }
	// }
	// }).invisible();
	// }
	// aq.find(itemIds[0]).keyed(new OnKeyListener() {
	//
	// @Override
	// public boolean onKey(View v, int keyCode, KeyEvent event) {
	// if( event.getAction() == KeyEvent.ACTION_DOWN){
	// if(keyCode == KeyEvent.KEYCODE_DPAD_LEFT){
	// if(!loading)
	// prePage();
	// return true;
	// }
	// }
	// return false;
	// }
	// });
	// aq.find(itemIds[itemIds.length-1]).keyed(new OnKeyListener() {
	//
	// @Override
	// public boolean onKey(View v, int keyCode, KeyEvent event) {
	// if( event.getAction() == KeyEvent.ACTION_DOWN){
	//
	// if(keyCode == KeyEvent.KEYCODE_DPAD_RIGHT){
	// if(!loading)
	// nextPage();
	// return true;
	// }
	// }
	// return false;
	// }
	// });
	// }
	public static void initDialog(String s) {

		View view = null;
		if ("three".equals(s)) {
			view = inflater.inflate(R.layout.music_detail1, null);
		}
		if ("one".equals(s)) {
			view = inflater.inflate(R.layout.soft_detail, null);
		}
		if ("three".equals(s)) {
			view = inflater.inflate(R.layout.music_detail1, null);
		}
		builder.setContentView(view);
		Window dialogWindow = builder.getWindow();
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		dialogWindow.setGravity(Gravity.CENTER);
		lp.width = DeviceWidth;
		lp.height = DeviceHeight;
		dialogWindow.setAttributes(lp);
	}

	/**
	 * setAllMusic 音乐
	 * @param list
	 */
	public static void setMusicMvInfo(ArrayList<News> list, String path) {
		for (int i = 0; i < horItems.length; i++) {
			itemView.findViewById(horItems[14 - i]).setVisibility(View.VISIBLE);
		}
		if (list.size() < 15) {
			int j = 15 - list.size();
			for (int i = 0; i < j; i++) {
				itemView.findViewById(horItems[14 - i]).setVisibility(
						View.INVISIBLE);
			}
		}

		UpdatePaging(path, "mv");
		for (int i = 0; i < list.size(); i++) {
			aQuery.find(horItems[i]).find(R.id.ItemTitle)
					.text(list.get(i).getName());
			String url = list.get(i).getImage_path();
			String URL_QUERY_SINGLE_IMAGE = HttpRequest.WEB_ROOT
					+ "download.action?token=myadmin&inputPath=";
			String uslPath = URL_QUERY_SINGLE_IMAGE + url;
			aQuery.find(horItems[i]).find(R.id.ItemIcon).image(uslPath);
		}
	}

	public static void setMusicChapterInfo(ArrayList<News> list, String path) {
		for (int i = 0; i < horItems.length; i++) {
			itemView.findViewById(horItems[14 - i]).setVisibility(View.VISIBLE);
		}
		if (list.size() < 15) {
			int j = 15 - list.size();
			for (int i = 0; i < j; i++) {
				itemView.findViewById(horItems[14 - i]).setVisibility(
						View.INVISIBLE);
			}
		}

		UpdatePaging(path, "musicChapter");
		for (int i = 0; i < list.size(); i++) {
			aQuery.find(horItems[i]).find(R.id.ItemTitle)
					.text(list.get(i).getName());
			String url = list.get(i).getImage_path();
			String URL_QUERY_SINGLE_IMAGE = HttpRequest.WEB_ROOT
					+ "download.action?token=myadmin&inputPath=";
			String uslPath = URL_QUERY_SINGLE_IMAGE + url;
			aQuery.find(horItems[i]).find(R.id.ItemIcon).image(uslPath);
		}
	}

	// Set softrecommend 软件推荐
	

	// 音乐推荐
	private static void setMusicrecommend(ArrayList<News> list, final View view,final int iswhat) {
		final ArrayList<String> myPathlist = new ArrayList<String>();
		ImageDownloader Downloader = new ImageDownloader(aQuery.getContext());
		for (int i = 0; i < 5; i++) {
			view.findViewById(horItems[i]).setVisibility(View.VISIBLE);
		}
		int j = 0;
		if (list.size() < 5) {
			for (int s = 0; s < 5 - list.size(); s++) {
				view.findViewById(horItems[4 - s])
						.setVisibility(View.INVISIBLE);
			}
		}
		for (int i = 0; i < ((list.size() <= 5) ? list.size() : 5); i++) {
			final News sb = list.get(i);
			String image_path = sb.getImage_path();
			final String title = sb.getName();
			final String turePath = HttpRequest.URL_QUERY_SINGLE_IMAGE
					+ image_path;
			Downloader.download(
					turePath,
					((ImageView) view.findViewById(horItems[i]).findViewById(
							R.id.ItemIcon)));
			((TextView) view.findViewById(horItems[i]).findViewById(
					R.id.ItemTitle)).setText(title);
			myPathlist.add(image_path);
			view.findViewById(horItems[i]).setOnClickListener(
					new OnClickListener() {
						@Override
						public void onClick(View v) {
							((TextView) view.findViewById(R.id.albumname))
									.setText(title);
							ImageView imageView = (ImageView) view
									.findViewById(R.id.albumimage);
							ImageDownloader downloader = new ImageDownloader(
									aQuery.getContext());
							downloader.download(turePath, imageView);
							setRecommedMusicInfo(sb.getId(), view, sb,iswhat);
						}
					});
		}
	}

	/**
	 * 试播,订购，下载 set music play
	 * 
	 * @param path
	 */
	// public static void setMusicCross(final String path,final String sid){
	// Log.e(TAG, "下载的路径是"+path);
	// String decryptURL = CryptUtil.decryptURL(path);
	// final String subpath =
	// decryptURL.substring(decryptURL.lastIndexOf(".")+1,
	// decryptURL.length()-1);
	// final View musiccrossView = inflater.inflate(R.layout.musiccross, null);
	// final Dialog dl = new Dialog(aQuery.getContext());
	// dl.requestWindowFeature(Window.FEATURE_NO_TITLE);
	// dl.setContentView(musiccrossView);
	// Window dialogWindow = dl.getWindow();
	// WindowManager.LayoutParams lp = dialogWindow.getAttributes();
	// dialogWindow.setGravity(Gravity.CENTER);
	// lp.width = 400;
	// lp.height =200;
	// dialogWindow.setAttributes(lp);
	// dl.show();
	// //试播
	// ((Button)musiccrossView.findViewById(R.id.btn_shibo)).setOnClickListener(new
	// OnClickListener() {
	// @Override
	// public void onClick(View v) {
	// // TODO Auto-generated method stub
	// if("mp3".equals(subpath)){
	// setMusicPilot(path);
	// }else if("mp4".equals(subpath)){
	// setMVPilot(path);
	// }
	// // setMusicPilot(path);
	// }
	// });
	// //下载
	// ((Button)musiccrossView.findViewById(R.id.btn_musicdown)).setOnClickListener(new
	// OnClickListener() {
	// @Override
	// public void onClick(View v) {
	// // TODO Auto-generated method stub
	// setMusicDown(sid);
	// }
	// });
	// //订阅单曲
	// ((Button)musiccrossView.findViewById(R.id.btn_musciorder)).setOnClickListener(new
	// OnClickListener() {
	// @Override
	// public void onClick(View v) {
	// // TODO Auto-generated method stub
	// //setMusicPilot(path);
	// }
	// });
	//
	// }
	/**
	 * setSoftDetial param 软件详细界面
	 */
//	public static void setSoftDetail(int id, final ArrayList<SoftwareBean> list) {
//		final Handler hdHandler = new Handler();
//		int j = 0;
//
//		for (int i = 0; i < horItems.length; i++) {
//			if (id == horItems[i]) {
//				j = i;
//			}
//		}
//		final String appId = list.get(j).getId();
//		
//		String appPath = HttpRequest.URL_QUERY_SINGLE_SOFT + appId;
//		final ProgressDialog Dialog = ProgressDialog.show(aQuery.getContext(),
//				"缓冲中。。", "正在缓冲请稍后。。");
//	    ProDiaglogDimiss(Dialog);
//		builder.setContentView(viewForsoftDetail);
//		Window dialogWindow = builder.getWindow();
//		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
//		dialogWindow.setGravity(Gravity.CENTER);
//		lp.width = DeviceWidth;
//		lp.height = DeviceHeight;
//		dialogWindow.setAttributes(lp);
//		Dialog.show();
//		String web_url = HttpRequest.URL_QUERY_LIST_SOFT + appId;
//		aQuery.ajax(web_url, String.class, new AjaxCallback<String>() {// 这里的函数是一个内嵌函数如果是函数体比较复杂的话这种方法就不太合适了
//					@Override
//					public void callback(String url, String json,
//							AjaxStatus status) {
//						// 得通过对一个url访问返回的数据存放在JSONObject json中
//						// 可以通过json.getContext()得到
//						if (json != null) {
//							Dialog.dismiss();
//							try {
//								JSONArray ja = new JSONArray(json);
//								for (int i = 0; i < ja.length(); i++) {
//									JSONObject jb = ja.getJSONObject(i);
//									final String appDownPaths = jb
//											.getString("filepath");
//									String version = jb.getString("title");
//									appDownPath = HttpRequest.URL_QUERY_DOWNLOAD_URL
//											+ appDownPaths + "&" + "多米";
//									viewForsoftDetail
//											.findViewById(R.id.install)
//											.setOnClickListener(
//													new OnClickListener() {
//														@Override
//														public void onClick(
//																View v) {
//															// TODO
//															// Auto-generated
//															// method stub
//															System.out
//																	.println("我已经被监听了");
//															String web_url = HttpRequest.URL_QUERY_LIST_SOFT
//																	+ appId;
//															new AsyncTask<Void, Void, Void>() {
//																@Override
//																protected Void doInBackground(
//																		Void... params) {
//																	UpdateVersion updateVersion = UpdateVersion
//																			.instance(
//																					aQuery.getContext(),
//																					hdHandler,
//																					true);
//																	updateVersion
//																			.setUpdateUrl(appDownPath);
//																	updateVersion
//																			.run();
//																	return null;
//																}
//															}.execute();
//														}
//													});
//								}
//							} catch (JSONException e) {
//								// TODO Auto-generated catch block
//								e.printStackTrace();
//							}
//							// successful ajax call, show status code and json
//							// content
//						} else {
//							Dialog.dismiss();
//							Toast.makeText(aQuery.getContext(),
//									"Error:" + status.getCode(),
//									Toast.LENGTH_LONG).show();
//						}
//					}
//				});
//		aQuery.ajax(appPath, String.class, new AjaxCallback<String>() {
//			@Override
//			public void callback(String url, String json, AjaxStatus status) {
//				// 得通过对一个url访问返回的数据存放在JSONObject json中 可以通过json.getContext()得到
//				if (json != null) {
//					System.out.println("下载的数据" + "====" + json);
//					final String image_path_boot;
//					try {
//						SoftwareBean softBean = JsonUtil.getSoftBean(json);
//						AppUtil appUtil = new AppUtil(aQuery.getContext());
//						 boolean install = appUtil.isInstall(softBean.getName());
//						 if(install){
//							 viewForsoftDetail
//								.findViewById(R.id.uninstall).setVisibility(View.VISIBLE);
//							 viewForsoftDetail
//							 .findViewById(R.id.uninstall).setFocusable(false);
//							 viewForsoftDetail
//								.findViewById(R.id.install).setVisibility(View.GONE);
//						 }else{
//							 viewForsoftDetail
//								.findViewById(R.id.uninstall).setVisibility(View.GONE);
//							 viewForsoftDetail
//								.findViewById(R.id.install).setVisibility(View.VISIBLE);
//						 }
//						builder.show();
//						((TextView) viewForsoftDetail
//								.findViewById(R.id.appname)).setText(softBean.getName());
//						((TextView) viewForsoftDetail
//								.findViewById(R.id.title_text)).setText(softBean.getName());
//						((TextView) viewForsoftDetail
//								.findViewById(R.id.appcompany)).setText(aQuery.getContext().getResources().getString(R.string.soft_company)+softBean.getAuthor());
//						((TextView) viewForsoftDetail
//								.findViewById(R.id.Issuedate)).setText(aQuery.getContext().getResources().getString(R.string.soft_addDate)+softBean.getRelease());
//						((TextView) viewForsoftDetail
//								.findViewById(R.id.versions)).setText(aQuery.getContext().getResources().getString(R.string.soft_version)+softBean.getVersion());
//						((TextView) viewForsoftDetail
//								.findViewById(R.id.publishdate)).setText(aQuery.getContext().getResources().getString(R.string.soft_reledate)+softBean.getRelease());
//						((TextView) viewForsoftDetail
//								.findViewById(R.id.platform)).setText(aQuery.getContext().getResources().getString(R.string.soft_evenment)+softBean.getEnvironment());
//						setSoftrecommend(list, viewForsoftDetail);
//						String path = HttpRequest.URL_QUERY_SINGLE_IMAGE
//								+ softBean.getImage_path();
//						ImageView imageView = (ImageView) viewForsoftDetail
//								.findViewById(R.id.appimage);
//						ImageDownloader downloader = new ImageDownloader(aQuery
//								.getContext());
//						downloader.download(path, imageView);
//					} catch (Exception e) {
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
//	}

	static boolean isExit = false;
	static Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			isExit = false;
		}
	};

	/**
	 * setMusicDetial param 音乐详细界面
	 */
//	public static void setMusicDetial(int id, final ArrayList<News> list,
//			final View view, final String orderId, final int bo) {
//		int j = 0;
//		for (int i = 0; i < horItems.length; i++) {
//			if (id == horItems[i]) {
//				j = i;
//			}
//		}
//		final Button btn_orderall = (Button) (viewFormusicdetail
//				.findViewById(R.id.btn_order_allmusic));
//		if(isWhatRight==Constant.MYMUSIC){
//			btn_orderall.setVisibility(View.INVISIBLE);
//		}
//		else{
//			btn_orderall.setVisibility(view.VISIBLE);
//		}
//		builder.setContentView(viewFormusicdetail);
//		Window dialogWindow = builder.getWindow();
//		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
//		dialogWindow.setGravity(Gravity.CENTER);
//		lp.width = DeviceWidth;
//		lp.height = DeviceHeight;
//		dialogWindow.setAttributes(lp);
//		builder.show();
//		for (int i = 0; i < tvlistItem.length; i++) {
//			view.findViewById(tvlistItem[i]).setVisibility(View.GONE);
//			TextView tv =(TextView)(view.findViewById(tvlistItem[i]));
//			tv.setWidth(LayoutParams.WRAP_CONTENT);
//		}
//		 
//			  String name = list.get(j).getName();
//			  String content =list.get(j).getContent();
//			  String image_path_boot =list.get(j).getImage_path();
//			  final String link = list.get(j).getLink();
//			  TextView tv =(TextView)(view.findViewById(tvlistItem[j]));
//				tv.setWidth(LayoutParams.WRAP_CONTENT);
//				tv.setVisibility(View.VISIBLE);
//				tv.setText(name);
//				((TextView) viewFormusicdetail
//						.findViewById(R.id.musicdetail_text))
//						.setText(name);
//				((TextView) viewFormusicdetail
//						.findViewById(R.id.albumname)).setText(name);
//				((TextView) viewFormusicdetail
//						.findViewById(R.id.albuminfo)).setText(content);
//				tv.setOnClickListener(new OnClickListener() {
//					@Override
//					public void onClick(View v) {
//						// TODO Auto-generated method stub
//						if(link!=null){
//						Uri uri = Uri.parse(link);
//						Intent it = new Intent(Intent.ACTION_VIEW,uri);
//						aQuery.getContext().startActivity(it);
//						}
//					}
//				});
//				String path = HttpRequest.URL_QUERY_SINGLE_IMAGE
//				+ image_path_boot;
//		ImageView imageView = (ImageView) viewFormusicdetail
//				.findViewById(R.id.albumimage);
//		ImageDownloader downloader = new ImageDownloader(aQuery
//				.getContext());
//		downloader.download(path, imageView);
//		final String myId =list.get(j).getId();
//		view.findViewById(R.id.btn_order_allmusic).setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				String path = HttpRequest.URL_QUERY_LIST_ORDER_NEWS_SINGLE+myId;
//				final ProgressDialog dialog = ProgressDialog.show(aQuery.getContext(),
//						"缓冲中。。", "正在缓冲请稍后。。");
//				aQuery.ajax(path, String.class, new AjaxCallback<String>(){
//
//					@Override
//					public void callback(String url, String json,
//							AjaxStatus status) {
//						
//						if(json!=null){
//							System.out.println("返回的内容"+json);
//							dialog.dismiss();
//							Toast.makeText(aQuery.getContext(), "订阅成功", 1).show();
//						}
//					}
//					 
//					 
//					
//				});
//					
//				
//			}
//		});
//		
////		if(isWhatLeft==Constant.MUSICCHAPTER){
////		for (int i = 0; i < tvlistItem.length; i++) {
////			view.findViewById(tvlistItem[i]).setVisibility(View.GONE);
////			TextView tv =(TextView)(view.findViewById(tvlistItem[i]));
////			tv.setWidth(LayoutParams.WRAP_CONTENT);
////		}
////		}else{
////			for (int i = 0; i < tvlistItem.length; i++) {
////				view.findViewById(tvlistItem[i]).setVisibility(View.INVISIBLE);
////			}
////		}
////		final ProgressDialog Dialog = ProgressDialog.show(aQuery.getContext(),
////				"缓冲中。。", "正在缓冲请稍后。。");
////		ProDiaglogDimiss(Dialog);
////		String web_url ="";
////		final String musicId = list.get(j).getId();
////		if(bo==isFilm){
////			web_url= HttpRequest.URL_QUERY_LIST_NEWSPAPER + musicId;
////		}else if(bo==isTv){
////			web_url=HttpRequest.URL_QUERY_LIST_NEWSPAPER+musicId;
////		}
////		aQuery.ajax(web_url, String.class, new AjaxCallback<String>() {// 这里的函数是一个内嵌函数如果是函数体比较复杂的话这种方法就不太合适了
////					@Override
////					public void callback(String url, String json,
////							AjaxStatus status) {
////						if (json != null) {
////							ArrayList<Paper> musicDetialList = new ArrayList<Paper>();
////							System.out.println("下载的数据" + "====" + json);
////							Dialog.dismiss();
////							try {
////								JSONArray ja = new JSONArray(json);
////								for (int i = 0; i < ja.length(); i++) {
////									Paper music = new Paper();
////									JSONObject jb = ja.getJSONObject(i);
////									String sid = jb.getString("sid");
////									String musicpath = jb.getString("filepath");
////									String title = jb.getString("title");
////									String seq = null;
////									if(!jb.isNull("seq")){
////									seq =jb.getString("seq");
////									}
////									music.setSeq(seq);
////									music.setDownload_path(musicpath);
////									music.setId(sid);
////									music.setName(title);
////									musicDetialList.add(music);
////									
////								}
////
////								// MusicAdapter myMusicAdapter = new
////								// MusicAdapter(aQuery.getContext(),
////								// musicDetialList,isWhatRight);
////								// listview.setAdapter(myMusicAdapter);
////								int temp = 0;//级数常量
////								for (int i = 0; i < musicDetialList.size(); i++) {
////									final Paper music =musicDetialList.get(i);
////									if(isWhatLeft==Constant.MUSICCHAPTER){
////									TextView tv =(TextView)(view.findViewById(tvlistItem[i]));
////									tv.setWidth(LayoutParams.WRAP_CONTENT);
////									tv.setVisibility(View.VISIBLE);
////									tv.setText(music.getName());
////									}else{
////										String seq = music.getSeq();
////										if(seq.length()!=0){
////											TextView tv =(TextView)(view.findViewById(tvlistItem[i]));
////											tv.setWidth(LayoutParams.WRAP_CONTENT);
////											view.findViewById(tvlistItem[i])
////													.setVisibility(View.VISIBLE);
////											tv.setText(seq);
////											temp =Integer.parseInt(seq);
////										}else{
////											TextView tv =(TextView)(view.findViewById(tvlistItem[i]));
////											tv.setWidth(LayoutParams.WRAP_CONTENT);
////											view.findViewById(tvlistItem[i])
////													.setVisibility(View.VISIBLE);
////											temp =temp+1;
////											tv.setText(temp+"");
////										}
////									}
////									view.findViewById(tvlistItem[i])
////											.setOnClickListener(
////													new OnClickListener() {
////														@Override
////														public void onClick(
////																View v) {
////																setMVPilot(music);
////														}
////													});
////								}
////							} catch (JSONException e) {
////								e.printStackTrace();
////							}
////						} else {
////							Dialog.dismiss();
////							Toast.makeText(aQuery.getContext(),
////									"Error:" + status.getCode(),
////									Toast.LENGTH_LONG).show();
////						}
////					}
////				});
////		String musicssPath ="";
////		if(bo==isFilm){
////			musicssPath= HttpRequest.URL_QUERY_SINGLE_NEWSPAPER + musicId;
////		}else if(bo==isTv){
////			musicssPath=HttpRequest.URL_QUERY_SINGLE_NEWSPAPER+musicId;
////		}
////		aQuery.ajax(musicssPath, String.class, new AjaxCallback<String>() {
////			@Override
////			public void callback(String url, String json, AjaxStatus status) {
////				if (json != null) {
////					System.out.println("下载的数据" + "=======" + json);
////					final String image_path_boot;
////					try {
////						final ArrayList<String> pathList = new ArrayList<String>();
////						ArrayList<String> nameList = new ArrayList<String>();
////						final ArrayList<PostMent> postMentList = new ArrayList<PostMent>();
////						JSONObject jb = new JSONObject(json);
////						image_path_boot = jb.getString("PIC");
////						String name = jb.getString("PNAME");
////						String note = jb.getString("PNOTE");
////						String director = "";
////						String kind = "";
////						String rose = "";
////						String pubdate = "";
////						if (!jb.isNull("DIRECTOR")) {
////							director = jb.getString("DIRECTOR");
////						}
////						if (!jb.isNull("KIND")) {
////							kind = jb.getString("KIND");
////						}
////						if (!jb.isNull("ROSE")) {
////							rose = jb.getString("ROSE");
////						}
////						if (!jb.isNull("RELEASE")) {
////						String 	mypubdate = jb.getString("RELEASE");
////						pubdate =mypubdate.substring(0, mypubdate.lastIndexOf(" "));
////						   
////						}
////						JSONArray postOb = jb.getJSONArray("potype");
////						for (int i = 0; i < postOb.length(); i++) {
////
////							PostMent pm = new PostMent();
////							pm.setType(postOb.getJSONObject(i)
////									.getString("TYPE"));
////							pm.setId(postOb.getJSONObject(i).getString("PUBID"));
////							pm.setPrice(postOb.getJSONObject(i).getString(
////									"PRICE"));
////							postMentList.add(pm);
////						}
////						btn_orderall.setOnClickListener(new OnClickListener() {
////							@Override
////							public void onClick(View v) {
////								setOrder(postMentList, orderId);
////							}
////						});
////						builder.show();
////						((TextView) viewFormusicdetail
////								.findViewById(R.id.musicdetail_text))
////								.setText(name);
////						((TextView) viewFormusicdetail
////								.findViewById(R.id.albumname)).setText(name);
////						((TextView) viewFormusicdetail
////								.findViewById(R.id.albuminfo)).setText(note);
////						((TextView) viewFormusicdetail
////								.findViewById(R.id.artist)).setText(aQuery
////								.getContext().getResources()
////								.getString(R.string.director)
////								+ " : " + director);
////						((TextView) viewFormusicdetail
////								.findViewById(R.id.Issuedate)).setText(aQuery
////								.getContext().getResources()
////								.getString(R.string.pubdate)
////								+ " ： " + pubdate);
////						((TextView) viewFormusicdetail
////								.findViewById(R.id.language)).setText(aQuery
////								.getContext().getResources()
////								.getString(R.string.rose)
////								+ " ： " + rose);
////						((TextView) viewFormusicdetail
////								.findViewById(R.id.company)).setText(aQuery
////								.getContext().getResources()
////								.getString(R.string.kind)
////								+ " ： " + kind);
////						setMusicrecommend(list, viewFormusicdetail,bo);
////						String path = HttpRequest.URL_QUERY_SINGLE_IMAGE
////								+ image_path_boot;
////						ImageView imageView = (ImageView) viewFormusicdetail
////								.findViewById(R.id.albumimage);
////						ImageDownloader downloader = new ImageDownloader(aQuery
////								.getContext());
////						downloader.download(path, imageView);
////					} catch (JSONException e) {
////						e.printStackTrace();
////					}
////					Dialog.dismiss();
////				} else {
////					Dialog.dismiss();
////					Toast.makeText(aQuery.getContext(),
////							"Error:" + status.getCode(), Toast.LENGTH_LONG)
////							.show();
////				}
////			}
////		});
//	}
	/**
	 * setAllSoftinfo
	 * 
	 * @param softList
	 *            设定软件列表信息
	 */
	public static void setSoftInfo(ArrayList<SoftwareBean> list,
			final String path) {
		for (int i = 0; i < horItems.length; i++) {
			itemView.findViewById(horItems[14 - i]).setVisibility(View.VISIBLE);
		}
		if (list.size() < 15) {
			int j = 15 - list.size();
			for (int i = 0; i < j; i++) {
				itemView.findViewById(horItems[14 - i]).setVisibility(
						View.INVISIBLE);
			}
		}
		UpdatePaging(path, "soft");
		for (int i = 0; i < list.size(); i++) {
			aQuery.find(horItems[i]).find(R.id.ItemTitle)
					.text(list.get(i).getName());
			String url = list.get(i).getImage_path();
			String URL_QUERY_SINGLE_IMAGE = HttpRequest.WEB_ROOT
					+ "download.action?token=myadmin&inputPath=";
			String uslPath = URL_QUERY_SINGLE_IMAGE + url;
			aQuery.find(horItems[i]).find(R.id.ItemIcon).image(uslPath);
		}
	}

	// 音乐app
	public static void setAppStoreList(final String path) {
		if (!isSearch) {
			currentPath = path.substring(0, path.lastIndexOf("&"));
		}
		viewForsoftDetail = inflater.inflate(R.layout.soft_detail, null);
		final ProgressDialog Dialog = ProgressDialog.show(aQuery.getContext(),
				"loading。。", "please wait a moment。。");
		ProDiaglogDimiss(Dialog);
		aQuery.ajax(path, String.class, new AjaxCallback<String>() {// 这里的函数是一个内嵌函数如果是函数体比较复杂的话这种方法就不太合适了
					@Override
					public void callback(String url, String json,
							AjaxStatus status) {
						if (json != null) {
							JSONObject jsObject;
							try {
								jsObject = new JSONObject(json);

								JSONObject pageObject = jsObject
										.getJSONObject("page");
								String currentPage = pageObject
										.getString("currentPage");
								String pageSize = pageObject
										.getString("pageSize");
								int totalRows = pageObject.getInt("totalRows");
								page.init(currentPage, pageSize, totalRows);
								TextView tv = (TextView) (itemView.findViewById(R.id.field_page_index));
								tv.setText("第" + page.getCurrentPage() + "页"+"/"+"总"+page.getTotalPage()+"页");
								musicAppList = new ArrayList<SoftwareBean>();
								musicAppList = JsonUtil.getSoftBeanList(json);
								if (musicAppList.size() != 0) {
									setSoftInfo(musicAppList, path);
									for (int i = 0; i < horItems.length; i++) {
										final int k =i;
										final SoftwareBean softwareBean = musicAppList.get(i);
										itemView.findViewById(horItems[i]).setOnClickListener(
												new OnClickListener() {
													@Override
													public void onClick(View v) {
//														System.out.println("item被点击了");
//														setSoftDetail(v.getId(), musicAppList);
														Intent t = new Intent(aQuery.getContext(),AppDetailActivity.class);
														 t.putExtra("itemid", k);
														 t.putExtra("app", softwareBean);
														  aQuery.getContext().startActivity(t);
													}
												});
									}
									
								} else {
									Toast.makeText(
											aQuery.getContext(),
											aQuery.getContext()
													.getResources()
													.getString(
															R.string.nocontent),
											Toast.LENGTH_LONG).show();
									for (int i = 0; i < horItems.length; i++) {
										itemView.findViewById(horItems[i]).setVisibility(View.INVISIBLE);
									}
									
								}
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							Dialog.dismiss();
							// successful ajax call, show status code and json
							// content
						} else {
							Dialog.dismiss();
							if (status.getCode() == -101) {
								Toast.makeText(
										aQuery.getContext(),
										"Error:"
												+ status.getCode()
												+ aQuery.getContext()
														.getResources()
														.getString(
																R.string.checknetwork),
										Toast.LENGTH_LONG).show();
							} else {
								Toast.makeText(
										aQuery.getContext(),
										aQuery.getContext().getResources()
												.getString(R.string.nocontent),
										Toast.LENGTH_LONG).show();
								for (int i = 0; i < horItems.length; i++) {
									itemView.findViewById(horItems[i]).setVisibility(View.INVISIBLE);
								}
							}
						}
					}
				});
	}

	// default UI

	public static void setDefalutView() {
		isWhatRight = Constant.MYMUSIC;
		setAppStoreList(Constant.MYMUSIC_APP + pageCount);
	}

	// set pilot play UI
//	public static void setMusicPilot(final String path, View v1, int viewid) {
//
//		v1.setOnKeyListener(new OnKeyListener() {
//
//			@Override
//			public boolean onKey(View v, int keyCode, KeyEvent event) {
//				if (keyCode == KeyEvent.KEYCODE_BACK) {
//					if (audioStreamer != null) {
//						if (audioStreamer.getMediaPlayer() != null) {
//							audioStreamer.getMediaPlayer().stop();
//						}
//					}
//				}
//				return false;
//			}
//		});
//		try {
//			if (whatisplay == 1) {
//				whatisplay = viewid;
//				final SeekBar progressBar = (SeekBar) (v1.findViewById(viewid)
//						.findViewById(R.id.progress_bar));
//				progressBar.setVisibility(View.VISIBLE);
//				final TextView playTime = (TextView) (v1.findViewById(viewid)
//						.findViewById(R.id.playTime));
//				audioStreamer = new StreamingMediaPlayer(aQuery.getContext(),
//						progressBar, playTime);
//				audioStreamer.startStreaming(path);
//			} else if (whatisplay == viewid) {
//				whatisplay = viewid;
//				if (audioStreamer.getMediaPlayer().isPlaying()) {
//					audioStreamer.getMediaPlayer().pause();
//				} else if (!audioStreamer.getMediaPlayer().isPlaying()) {
//					audioStreamer.getMediaPlayer().start();
//					audioStreamer.startPlayProgressUpdater();
//				}
//			} else if (whatisplay != viewid) {
//				final SeekBar progressBarold = (SeekBar) (v1
//						.findViewById(whatisplay)
//						.findViewById(R.id.progress_bar));
//				progressBarold.setVisibility(View.INVISIBLE);
//				whatisplay = viewid;
//				if (audioStreamer.getMediaPlayer().isPlaying()) {
//					audioStreamer.getMediaPlayer().stop();
//				}
//				final SeekBar progressBar = (SeekBar) (v1.findViewById(viewid)
//						.findViewById(R.id.progress_bar));
//				progressBar.setVisibility(View.VISIBLE);
//				final TextView playTime = (TextView) (v1.findViewById(viewid)
//						.findViewById(R.id.playTime));
//				audioStreamer = new StreamingMediaPlayer(aQuery.getContext(),
//						progressBar, playTime);
//				audioStreamer.startStreaming(path);
//			}
//
//		} catch (Exception e) {
//		}
//		// //
//		// final Dialog dl = new Dialog(aQuery.getContext());
//		// final View view = inflater.inflate(R.layout.musicplay, null);
//		// dl.requestWindowFeature(Window.FEATURE_NO_TITLE);
//		// dl.setContentView(view);
//		// Window dialogWindow = dl.getWindow();
//		// WindowManager.LayoutParams lp = dialogWindow.getAttributes();
//		// dialogWindow.setGravity(Gravity.CENTER);
//		// lp.width = 400;
//		// lp.height =300;
//		// dialogWindow.setAttributes(lp);
//		// dl.show();
//		// final ImageButton iv = (ImageButton)
//		// view.findViewById(R.id.btn_play);
//		// ((TextView)view.findViewById(R.id.btn_playname)).setText(name);
//		// mp = new MediaPlayer();
//		// view.findViewById(R.id.btn_stop).setOnClickListener(new
//		// OnClickListener() {
//		// @Override
//		// public void onClick(View v) {
//		// // TODO Auto-generated method stub
//		// mp.stop();
//		// dl.dismiss();
//		// }
//		// });
//		// view.findViewById(R.id.btn_play).setOnClickListener(new
//		// OnClickListener() {
//		// @Override
//		// public void onClick(View v) {
//		// if(isplay){
//		// mp.pause();
//		// iv.setImageResource(R.drawable.desktop_playbt_b);
//		// isplay=false;
//		// }
//		// else if(!isplay){
//		// mp.start();
//		// iv.setImageResource(R.drawable.desktop_pausebt_b);
//		// isplay =true;
//		// }
//		// }
//		// });
//		// dl.setOnKeyListener(new OnKeyListener() {
//		// @Override
//		// public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent
//		// event) {
//		// // TODO Auto-generated method stub
//		// if(keyCode==KeyEvent.KEYCODE_BACK){
//		// if(isplay){
//		// if(mp!=null){
//		// mp.stop();
//		// }
//		// }
//		// return false;
//		// }
//		// return false;
//		// }
//		// });
//		//
//		// if (musicTryTask != null && musicTryTask.getStatus() ==
//		// AsyncTask.Status.RUNNING) {
//		// musicTryTask.cancel(true); // 如果Task还在运行，则先取消它
//		// }else{
//		// musicTryTask = new musicTryplayAsyncTask(iv);
//		// }
//		// musicTryTask.execute(path);
//
//	}

	// setmvplay
	public static void setMVPilot(final Paper music) {
//	
//		Intent it =  new Intent(aQuery.getContext(),PlayerActivity.class);
//		
//		it.putExtra("clientControll", false);
//		it.putExtra("movie",music);
//		// i.setClass(aQuery.getContext(), Simplayer.class);
//		aQuery.getContext().startActivity(it);
		Intent it;
		if(Constant.useVitamio){
			it =  new Intent(aQuery.getContext(),VitamioPlayer.class);
		}else{
			 it =  new Intent(aQuery.getContext(),PlayerActivity.class);
		}
		
		it.putExtra("clientControll", false);
		it.putExtra("movie",music);
		// i.setClass(aQuery.getContext(), Simplayer.class);
		aQuery.getContext().startActivity(it);
	}

	// setdown（music，mv）
	public static void setMusicDown(String sid) {
		// downcheck
		String path = HttpRequest.URL_QUERY_DOWNLOAD_CHECK + "sid=" + sid
				+ "&type=music";
		final ProgressDialog Dialog = ProgressDialog.show(aQuery.getContext(),
				"Checking。。", "please wait a moment。。");
		ProDiaglogDimiss(Dialog);
		final Handler handler = new Handler();
		Dialog.show();
		aQuery.ajax(path, String.class, new AjaxCallback<String>() {

			@Override
			public void callback(String url, String json, AjaxStatus status) {
				if (json != null) {
					System.out.println(json);
					Dialog.dismiss();
					try {
						JSONObject jb = new JSONObject(json);
						if ("true".equals(jb.get("success"))) {
							String filepath = jb.getString("filepath");
							String filename = jb.getString("filename");
							final String path = HttpRequest.URL_QUERY_DOWNLOAD_URL
									+ filepath + "&" + "duomi";
							new AsyncTask<Void, Void, Void>() {
								@Override
								protected Void doInBackground(Void... params) {
									UpdateVersion updateVersion = UpdateVersion
											.instance(aQuery.getContext(),
													handler, false);
									updateVersion.setUpdateUrl(path);
									updateVersion.run();
									return null;
								}
							}.execute();

						} else {
							String errmessage = jb.getString("errmessage");
							if (errmessage != null) {
								Toast.makeText(aQuery.getContext(), errmessage,
										1).show();

							} else {
								Toast.makeText(
										aQuery.getContext(),
										aQuery.getContext()
												.getResources()
												.getString(
														R.string.unknownError),
										1).show();
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}

				}

				else {
					Dialog.dismiss();
					if (status.getCode() == -101) {
						Toast.makeText(
								aQuery.getContext(),
								aQuery.getContext().getResources()
										.getString(R.string.checknetwork),
								Toast.LENGTH_LONG).show();
					} else {
						Toast.makeText(aQuery.getContext(),
								"Error:" + status.getCode(), Toast.LENGTH_LONG)
								.show();
					}
				}
			}
		});
	}

	// setMusicChapterlist
	public static void setMusicChapterList(final String path) {
		if (!isSearch) {
			currentPath = path.substring(0, path.lastIndexOf("&"));
		}
		initDialog("three");
		viewFormusicdetail = inflater.inflate(R.layout.tv_detail, null);
		final ProgressDialog Dialog = ProgressDialog.show(aQuery.getContext(),
				"loading。。", "please wait。。");
		ProDiaglogDimiss(Dialog);
		aQuery.ajax(path, String.class, new AjaxCallback<String>() {
			@Override
			public void callback(String url, String json, AjaxStatus status) {
				if (json != null) {
					JSONObject jsObject;
					Dialog.dismiss();
						try {
							System.out.println("下载下来的json是"+json);
							System.out.println("下载的地址为"+path);
							jsObject = new JSONObject(json);
						JSONObject pageObject = jsObject
								.getJSONObject("page");
						String currentPage = pageObject
								.getString("currentPage");
						String pageSize = pageObject
								.getString("pageSize");
						int totalRows = pageObject.getInt("totalRows");
						page.init(currentPage, pageSize, totalRows);
						TextView tv = (TextView) (itemView.findViewById(R.id.field_page_index));
						tv.setText("第" + page.getCurrentPage() + "页"+"/"+"总"+page.getTotalPage()+"页");
					music_chapterList = new ArrayList<News>();
					music_chapterList = JsonUtil.getNewsList(json);
					if (music_chapterList.size() != 0) {
						setMusicChapterInfo(music_chapterList, path);
					} else {
						Dialog.dismiss();
						Toast.makeText(
								aQuery.getContext(),
								aQuery.getContext().getResources()
										.getString(R.string.nocontent),
								Toast.LENGTH_LONG).show();
						for (int i = 0; i < horItems.length; i++) {
							itemView.findViewById(horItems[i]).setVisibility(View.INVISIBLE);
						}
					}
						} catch (JSONException e) {
							Dialog.dismiss();
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					Dialog.dismiss();
				} else {
					Dialog.dismiss();
					if (status.getCode() == -101) {
						Toast.makeText(
								aQuery.getContext(),
								"Error:"
										+ status.getCode()
										+ aQuery.getContext()
												.getResources()
												.getString(
														R.string.checknetwork),
								Toast.LENGTH_LONG).show();
						for (int i = 0; i < horItems.length; i++) {
							itemView.findViewById(horItems[i]).setVisibility(View.INVISIBLE);
						}
					} else {
						Toast.makeText(
								aQuery.getContext(),
								aQuery.getContext().getResources()
										.getString(R.string.nocontent),
								Toast.LENGTH_LONG).show();
						for (int i = 0; i < horItems.length; i++) {
							itemView.findViewById(horItems[i]).setVisibility(View.INVISIBLE);
						}
					}
				}
				if (music_chapterList != null) {
					for (int i = 0; i < music_chapterList.size(); i++) {
						final int k = i;
						final String orderId = music_chapterList.get(i).getId();
						final News news = music_chapterList.get(i);
						itemView.findViewById(horItems[i]).setOnClickListener(
								new OnClickListener() {
									@Override
									public void onClick(View v) {
//										setMusicDetial(v.getId(),
//												news,
//												orderId,
//												iswh);
										Intent i = new Intent(aQuery.getContext(),NewsDetailActivity.class);
										 i.putExtra("news", news);
										 aQuery.getContext().startActivity(i);
										 
									}
								});
					}
				}
			}
		});
	}

	// setTv list
	public static void setTVList(final String path) {
		if (!isSearch) {
			currentPath = path.substring(0, path.lastIndexOf("&"));
		}
		initDialog("three");
		viewFormusicdetail = inflater.inflate(R.layout.tv_detail, null);
		final ProgressDialog Dialog = ProgressDialog.show(aQuery.getContext(),
				"loading。。", "please wait a moment。。");
		ProDiaglogDimiss(Dialog);
		aQuery.ajax(path, String.class, new AjaxCallback<String>() {
			@Override
			public void callback(String url, String json, AjaxStatus status) {
				if (json != null) {
					JSONObject jsObject;
				
						try {
							jsObject = new JSONObject(json);
						
					JSONObject pageObject = jsObject
							.getJSONObject("page");
					String currentPage = pageObject
							.getString("currentPage");
					String pageSize = pageObject
							.getString("pageSize");
					int totalRows = pageObject.getInt("totalRows");
					page.init(currentPage, pageSize, totalRows);
					TextView tv = (TextView) (itemView.findViewById(R.id.field_page_index));
					tv.setText("第" + page.getCurrentPage() + "页"+"/"+"总"+page.getTotalPage()+"页");
					mvlist = new ArrayList<News>();
					mvlist = JsonUtil.getNewsList(json);
					if (mvlist.size() != 0) {
						setMusicMvInfo(mvlist, path);
					} else {
						Toast.makeText(
								aQuery.getContext(),
								aQuery.getContext().getResources()
										.getString(R.string.nocontent),
								Toast.LENGTH_LONG).show();
						for (int i = 0; i < horItems.length; i++) {
							itemView.findViewById(horItems[i]).setVisibility(View.INVISIBLE);
						}
					}} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				
					Dialog.dismiss();
				} else {
					Dialog.dismiss();
					if (status.getCode() == -101) {
						Toast.makeText(
								aQuery.getContext(),
								"Error:"
										+ status.getCode()
										+ aQuery.getContext()
												.getResources()
												.getString(
														R.string.checknetwork),
								Toast.LENGTH_LONG).show();
						for (int i = 0; i < horItems.length; i++) {
							itemView.findViewById(horItems[i]).setVisibility(View.INVISIBLE);
						}
					} else {
						Toast.makeText(
								aQuery.getContext(),
								aQuery.getContext().getResources()
										.getString(R.string.nocontent),
								Toast.LENGTH_LONG).show();
						for (int i = 0; i < horItems.length; i++) {
							itemView.findViewById(horItems[i]).setVisibility(View.INVISIBLE);
						}
					}
				}
				if (mvlist.size() != 0) {
					for (int i = 0; i < mvlist.size(); i++) {
						final String orderid = mvlist.get(i).getId();
						itemView.findViewById(horItems[i]).setOnClickListener(
								new OnClickListener() {
									public void onClick(View v) {
//										setMusicDetial(v.getId(), mvlist,
//												viewFormusicdetail, orderid,
//												isTv);
									}
								});
					}
				}
			}
		});
	}
	// loading dialog for recommedmusic
	public static void setRecommedMusicInfo(String musicId, final View view,
			News music,final int iswhat) {
		
		String web_url = null;
		for (int i = 0; i < tvlistItem.length; i++) {
			if(isWhatLeft==Constant.MUSICCHAPTER){
			view.findViewById(tvlistItem[i]).setVisibility(View.GONE);
			
			}else{
				view.findViewById(tvlistItem[i]).setVisibility(View.INVISIBLE);
			}
		}
		final ProgressDialog Dialog = ProgressDialog.show(aQuery.getContext(),
				"Loading。。", "please wait moment。。");
		Dialog.show();
		ProDiaglogDimiss(Dialog);
		if(iswhat==isFilm){
			web_url= HttpRequest.URL_QUERY_LIST_MOVIE + musicId;
		}else if(iswhat==isTv){
			web_url=HttpRequest.URL_QUERY_LIST_TV+musicId;
		}
		aQuery.ajax(web_url, String.class, new AjaxCallback<String>() {
			@Override
			public void callback(String url, String json, AjaxStatus status) {
				if (json != null) {
					final ArrayList<Paper> musicDetialList = new ArrayList<Paper>();
					System.out.println("下载的数据" + "====" + json);
					Dialog.dismiss();
					try {
						JSONArray ja = new JSONArray(json);
						for (int i = 0; i < ja.length(); i++) {
							Paper music = new Paper();
							JSONObject jb = ja.getJSONObject(i);
							String sid = jb.getString("sid");
							String musicpath = jb.getString("filepath");
							String title = jb.getString("title");
							String seq = null;
							if(!jb.isNull("seq")){
							seq =jb.getString("seq");
							}
							music.setSeq(seq);
							music.setDownload_path(musicpath);
							music.setId(sid);
							music.setName(title);
							musicDetialList.add(music);
						}
						int temp = 0;//级数常量
						for (int i = 0; i < musicDetialList.size(); i++) {
							
							final Paper music =musicDetialList.get(i);
							if(isWhatLeft==Constant.MUSICCHAPTER){
							TextView tv =(TextView)(view.findViewById(tvlistItem[i]));
							tv.setWidth(LayoutParams.WRAP_CONTENT);
							tv.setVisibility(View.VISIBLE);
							tv.setText(music.getName());
							}else{
								String seq = music.getSeq();
								if(seq.length()!=0){
									TextView tv =(TextView)(view.findViewById(tvlistItem[i]));
									tv.setWidth(LayoutParams.WRAP_CONTENT);
									view.findViewById(tvlistItem[i])
											.setVisibility(View.VISIBLE);
									tv.setText(seq);
									temp =Integer.parseInt(seq);
								}else{
									TextView tv =(TextView)(view.findViewById(tvlistItem[i]));
									tv.setWidth(LayoutParams.WRAP_CONTENT);
									view.findViewById(tvlistItem[i])
											.setVisibility(View.VISIBLE);
									temp =temp+1;
									tv.setText(temp+"");
								}
							}
							view.findViewById(tvlistItem[i])
									.setOnClickListener(
											new OnClickListener() {
												@Override
												public void onClick(
														View v) {
														setMVPilot(music);
												}
											});
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
					Dialog.dismiss();
				} else {
					Toast.makeText(aQuery.getContext(),
							"Error:" + status.getCode(), Toast.LENGTH_LONG)
							.show();
				}
			}
		});
		String musicssPath = null  ;
		if(iswhat==isFilm){
			musicssPath= HttpRequest.URL_QUERY_SINGLE_MOVIE + musicId;
		}else if(iswhat==isTv){
			musicssPath=HttpRequest.URL_QUERY_SINGLE_TV+musicId;
		}
		aQuery.ajax(musicssPath, String.class, new AjaxCallback<String>() {
			@Override
			public void callback(String url, String json, AjaxStatus status) {
				if (json != null) {
					final String image_path_boot;
					try {
						final ArrayList<String> pathList = new ArrayList<String>();
						ArrayList<String> nameList = new ArrayList<String>();
						final ArrayList<PostMent> postMentList = new ArrayList<PostMent>();
						JSONObject jb = new JSONObject(json);
						image_path_boot = jb.getString("PIC");
						String name = jb.getString("PNAME");
						String note = jb.getString("PNOTE");
						String director = "";
						String kind = "";
						String rose = "";
						String pubdate = "";
						if (!jb.isNull("DIRECTOR")) {
							director = jb.getString("DIRECTOR");
						}
						if (!jb.isNull("KIND")) {
							kind = jb.getString("KIND");
						}
						if (!jb.isNull("ROSE")) {
							rose = jb.getString("ROSE");
						}
						if (!jb.isNull("RELEASE")) {
							pubdate = jb.getString("RELEASE");
						}
						JSONArray postOb = jb.getJSONArray("potype");
						for (int i = 0; i < postOb.length(); i++) {

							PostMent pm = new PostMent();
							pm.setType(postOb.getJSONObject(i)
									.getString("TYPE"));
							pm.setId(postOb.getJSONObject(i).getString("PUBID"));
							pm.setPrice(postOb.getJSONObject(i).getString(
									"PRICE"));
							postMentList.add(pm);
						}
						builder.show();
						((TextView) viewFormusicdetail
								.findViewById(R.id.albumname)).setText(name);
						((TextView) viewFormusicdetail
								.findViewById(R.id.albuminfo)).setText(note);
						((TextView) viewFormusicdetail
								.findViewById(R.id.artist)).setText(aQuery
								.getContext().getResources()
								.getString(R.string.director)
								+ " : " + director);
						((TextView) viewFormusicdetail
								.findViewById(R.id.Issuedate)).setText(aQuery
								.getContext().getResources()
								.getString(R.string.pubdate)
								+ " ： " + pubdate);
						((TextView) viewFormusicdetail
								.findViewById(R.id.language)).setText(aQuery
								.getContext().getResources()
								.getString(R.string.language)
								+ " ： " + rose);
						((TextView) viewFormusicdetail
								.findViewById(R.id.company)).setText(aQuery
								.getContext().getResources()
								.getString(R.string.company)
								+ " ： " + kind);
					} catch (JSONException e) {
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

	/**
	 * subscription
	 *  加入到购物车中
	 * @param postMentList
	 * @param id
	 */
	static void setOrder(final ArrayList<PostMent> postMentList, final String id) {
		String[]  myRadio = new String[postMentList.size()];
		for (int i = 0; i < postMentList.size(); i++) {
			String type = postMentList.get(i).getType();
			String price = postMentList.get(i).getPrice();
			myRadio[i] = type + "/" + price;

		}

		final View view = inflater.inflate(R.layout.orderstype, null);
		for (int i = 0; i < orderRadioItem.length; i++) {
			view.findViewById(orderRadioItem[i]).setVisibility(View.INVISIBLE);
		}
		for (int i = 0; i < postMentList.size(); i++) {
			view.findViewById(orderRadioItem[i]).setVisibility(View.INVISIBLE);
			RadioButton btn_rb = (RadioButton) view
					.findViewById(orderRadioItem[i]);
			String typeE = postMentList.get(i).getType();
			if(i==0){
				
				whatTrueOrder=typeE;
			}
			if (typeE.equals("day")) {
				btn_rb.setText("day/" + postMentList.get(i).getPrice());
				btn_rb.setVisibility(View.VISIBLE);
			}
			if (typeE.equals("month")) {
				btn_rb.setText("month/" + postMentList.get(i).getPrice());
				btn_rb.setVisibility(View.VISIBLE);
			}
			if (typeE.equals("quarter")) {
				btn_rb.setText("quarter/" + postMentList.get(i).getPrice());
				btn_rb.setVisibility(View.VISIBLE);
			}
			if (typeE.equals("year")) {
				btn_rb.setText("year/" + postMentList.get(i).getPrice());
				btn_rb.setVisibility(View.VISIBLE);
			}
		}
	
		final Dialog dl = new Dialog(aQuery.getContext());
		dl.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dl.setContentView(view);
		Window dialogWindow = dl.getWindow();
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		dialogWindow.setGravity(Gravity.CENTER);
		lp.width = 600;
		lp.height = 400;
		dialogWindow.setAttributes(lp);
		dl.show();
		RadioGroup group = (RadioGroup) view.findViewById(R.id.radioGroup);
		group.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				int checkedRadioButtonId = group.getCheckedRadioButtonId();
				// 根据ID获取RadioButton的实例
				RadioButton RadioButtonrb = (RadioButton) view
						.findViewById(checkedRadioButtonId);
				String s = RadioButtonrb.getText().toString();
				String whatOrder = s.substring(0, s.lastIndexOf("/"));
				whatTrueOrder = whatOrder;
				Toast.makeText(aQuery.getContext(), whatOrder, 1).show();

			}
		});
		view.findViewById(R.id.btn_order_confrm).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						new AsyncTask<Void, Void, String>(){

							@Override
							protected void onPostExecute(String result) {
								pd.dismiss();
								if(result.equals("true")){
									Toast.makeText(aQuery.getContext(), "加入购物车成功", 1).show();
								}else{
									
									Toast.makeText(aQuery.getContext(), "加入购物车失败，请重试", 1).show();
								}
								
								super.onPostExecute(result);
								
							}

							@Override
							protected void onPreExecute() {
								pd =new ProgressDialog(aQuery.getContext());
								pd.show();
								super.onPreExecute();
							}
							@Override
							protected String doInBackground(Void... params) {
								String url = null;
								if(isWhatLeft==Constant.MUSICCHAPTER){
									url = HttpRequest.URL_QUERY_LIST_PAY_ALLMOVIE + id
										+ HttpRequest.URL_ADD + whatTrueOrder;
								}else if(isWhatLeft==Constant.MUSICMV){
									url= HttpRequest.URL_QUERY_LIST_PAY_ALLTV + id
											+ HttpRequest.URL_ADD + whatTrueOrder;
								}
								String result = null;
								System.out.println("订购的地址为：===" + url);
								try {
									HttpGet request = new HttpGet(url);
									// 绑定到请求 Entry
									// 发送请求
									HttpResponse response = new DefaultHttpClient()
											.execute(request);
									// 得到应答的字符串，这也是一个 JSON 格式保存的数据
									result = EntityUtils.toString(response.getEntity());
									dl.dismiss();
								} catch (Exception e) {
									dl.dismiss();
									// TODO Auto-generated catch block
									e.printStackTrace();

								}
								return result;
							}
							
						}.execute();
						

					}
				});
		view.findViewById(R.id.btn_order_cancle).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						dl.dismiss();
					}
				});
	}

	/**
	 * check version
	 */
	private void checkVersion() {
		final Handler hd = new Handler();
		// TODO Auto-generated method stub
		new AsyncTask<Void, Void, String>() {
			@Override
			protected void onPostExecute(String result) {
				if (isUpdate) {
					setUpdateDiago(path, desc);
				}
				super.onPostExecute(path);
			}

			String path;
			String desc;
			private ArrayList<VersionInfo> versionInfoList;

			@Override
			protected String doInBackground(Void... params) {
				try {
					URL url = new URL(HttpRequest.URL_UPDATE);
					URLConnection con = url.openConnection();
					InputStream is = con.getInputStream();
					XmlParse xp = new XmlParse();
					versionInfoList = xp.getVersionInfo(is);
					for (int i = 0; i < versionInfoList.size(); i++) {
						if ("NewsStore".equals(versionInfoList.get(i)
								.getName())) {
							String updateVersion = versionInfoList.get(i)
									.getVersion();
							PackageManager packageManager = getPackageManager();
							// getPackageName()是你当前类的包名，0代表是获取版本信息
							PackageInfo packInfo = packageManager
									.getPackageInfo(getPackageName(), 0);
							String version = packInfo.versionName;
							System.out.println("version============" + version);
							if (!version.equals(updateVersion)) {
								isUpdate = true;
							}
							path = HttpRequest.URL_UPDATE_ROOT
									+ versionInfoList.get(i).getFilepath();
							desc = versionInfoList.get(i).getDescription();
							System.out.println("下载的地址为" + path);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return path;
			}
		}.execute();
	}

	/**
	 * appearUpdateDialog
	 * 
	 * @param path
	 *            Update url path
	 * @param desc
	 *            Update content
	 */
	void setUpdateDiago(final String path, String desc) {
		final Handler hd = new Handler();

		// TODO Auto-generated method stub
		Dialog dialog = new AlertDialog.Builder(aQuery.getContext())
				.setTitle(getResources().getString(R.string.findnewversion))
				.setMessage(
						getResources().getString(R.string.vserioncontent)
								+ "\n" + desc)
				.setPositiveButton(getResources().getString(R.string.confirm),
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								new AsyncTask<Void, Void, Void>() {
									@Override
									protected Void doInBackground(
											Void... params) {
										UpdateVersion uv = UpdateVersion
												.instance(aQuery.getContext(),
														hd, true);
										uv.setUpdateUrl(path);
										uv.run();
										return null;
									}

								}.execute();
							}
						})
				.setNegativeButton(getResources().getString(R.string.cancle),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								dialog.dismiss();
							}
						}).create();
		dialog.show();

	}

	/**
	 * set Next or Pre page
	 * 
	 * @param path
	 *            Url path
	 * @param what
	 *            soft or musicChapter or mv
	 * 
	 */
	static void UpdatePaging(final String path, final String what) {
		final TextView tv = (TextView) itemView
				.findViewById(R.id.field_page_index);
		Button btn_pre = (Button) itemView.findViewById(R.id.page_pre);
		btn_pre.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				if (page.isFirstPage()) {
					pageCount = 1;
					Toast.makeText(aQuery.getContext(), "已经是第一页了", 1).show();
					tv.setText("第" + pageCount + "页"+"/"+"总"+1+"页");
				} else {
					pageCount = page.getCurrentPage()-1;
					tv.setText("第" + pageCount + "页"+"/"+"总"+page.getTotalPage()+"页");
					String truePath = path.substring(0,
							path.lastIndexOf("=") + 1);
					System.out.println("TruePATH===========" + truePath);
					String myPath = truePath + pageCount;
					if (what.equals("soft")) {
						setAppStoreList(myPath);
					} else if (what.equals("musicChapter")) {
						setMusicChapterList(myPath);
					} else if (what.equals("mv")) {
						setTVList(myPath);
					}
				}
			}
		});
		Button btn_next = (Button) itemView.findViewById(R.id.page_next);
		btn_next.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(!page.isLastPage()){
				if (what.equals("soft")) {
						pageCount = page.getCurrentPage() + 1;
						tv.setText("第" + pageCount + "页"+"/"+"总"+page.getTotalPage()+"页");
						String truePath = path.substring(0,
								path.lastIndexOf("=") + 1);
						String myPath = truePath + pageCount;
						setAppStoreList(myPath);
				} else if (what.equals("musicChapter")) {
						pageCount = page.getCurrentPage() + 1;;
						tv.setText("第" + pageCount + "页"+"/"+"总"+page.getTotalPage()+"页");
						String truePath = path.substring(0,
								path.lastIndexOf("=") + 1);
						String myPath = truePath + pageCount;
						setMusicChapterList(myPath);
				} else if (what.equals("mv")) {
						pageCount = page.getCurrentPage() + 1;
						tv.setText("第" + pageCount + "页"+"/"+"总"+page.getTotalPage()+"页");
						String truePath = path.substring(0,
								path.lastIndexOf("=") + 1);
						String myPath = truePath + pageCount;
						setTVList(myPath);
				}
			}else{
				Toast.makeText(aQuery.getContext(), "已经为最后一页了", 1).show();
			}
			}
		});

	}

	public static class musicTryplayAsyncTask extends
			AsyncTask<String, Void, Void> {
		private ImageButton myBtn;
		public musicTryplayAsyncTask(ImageButton iv) {
			myBtn = iv;
		}

		@Override
		protected Void doInBackground(String... params) {
			try {
				String path = params[0];
				mp.setDataSource(aQuery.getContext(), Uri.parse(path));
				mp.prepare();
			} catch (Exception e) {
				e.printStackTrace();
			}
			mp.start();
			isplay = true;
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			myBtn.setImageResource(R.drawable.desktop_pause_b);
			super.onPostExecute(result);
		}
	}
	  public static void DiaglogDimiss(Dialog dialog){
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
	  public static void ProDiaglogDimiss(ProgressDialog dialog){
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
	  /**
	   * 获得屏幕的宽高
	   */
	  private void setParams() {
		  DisplayMetrics dm = new DisplayMetrics();
		  getWindowManager().getDefaultDisplay().getMetrics(dm);
		  Rect rect = new Rect();
		  View view = getWindow().getDecorView();
		  view.getWindowVisibleDisplayFrame(rect);
		  DeviceHeight = dm.heightPixels - rect.top;
		  DeviceWidth = dm.widthPixels;
		 }
}
