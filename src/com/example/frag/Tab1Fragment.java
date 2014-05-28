package com.example.frag;

import java.util.ArrayList;

import com.example.frag.DynGridView.DynGridViewListener;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.LevelListDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.ViewGroup.LayoutParams;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

public class Tab1Fragment extends Fragment implements TabHost.OnTabChangeListener, View.OnClickListener,
DynGridView.DynGridViewListener{
	final static int		idTopLayout = Menu.FIRST + 100,
			idBack 		= Menu.FIRST + 101,
			idBotLayout	= Menu.FIRST + 102,
			idToggleScroll=Menu.FIRST+ 103,
			idToggleFavs = Menu.FIRST+ 104;

DynGridViewAdapter	 	m_gridviewAdapter		= null; 
DeleteZone 				mDeleteZone				= null;
ArrayList<DynGridViewItemData> itemList			= null;
DynGridView 			gv						= null;
boolean					mToggleScroll			= false,
			mToggleFavs				= false;

	
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (container == null) {
            // We have different layouts, and in one of them this
            // fragment's containing frame doesn't exist.  The fragment
            // may still be created from its saved state, but there is
            // no eason to try to create its view hierarchy because it
            // won't be displayed.  Note this is not needed -- we could
            // just run the code below, where we would create and return
            // the view hierarchy; it would just never be used.
            return null;
        }
//		RelativeLayout global_panel = new RelativeLayout (getActivity());
		View v=inflater.inflate(R.layout.tab_frag1_layout, container, false);
		RelativeLayout global_panel = (RelativeLayout) v.findViewById(R.id.globalpanel);
			
			//global_panel.setGravity(Gravity.FILL);
//		getActivity().setContentView(global_panel);
			
			
			// +++++++++++++ TOP COMPONENT: the header
			RelativeLayout ibMenu = (RelativeLayout) v.findViewById(R.id.idTopLayout) ;//= new RelativeLayout(getActivity());
//	     	ibMenu.setId(idTopLayout);
//			ibMenu.setBackgroundDrawable(getResources().getDrawable(R.drawable.line));
//	     	int ibMenuPadding = (int) 6;
//	     	ibMenu.setPadding(ibMenuPadding,ibMenuPadding,ibMenuPadding,ibMenuPadding);
//	     	RelativeLayout.LayoutParams topParams = new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
//	     	topParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
//	     	global_panel.addView(ibMenu,topParams);
	     	// textview in ibMenu : card holder
//			TextView cTV = new TextView(getActivity());
//			TextView cTV = (TextView) v.findViewById(R.id.textViewFrag1);
////			cTV.setText("Header");
//			cTV.setTextColor(Color.rgb(255,255,255));
//			int nTextH =  18;
//			cTV.setTextSize(nTextH);
//			cTV.setTypeface(Typeface.create("arial", Typeface.BOLD));
////			RelativeLayout.LayoutParams lpcTV = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
//			lpcTV.addRule(RelativeLayout.CENTER_IN_PARENT);
//			ibMenu.addView(cTV, lpcTV);
			// cancel button in ibMenu
			/*Button m_bCancel = new Button(this);
			m_bCancel.setId(idBack);
			m_bCancel.setOnClickListener((OnClickListener) this);
			m_bCancel.setText("Exit");
			nTextH =  12;
			m_bCancel.setTextSize(nTextH);
			m_bCancel.setTypeface(Typeface.create("arial", Typeface.BOLD));
			*/RelativeLayout.LayoutParams lpb = 
				new RelativeLayout.LayoutParams(150,50);//LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			lpb.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
			lpb.addRule(RelativeLayout.CENTER_VERTICAL);
			//ibMenu.addView(m_bCancel, lpb);
			
//			mDeleteZone = new DeleteZone(getActivity());
			
			/*
			mDeleteZone = (DeleteZone) v.findViewById(R.id.mDeleteZoneView);
			//ivD.setImageResource(R.drawable.ic_launcher);
			LevelListDrawable a  = new LevelListDrawable();
			a.addLevel(0, 1, getResources().getDrawable(R.drawable.delete_icon)); // normal image
			a.addLevel(1, 2, getResources().getDrawable(R.drawable.delete_icon_red)); // delete image, drag over
			mDeleteZone.setImageDrawable(a);
			*/
//			RelativeLayout.LayoutParams lpbDel = 
//					new RelativeLayout.LayoutParams(50,50);//LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
//			lpbDel.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
//			lpbDel.addRule(RelativeLayout.CENTER_VERTICAL);
//			ibMenu.addView(mDeleteZone, lpbDel);
				

			// +++++++++++++ BOTTOM COMPONENT: the footer
			/*
			RelativeLayout ibMenuBot = new RelativeLayout(this);
			ibMenuBot.setId(idBotLayout);
			ibMenuBot.setBackgroundDrawable(getResources().getDrawable(R.drawable.line));
			ibMenuBot.setPadding(ibMenuPadding,ibMenuPadding,ibMenuPadding,ibMenuPadding);
			RelativeLayout.LayoutParams botParams = new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
			botParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
			global_panel.addView(ibMenuBot,botParams);
			// textview in ibMenu : card holder
			TextView cTVBot = new TextView(this);
			cTVBot.setText("www.pocketmagic.net");
			cTVBot.setTextColor(Color.rgb(179,116,197));
			cTVBot.setTextSize(nTextH);
			cTVBot.setTypeface(Typeface.create("arial", Typeface.NORMAL));
			RelativeLayout.LayoutParams lpcTVBot = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			lpcTVBot.addRule(RelativeLayout.CENTER_IN_PARENT);
			ibMenuBot.addView(cTVBot, lpcTVBot);
			
			Button m_bToggleScroll = new Button(this);
			m_bToggleScroll.setId(idToggleScroll);
			m_bToggleScroll.setOnClickListener((OnClickListener) this);
			m_bToggleScroll.setText("Scroll/Swipe");
			nTextH =  12;
			m_bToggleScroll.setTextSize(nTextH);
			m_bToggleScroll.setTypeface(Typeface.create("arial", Typeface.BOLD));
			lpb = new RelativeLayout.LayoutParams(150,50);//LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			lpb.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
			lpb.addRule(RelativeLayout.CENTER_VERTICAL);
			ibMenuBot.addView(m_bToggleScroll, lpb);
			
			Button m_bToggleFavs = new Button(this);
			m_bToggleFavs.setId(idToggleFavs);
			m_bToggleFavs.setOnClickListener((OnClickListener) this);
			m_bToggleFavs.setText("Toggle Favs");
			nTextH =  12;
			m_bToggleFavs.setTextSize(nTextH);
			m_bToggleFavs.setTypeface(Typeface.create("arial", Typeface.BOLD));
			//lpb = new RelativeLayout.LayoutParams(150,50);//LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			lpb.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			lpb.addRule(RelativeLayout.CENTER_VERTICAL);
			ibMenuBot.addView(m_bToggleFavs, lpb);
			*/
			// +++++++++++++ MIDDLE COMPONENT: all our GUI content
			LinearLayout midLayout = new LinearLayout (getActivity());
			midLayout.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT));
			midLayout.setOrientation(LinearLayout.VERTICAL);
			//DragLayer mid = new DragLayer(this);
			//LinearLayout mid = new LinearLayout (this);
			

		
		
		
		//GridView g=(DynGridView) getActivity().findViewById(R.id.gvMainIcon);
		gv=new DynGridView(getActivity());
