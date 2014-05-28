package com.example.frag;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;

public class GridAdapter extends BaseAdapter {
	
	private Context context;
	private Integer[]imgid={R.drawable.phonealt,R.drawable.camera,R.drawable.email,
			R.drawable.appstore,R.drawable.facebook,R.drawable.gtalk};
	
	//List<PackageInfo>pack=context.getPackageManager().getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);
	
	public GridAdapter(Context c){
		context=c;
	}
	
	
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return imgid.length;
	}
	@Override
	public long getItemId(int pos) {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public View getView(int pos, View view, ViewGroup parent) {
		Log.v("Alert", "In grid adapetr getview");
		
		ImageView ibt;
		/*for(int i=0;i<pack.size();i++){
			PackageInfo p=pack.get(i);
			if ((pack.get(i).applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 1) {
		            continue ;
		        }
		
		List<String>icons=new ArrayList<String>();
		icons.add(p.applicationInfo.loadLabel(context.getPackageManager()).toString());
		Log.v("APP","Info"+icons.get(i));
		}*/
		if(view==null){
			ibt=new ImageView(context);
			ibt.setLayoutParams(new GridView.LayoutParams(125,150));
			ibt.setPadding(5,5,5,5);
			}
		else
			ibt=(ImageView)view;
		ibt.setImageResource(imgid[pos]);
		return ibt;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return imgid[position];
	}
	
	

	
	}
	
	
	


