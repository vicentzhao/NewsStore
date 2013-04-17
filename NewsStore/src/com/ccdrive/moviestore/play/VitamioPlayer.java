package com.ccdrive.moviestore.play;

import io.vov.utils.StringUtils;
import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.MediaPlayer.OnBufferingUpdateListener;
import io.vov.vitamio.MediaPlayer.OnCompletionListener;
import io.vov.vitamio.MediaPlayer.OnErrorListener;
import io.vov.vitamio.MediaPlayer.OnInfoListener;
import io.vov.vitamio.MediaPlayer.OnPreparedListener;
import io.vov.vitamio.MediaPlayer.OnSeekCompleteListener;
import io.vov.vitamio.MediaPlayer.OnVideoSizeChangedListener;

import java.io.IOException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.ccdrive.moviestore.R;
import com.ccdrive.moviestore.bean.Movie;
import com.ccdrive.moviestore.http.HttpRequest;

public class VitamioPlayer extends Activity {
	public static final String TAG = "VitamioPlayer";

	private MATController mMediaController;
	private MediaPlayer mMediaPlayer;
	private SurfaceView mSurfaceView;
	private SurfaceHolder mSurfaceHolder;

	private int mVideoWidth = 0;
	private int mVideoHeight = 0;
	private int mSurfaceWidth;
	private int mSurfaceHeight;

	private String title;
	private int currentPosition = 0;

	private int mCurrentState = STATE_IDLE;
	private int mTargetState = STATE_IDLE;

	private float mAspectRatio = 0;
	private float mVideoAspectRatio;

	private int mVideoLayout = VIDEO_LAYOUT_KEEP;

	public static final int VIDEO_LAYOUT_ORIGIN = 0;// 
	public static final int VIDEO_LAYOUT_SCALE = 1;//
	public static final int VIDEO_LAYOUT_STRETCH = 2;//
	public static final int VIDEO_LAYOUT_ZOOM = 3;//
	public static final int VIDEO_LAYOUT_KEEP = 4;// 

	private boolean clientControll = false;
	// -------------------------------------------
	// -------------------------------------------

	private static final int MSG_WHAT_HIDE_CONTROLLER = 1;
	private static final int MSG_WHAT_UPDATE_CONTROLLER_INFO = 2;
	private static final int MSG_WHAT_RESET_EXIT_NUM = 3;
	private static final int MSG_WHAT_AUTO_EXIT = 4;

	private static final int BUFFER_SIZE = 2 * 1024 * 1024;

	private static final int STATE_ERROR = -1;
	private static final int STATE_IDLE = 0;
	private static final int STATE_PREPARING = 1;
	private static final int STATE_PREPARED = 2;
	private static final int STATE_PLAYING = 3;
	private static final int STATE_PAUSED = 4;
	private static final int STATE_PLAYBACK_COMPLETED = 5;
	private static final int STATE_SUSPEND = 6;

	private int mCurrentBufferPercentage;

	private MATDialog dialog;
	private String url;
	private int mediaType = MediaConstant.MediaType.TYPE_VIDEO;

	private AudioManager mAudioManager;

	private SharedPreferences sp;
	private Editor editor;
	private Resources res;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// TODO:onCreate
		setContentView(R.layout.player);
		sp = getSharedPreferences("Player", MODE_PRIVATE);
		editor = sp.edit();
		res = getResources();

		initUI();
		initData();
		initMediaPlayer();
		initSurfaceView();
//		updateCurrentStatus(true, url, title);
	}

	public void printVideoInfo() {
		Log.d(TAG, "===============VideoInfo================");
		Log.d(TAG, "clientControll--> " + clientControll);
		Log.d(TAG, "videoInfo:name-->" + title);
		Log.d(TAG, "videoInfo:url-->" + url);
		Log.d(TAG, "========================================");
	}

//	public void updateCurrentStatus(boolean isPlaying, String url, String title) {
//		DeviceProxy.getInstance().setPlaying(isPlaying);
//		DeviceProxy.getInstance().setCurrentPlayingUrl(url);
//		DeviceProxy.getInstance().setCurrentPlayTitle(title);
//	}

	private int index = 0;
