package edu.dhbw.andar.layout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabContentFactory;
import android.widget.TextView;
import android.widget.Toast;
import edu.dhbw.andar.database.SchemaHelper;
import edu.dhbw.andar.database.StorageFileTable;
import edu.dhbw.andar.models.JsonModel;
import edu.dhbw.andar.models.Model3DPhoto;
import edu.dhbw.andar.models.OBJ_PNG;
import edu.dhbw.andar.pub.AugmentedModelViewerActivity;
import edu.dhbw.andar.service.DownloaderThread;
import edu.dhbw.andar.service.JSONParser;
import edu.dhbw.andar.util.ManageAssetsFile;
import edu.dhbw.andopenglcam.R;

public class ListView_CheckBoxActivity extends TabActivity implements
		OnTabChangeListener,OnClickListener {
	// Used to communicate state changes in the DownloaderThread
		public static final int MESSAGE_DOWNLOAD_STARTED = 1000;
		public static final int MESSAGE_DOWNLOAD_COMPLETE = 1001;
		public static final int MESSAGE_UPDATE_PROGRESS_BAR = 1002;
		public static final int MESSAGE_DOWNLOAD_CANCELED = 1003;
		public static final int MESSAGE_CONNECTING_STARTED = 1004;
		public static final int MESSAGE_ENCOUNTERED_ERROR = 1005;
		private ListView_CheckBoxActivity thisActivity;
		private Thread downloaderThread;
		private ProgressDialog progressDialog;
	// é€‚é…�å™¨
	private static final String LIST1_TAB_TAG = "3D Model";
	private static final String LIST2_TAB_TAG = "Carpet";
	JSONParser jsonParser = new JSONParser();
	ArrayList<JsonModel> arrJsonModel;
	private String[] category;
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ListView mCategoryList;

	private ListView listView1;
	private ListView listView2;
	private boolean tab3D = false;
	private boolean tab2D = false;
	SharedPreferences mPreference;
	CheckboxAdapter listItemAdapter;
	CheckboxAdapter2 listItemAdapter2;
	
	public static Integer[] model_2D_Picture = { R.drawable.texture1,
			R.drawable.texture2, R.drawable.texture3, R.drawable.texture4,
			R.drawable.texture5, R.drawable.texture6, R.drawable.texture6 };
	
	public static String[] name_2D_Picture = { "texture1", "texture2",
			"texture3", "texture4", "texture5", "texture6", "texture7" };
	public  List<Integer> picture_3D_Choosed;
	public  List<Integer> picture_2D_Choosed;
	public  List<String> namePicture_2D_Choosed;
	public  List<String> namePicture_3D_Choosed;
	int lenghtPicture;
	ImageButton imgContinue;
	ImageButton imgDownload;
	// Button btnBack;
	private TabHost tabHost;

	HashMap<String, Object> map1;
	HashMap<String, Object> map2;
	ArrayList<OBJ_PNG> arrObjPng;
	ArrayList<Model3DPhoto> listModel3DPhoto;
	AlertDialog alert;
	SchemaHelper sHelper;
	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		  thisActivity = this;
	        downloaderThread = null;
	        progressDialog = null;
		setContentView(R.layout.category_model);
		sHelper = new SchemaHelper(this);
		
		
		tabHost = getTabHost();
		// tabHost.setOnTabChangedListener(this);
		// init
		category = getResources().getStringArray(R.array.category);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.list_left_drawer);
		mDrawerList.setAdapter(new ArrayAdapter<String>(this,
				R.layout.drawer_listview_item, category));
		// setup list view 1
		
		listView1 = (ListView) findViewById(R.id.list1);
		listView2 = (ListView) findViewById(R.id.list2);
        if(checkFirstLoad()==false){
        	insertToStorageTable();
        }
		imgContinue = (ImageButton) findViewById(R.id.img_next);
		imgDownload = (ImageButton)findViewById(R.id.img_download);
		
		imgContinue.setOnClickListener(listener);
		imgDownload.setOnClickListener(this) ;
		

		
		ArrayList<HashMap<String, Object>> listData2 = new ArrayList<HashMap<String, Object>>();

		
		for (int i = 0; i < 7; i++) {
			map2 = new HashMap<String, Object>();
			map2.put("model_image_2d", model_2D_Picture[i]);
			map2.put("model_name_2d", name_2D_Picture[i]);
			map2.put("selected_model_2d", false);
			listData2.add(map2);
		}
		
		initAdapter("furniture");
		listItemAdapter2 = new CheckboxAdapter2(this, listData2);
		listView2.setAdapter(listItemAdapter2);

		View tabView = createTabView(this, "architecture");
		

		// add views to tab host
		tabHost.addTab(tabHost.newTabSpec(LIST1_TAB_TAG).setIndicator(tabView)
				.setContent(new TabContentFactory() {
					public View createTabContent(String arg0) {
						return listView1;
					}
				}));

		tabHost.getTabWidget().setStripEnabled(false);
		tabHost.getTabWidget().setDividerDrawable(null);
		View tabView2 = createTabView(this, "Tab 2");
		tabHost.addTab(tabHost.newTabSpec(LIST2_TAB_TAG).setIndicator(tabView2)
				.setContent(new TabContentFactory() {
					public View createTabContent(String arg0) {
						return listView2;
					}
				}));
		tabHost.getTabWidget().getChildAt(0).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//if (getTabHost().getCurrentTabTag().equals(sort)) {
	                getTabHost().setCurrentTab(0);
	                createDialogCategory();
	           // }
			}
		});
			
			
			
			
	}

	OnClickListener listener = new OnClickListener() {
		@Override
		public void onClick(View v) {

			switch (v.getId()) {
			case R.id.img_next: {

				// TODO Auto-generated method stub
				HashMap<Integer, Boolean> state = listItemAdapter.state;

				HashMap<Integer, Boolean> state2 = listItemAdapter2.state;
				Model3DPhoto a;
				a= (Model3DPhoto) listItemAdapter.getItem(0);

				for (int j = 0; j < listItemAdapter.getCount(); j++) {

					if (state.get(j) != null) {
						@SuppressWarnings("unchecked")
						Model3DPhoto model = (Model3DPhoto) listItemAdapter.getItem(j);
								
						namePicture_3D_Choosed.add(model.getName());
								
						picture_3D_Choosed.add(j);

					}
					if (state2.get(j) != null) {
						@SuppressWarnings("unchecked")
						Model3DPhoto model = (Model3DPhoto) listItemAdapter2.getItem(j);

						namePicture_2D_Choosed.add(model.getName());
								
						picture_2D_Choosed.add(j);
					}

				}

				/*
				 * Bundle b=new Bundle();
				 * b.putIntegerArrayList("picture",(ArrayList<Integer>)
				 * picture_3D_Choosed);
				 */
				final Intent intent = new Intent(
						ListView_CheckBoxActivity.this,
						AugmentedModelViewerActivity.class);
				intent.putIntegerArrayListExtra("picture_3d_choose", (ArrayList<Integer>) picture_3D_Choosed);
				intent.putIntegerArrayListExtra("picture_2d_choose", (ArrayList<Integer>) picture_2D_Choosed);
				startActivity(intent);

			}
				break;

			}
		}

	};

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		namePicture_3D_Choosed = new ArrayList<String>();
		namePicture_2D_Choosed = new ArrayList<String>();
		picture_3D_Choosed = new ArrayList<Integer>();
		picture_2D_Choosed = new ArrayList<Integer>();
	}

	/**
	 * Implement logic here when a tab is selected
	 */

	@Override
	public void onTabChanged(String arg0) {
		// TODO Auto-generated method stub
		if (arg0.equals(LIST2_TAB_TAG)) {
			// do something
			tab2D = true;
			Log.d("tab 3d","hello tab");

		} else if (arg0.equals(LIST1_TAB_TAG)) {
			// do something
			tab3D = true;
			Log.d("tab 2d","hello tab");
		}
	}

	private static View createTabView(Context context, String tabText) {
		View view = LayoutInflater.from(context).inflate(R.layout.custom_tab,
				null, false);
		TextView tv = (TextView) view.findViewById(R.id.tabTitleText);
		
		tv.setText(tabText);
		return view;
	}
	
	private void insertToStorageTable() {
		
		String[] sCategory = getResources().getStringArray(R.array.category);
		int size = sCategory.length;
		int sizeObjPng;
		for(int i = 0 ;i<size; i++) {
			arrObjPng = ManageAssetsFile.getPNGName(sCategory[i], this);
			sizeObjPng = arrObjPng.size();
		    for(int j=0 ;j<sizeObjPng;j++){
			sHelper.addStorageModel("asasa",sCategory[i],arrObjPng.get(j));
		    }
			
		}
	}

	private boolean checkFirstLoad() {
		mPreference = getSharedPreferences("first_load", MODE_PRIVATE);
		boolean hasVisited = mPreference.getBoolean("hasVisited", false);
		if (!hasVisited) {
			Editor e = mPreference.edit();
			e.putBoolean("hasVisited", true);
			e.commit();
		}
		return hasVisited;
	}
	private void createDialogCategory() {
		
		final String[] category =  getResources().getStringArray(R.array.category);
		LayoutInflater inflater = getLayoutInflater().from(this);
		View customView = inflater.inflate(R.layout.category_listview, null,false);
		mCategoryList = (ListView)customView.findViewById(R.id.list_category);
		AlertDialog.Builder buider = new AlertDialog.Builder(this);
		mCategoryList.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,category));
		buider.setView(customView);
		mCategoryList.setOnItemClickListener(new OnItemClickListener() {
		    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		        // Do as you please
		    	initAdapter(category[position]);
		    	//change tab title
		    	final TextView label = (TextView) tabHost.getTabWidget().findViewById(R.id.tabTitleText);
		    	label .setText(category[position]);
		    	alert.cancel();
		    }
		});
		alert = buider.create();
		alert.show();
		
	}
	private void initAdapter(String category) {
         listModel3DPhoto = StorageFileTable.getModelByCategory(sHelper, category,this);
		 listItemAdapter = new CheckboxAdapter(this, listModel3DPhoto);
		 listView1.setAdapter(listItemAdapter);
	}
	public Handler activityHandler = new Handler()
	{
		public void handleMessage(Message msg)
		{
			switch(msg.what)
			{
				/*
				 * Handling MESSAGE_UPDATE_PROGRESS_BAR:
				 * 1. Get the current progress, as indicated in the arg1 field
				 *    of the Message.
				 * 2. Update the progress bar.
				 */
				case MESSAGE_UPDATE_PROGRESS_BAR:
					if(progressDialog != null)
					{
						int currentProgress = msg.arg1;
						progressDialog.setProgress(currentProgress);
					}
					break;
				
				/*
				 * Handling MESSAGE_CONNECTING_STARTED:
				 * 1. Get the URL of the file being downloaded. This is stored
				 *    in the obj field of the Message.
				 * 2. Create an indeterminate progress bar.
				 * 3. Set the message that should be sent if user cancels.
				 * 4. Show the progress bar.
				 */
				case MESSAGE_CONNECTING_STARTED:
					if(msg.obj != null && msg.obj instanceof String)
					{
						String url = (String) msg.obj;
						// truncate the url
						if(url.length() > 16)
						{
							String tUrl = url.substring(0, 15);
							tUrl += "...";
							url = tUrl;
						}
						String pdTitle = thisActivity.getString(R.string.progress_dialog_title_connecting);
						String pdMsg = thisActivity.getString(R.string.progress_dialog_message_prefix_connecting);
						pdMsg += " " + url;
						
						dismissCurrentProgressDialog();
						progressDialog = new ProgressDialog(thisActivity);
						progressDialog.setTitle(pdTitle);
						progressDialog.setMessage(pdMsg);
						progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
						progressDialog.setIndeterminate(true);
						// set the message to be sent when this dialog is canceled
						Message newMsg = Message.obtain(this, MESSAGE_DOWNLOAD_CANCELED);
						progressDialog.setCancelMessage(newMsg);
						progressDialog.show();
					}
					break;
					
				/*
				 * Handling MESSAGE_DOWNLOAD_STARTED:
				 * 1. Create a progress bar with specified max value and current
				 *    value 0; assign it to progressDialog. The arg1 field will
				 *    contain the max value.
				 * 2. Set the title and text for the progress bar. The obj
				 *    field of the Message will contain a String that
				 *    represents the name of the file being downloaded.
				 * 3. Set the message that should be sent if dialog is canceled.
				 * 4. Make the progress bar visible.
				 */
				case MESSAGE_DOWNLOAD_STARTED:
					// obj will contain a String representing the file name
					if(msg.obj != null && msg.obj instanceof String)
					{
						int maxValue = msg.arg1;
						String fileName = (String) msg.obj;
						String pdTitle = thisActivity.getString(R.string.progress_dialog_title_downloading);
						String pdMsg = thisActivity.getString(R.string.progress_dialog_message_prefix_downloading);
						pdMsg += " " + fileName;
						
						dismissCurrentProgressDialog();
						progressDialog = new ProgressDialog(thisActivity);
						progressDialog.setTitle(pdTitle);
						progressDialog.setMessage(pdMsg);
						progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
						progressDialog.setProgress(0);
						progressDialog.setMax(maxValue);
						// set the message to be sent when this dialog is canceled
						Message newMsg = Message.obtain(this, MESSAGE_DOWNLOAD_CANCELED);
						progressDialog.setCancelMessage(newMsg);
						progressDialog.setCancelable(true);
						progressDialog.show();
					}
					break;
				
				/*
				 * Handling MESSAGE_DOWNLOAD_COMPLETE:
				 * 1. Remove the progress bar from the screen.
				 * 2. Display Toast that says download is complete.
				 */
				case MESSAGE_DOWNLOAD_COMPLETE:
					dismissCurrentProgressDialog();
					displayMessage(getString(R.string.user_message_download_complete));
					break;
					
				/*
				 * Handling MESSAGE_DOWNLOAD_CANCELLED:
				 * 1. Interrupt the downloader thread.
				 * 2. Remove the progress bar from the screen.
				 * 3. Display Toast that says download is complete.
				 */
				case MESSAGE_DOWNLOAD_CANCELED:
					if(downloaderThread != null)
					{
						downloaderThread.interrupt();
					}
					dismissCurrentProgressDialog();
					displayMessage(getString(R.string.user_message_download_canceled));
					break;
				
				/*
				 * Handling MESSAGE_ENCOUNTERED_ERROR:
				 * 1. Check the obj field of the message for the actual error
				 *    message that will be displayed to the user.
				 * 2. Remove any progress bars from the screen.
				 * 3. Display a Toast with the error message.
				 */
				case MESSAGE_ENCOUNTERED_ERROR:
					// obj will contain a string representing the error message
					if(msg.obj != null && msg.obj instanceof String)
					{
						String errorMessage = (String) msg.obj;
						dismissCurrentProgressDialog();
						displayMessage(errorMessage);
					}
					break;
					
				default:
					// nothing to do here
					break;
			}
		}
	};
	
	/**
	 * If there is a progress dialog, dismiss it and set progressDialog to
	 * null.
	 */
	public void dismissCurrentProgressDialog()
	{
		if(progressDialog != null)
		{
			progressDialog.hide();
			progressDialog.dismiss();
			progressDialog = null;
		}
	}
	
	/**
	 * Displays a message to the user, in the form of a Toast.
	 * @param message Message to be displayed.
	 */
	public void displayMessage(String message)
	{
		if(message != null)
		{
			Toast.makeText(thisActivity, message, Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.getId()==R.id.img_download) {
			String urlInput = "http://borain89vn.byethost13.com/api/upload/2.zip";
			downloaderThread = new DownloaderThread(thisActivity, urlInput,"architecture");
			downloaderThread.start();
		}
		
	}
	
}