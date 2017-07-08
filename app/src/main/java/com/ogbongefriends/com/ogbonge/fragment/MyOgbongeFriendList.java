package com.ogbongefriends.com.ogbonge.fragment;

import java.io.File;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.Notification;
import android.content.Context;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.ogbongefriends.com.ogbonge.helper.LocationHelper;
import com.ogbongefriends.com.ogbonge.helper.LocationHelper.MyLocationListener;
import com.ogbongefriends.com.DrawerActivity;
import com.ogbongefriends.com.R;
import com.ogbongefriends.com.DB.DB;
import com.ogbongefriends.com.api.getAllUserProfileAPI;
import com.ogbongefriends.com.common.CelUtils;
import com.ogbongefriends.com.common.Constants;
import com.ogbongefriends.com.common.CustomLoader;
import com.ogbongefriends.com.common.GridView_Adapter_LongClicked;
import com.ogbongefriends.com.common.Preferences;
import com.ogbongefriends.com.common.Utils;


@SuppressLint("NewApi") public class MyOgbongeFriendList extends Fragment implements Runnable,MyLocationListener {

		// private ListView listView;
		@SuppressWarnings("unused")
		private EditText posttetx;
		@SuppressWarnings("unused")
		private Button post;
		private Button attchbtn;
		private JsonArray searchedata;
		private Uri imageUri;

		private Uri selectedImage;

		private long str;
		private LocationHelper mLocationHelper;
		Cursor data;
		Cursor eventdatacorsor;
		Cursor followerdatacorsor;
		Cursor followingdatacorsor;
		Cursor secfollowingdatacorsor;
		Preferences pref;

		private GridView user_of_city;

		private CheckBox chfollow;
		private CheckBox chsecretfollow;
		private final int NUM_OF_ROWS_PER_PAGE = 10;
		// EventAdapter showfollowerlist;
		private ArrayList<HashMap<String, String>> data_map;
		
		FragmentManager fragmentManager;
		@SuppressLint("NewApi")
		Fragment fragment;
		Cursor placedatacursor;
		private String followStatus;
//		private String followintType;
		View rootView;
		private String imgname;
		private TextView notificationText;
		private TextView chatText;
		private TextView checkInText;
		private Button notificationBtn;
		private CheckBox ch1,ch2,ch3,ch4,ch5,ch6;
		private Button save;
		private Button checkInBtn;
		
		private DB db;
		private CustomLoader p;
		private ImageLoader imageLoader;
		Notification nt;
		private LinearLayout ll_user;
		private TextView nodata,msg_to_fav;
		private getAllUserProfileAPI get_all_user_pfro_api,get_all_favd_user_api;
		// getEventApi geteventapi;
		int count = 0;
	   private Context _ctx; 
	  public HashMap<String, String> urls;
	  DisplayImageOptions options;
	  private PullToRefreshGridView mPullRefreshGridView;
	  private ImageView search_btn,filter;
	  private EditText search_key;
	  private RelativeLayout popup_layout;
	  private LinearLayout searchLayout;
	  private int current_user=0;
	  static	LinearLayout.LayoutParams lp;
	  boolean isPulled=false;
	  private searchApi searchApi;
	  private int category_type=1,search_page_index=1,isSearched=0,search_page=0;
	  private TextView title;
	  private ListView poplist;
	  private Button filter_btn;
	  private GridView_Adapter_LongClicked gridViewAdapter;
	  
	  ScrollView view;
	  
	  

