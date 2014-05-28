package com.example.frag;








import android.annotation.TargetApi;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Build.VERSION_CODES;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

public class Tab4Fragment extends Fragment{
	private SimpleGestureFilter detector;
	ImageView iv;
	int count=0;
	ImageButton next,prev;
	int []imgid={R.drawable.leg,R.drawable.tree,R.drawable.cake,R.drawable.gift,
			R.drawable.lyts,R.drawable.purse,R.drawable.snowtree,R.drawable.watch};
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		if (BuildConfig.DEBUG) {
            Utils.enableStrictMode();
        }
		if (container == null) {
            // We have different layouts, and in one of them this
            // fragment's containing frame doesn't exist.  The fragment
            // may still be created from its saved state, but there is
            // no reason to try to create its view hierarchy because it
            // won't be displayed.  Note this is not needed -- we could
            // just run the code below, where we would create and return
            // the view hierarchy; it would just never be used.
            return null;
        }
		View v=inflater.inflate(R.layout.activity_gallery, container, false);
		//detector = new SimpleGestureFilter(getActivity(),getActivity());
		//getActivity().setContentView(R.layout.activity_gallery);
		
		Log.v("@@@@@", v.toString());
		iv=(ImageView)v.findViewById(R.id.imageView);
		
		next = (ImageButton)v.findViewById(R.id.imageButton1);
		prev = (ImageButton)v.findViewById(R.id.imageButton2);
		
		
		next.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				count++;
				if(count>=imgid.length){
					count=0;
				}
				iv.setImageResource(imgid[count]);
				
			}
		});
		
		
prev.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				count--;
				if(count<0){
					count=imgid.length-1;
				}
				iv.setImageResource(imgid[count]);
				
			}
		});
		return v;
	}
		
	
	

}