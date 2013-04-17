package com.ccdrive.moviestore.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.ccdrive.moviestore.R;
import com.ccdrive.moviestore.bean.OrderBean;

public class DownLoadListAdapter extends BaseAdapter
{
	// ����Context
	private Context		mContext;
	ArrayList<OrderBean> list;
	private LayoutInflater inflater;
	private Fragment fragment;
	// ������������ ��ͼƬԴ

	public DownLoadListAdapter(Context c,Fragment fragment,ArrayList<OrderBean> list)
	{
		mContext = c;
		this.list = list;
		inflater = LayoutInflater.from(mContext);
		this.fragment = fragment;
	}

	// ��ȡͼƬ�ĸ���
	public int getCount()
	{
		return list.size();
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
			convertView = inflater.inflate(R.layout.download_item, null);
			holder.name = (TextView) convertView
			        .findViewById(R.id.name);
			holder.version = (TextView) convertView
			         .findViewById(R.id.verson);
			holder.button = (Button) convertView
			        .findViewById(R.id.download_single);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.name.setText(list.get(position).getName());
		holder.version.setText(list.get(position).getVersion());
		holder.button.setOnClickListener((OnClickListener) fragment);
		holder.button.setTag(list.get(position).getSid());
		return convertView;
	}
	
	class ViewHolder {
		TextView name;
		TextView version;
		Button button;
	}

}