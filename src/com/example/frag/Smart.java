package com.example.frag;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import javax.net.ssl.HttpsURLConnection;
import org.apache.commons.collections4.MultiMap;
import org.apache.commons.collections4.map.MultiValueMap;
import org.json.JSONException;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.Toast;

public class Smart extends Activity implements OnClickListener{
	private final String accessToken = "7b377ccd4ed967f2f379c0d6b93195012729bdff";
	private final String resourceUrl = "https://42matters.com/api/1/apps/lookup.json";
	List<PackageInfo> packageList1;
	List<PackageInfo> pckinfo=new ArrayList<PackageInfo>();
	ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.smart);
 
        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.lvExp);
        
        button = (Button) findViewById(R.id.button1);
		button.setOnClickListener(this);
        
        // preparing list data
       prepareListData();
 
        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);
 
        // setting list adapter
        expListView.setAdapter(listAdapter);
     // Listview Group click listener
        expListView.setOnGroupClickListener(new OnGroupClickListener() {
 
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                    int groupPosition, long id) {
                // Toast.makeText(getApplicationContext(),
                // "Group Clicked " + listDataHeader.get(groupPosition),
                // Toast.LENGTH_SHORT).show();
                return false;
            }
        });
     // Listview Group expanded listener
        expListView.setOnGroupExpandListener(new OnGroupExpandListener() {
 
            @Override
            public void onGroupExpand(int groupPosition) {
                Toast.makeText(getApplicationContext(),
                        listDataHeader.get(groupPosition) + " Expanded",
                        Toast.LENGTH_SHORT).show();
            }
        });
 
        // Listview Group collasped listener
        expListView.setOnGroupCollapseListener(new OnGroupCollapseListener() {
 
            @Override
            public void onGroupCollapse(int groupPosition) {
                Toast.makeText(getApplicationContext(),
                        listDataHeader.get(groupPosition) + " Collapsed",
                        Toast.LENGTH_SHORT).show();
 
            }
        });
 
        // Listview on child click listener
        expListView.setOnChildClickListener(new OnChildClickListener() {
 
            @SuppressLint("NewApi")
			@Override
            public boolean onChildClick(ExpandableListView parent, View v,
                    int groupPosition, int childPosition, long id) {
                // TODO Auto-generated method stub
            	//Intent launchers=getPackageManager().getLaunchIntentForPackage(listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition));
            	//startActivity(launchers);
            	for(int i=0;i<pckinfo.size();i++){
            		if((pckinfo.get(i).applicationInfo.loadLabel(getPackageManager()).toString()).compareToIgnoreCase((listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition)))==0){
            			Intent launchers=getPackageManager().getLaunchIntentForPackage(pckinfo.get(i).packageName);
            			Log.v("Runn", pckinfo.get(i).packageName);
            			startActivity(launchers);
            			
            		}
            	}
            	Log.v("header:"+listDataHeader.get(groupPosition),"app:"+listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition));
            	
            	
                Toast.makeText(
                        getApplicationContext(),
                        listDataHeader.get(groupPosition)
                                + " : "
                                + listDataChild.get(
                                        listDataHeader.get(groupPosition)).get(
                                        childPosition), Toast.LENGTH_SHORT)
                        .show();
                return false;
            }
        });
        
        
        
    }
    public void onClick(View v) {
		switch(v.getId()){
		case R.id.button1:
			Log.v("alert","Starting");
			GetAppCategorizationsTask task = new GetAppCategorizationsTask();
			task.execute();
		}
		
	}
    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();
        expListView.setVisibility(View.INVISIBLE);
        
        // Adding child data
        listDataHeader.add("Top 250");
        listDataHeader.add("Now Showing");
        listDataHeader.add("Coming Soon..");
         
        // Adding child data
        List<String> top250 = new ArrayList<String>();
        top250.add("The Shawshank Redemption");
        top250.add("The Godfather");
        top250.add("The Godfather: Part II");
        top250.add("Pulp Fiction");
        top250.add("The Good, the Bad and the Ugly");
        top250.add("The Dark Knight");
        top250.add("12 Angry Men");
 
        List<String> nowShowing = new ArrayList<String>();
        nowShowing.add("The Conjuring");
        nowShowing.add("Despicable Me 2");
        nowShowing.add("Turbo");
        nowShowing.add("Grown Ups 2");
        nowShowing.add("Red 2");
        nowShowing.add("The Wolverine");
 
        List<String> comingSoon = new ArrayList<String>();
        comingSoon.add("2 Guns");
        comingSoon.add("The Smurfs 2");
        comingSoon.add("The Spectacular Now");
        comingSoon.add("The Canyons");
        comingSoon.add("Europa Report");
 
        listDataChild.put(listDataHeader.get(0), top250); // Header, Child data
        listDataChild.put(listDataHeader.get(1), nowShowing);
        listDataChild.put(listDataHeader.get(2), comingSoon);
        
     
 
    }
 
    /*
     * Preparing the list data
     */
    private void prepareListData(Set<String>k,List<PackageInfo> packageList,MultiMap multiMap) {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();
        expListView.setVisibility(View.VISIBLE);
        HashMap<String,String> data = new HashMap<String,String>();
        for(String key: k){
        	listDataHeader.add(key);
        }
        // Adding child data
        /*listDataHeader.add("Top 250");
        listDataHeader.add("Now Showing");
        listDataHeader.add("Coming Soon..");
         */
        for(String key: k){
        	System.out.println(key+"=");
       Collection<PackageInfo> c = (Collection<PackageInfo>) multiMap.get(key);
       ArrayList<String> listt=new ArrayList<String>();
        for(PackageInfo p: c){
			
			listt.add(p.applicationInfo.loadLabel(getPackageManager()).toString());
        	//listt.add(p.packageName);
        	data.put(key,p.packageName);
			pckinfo.add(p);
			System.out.print(p.packageName+" ");
		}
        listDataChild.put(key,listt); // Header, Child data
		}
        
        
        // Adding child data
       for(int i=0;i<listDataHeader.size();i++){
    	   System.out.println("header="+listDataHeader.get(i)+"size"+listDataChild.size());
    	   //System.out.println("Child size"+listt.size());
    	 //  System.out.println("Data header="+data.get(listDataHeader.get(i))+"size="+data.size());
    	  // listDataChild.put(listDataHeader.get(i),listt); // Header, Child data  
       }
        
        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);
        
        // setting list adapter
        expListView.setAdapter(listAdapter);
 
 
    }


	@SuppressLint("NewApi")
	 class GetAppCategorizationsTask extends AsyncTask<Void, String, Void>{
		
		
		String currentStatus;
		public GetAppCategorizationsTask() {
			currentStatus = new String();
			packageList1 = new ArrayList<PackageInfo>();
		}
		
		ProgressDialog progressDialog;
	    //declare other objects as per your need
	    @Override
	    protected void onPreExecute()
	    {
	        progressDialog= ProgressDialog.show(Smart.this, "Progress Dialog Title Text","Process Description Text", true);
	        Log.v("alert","In preexecute");
	        //do initialization of required objects objects here                
	    };    
		
		@Override
		protected void onProgressUpdate(String... values) {
			// TODO Auto-generated method stub
			String v = (String) values[0];
			progressDialog.setMessage(v);
//			super.onProgressUpdate(v);
			
		}
		
		MultiMap multiMap ;
		@Override
		protected Void doInBackground(Void... arg0) {
			currentStatus = "Fetching installed applications..";
			this.publishProgress(currentStatus);
			
			PackageManager packageManager = getPackageManager();
	        List<PackageInfo> packageList = packageManager
	                .getInstalledPackages(PackageManager.GET_PERMISSIONS);
	 
//	        packageList1 = new ArrayList<PackageInfo>();
	         
	  //      To filter out System apps
	        for(PackageInfo pi : packageList) {
	            boolean b = isSystemPackage(pi);
	            if(!b) {
	                packageList1.add(pi);
	            }
	        }
	        
	        for(PackageInfo info: packageList1){
	        	System.out.println("main: packageName= " + info.packageName);
	        }
	        
	        System.out.println(packageList1.size());
//	        button.setText("Done");
//	        Toast.makeText(getApplicationContext(), new String("Categorizing ")+packageList1.size()+" apps...", Toast.LENGTH_LONG);
	        this.publishProgress("Getting App Categories..");
	        
	        multiMap = new MultiValueMap();
	        String urlString = resourceUrl+"?access_token="+accessToken;
	        int count = 0;
	        for(PackageInfo info: packageList1){
	        	try {
	        		
					URL url = new URL(urlString+"&p="+info.packageName);
					HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
					InputStream in = connection.getInputStream();
					BufferedReader br = new BufferedReader(new InputStreamReader(in));
				
					String responseString = br.readLine();
					JSONObject responseJson = new JSONObject(responseString);
					String respCategory = responseJson.getString("category");
					
					System.out.println(info.packageName + " " + respCategory);
									
					multiMap.put(respCategory, info);
					
					count++;
//					connection.disconnect();
					
	        		
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					count--;
					e.printStackTrace();
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					count--;
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        	
	        	
	        }
	        System.out.println("count="+count);
//	        Toast.makeText(getApplicationContext(), new String("Categorized")+count+" apps.", Toast.LENGTH_LONG);
			return null;
	        
	        
			
			
		}
		
		@Override
		protected void onPostExecute(Void result) {
			System.out.println("********");
			GetAppCategorizationsTask gap =new GetAppCategorizationsTask();
			progressDialog.dismiss();
			// TODO Auto-generated method stub
			Set<String> keys = multiMap.keySet();
			
			System.out.println("********");
			//if(gap.getStatus()==AsyncTask.Status.FINISHED)
				prepareListData(keys,packageList1,multiMap);
			System.out.println("********");
			
			for(String key: keys){
				System.out.println(key+"=");
				Collection<PackageInfo> c = (Collection<PackageInfo>) multiMap.get(key);
				for(PackageInfo p: c){
					
					System.out.print(p.packageName+" ");
				}
				System.out.println();
			}
		}
		
		private boolean isSystemPackage(PackageInfo pkgInfo) {
	        return ((pkgInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) ? true
	                : false;
	    }

		public List<PackageInfo> getPackageList1() {
			return packageList1;
		}

		public void setPackageList1(List<PackageInfo> packageList1) {
			packageList1 = packageList1;
		}
		
		
		
	}
}



