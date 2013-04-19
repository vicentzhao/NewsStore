package com.ccdrive.newsstore.play;

import android.content.Context;

public abstract class BaseController {

	public abstract void initControllerView();
	public abstract void setFilmName(String title);
	public abstract void resetInfo();
	public abstract void startVideoTask();
	public abstract boolean isShowing();
	public abstract void show();
	public abstract void hide();
	public abstract void updateInfo();

	public abstract void setClientContoll(boolean b);
	public abstract void changeRingerMode();

	public abstract void play();
	public abstract void resume();
	public abstract void pause();
	public abstract void stop();
	
	public abstract void changeVolume(int voice);
//	public abstract void FF();
//	public abstract void FB();
	public abstract void seekTo(int msec);
	public abstract void seekProcent(int pos);
	public abstract void changeDataSource(final String title,final String url,int mediaType);

	public abstract int getDuration();
	public abstract int getCurrentPosition();
	public abstract boolean isPlaying();
	public abstract void dismissContinueDialog();
	
	public abstract Context getContext();
	
}