//	private List<String> list = new ArrayList<String>();
	private Movie movie;

	public void initData() {
		Intent i = getIntent();
		mediaType = i.getIntExtra("mediaType",
				MediaConstant.MediaType.TYPE_VIDEO);
		String clientIP = i.getStringExtra("clientIP");
		if (clientIP != null) {
			clientControll = true;
			mMediaController.allowLoadVideoTask = false;
			title = i.getStringExtra("title");
			url = i.getStringExtra("url");
			Log.d(TAG, "clientIP-->" + clientIP);
		} else {
			movie = (Movie) i.getSerializableExtra("movie");
//			String category = movie.getCategory();  //�жϵ�Ӱ���
			String category="影视";
			title = movie.getName();
//			list = movie.getSTBPlayerUrls();   //�ж��Ƿ�����һ��
//			if (list.size() > 1)
//				mMediaController.allowLoadVideoTask = true;
			if (category.equals(res.getString(R.string.movie))
					) {
//				url = movie.getPc_flv_high_url();
				url=HttpRequest.URL_QUERY_DOWNLOAD_URL+movie.getDownload_path();
				mMediaController.allowLoadVideoTask = false;
			} else {
				index = i.getIntExtra("index", 0);
//				Log.i(TAG, "list.size" + list.size() + "--index:" + index);
//				if (list == null) {
//					Log.w(TAG, "url list is null");
//					finish();
//					return;
//				}
				 url=HttpRequest.URL_QUERY_DOWNLOAD_URL+movie.getDownload_path();
				title = movie.getName() + "(" + (index + 1) + ")";
			}
		}
		if (mediaType == MediaConstant.MediaType.TYPE_MUSIC) {
			findViewById(R.id.foreground).setBackgroundResource(R.drawable.default_bg_two);
		}
		printVideoInfo();
		mMediaController.setFilmName(title);
	}

	boolean isLoading = false;
	Runnable loadVideoTask = new Runnable() {

		@Override
		public void run() {
			isLoading = true;
			updateUrl();
			isLoading = false;
		}
	};

	private void updateUrl() {
//		Log.d(TAG, "updateUrl ->index:" + index + ";list.size:" + list.size());
		_handler.removeMessages(MSG_WHAT_UPDATE_CONTROLLER_INFO);
//		url = list.get(index);
		url=movie.getDownload_path();

		dialog.setMessage(res.getString(R.string.onprepare) + title);
		dialog.show();
		release(false);
		initMediaPlayer();
//		updateCurrentStatus(true, url, title);
		callback.surfaceCreated(mSurfaceHolder);
	}

	public void initUI() {
		mMediaController = new MATController(this);
//		DeviceProxy.getInstance().setController(mMediaController);
		initVolume();
		dialog = new MATDialog(this, R.style.dialog);
		dialog.setMessage(res.getString(R.string.pleasewait));
		dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_TOAST);
	}

	// -------------------------------------------
