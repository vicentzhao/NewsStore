package com.ccdrive.moviestore.bean;

import java.io.Serializable;
import java.util.HashMap;

public class Movie implements Serializable{
	
	private String name;
	private String pubName;
	private String info;
	private String version;
	private String image_path;
	private String download_path;
	private String id;
	private String seq;
	public String getSeq() {
		return seq;
	}
	public void setSeq(String seq) {
		this.seq = seq;
	}
	public PagenationBean getPagep() {
		return pagep;
	}
	public void setPagep(PagenationBean pagep) {
		this.pagep = pagep;
	}
	private PostMent postMent;
	private String type;
	private PagenationBean pagep;
	private String 
	artist, language, pubdate ,company;
	public String getArtist() {
		return artist;
	}
	public void setArtist(String artist) {
		this.artist = artist;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public String getPubdate() {
		return pubdate;
	}
	public void setPubdate(String pubdate) {
		this.pubdate = pubdate;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public PostMent getPostMent() {
		return postMent;
	}
	public void setPostMent(PostMent postMent) {
		this.postMent = postMent;
	}
	public String getPubName() {
		return pubName;
	}
	public void setPubName(String pubName) {
		this.pubName = pubName;
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
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	 
   
}
