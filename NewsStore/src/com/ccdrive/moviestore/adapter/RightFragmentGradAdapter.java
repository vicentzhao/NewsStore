package com.ccdrive.moviestore.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ccdrive.moviestore.R;
import com.ccdrive.moviestore.bean.SoftwareBean;
import com.ccdrive.moviestore.http.HttpRequest;
import com.ccdrive.moviestore.http.ImageDownloader;

public class RightFragmentGradAdapter extends BaseAdapter
{
	// ����Context
	private Context		mContext;
	ArrayList<SoftwareBean> list;
	private LayoutInflater inflater;
	private ImageDownloader imageDownloader;
	HttpRequest http;

	public RightFragmentGradAdapter(Context c,ArrayList<SoftwareBean> list)
	{
		mContext = c;
		this.list = list;
		inflater = LayoutInflater.from(mContext);
		http = new HttpRequest();
		imageDownloader = new ImageDownloader(mContext);
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
		if(list.size()>position){
			holder.info.setText(list.get(position).getName());
			String url = HttpRequest.URL_QUERY_SINGLE_IMAGE+list.get(position).getImage_path();
			if(list.get(position).getImage_path()!=null && !list.get(position).getImage_path().equals("")){
				imageDownloader.download(url, holder.imageView);
			}else{
				holder.imageView.setBackgroundDrawable(null);
				holder.imageView.setImageResource(R.drawable.grid_item);
			}
		}
		return convertView;
	}
	class ViewHolder {
		TextView info;
//		TextView count;
		ImageView imageView;
	}

}