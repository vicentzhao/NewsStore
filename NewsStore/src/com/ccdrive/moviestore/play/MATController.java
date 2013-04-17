package com.ccdrive.moviestore.play;


import io.vov.utils.StringUtils;
import android.content.Context;
import android.graphics.Rect;
import android.media.AudioManager;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.ccdrive.moviestore.R;

public class MATController extends BaseController{
		private static final String TAG = "MATController";
		private static final int HIDE_CONTROLLER_INTERVAL = 5000;
		private PopupWindow mWindow;
		private View mRoot;
		private View mAnchor;
		// ------------------------------
		private TextView _instantSpeedTextView;
		private TextView _avgSpeedTextView;
		private TextView _bufferPercentTextView;
		private TextView _bufferBytesTextView;
		// ------------------------------

		private Context mContext;
		private Button play_pause, next, prev, stop, sound;// ff, fb,
		private TextView filmName, time;
		private SeekBar seekBar, voiceBar;

		boolean isMute = false;
		public int status = MediaConstant.PlayStatus.ISPLAYING;
		private int focusViewId = 0;

		private boolean allowLoadVideoTask = false;
		
		public boolean isAllowLoadVideoTask() {
			return allowLoadVideoTask;
		}

		public void setAllowLoadVideoTask(boolean allowLoadVideoTask) {
			this.allowLoadVideoTask = allowLoadVideoTask;
		}

		private Handler mHandler;
		
		public void setHandler(Handler handler) {
			mHandler = handler;
		}

		public MATController(Context context,View view) {
			mContext = context;
			mAnchor = view;
			init();
		}
		
		public boolean isShowing() {
			return mWindow.isShowing();
		}

		public void show() {
			if(mAnchor.getWindowToken()==null)return;
			int[] location = new int[2];
			mAnchor.getLocationOnScreen(location);
			Rect anchorRect = new Rect(location[0], location[1], location[0] + mAnchor.getWidth(), location[1] + mAnchor.getHeight());
			mWindow.showAtLocation(mAnchor, Gravity.NO_GRAVITY, anchorRect.left, anchorRect.bottom);
			if (focusViewId == 0) {
				play_pause.requestFocus();
			} else {
				mRoot.findViewById(focusViewId).requestFocus();
			}
			mHandler.removeMessages(MPlayer.MSG_WHAT_HIDE_CONTROLLER);
			mHandler.sendEmptyMessageDelayed(MPlayer.MSG_WHAT_HIDE_CONTROLLER,HIDE_CONTROLLER_INTERVAL);
		}

		public void hide() {
			if(mWindow.isShowing()&&mAnchor.getWindowToken()!=null)mWindow.dismiss();
			mHandler.removeMessages(MPlayer.MSG_WHAT_HIDE_CONTROLLER);
		}

		private void init() {
			// TODO Auto-generated method stub
			initControllerView();
			initVolume();
			initPopWindow();
		}

