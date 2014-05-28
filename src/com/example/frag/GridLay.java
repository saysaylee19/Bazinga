package com.example.frag;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.frag.SimpleGestureFilter.SimpleGestureListener;

public class GridLay extends Activity implements SimpleGestureListener{
	private SimpleGestureFilter detector;
	ImageView iv;
	ImageButton ib;
	GridView igrid;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_grid_lay);
		detector = new SimpleGestureFilter(this,this);
		Log.v("Alert", "After detector");
		iv=(ImageView)findViewById(R.id.imageView1);
		igrid=(GridView)findViewById(R.id.gvMainIcon);
		Log.v("Alert", "After igrid");
		final GridAdapter gadptr=new GridAdapter(this);
		Log.v("Alert", "After gadptr"+gadptr.toString());
		igrid.setAdapter(gadptr);
		Log.v("Alert", "After setadapter");
		igrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
		public void onItemClick(AdapterView<?> adapterView, View view, int position, long l){
			Log.v("Data",gadptr.getItem(position)+"");
			if(gadptr.getItem(position).equals(2130837523)){
				Log.v("Data", "Yess");
				Intent launcher=getPackageManager().getLaunchIntentForPackage("com.android.contacts");
			    startActivity(launcher);
			    }
				else if(gadptr.getItem(position).equals(2130837506)){
					Log.v("Data", "Yess");
					Intent launcher=getPackageManager().getLaunchIntentForPackage("com.sec.android.app.camera");
				    startActivity(launcher);
				}
				else if(gadptr.getItem(position).equals(2130837510)){
					Log.v("Data", "Yess");
					Intent launcher=getPackageManager().getLaunchIntentForPackage("com.google.android.gm");
				    startActivity(launcher);
				}
				else if(gadptr.getItem(position).equals(2130837504)){
					Log.v("Data", "Yess");
					Intent launcher=getPackageManager().getLaunchIntentForPackage("com.android.vending");
				    startActivity(launcher);
				}
				else if(gadptr.getItem(position).equals(2130837511)){
					Log.v("Data", "Yess");
					Intent launcher=getPackageManager().getLaunchIntentForPackage("com.facebook.katana");
				    startActivity(launcher);
				}
				else if(gadptr.getItem(position).equals(2130837513)){
					Log.v("Data", "Yess");
					Intent launcher=getPackageManager().getLaunchIntentForPackage("com.android.mms");
				    startActivity(launcher);
				}
		
			
		}
	});
		
		
			
		
		
		
		ib=(ImageButton)findViewById(R.id.button1);
		
		
	

    
    
    
    
    
    

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.grid_lay, menu);
		return true;
	}

	@Override
	public void onSwipe(int direction) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDoubleTap() {
		// TODO Auto-generated method stub
		
	}

}
