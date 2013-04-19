package com.ccdrive.newsstore.download;

import android.media.MediaPlayer;
import android.util.Log;

public class MediaPlayerListener {
	String TAG = "_____TAG_____";

	public MediaPlayerListener() {

	}

	public void setAllListener(MediaPlayer mMediaPlayer) {
		mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
			@Override
			public boolean onError(MediaPlayer mp, int what, int extra) {
				// TODO Auto-generated method stub
				Log.i(TAG, "Error on Listener, what: " + what + "extra: "
						+ extra);
				return false;
			}
		});

		mMediaPlayer
				.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
					@Override
					public void onBufferingUpdate(MediaPlayer mp, int percent) {
						// TODO Auto-generated method stub
						Log.i(TAG, "Update buffer: "
								+ Integer.toString(percent) + "%");
					}
				});

		mMediaPlayer
				.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
					@Override
					public void onCompletion(MediaPlayer mp) {
						// TODO Auto-generated method stub
						Log.i(TAG, "mMediaPlayer Listener Completed");
					}
				});

		mMediaPlayer
				.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
					@Override
					public void onPrepared(MediaPlayer mp) {
						// TODO Auto-generated method stub
						Log.i(TAG, "Prepared Listener");
					}
				});
	}
}