//			gv = (DynGridView) v.findViewById(R.id.mDynGridView);
		
		RelativeLayout.LayoutParams midParams = new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
//		midParams.addRule(RelativeLayout.ABOVE,ibMenuBot.getId());
		midParams.addRule(RelativeLayout.BELOW, ibMenu.getId());
		
		global_panel.addView(gv,midParams);
		
		
		int imgs[] = new int[]{ R.drawable.facebook,R.drawable.camera,R.drawable.phonealt,
				R.drawable.email,R.drawable.gtalk,R.drawable.appstore};
		String texts[] = new String[]{"facebok", "camera", "phone","email", 
				"sms", "appstore"}; 
		itemList = new ArrayList<DynGridViewItemData>();
		for (int i=0;i<6;i++) {
			DynGridViewItemData item = new DynGridViewItemData( 
					texts[i%6], // item string name
					65, 65, 10, // sizes: item w, item h, item padding
					0, // item background image
					R.drawable.favon, 
					R.drawable.favoff, 
					true, // favorite state, if favs are enabled
					mToggleFavs, // favs disabled
					imgs[i%6], // item image res id
					i  // item id
					);

			itemList.add(item);
		}
		
		//3. create adapter
		m_gridviewAdapter = new DynGridViewAdapter(getActivity(), itemList);
		
		//4. add adapter to gridview
		gv.setAdapter(m_gridviewAdapter);   
		//gv.setColumnWidth(300);
		gv.setNumColumns(2);