//	private void sendMessageToClient(byte[] data) {
//		if (!clientControll)
//			return;
//		synchronized (DeviceProxy.getInstance().getSyncClients()) {
//			List<MscpClient> clients = DeviceProxy.getInstance().getSyncClients();
//			if (clients.size() > 0) {
//				for (int i = 0; i < clients.size(); i++) {
//					clients.get(i).sendTo(data, data.length);
//				}
//			} else {
//				Log.w(TAG,
//						"can't sendMessageToClient ,because DeviceProxy.getInstance().playClient == null");
//			}
//		}
//	}

	Handler _handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case MSG_WHAT_HIDE_CONTROLLER:
				mMediaController.hide();
				break;
			case MSG_WHAT_UPDATE_CONTROLLER_INFO:
				updateControllerInfo();
				mMediaController.updateInfo();
				break;
			case MSG_WHAT_RESET_EXIT_NUM:
				exit_num = 0;
				break;
			case MSG_WHAT_AUTO_EXIT:
				doAutoExit(countdown--);
				break;
			default:
				break;
			}
		}
	};

	int countdown = 5;

	private void doAutoExit(int s) {
		if (s <= 1) {
			if (errordialog != null && errordialog.isShowing()) {
				errordialog.dismiss();
			}
			finish();
			return;
		}
		_handler.sendEmptyMessageDelayed(MSG_WHAT_AUTO_EXIT, 1000);
	}

	private int position;
	private int duration;

	/**
	 * 1�����½���� 2������ʱ�� 3�����������ٶ�
	 */
	protected void updateControllerInfo() {
		// TODO Auto-generated method stub
		try {
			if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
				position = (int) mMediaPlayer.getCurrentPosition();
				if (duration > 0) {
					String txt = stringForTime(position) + "/"
							+ stringForTime(duration);
					mMediaController.time.setText(txt);
					mMediaController.seekBar.setProgress(100 * position
							/ duration);
//					sendMessageToClient(CMDConstructor.constructPlayerStatusResp(
//							title, url, position,
//							duration,mMediaController.status,
//									mMediaController.isMute));
					// controller.seekBar.setSecondaryProgress(???+100*position/duration);//???=100*blocksize*_progress/totalsize;
				}
				return;
			}
		} catch (Exception e) {
			finish();
		}
	}

	private String stringForTime(int timeMs) {
		int totalSeconds = timeMs / 1000;
		int seconds = totalSeconds % 60;
		int minutes = (totalSeconds / 60) % 60;
		int hours = totalSeconds / 3600;
		String s = seconds < 10 ? ("0" + seconds) : seconds + "";
		String m = minutes < 10 ? ("0" + minutes) : minutes + "";
		String h = hours < 10 ? ("0" + hours) : hours + "";
		return h + ":" + m + ":" + s;
	}

	void initSurfaceView() {
		mSurfaceView = (SurfaceView) findViewById(R.id.surfaceView);
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
		mSurfaceHolder.addCallback(callback);
	}

	private void initMediaPlayer() {
		if (!io.vov.vitamio.LibsChecker.checkVitamioLibs(this))
			return;
		Intent i = new Intent("com.android.music.musicservicecommand");
		i.putExtra("command", "pause");
		sendBroadcast(i);
		if (mSurfaceView != null) {
			mSurfaceView.invalidate();
			mSurfaceView.destroyDrawingCache();
		}
		mMediaPlayer = new MediaPlayer(this);
		mMediaPlayer.setOnCompletionListener(mCompletionListener);
		mMediaPlayer.setOnErrorListener(mErrorListener);
		mMediaPlayer.setOnInfoListener(mInfoListener);
		mMediaPlayer.setOnPreparedListener(mPreparedListener);
		mMediaPlayer.setOnSeekCompleteListener(mSeekCompleteListener);
		mMediaPlayer.setOnVideoSizeChangedListener(mSizeChangedListener);
		mMediaPlayer.setOnBufferingUpdateListener(mBufferingUpdateListener);
		mMediaPlayer.setDisplay(mSurfaceHolder);
		mMediaPlayer.setBufferSize(BUFFER_SIZE);
		mMediaPlayer.setScreenOnWhilePlaying(true);
		mMediaPlayer.setVideoQuality(MediaPlayer.VIDEOQUALITY_HIGH);
		dialog.setMessage(res.getString(R.string.onprepare) + title);
		dialog.show();
	}

	SurfaceHolder.Callback callback = new Callback() {
		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int w,
				int h) {
			mSurfaceWidth = w;
			mSurfaceHeight = h;
			boolean isValidState = (mTargetState == STATE_PLAYING);
			boolean hasValidSize = (mVideoWidth == w && mVideoHeight == h);
			if (mMediaPlayer != null && isValidState && hasValidSize) {
				start();
				if (mMediaController != null) {
					if (mMediaController.isShowing())
						mMediaController.hide();
					mMediaController.show();
				}
			}
		}

		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			Log.d(TAG, "==============>surfaceCreated<===============");
			if (mMediaPlayer == null) {
				finish();
				return;
			}
			try {
				mSurfaceHolder = holder;
				mMediaPlayer.setDisplay(mSurfaceHolder);
				mMediaPlayer.setDataSource(url);
				mMediaPlayer.prepareAsync();
//				sendMessageToClient(CMDConstructor.constructPlayerStatusResp(
//						title, url, position, duration,
//						MediaConstant.PlayStatus.ISPLAYING,mMediaController.isMute));
				mCurrentState = STATE_PREPARING;
			} catch (IllegalStateException e) {
				Log.e(TAG, e.getMessage());
				mCurrentState = STATE_ERROR;
				mTargetState = STATE_ERROR;
				mErrorListener.onError(mMediaPlayer,
						MediaPlayer.MEDIA_ERROR_UNKNOWN, 0);
				return;
			} catch (IllegalArgumentException e) {
				Log.e(TAG, e.getMessage());
				mCurrentState = STATE_ERROR;
				mTargetState = STATE_ERROR;
				mErrorListener.onError(mMediaPlayer,
						MediaPlayer.MEDIA_ERROR_UNKNOWN, 0);
				return;
			} catch (IOException e) {
				Log.e(TAG, e.getMessage());
				mCurrentState = STATE_ERROR;
				mTargetState = STATE_ERROR;
				mErrorListener.onError(mMediaPlayer,
						MediaPlayer.MEDIA_ERROR_UNKNOWN, 0);
				return;
			}

		}
		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {
			Log.d(TAG, "==============>surfaceDestroyed<===============");
			mSurfaceHolder = null;
			if (mMediaController != null)
				mMediaController.hide();
			if (mCurrentState != STATE_SUSPEND)
				release(true);
		}
	};

	private void release(boolean cleartargetstate) {

		if (mMediaPlayer != null) {
			setWatchRecord(url, mMediaController.getCurrentPosition());
			mMediaPlayer.reset();
			mMediaPlayer.release();
			mMediaPlayer = null;
			mCurrentState = STATE_IDLE;
			if (cleartargetstate)
				mTargetState = STATE_IDLE;
		}
	}

	OnVideoSizeChangedListener mSizeChangedListener = new OnVideoSizeChangedListener() {
		public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
			Log.d(TAG, "onVideoSizeChanged:width" + width + ";height:" + height);
			mVideoWidth = mp.getVideoWidth();
			mVideoHeight = mp.getVideoHeight();
			mVideoAspectRatio = mp.getVideoAspectRatio();
			if (mVideoWidth != 0 && mVideoHeight != 0)
				setVideoLayout(mVideoLayout, mAspectRatio);
		}
	};

	private OnSeekCompleteListener mSeekCompleteListener = new OnSeekCompleteListener() {
		@Override
		public void onSeekComplete(MediaPlayer mp) {
			Log.d(TAG, "==============>onSeekComplete<===============");
			mMediaController.updateInfo();
			mMediaController.isSeeking = false;
		}
	};

	public long getCurrentPosition() {
		if (isInPlaybackState())
			return mMediaPlayer.getCurrentPosition();
		return 0;
	}

	public boolean isPlaying() {
		return isInPlaybackState() && mMediaPlayer.isPlaying();
	}

	OnPreparedListener mPreparedListener = new OnPreparedListener() {
		public void onPrepared(MediaPlayer mp) {
			mMediaController.updateInfo();
			duration = (int) mMediaPlayer.getDuration();
			mCurrentState = STATE_PREPARED;
			mTargetState = STATE_PLAYING;
			mVideoWidth = mp.getVideoWidth();
			mVideoHeight = mp.getVideoHeight();
			mVideoAspectRatio = mp.getVideoAspectRatio();

			int pos = getWatchRecord(url);
			if (pos != -1) {
				showContinueDialog(pos);
			}
			if (mVideoWidth != 0 && mVideoHeight != 0) {
				setVideoLayout(mVideoLayout, mAspectRatio);
				if (mSurfaceWidth == mVideoWidth
						&& mSurfaceHeight == mVideoHeight) {
					if (mTargetState == STATE_PLAYING) {
						start();
						if (mMediaController != null)
							mMediaController.show();
					}
				}
			} else if (mTargetState == STATE_PLAYING) {
				start();
			}
		}
	};

	public void start() {
		mMediaController.play();
		mCurrentState = STATE_PLAYING;
		mTargetState = STATE_PLAYING;
	}

	public void setVideoLayout(int layout, float aspectRatio) {
		LayoutParams lp = mSurfaceView.getLayoutParams();
		DisplayMetrics disp = res.getDisplayMetrics();
		int windowWidth = disp.widthPixels, windowHeight = disp.heightPixels;
		float windowRatio = windowWidth / (float) windowHeight;
		float videoRatio = aspectRatio <= 0.01f ? mVideoAspectRatio
				: aspectRatio;
		mSurfaceHeight = mVideoHeight;
		mSurfaceWidth = mVideoWidth;
		if (VIDEO_LAYOUT_ORIGIN == layout && mSurfaceWidth < windowWidth
				&& mSurfaceHeight < windowHeight) {
			lp.width = (int) (mSurfaceHeight * videoRatio);
			lp.height = mSurfaceHeight;
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
			lp.width = windowRatio > videoRatio ? (int) (videoRatio * windowHeight)
					: windowWidth;
			lp.height = windowRatio > videoRatio ? windowHeight
					: (int) (windowWidth / videoRatio);
		}
		mSurfaceView.setLayoutParams(lp);
		mSurfaceHolder.setFixedSize(mSurfaceWidth, mSurfaceHeight);
		Log.d(TAG, "VIDEO: " + mVideoWidth + "x" + mVideoHeight + "x"
				+ mVideoAspectRatio + ", Surface: " + mSurfaceWidth + "x"
				+ mSurfaceHeight + ", LP: " + lp.width + "x" + lp.height
				+ ", Window: +" + windowWidth + "x" + windowHeight + "x"
				+ windowRatio);
		mVideoLayout = layout;
		mAspectRatio = aspectRatio;
	}

	protected boolean isInPlaybackState() {
		return (mMediaPlayer != null && mCurrentState != STATE_ERROR
				&& mCurrentState != STATE_IDLE && mCurrentState != STATE_PREPARING);
	}

	@Override
	protected void onPause() {
		Log.d(TAG, "==============>onPause<===============");
		if (mMediaController != null && mMediaController.isShowing())
			mMediaController.hide();
		finish();
		super.onPause();
	}

	private OnBufferingUpdateListener mBufferingUpdateListener = new OnBufferingUpdateListener() {
		public void onBufferingUpdate(MediaPlayer mp, int percent) {
			// Log.i(TAG,"==========>onBufferingUpdate  percent:"+percent+" <==========");
			if (mMediaPlayer.isPlaying())
				mMediaController.pause();
			mCurrentBufferPercentage = percent;
			dialog.setMessage(res.getString(R.string.loading));
			dialog.setProgress(mCurrentBufferPercentage);
		}
	};

	private OnInfoListener mInfoListener = new OnInfoListener() {
		@Override
		public boolean onInfo(MediaPlayer mp, int what, int extra) {
			if (what == MediaPlayer.MEDIA_INFO_BUFFERING_START) {
				Log.i(TAG, "onInfo MEDIA_INFO_BUFFERING_START");
				mMediaController.pause();
				dialog.setMessage(res.getString(R.string.loading));
				dialog.show();
			} else if (what == MediaPlayer.MEDIA_INFO_BUFFERING_END) {
				Log.i(TAG, "onInfo MEDIA_INFO_BUFFERING_END");
				mMediaController.play();
				dialog.dismiss();
				dialog.setProgress(0);
			}
			return true;
		}
	};

	private Dialog errordialog;
	private OnErrorListener mErrorListener = new OnErrorListener() {
		public boolean onError(MediaPlayer mp, int framework_err, int impl_err) {
			Log.d(TAG, "onError:" + framework_err + "," + impl_err);
			mCurrentState = STATE_ERROR;
			mTargetState = STATE_ERROR;
			if (mMediaController != null)
				mMediaController.hide();
			if (dialog != null && dialog.isShowing())
				dialog.dismiss();
			if (mSurfaceView.getWindowToken() != null) {
				int message = framework_err == MediaPlayer.MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK ? R.string.vitamio_videoview_error_text_invalid_progressive_playback
						: R.string.vitamio_videoview_error_text_unknown;

				errordialog = new AlertDialog.Builder(VitamioPlayer.this)
						.setTitle(R.string.vitamio_videoview_error_title)
						.setMessage(
								res.getString(message)
										+ res.getString(R.string.countdown))
						.setPositiveButton(
								R.string.vitamio_videoview_error_button,
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int whichButton) {
										_handler.removeMessages(MSG_WHAT_AUTO_EXIT);
										finish();
									}
								}).setCancelable(false).show();
				_handler.sendEmptyMessageDelayed(MSG_WHAT_AUTO_EXIT, 2000);
			}
			return true;
		}
	};

	private OnCompletionListener mCompletionListener = new OnCompletionListener() {
		public void onCompletion(MediaPlayer mp) {
			Log.d(TAG, "==============>onCompletion<===============");
//			if (index + 1 < list.size()) {
//				index += 1;
//				mMediaController.startVideoTask();
//			} else {
//				mCurrentState = STATE_PLAYBACK_COMPLETED;
//				mTargetState = STATE_PLAYBACK_COMPLETED;
//				Toast.makeText(VitamioPlayer.this,
//						res.getString(R.string.playover), 500).show();
//				finish();
//			}
			mCurrentState = STATE_PLAYBACK_COMPLETED;
			mTargetState = STATE_PLAYBACK_COMPLETED;
			Toast.makeText(VitamioPlayer.this,
					res.getString(R.string.playover), 500).show();
			finish();
		}
	};

	int exit_num = 0;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_DPAD_UP:
			if (!mMediaController.isShowing()) {
				mMediaController.show();
				return true;
			}
			break;
		case KeyEvent.KEYCODE_DPAD_DOWN:
			if (!mMediaController.isShowing()) {
				mMediaController.show();
				return true;
			}
			break;
		case KeyEvent.KEYCODE_DPAD_CENTER:
			if (!mMediaController.isShowing()) {
				mMediaController.show();
				return true;
			}
			break;
		case KeyEvent.KEYCODE_DPAD_LEFT:
			if (!mMediaController.isShowing()) {
				mMediaController.show();
				return true;
			}
			break;
		case KeyEvent.KEYCODE_DPAD_RIGHT:
			if (!mMediaController.isShowing()) {
				mMediaController.show();
				return true;
			}
			break;
		case KeyEvent.KEYCODE_BACK:
			if (exit_num == 0) {
				exit_num += 1;
				Toast.makeText(this, res.getString(R.string.do_again_exit), 1)
						.show();
				_handler.sendEmptyMessageDelayed(MSG_WHAT_RESET_EXIT_NUM, 3000);
			} else
				finish();
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onStop() {
		Log.d(TAG, "==============>onStop<===============");
		if (dialog.isShowing())
			dialog.dismiss();
//		DeviceProxy.getInstance().setPlaying(false);
//		DeviceProxy.getInstance().setController(null);
//		if (clientControll)
//			sendMessageToClient(CMDConstructor.constructPlayerStatusResp(title,
//					url, position, duration, MediaConstant.PlayStatus.ISCLOSED,mMediaController.isMute));
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		release(true);
		super.onDestroy();
	}

	public class MATController extends BaseController {
		private static final int HIDE_CONTROLLER_INTERVAL = 8000;

		private Activity context;
		private Button play_pause, next, prev, stop, sound;//ff, fb, 
		private TextView filmName, time;
		private SeekBar seekBar, voiceBar;

		private int currentProgress;
		private boolean isSeeking = false;
		boolean isMute = false;
		private int status = MediaConstant.PlayStatus.ISPLAYING;
		int focusViewId = 0;

		private boolean allowLoadVideoTask = false;

		public MATController(Activity context) {
			super();
			this.context = context;
			initPopWindow();
		}

		
		private PopupWindow popwindow;
		private View controllerView;
		
		public boolean isShowing() {
			return popwindow.isShowing();
		}

		public void show() {
			if(mSurfaceView.getWindowToken()==null)return;
			int[] location = new int[2];
			mSurfaceView.getLocationOnScreen(location);
			Rect anchorRect = new Rect(location[0], location[1], location[0] + mSurfaceView.getWidth(), location[1] + mSurfaceView.getHeight());
			popwindow.showAtLocation(mSurfaceView, Gravity.NO_GRAVITY, anchorRect.left, anchorRect.bottom);
			if (focusViewId == 0) {
				play_pause.requestFocus();
			} else {
				controllerView.findViewById(focusViewId).requestFocus();
			}
			_handler.sendEmptyMessageDelayed(MSG_WHAT_HIDE_CONTROLLER,HIDE_CONTROLLER_INTERVAL);
		}

		public void hide() {
			if(popwindow.isShowing()&&mSurfaceView.getWindowToken()!=null)popwindow.dismiss();
			_handler.removeMessages(MSG_WHAT_HIDE_CONTROLLER);
		}

		private void initPopWindow() {
			// TODO Auto-generated method stub
			controllerView = LayoutInflater.from(context).inflate(R.layout.player_controller, null);
			initControllerView();
			popwindow = new PopupWindow(controllerView,-1,-2);
			popwindow.setFocusable(true);
			controllerView.setFocusableInTouchMode(true);
			popwindow.setAnimationStyle(R.style.AnimationControllerFade);
			controllerView.setOnTouchListener(new OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					// TODO Auto-generated method stub
					hide();
					return false;
				}
			});
			controllerView.setOnKeyListener(new OnKeyListener() {
				
				@Override
				public boolean onKey(View v, int keyCode, KeyEvent event) {
					// TODO Auto-generated method stub
					switch (keyCode) {
					case KeyEvent.KEYCODE_BACK:
						hide();
						return true;
					default:
						break;
					}
					return false;
				}
			});
		}
		
		public void resetInfo() {
			time.setText("00:00:00/00:00:00");
			seekBar.setProgress(0);
			_handler.removeMessages(MSG_WHAT_UPDATE_CONTROLLER_INFO);
		}

		public void initControllerView() {
			// TODO Auto-generated method stub
			play_pause = (Button) controllerView.findViewById(R.id.controller_play_pause);
			next = (Button) controllerView.findViewById(R.id.controller_next);
			prev = (Button) controllerView.findViewById(R.id.controller_prev);
			stop = (Button) controllerView.findViewById(R.id.controller_stop);
			sound = (Button) controllerView.findViewById(R.id.controller_silence);

			filmName = (TextView) controllerView.findViewById(R.id.controller_filename);
			time = (TextView) controllerView.findViewById(R.id.controller_process);
			seekBar = (SeekBar) controllerView.findViewById(R.id.controller_seekbar);
			voiceBar = (SeekBar) controllerView.findViewById(R.id.controller_voicebar);
			// ------------------------------------------------------------------------
			ViewGroup infoBar = (ViewGroup) controllerView.findViewById(R.id.infoBar);
			infoBar.setVisibility(View.INVISIBLE);
			seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

				@Override
				public void onStopTrackingTouch(SeekBar seekBar) {
					Log.i(TAG, "controller.seekbar onStop");
					if(isInPlaybackState()){
						_handler.postDelayed(seekRunnable, 1000);
					}
				}

				@Override
				public void onStartTrackingTouch(SeekBar seekBar) {
					Log.i(TAG, "controller.seekbar onStart");
				}

				@Override
				public void onProgressChanged(SeekBar seekBar, int progress,
						boolean fromUser) {

					if (fromUser && isInPlaybackState()) {
						focusViewId = R.id.controller_seekbar;
						Log.i(TAG, "controller.seekBar  onChanged-->"+ progress);
						currentProgress = progress;
						_handler.removeMessages(MSG_WHAT_UPDATE_CONTROLLER_INFO);

						long newposition = duration * currentProgress / 100;
						String time = StringUtils.generateTime(newposition)
								+ "/" + StringUtils.generateTime(duration);
						mMediaController.time.setText(time);
					}
				}
			});

			seekBar.setOnKeyListener(new OnKeyListener() {

				@Override
				public boolean onKey(View view, int i, KeyEvent keyevent) {
					if (!isInPlaybackState())
						return false;
					focusViewId = R.id.controller_seekbar;
					if (keyevent.getAction() == KeyEvent.ACTION_UP) {
						if (keyevent.getKeyCode() == KeyEvent.KEYCODE_DPAD_LEFT
								|| keyevent.getKeyCode() == KeyEvent.KEYCODE_DPAD_RIGHT) {
							if (isSeeking) {
								_handler.removeCallbacks(seekRunnable);
								isSeeking = false;
							} else {
								_handler.postDelayed(seekRunnable, 2000);
							}
						}else if(keyevent.getKeyCode()==KeyEvent.KEYCODE_BACK){
							hide();
						}
					}
					return false;
				}
			});

			voiceBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

				@Override
				public void onStopTrackingTouch(SeekBar seekBar) {

				}

				@Override
				public void onStartTrackingTouch(SeekBar seekBar) {

				}

				@Override
				public void onProgressChanged(SeekBar seekBar, int progress,
						boolean fromUser) {
					if (fromUser) {
						focusViewId = R.id.controller_voicebar;
					}
					changeVolume(progress);
				}
			});

			play_pause.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					focusViewId = v.getId();
					if (mMediaPlayer.isPlaying()) {
						pause();
					} else {
						play();
					}
				}
			});
			next.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					focusViewId = v.getId();
					if (allowLoadVideoTask) {
						resetInfo();
//						nextVideo();
					}
				}

