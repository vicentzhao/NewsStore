package com.ccdrive.newsstore.bean;
/*
 * ֧ 需要支付订单的信息
 */
public class PayOrderBean {
	private String name;
	private String price;
	private String olid,num,kind,orderDate;
	public String getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}
	public String getNum() {
		return num;
	}
	public void setNum(String num) {
		this.num = num;
	}
	public String getKind() {
		return kind;
	}
	public void setKind(String kind) {
		this.kind = kind;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getOlid() {
		return olid;
	}
	public void setOlid(String olid) {
		this.olid = olid;
	}
}
