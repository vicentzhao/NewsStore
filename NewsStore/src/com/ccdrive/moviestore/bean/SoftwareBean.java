package com.ccdrive.moviestore.bean;

import java.util.ArrayList;

public class SoftwareBean {
	private String name;
	private String info;
	private String version;
	private String image_path;
	private String download_path;
	private String id;
	private String author, release, //发布日期
	environment,addDate;
	public String getAddDate() {
		return addDate;
	}
	public void setAddDate(String addDate) {
		this.addDate = addDate;
	}
	private ArrayList<PostMent> postMentList;
	
	
	public ArrayList<PostMent> getPostMentList() {
		return postMentList;
	}
	public void setPostMentList(ArrayList<PostMent> postMentList) {
		this.postMentList = postMentList;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getRelease() {
		return release;
	}
	public void setRelease(String release) {
		this.release = release;
	}
	public String getEnvironment() {
		return environment;
	}
	public void setEnvironment(String environment) {
		this.environment = environment;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getImage_path() {
		return image_path;
	}
	public void setImage_path(String image_path) {
		this.image_path = image_path;
	}
	public String getDownload_path() {
		return download_path;
	}
	public void setDownload_path(String download_path) {
		this.download_path = download_path;
	}

}