//				public void nextVideo() {
//					if (index + 1 >= list.size())
//						return;
//					if (isLoading) {
//						_handler.removeCallbacks(loadVideoTask);
//						isLoading = false;
//					}
//					index += 1;
//					startVideoTask();
//				}
			});
			prev.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					focusViewId = v.getId();
					if (allowLoadVideoTask) {
						resetInfo();
						preVideo();
					}
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

			});

			stop.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					focusViewId = v.getId();
					hide();
					finish();
				}
			});
			sound.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					focusViewId = v.getId();
					changeRingerMode();
				}
			});

		}

		public void startVideoTask() {
			title = movie.getName() + "(" + (index + 1) + ")";
			filmName.setText(title);
			_handler.postDelayed(loadVideoTask, 200);
		}

		public void changeVolume(int voice) {
			// TODO Auto-generated method stub
			if (clientControll)
				show();
			if (mAudioManager != null) {
				voiceBar.setProgress(voice);
				mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, voice,
						0);
			}
		}

		private Runnable seekRunnable = new Runnable() {

			@Override
			public void run() {
				if (isInPlaybackState()) {
					isSeeking = true;
					mMediaPlayer.seekTo(duration * currentProgress / 100);
				}
			}
		};

//		public void FF() {
//			position += FF_FB_STEP_SIZE;
//			_handler.removeMessages(MSG_WHAT_UPDATE_CONTROLLER_INFO);
//			seekTo(position);
//		}
//
//		public void FB() {
//			position -= FF_FB_STEP_SIZE;
//			_handler.removeMessages(MSG_WHAT_UPDATE_CONTROLLER_INFO);
//			seekTo(position);
//		}

		public void changeRingerMode() {
			show();
			if (isMute) {// the current status is mute ,set it to sound.
				isMute = false;
				sound.setBackgroundResource(R.drawable.player_sound_selector);
				mAudioManager.setStreamMute(AudioManager.STREAM_MUSIC, false);
			} else {
				isMute = true;
				sound.setBackgroundResource(R.drawable.player_mute_selector);
				mAudioManager.setStreamMute(AudioManager.STREAM_MUSIC, true);
			}
		}

		public void setFilmName(String title) {
			filmName.setText(res.getString(R.string.playing) + title);
		}

		public void updateInfo() {
			_handler.sendEmptyMessageDelayed(MSG_WHAT_UPDATE_CONTROLLER_INFO,
					1000);
		}

		@Override
		public int getCurrentPosition() {
			try {
				if (mMediaPlayer != null) {
					if (mMediaPlayer.isPlaying()) {
						currentPosition = (int) mMediaPlayer
								.getCurrentPosition();
						return currentPosition;
					} else {
						return currentPosition;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				Log.d(TAG, "mediaPlayer.getCurrentPosition() �����ˣ�");
				finish();
			}
			return 0;
		}

		@Override
		public int getDuration() {
			if (mMediaPlayer != null) {
				if (mMediaPlayer.isPlaying()) {
					return (int) mMediaPlayer.getDuration();
				} else {
					return 0;
				}
			}
			return 0;
		}

		@Override
		public boolean isPlaying() {
			try {
				if (mMediaPlayer != null) {
					return mMediaPlayer.isPlaying();
				}
			} catch (Exception e) {
				Log.d(TAG, "mediaPlayer.isPlaying() ");
				e.printStackTrace();
				finish();
			}
			return false;
		}

		@Override
		public void pause() {
			status = MediaConstant.PlayStatus.ISPAUSING;
			play_pause.setBackgroundResource(R.drawable.player_play_selector);
			if (clientControll)
				show();
			if (isInPlaybackState()) {
				if (mMediaPlayer.isPlaying()) {
					mMediaPlayer.pause();
					mCurrentState = STATE_PAUSED;
					mTargetState = STATE_PAUSED;
//					sendMessageToClient(CMDConstructor.constructPlayerStatusResp(
//							title, url, position, duration,
//							MediaConstant.PlayStatus.ISPAUSING,mMediaController.isMute));
				}
			}
		}

		@Override
		public void seekTo(int msec) {
			if (isInPlaybackState()) {
				if (clientControll)
					show();
				mMediaPlayer.seekTo(msec);
			}
		}

		public void seekProcent(int pos) {
			seekTo(duration * pos / 100);
		}

		@Override
		public void play() {
			if (isInPlaybackState()) {
				status = MediaConstant.PlayStatus.ISPLAYING;
				play_pause
						.setBackgroundResource(R.drawable.player_pause_selector);
				show();
				mMediaPlayer.start();
//				sendMessageToClient(CMDConstructor.constructPlayerStatusResp(
//						title, url, position, duration,
//						MediaConstant.PlayStatus.ISPLAYING,mMediaController.isMute));
			}
		}

		public void setClientContoll(boolean b) {
			clientControll = b;
		}

		public void resume() {
			// TODO Auto-generated method stub
			if (!mMediaPlayer.isPlaying()) {
				play();
			} else
				Log.d(TAG, "无法播放，请检查");
		}

		public void stop() {
			doAutoExit(0);
		}

		public void changeDataSource(final String title, final String url,
				int mediaType) {

			VitamioPlayer.this.url = url;
			VitamioPlayer.this.title = title;
			VitamioPlayer.this.mediaType = mediaType;
			VitamioPlayer.this.mCurrentBufferPercentage = 0;

			if (mediaType == MediaConstant.MediaType.TYPE_MUSIC) {
				findViewById(R.id.foreground).setBackgroundResource(R.drawable.default_bg_two);
			} else if (mediaType == MediaConstant.MediaType.TYPE_VIDEO) {
				findViewById(R.id.foreground).setBackgroundResource(0);
			}
			resetInfo();
			filmName.setText(title);
			_handler.postDelayed(new Runnable() {

				private void updateUrl() {
					dialog.setMessage(res.getString(R.string.onprepare) + title);
					dialog.show();
					mMediaPlayer.reset();
					mSurfaceView.destroyDrawingCache();
					mSurfaceView.invalidate();
					initMediaPlayer();
					_handler.removeMessages(MSG_WHAT_UPDATE_CONTROLLER_INFO);
//					updateCurrentStatus(true, url, title);
					callback.surfaceCreated(mSurfaceHolder);
				}

				@Override
				public void run() {
					// TODO Auto-generated method stub
					updateUrl();
				}
			}, 200);
		}

		@Override
		public void dismissContinueDialog() {
			// TODO Auto-generated method stub
			if(continueDialog!=null)continueDialog.dismiss();
		}

		@Override
		public Context getContext() {
			// TODO Auto-generated method stub
			return VitamioPlayer.this;
		}
	}

	public void initVolume() {
		mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		int mVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
		mMediaController.voiceBar.setMax(mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
		mMediaController.voiceBar.setProgress(mVolume);
		if(mAudioManager.getRingerMode()==AudioManager.RINGER_MODE_SILENT){
			mMediaController.isMute = true;
		}else{
			mMediaController.isMute = false;
		}
		
	}

	class MATDialog extends Dialog {

		TextView msgText, progressText;
		ImageView logo;

		public MATDialog(Context context, int theme) {
			super(context, theme);
			initContentView();
		}

		private View initContentView() {
			View v = LayoutInflater.from(VitamioPlayer.this).inflate(
					R.layout.service_dialog, null);
			msgText = (TextView) v.findViewById(R.id.msg);
			progressText = (TextView) v.findViewById(R.id.progress);
			logo = (ImageView) v.findViewById(R.id.dialoglogo);
			setContentView(v);
			return v;
		}

		void setMessage(String msg) {
			if (msg == null)
				return;
			msgText.setText(msg);
		}

		void setProgress(int progress) {
			if (progress < 0)
				return;
			progressText.setText(progress + "%");
		}

		void setLogo(int resId) {
			logo.setImageResource(resId);
		}

		void setLogo(Bitmap bm) {
			if (bm == null)
				return;
			logo.setImageBitmap(bm);
		}

		void setLogo(Drawable drawable) {
			if (drawable == null)
				return;
			logo.setImageDrawable(drawable);
		}

	}

	private int getWatchRecord(String url) {
		// TODO Auto-generated method stub
		int continuePos = sp.getInt(url, -1);
		return continuePos;
	}

	private void setWatchRecord(String url, int pos) {
		if ((url == null || url.isEmpty()) && pos != 0 && pos != -1||duration==0){
			return;
		}
		if(pos*100/duration>98){
			editor.remove(url);
			editor.commit();
			return;
		}
		Log.d(TAG, "setWatchRecord:" + StringUtils.generateTime(pos));
		editor.putInt(url, pos);
		editor.commit();
	}

	
	private AlertDialog continueDialog;
	private void showContinueDialog(int pos) {
		continueDialog = new AlertDialog.Builder(this).create();
		continueDialog.setTitle(res.getString(R.string.pmt));
		continueDialog.setMessage(String.format(
				res.getString(R.string.player_continue_title),
				StringUtils.generateTime(pos)));
		continueDialog.setButton(Dialog.BUTTON_POSITIVE,
				res.getString(R.string.player_continue_yes),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						mMediaPlayer.seekTo(getWatchRecord(url));
					}
				});
		continueDialog.setButton(Dialog.BUTTON_NEGATIVE,
				res.getString(R.string.player_continue_no),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						mMediaPlayer.seekTo(0);
					}
				});
		continueDialog.show();
	}
}