//		gv.setSelection(2);
		gv.setDynGridViewListener((DynGridViewListener) this);

        
     
        
        // drag functionality
        gv.setDeleteView(mDeleteZone);
        DragController dragController = new DragController(getActivity());
        
        gv.setDragController(dragController);
        
        // gv.getDragController().setDragEnabled(false); // disable DRAG functionality
        gv.setSwipeEnabled(mToggleScroll); // enable swipe but disable scrolling
       
        // getActivity().setContentView(global_panel);
        
		
        return v;
	
	
	}
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		int id = arg0.getId();
		// If cancel is pressed, close our app
		if (id == idBack) getActivity().finish();
		if (id == idToggleScroll) {
			mToggleScroll = !mToggleScroll;
			gv.setSwipeEnabled(mToggleScroll);
			String text = "Swipe enabled:"+mToggleScroll;
			Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();		
		}
		if (id == idToggleFavs) {
			mToggleFavs = !mToggleFavs;
			for (DynGridViewItemData item:itemList)
				item.setFavoriteStateShow(mToggleFavs);
			m_gridviewAdapter.notifyDataSetChanged();
			gv.invalidateViews();
			
			String text = "Favs enabled:"+mToggleFavs;
			Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
		}
	}

	@SuppressLint("NewApi")
	public void onItemClick(View v, int position, int id) {
		String text = "Click on:"+id+ " " +
				((DynGridViewItemData)m_gridviewAdapter.getItem(position)).getLabel();
		Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();	
		if(id==0){
			Intent launcher=getActivity().getPackageManager().getLaunchIntentForPackage("com.facebook.katana");
			startActivity(launcher);
		}	
		if(id==1){
				Intent launcher=getActivity().getPackageManager().getLaunchIntentForPackage("com.sec.android.app.camera");
				startActivity(launcher);
		}			
		if(id==2){
				Intent launcher=getActivity().getPackageManager().getLaunchIntentForPackage("com.android.contacts");
				startActivity(launcher);
		}
		if(id==3){
				Intent launcher=getActivity().getPackageManager().getLaunchIntentForPackage("com.google.android.gm");
				startActivity(launcher);
		}									
		if(id==4){
			Intent launcher=getActivity().getPackageManager().getLaunchIntentForPackage("com.android.mms");
			startActivity(launcher);
		}
		if(id==5){
			Intent launcher=getActivity().getPackageManager().getLaunchIntentForPackage("com.android.vending");
			startActivity(launcher);
		}	
	}

	public void onItemFavClick(View v, int position, int id) {
		itemList.get(position).setFavoriteState(!itemList.get(position).getFavoriteState());
		m_gridviewAdapter.notifyDataSetChanged();
		gv.invalidateViews();
		
		String text = "Item:"+position+ " fav state:" +
				((DynGridViewItemData)m_gridviewAdapter.getItem(position)).getFavoriteState();
		Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();		
	}

	public void onDragStart() {
	}

	public void onDragStop() {
	}

	public void onItemsChanged(int positionOne, int positionTwo) {
		String text = "You've changed item " + positionOne + " with item "+ positionTwo;
		Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();		
	}

	public void onItemDeleted(int position, int id) {
		String text = "You've deleted item " + id + " " +
				((DynGridViewItemData)m_gridviewAdapter.getItem(position)).getLabel();
		Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();		
	}

	

	

	public void onSwipeUp() {
		String text = "Swipe UP detected";
		Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();		
	}

	public void onSwipeDown() {
		String text = "Swipe DOWN detected";
		Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
	}
	@Override
	public void onTabChanged(String arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onSwipeLeft() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onSwipeRight() {
		// TODO Auto-generated method stub
		
	}


	
}