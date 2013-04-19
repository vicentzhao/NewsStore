package com.ccdrive.newsstore.page;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.ccdrive.newsstore.R;
import com.ccdrive.newsstore.bean.OrderBean;
import com.ccdrive.newsstore.bean.PagenationBean;
import com.ccdrive.newsstore.bean.PayOrderBean;
import com.ccdrive.newsstore.http.HttpRequest;
import com.ccdrive.newsstore.util.JsonUtil;

public class OrderPage {
	
	
	private Context  mContext ;
	private static LayoutInflater inflater;
	private ArrayList<OrderBean> orderList;
	private Dialog builder ;
	private AQuery aQuery;
	private ProgressDialog pd;
	private int deleId;
	private String mainTotalPrice;
	private View orderPageView;
	//分页信息
	private int pageSize =10;
	private int pageCount =1;
	private String orderPathBegin ;
	private int DeviceHeight,DeviceWidth;
	private static PagenationBean page = new PagenationBean();
	//orderInfo id
	private static int[] orderInfoItems = { R.id.order_main_item01, R.id.order_main_item02,
		R.id.order_main_item03, R.id.order_main_item04, R.id.order_main_item05,
		R.id.order_main_item06, R.id.order_main_item07, R.id.order_main_item08,
		R.id.order_main_item09, R.id.order_main_item10 };
	
	//dele id 
	private static int[] deleItems = { R.id.order_dele_01, R.id.order_dele_02,
		R.id.order_dele_03, R.id.order_dele_04, R.id.order_dele_05,
		R.id.order_dele_06, R.id.order_dele_07, R.id.order_dele_08,
		R.id.order_dele_09, R.id.order_dele_10 };
	
	// view id
	private static int[] lineItems = { R.id. view_order_line01, R.id.view_order_line02,
		R.id.view_order_line03, R.id.view_order_line04, R.id.view_order_line05,
		R.id.view_order_line06, R.id.view_order_line07, R.id.view_order_line08,
		R.id.view_order_line09, R.id.view_order_line10 };

	public OrderPage(Context context,int deviceHeight,int deviceWidth) {
		this.mContext = context;
		inflater =LayoutInflater.from(context);
		builder=new Dialog(context);
		aQuery =new AQuery(context);
		this.DeviceHeight=deviceHeight;
		this.DeviceWidth = deviceWidth;
		orderPathBegin =HttpRequest.URL_QUERY_LIST_ORDER+pageCount;
		orderPageView = inflater.inflate(R.layout.order_jay, null);
		  builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
			builder.setContentView(orderPageView);
			Window dialogWindow = builder.getWindow();
			WindowManager.LayoutParams lp = dialogWindow.getAttributes();
			dialogWindow.setGravity(Gravity.CENTER);
			lp.width = deviceWidth;
			lp.height = deviceHeight;
			dialogWindow.setAttributes(lp);
			builder.show();
	}
	 
