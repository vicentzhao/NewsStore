package com.ccdrive.moviestore.play;



public class MediaConstant {
	
	public static boolean useVitamio = false;
	
//	public static final String BASE_URL = "http://tvsrv.webhop.net:9062/";
//	public static final String BASE_URL = "http://192.168.3.107:8080/" ;
//	public static final String BASE_URL = "http://113.31.40.102:9062/";
	public static final String BASE_URL = "http://113.31.88.104:9062/";
	public static final String EXCEPTION_URL = BASE_URL+"report_exception";
	
	public class PlayStatus{
		public static final int ISPLAYING = 1;
		public static final int ISPAUSING = 0;
		public static final int ISCLOSED = -1;
	}
	
	public class ErrorCode{
		public static final int RET_SUCCESS = 0;
	    public static final int RET_INVALID_PASSWORD = -1;
	    public static final int RET_INVALID_USERTOKEN = -2;
		public static final int RET_WAIT = -3;
	}
	public class ConnectStatus{
		public static final int NUKNOWN = -2;
		public static final int ISOFF = -1;
		public static final int ISCONNECTING = 0;
		public static final int ISON = 1;
	}
	
	public class ControllMode{
		public static final int MODE_MEDIA = 1000;
		public static final int MODE_MOUSE = 1001;
		public static final int MODE_REMOTE = 1002;
	}
	
	public class MediaType{
		public static final int TYPE_IMAGE = 0;
		public static final int TYPE_MUSIC = 1;
		public static final int TYPE_VIDEO = 2;
	}
}
