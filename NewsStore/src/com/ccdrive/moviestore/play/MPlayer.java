package com.ccdrive.moviestore.play;

import io.vov.utils.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnInfoListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaPlayer.OnSeekCompleteListener;
import android.media.MediaPlayer.OnVideoSizeChangedListener;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.Toast;

import com.ccdrive.moviestore.R;
import com.ccdrive.moviestore.bean.Movie;
import com.ccdrive.moviestore.http.HttpRequest;
import com.rushfusion.vhttpproxy.VHttpProxyDaemon.OnBufferingStatus;

public class MPlayer{
	

	private static final String TAG = "MPlayer";

	private int mMediaType = MediaConstant.MediaType.TYPE_VIDEO;
	public static final int TYPE_VIDEO = MediaConstant.MediaType.TYPE_VIDEO;
	public static final int TYPE_MUSIC = MediaConstant.MediaType.TYPE_MUSIC;
	
	private Activity mContext;
	private static MPlayer mInstance;
	
	private int index = 0;
	private List<String> episodes = new ArrayList<String>();
	private Movie movie;
	
	private String mUrl = "";
	
	public String getCurrentUrl(){
		return mUrl;
	}
	private String mTitle = "";
	
	public String getCurrentTitle(){
		return mTitle;
	}
	
	
	private View mRootView;
	public View getRootView() {
		return mRootView;
	}

	private SurfaceView mSurfaceView;
	private SurfaceHolder mSurfaceHolder;
	private MediaPlayer mMediaPlayer;
	private MATController mMediaController;
	public MATDialog mDialog;
	
	private int mVideoWidth = 0;
	private int mVideoHeight = 0;
	private int mSurfaceWidth;
	private int mSurfaceHeight;
	
	private int mPosition = -1;
	private int mDuration = -1;
	
	private boolean useHttpProxy = false;
	
	public boolean isHttpProxy() {
		return useHttpProxy;
	}

	public void setHttpProxy(boolean httpProxy) {
		this.useHttpProxy = httpProxy;
	}
	
	private boolean clientControll = false;
	
	public boolean isClientControll() {
		return clientControll;
	}

	public void setClientControll(boolean clientControll) {
		this.clientControll = clientControll;
	}

	public static final int MSG_WHAT_UPDATE_PROGRESS = 1;
	public static final int MSG_WHAT_SHOW_NETWORK_SPEED = 2;
	public static final int MSG_WHAT_SHOW_BUFFER_INFO = 3;
	public static final int MSG_WHAT_HIDE_CONTROLLER = 4;
	public static final int MSG_WHAT_UPDATE_CONTROLLER_INFO = 5;
	public static final int MSG_WHAT_AUTO_EXIT = 6;

	public static final int REFRESH_NETWORK_INTERVAL = 1000;
	public static final int REFRESH_BUFFER_INTERVAL = 1000;

	private float mVideoAspectRatio;

	private int mVideoLayout = VIDEO_LAYOUT_KEEP;

	public static final int VIDEO_LAYOUT_ORIGIN = 0;// 原始大小
	public static final int VIDEO_LAYOUT_SCALE = 1;//
	public static final int VIDEO_LAYOUT_STRETCH = 2;// 全屏拉伸
	public static final int VIDEO_LAYOUT_ZOOM = 3;//
	public static final int VIDEO_LAYOUT_KEEP = 4;// 保持比例拉伸

	private int mCurrentState = STATE_IDLE;

	public int getState() {
		return mCurrentState;
	}

	public void setState(int state) {
		this.mCurrentState = state;
	}

	public static final int STATE_ERROR = -1;
	public static final int STATE_IDLE = 0;
	public static final int STATE_PREPARING = 1;
	public static final int STATE_PREPARED = 2;
	public static final int STATE_PLAYING = 3;
	public static final int STATE_PAUSED = 4;
	public static final int STATE_PLAYBACK_COMPLETED = 5;
	public static final int STATE_SUSPEND = 6;

