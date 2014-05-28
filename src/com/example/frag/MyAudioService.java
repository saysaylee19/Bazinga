package com.example.frag;

import java.io.IOException;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Binder;
import android.os.IBinder;
import android.provider.MediaStore;
import android.util.Log;



public class MyAudioService extends Service implements OnPreparedListener {

	private MediaPlayer mPlayer = new MediaPlayer();
	private final IBinder mBinder = new MyBinder();
	
	
	private final String TAG = "MyAudioService";
	
	private String[] projection = {
		MediaStore.Audio.Media._ID,
		MediaStore.Audio.Media.DATA,
		MediaStore.Audio.Media.ARTIST,
		MediaStore.Audio.Media.TITLE
	};
	
	int dataColumnIndex, titleColumnIndex;
	String songTitle;
	
	enum ServiceState {
		retrieving, preparing, playing, paused, stopped, prepared
	};
	ServiceState serviceState;
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		
		
	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		
		Log.d(TAG, "here");
//		serviceState = ServiceState.retrieving;
//		
		return Service.START_STICKY;
		
	}
	
	@Override
	public void onDestroy() {
		if(mPlayer != null){
			mPlayer.release();
		}
		super.onDestroy();
	}

	public class MyBinder extends Binder{
		MyAudioService getService(){
			return MyAudioService.this;
		}
	}
	SongInfo info;
	ContentResolver resolver;
	Cursor cursor;
	@Override
	public IBinder onBind(Intent intent) {
		Log.d(TAG, "retrieving");
	//	mPlayer = new MediaPlayer();
		
		mPlayer.setOnPreparedListener(this);
		resolver = getContentResolver();
		cursor = resolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection, null, null, null);
		cursor.moveToFirst();
		info = new SongInfo();
		copySongInfo(info);
		
		dataColumnIndex = cursor.getColumnIndex(MediaStore.Audio.Media.DATA);
		titleColumnIndex = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);	
		try {
			mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mPlayer.setDataSource(cursor.getString(dataColumnIndex));
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch(Exception e){
			e.printStackTrace();
		}
		
		try {
			mPlayer.prepareAsync();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		}
		serviceState = ServiceState.preparing;
		Log.d(TAG, "preparing");
		
//		mPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
//		mPlayer.
		
		
		return mBinder;
	}
	
	public void copySongInfo(SongInfo i){
		i.set_ID(cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media._ID)));
		i.setTitle(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)));
		i.setArtist(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)));
		
	}
	
	public String getPlayingString(){
		return "Playing";
	}
	
	boolean currentlyPlaying = false;
	
	@Override
	public void onPrepared(MediaPlayer arg0) {
		serviceState = ServiceState.prepared;
		Log.d(TAG, "prepared");
		
		if (currentlyPlaying){
			
			mPlayer.start();
			info.setPlaying(true);
			updateNotification();
		}
		
	}

	public SongInfo startPlaying() {
		Log.d(TAG, "startPlaying");
		if(serviceState == ServiceState.playing){
			mPlayer.pause();
			serviceState = ServiceState.paused;
			Log.d(TAG, "paused");
			info.setPlaying(true);
			updateNotification();
			return info;
			
		} else if(serviceState == ServiceState.prepared || serviceState == ServiceState.paused){
			mPlayer.start();
			currentlyPlaying = true;
			serviceState = ServiceState.playing;
			Log.d(TAG, "playing");
			info.setPlaying(false);
			updateNotification();
			return info;
		}
		return null;
	}
	
	public SongInfo nextTrack(){
		Log.d(TAG, "nextTrack");
		mPlayer.stop();
		mPlayer.reset();
		serviceState = ServiceState.stopped;
		if(!cursor.moveToNext()){
			cursor.moveToFirst();
		}
		copySongInfo(info);
		
		try {
			mPlayer.setDataSource(cursor.getString(dataColumnIndex));
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mPlayer.setOnPreparedListener(this);
		mPlayer.prepareAsync();
		serviceState = ServiceState.preparing;
		
		return info;
						
	}
	
	public SongInfo previousTrack(){
		Log.d(TAG, "previousTrack");
		mPlayer.stop();
		mPlayer.reset();
		serviceState = ServiceState.stopped;
		
		if(!cursor.moveToPrevious()){
			cursor.moveToLast();
		}
		copySongInfo(info);
		
		try {
			mPlayer.setDataSource(cursor.getString(dataColumnIndex));
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		mPlayer.setOnPreparedListener(this);
		mPlayer.prepareAsync();
		serviceState = ServiceState.preparing;	
		
		return info;
		
	}

	private void updateNotification() {
		songTitle = cursor.getString(titleColumnIndex);
		String status = "";
		if(serviceState == ServiceState.playing)
			status = "Playing";
		else if(serviceState == ServiceState.paused)
			status = "Paused";

		PendingIntent pi = PendingIntent.getActivity(getApplicationContext(), 0, new Intent(getApplicationContext(), MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
		Notification notification = new Notification();
		notification.tickerText = songTitle;
		notification.icon = R.drawable.ic_launcher;
		notification.flags |= Notification.FLAG_ONGOING_EVENT;
		notification.setLatestEventInfo(getApplicationContext(), status, songTitle, pi);
		startForeground(1, notification);
		
		
//		Notification notification = new Notification.Builder(getApplicationContext())
//							.setContentTitle(status + " " + songTitle)
//							.setSmallIcon(R.drawable.ic_launcher)
//							.build();
	}
	
	
}

