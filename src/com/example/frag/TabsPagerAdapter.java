package com.example.frag;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
 
/**
 * The <code>PagerAdapter</code> serves the fragments when paging.
 * @author mwho
 */
public class TabsPagerAdapter extends FragmentPagerAdapter {
 
    public TabsPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
		super(fm);
		this.fragments=fragments;
	}

	private List<Fragment> fragments;
    /**
     * @param fm
     * @param fragments
     */
    /* (non-Javadoc)
     * @see android.support.v4.app.FragmentPagerAdapter#getItem(int)
     */
    @Override
    public Fragment getItem(int position) {
        return this.fragments.get(position);
    }
 
    /* (non-Javadoc)
     * @see android.support.v4.view.PagerAdapter#getCount()
     */
    @Override
    public int getCount() {
        return this.fragments.size();
    }
}