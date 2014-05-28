package com.example.frag;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


		 
	public class AppInfoAdapter extends BaseAdapter {
	    private Context mContext;
	    private List<AppInfo> mListAppInfo;
	 
	    public AppInfoAdapter(Context context, List<AppInfo> list) {
	        Log.v("**","In AppInfo Adapter");
	    	mContext = context;
	        mListAppInfo = list;
	        Log.v("**","In After init AppInfo Adapter");
	    }
	 
	    @Override
	    public int getCount() {
	        return mListAppInfo.size();
	    }
	 
	    @Override
	    public Object getItem(int position) {
	        return mListAppInfo.get(position);
	    }
	 
	    @Override
	    public long getItemId(int position) {
	        return position;
	    }
	 
	    @Override
	    public View getView(int position, View convertView, ViewGroup parent) {
	        AppInfo entry = mListAppInfo.get(position);
	        Log.v("**","In getview of AppInfo Adapter");
	        if(convertView == null) {
	            LayoutInflater inflater = LayoutInflater.from(mContext);
	            convertView = inflater.inflate(R.layout.appinfo, null);
	        }
	 
	        ImageView ivIcon = (ImageView)convertView.findViewById(R.id.ivIcon);
	        ivIcon.setImageDrawable(entry.getIcon());
	 
	        TextView tvName = (TextView)convertView.findViewById(R.id.tvName);
	        tvName.setText(entry.getName());
	        Log.v("**","Before returning In AppInfo Adapter");
	        return convertView;
	    }
	}
