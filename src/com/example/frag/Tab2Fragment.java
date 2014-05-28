package com.example.frag;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SlidingDrawer;
import android.widget.TextView;
import android.widget.Toast;

public class Tab2Fragment extends Fragment {
	TextView songTitle, songArtist;
	ImageButton previous, play, next;
	ImageView albumArtwork;
	
	boolean isBound = false;
	
	
	private PowerManager.WakeLock wl;
	private PowerManager pm;

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@SuppressLint("NewApi")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
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
		
		Log.v("ALERT", "In MediaPlayer");
		View av=inflater.inflate(R.layout.activity_media_player, container, false);
        Log.v("Alerttt",av.toString());
        
        songTitle = (TextView)av.findViewById(R.id.textView1);
        songArtist = (TextView) av.findViewById(R.id.textView2);
        albumArtwork = (ImageView) av.findViewById(R.id.imageView1);
        
        previous = (ImageButton) av.findViewById(R.id.previousBtn);
        Log.v("Loggged","Previous"+previous);
        play = (ImageButton) av.findViewById(R.id.playBtn);
        next = (ImageButton) av.findViewById(R.id.nextBtn);
        
        previous.setOnClickListener(previousClickListener);
        play.setOnClickListener(playClickListener);
        next.setOnClickListener(nextClickListener);
        pm = (PowerManager) getActivity().getSystemService(Context.POWER_SERVICE);
        wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "sleeplock");
		wl.acquire();
		
		 //SlidingDrawer sd2=(SlidingDrawer) getActivity().findViewById(R.id.slidingDrawer2);
		 //sd2.unlock();
		
		return av;
	}
	 @Override
	public void onDestroy() {
	    	super.onStop();
			wl.release();
	    	if (isBound){
	    		getActivity().unbindService(conn);
	    	}
	    }
	    public void onBackPressed() {
	    	getActivity().moveTaskToBack(true);
	    	//new Finalizer().killApp(false);
	    }
	    MyAudioService s;
	    @Override
		public void onResume() {
	    	super.onResume();
	    	Intent intent = new Intent(getActivity(), MyAudioService.class);
	    	getActivity().bindService(intent, conn, Context.BIND_AUTO_CREATE);
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
				Toast.makeText(getActivity().getApplicationContext(), "Connected", Toast.LENGTH_LONG).show();
			}
		};
		
	   
	    SongInfo info;
	    boolean check = false;
	    private OnClickListener playClickListener = new OnClickListener() {
			
			@SuppressLint("NewApi")
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
			
			@SuppressLint("NewApi")
			@Override
			public void onClick(View v) {
				v.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP);
				info = s.previousTrack();
				if(info != null){
					updateSongInfoViews();
					
				}
			}
		};
		
		@SuppressLint("NewApi")
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
			
//			Uri artworkUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
//			Uri uri = ContentUris.withAppendedId(artworkUri, info.get_ID());
//			ContentResolver resolver = getContentResolver();
//			
//			try {
//				InputStream in = resolver.openInputStream(uri);
//				Bitmap artwork = BitmapFactory.decodeStream(in);
//				albumArtwork.setImageBitmap(artwork);
//				
//			} catch (FileNotFoundException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			
		}

	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		//Intent intent = new Intent(getActivity(),MediaPlayer.class);
		//startActivity(intent);
		
		
		
	}
	public void onAttach(Activity activity){
		super.onAttach(activity);
	}



}