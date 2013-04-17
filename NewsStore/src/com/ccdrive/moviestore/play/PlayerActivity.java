package com.ccdrive.moviestore.play;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ccdrive.moviestore.R;

public class PlayerActivity extends Activity{
	private MATController mMediaController;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.player_activity);
		ViewGroup parent = (ViewGroup) findViewById(R.id.player_parent);
		MPlayer.getInstance().onCreate(this,parent,getIntent());
		mMediaController = MPlayer.getInstance().getController();
	}
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}
	@Override
	protected void onResume() {
		MPlayer.getInstance().onResume();
		super.onResume();
	}
	@Override
	protected void onPause() {
		MPlayer.getInstance().onPause();
		super.onPause();
	}
	@Override
	protected void onDestroy() {
		MPlayer.getInstance().onDestory();
		super.onDestroy();
	}
	
	private static final int MSG_WHAT_RESET_EXIT_NUM = 7;
	Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case MSG_WHAT_RESET_EXIT_NUM:
				exit_num = 0;
				break;
			default:
				break;
			}
		};
	};
	
	private int exit_num = 0;
	
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_DPAD_UP:
			mMediaController.show();
			return true;
		case KeyEvent.KEYCODE_DPAD_DOWN:
			mMediaController.show();
			return true;
		case KeyEvent.KEYCODE_DPAD_CENTER:
			mMediaController.show();
			return true;
		case KeyEvent.KEYCODE_DPAD_LEFT:
			mMediaController.show();
			return true;
		case KeyEvent.KEYCODE_DPAD_RIGHT:
			mMediaController.show();
			return true;
		case KeyEvent.KEYCODE_BACK:
			if (exit_num == 0) {
				exit_num += 1;
				Toast.makeText(this,getResources().getString(R.string.do_again_exit), 1).show();
				handler.sendEmptyMessageDelayed(MSG_WHAT_RESET_EXIT_NUM, 3000);
			} else if(exit_num==1){
				mMediaController.stop();
			}
			return false;
		case KeyEvent.KEYCODE_MUTE:
			mMediaController.changeRingerMode();
			break;
		case KeyEvent.KEYCODE_VOLUME_UP:
			mMediaController.volumeChanged();
			break;
		case KeyEvent.KEYCODE_VOLUME_DOWN:
			mMediaController.volumeChanged();
			break;
		}
		return super.onKeyDown(keyCode, event);
	}
}
