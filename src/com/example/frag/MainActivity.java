package com.example.frag;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import com.example.frag.SimpleGestureFilter.SimpleGestureListener;
import com.example.frag.Smart.GetAppCategorizationsTask;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ActivityManager;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.app.ActivityManager.RecentTaskInfo;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.*;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.SlidingDrawer;
import android.widget.TabHost;
import android.widget.Toast;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TabHost.TabContentFactory;

@SuppressLint("NewApi")
public class MainActivity extends FragmentActivity implements TabHost.OnTabChangeListener, ViewPager.OnPageChangeListener,SimpleGestureListener
,OnQueryTextListener{
	//public static FragmentTransaction fragmentTransaction;
    //public static Tab1Fragment fragmentActivity1;
  //  public static FragmentActivity2 fragmentActivity2;
	GridView mrugrid;
	GridView appgrid;
	int slideflag=0;
	private SimpleGestureFilter detector;
	//public static FragmentManager fragmentManager;
    private TabHost mTabHost;
    private ViewPager mViewPager;
    private HashMap<String, TabInfo> mapTabInfo = new HashMap<String, MainActivity.TabInfo>();
    private PagerAdapter mPagerAdapter;
    /**
     *
     * @author mwho
     * Maintains extrinsic info of a tab's construct
     */
    private class TabInfo {
         private String tag;
         private Class<?> clss;
         private Bundle args;
         private Fragment fragment;
         TabInfo(String tag, Class<?> clazz, Bundle args) {
             this.tag = tag;
             this.clss = clazz;
             this.args = args;
         }
 
    }
    /**
     * A simple factory that returns dummy views to the Tabhost
     * @author mwho
     */
    class TabFactory implements TabContentFactory {
 
        private final Context mContext;
 
        /**
         * @param context
         */
        public TabFactory(Context context) {
            mContext = context;
        }
 
        /** (non-Javadoc)
         * @see android.widget.TabHost.TabContentFactory#createTabContent(java.lang.String)
         */
        public View createTabContent(String tag) {
            View v = new View(mContext);
            v.setMinimumWidth(0);
            v.setMinimumHeight(0);
            return v;
        }
 
    }
    /** (non-Javadoc)
     * @see android.support.v4.app.FragmentActivity#onCreate(android.os.Bundle)
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //NEw
    
        	//setContentView(R.layout.activity_main);
    		detector = new SimpleGestureFilter(this,this);
    		setContentView(R.layout.activity_main);
        //Click icon
    		ActionBar actionBar = getActionBar();
    	    actionBar.setDisplayHomeAsUpEnabled(true);
    		//fragmentManager = getFragmentManager();
        
        //Sliding drawer
    		this.initSlidingDrawer();
        
        //Sliding drawer end
        //Appdrawer
    		this.initAppDraw();
    		
        // Inflate the layout
        Log.v("ALERT","I m in main");
        
       // Initialise the TabHost
        this.initialiseTabHost(savedInstanceState);
        if (savedInstanceState != null) {
            mTabHost.setCurrentTabByTag(savedInstanceState.getString("tab")); //set the tab as per the saved state
        }
        // Intialise ViewPager
        this.intialiseViewPager();
        handleIntent(getIntent());
        
        //fragact();
        
    }
 
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home:
        	Log.v("ALERT","App Clicked");
        	Intent l=new Intent(this,Smart.class);
			startActivity(l);  
			
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }
    private void handleIntent(Intent intent) {
		Log.d("handleIntent", intent.getAction());
		if(Intent.ACTION_VIEW.equals(intent.getAction())){
			Log.d("handleIntent", "here!!!");
			Uri uri = intent.getData();
			Intent i = new Intent(intent.getAction());
			i.setData(uri);
			startActivity(i);
			this.finish();
			
		}
		
	}

    
    
    /** (non-Javadoc)
     * @see android.support.v4.app.FragmentActivity#onSaveInstanceState(android.os.Bundle)
     */
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("tab", mTabHost.getCurrentTabTag()); //save the tab selected
        super.onSaveInstanceState(outState);
    }
    SearchView mSearchView;
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		
		mSearchView = (SearchView) menu.findItem(R.id.search).getActionView();
		setupSearchView(mSearchView);
		return true;
	}
	private void setupSearchView(SearchView mSearchView) {
		// TODO Auto-generated method stub
		SearchManager mSearchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		if(mSearchManager!=null){
			List<SearchableInfo> searchables = mSearchManager.getSearchablesInGlobalSearch();
			
			for(SearchableInfo i:searchables){
				Log.d("setupSearchView", i.getSuggestAuthority());
				if(i.getSuggestAuthority()!=null && i.getSuggestAuthority().equalsIgnoreCase("applications")){
					Log.d("->", i.getSuggestPackage());
					Log.d("->", i.getSuggestIntentAction());
					mSearchView.setSearchableInfo(i);
//					break;
				}
			}
			
			//mSearchView.setSearchableInfo(mSearchManager.getSearchableInfo(getComponentName()));
		}
		//mSearchView.
		mSearchView.setOnQueryTextListener(this);
		//mSearchView.setOnSuggestionListener(this);
	}

	
	@Override
	public boolean onSearchRequested() {
		startSearch(null, false, null, true);
		return true;
	}
	@Override
	public boolean onQueryTextChange(String newText) {
		Log.d("onQueryTextChange", newText);
	//	onSearchRequested();
	//	Log.d("onQueryTextChange", "onSearchRequestedReturned "+ newText);
		return true;
	}

	@Override
	public boolean onQueryTextSubmit(String query) {
		Log.d("onQueryTextSubmit", query);
		return false;
	}

    /**
     * Initialise ViewPager
     */
    private void intialiseViewPager() {
 
        List<Fragment> fragments = new Vector<Fragment>();
        fragments.add(Fragment.instantiate(this, Tab1Fragment.class.getName()));
        fragments.add(Fragment.instantiate(this, Tab2Fragment.class.getName()));
        fragments.add(android.support.v4.app.ListFragment.instantiate(this, Tab3Fragment.class.getName()));
        fragments.add(Fragment.instantiate(this, Tab4Fragment.class.getName()));
        fragments.add(Fragment.instantiate(this, Tab5Fragment.class.getName()));
        
        
        this.mPagerAdapter  = new TabsPagerAdapter(super.getSupportFragmentManager(), fragments);
        //
        this.mViewPager = (ViewPager)super.findViewById(R.id.viewpager);
        this.mViewPager.setAdapter(this.mPagerAdapter);
        this.mViewPager.setOnPageChangeListener(this);
    }
    
    private void initAppDraw(){
    	appgrid=(GridView)findViewById(R.id.gvMain);
		
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
	    
		appgrid.setAdapter(new AppInfoAdapter(this,listAppInfo));
		appgrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> adapterView, View view, int position, long l){
				Log.v("Alert", listAppInfo.get(position).getName()+"");
				Intent launchers=getPackageManager().getLaunchIntentForPackage(listAppInfo.get(position).getPckName());
			    startActivity(launchers);
			}
		});
			
		

    	
    	
    	
    }
    
    
    
    
    
    
    
    private void initSlidingDrawer(){
    	mrugrid=(GridView)findViewById(R.id.gvMru);
		List<PackageInfo>pck=getPackageManager().getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);
		ActivityManager am=(ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
		//List< ActivityManager.RunningTaskInfo > taskInfo = am.getRunningTasks();
		List<RecentTaskInfo> rt=am.getRecentTasks(10,ActivityManager.RECENT_WITH_EXCLUDED);
		final ArrayList<AppInfo>applist=new ArrayList<AppInfo>();
		
		for(int i=1;i<7;i++){
			RecentTaskInfo r=rt.get(i);
			if(r.equals(null)){
				
			}else{
			String pname=r.baseIntent.getComponent().getPackageName();
			for(int j=0;j<pck.size();j++){
				PackageInfo p=pck.get(j);
				//if ((pck.get(i).applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 1) {
		          //  continue ;
		        //}
				if(p.packageName.equals(pname)){
					//Log.v("**","EQUAL");
					AppInfo ap=new AppInfo();
					ap.mIcon=p.applicationInfo.loadIcon(getPackageManager());
					ap.mName=p.applicationInfo.loadLabel(getPackageManager()).toString();
					ap.pckName=pname;
					
					applist.add(ap);
					Log.v("RECENT","Recent task name"+ap.mName);
					
				}
			}
			//Log.v("RECENT","Recent task-"+r.baseIntent.getComponent().getPackageName());
			
		}
		Log.v("***", "Before");
		
		mrugrid.setAdapter(new AppInfoAdapter(this, applist));
		Log.v("***", "After");
		mrugrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> adapterView, View view, int position, long l){
				Log.v("Alert", applist.get(position).getName()+"");
				Intent launchers=getPackageManager().getLaunchIntentForPackage(applist.get(position).getPckName());
			    startActivity(launchers);
			}	
			
		});
		
		
	
		}
		

    }
    
    
 
    /**
     * Initialise the Tab Host
     */
    private void initialiseTabHost(Bundle args) {
    	
    	Log.v("Alert","In initialise tab host");
    	
        mTabHost = (TabHost)findViewById(android.R.id.tabhost);
        Log.v("Alert", mTabHost.toString());
        mTabHost.setup();
   
        
        Log.v("Alert","In initialise tab host");
        TabInfo tabInfo = null;
        MainActivity.AddTab(this, this.mTabHost, this.mTabHost.newTabSpec("Home").setIndicator("Home"), ( tabInfo = new TabInfo("Tab1", Tab1Fragment.class, args)));
        this.mapTabInfo.put(tabInfo.tag, tabInfo);
        MainActivity.AddTab(this, this.mTabHost, this.mTabHost.newTabSpec("Media").setIndicator("Media"), ( tabInfo = new TabInfo("Tab2", Tab2Fragment.class, args)));
        this.mapTabInfo.put(tabInfo.tag, tabInfo);
        MainActivity.AddTab(this, this.mTabHost, this.mTabHost.newTabSpec("RSS").setIndicator("RSS"), ( tabInfo = new TabInfo("Tab3", Tab3Fragment.class, args)));
        this.mapTabInfo.put(tabInfo.tag, tabInfo);
        MainActivity.AddTab(this, this.mTabHost, this.mTabHost.newTabSpec("Gallery").setIndicator("Gallery"), ( tabInfo = new TabInfo("Tab4", Tab4Fragment.class, args)));
        this.mapTabInfo.put(tabInfo.tag, tabInfo);
        MainActivity.AddTab(this, this.mTabHost, this.mTabHost.newTabSpec("Weather").setIndicator("Weather"), ( tabInfo = new TabInfo("Tab5", Tab5Fragment.class, args)));
        this.mapTabInfo.put(tabInfo.tag, tabInfo);
        
        // Default to first tab
        //this.onTabChanged("Tab1");
        //
        mTabHost.setOnTabChangedListener(this);
    }
 
    /**
     * Add Tab content to the Tabhost
     * @param activity
     * @param tabHost
     * @param tabSpec
     * @param clss
     * @param args
     */
    private static void AddTab(MainActivity activity, TabHost tabHost, TabHost.TabSpec tabSpec, TabInfo tabInfo) {
        // Attach a Tab view factory to the spec
        tabSpec.setContent(activity.new TabFactory(activity));
        tabHost.addTab(tabSpec);
    }
 
    /** (non-Javadoc)
     * @see android.widget.TabHost.OnTabChangeListener#onTabChanged(java.lang.String)
     */
    public void onTabChanged(String tag) {
    	int []imgid={R.drawable.flash,R.drawable.bat,R.drawable.iron2,R.drawable.super2,
    			R.drawable.hulk};
        //TabInfo newTab = this.mapTabInfo.get(tag);
    	ImageView iv=(ImageView) findViewById(R.id.imageView1);
    	
        int pos = this.mTabHost.getCurrentTab();
       
        	
        Log.v("TAB", "current ab pos"+pos);
        iv.setImageResource(imgid[pos]);
        this.mViewPager.setCurrentItem(pos);
    }
 
    /* (non-Javadoc)
     * @see android.support.v4.view.ViewPager.OnPageChangeListener#onPageScrolled(int, float, int)
     */
    @Override
    public void onPageScrolled(int position, float positionOffset,
            int positionOffsetPixels) {
        // TODO Auto-generated method stub
 
    }
 
    /* (non-Javadoc)
     * @see android.support.v4.view.ViewPager.OnPageChangeListener#onPageSelected(int)
     */
    @Override
    public void onPageSelected(int position) {
    	 
        // TODO Auto-generated method stub
        this.mTabHost.setCurrentTab(position);
    }
 
    /* (non-Javadoc)
     * @see android.support.v4.view.ViewPager.OnPageChangeListener#onPageScrollStateChanged(int)
     */
    @Override
    public void onPageScrollStateChanged(int state) {
        // TODO Auto-generated method stub
 
    }
    public void appDrawer(View view){
		Log.v("Print","Function call hua");
		Intent intent=new Intent(this,AppDrawer.class);
		Log.d("print", intent.toString());
		startActivity(intent);
	}
	public boolean dispatchTouchEvent(MotionEvent me){
	    // Call onTouchEvent of SimpleGestureFilter class
	     this.detector.onTouchEvent(me);
	   return super.dispatchTouchEvent(me);
	}
	public void handleSlide(){
		slideflag=1;
		SlidingDrawer sd=(SlidingDrawer) findViewById(R.id.slidingDrawer2);
		sd.open();
	}
	public void closeSlide(){
		SlidingDrawer sd=(SlidingDrawer) findViewById(R.id.slidingDrawer2);
		if(slideflag==1){
			slideflag=0;
		sd.close();
		}
	}
	
	@Override
	public void onSwipe(int direction) {
		 String str = "";
		  
		  switch (direction) {
		  case SimpleGestureFilter.SWIPE_DOWN :  str = "Swipe Down";
		  closeSlide();
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
		  										int pos = this.mTabHost.getCurrentTab();
		  										if(pos==2){
		  								         	SlidingDrawer sd2=(SlidingDrawer) findViewById(R.id.slidingDrawer2);
		  								         	sd2.close();
		  								         }
		  										else
		  										 handleSlide();
		  										 //this.startActivity(intent);
		                                                 break;
		  
		  }
		   Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
		 
		
	}

	@Override
	public void onDoubleTap() {
		  Toast.makeText(this, "Double Tap", Toast.LENGTH_SHORT).show();
		
	}
	
	public void onBackPressed(){
		if(slideflag==1)
			closeSlide();
		else
			finish();
	}
	
	protected void onResume() {
		// TODO Auto-generated method stub
		 super.onResume();
	//	setContentView(R.layout.activity_main);
	}
}