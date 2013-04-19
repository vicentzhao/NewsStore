package com.ccdrive.newsstore.http;

import java.io.Serializable;

public class Manager_Token implements Serializable{
	private boolean tokenflag=false;
	private String id;
	private String username;
	private String truename;
	private String usn;
	private String usertype;
	private String role;
	private String idcard;
	private String token;
	private String accepttime;
	private String lastaccepttime;
	public String accessToken;
    public String tokenSecret;
	public boolean isTokenflag() {
		return tokenflag;
	}
	public void setTokenflag(boolean tokenflag) {
		this.tokenflag = tokenflag;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getTruename() {
		return truename;
	}
	public void setTruename(String truename) {
		this.truename = truename;
	}
	public String getUsn() {
		return usn;
	}
	public void setUsn(String usn) {
		this.usn = usn;
	}
	public String getUsertype() {
		return usertype;
	}
	public void setUsertype(String usertype) {
		this.usertype = usertype;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getAccepttime() {
		return accepttime;
	}
	public void setAccepttime(String accepttime) {
		this.accepttime = accepttime;
	}
	public String getLastaccepttime() {
		return lastaccepttime;
	}
	public void setLastaccepttime(String lastaccepttime) {
		this.lastaccepttime = lastaccepttime;
	}
	public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	public String getTokenSecret() {
		return tokenSecret;
	}
	public void setTokenSecret(String tokenSecret) {
		this.tokenSecret = tokenSecret;
	}
	public String getIdcard() {
		return idcard;
	}
	public void setIdcard(String idcard) {
		this.idcard = idcard;
	}
    
	
}
