package com.example.frag;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.frag.SimpleGestureFilter.SimpleGestureListener;



public class MediaPlayer extends FragmentActivity implements SimpleGestureListener{
	
	
	private SimpleGestureFilter detector;
	TextView songTitle, songArtist;
	ImageButton previous, play, next;
	ImageView albumArtwork;
	
	boolean isBound = false;
	
	
	private PowerManager.WakeLock wl;
	private PowerManager pm;
	
		
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v("ALERT", "In MediaPlayer");
        setContentView(R.layout.activity_media_player);
        detector = new SimpleGestureFilter(this,this);
        
        songTitle = (TextView) findViewById(R.id.textView1);
        songArtist = (TextView) findViewById(R.id.textView2);
        albumArtwork = (ImageView) findViewById(R.id.imageView1);
        
        previous = (ImageButton) findViewById(R.id.previousBtn);
        play = (ImageButton) findViewById(R.id.playBtn);
        next = (ImageButton) findViewById(R.id.nextBtn);
        
        previous.setOnClickListener(previousClickListener);
        play.setOnClickListener(playClickListener);
        next.setOnClickListener(nextClickListener);
        pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "sleeplock");
		wl.acquire();
    }
    
    @Override
    protected void onDestroy() {
    	super.onStop();
		wl.release();
    	if (isBound){
    		unbindService(conn);
    	}
    }
    @Override
    public void onBackPressed() {
    	moveTaskToBack(true);
    	//new Finalizer().killApp(false);
    }
    MyAudioService s;
    @Override
    protected void onResume() {
    	super.onResume();
    	Intent intent = new Intent(this, MyAudioService.class);
    	bindService(intent, conn, Context.BIND_AUTO_CREATE);
    }

    private ServiceConnection conn = new ServiceConnection() {
		
		@Override
		public void onServiceDisconnected(ComponentName name) {
			s = null;
		}
		
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			MyAudioService.MyBinder b = (MyAudioService.MyBinder) service;
			s = b.getService();
			isBound = true;
			Toast.makeText(getApplicationContext(), "Connected", Toast.LENGTH_LONG).show();
		}
	};
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.media_player, menu);
        return true;
    }
    SongInfo info;
    boolean check = false;
    private OnClickListener playClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			v.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP);
			
			info = s.startPlaying();
			if(info != null){
				updateSongInfoViews();
				if (!info.isPlaying()){
					((ImageButton)v).setImageResource(R.drawable.ic_action_pause);
				} else{
					((ImageButton)v).setImageResource(R.drawable.ic_action_play);
				}
			}
			Log.d("playClickListener", "clicked");
		}
	};
	
	
	
	private OnClickListener previousClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			v.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP);
			info = s.previousTrack();
			if(info != null){
				updateSongInfoViews();
				
			}
		}
	};
	
	private OnClickListener nextClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			v.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP);
			info = s.nextTrack();
			if(info != null){
				updateSongInfoViews();
				
			}
		}
	};


	protected void updateSongInfoViews() {
		songTitle.setText(info.getTitle());
		songArtist.setText(info.getArtist());
		
//		Uri artworkUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
//		Uri uri = ContentUris.withAppendedId(artworkUri, info.get_ID());
//		ContentResolver resolver = getContentResolver();
//		
//		try {
//			InputStream in = resolver.openInputStream(uri);
//			Bitmap artwork = BitmapFactory.decodeStream(in);
//			albumArtwork.setImageBitmap(artwork);
//			
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
	}
	public boolean dispatchTouchEvent(MotionEvent me){
	    // Call onTouchEvent of SimpleGestureFilter class
	     this.detector.onTouchEvent(me);
	   return super.dispatchTouchEvent(me);
	}
	public void appDrawer(View view){
		Log.v("Print","Function call hua");
		Intent intent=new Intent(this,AppDrawer.class);
		Log.d("print", intent.toString());
		startActivity(intent);
	}


	public void onSwipe(int direction) {
		String str = "";
		  
		  switch (direction) {
		  
		  /*case SimpleGestureFilter.SWIPE_RIGHT : str = "Swipe Right";
		  
		  Intent intent1=new Intent(this,MainActivity.class);
			Log.v("Error", intent1+"");
				this.startActivity(intent1);
		                                         break;
		  case SimpleGestureFilter.SWIPE_LEFT :  str = "Swipe Left";
		  			
		  											break;
		  */
		  case SimpleGestureFilter.SWIPE_DOWN :  str = "Swipe Down";
		  Object sbservice = getSystemService( "statusbar" );
			Class<?> statusbarManager = null;
			try {
				statusbarManager = Class.forName( "android.app.StatusBarManager" );
				Log.v("Alert", ""+statusbarManager.getName());
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		  Method showsb = null;
		  if (Build.VERSION.SDK_INT >= 17) {
		      try {
				showsb = statusbarManager.getMethod("expandNotificationsPanel");
				Log.v("Alert", "secnd catch");
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		  }
		  else {
		      try {
				showsb = statusbarManager.getMethod("expand");
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		  }
			try {
				showsb.invoke( sbservice );
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 break;
		  case SimpleGestureFilter.SWIPE_UP :    str = "Swipe Up";
		  										 Intent intent=new Intent(this,AppDrawer.class);
		  										 this.startActivity(intent);
		                                                 break;
		  
		  }
		   Toast.makeText(this, str, Toast.LENGTH_SHORT).show();

		
	}

	@Override
	public void onDoubleTap() {
		Toast.makeText(this, "Double Tap", Toast.LENGTH_SHORT).show();
		
	}
	
	
}