	 public  void setOrderPage(String path){
		 final ProgressDialog Dialog = ProgressDialog.show(aQuery.getContext(),
					"缓冲中。。", "正在加载数据请稍后。。");
		    ProDiaglogDimiss(Dialog);
		    Dialog.show();
		  
			for (int i = 0; i < 10; i++) {
				orderPageView.findViewById(lineItems[i]).setVisibility(View.GONE);
			}
			aQuery.ajax(path, String.class, new AjaxCallback<String>() {// 这里的函数是一个内嵌函数如果是函数体比较复杂的话这种方法就不太合适了
				@Override
				public void callback(String url, String json,
						AjaxStatus status) {
					if (json != null) {
						Dialog.dismiss();
						final ArrayList<PayOrderBean> orderNumList = JsonUtil.getOrderNum(json);
						try {
							JSONObject jsObject = new JSONObject(json);
							JSONObject pageObject = jsObject
									.getJSONObject("page");
							String currentPage = pageObject
									.getString("currentPage");
							String pageSize = pageObject
									.getString("pageSize");
							int totalRows = pageObject.getInt("totalRows");
							page.init(currentPage, pageSize, totalRows);
							UpdatePaging();
							JSONObject jb = new JSONObject(json);
							String total =jb.getString("total");
							mainTotalPrice = total;
							((TextView)(orderPageView.findViewById(R.id.btn_order_totalprice))).setText("total :"+total+" (元)");
						} catch (JSONException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						if(orderNumList.size()!=0){
							setOrderPageInfo(orderNumList, orderPageView);
						}
						
					} else {
						Dialog.dismiss();
						if (status.getCode() == -101) {
							Toast.makeText(
									aQuery.getContext(),
									"Error:"
											+ status.getCode()
											+ aQuery.getContext()
													.getResources()
													.getString(
															R.string.checknetwork),
									Toast.LENGTH_LONG).show();
						} else {
							Toast.makeText(
									aQuery.getContext(),
									aQuery.getContext().getResources()
											.getString(R.string.nocontent),
									Toast.LENGTH_LONG).show();
						}
					}
				}
			});
			
			 orderPageView.findViewById(R.id.btn_order_payconfrim).setOnClickListener(new  OnClickListener() {
				
				@Override
				public void onClick(View v) {
					createPayDialog();
				}
			});
			 orderPageView.findViewById(R.id.btn_order_paycancle).setOnClickListener(new  OnClickListener() {
					
					@Override
					public void onClick(View v) {
						builder.dismiss();
					}
				});
			
	 }
	  public static void DiaglogDimiss(Dialog dialog){
		  dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
				@Override
				public boolean onKey(DialogInterface dialog, int keyCode,
						KeyEvent event) {
					 if (keyCode == KeyEvent.KEYCODE_BACK){
					          // Perform action on key press
						 dialog.dismiss();
					          return true;
					        }
					return false;
				}
			});
	  }
	  public static void ProDiaglogDimiss(ProgressDialog dialog){
		  dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
			  @Override
			  public boolean onKey(DialogInterface dialog, int keyCode,
					  KeyEvent event) {
				  if (keyCode == KeyEvent.KEYCODE_BACK){
					  // Perform action on key press
					  dialog.dismiss();
					  return true;
				  }
				  return false;
			  }
		  });
	  }
	  public void createPayDialog() {
		    Resources res = mContext.getResources();
			AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
			builder.setTitle(res.getString(R.string.pmt));
			builder.setMessage(res.getString(R.string.orderpayinfo)+mainTotalPrice);
			builder.setPositiveButton(res.getString(R.string.paysure),
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							new AsyncTask<Void, Void, String>(){

								@Override
								protected void onPostExecute(String result) {
									pd.dismiss();
									if(result!=null){
										if((aQuery.getContext().getResources().getString(R.string.paysuccess)).equals(result)){
											Toast.makeText(aQuery.getContext(), aQuery.getContext().getResources().getString(R.string.paysuccess), Toast.LENGTH_LONG).show();
										}
									}else{
										Toast.makeText(aQuery.getContext(), result, Toast.LENGTH_LONG).show();

									}
									super.onPostExecute(result);
								}

								@Override
								protected void onPreExecute() {
									pd = new ProgressDialog(aQuery.getContext());
									pd.show();
									super.onPreExecute();
								}

								@Override
								protected String doInBackground(Void... params) {
									String path =HttpRequest.URL_QUERY_LIST_ORDER_NUM;
									String jsonString = getInforFromUrl(path);
									try {
									if(jsonString!=null){
										JSONObject jsonO;
											jsonO = new JSONObject(jsonString);
										String num =jsonO.getString("num");
										String payPath = HttpRequest.URL_QUERY_LIST_PAY_ID+num;
										String infoIdString = getInforFromUrl(payPath);
										if(infoIdString!=null){
										JSONObject jsonPay = new JSONObject(infoIdString);
										String payId = jsonPay.getString("ID");
										String lastPayPath =HttpRequest.URL_QUERY_LIST_PAY+payId;
										String payInfoLast = getInforFromUrl(lastPayPath);
										if(payInfoLast!=null){
										JSONObject js =new JSONObject(payInfoLast);
										  String content = js.getString("CONTENT");
										  return content;
										}
										}
									}} catch (JSONException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} 
										
									
									return null;
								}
								
							}.execute();
						}
					});
			builder.setNegativeButton(res.getString(R.string.cancle),
					new DialogInterface.OnClickListener() {
      			@Override
						public void onClick(DialogInterface dialog, int which) {
      				    
      				dialog.dismiss();
      				
						}
					});
			 builder.create().show();
		}
	  final static int BUFFER_SIZE = 4096; 
	  private static String InputStreamTOString(InputStream in) throws Exception{ 
	                        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
	                        byte[] data = new byte[BUFFER_SIZE ]; 
	                        int count = -1; 
	                        while((count = in.read(data,0,BUFFER_SIZE )) != -1) 
	                          outStream.write(data, 0, count); 
	                          data = null;
	                          String result= new String(outStream.toByteArray(),"utf-8" );
	                          return result;
	          }
	  
	  private static String getInforFromUrl(String path) {
		     URL url;
			try {
				url = new URL(path);
			URLConnection conn = url.openConnection();
			InputStream input = conn.getInputStream();
			String jsonString = InputStreamTOString(input);
			return jsonString;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
	  } 
	  private  void setOrderPageInfo(final ArrayList<PayOrderBean> orderNumList,final View orderPageView){
			for (int i = 0; i < orderNumList.size(); i++) {
				orderPageView.findViewById(lineItems[i]).setVisibility(View.VISIBLE);
			TextView ductText=	(TextView) orderPageView.findViewById(orderInfoItems[i]).findViewById(R.id.order_duct);
			ductText.setText(orderNumList.get(i).getName());
			TextView numbText=	(TextView) orderPageView.findViewById(orderInfoItems[i]).findViewById(R.id.order_numb);
			numbText.setText(orderNumList.get(i).getNum());
			TextView priceText=	(TextView) orderPageView.findViewById(orderInfoItems[i]).findViewById(R.id.order_price);
			priceText.setText(orderNumList.get(i).getPrice());
			TextView kindText=	(TextView) orderPageView.findViewById(orderInfoItems[i]).findViewById(R.id.order_kind);
			String kind = orderNumList.get(i).getKind();
			if(kind.equals("tvplay")){
				kindText.setText(aQuery.getContext().getResources().getString(R.string.order_kind_tv));
			}
			if(kind.equals("music")){
				kindText.setText(aQuery.getContext().getResources().getString(R.string.order_kind_music));
			}
			if(kind.equals("movie")){
				kindText.setText(aQuery.getContext().getResources().getString(R.string.order_kind_movie));
			}
			TextView dateText=	(TextView) orderPageView.findViewById(orderInfoItems[i]).findViewById(R.id.order_date);
			dateText.setText(orderNumList.get(i).getOrderDate());
			orderPageView.findViewById(deleItems[i]).setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					int k = 0;
					int id = v.getId();
					for (int j = 0; j < lineItems.length; j++) {
						if(id==deleItems[j]){
							k=j;
						}
					}
					final int n =k;
					final String olid=orderNumList.get(k).getOlid();
					 final int deleid = lineItems[k];
					pd = new ProgressDialog(aQuery.getContext());
					String delePath =HttpRequest.URL_QUERY_LIST_DELE_ORDER+olid;
					System.out.println("dele的地址为"+delePath);
					System.out.println("删除的lineid号为"+deleId);
					pd.show();
					aQuery.ajax(delePath, String.class, new AjaxCallback<String>() {
						@Override
						public void callback(String url, String json,
								AjaxStatus status) {
							if(json!=null){
								pd.dismiss();
								String info = null;
								JSONObject js = null;
								try {
									js = new JSONObject(json);
									info =js.getString("success");
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								if(info.equals("true")){
									int totalPrice = 0;
									Toast.makeText(aQuery.getContext(), "删除成功", 1).show();
									String myPath = HttpRequest.URL_QUERY_LIST_ORDER + pageCount;
									setOrderPage(myPath);
								}else{
									Toast.makeText(aQuery.getContext(), "删除失败，请重试", 1).show();
								}
							}else{
								pd.dismiss();
								Toast.makeText(aQuery.getContext(),
										"Error:" + status.getCode(), Toast.LENGTH_LONG)
										.show();
							}
						
						}});
					
				}
			});
			}
	  }
	  private  void UpdatePaging() {
			View btn_pre = orderPageView .findViewById(R.id.order_pagepre);
			btn_pre.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					
					if (page.isFirstPage()) {
						pageCount = 1;
						Toast.makeText(aQuery.getContext(), "已经是第一页了", 1).show();
					} else {
						pageCount = page.getCurrentPage()-1;
						String myPath = HttpRequest.URL_QUERY_LIST_ORDER + pageCount;
						setOrderPage(myPath);
						System.out.println("TruePATH===========" + myPath);
					}
					}
				
			});
			View btn_next =  orderPageView.findViewById(R.id.order_pagenext);
			btn_next.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if(!page.isLastPage()){
						pageCount = page.getCurrentPage() + 1;
						String myPath = HttpRequest.URL_QUERY_LIST_ORDER + pageCount;
						setOrderPage(myPath);
					}else{
					Toast.makeText(aQuery.getContext(), "已经为最后一页了", 1).show();
				}
				}
			});

		}

}
