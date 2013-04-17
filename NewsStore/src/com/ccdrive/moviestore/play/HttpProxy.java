package com.ccdrive.moviestore.play;

import java.io.IOException;
import java.util.LinkedList;

import android.os.Handler;
import android.util.Log;

import com.rushfusion.vhttpproxy.VHttpProxyDaemon;
import com.rushfusion.vhttpproxy.VHttpProxyDaemon.OnBufferingStatus;

public class HttpProxy {

	private static final String TAG = "HttpProxy";
	private static HttpProxy mInstance = null;

	// ------------HttpProxy-----------------------
	private VHttpProxyDaemon _httpProxy;
	private static final int _port = 9001;
	private static final int BUFFER_SIZE = 2 * 1024 * 1024;
	public static final int NETWORK_SPEED_SAMPLE_COUNT = 10;
	
	boolean _isDebug = false;
	boolean _enableProxyAutoBuffering = false;
	boolean _enableProxyBuffering = true;
	boolean _setSource = false;

	boolean _mediaplayerIsPrepared;
	boolean _mediaplayerIsBuffering;
	boolean _proxyIsBuffering;
	// -------------------------------------------
	private boolean _isPlaying = false;
	private int _downloadSpeedSampleCount = 1;
	private long _lastTime = 0;
	private long _startTime = 0;
	private long _currentBytes = 0;
	private LinkedList<Long> _downloadSpeed = new LinkedList<Long>();

	private String originUrl = "";
	private String proxyUrl = "http://127.0.0.1:" + _port;
	
	public String getProxyUrl() {
		Log.i(TAG, "_proxyUrl is :" + proxyUrl);
		return proxyUrl;
	}

	private String type = "normal";
	private MATController mMediaController;
	
	public String getType() {
		Log.d(TAG, "HttpProxy url.type-->" + type);
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	private HttpProxy(){
		init();
	}

	public void init() {
		try {
			_httpProxy = new VHttpProxyDaemon(_port);
			_httpProxy.setHlsUserAgentIpad();
			_httpProxy.setBufferSize(BUFFER_SIZE);
			_httpProxy.enableAutoBuffering(_enableProxyAutoBuffering);
			setDebugFlag(_isDebug);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static HttpProxy getInstance(){
		if(mInstance == null){
			mInstance = new HttpProxy();
		}
		return mInstance;
	}
	
	
	void setDebugFlag(boolean flag) {
		VHttpProxyDaemon.enableSessionLog(flag);
		VHttpProxyDaemon.enableDataSourceLog(flag);
		VHttpProxyDaemon.enableDataSourceBufferLog(flag);
		VHttpProxyDaemon.enableDataSourceM3u8Log(flag);
	}
	
	private Handler _handler;
	public void init(String url,Handler handler,MATController controller) {
		originUrl = url;
		if(url.contains(".m3u8")){
			type = "hls";
		}
		mMediaController = controller;
		_handler = handler;
	}
	
	public String start(){
		_httpProxy.setSource(originUrl, type, 0);
		_httpProxy.start();
		Log.d(TAG, "start HttpProxy");
		if (_isPlaying) {
			Log.i(TAG, "is playing, stop it first");
			return "";
		}
		_lastTime = System.currentTimeMillis();
		_startTime = _lastTime;
		_currentBytes = 0;
		_downloadSpeed.clear();
		_downloadSpeedSampleCount = 1;

		_isPlaying = true;
		startNetworkInfo();
		_handler.sendEmptyMessageDelayed(MPlayer.MSG_WHAT_SHOW_BUFFER_INFO,MPlayer.REFRESH_BUFFER_INTERVAL);
		return proxyUrl;
	}
	
	public void startNetworkInfo() {
		_handler.sendEmptyMessageDelayed(MPlayer.MSG_WHAT_SHOW_NETWORK_SPEED,MPlayer.REFRESH_NETWORK_INTERVAL);
	}

	private void stopNetworkInfo() {
		_handler.removeMessages(MPlayer.MSG_WHAT_SHOW_NETWORK_SPEED);
	}
	
	public void showBufferInfo() {
		int p = _httpProxy.getBufferPercent();
		int b = _httpProxy.getBufferAvaiableBytes();
		if (b == 0)
			stopBufferInfo();
		b /= 1024;
		mMediaController.syncProxyBufferInfo(p,b);
	}
	
	public void stopBufferInfo() {
		_httpProxy.stopBuffering();
		_handler.removeMessages(MPlayer.MSG_WHAT_SHOW_BUFFER_INFO);
	}
	
	
	public long showNetworkInfo() {
		if (_httpProxy == null)
			return 0;
		long bytes = _httpProxy.getBufferReadInBytes();
		long time = System.currentTimeMillis();
		long delta = ((time - _startTime) / 1000);
		if (delta != 0) {
			long avgSpeed = bytes / 1024 / delta;
			mMediaController.syncAverSpeed(avgSpeed);
		}

		long dt = time - _lastTime;
		long db = bytes - _currentBytes;

		if (db < 0)
			return 0;
		if (dt <= 0)
			return 0;

		_currentBytes = bytes;
		_lastTime = time;

		long thisSpeed = db * 1000 / dt;
		_downloadSpeed.addLast(thisSpeed);
		if (_downloadSpeedSampleCount < NETWORK_SPEED_SAMPLE_COUNT) {
			++_downloadSpeedSampleCount;
			return 0;
		}

		long total = 0;
		for (long each : _downloadSpeed) {
			total += each;
		}

		long lastestSpeed = total / 1024 / _downloadSpeed.size();
		_downloadSpeed.removeFirst();
		mMediaController.syncInstantSpeed(lastestSpeed);
		return lastestSpeed;
	}
	

	public void startBuffering() {
		// TODO Auto-generated method stub
		_mediaplayerIsBuffering = true;
		if (_enableProxyBuffering) {
			Log.i(TAG, "start proxy buffering");
			_httpProxy.startBuffering();
		}
	}

	public void setIsBuffing(boolean b) {
		_mediaplayerIsBuffering = b;
	}

	public void stop() {
		// TODO Auto-generated method stub
		stopBufferInfo();
		stopNetworkInfo();
		if (_httpProxy != null)
			_httpProxy.stop();
	}

	public int getHlsDuration() {
		return _httpProxy.getHlsDuration();
	}

	public void setListener(OnBufferingStatus _bufferStatuslistener) {
		_httpProxy.setOnBufferingStatusListener(_bufferStatuslistener);
	}
}
