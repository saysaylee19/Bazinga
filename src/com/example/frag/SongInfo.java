package com.example.frag;

public class SongInfo {
	
	private long _ID;
	private String title;
	private String artist;
	private boolean isPlaying;
	
	public boolean isPlaying() {
		return isPlaying;
	}
	public void setPlaying(boolean isPlaying) {
		this.isPlaying = isPlaying;
	}
	public long get_ID() {
		return _ID;
	}
	public void set_ID(long _ID) {
		this._ID = _ID;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getArtist() {
		return artist;
	}
	public void setArtist(String artist) {
		this.artist = artist;
	}
	
	

}
