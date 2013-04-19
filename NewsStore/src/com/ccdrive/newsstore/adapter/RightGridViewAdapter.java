package com.ccdrive.newsstore.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ccdrive.newsstore.R;
import com.ccdrive.newsstore.bean.SoftwareBean;

public class RightGridViewAdapter extends BaseAdapter
{
	// ����Context
	private Context		mContext;
	ArrayList<SoftwareBean> list;
	private LayoutInflater inflater;
	// ������������ ��ͼƬԴ
	private Integer[]	mImageIds	= 
	{ 
			R.drawable.grid_item, 
			R.drawable.grid_item, 
			R.drawable.grid_item, 
			R.drawable.grid_item, 
			R.drawable.grid_item, 
			R.drawable.grid_item
	};

	public RightGridViewAdapter(Context c,ArrayList<SoftwareBean> list)
	{
		mContext = c;
		this.list = list;
		inflater = LayoutInflater.from(mContext);
	}

	// ��ȡͼƬ�ĸ���
	public int getCount()
	{
		return mImageIds.length;
	}

	// ��ȡͼƬ�ڿ��е�λ��
	public Object getItem(int position)
	{
		return position;
	}


	// ��ȡͼƬID
	public long getItemId(int position)
	{
		return position;
	}


	public View getView(int position, View convertView, ViewGroup parent){
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.gridview_item, null);
			holder.info = (TextView) convertView
			        .findViewById(R.id.ItemText);
//			holder.count = (TextView) convertView
//			         .findViewById(R.id.count_tv);
			holder.imageView = (ImageView) convertView
			        .findViewById(R.id.ItemImage);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.info.setText(list.get(position).getInfo());
		return convertView;
	}
	
	class ViewHolder {
		TextView info;
//		TextView count;
		ImageView imageView;
	}

}