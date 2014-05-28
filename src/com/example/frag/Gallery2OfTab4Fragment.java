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
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.Toast;

public class Gallery2OfTab4Fragment extends Fragment{
	private static final String IMAGE_CACHE_DIR = "images";
    public static final String EXTRA_IMAGE = "extra_image";

    private ImagePagerAdapter mAdapter;
    private ImageFetcher mImageFetcher;
    private ViewPager mPager;

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
		View v=inflater.inflate(R.layout.image_detail_pager, container, false);
		 String[] proj = {MediaStore.Images.Media.DATA};
	        Cursor cursor = getActivity().managedQuery(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, proj, null, null, null);
	        Log.v("Cursorr",cursor.toString());
	        cursor.moveToFirst();
	        
	        Images.getFromSdCard(cursor);
	        Log.v("****","After images");
	        
	        Button btn1 = (Button) v.findViewById(R.id.button1);
	        Button btn2 = (Button) v.findViewById(R.id.button2);
	        
	        

	        // Fetch screen height and width, to use as our max size when loading images as this
	        // activity runs full screen
	        final DisplayMetrics displayMetrics = new DisplayMetrics();
	        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
	        final int height = displayMetrics.heightPixels;
	        final int width = displayMetrics.widthPixels;

	        // For this sample we'll use half of the longest width to resize our images. As the
	        // image scaling ensures the image is larger than this, we should be left with a
	        // resolution that is appropriate for both portrait and landscape. For best image quality
	        // we shouldn't divide by 2, but this will use more memory and require a larger memory
	        // cache.
	        final int longest = (height > width ? height : width) / 2;

	        ImageCache.ImageCacheParams cacheParams =
	                new ImageCache.ImageCacheParams(getActivity(), IMAGE_CACHE_DIR);
	        cacheParams.setMemCacheSizePercent(0.25f); // Set memory cache to 25% of app memory

	        // The ImageFetcher takes care of loading images into our ImageView children asynchronously
	        mImageFetcher = new ImageFetcher(getActivity(), longest);
	        mImageFetcher.addImageCache(getActivity().getSupportFragmentManager(), cacheParams);
	        mImageFetcher.setImageFadeIn(false);

	        // Set up ViewPager and backing adapter
	        mAdapter = new ImagePagerAdapter(getActivity().getSupportFragmentManager(), Images.imageUrls.length);
	        mPager = (ViewPager) v.findViewById(R.id.pager);
	        mPager.setAdapter(mAdapter);
	        mPager.setPageMargin((int) getResources().getDimension(R.dimen.image_detail_pager_margin));
	        mPager.setOffscreenPageLimit(2);
	        
	        btn1.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					mPager.setCurrentItem(mPager.getCurrentItem()-1, true);
				}
	        });
	        
	        btn2.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					mPager.setCurrentItem(mPager.getCurrentItem()+1, true);				
				}
			});
	        //DetailOnPagerListener pageChangeLister = new DetailOnPagerListener();
	        
	        //mPager.setCurrentItem(mPager.getCurrentItem()+1);

	        // Set up activity to go full screen
	        getActivity().getWindow().addFlags(LayoutParams.FLAG_FULLSCREEN);

	        // Enable some additional newer visibility and ActionBar features to create a more
	        // immersive photo viewing experience
	/*        if (Utils.hasHoneycomb()) {
	            final ActionBar actionBar = getActionBar();

	            // Hide title text and set home as up
	            actionBar.setDisplayShowTitleEnabled(false);
	            actionBar.setDisplayHomeAsUpEnabled(true);

	            // Hide and show the ActionBar as the visibility changes
	            mPager.setOnSystemUiVisibilityChangeListener(
	                    new View.OnSystemUiVisibilityChangeListener() {
	                        @Override
	                        public void onSystemUiVisibilityChange(int vis) {
	                            if ((vis & View.SYSTEM_UI_FLAG_LOW_PROFILE) != 0) {
	                                actionBar.hide();
	                            } else {
	                                actionBar.show();
	                            }
	                        }
	                    });

	            // Start low profile mode and hide ActionBar
	            mPager.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
	            actionBar.hide();
	        }*/

	        // Set the current item based on the extra passed in to this activity
	        final int extraCurrentItem = getActivity().getIntent().getIntExtra(EXTRA_IMAGE, -1);
	        if (extraCurrentItem != -1) {
	            mPager.setCurrentItem(extraCurrentItem);
	        }

		
		Log.v("ALERT","In TAB 4");
		return v;
	}
	 public void onResume() {
	        super.onResume();
	        mImageFetcher.setExitTasksEarly(false);
	 }
	    
	    public void onPause() {
	        super.onPause();
	        mImageFetcher.setExitTasksEarly(true);
	        mImageFetcher.flushCache();
	    }

	    
	    public void onDestroy() {
	        super.onDestroy();
	        mImageFetcher.closeCache();
	    }

	    @Override
	    public boolean onOptionsItemSelected(MenuItem item) {
	        switch (item.getItemId()) {
	            case android.R.id.home:
	                NavUtils.navigateUpFromSameTask(getActivity());
	                return true;
	            case R.id.clear_cache:
	                mImageFetcher.clearCache();
	                Toast.makeText(
	                        getActivity(), R.string.clear_cache_complete_toast,Toast.LENGTH_SHORT).show();
	                return true;
	        }
	        return super.onOptionsItemSelected(item);
	    }

	   
	    /**
	     * Called by the ViewPager child fragments to load images via the one ImageFetcher
	     */
	    public ImageFetcher getImageFetcher() {
	        return mImageFetcher;
	    }

	    /**
	     * The main adapter that backs the ViewPager. A subclass of FragmentStatePagerAdapter as there
	     * could be a large number of items in the ViewPager and we don't want to retain them all in
	     * memory at once but create/destroy them on the fly.
	     */
	    private class ImagePagerAdapter extends FragmentStatePagerAdapter {
	        private final int mSize;

	        public ImagePagerAdapter(FragmentManager fm, int size) {
	            super(fm);
	            mSize = size;
	        }

	        @Override
	        public int getCount() {
	            return mSize;
	        }

	        @Override
	        public Fragment getItem(int position) {
	            return ImageDetailFragment.newInstance(Images.f.get(position));
	        }
	    }

	    /**
	     * Set on the ImageView in the ViewPager children fragments, to enable/disable low profile mode
	     * when the ImageView is touched.
	     */
	    @TargetApi(VERSION_CODES.HONEYCOMB)
	    public void onClick(View v) {
	        final int vis = mPager.getSystemUiVisibility();
	        if ((vis & View.SYSTEM_UI_FLAG_LOW_PROFILE) != 0) {
	            mPager.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
	        } else {
	            mPager.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
	        }
	    }
	}




