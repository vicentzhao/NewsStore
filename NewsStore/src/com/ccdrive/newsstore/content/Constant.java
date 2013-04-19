package com.ccdrive.newsstore.content;

import java.io.File;

import com.ccdrive.newsstore.http.HttpRequest;
/*
 * һЩ�ĳ���
 */
public class Constant {
	   	
       public static final String SOFT_FIELD = "soft";	
       public static final String BOOK_FIELD = "book";	
       public static final String PAPER_FIELD = "paper";	 
       public static final String PRINT_FIELD = "journal";	 
       public static final String MOVIE_FIELD = "movie";	 
       public static final String TV_FIELD = "tvplay";	 
       public static final String ANIME_FIELD = "cartoon";	 
       public static final String MUSIC_FIELD = "music";	 
       public static final String RECRD_FIELD = "tape";	 
	
	   public static File FILE_NAME = null;
	   public static String filePath = "";
		//�������
		public static final int FLFG = 0x1;
		public static final int AL = 0x2;
		public static final int FLWSYS = 0x3;
		public static final int MOVIE = 0x4;
		public static final int RECRD = 0x5;
		public static final int TV = 0x6;
		public static final int ANIME = 0x7;
		public static final int MUSIC = 0x8;
		public static final int SOFT = 0x9;
		
		//��������
		public static final int ALL_MOVIES =10004;
		public static final int ALL_RECORD =10005;//�����б�
		public static final int SINGLE_RECORD =100051;//��������
		public static final int SINGLE_IMAGE =100052;//ͼƬ
		public static final int DOWNLOAD_LIST =100053;//
		public static final int DOWNLOAD_CHECK =10006;//֤
		public static final int ORDER_SOFT_LIST=100091;//
		public static final int ORDER_SOFT_SINGLE=100092;//
		public static final int ORDER_LIST=100093;//
		public static final int ORDER_LIST_NUM=100094;//
		public static final int ORDER_LIST_ID=100095;//����ID
		public static final int ORDER_LIST_PAY=100000;//֧��
		public static final int DOWN_SOFT_LIST=100001;//
		public static final int USERLOGIN=100010;//
		
		//һЩ����
		public static final int MYMUSIC = 100011;
		public static final int MUSICSTORE = 100012;
		public static final int MUSICAPP = 100013;
		public static final int MUSICCHAPTER = 100014;
		public static final int MUSICMV = 100015;
		
		public static final String MYMUSIC_APP=HttpRequest.URL_QUERY_LIST__SOFTORDERED;
		public static final String MYMUSIC_CHAPTER=HttpRequest.URL_QUERY_lIST_ORDER_NEWS;
		public static final String MYMUSIC_MV=HttpRequest.URL_QUERY_lIST_ORDER_NEWS;
		public static final String MUSICSTORE_APP=HttpRequest.URL_QUERY_STROE_ALL_SOFT;
		public static final String MUSICSTORE_CHAPTER=HttpRequest.URL_QUERY_STORE_ALL_NEWS;
		public static final String MUSICSTORE_MV=HttpRequest.URL_QUERY_STROE_ALL_TV;
		public static final String MUSIC_DEFALUT=HttpRequest.URL_QUERY_LIST__SOFTORDERED;
		
		public static final String app_recommde=HttpRequest.URL_QUERY_STROE_ALL_SOFT;
		
		 public static final  boolean useVitamio = true;  //是否使用vitamio
		
		
	 
		
		
}
