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
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabContentFactory;
import android.widget.TextView;
import android.widget.Toast;




import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import edu.dhbw.andar.database.SchemaHelper;
import edu.dhbw.andar.database.StorageFileTable;
import edu.dhbw.andar.models.JsonModel;
import edu.dhbw.andar.models.Model3DPhoto;
import edu.dhbw.andar.models.OBJ_PNG;
import edu.dhbw.andar.pub.AugmentedModelViewerActivity;
import edu.dhbw.andar.service.JSONParser;
import edu.dhbw.andar.service.JsonModelAsynTask;
import edu.dhbw.andar.util.ManageAssetsFile;
import edu.dhbw.andopenglcam.R;

public class ListView_CheckBoxActivity extends TabActivity implements
		OnTabChangeListener, OnClickListener {
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
	// é€‚é…?å™¨
	private static final String LIST1_TAB_TAG = "3D Model";
	private static final String LIST2_TAB_TAG = "Carpet";
	JSONParser jsonParser = new JSONParser();
	ArrayList<JsonModel> arrJsonModel;
	private String[] category;
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ListView mCategoryList;
	private ActionBarDrawerToggle mDrawerToggle;
	private ListView listView1;
	private ListView listView2;

	SharedPreferences mPreference;
	CheckboxAdapter listItemAdapter;
	CheckboxAdapter2 listItemAdapter2;

	public static Integer[] model_2D_Picture = { R.drawable.texture1,
			R.drawable.texture2, R.drawable.texture3, R.drawable.texture4,
			R.drawable.texture5, R.drawable.texture6, R.drawable.texture6 };

	public static String[] name_2D_Picture = { "texture1", "texture2",
			"texture3", "texture4", "texture5", "texture6", "texture7" };
	public List<Integer> picture_3D_Choosed;
	public List<Integer> picture_2D_Choosed;
	public List<String> namePicture_2D_Choosed;
	public List<String> namePicture_3D_Choosed;
	int lenghtPicture;
	ImageButton imgContinue;
	ImageButton imgDownload;
	// Button btnBack;
	private TabHost tabHost;

	HashMap<String, Object> map1;
	HashMap<String, Object> map2;
	ArrayList<OBJ_PNG> arrObjPng;
	ArrayList<Model3DPhoto> listModel3DPhoto;
	ArrayList<Model3DPhoto> listModel3DPhotoChoosed;
    ProgressDialog mPrgressDilog;
	AlertDialog alert;
	SchemaHelper sHelper;
	String[] imageUrls=new String[]{"https://lh6.googleusercontent.com/-55osAWw3x0Q/URquUtcFr5I/AAAAAAAAAbs/rWlj1RUKrYI/s1024/A%252520Photographer.jpg",
			"https://lh6.googleusercontent.com/-55osAWw3x0Q/URquUtcFr5I/AAAAAAAAAbs/rWlj1RUKrYI/s1024/A%252520Photographer.jpg",
			"https://lh4.googleusercontent.com/--dq8niRp7W4/URquVgmXvgI/AAAAAAAAAbs/-gnuLQfNnBA/s1024/A%252520Song%252520of%252520Ice%252520and%252520Fire.jpg",
			"https://lh5.googleusercontent.com/-7qZeDtRKFKc/URquWZT1gOI/AAAAAAAAAbs/hqWgteyNXsg/s1024/Another%252520Rockaway%252520Sunset.jpg",
			"https://lh3.googleusercontent.com/--L0Km39l5J8/URquXHGcdNI/AAAAAAAAAbs/3ZrSJNrSomQ/s1024/Antelope%252520Butte.jpg",
			"https://lh6.googleusercontent.com/-8HO-4vIFnlw/URquZnsFgtI/AAAAAAAAAbs/WT8jViTF7vw/s1024/Antelope%252520Hallway.jpg",
			"https://lh4.googleusercontent.com/-WIuWgVcU3Qw/URqubRVcj4I/AAAAAAAAAbs/YvbwgGjwdIQ/s1024/Antelope%252520Walls.jpg",
			"https://lh6.googleusercontent.com/-UBmLbPELvoQ/URqucCdv0kI/AAAAAAAAAbs/IdNhr2VQoQs/s1024/Apre%2525CC%252580s%252520la%252520Pluie.jpg",
			"https://lh3.googleusercontent.com/-s-AFpvgSeew/URquc6dF-JI/AAAAAAAAAbs/Mt3xNGRUd68/s1024/Backlit%252520Cloud.jpg",
			"https://lh5.googleusercontent.com/-bvmif9a9YOQ/URquea3heHI/AAAAAAAAAbs/rcr6wyeQtAo/s1024/Bee%252520and%252520Flower.jpg",
			"https://lh5.googleusercontent.com/-n7mdm7I7FGs/URqueT_BT-I/AAAAAAAAAbs/9MYmXlmpSAo/s1024/Bonzai%252520Rock%252520Sunset.jpg",
			"https://lh6.googleusercontent.com/-4CN4X4t0M1k/URqufPozWzI/AAAAAAAAAbs/8wK41lg1KPs/s1024/Caterpillar.jpg",
			"https://lh3.googleusercontent.com/-rrFnVC8xQEg/URqufdrLBaI/AAAAAAAAAbs/s69WYy_fl1E/s1024/Chess.jpg",
			"https://lh5.googleusercontent.com/-WVpRptWH8Yw/URqugh-QmDI/AAAAAAAAAbs/E-MgBgtlUWU/s1024/Chihuly.jpg",
			"https://lh5.googleusercontent.com/-0BDXkYmckbo/URquhKFW84I/AAAAAAAAAbs/ogQtHCTk2JQ/s1024/Closed%252520Door.jpg",
			"https://lh3.googleusercontent.com/-PyggXXZRykM/URquh-kVvoI/AAAAAAAAAbs/hFtDwhtrHHQ/s1024/Colorado%252520River%252520Sunset.jpg",
			"https://lh3.googleusercontent.com/-ZAs4dNZtALc/URquikvOCWI/AAAAAAAAAbs/DXz4h3dll1Y/s1024/Colors%252520of%252520Autumn.jpg",
			"https://lh4.googleusercontent.com/-GztnWEIiMz8/URqukVCU7bI/AAAAAAAAAbs/jo2Hjv6MZ6M/s1024/Countryside.jpg",
			"https://lh4.googleusercontent.com/-bEg9EZ9QoiM/URquklz3FGI/AAAAAAAAAbs/UUuv8Ac2BaE/s1024/Death%252520Valley%252520-%252520Dunes.jpg",
			"https://lh6.googleusercontent.com/-ijQJ8W68tEE/URqulGkvFEI/AAAAAAAAAbs/zPXvIwi_rFw/s1024/Delicate%252520Arch.jpg",
			"https://lh5.googleusercontent.com/-Oh8mMy2ieng/URqullDwehI/AAAAAAAAAbs/TbdeEfsaIZY/s1024/Despair.jpg",
			"https://lh5.googleusercontent.com/-gl0y4UiAOlk/URqumC_KjBI/AAAAAAAAAbs/PM1eT7dn4oo/s1024/Eagle%252520Fall%252520Sunrise.jpg",
			"https://lh3.googleusercontent.com/-hYYHd2_vXPQ/URqumtJa9eI/AAAAAAAAAbs/wAalXVkbSh0/s1024/Electric%252520Storm.jpg",
			"https://lh5.googleusercontent.com/-PyY_yiyjPTo/URqunUOhHFI/AAAAAAAAAbs/azZoULNuJXc/s1024/False%252520Kiva.jpg",
			"https://lh6.googleusercontent.com/-PYvLVdvXywk/URqunwd8hfI/AAAAAAAAAbs/qiMwgkFvf6I/s1024/Fitzgerald%252520Streaks.jpg",
			"https://lh4.googleusercontent.com/-KIR_UobIIqY/URquoCZ9SlI/AAAAAAAAAbs/Y4d4q8sXu4c/s1024/Foggy%252520Sunset.jpg",
			"https://lh6.googleusercontent.com/-9lzOk_OWZH0/URquoo4xYoI/AAAAAAAAAbs/AwgzHtNVCwU/s1024/Frantic.jpg",
			"https://lh3.googleusercontent.com/-0X3JNaKaz48/URqupH78wpI/AAAAAAAAAbs/lHXxu_zbH8s/s1024/Golden%252520Gate%252520Afternoon.jpg",
			"https://lh6.googleusercontent.com/-95sb5ag7ABc/URqupl95RDI/AAAAAAAAAbs/g73R20iVTRA/s1024/Golden%252520Gate%252520Fog.jpg",
			"https://lh3.googleusercontent.com/-JB9v6rtgHhk/URqup21F-zI/AAAAAAAAAbs/64Fb8qMZWXk/s1024/Golden%252520Grass.jpg",
			"https://lh4.googleusercontent.com/-EIBGfnuLtII/URquqVHwaRI/AAAAAAAAAbs/FA4McV2u8VE/s1024/Grand%252520Teton.jpg",
			"https://lh4.googleusercontent.com/-WoMxZvmN9nY/URquq1v2AoI/AAAAAAAAAbs/grj5uMhL6NA/s1024/Grass%252520Closeup.jpg",
			"https://lh3.googleusercontent.com/-6hZiEHXx64Q/URqurxvNdqI/AAAAAAAAAbs/kWMXM3o5OVI/s1024/Green%252520Grass.jpg",
			"https://lh5.googleusercontent.com/-6LVb9OXtQ60/URquteBFuKI/AAAAAAAAAbs/4F4kRgecwFs/s1024/Hanging%252520Leaf.jpg",
			"https://lh4.googleusercontent.com/-zAvf__52ONk/URqutT_IuxI/AAAAAAAAAbs/D_bcuc0thoU/s1024/Highway%2525201.jpg",
			"https://lh6.googleusercontent.com/-H4SrUg615rA/URquuL27fXI/AAAAAAAAAbs/4aEqJfiMsOU/s1024/Horseshoe%252520Bend%252520Sunset.jpg",
			"https://lh4.googleusercontent.com/-JhFi4fb_Pqw/URquuX-QXbI/AAAAAAAAAbs/IXpYUxuweYM/s1024/Horseshoe%252520Bend.jpg",
			"https://lh5.googleusercontent.com/-UGgssvFRJ7g/URquueyJzGI/AAAAAAAAAbs/yYIBlLT0toM/s1024/Into%252520the%252520Blue.jpg",
			"https://lh3.googleusercontent.com/-CH7KoupI7uI/URquu0FF__I/AAAAAAAAAbs/R7GDmI7v_G0/s1024/Jelly%252520Fish%2525202.jpg",
			"https://lh4.googleusercontent.com/-pwuuw6yhg8U/URquvPxR3FI/AAAAAAAAAbs/VNGk6f-tsGE/s1024/Jelly%252520Fish%2525203.jpg",
			"https://lh5.googleusercontent.com/-GoUQVw1fnFw/URquv6xbC0I/AAAAAAAAAbs/zEUVTQQ43Zc/s1024/Kauai.jpg",
			"https://lh6.googleusercontent.com/-8QdYYQEpYjw/URquwvdh88I/AAAAAAAAAbs/cktDy-ysfHo/s1024/Kyoto%252520Sunset.jpg",
			"https://lh4.googleusercontent.com/-vPeekyDjOE0/URquwzJ28qI/AAAAAAAAAbs/qxcyXULsZrg/s1024/Lake%252520Tahoe%252520Colors.jpg",
			"https://lh4.googleusercontent.com/-xBPxWpD4yxU/URquxWHk8AI/AAAAAAAAAbs/ARDPeDYPiMY/s1024/Lava%252520from%252520the%252520Sky.jpg",
			"https://lh3.googleusercontent.com/-897VXrJB6RE/URquxxxd-5I/AAAAAAAAAbs/j-Cz4T4YvIw/s1024/Leica%25252050mm%252520Summilux.jpg",
			"https://lh5.googleusercontent.com/-qSJ4D4iXzGo/URquyDWiJ1I/AAAAAAAAAbs/k2pBXeWehOA/s1024/Leica%25252050mm%252520Summilux.jpg",
			"https://lh6.googleusercontent.com/-dwlPg83vzLg/URquylTVuFI/AAAAAAAAAbs/G6SyQ8b4YsI/s1024/Leica%252520M8%252520%252528Front%252529.jpg",
			"https://lh3.googleusercontent.com/-R3_EYAyJvfk/URquzQBv8eI/AAAAAAAAAbs/b9xhpUM3pEI/s1024/Light%252520to%252520Sand.jpg",
			"https://lh3.googleusercontent.com/-fHY5h67QPi0/URqu0Cp4J1I/AAAAAAAAAbs/0lG6m94Z6vM/s1024/Little%252520Bit%252520of%252520Paradise.jpg",
			"https://lh5.googleusercontent.com/-TzF_LwrCnRM/URqu0RddPOI/AAAAAAAAAbs/gaj2dLiuX0s/s1024/Lone%252520Pine%252520Sunset.jpg",
			"https://lh3.googleusercontent.com/-4HdpJ4_DXU4/URqu046dJ9I/AAAAAAAAAbs/eBOodtk2_uk/s1024/Lonely%252520Rock.jpg",
			"https://lh6.googleusercontent.com/-erbF--z-W4s/URqu1ajSLkI/AAAAAAAAAbs/xjDCDO1INzM/s1024/Longue%252520Vue.jpg",
			"https://lh6.googleusercontent.com/-0CXJRdJaqvc/URqu1opNZNI/AAAAAAAAAbs/PFB2oPUU7Lk/s1024/Look%252520Me%252520in%252520the%252520Eye.jpg",
			"https://lh3.googleusercontent.com/-D_5lNxnDN6g/URqu2Tk7HVI/AAAAAAAAAbs/p0ddca9W__Y/s1024/Lost%252520in%252520a%252520Field.jpg",
			"https://lh6.googleusercontent.com/-flsqwMrIk2Q/URqu24PcmjI/AAAAAAAAAbs/5ocIH85XofM/s1024/Marshall%252520Beach%252520Sunset.jpg",
			"https://lh4.googleusercontent.com/-Y4lgryEVTmU/URqu28kG3gI/AAAAAAAAAbs/OjXpekqtbJ4/s1024/Mono%252520Lake%252520Blue.jpg",
			"https://lh4.googleusercontent.com/-AaHAJPmcGYA/URqu3PIldHI/AAAAAAAAAbs/lcTqk1SIcRs/s1024/Monument%252520Valley%252520Overlook.jpg",
			"https://lh4.googleusercontent.com/-vKxfdQ83dQA/URqu31Yq_BI/AAAAAAAAAbs/OUoGk_2AyfM/s1024/Moving%252520Rock.jpg",
			"https://lh5.googleusercontent.com/-CG62QiPpWXg/URqu4ia4vRI/AAAAAAAAAbs/0YOdqLAlcAc/s1024/Napali%252520Coast.jpg",
			"https://lh6.googleusercontent.com/-wdGrP5PMmJQ/URqu5PZvn7I/AAAAAAAAAbs/m0abEcdPXe4/s1024/One%252520Wheel.jpg",
			"https://lh6.googleusercontent.com/-6WS5DoCGuOA/URqu5qx1UgI/AAAAAAAAAbs/giMw2ixPvrY/s1024/Open%252520Sky.jpg",
			"https://lh6.googleusercontent.com/-u8EHKj8G8GQ/URqu55sM6yI/AAAAAAAAAbs/lIXX_GlTdmI/s1024/Orange%252520Sunset.jpg",
			"https://lh6.googleusercontent.com/-74Z5qj4bTDE/URqu6LSrJrI/AAAAAAAAAbs/XzmVkw90szQ/s1024/Orchid.jpg",
			"https://lh6.googleusercontent.com/-lEQE4h6TePE/URqu6t_lSkI/AAAAAAAAAbs/zvGYKOea_qY/s1024/Over%252520there.jpg",
			"https://lh5.googleusercontent.com/-cauH-53JH2M/URqu66v_USI/AAAAAAAAAbs/EucwwqclfKQ/s1024/Plumes.jpg",
			"https://lh3.googleusercontent.com/-eDLT2jHDoy4/URqu7axzkAI/AAAAAAAAAbs/iVZE-xJ7lZs/s1024/Rainbokeh.jpg",
			"https://lh5.googleusercontent.com/-j1NLqEFIyco/URqu8L1CGcI/AAAAAAAAAbs/aqZkgX66zlI/s1024/Rainbow.jpg",
			"https://lh5.googleusercontent.com/-DRnqmK0t4VU/URqu8XYN9yI/AAAAAAAAAbs/LgvF_592WLU/s1024/Rice%252520Fields.jpg",
			"https://lh3.googleusercontent.com/-hwh1v3EOGcQ/URqu8qOaKwI/AAAAAAAAAbs/IljRJRnbJGw/s1024/Rockaway%252520Fire%252520Sky.jpg",
			"https://lh5.googleusercontent.com/-wjV6FQk7tlk/URqu9jCQ8sI/AAAAAAAAAbs/RyYUpdo-c9o/s1024/Rockaway%252520Flow.jpg",
			"https://lh6.googleusercontent.com/-6cAXNfo7D20/URqu-BdzgPI/AAAAAAAAAbs/OmsYllzJqwo/s1024/Rockaway%252520Sunset%252520Sky.jpg",
			"https://lh3.googleusercontent.com/-sl8fpGPS-RE/URqu_BOkfgI/AAAAAAAAAbs/Dg2Fv-JxOeg/s1024/Russian%252520Ridge%252520Sunset.jpg",
			"https://lh6.googleusercontent.com/-gVtY36mMBIg/URqu_q91lkI/AAAAAAAAAbs/3CiFMBcy5MA/s1024/Rust%252520Knot.jpg",
			"https://lh6.googleusercontent.com/-GHeImuHqJBE/URqu_FKfVLI/AAAAAAAAAbs/axuEJeqam7Q/s1024/Sailing%252520Stones.jpg",
			"https://lh3.googleusercontent.com/-hBbYZjTOwGc/URqu_ycpIrI/AAAAAAAAAbs/nAdJUXnGJYE/s1024/Seahorse.jpg",
			"https://lh3.googleusercontent.com/-Iwi6-i6IexY/URqvAYZHsVI/AAAAAAAAAbs/5ETWl4qXsFE/s1024/Shinjuku%252520Street.jpg",
			"https://lh6.googleusercontent.com/-amhnySTM_MY/URqvAlb5KoI/AAAAAAAAAbs/pFCFgzlKsn0/s1024/Sierra%252520Heavens.jpg",
			"https://lh5.googleusercontent.com/-dJgjepFrYSo/URqvBVJZrAI/AAAAAAAAAbs/v-F5QWpYO6s/s1024/Sierra%252520Sunset.jpg",
			"https://lh4.googleusercontent.com/-Z4zGiC5nWdc/URqvBdEwivI/AAAAAAAAAbs/ZRZR1VJ84QA/s1024/Sin%252520Lights.jpg",
			"https://lh4.googleusercontent.com/-_0cYiWW8ccY/URqvBz3iM4I/AAAAAAAAAbs/9N_Wq8MhLTY/s1024/Starry%252520Lake.jpg",
			"https://lh3.googleusercontent.com/-A9LMoRyuQUA/URqvCYx_JoI/AAAAAAAAAbs/s7sde1Bz9cI/s1024/Starry%252520Night.jpg",
			"https://lh3.googleusercontent.com/-KtLJ3k858eY/URqvC_2h_bI/AAAAAAAAAbs/zzEBImwDA_g/s1024/Stream.jpg",
			"https://lh5.googleusercontent.com/-dFB7Lad6RcA/URqvDUftwWI/AAAAAAAAAbs/BrhoUtXTN7o/s1024/Strip%252520Sunset.jpg",
			"https://lh5.googleusercontent.com/-at6apgFiN20/URqvDyffUZI/AAAAAAAAAbs/clABCx171bE/s1024/Sunset%252520Hills.jpg",
			"https://lh4.googleusercontent.com/-7-EHhtQthII/URqvEYTk4vI/AAAAAAAAAbs/QSJZoB3YjVg/s1024/Tenaya%252520Lake%2525202.jpg",
			"https://lh6.googleusercontent.com/-8MrjV_a-Pok/URqvFC5repI/AAAAAAAAAbs/9inKTg9fbCE/s1024/Tenaya%252520Lake.jpg",
			"https://lh5.googleusercontent.com/-B1HW-z4zwao/URqvFWYRwUI/AAAAAAAAAbs/8Peli53Bs8I/s1024/The%252520Cave%252520BW.jpg",
			"https://lh3.googleusercontent.com/-PO4E-xZKAnQ/URqvGRqjYkI/AAAAAAAAAbs/42nyADFsXag/s1024/The%252520Fisherman.jpg",
			"https://lh4.googleusercontent.com/-iLyZlzfdy7s/URqvG0YScdI/AAAAAAAAAbs/1J9eDKmkXtk/s1024/The%252520Night%252520is%252520Coming.jpg",
			"https://lh6.googleusercontent.com/-G-k7YkkUco0/URqvHhah6fI/AAAAAAAAAbs/_taQQG7t0vo/s1024/The%252520Road.jpg",
			"https://lh6.googleusercontent.com/-h-ALJt7kSus/URqvIThqYfI/AAAAAAAAAbs/ejiv35olWS8/s1024/Tokyo%252520Heights.jpg",
			"https://lh5.googleusercontent.com/-Hy9k-TbS7xg/URqvIjQMOxI/AAAAAAAAAbs/RSpmmOATSkg/s1024/Tokyo%252520Highway.jpg",
			"https://lh6.googleusercontent.com/-83oOvMb4OZs/URqvJL0T7lI/AAAAAAAAAbs/c5TECZ6RONM/s1024/Tokyo%252520Smog.jpg",
			"https://lh3.googleusercontent.com/-FB-jfgREEfI/URqvJI3EXAI/AAAAAAAAAbs/XfyweiRF4v8/s1024/Tufa%252520at%252520Night.jpg",
			"https://lh4.googleusercontent.com/-vngKD5Z1U8w/URqvJUCEgPI/AAAAAAAAAbs/ulxCMVcU6EU/s1024/Valley%252520Sunset.jpg",
			"https://lh6.googleusercontent.com/-DOz5I2E2oMQ/URqvKMND1kI/AAAAAAAAAbs/Iqf0IsInleo/s1024/Windmill%252520Sunrise.jpg",
			"https://lh5.googleusercontent.com/-biyiyWcJ9MU/URqvKculiAI/AAAAAAAAAbs/jyPsCplJOpE/s1024/Windmill.jpg",
			"https://lh4.googleusercontent.com/-PDT167_xRdA/URqvK36mLcI/AAAAAAAAAbs/oi2ik9QseMI/s1024/Windmills.jpg",
			"https://lh5.googleusercontent.com/-kI_QdYx7VlU/URqvLXCB6gI/AAAAAAAAAbs/N31vlZ6u89o/s1024/Yet%252520Another%252520Rockaway%252520Sunset.jpg",
			"https://lh4.googleusercontent.com/-e9NHZ5k5MSs/URqvMIBZjtI/AAAAAAAAAbs/1fV810rDNfQ/s1024/Yosemite%252520Tree.jpg"};
	protected AbsListView listView;
	DisplayImageOptions options;
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		thisActivity = this;
		downloaderThread = null;
		progressDialog = null;
		setContentView(R.layout.category_model);
		sHelper = new SchemaHelper(this);
		listModel3DPhotoChoosed = new ArrayList<Model3DPhoto>();
		imageLoader.init(ImageLoaderConfiguration.createDefault(this));
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
		if (checkFirstLoad() == false) {
			insertToStorageTable();
		}
		imgContinue = (ImageButton) findViewById(R.id.img_next);
		imgDownload = (ImageButton) findViewById(R.id.img_download);

		imgContinue.setOnClickListener(listener);
		imgDownload.setOnClickListener(this);

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
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
		/* host Activity */

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
		tabHost.getTabWidget().getChildAt(0)
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						// if (getTabHost().getCurrentTabTag().equals(sort)) {
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
						Model3DPhoto model = (Model3DPhoto) listItemAdapter
								.getItem(j);

						listModel3DPhotoChoosed.add(model);

						picture_3D_Choosed.add(j);

					}
					if (state2.get(j) != null) {
						@SuppressWarnings("unchecked")
						Model3DPhoto model = (Model3DPhoto) listItemAdapter2
								.getItem(j);

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
				intent.putIntegerArrayListExtra("picture_3d_choose",
						(ArrayList<Integer>) picture_3D_Choosed);
				intent.putIntegerArrayListExtra("picture_2d_choose",
						(ArrayList<Integer>) picture_2D_Choosed);
				intent.putParcelableArrayListExtra("model_3d_photo",
						listModel3DPhotoChoosed);
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
		if (listModel3DPhotoChoosed != null) {
			listModel3DPhotoChoosed.clear();
		} else {
			listModel3DPhotoChoosed = new ArrayList<Model3DPhoto>();
		}

		if (namePicture_2D_Choosed != null) {
			namePicture_2D_Choosed.clear();
		} else {
			namePicture_2D_Choosed = new ArrayList<String>();
		}
		if (picture_3D_Choosed != null) {
			picture_3D_Choosed.clear();
		} else {
			picture_3D_Choosed = new ArrayList<Integer>();
		}
		if (picture_2D_Choosed != null) {
			picture_2D_Choosed.clear();
		} else {
			picture_2D_Choosed = new ArrayList<Integer>();
		}
	}

	/**
	 * Implement logic here when a tab is selected
	 */

	@Override
	public void onTabChanged(String arg0) {
		// TODO Auto-generated method stub
		if (arg0.equals(LIST2_TAB_TAG)) {
			// do something
			// tab2D = true;
			Log.d("tab 3d", "hello tab");

		} else if (arg0.equals(LIST1_TAB_TAG)) {
			// do something
			// tab3D = true;
			Log.d("tab 2d", "hello tab");
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
		for (int i = 0; i < size; i++) {
			arrObjPng = ManageAssetsFile.getPNGName(sCategory[i], this);
			sizeObjPng = arrObjPng.size();
			for (int j = 0; j < sizeObjPng; j++) {
				sHelper.addStorageModel("asasa", sCategory[i], arrObjPng.get(j));
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

		final String[] category = getResources().getStringArray(
				R.array.category);
		LayoutInflater inflater = getLayoutInflater().from(this);
		View customView = inflater.inflate(R.layout.category_listview, null,
				false);
		mCategoryList = (ListView) customView.findViewById(R.id.list_category);
		AlertDialog.Builder buider = new AlertDialog.Builder(this);
		mCategoryList.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, category));
		buider.setView(customView);
		mCategoryList.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// Do as you please
				initAdapter(category[position]);
				// change tab title
				final TextView label = (TextView) tabHost.getTabWidget()
						.findViewById(R.id.tabTitleText);
				label.setText(category[position]);
				alert.cancel();
			}
		});
		alert = buider.create();
		alert.show();

	}

	private void initAdapter(String category) {
		listModel3DPhoto = StorageFileTable.getModelByCategory(sHelper,
				category, this);
		listItemAdapter = new CheckboxAdapter(this, listModel3DPhoto);
		listView1.setAdapter(listItemAdapter);
	}

	public Handler activityHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			/*
			 * Handling MESSAGE_UPDATE_PROGRESS_BAR: 1. Get the current
			 * progress, as indicated in the arg1 field of the Message. 2.
			 * Update the progress bar.
			 */
			case MESSAGE_UPDATE_PROGRESS_BAR:
				if (progressDialog != null) {
					int currentProgress = msg.arg1;
					progressDialog.setProgress(currentProgress);
				}
				break;

			/*
			 * Handling MESSAGE_CONNECTING_STARTED: 1. Get the URL of the file
			 * being downloaded. This is stored in the obj field of the Message.
			 * 2. Create an indeterminate progress bar. 3. Set the message that
			 * should be sent if user cancels. 4. Show the progress bar.
			 */
			case MESSAGE_CONNECTING_STARTED:
				if (msg.obj != null && msg.obj instanceof String) {
					String url = (String) msg.obj;
					// truncate the url
					if (url.length() > 16) {
						String tUrl = url.substring(0, 15);
						tUrl += "...";
						url = tUrl;
					}
					String pdTitle = thisActivity
							.getString(R.string.progress_dialog_title_connecting);
					String pdMsg = thisActivity
							.getString(R.string.progress_dialog_message_prefix_connecting);
					pdMsg += " " + url;

					dismissCurrentProgressDialog();
					progressDialog = new ProgressDialog(thisActivity);
					progressDialog.setTitle(pdTitle);
					progressDialog.setMessage(pdMsg);
					progressDialog
							.setProgressStyle(ProgressDialog.STYLE_SPINNER);
					progressDialog.setIndeterminate(true);
					// set the message to be sent when this dialog is canceled
					Message newMsg = Message.obtain(this,
							MESSAGE_DOWNLOAD_CANCELED);
					progressDialog.setCancelMessage(newMsg);
					progressDialog.show();
				}
				break;

			/*
			 * Handling MESSAGE_DOWNLOAD_STARTED: 1. Create a progress bar with
			 * specified max value and current value 0; assign it to
			 * progressDialog. The arg1 field will contain the max value. 2. Set
			 * the title and text for the progress bar. The obj field of the
			 * Message will contain a String that represents the name of the
			 * file being downloaded. 3. Set the message that should be sent if
			 * dialog is canceled. 4. Make the progress bar visible.
			 */
			case MESSAGE_DOWNLOAD_STARTED:
				// obj will contain a String representing the file name
				if (msg.obj != null && msg.obj instanceof String) {
					int maxValue = msg.arg1;
					String fileName = (String) msg.obj;
					String pdTitle = thisActivity
							.getString(R.string.progress_dialog_title_downloading);
					String pdMsg = thisActivity
							.getString(R.string.progress_dialog_message_prefix_downloading);
					pdMsg += " " + fileName;

					dismissCurrentProgressDialog();
					progressDialog = new ProgressDialog(thisActivity);
					progressDialog.setTitle(pdTitle);
					progressDialog.setMessage(pdMsg);
					progressDialog
							.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
					progressDialog.setProgress(0);
					progressDialog.setMax(maxValue);
					// set the message to be sent when this dialog is canceled
					Message newMsg = Message.obtain(this,
							MESSAGE_DOWNLOAD_CANCELED);
					progressDialog.setCancelMessage(newMsg);
					progressDialog.setCancelable(true);
					progressDialog.show();
				}
				break;

			/*
			 * Handling MESSAGE_DOWNLOAD_COMPLETE: 1. Remove the progress bar
			 * from the screen. 2. Display Toast that says download is complete.
			 */
			case MESSAGE_DOWNLOAD_COMPLETE:
				dismissCurrentProgressDialog();
				displayMessage(getString(R.string.user_message_download_complete));
				break;

			/*
			 * Handling MESSAGE_DOWNLOAD_CANCELLED: 1. Interrupt the downloader
			 * thread. 2. Remove the progress bar from the screen. 3. Display
			 * Toast that says download is complete.
			 */
			case MESSAGE_DOWNLOAD_CANCELED:
				if (downloaderThread != null) {
					downloaderThread.interrupt();
				}
				dismissCurrentProgressDialog();
				displayMessage(getString(R.string.user_message_download_canceled));
				break;

			/*
			 * Handling MESSAGE_ENCOUNTERED_ERROR: 1. Check the obj field of the
			 * message for the actual error message that will be displayed to
			 * the user. 2. Remove any progress bars from the screen. 3. Display
			 * a Toast with the error message.
			 */
			case MESSAGE_ENCOUNTERED_ERROR:
				// obj will contain a string representing the error message
				if (msg.obj != null && msg.obj instanceof String) {
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
	 * If there is a progress dialog, dismiss it and set progressDialog to null.
	 */
	public void dismissCurrentProgressDialog() {
		if (progressDialog != null) {
			progressDialog.hide();
			progressDialog.dismiss();
			progressDialog = null;
		}
	}

	/**
	 * Displays a message to the user, in the form of a Toast.
	 * 
	 * @param message
	 *            Message to be displayed.
	 */
	public void displayMessage(String message) {
		if (message != null) {
			Toast.makeText(thisActivity, message, Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.img_download) {
			/*
			 * String urlInput =
			 * "http://borain89vn.byethost13.com/api/upload/1.zip";
			 * downloaderThread = new DownloaderThread(thisActivity,
			 * urlInput,"furniture"); downloaderThread.start();
			 */
			
			mDrawerLayout.openDrawer(mDrawerList);
		}

	}

	private class DrawerItemClickListener implements
			ListView.OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			JsonModelAsynTask asyn = new JsonModelAsynTask(sHelper,ListView_CheckBoxActivity.this);
			
		    asyn.execute(category[arg2]);

		}

	}

	private void selectItem(int position) {
		Toast.makeText(this, R.string.app_name, Toast.LENGTH_SHORT).show();

		// Highlight the selected item, update the title, and close the drawer
		mDrawerList.setItemChecked(position, true);

		mDrawerLayout.closeDrawer(mDrawerList);
	}

	public class ImageAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			return imageUrls.length;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final ViewHolder holder;
			View view = convertView;
			if (view == null) {
				view = getLayoutInflater().inflate(R.layout.item_grid_image, parent, false);
				holder = new ViewHolder();
				assert view != null;
				holder.imageView = (ImageView) view.findViewById(R.id.image);
				holder.progressBar = (ProgressBar) view.findViewById(R.id.progress);
				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}

			imageLoader.displayImage(imageUrls[position], holder.imageView, options, new SimpleImageLoadingListener() {
										 @Override
										 public void onLoadingStarted(String imageUri, View view) {
											 holder.progressBar.setProgress(0);
											 holder.progressBar.setVisibility(View.VISIBLE);
										 }

										 @Override
										 public void onLoadingFailed(String imageUri, View view,
												 FailReason failReason) {
											 holder.progressBar.setVisibility(View.GONE);
										 }

										 @Override
										 public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
											 holder.progressBar.setVisibility(View.GONE);
										 }
									 }, new ImageLoadingProgressListener() {
										 @Override
										 public void onProgressUpdate(String imageUri, View view, int current,
												 int total) {
											 holder.progressBar.setProgress(Math.round(100.0f * current / total));
										 }
									 }
			);

			return view;
		}

		class ViewHolder {
			ImageView imageView;
			ProgressBar progressBar;
		}
	}
	private void createImageCategoryDialog() {
		options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.ic_stub)
		.showImageForEmptyUri(R.drawable.ic_empty)
		.showImageOnFail(R.drawable.ic_error)
		.cacheInMemory(true)
		.cacheOnDisk(true)
		.considerExifParams(true)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.build();
		AlertDialog.Builder  builder = new AlertDialog.Builder(this); ;     
        Context mContext = this;     
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);     
        View layout = inflater.inflate(R.layout.category_gridview,null,false); 
           
        builder.setView(layout);  
        listView = (GridView)layout.findViewById(R.id.gridview);
	((GridView) listView).setAdapter(new ImageAdapter());
	listView.setOnItemClickListener(new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			//startImagePagerActivity(position);
		}
	});
	   
	alert = builder.create();
	alert.show();   
	}
	
}