	private Resources res;
	private SharedPreferences sp;
	private Editor editor;
	private int _progress;
	
	Handler _handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO:handleMsg
			super.handleMessage(msg);
			switch (msg.what) {
			case MSG_WHAT_UPDATE_PROGRESS:
				mDialog.setMessage(res.getString(R.string.loading));
				mDialog.setProgress(_progress);
				return;
			case MSG_WHAT_HIDE_CONTROLLER:
				mMediaController.hide();
				break;
			case MSG_WHAT_SHOW_NETWORK_SPEED:
				HttpProxy.getInstance().showNetworkInfo();
				HttpProxy.getInstance().startNetworkInfo();
				return;
			case MSG_WHAT_SHOW_BUFFER_INFO:
				HttpProxy.getInstance().showBufferInfo();
				return;
			case MSG_WHAT_UPDATE_CONTROLLER_INFO:
				updateControllerInfo();
				mMediaController.updateInfo();
				break;
			case MSG_WHAT_AUTO_EXIT:
				int s = (Integer) msg.obj;
				doAutoExit(s);
				break;
			default:
				break;
			}
		}
	};
	
	private MPlayer() {
	}

	private void initResources(Context context) {
		sp = context.getSharedPreferences("watch_record", Context.MODE_PRIVATE);
		editor = sp.edit();
		res = context.getResources();
	}

	public void initController() {
		mMediaController = new MATController(mContext,mRootView);
		mMediaController.setHandler(_handler);
	}

	public static MPlayer getInstance(){
		if(mInstance==null){
			mInstance = new MPlayer();
		}
		return mInstance;
	}
	
	public void setOwnerActivity(Activity context){
		mContext = context;
		mRootView = LayoutInflater.from(mContext).inflate(R.layout.player, null);
		mSurfaceView = (SurfaceView) mRootView.findViewById(R.id.surfaceView);
		initResources(mContext);
	}

	
	
	private void doAutoExit(int s) {
		System.out.println("s:"+s);
		if (s <= 1) {
			if (errordialog != null && errordialog.isShowing()
					&& errordialog.getWindow() != null) {
				errordialog.dismiss();
			}
			doExitPlayer();
			return;
		}
		_handler.sendMessageDelayed(_handler.obtainMessage(MSG_WHAT_AUTO_EXIT, --s), 1000);
	}
	
	
	public boolean isInPlaybackState() {
		return (mMediaPlayer != null && mCurrentState != STATE_ERROR
				&& mCurrentState != STATE_IDLE && mCurrentState != STATE_PREPARING);
	}
	
	
	/**
	 * 1、更新进度条 2、更新时�?3、更新下载�?�?
	 */
	protected void updateControllerInfo() {
		// TODO Auto-generated method stub
		if (isInPlaybackState()) {
			mPosition = getCurrentPosition();
			if(mDuration==0)mDuration = getDuration();
			if (mDuration > 0) {
				mMediaController.syncTimeBar(mPosition,mDuration);
				// controller.seekBar.setSecondaryProgress(???+100*position/duration);//???=100*blocksize*_progress/totalsize;
			}
			return;
		}
	}
	
	public void init() {
		initDialog();
		initController();
		initMediaPlayer();
		initSurfaceView();
		if(episodes.size()>1){
			mMediaController.setAllowLoadVideoTask(true);
		}else{
			mMediaController.setAllowLoadVideoTask(false);
		}
		mMediaController.setFilmName(mTitle);
	}
	
	
	void initSurfaceView() {
		mSurfaceView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (mMediaController.isShowing()) {
					mMediaController.hide();
				} else {
					mMediaController.show();
				}
			}
		});
		mSurfaceHolder = mSurfaceView.getHolder();
		mSurfaceHolder.setFixedSize(500, 500);
		mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		mSurfaceHolder.addCallback(mCallback);
	}
	
	SurfaceHolder.Callback mCallback = new Callback() {

		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int w,
				int h) {
			Log.d(TAG, ">>surfaceChanged,w:" + w + ";h:" + h);
			mSurfaceWidth = w;
			mSurfaceHeight = h;
		}

		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			Log.d(TAG, "==============>surfaceCreated<===============");
			mSurfaceHolder = holder;
			if(mCurrentState == STATE_IDLE){
				if(mUrl==null||mUrl.isEmpty()){
					Log.e(TAG, "url is null");
					Toast.makeText(mContext, res.getString(R.string.player_invalid_url), 1).show();
					doExitPlayer();
					return;
				}
				doPlay();
			}else{
				try {
					mMediaPlayer.setDisplay(mSurfaceHolder);
					return;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {
			Log.d(TAG, "==============>surfaceDestroyed<===============");
			mDialog.hide();
			if (errordialog != null && errordialog.isShowing())
				errordialog.dismiss();
		}
	};
	
	
	public void doPlay() {
		try {
			Log.d(TAG,"doPlay");
			mCurrentState = STATE_PREPARING;
			mMediaPlayer.setDisplay(mSurfaceHolder);
			if (useHttpProxy) {
				HttpProxy.getInstance().init(mUrl,_handler,mMediaController);
				HttpProxy.getInstance().setListener(_bufferStatuslistener);
				mUrl = HttpProxy.getInstance().start();
			}
			mMediaPlayer.setDataSource(mUrl);
			mMediaPlayer.prepareAsync();
		} catch (IllegalStateException e) {
			Log.e(TAG,"IllegalStateException");
			mCurrentState = STATE_ERROR;
			mErrorListener.onError(mMediaPlayer,MediaPlayer.MEDIA_ERROR_UNKNOWN, 0);
			return;
		} catch (IllegalArgumentException e) {
			Log.e(TAG,"IllegalArgumentException");
			mCurrentState = STATE_ERROR;
			mErrorListener.onError(mMediaPlayer,MediaPlayer.MEDIA_ERROR_UNKNOWN, 0);
			return;
		} catch (IOException e) {
			Log.e(TAG,"IOException");
			mCurrentState = STATE_ERROR;
			mErrorListener.onError(mMediaPlayer,MediaPlayer.MEDIA_ERROR_UNKNOWN, 0);
			return;
		}
	}
	
	OnBufferingStatus _bufferStatuslistener = new OnBufferingStatus() {
		public void onStartOfBuffering() {
			Log.i(TAG, "proxy start of buffering");
			mInfoListener.onInfo(mMediaPlayer,MediaPlayer.MEDIA_INFO_BUFFERING_START, 0);
		}

		public void onStopOfBuffering() {
			Log.i(TAG, "proxy stop of buffering");
			mInfoListener.onInfo(mMediaPlayer,MediaPlayer.MEDIA_INFO_BUFFERING_END, 0);
		}

		public void onUpdateBuffering(int percent) {
			Log.i(TAG, "proxy buffering percent " + percent);
			mBufferingUpdateListener.onBufferingUpdate(mMediaPlayer, percent);
		}
	};
	
	boolean isLoading = false;
	Runnable loadVideoTask = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			isLoading = true;
			updateUrl();
			isLoading = false;
		}
	};
	
	private void updateUrl() {
		Log.d(TAG, "updateUrl ->index:" + index + ";list.size:" + episodes.size());
		mDialog.showMessage(res.getString(R.string.onprepare) + mTitle);
		releaseMediaPlayer();
		
		mUrl = episodes.get(index);
		initMediaPlayer();
		mCallback.surfaceCreated(mSurfaceHolder);
	}
	
	private boolean exited = false;
	public void doExitPlayer() {
		Log.d(TAG, "exit");
		new AsyncTask<Void, Void, Void>() {
			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				if(mContext==null){
					cancel(true);
					return;
				}

				canPauseListener = null; 
				mContext.finish();
				
				if(mDialog!=null&&mDialog.isShowing())mDialog.dismiss();
				if(continueDialog!=null&&continueDialog.isShowing())continueDialog.dismiss();
				if(errordialog!=null&&errordialog.isShowing())errordialog.dismiss();
				mMediaController.hide();
				
				if (useHttpProxy) {
					HttpProxy.getInstance().stop();
				}
				
				_handler.removeMessages(MSG_WHAT_UPDATE_CONTROLLER_INFO);
				episodes.clear();
				mRootView.findViewById(R.id.progressBar_player_exit).setVisibility(View.VISIBLE);
			}

			@Override
			protected Void doInBackground(Void... params) {
				// TODO Auto-generated method stub
				if(mCurrentState!=STATE_IDLE)releaseMediaPlayer();//&&mCurrentState!=STATE_ERROR
				return null;
			}
			
			@Override
			protected void onPostExecute(Void result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				mRootView.findViewById(R.id.progressBar_player_exit).setVisibility(View.INVISIBLE);
			}
		}.execute();
	}
	
	

	private void initMediaPlayer() {
		mCurrentState = STATE_IDLE;
		mMediaPlayer = new MediaPlayer();
		mMediaPlayer.setOnInfoListener(mInfoListener);
		mMediaPlayer.setOnBufferingUpdateListener(mBufferingUpdateListener);
		mMediaPlayer.setOnVideoSizeChangedListener(mVideoSizeChangeListener);
		mMediaPlayer.setOnCompletionListener(mCompletionListener);
		mMediaPlayer.setOnErrorListener(mErrorListener);
		mMediaPlayer.setOnPreparedListener(mPreparedListener);
		mMediaPlayer.setOnSeekCompleteListener(mSeekCompleteListener);
	}
	
	private MATDialog initDialog() {
		mDialog = new MATDialog(mContext, R.style.dialog);
		mDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_TOAST);
		mDialog.showMessage(res.getString(R.string.onprepare) + mTitle);
		return mDialog;
	}
	
	public void setUrl(String url){
		mUrl = url;
	}

	public void setMediaType(int mediaType) {
		mMediaType = mediaType;
	}
	
	private OnInfoListener mInfoListener = new OnInfoListener() {

		@Override
		public boolean onInfo(MediaPlayer mp, int what, int extra) {
			switch (what) {
			case MediaPlayer.MEDIA_INFO_BUFFERING_START:
				if(exited)return false;
				Log.i(TAG, "onInfo MEDIA_INFO_BUFFERING_START");
				isBuffering = true;
				mDialog.showMessage(res.getString(R.string.loading));

				if (useHttpProxy) {
					HttpProxy.getInstance().startBuffering();
				}
				break;
			case MediaPlayer.MEDIA_INFO_BUFFERING_END:
				Log.i(TAG, "onInfo MEDIA_INFO_BUFFERING_END");
				isBuffering = false;
				if (useHttpProxy) {
					HttpProxy.getInstance().setIsBuffing(false);
				}
				mDialog.hide();
				break;
			}
			return true;
		}
	};
	
	boolean isBuffering = false;
	OnBufferingUpdateListener mBufferingUpdateListener = new OnBufferingUpdateListener() {

		int temp = 0;
		int delta = 15;//由于监听不到buffer_end，暂用刷�?5�?

		@Override
		public void onBufferingUpdate(MediaPlayer mp, int percent) {
			Log.i(TAG, "onBufferingUpdate  percent:" + percent);
			_progress = percent;
			if (useHttpProxy) {
				_handler.sendEmptyMessage(MSG_WHAT_UPDATE_PROGRESS);
			}
			if (temp++ >= delta && isBuffering) {
				temp = 0;
				mInfoListener.onInfo(mp, MediaPlayer.MEDIA_INFO_BUFFERING_END, 0);
			}
		}
	};
	
	OnVideoSizeChangedListener mVideoSizeChangeListener = new OnVideoSizeChangedListener() {

		@Override
		public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
			Log.d(TAG, "onVideoSizeChanged>>>width:" + width + ";height:"
					+ height);
			mVideoWidth = width;
			mVideoHeight = height;
			mVideoAspectRatio = (float) mVideoWidth / (float) mVideoHeight;
			if (mVideoWidth != 0 && mVideoHeight != 0)
				setVideoLayout(mVideoLayout, mVideoAspectRatio);
		}

	};
	
	OnCompletionListener mCompletionListener = new OnCompletionListener() {

		@Override
		public void onCompletion(MediaPlayer arg0) {
			Log.d(TAG, "==============>onCompletion<===============");
			mMediaController.resetInfo();
			if(mMediaType==MediaConstant.MediaType.TYPE_MUSIC){
				episodes.clear();
			}
			if (index + 1 < episodes.size()) {
				nextVideo();
			} else {
				mCurrentState = STATE_PLAYBACK_COMPLETED;
				Toast.makeText(mContext,res.getString(R.string.playover), 500).show();
				doExitPlayer();
			}
		}
	};
	
	private Dialog errordialog;
	OnErrorListener mErrorListener = new OnErrorListener() {

		@Override
		public boolean onError(MediaPlayer mp, int framework_err, int impl_err) {
			Log.d(TAG, "onError:" + framework_err + "," + impl_err);
			if (mMediaController != null)
				mMediaController.hide();
			if (mDialog != null)
				mDialog.hide();
			mCurrentState = STATE_ERROR;
			if (mSurfaceView.getWindowToken() != null) {
				int message = framework_err == MediaPlayer.MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK ? R.string.vitamio_videoview_error_text_invalid_progressive_playback
						: R.string.vitamio_videoview_error_text_unknown;

				errordialog = new AlertDialog.Builder(mContext)
						.setTitle(R.string.vitamio_videoview_error_title)
						.setMessage(res.getString(message)
								  + res.getString(R.string.countdown))
						.setPositiveButton(R.string.vitamio_videoview_error_button,
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int whichButton) {
										_handler.removeMessages(MSG_WHAT_AUTO_EXIT);
										doExitPlayer();
										
									}
								}).setCancelable(false).create();
				errordialog.show();
				doAutoExit(5);
			}
			return true;
		}
	};

	private int watchRecordPos = -1;
	private boolean canResumePlay = true;
	OnPreparedListener mPreparedListener = new OnPreparedListener() {

		@Override
		public void onPrepared(MediaPlayer mp) {
			// TODO Auto-generated method stub
			Log.d(TAG, "==============>onPrepared<===============");
			mCurrentState = STATE_PREPARED;
			mDuration = (useHttpProxy && HttpProxy.getInstance().getType().equals("hls"))?
					HttpProxy.getInstance().getHlsDuration() : mMediaPlayer.getDuration();
			mDialog.hide();
			mMediaController.updateInfo();
			watchRecordPos = getWatchRecord(mUrl);
			if(watchRecordPos!=-1){
				if(clientControll){
					showContinueDialog(watchRecordPos);
				}else{
					showContinueDialog(watchRecordPos);
				}
				
			}else{
				play();
				if(canPauseListener!=null){
					if(continueDialog!=null&&continueDialog.isShowing())continueDialog.dismiss();
					canPauseListener.onCanPause();
				}
			}
		}
	};

	OnSeekCompleteListener mSeekCompleteListener = new OnSeekCompleteListener() {

		@Override
		public void onSeekComplete(MediaPlayer mp) {
			Log.d(TAG, "==============>onSeekComplete<===============");
			if (!isBuffering)
				mDialog.hide();
			mMediaController.updateInfo();
			isSeeking = false;
		}
	};

	private void setVideoLayout(int layout, float videoRatio) {
		LayoutParams lp = mSurfaceView.getLayoutParams();
		DisplayMetrics disp = res.getDisplayMetrics();
		int windowWidth = disp.widthPixels, windowHeight = disp.heightPixels;
		float windowRatio = windowWidth / (float) windowHeight;
		mSurfaceHeight = mVideoHeight;
		mSurfaceWidth = mVideoWidth;
		if (VIDEO_LAYOUT_ORIGIN == layout && mSurfaceWidth < windowWidth
				&& mSurfaceHeight < windowHeight) {
			lp.width = (int) (mSurfaceHeight * videoRatio);
			lp.height = (int) mSurfaceHeight;
		} else if (layout == VIDEO_LAYOUT_ZOOM) {
			lp.width = windowRatio > videoRatio ? windowWidth
					: (int) (videoRatio * windowHeight);
			lp.height = windowRatio < videoRatio ? windowHeight
					: (int) (windowWidth / videoRatio);
		} else if (layout == VIDEO_LAYOUT_STRETCH) {
			lp.width = (windowRatio < videoRatio) ? windowWidth
					: (int) (videoRatio * windowHeight);
			lp.height = (windowRatio > videoRatio) ? windowHeight
					: (int) (windowWidth / videoRatio);
		} else if (layout == VIDEO_LAYOUT_KEEP) {
			if (windowRatio > videoRatio) {
				lp.width = (int) (videoRatio * windowHeight);
				lp.height = windowHeight;
			} else {
				lp.width = windowWidth;
				lp.height = (int) (windowWidth / videoRatio);
			}
		}
		mSurfaceView.setLayoutParams(lp);
		mSurfaceHolder.setFixedSize(mSurfaceWidth, mSurfaceHeight);
		Log.d(TAG, "VIDEO: " + mVideoWidth + ":" + mVideoHeight + "="
				+ mVideoAspectRatio + ", Surface: " + mSurfaceWidth + "x"
				+ mSurfaceHeight + ", LP: " + lp.width + ":" + lp.height
				+ ", Window:" + windowWidth + ":" + windowHeight + "="
				+ windowRatio);
		mVideoLayout = layout;
	}
	
	
	private int getWatchRecord(String url) {
		// TODO Auto-generated method stub
		int continuePos = sp.getInt(url, -1);
		Log.d(TAG,"getWatchRecord:"+continuePos);//+";url:"+url
		return continuePos;
	}
	
	private void setWatchRecord(String url,int pos){
		if(url==null||url.isEmpty()||pos==0||pos==-1||mDuration==0){
			return;
		}
		if(pos*100/mDuration>98){
			editor.remove(url);
			editor.commit();
			return;
		}
		Log.d(TAG,"setWatchRecord:"+StringUtils.generateTime(pos));//+";url:"+url
		editor.putInt(url,pos);
		editor.commit();
	}
	
	public AlertDialog continueDialog;
	
	private void showContinueDialog(final int pos){
		continueDialog = new AlertDialog.Builder(mContext).create();
		continueDialog.setTitle(res.getString(R.string.pmt));
		continueDialog.setMessage(String.format(res.getString(R.string.player_continue_title), StringUtils.generateTime(pos)));
		continueDialog.setButton(Dialog.BUTTON_POSITIVE, res.getString(R.string.player_continue_yes), new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				play();
				seekTo(pos);
			}
		});
		continueDialog.setButton(Dialog.BUTTON_NEGATIVE,res.getString(R.string.player_continue_no) , new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				play();
			}
		});
		continueDialog.setOnCancelListener(new OnCancelListener() {
			
			@Override
			public void onCancel(DialogInterface dialog) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				play();
			}
		});
		continueDialog.setOnDismissListener(new OnDismissListener() {
			
			@Override
			public void onDismiss(DialogInterface dialog) {
				canResumePlay = true;
			}
		});
		if(mContainer.getWindowToken()!=null)continueDialog.show();
		canResumePlay = false;
	}
	
	private void releaseMediaPlayer() {
		if (mMediaPlayer == null)
			return;
		try {
			mCurrentState = STATE_IDLE;
			setWatchRecord(mUrl, mPosition);
			watchRecordPos = -1;
			mMediaPlayer.reset();
			mMediaPlayer.release();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		}
	}
	
	
	
	public void printfVideoInfo() {
		Log.d(TAG, "===============VideoInfo================");
		Log.d(TAG, "HttpProxy-->" + useHttpProxy);
		Log.d(TAG, "clientControll--> " + clientControll);
		Log.d(TAG, "videoInfo:name-->" + mTitle);
		Log.d(TAG, "episodes.size:" + episodes.size() + ";index:" + index);
		Log.d(TAG, "videoInfo:url-->" + mUrl);
		Log.d(TAG, "========================================");
	}

	public void setController(MATController controller) {
		// TODO Auto-generated method stub
		mMediaController = controller;
		controller.setHandler(_handler);
	}
	
	public MATController getController(){
		return mMediaController;
	}
	
	public MediaPlayer getMediaPlayer(){
		return mMediaPlayer;
	}

	public void changeData(String newTitle,String newUrl,int newMediaType) {
		// TODO Auto-generated method stub
		releaseMediaPlayer();
		mUrl = newUrl;
		mTitle = newTitle;
		mMediaType = newMediaType;
		
		printfVideoInfo();
		mMediaController.setAllowLoadVideoTask(false);
		mDialog.showMessage(res.getString(R.string.onprepare)+ newTitle);
		setForegroundByMediaType(newMediaType);
		initMediaPlayer();
		mCallback.surfaceCreated(mSurfaceHolder);
	}

	public void setForegroundByMediaType(final int mediaType) {
		if (mediaType == MediaConstant.MediaType.TYPE_MUSIC) {
			mRootView.findViewById(R.id.foreground).setBackgroundResource(R.drawable.default_bg_two);
		} else if (mediaType == MediaConstant.MediaType.TYPE_VIDEO) {
			mRootView.findViewById(R.id.foreground).setBackgroundResource(0);
		}
	}

	
	private int currentProgress = 0;
	public void doSeek(int currentProgress) {
		// TODO Auto-generated method stub
		this.currentProgress = currentProgress;
		_handler.postDelayed(seekRunnable, 1000);
	}
	
	private boolean isSeeking = false;
	private Runnable seekRunnable = new Runnable() {

		@Override
		public void run() {
			if (isInPlaybackState()){
				isSeeking = true;
				int newposition = mDuration * currentProgress / 100;
				seekTo(newposition);
			}
		}
	};

	public void checkSeeking(int currentProgress) {
		// TODO Auto-generated method stub
		this.currentProgress = currentProgress;
		if (isSeeking) {
			_handler.removeCallbacks(seekRunnable);
			isSeeking = false;
		} else {
			_handler.postDelayed(seekRunnable, 2000);
		}
	}

	public void nextVideo() {
		// TODO Auto-generated method stub
		if (index + 1 >= episodes.size())
			return;
		if (isLoading) {
			_handler.removeCallbacks(loadVideoTask);
			isLoading = false;
		}
		index += 1;
		startVideoTask();
	}
	
	public void preVideo() {
		if (index - 1 < 0)
			return;
		if (isLoading) {
			_handler.removeCallbacks(loadVideoTask);
			isLoading = false;
		}
		index -= 1;
		startVideoTask();
	}
	
	public void startVideoTask() {
		mTitle = movie.getName() + String.format(res.getString(R.string.episode), index+1);//"(" + (index + 1) + ")";
		mMediaController.setFilmName(mTitle);
		_handler.postDelayed(loadVideoTask, 200);
	}

	public void initData(Intent i) {
		// TODO Auto-generated method stub
		mMediaType = i.getIntExtra("mediaType",MediaConstant.MediaType.TYPE_VIDEO);
		String clientIP = i.getStringExtra("clientIP");
		if (clientIP != null) {
			clientControll = true;
			mTitle = i.getStringExtra("title");
			mUrl = i.getStringExtra("url");
			Log.d(TAG, "clientIP:" + clientIP);
		} else {
			movie = (Movie) i.getSerializableExtra("movie");
			String category = "��Ӱ";
//			String category = movie.getCategory();
//			mTitle = movie.getName();
//			episodes = movie.getSTBPlayerUrls();
//			if (category.equals(res.getString(R.string.movie))&& episodes.size() == 1) {
//				mUrl = movie.getPc_flv_high_url();
//				if (mUrl == null || "null".equals(mUrl) || "".equals(mUrl))
//					mUrl = movie.getIpad_hls_url();
//				if (mUrl == null || "null".equals(mUrl) || "".equals(mUrl))
//					mUrl = movie.getIpad_mp4_url();
//			} else {
//				index = i.getIntExtra("index", 0);
//				if (episodes == null) {
//					Log.w(TAG, "url list is null");
//					doExitPlayer();
//					return;
//				}
//				mUrl = episodes.get(index);
//				mTitle = movie.getName() + String.format(res.getString(R.string.episode), index+1);//"(" + (index + 1) + ")";
//			}
			String path = movie.getDownload_path();
			mUrl =HttpRequest.URL_QUERY_DOWNLOAD_URL+path;
			mTitle = movie.getName();
		}
		setForegroundByMediaType(mMediaType);
		printfVideoInfo();
	}

	
	public interface CanPauseListener{
		public void onCanPause();
	}
	private CanPauseListener canPauseListener;
	
	public void setOnCanPauseListener(CanPauseListener callback){
		this.canPauseListener = callback;
	}

	private ViewGroup mContainer;
	public void onCreate(Activity ownerActivity, ViewGroup container,Intent intent) {
		mContainer = container;
		setOwnerActivity(ownerActivity);
		container.addView(getRootView());
		if(mCurrentState==STATE_IDLE){
			initData(intent);
			init();
		}
	}

	public void onResume() {
		canPauseListener = null;
		if(isInPlaybackState() && canResumePlay){
			play();
		}
	}

	public void onPause() {
		//when the player is prepared..
		if(mCurrentState==STATE_PLAYING){
			pause();
		}
		//when the player is not prepared..
		setOnCanPauseListener(new CanPauseListener() {
			@Override
			public void onCanPause() {
				pause();
				canPauseListener = null;
			}
		});
	}

	
	public void onDestory(){
		if(mContainer!=null)mContainer.removeAllViews();
		currentProgress = 0;
		mDuration = 0;
		mPosition = 0;
		canPauseListener = null;
		mInstance = null;
		if(continueDialog!=null&&continueDialog.isShowing())continueDialog.dismiss();
	}

	public void seekTo(int pos) {
		if (isInPlaybackState()) {
			if (!mDialog.isShowing()) {
				mDialog.showMessage(res.getString(R.string.loading));
			}
			mMediaPlayer.seekTo(pos);
		}
	}

	public boolean isPlaying() {
		// TODO Auto-generated method stub
		if(isInPlaybackState()){
			return mMediaPlayer.isPlaying();
		}
		return false;
	}

	public void pause() {
		// TODO Auto-generated method stub
		if(isInPlaybackState()){
			mMediaPlayer.pause();
			mCurrentState = STATE_PAUSED;
			mMediaController.syncStatus(MediaConstant.PlayStatus.ISPAUSING);
		}
	}

	public int getDuration() {
		// TODO Auto-generated method stub
		if(isInPlaybackState()){
			return mMediaPlayer.getDuration();
		}
		return 0;
	}

	public int getCurrentPosition() {
		// TODO Auto-generated method stub
		if(isInPlaybackState()){
			return mMediaPlayer.getCurrentPosition();
		}
		return 0;
	}

	public void dismissContinueDialog() {
		if(continueDialog!=null&&continueDialog.isShowing())continueDialog.dismiss();
	}

	public void resume() {
		if(isInPlaybackState()){
			play();
		}
	}

	public void play() {
		// TODO Auto-generated method stub
		if (isInPlaybackState()) {
			mMediaPlayer.start();
			mCurrentState = STATE_PLAYING;
			mMediaController.syncStatus(MediaConstant.PlayStatus.ISPLAYING);
			if (mDialog != null && mDialog.isShowing())mDialog.dismiss();
		}
	}
}