	  String[] listContent = { "Name","Location"};
		public MyOgbongeFriendList(){

		}
		public MyOgbongeFriendList(Context ctx) {
			_ctx=ctx;
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			if(rootView==null){
			 p = new CustomLoader(_ctx, android.R.style.Theme_Translucent_NoTitleBar);
				db=new DB(_ctx);
			p.show();
			rootView = inflater.inflate(R.layout.who_fav_you, container, false);
			ll_user=(LinearLayout)rootView.findViewById(R.id.parent_user_images);
			msg_to_fav=(TextView)rootView.findViewById(R.id.msg_to_see);
			title=(TextView)rootView.findViewById(R.id.title);
			title.setText("Ogbonge Friend's List");
			searchLayout=(LinearLayout)rootView.findViewById(R.id.search_layout);
			view=(ScrollView)rootView.findViewById(R.id.scrollView1);
			searchLayout.setVisibility(View.GONE);
			search_key=(EditText)rootView.findViewById(R.id.key_to_search);
			filter_btn=(Button)rootView.findViewById(R.id.filter_btn);
			search_btn=(ImageView)rootView.findViewById(R.id.search_btn);
			filter=(ImageView)rootView.findViewById(R.id.filter);
			popup_layout=(RelativeLayout)rootView.findViewById(R.id.popuplaout);
			filter_btn=(Button)rootView.findViewById(R.id.filter_btn);
			poplist   = (ListView) rootView.findViewById(R.id.custom_search_option_list);
			
			mPullRefreshGridView = (PullToRefreshGridView) rootView.findViewById(R.id.user_of_city);
			user_of_city=mPullRefreshGridView.getRefreshableView();
			
			nodata=(TextView)rootView.findViewById(R.id.nodata_text);
			data_map=new ArrayList<HashMap<String,String>>();
			pref=new Preferences(_ctx);
			manageLocation();
			
			
			 filter_btn.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						
						

						
						
						
						
						if(searchLayout.getVisibility()==View.VISIBLE){
							searchLayout.setVisibility(View.GONE);
						}
						else{
							searchLayout.setVisibility(View.VISIBLE);
						}
					}
				});
			
			
			filter.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(popup_layout.getVisibility()==View.VISIBLE){
						popup_layout.setVisibility(View.GONE);
					}
					else{
						popup_layout.setVisibility(View.VISIBLE);
					}
				}
	});



			search_btn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(search_key.getText().length()>0){
						isPulled=false;
					search_page_index=1;
					search_page=1;
					data_map.clear();
					p.show();
						get_resulted_data(search_page_index);
				}
				}
});


	ArrayAdapter<String> adapter = new ArrayAdapter<String>(_ctx,R.layout.popuplist_item, listContent);
					poplist.setAdapter(adapter);
			
			
					poplist.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> parent, View view,
								int position, long id) {
							// TODO Auto-generated method stub
							
							category_type=position+1;
							popup_layout.setVisibility(View.GONE);
						}
	});
					
					
			msg_to_fav.setText("people liked you. See if you fav'd them too.");
			
			File cacheDir = StorageUtils.getCacheDirectory(_ctx);
					
					
					 options = CelUtils.getImageOption();
					
					ImageLoader.getInstance().init(CelUtils.getImageConfig(_ctx));
					
				 imageLoader = ImageLoader.getInstance();
				 
				 mPullRefreshGridView.setOnRefreshListener(new OnRefreshListener2<GridView>() {

						

						@Override
						public void onPullDownToRefresh(
								PullToRefreshBase<GridView> refreshView) {
							// TODO Auto-generated method stub
							mPullRefreshGridView.onRefreshComplete();
							
						}

						@Override
						public void onPullUpToRefresh(
								PullToRefreshBase<GridView> refreshView) {
							// TODO Auto-generated method stub
							if(search_page_index>0){
								isPulled=true;
								if(search_page==1){
								get_resulted_data(search_page_index);
								}
								else{
									getAllUsers(search_page_index);
								}
							}
							mPullRefreshGridView.onRefreshComplete();
							mPullRefreshGridView.getRefreshableView().setSelection(data_map.size());
						}

					});
			
				 gridViewAdapter= new GridView_Adapter_LongClicked(_ctx, data_map)
					{

						@Override
						protected void onItemClick(View v, String string) {
							// TODO Auto-generated method stub
							//pref.set(Constants.selected_user_id,string );
							pref.set(Constants.OtherUser,string );
							
							pref.commit();
							((DrawerActivity) getActivity()).displayView(37);
						}

						@Override
						protected void onItemLongClick(View v, String string) {
							// TODO Auto-generated method stub
							pref.set(Constants.OtherUser,string );
							pref.commit();
						}
						
					};
				 
				 user_of_city.setAdapter(gridViewAdapter);
						
			
			getAllUsersFroBanner();
			
			getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
			fragmentManager = getFragmentManager();
			}
		return 	rootView;
			

		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			
		}	
		
		
		private void manageLocation() {
			mLocationHelper = new LocationHelper(_ctx);
			//mLocationHelper.startLocationUpdates(this);
		}
		
		public void get_resulted_data(int page_index){
			
			
			searchApi=new searchApi(_ctx, db, p){

					@Override
					protected void onError(Exception e) {
						// TODO Auto-generated method stub
						super.onError(e);
					}

					@Override
					protected void onDone() {
						// TODO Auto-generated method stub
						super.onDone();
					}

					@Override
					protected void onResponseReceived(InputStream is) {
						// TODO Auto-generated method stub
						super.onResponseReceived(is);
						searchedata=searchApi.userdata;
						search_page_index=searchApi.page_index;
						Log.d("JSON Array",String.valueOf(searchApi.userdata));
						Log.d("JSON Array",String.valueOf(searchedata));
						if(searchedata.size()>0){
							showuser(searchedata);
							}
					}

					@Override
					protected void updateUI() {
						// TODO Auto-generated method stub
						super.updateUI();
						//showuser(searchedata);
					}
					
				};
				
				
				if(search_key.getText().length()>0){
					p.show();
				 HashMap<String, String>map=new HashMap<String, String>();
			     map.put("uuid", pref.get(Constants.KeyUUID));
			     map.put("auth_token", pref.get(Constants.kAuthT));
			     map.put("category",String.valueOf(category_type));  
			     map.put("keyword",search_key.getText().toString()); 
			     map.put("time_stamp",""); 
			     map.put("type","1"); 
			     map.put("page_index",String.valueOf(page_index)); 
			     
			     	      
			     searchApi.setPostData(map);
			     callApi(searchApi);
				}
			}
		
		
	public void getAllUsers(int page_index){
			
		get_all_user_pfro_api=new getAllUserProfileAPI(getActivity(), db, p){

			@Override
			protected void onError(Exception e) {
				// TODO Auto-generated method stub
				super.onError(e);
			}

			@Override
			protected void onResponseReceived(InputStream is) {
				// TODO Auto-generated method stub
				super.onResponseReceived(is);
				searchedata=get_all_user_pfro_api.getUserdata();
				
			}

			@Override
			protected void onDone() {
				// TODO Auto-generated method stub
				super.onDone();
			}

			@Override
			protected void updateUI() {
				// TODO Auto-generated method stub
				super.updateUI();
				showuser(searchedata);
							//showuser(searchedata);
			}
			
		};
		
		
		 HashMap<String, String>map=new HashMap<String, String>();
	     map.put("uuid", pref.get(Constants.KeyUUID));
	     map.put("auth_token", pref.get(Constants.kAuthT));
	     map.put("time_stamp", "");  
	     map.put("type", "2"); 
	     map.put("page_index",String.valueOf(page_index)); 
	     Log.d("arv", ""+mLocationHelper.getMyLocation());
	     
	     if (mLocationHelper.getMyLocation() == null) {
	    	 map.put("latitude","2345"); 
		     map.put("longitude", "2345"); 
			}
	     else{
	    	 map.put("latitude",String.valueOf(mLocationHelper.getMyLocation()
						.getLatitude())); 
		     map.put("longitude", String.valueOf(mLocationHelper.getMyLocation()
						.getLongitude())); 
	     }
	    
	     
	     get_all_user_pfro_api.setPostData(map);
	     callApi(get_all_user_pfro_api);
	     
	     	
		}
		


	public void getAllUsersFroBanner(){
		
		get_all_user_pfro_api=new getAllUserProfileAPI(getActivity(), db, p){

			@Override
			protected void onError(Exception e) {
				// TODO Auto-generated method stub
				super.onError(e);
			}

			@Override
			protected void onDone() {
				// TODO Auto-generated method stub
				super.onDone();
				getActivity().runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						if(get_all_user_pfro_api.getUserdata().size()>0){
							showuserForBanner(get_all_user_pfro_api.getUserdata());
							getAllfavdUser();
							
						}
					}
				});
			}

			@Override
			protected void updateUI() {
				// TODO Auto-generated method stub
				super.updateUI();
				//showuserForBanner();
				//getAllUsers(search_page_index);
				
			}
			
		};
		
		
		 HashMap<String, String>map=new HashMap<String, String>();
	     map.put("uuid", pref.get(Constants.KeyUUID));
	     map.put("auth_token", pref.get(Constants.kAuthT));
	     map.put("time_stamp", "");  
	     map.put("type", "6"); 
	     map.put("page_index", "1");
	     Log.d("arv", ""+mLocationHelper.getMyLocation());
	     
	     if (mLocationHelper.getMyLocation() == null) {
	    	 map.put("latitude","1234"); 
		     map.put("longitude", "12345"); 
			}
	     else{
	    	 map.put("latitude",String.valueOf(mLocationHelper.getMyLocation()
						.getLatitude())); 
		     map.put("longitude", String.valueOf(mLocationHelper.getMyLocation()
						.getLongitude())); 
	     }
	    
	     
	     get_all_user_pfro_api.setPostData(map);
	     callApi(get_all_user_pfro_api);
}
	
	public void getAllfavdUser(){
		
		

		
		get_all_favd_user_api=new getAllUserProfileAPI(getActivity(), db, p){

			@Override
			protected void onError(Exception e) {
				// TODO Auto-generated method stub
				super.onError(e);
				}

			@Override
			protected void onDone() {
				// TODO Auto-generated method stub
				super.onDone();
				getActivity().runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						search_page_index=getAllUserProfileAPI.index;
						if(get_all_favd_user_api.getUserdata().size()>0){
							showuser(get_all_favd_user_api.getUserdata());	
							
						}
					}
				});
			}

			@Override
			protected void updateUI() {
				// TODO Auto-generated method stub
				super.updateUI();
				//if(get_all_user_pfro_api.getUserdata()!=null){
				
			}
			
		};
		
		
		 HashMap<String, String>map=new HashMap<String, String>();
	     map.put("uuid", pref.get(Constants.KeyUUID));
	     map.put("auth_token", pref.get(Constants.kAuthT));
	     map.put("time_stamp", "");  
	     map.put("type", "2"); 
	     map.put("distance", "2");  
	     map.put("page_index", "1"); 
	     Log.d("arv", ""+mLocationHelper.getMyLocation());
	     if (mLocationHelper.getMyLocation() == null) {
	    	 map.put("latitude","1234"); 
		     map.put("longitude", "1234"); 
			}
	     else{
	    	 map.put("latitude",String.valueOf(mLocationHelper.getMyLocation()
						.getLatitude())); 
		     map.put("longitude", String.valueOf(mLocationHelper.getMyLocation()
						.getLongitude())); 
	     }
	    
	     
	     get_all_favd_user_api.setPostData(map);
	     callApi(get_all_favd_user_api);

		
	}
	
	private void showuserForBanner(final JsonArray jsnData){
		
		for(int i=0;i<jsnData.size();i++){
		JsonObject obj=jsnData.get(i).getAsJsonObject();
			
		
		if(obj.get(DB.Table.user_master.profile_pic.toString()).getAsString().length()>3){
			RelativeLayout.LayoutParams parent_param = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.FILL_PARENT);
			RelativeLayout.LayoutParams topbar_param = new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, 30);
			
			RelativeLayout parent = new RelativeLayout(getActivity());
			
			
			RelativeLayout top_bar = new RelativeLayout(getActivity());
			top_bar.setBackgroundResource(R.drawable.top_element);
			
			
			parent.setLayoutParams(parent_param);
				 
			 if(i==0){
				 parent.setBackgroundColor(Color.DKGRAY);
			 }
			 
		parent.setTag(obj.get(DB.Table.user_master.uuid.toString()).getAsString());
		parent.setTag(R.id.account_setting_text, i);
		parent.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				///loadUser(v,(Integer) v.getTag(R.id.account_setting_text));
				current_user=(Integer) v.getTag(R.id.account_setting_text);
				//data.moveToPosition(current_user);
				pref.set(Constants.OtherUser,(jsnData.get(current_user).getAsJsonObject().get(DB.Table.user_master.uuid.toString()).getAsString()));
				pref.commit();
				((DrawerActivity) getActivity()).displayView(37);	
				
				
			}
		});
			String url=getActivity().getString(R.string.urlString)+"userdata/image_gallery/"+obj.get(DB.Table.user_master.id.toString()).getAsString()+"/photos_of_you/"+obj.get(DB.Table.user_master.profile_pic.toString()).getAsString();
			ImageView imagView = new ImageView(getActivity());
			imagView.setScaleType(ScaleType.FIT_XY);
			ll_user.setOrientation(LinearLayout.HORIZONTAL);
			//LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(140, 140);
			

			int screenSize = getResources().getConfiguration().screenLayout &
			        Configuration.SCREENLAYOUT_SIZE_MASK;

			String toastMsg;
			switch(screenSize) {
			    case Configuration.SCREENLAYOUT_SIZE_LARGE:
			        toastMsg = "Large screen";
			        
			        lp = new LinearLayout.LayoutParams(190, LayoutParams.FILL_PARENT);
			        
			        break;
			    case Configuration.SCREENLAYOUT_SIZE_NORMAL:
			        toastMsg = "Normal screen";
			        
			        lp = new LinearLayout.LayoutParams(150, LayoutParams.FILL_PARENT);
			        lp.setMargins(5, 0, 0, 0);
			        break;
			    case Configuration.SCREENLAYOUT_SIZE_SMALL:
			        toastMsg = "Small screen";
			        break;
			    default:
			        toastMsg = "Screen size is neither large, normal or small";
			}
			
			

			    imagView.setLayoutParams(lp);
			    ProgressBar p=new ProgressBar(_ctx);
			    p.setLayoutParams(lp);
			    imagView.setTag(url);
			    imagView.setImageResource(R.drawable.profile);
			 //   imageLoader.displayImage(url, imagView);
			    
			    imageLoader.displayImage(url, imagView, new ImageLoadingListener() {
					
					@Override
					public void onLoadingStarted(String arg0, View arg1) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
						// TODO Auto-generated method stub
						//if(i==data.getCount()-1){
						//loading.setVisibility(View.GONE);
					//	}
					}
					
					@Override
					public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
						// TODO Auto-generated method stub
					//	loading.setVisibility(View.GONE);
					}
					
					@Override
					public void onLoadingCancelled(String arg0, View arg1) {
						// TODO Auto-generated method stub
					//	loading.setVisibility(View.GONE);
					}
				});

			RelativeLayout.LayoutParams online_param = new RelativeLayout.LayoutParams(15, 15);
			online_param.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
			    ImageView online_image = new ImageView(getActivity());
			    online_image.setLayoutParams(online_param);
			    online_image.setImageResource(R.drawable.online_dot);
			    TextView tv = new TextView(getActivity());
			    RelativeLayout.LayoutParams text_param = new RelativeLayout.LayoutParams(android.widget.LinearLayout.LayoutParams.FILL_PARENT, android.widget.LinearLayout.LayoutParams.WRAP_CONTENT);
			    tv.setText(obj.get(DB.Table.user_master.first_name.toString()).getAsString());
			    tv.setTextColor(Color.WHITE);
			    tv.setLayoutParams(text_param);
			    tv.setGravity(Gravity.CENTER_HORIZONTAL);
			    
			    parent.addView(imagView);
			    parent.addView(top_bar, topbar_param);
			    parent.addView(tv);
			   // parent.addView(online_image);
			    
			   
			    ll_user.addView(parent);
			    
		}
		
		}
		
	}

	
	
	
	
	private void showuser(JsonArray usersData){
		// TODO Auto-generated method stub
		
				if(usersData.size()>0){
				
		for(int i=0; i<usersData.size();i++){
			
			JsonObject form = usersData.get(i).getAsJsonObject();
			urls=new HashMap<String, String>();
			if(!(form.get(DB.Table.user_master.profile_pic.toString()).getAsString().equals(""))){
				String url=getActivity().getString(R.string.urlString)+"userdata/image_gallery/"+form.get(DB.Table.user_master.id.toString()).getAsString()+"/photos_of_you/"+form.get(DB.Table.user_master.profile_pic.toString()).getAsString();
					
				urls.put("Image_url", url);
				urls.put("user_name", form.get(DB.Table.user_master.first_name.toString()).getAsString());
				urls.put(DB.Table.user_master.uuid.toString(), form.get(DB.Table.user_master.uuid.toString()).getAsString());
		}
			else{
				urls.put("Image_url","");
				urls.put("user_name", form.get(DB.Table.user_master.first_name.toString()).getAsString());
				urls.put(DB.Table.user_master.uuid.toString(), form.get(DB.Table.user_master.uuid.toString()).getAsString());
			}
			
			urls.put("photos", form.get("photos").getAsString());
			urls.put(DB.Table.user_master.rating.toString(), form.get(DB.Table.user_master.rating.toString()).getAsString());
			urls.put(DB.Table.user_master.rate_count.toString(), form.get(DB.Table.user_master.rate_count.toString()).getAsString());
			urls.put(DB.Table.user_master.city.toString(), form.get(DB.Table.user_master.city.toString()).getAsString());
			urls.put(DB.Table.user_master.verification_level.toString(), form.get(DB.Table.user_master.verification_level.toString()).getAsString());
			urls.put("rate_count", form.get("rate_count").getAsString());
			
			
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			//df.setTimeZone(TimeZone.getTimeZone("EDT"));
		    df.setTimeZone(TimeZone.getTimeZone("GMT"));
		    Date timestamp = null;
		    
			if(form.get(DB.Table.user_master.last_seen.toString()).getAsString().length()>0){
			    try {
					timestamp = df.parse(form.get(DB.Table.user_master.last_seen.toString()).getAsString());
					timestamp.setHours(timestamp.getHours()+4);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			    
			    timestamp.getTime();
			    
			    String parsed = df.format(timestamp);
			    long tim = 0;
			    try {
				 tim=df.parse(parsed).getTime();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			    
			    System.out.println("New = " + parsed);
			    long millis = 
			    Calendar.getInstance(TimeZone.getTimeZone("GMT")).getTimeInMillis();
			    Log.d("ARVI", ""+(millis-tim)/(1000*60)+"min");
			    
			    urls.put(DB.Table.user_master.last_seen.toString(), String.valueOf((millis-tim)));
			    
		}
		else{
			 urls.put(DB.Table.user_master.last_seen.toString(), "");
		}
			
			
			
			data_map.add(i, urls);
		}
		
		msg_to_fav.setText(data_map.size()+" people liked you. See if you fav'd them too.");
		

		int screenSize = getResources().getConfiguration().screenLayout &
		        Configuration.SCREENLAYOUT_SIZE_MASK;

		String toastMsg;
		switch(screenSize) {
		    case Configuration.SCREENLAYOUT_SIZE_LARGE:
		        toastMsg = "Large screen";
		        
		        user_of_city.setNumColumns(3);
		        
		        break;
		    case Configuration.SCREENLAYOUT_SIZE_NORMAL:
		        toastMsg = "Normal screen";
		        
		        user_of_city.setNumColumns(2);
		        break;
		    case Configuration.SCREENLAYOUT_SIZE_SMALL:
		        toastMsg = "Small screen";
		        break;
		    default:
		        toastMsg = "Screen size is neither large, normal or small";
		}
		
		   
			
				}
				else{
					if(isPulled==false){
					nodata.setVisibility(View.VISIBLE);
					}
				}
				gridViewAdapter.notifyDataSetChanged();
			p.cancel();
	}

	private void callApi(Runnable r) {

		if (!Utils.isNetworkConnectedMainThred(getActivity())) {
			Log.v("Internet Not Conneted", "");
			getActivity().runOnUiThread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					Thread.currentThread().setPriority(1);
					p.cancel();
					Utils.same_id("Error", getString(R.string.no_internet),
							getActivity());
				}
			});
			return;
		} else {
			Log.v("Internet Conneted", "");
		}

		Thread t = new Thread(r);
		t.setName(r.getClass().getName());
		t.start();

	}
		

		private void saveSetting(){
			
			if(ch1.isChecked()){
				pref.setBoolean(Constants.profSetting1, true);
				pref.commit();
			}
			else{
				pref.setBoolean(Constants.profSetting1, false);
				pref.commit();
			}
			
			if(ch2.isChecked()){
				pref.setBoolean(Constants.profSetting2, true);
				pref.commit();
			}
			else{
				pref.setBoolean(Constants.profSetting2, false);
				pref.commit();
			}
			
			if(ch3.isChecked()){
				pref.setBoolean(Constants.profSetting3, true);
				pref.commit();
			}
			else {
				pref.setBoolean(Constants.profSetting3, false);
				pref.commit();
			}
			if(ch4.isChecked()){
				pref.setBoolean(Constants.profSetting4, true);
				pref.commit();
			}
			
			else {
				pref.setBoolean(Constants.profSetting4, false);
				pref.commit();
			}
			
			if(ch5.isChecked()){
				pref.setBoolean(Constants.profSetting5, true);
				pref.commit();
			}
			else{
				pref.setBoolean(Constants.profSetting5, false);
				pref.commit();
			}
			
			if(ch6.isChecked()){
				pref.setBoolean(Constants.profSetting6, true);
				pref.commit();
			}
			else{
				pref.setBoolean(Constants.profSetting6, false);
				pref.commit();
			}
		}

		@Override
		public void onLocationUpdate(Location location) {
			// TODO Auto-generated method stub
			
		}
		
	}



