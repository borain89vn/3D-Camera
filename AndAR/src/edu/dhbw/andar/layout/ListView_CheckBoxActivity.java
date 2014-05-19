package edu.dhbw.andar.layout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
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
import edu.dhbw.andar.database.SchemaHelper;
import edu.dhbw.andar.models.JsonModel;
import edu.dhbw.andar.models.Model3DPhoto;
import edu.dhbw.andar.models.OBJ_PNG;
import edu.dhbw.andar.pub.AugmentedModelViewerActivity;
import edu.dhbw.andar.service.JSONParser;
import edu.dhbw.andar.util.ManageAssetsFile;
import edu.dhbw.andopenglcam.R;

public class ListView_CheckBoxActivity extends TabActivity implements
		OnTabChangeListener {
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
	public static List<Integer> picture_3D_Choosed;
	public static List<Integer> picture_2D_Choosed;
	public static List<String> namePicture_2D_Choosed;
	public static List<String> namePicture_3D_Choosed;
	int lenghtPicture;
	ImageButton btnContinue;
	// Button btnBack;
	private TabHost tabHost;

	HashMap<String, Object> map1;
	HashMap<String, Object> map2;
	ArrayList<OBJ_PNG> arrObjPng;
	ArrayList<Model3DPhoto> listModel3DPhoto;
	AlertDialog alert;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.category_model);
		
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

		btnContinue = (ImageButton) findViewById(R.id.img_next);
		// btnBack=(Button)findViewById(R.id.btnBack);
		btnContinue.setOnClickListener(listener);
		// btnBack.setOnClickListener(listener);

	
		
		ArrayList<HashMap<String, Object>> listData2 = new ArrayList<HashMap<String, Object>>();

		
		for (int i = 0; i < 7; i++) {
			map2 = new HashMap<String, Object>();
			map2.put("model_image_2d", model_2D_Picture[i]);
			map2.put("model_name_2d", name_2D_Picture[i]);
			map2.put("selected_model_2d", false);
			listData2.add(map2);
		}

		initAdapter("architecture");
		listItemAdapter2 = new CheckboxAdapter2(this, listData2);
		listView2.setAdapter(listItemAdapter2);

		View tabView = createTabView(this, "Tab 1");

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

				for (int j = 0; j < listItemAdapter.getCount(); j++) {

					if (state.get(j) != null) {
						@SuppressWarnings("unchecked")
						HashMap<String, Object> map = (HashMap<String, Object>) listItemAdapter
								.getItem(j);

						namePicture_3D_Choosed.add(map.get("model_name_3d")
								.toString());
						picture_3D_Choosed.add(j);

					}
					if (state2.get(j) != null) {
						@SuppressWarnings("unchecked")
						HashMap<String, Object> map = (HashMap<String, Object>) listItemAdapter2
								.getItem(j);

						namePicture_2D_Choosed.add(map.get("model_name_2d")
								.toString());
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
				// intent.putExtras(b);
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
	
	private void insertToTable() {
		SchemaHelper sHelper = new SchemaHelper(this);
		int size = listModel3DPhoto.size();
		for(int i=0;i<size;i++) {
		sHelper.addStorageModel(listModel3DPhoto.get(i));
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
		SchemaHelper sHelper = new SchemaHelper(this);
		 arrObjPng= ManageAssetsFile.getPNGName(category,this);
		 listModel3DPhoto = new ArrayList<Model3DPhoto>();
		 lenghtPicture = arrObjPng.size();
		 for (int i = 0; i < lenghtPicture; i++) {
				Model3DPhoto model = new Model3DPhoto(this);
	            model.setCategory(category);
				model.setObj_png(arrObjPng.get(i));
				model.setName("adas");
				sHelper.addStorageModel(model);
				
             listModel3DPhoto.add(model);

			}
		 listItemAdapter = new CheckboxAdapter(this, listModel3DPhoto);
		 listView1.setAdapter(listItemAdapter);
	}
	
}