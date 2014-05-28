package com.example.frag;

import android.graphics.drawable.Drawable;

public class AppInfo {


	    public Drawable mIcon;
	    public String mName;
	    public String pckName;
	 
	    public AppInfo() {
	       
	    }
	 
	    public void setIcon(Drawable icon) {
	        this.mIcon = icon;
	    }
	    public Drawable getIcon() {
	        return this.mIcon;
	    }
	 
	    public void setName(String name) {
	        mName = name;
	    }
	    public String getName() {
	        return mName;
	    }
	    public void setPckName(String name) {
	        pckName = name;
	    }
	    public String getPckName() {
	        return pckName;
	    }
	 
	}