		public void initPopWindow() {
			mWindow = new PopupWindow(mRoot,-1,-2);
			mWindow.setFocusable(true);
			mRoot.setFocusableInTouchMode(true);
			mWindow.setAnimationStyle(R.style.AnimationControllerFade);
			mRoot.setOnTouchListener(new OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					// TODO Auto-generated method stub
					hide();
					return false;
				}
			});
			mRoot.setOnKeyListener(new OnKeyListener() {
				
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
			mHandler.removeMessages(MPlayer.MSG_WHAT_UPDATE_CONTROLLER_INFO);
		}

		private int currentProgress = 0;
		
		public void initControllerView() {
			// TODO Auto-generated method stub
			mRoot = LayoutInflater.from(mContext).inflate(R.layout.player_controller, null);
			play_pause = (Button) mRoot.findViewById(R.id.controller_play_pause);
			next = (Button) mRoot.findViewById(R.id.controller_next);
			prev = (Button) mRoot.findViewById(R.id.controller_prev);
			stop = (Button) mRoot.findViewById(R.id.controller_stop);
			sound = (Button) mRoot.findViewById(R.id.controller_silence);

			filmName = (TextView) mRoot.findViewById(R.id.controller_filename);
			time = (TextView) mRoot.findViewById(R.id.controller_process);
			seekBar = (SeekBar) mRoot.findViewById(R.id.controller_seekbar);
			voiceBar = (SeekBar) mRoot.findViewById(R.id.controller_voicebar);
			// ------------------------------------------------------------------------
			ViewGroup infoBar = (ViewGroup) mRoot.findViewById(R.id.infoBar);
			_instantSpeedTextView = (TextView) mRoot.findViewById(R.id.speed);
			_avgSpeedTextView = (TextView) mRoot.findViewById(R.id.avgspeed);
			_bufferPercentTextView = (TextView) mRoot.findViewById(R.id.percent);
			_bufferBytesTextView = (TextView) mRoot.findViewById(R.id.bufferbytes);
			// ------------------------------------------------------------------------
			if(!MPlayer.getInstance().isHttpProxy())infoBar.setVisibility(View.INVISIBLE);
			seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

				
				@Override
				public void onStopTrackingTouch(SeekBar seekBar) {
					Log.i(TAG, "controller.seekbar onStop");
					if(MPlayer.getInstance().isInPlaybackState()){
						MPlayer.getInstance().doSeek(currentProgress);
					}
				}

				@Override
				public void onStartTrackingTouch(SeekBar seekBar) {
					Log.i(TAG, "controller.seekbar onStart");
				}

				@Override
				public void onProgressChanged(SeekBar seekBar, int progress,
						boolean fromUser) {

					if (fromUser && MPlayer.getInstance().isInPlaybackState()) {
						focusViewId = R.id.controller_seekbar;
						Log.i(TAG, "controller.seekBar  onChanged-->"+ progress);
						show();
						currentProgress = progress;
						mHandler.removeMessages(MPlayer.MSG_WHAT_UPDATE_CONTROLLER_INFO);
						syncTimeBar(getDuration()* currentProgress / 100, getDuration());
					}
				}
			});

			seekBar.setOnKeyListener(new OnKeyListener() {

				@Override
				public boolean onKey(View view, int i, KeyEvent keyevent) {
					if (!MPlayer.getInstance().isInPlaybackState())
						return false;
					focusViewId = R.id.controller_seekbar;
					if (keyevent.getAction() == KeyEvent.ACTION_UP) {
						if (keyevent.getKeyCode() == KeyEvent.KEYCODE_DPAD_LEFT
								|| keyevent.getKeyCode() == KeyEvent.KEYCODE_DPAD_RIGHT) {
							MPlayer.getInstance().checkSeeking(currentProgress);
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
					if (MPlayer.getInstance().isInPlaybackState()&&MPlayer.getInstance().getMediaPlayer().isPlaying()) {
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
						MPlayer.getInstance().nextVideo();
					}
				}
			});
			prev.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					focusViewId = v.getId();
					if (allowLoadVideoTask) {
						resetInfo();
						MPlayer.getInstance().preVideo();
					}
				}
			});

			stop.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					focusViewId = v.getId();
					MPlayer.getInstance().doExitPlayer();
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

		public void changeVolume(int voice) {
			// TODO Auto-generated method stub
			if (MPlayer.getInstance().isClientControll())
				show();
			if (mAudioManager != null) {
				voiceBar.setProgress(voice);
				mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, voice,0);
				if (isMute) {
					isMute = false;
					sound.setBackgroundResource(R.drawable.player_sound_selector);
					mAudioManager.setStreamMute(AudioManager.STREAM_MUSIC,false);
				}
			}
		}

		public void changeRingerMode() {
			show();
			if (isMute) {
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
			filmName.setText(mContext.getResources().getString(R.string.playing) + title);
		}

		public void updateInfo() {
			if (MPlayer.getInstance().isHttpProxy()) {
				mHandler.sendEmptyMessageDelayed(MPlayer.MSG_WHAT_SHOW_BUFFER_INFO,MPlayer.REFRESH_BUFFER_INTERVAL);
				HttpProxy.getInstance().startNetworkInfo();
			}
			mHandler.sendEmptyMessageDelayed(MPlayer.MSG_WHAT_UPDATE_CONTROLLER_INFO,1000);

		}

		@Override
		public int getCurrentPosition() {
			return MPlayer.getInstance().getCurrentPosition();
		}

		@Override
		public int getDuration() {
			return MPlayer.getInstance().getDuration();
		}

		@Override
		public boolean isPlaying() {
			return MPlayer.getInstance().isPlaying();
		}

		@Override
		public void pause() {
			show();
			MPlayer.getInstance().pause();
		}
		
		public void syncStatus(int status) {
			// TODO Auto-generated method stub
			this.status = status;
			if (status == MediaConstant.PlayStatus.ISPLAYING) {
				play_pause.setBackgroundResource(R.drawable.player_pause_selector);
			} else if (status == MediaConstant.PlayStatus.ISPAUSING) {
				play_pause.setBackgroundResource(R.drawable.player_play_selector);
			} else if (status == MediaConstant.PlayStatus.ISCLOSED) {
				resetInfo();
			}else {
				Log.d(TAG, "unknown playstatus");
			}
		}

		@Override
		public void seekTo(int pos) {
			if (MPlayer.getInstance().isClientControll())show();
			MPlayer.getInstance().seekTo(pos);
		}

		public void seekProcent(int pos) {
			Log.d(TAG,"seekProcent:"+pos);
			seekTo(getDuration() * pos / 100);
		}

		@Override
		public void play() {
			show();
			MPlayer.getInstance().play();
		}

		public void resume() {
			MPlayer.getInstance().resume();
		}

		public void stop() {
			MPlayer.getInstance().doExitPlayer();
		}

		public void changeDataSource(final String newTitle, final String newUrl,final int newMediaType) {
			Log.d(TAG, "==========changeDataSource=======");
			resetInfo();
			setFilmName(newTitle);
			MPlayer.getInstance().changeData(newTitle,newUrl,newMediaType);
		}

		@Override
		public void dismissContinueDialog() {
			MPlayer.getInstance().dismissContinueDialog();
		}

		public void syncProxyBufferInfo(int p, int b) {
			_bufferPercentTextView.setText(String.valueOf(p) + "%");
			_bufferBytesTextView.setText(String.valueOf(b) + "Kb");
		}

		public void syncAverSpeed(long avgSpeed) {
			_avgSpeedTextView.setText(String.valueOf(avgSpeed)+ "Kbps");
		}

		public void syncInstantSpeed(long lastestSpeed) {
			_instantSpeedTextView.setText(String.valueOf(lastestSpeed) + "Kbps");
		}

		public void syncTimeBar(int mPosition, int mDuration) {
			if(mDuration==0)return;
			seekBar.setProgress(100 * mPosition/ mDuration);
			time.setText(StringUtils.generateTime(mPosition) + "/" + StringUtils.generateTime(mDuration));
		}

		
		private AudioManager mAudioManager;
		private void initVolume() {
			mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
			int mVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
			voiceBar.setMax(mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
			voiceBar.setProgress(mVolume);
			if (mAudioManager.getRingerMode() == AudioManager.RINGER_MODE_SILENT) {
				isMute = true;
			} else {
				isMute = false;
			}
		}

		public void volumeChanged() {
			// TODO Auto-generated method stub
			if (mAudioManager != null) {
				int mVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
				changeVolume(mVolume);
			}
		}

		@Override
		public void startVideoTask() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void setClientContoll(boolean b) {
			MPlayer.getInstance().setClientControll(b);
		}

		@Override
		public Context getContext() {
			return mContext;
		}
	}