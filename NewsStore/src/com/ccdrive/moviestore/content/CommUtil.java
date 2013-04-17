package com.ccdrive.moviestore.content;

import java.util.ArrayList;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

import com.ccdrive.moviestore.bean.SoftwareBean;


public class CommUtil {
	
	public static ArrayList<String> list_left;
	HttpResponse response;
	HttpGet request;
	HttpEntity responseEntity;
	HttpClient httpClient ;
	int size = 10;
	ArrayList<SoftwareBean> softlist;
		//���ýӿ�
	public ArrayList<String> GetLawRuleList(int left_type,int library) {
		// TODO Auto-generated method stub 
		return null;
	}
	
	public ArrayList<SoftwareBean> GetSoftwareList(int left_type) {
		// TODO Auto-generated method stub
		softlist = new ArrayList<SoftwareBean>();
		if(left_type == Constant.ANIME){
			for(int i = 0;i<10;i++){
				SoftwareBean shop  = new SoftwareBean();
				shop.setName("pptv");
				shop.setInfo("Ӱ��-pptv");
				shop.setVersion("�������� 2013.01.01");
				softlist.add(shop);
			}
		}
		if(left_type == Constant.MUSIC){
			for(int i = 0;i<10;i++){
				SoftwareBean shop  = new SoftwareBean();
				shop.setName("������");
				shop.setInfo("��Ϸ-������");
				shop.setVersion("�������� 2012.01.01");
				softlist.add(shop);
			}
		}
		if(left_type == Constant.TV || left_type == Constant.FLFG){
			for(int i = 0;i<10;i++){
				SoftwareBean shop  = new SoftwareBean();
				shop.setName("�ṷ����");
				shop.setInfo("����-�ṷ");
				shop.setVersion("�������� 2012.05.01");
				softlist.add(shop);
			}
		}
		return  softlist;
	}
	
	public ArrayList<String> GetRightTitle(int left_type) {
		// TODO Auto-generated method stub
		list_left = new ArrayList<String>();
		if(left_type == Constant.FLFG){
			String[] flfgStr = { "Ӱ������", "����"};
			for (int i = 0; i < flfgStr.length; i++) {
			list_left.add(flfgStr[i]);
			}
		}
		if(left_type == Constant.AL){
			String[] alStr = { "PPTV���°�", "������"};
			for (int i = 0; i < alStr.length; i++) {
			list_left.add(alStr[i]);
			}
		}
		if(left_type == Constant.FLWSYS){
			String[] flwsysStr = { "��������"};
			for (int i = 0; i < flwsysStr.length; i++) {
			list_left.add(flwsysStr[i]);
			}
		}
		return  list_left;
	}
}
