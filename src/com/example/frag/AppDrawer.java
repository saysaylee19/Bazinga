package com.example.frag;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

public class AppDrawer extends Activity {

	GridView mgrid;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_app_drawer);
		mgrid=(GridView)findViewById(R.id.gvMain);
		
		final ArrayList<AppInfo> listAppInfo=new ArrayList<AppInfo>();
		List<PackageInfo>pack=getPackageManager().getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);
		for(int i=0;i<pack.size();i++){
			PackageInfo p=pack.get(i);
			if ((pack.get(i).applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 1) {
		            continue ;
		        }
			
			AppInfo a=new AppInfo();
			a.mIcon=p.applicationInfo.loadIcon(getPackageManager());
			a.mName=p.applicationInfo.loadLabel(getPackageManager()).toString();
			a.pckName=p.packageName;
			//Log.v("DATA",a.mName+"");
			listAppInfo.add(a);
		}
	    
		mgrid.setAdapter(new AppInfoAdapter(this,listAppInfo));
		mgrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> adapterView, View view, int position, long l){
				Log.v("Alert", listAppInfo.get(position).getName()+"");
				Intent launchers=getPackageManager().getLaunchIntentForPackage(listAppInfo.get(position).getPckName());
			    startActivity(launchers);
			}
		});
			
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
