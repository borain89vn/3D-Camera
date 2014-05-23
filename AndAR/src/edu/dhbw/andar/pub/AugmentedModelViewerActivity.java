package edu.dhbw.andar.pub;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

import android.app.ProgressDialog;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.hardware.Camera.Size;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.Toast;
import edu.dhbw.andar.ARToolkit;
import edu.dhbw.andar.AndARActivity;
import edu.dhbw.andar.MarkerInfo;
import edu.dhbw.andar.exceptions.AndARException;
import edu.dhbw.andar.graphics.LightingRenderer;
import edu.dhbw.andar.graphics.Model3D;
import edu.dhbw.andar.layout.RandomPosition;
import edu.dhbw.andar.models.Model;
import edu.dhbw.andar.parser.ObjParser;
import edu.dhbw.andar.parser.ParseException;
import edu.dhbw.andar.util.AssetsFileUtil;
import edu.dhbw.andar.util.BaseFileUtil;
import edu.dhbw.andar.util.SDCardFileUtil;
import edu.dhbw.andopenglcam.R;

/**
 * Example of an application that makes use of the AndAR toolkit.
 * 
 * @author Tobi
 * 
 */
public class AugmentedModelViewerActivity extends AndARActivity implements
		SurfaceHolder.Callback, OnClickListener {

	/**
	 * View a file in the assets folder
	 */
	public static final int TYPE_INTERNAL = 0;
	/**
	 * View a file on the sd card.
	 */
	public static final int TYPE_EXTERNAL = 1;

	public static final boolean DEBUG = false;

	/* Menu Options: */
	int i = R.raw.aslongas;
	private final int MENU_CHANGE_BRICK = 4;

	// chon che do 2d hoac 3d;

	// array save coordinate random

	// private int modeMotion = -1;

	private float xRatio;
	private float yRatio;

	private Model[] model;
	private Model[] music;
	private Model3D[] model3d;
	private Model3D[] modelMusic;
	
	private ProgressDialog waitDialog;
	// choose menu
	int idMenuButton = -1;
	//
	// choose mode animation
	int modeAnimation = -1;
	//
	// choose mode motion
	int modeMotion = -1;
	// get id Model3d
	int getIdModel3D = 0;
	//
	// save state of animate model3d
	boolean[] stateModelDance;
	int[] modelStyleDance;
	// menu button mode chane
	// boolean aniButton=false;
	// boolean motiButton=false;
	boolean carpetOnOff = false;
	String stringCarpetOnOff = "Carpet On";
	ARToolkit artoolkit;
	// change buttonMenu mode

	// save on,off mode carpet

	// play,pause texture
	int[] playTexture = { R.drawable.pausetexture, R.drawable.pausetexture };
	String[] musicFileName = { "music1.obj", "music3.obj", "music2.obj" };
	/*String[] modelFileName = { "android.obj", "armchair.obj", "asimo.obj",
			"bench.obj", "chair3.obj", "desk.obj", "dinning_room1.obj",
			"ext_chair.obj", "jack2.obj", "johnny.obj", "liza3.obj",
			"macOS.obj", "car_red.obj", "plant.obj", "royalTree.obj",
			"smallbookshelves.obj", "superman.obj", "table_lamp.obj",
			"vase1.obj", "vase3.obj", "wardobe1.obj", "wall_e.obj" };*/
	int loadedModel;
	AndARActivity andar;

	// turn on,off model and brick
	String model_on = "Off";
	String brick_on = "On";

	boolean modeModel = true;
	boolean modeBrick = false;
	boolean markerTouch = false;
	// animate rotation x,y
	// get mode Game
	int getModeGame = 0;

	private String[] pattName = { "patt.hiro", "patt.android", "patt.kanji" };
	int numberPatt = pattName.length;
	List<Integer> getModelFromActivity;
	List<Integer> getPictureFromActivity;

	// manage Maker and modelalama

	private static final float THRESHOLD = 40.0f;

	//
	// play music
	// MediaPlayer player;
	boolean mp3Playing = false;
	boolean mp3Pause = false;
	boolean detectMusic = false;
	boolean checkFlyTo = false;
	boolean turnOnMusic = false;
	PlayMp3 mp3Player;
	//
	CustomObject[] Texture3D;
	int numberModel_3D;
	int numberMode_2D;
	// name of pattern
	String patternName;
	boolean isrunning = false;
	// public Integer[] pics;
	// public static ImageView[] imageView;

	// public static Button[] ButtonMenu;
	// bitmap texture for customObject
	public static Bitmap[] b = new Bitmap[10];
	// 2d picture
	// public static ImageView[] imageView2;
	public static float getCameraWidth;

	// random

	Random rand;
	RandomPosition randPostion;
	public static int randomHeigh;
	int randomXModel3d;
	int randomYModel3d;
	private int timeDance = 0;
	private int timeDance2 = 0;

	int musicMode = -1;
	List<Float> getXModelRand;
	List<Float> getYModelRand;
	List<Integer> saveModeDance;
	List<Integer> saveModeDance2;
	int getMarkerID;
	AssetManager am;
	List<String> mapList = null;

	public AugmentedModelViewerActivity() {
		super(false);
	}

	// display inforModel
	Thread threadDisplayInforModel;

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setNonARRenderer(new LightingRenderer());// or might be omited

		artoolkit = getArtoolkit();
		
		rand = new Random();
		// player=MediaPlayer.create(this, R.raw.testsong_20_sec);
		randomHeigh = rand.nextInt(29) + 20;
		randPostion = new RandomPosition();
		randomHeigh = 15;
		saveModeDance = new ArrayList<Integer>();
		saveModeDance2 = new ArrayList<Integer>();
		saveModeDance = randomMode();
		saveModeDance2 = randomMode();
		getXModelRand = new ArrayList<Float>();
		getYModelRand = new ArrayList<Float>();

		getModelFromActivity = new ArrayList<Integer>();
		getPictureFromActivity = new ArrayList<Integer>();
		loadedModel = 0;

		getModelFromActivity = getModelFromActivityParent;
		getPictureFromActivity = getPictureFromActivityParent;
        
		numberModel_3D = getModelFromActivity.size();
		numberMode_2D = getPictureFromActivity.size();
		model = new Model[numberModel_3D];

		model3d = new Model3D[numberModel_3D];
		modelMusic = new Model3D[3];
		music = new Model[3];
		// pics=ListView_CheckBoxActivity.model_3D_Picture;
		for (int i = 0; i < 5; i++) {
			// ButtonMenu[i].setOnClickListener(AugmentedModelViewerActivity.this);
		}
		getSurfaceView().setOnTouchListener(new TouchEventHandler());

		getSurfaceView().setOnClickListener(this);
		getSurfaceView().getHolder().addCallback(this);

	}

	/**
	 * Inform the user about exceptions that occurred in background threads.
	 */

	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		System.out.println("");
	}

	/*
	 * create the menu
	 * 
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		//saveModeDanceModel();

		// add texture to customObject
		//addTexture();

		for (int i = 0; i < numberMode_2D; i++) {

			// imageView2[i].setOnClickListener(AugmentedModelViewerActivity.this);

		}
		for (int i = 0; i < numberModel_3D; i++) {
			// imageView[i].setOnTouchListener(new TouchEventHandler());
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub

		return true;

	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		menu.clear();
		switch (idMenuButton) {
		case 0:
			MenuInflater inflater = getMenuInflater();
			inflater.inflate(R.menu.animation, menu);
			break;
		case 1:
			MenuInflater inflater1 = getMenuInflater();
			inflater1.inflate(R.menu.motion, menu);
			break;
		case 3:
			MenuInflater inflater2 = getMenuInflater();
			inflater2.inflate(R.menu.option, menu);
			if (carpetOnOff == false) {
				stringCarpetOnOff = "Carpet On";
				menu.getItem(0).setTitle(stringCarpetOnOff);
			} else {
				stringCarpetOnOff = "Carpet Off";
				menu.getItem(0).setTitle(stringCarpetOnOff);
			}
			break;
		case 2:

		}
		// music maker touch
		if (markerTouch == true) {
			MenuInflater inflater = getMenuInflater();
			inflater.inflate(R.menu.music, menu);
			/*
			 * if(mp3Player.mp3Pause==false) {
			 * 
			 * menu.getItem(1).setTitle("Pause");
			 * 
			 * 
			 * } else {
			 * 
			 * menu.getItem(1).setTitle("Play");
			 * 
			 * }
			 */
		}

		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (idMenuButton) {
		case 0:
			switch (item.getItemId()) {
			case R.id.menu_dance_move:

				stateModelDance[getIdModel3D] = true;

				modelStyleDance[getIdModel3D] = 0;
				break;
			case R.id.menu_dance_rotate:

				stateModelDance[getIdModel3D] = true;
				modelStyleDance[getIdModel3D] = 1;
				break;
			case R.id.menu_dance_fall:

				stateModelDance[getIdModel3D] = true;
				modelStyleDance[getIdModel3D] = 2;
				break;
			case R.id.menu_dance_jump:

				stateModelDance[getIdModel3D] = true;
				modelStyleDance[getIdModel3D] = 3;
				break;
			case R.id.menu_dance_stop:

				stateModelDance[getIdModel3D] = false;
				// modeAnimation=4;
				break;
			}
			break;
		case 1:
			switch (item.getItemId()) {
			case R.id.menu_move:
				modeMotion = 0;
				break;
			case R.id.menu_rotate:
				modeMotion = 1;
				break;
			case R.id.menu_lift:
				modeMotion = 2;
				break;
			case R.id.menu_zoom:
				modeMotion = 3;
				break;
			case R.id.menu_exit:
				invisibleMenuButton();
				break;
			}
			break;
		case 3:
			switch (item.getItemId()) {
			case R.id.menu_carpet_on:
				if (carpetOnOff == true) {
					carpetOnOff = false;
					hidenTexture();
				} else {
					carpetOnOff = true;

				}
				break;
			case R.id.menu_take_photo:
				new TakeAsyncScreenshot().execute();
				break;
			case R.id.menu_carpet_change:
				hiden3DPicture();
				break;

			}
			break;

		}
		if (markerTouch == true) {
			switch (item.getItemId()) {
			case R.id.menu_music_back:
				mp3Player.backMusic();
				turnOnMusic = true;

				break;
			/*
			 * case R.id.menu_music_play:
			 * 
			 * if(mp3Player.mp3Pause==false) { mp3Player.pauseMusic();
			 * turnOnMusic=false;
			 * 
			 * } else { mp3Player.resumeMusic(); turnOnMusic=true; }
			 * 
			 * 
			 * break;
			 */
			case R.id.menu_music_next:
				mp3Player.nextMusic();
				turnOnMusic = true;

				break;

			}
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		super.surfaceCreated(holder);
		// load the model
		// this is done here, to assure the surface was already created, so that
		// the preview can be started
		// after loading the model
		// if(model[0] == null) {
		waitDialog = ProgressDialog.show(this, "",
				getResources().getText(R.string.loading), true);
		waitDialog.show();
		new ModelLoader().execute();

		// }
	}

	/**
	 * Handles touch events.
	 * 
	 * @author Tobias Domhan
	 * 
	 */
	class TouchEventHandler implements OnTouchListener {

		private float lastX = 0;
		private float lastY = 0;

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (model != null) {
				switch (event.getAction()) {
				// Action started

				default:
				case MotionEvent.ACTION_DOWN:
					lastX = event.getX();
					lastY = event.getY();
					// onTouchModel(v.getId());

					if (ARToolkit.detectMarker == true) {
						Map<Integer, MarkerInfo> markerInfos = artoolkit
								.getMarkerInfos();
						for (MarkerInfo markerInfo : markerInfos.values()) {
							float markerX = markerInfo.getPos()[0];
							float markerY = markerInfo.getPos()[1];

							if (markerInfo.getId() == 1) {
								if (Math.abs(markerX * xRatio - lastX) < THRESHOLD
										&& Math.abs(markerY * yRatio - lastY) < THRESHOLD) {
									// VisibleMenuButton();
									markerTouch = true;
									idMenuButton = -1;
									(AugmentedModelViewerActivity.this)
											.openOptionsMenu();
									// invisibleMenuButton();

								}
							}
						}
					}

					break;
				// Action ongoing

				case MotionEvent.ACTION_MOVE:
					float dX = lastX - event.getX();
					float dY = lastY - event.getY();
					lastX = event.getX();
					lastY = event.getY();
					if (ARToolkit.detectMarker == true) {

						if (model[getMarkerID] != null) {
							switch (modeMotion) {

							case 3:

								model[getIdModel3D].setScale(dY / 100.0f);

								break;
							case 1:

								model[getIdModel3D].setXrot(-1 * dX);
								model[getIdModel3D].setYrot(-1 * dY);
								break;
							case 0:
								model[getIdModel3D].setXpos(-dX / 10f);
								model[getIdModel3D].setYpos(dY / 10f);
							case 2:
								model[getIdModel3D].setZpos(dY / 10f);
								break;
							}
						}
					}
					break;
				// Action ended
				case MotionEvent.ACTION_CANCEL:
				case MotionEvent.ACTION_UP:

					lastX = event.getX();
					lastY = event.getY();
					break;
				}
			}
			return true;
		}

	}

	private void danceWithMusic() {
		for (int i = 0; i < numberModel_3D; i++) {

			if (timeDance % 2000 == 0) {

				saveModeDance = randomMode();

				for (int j = 0; j < numberModel_3D; j++) {
					// model[j].xpos=0;
					// model[j].ypos=0;
					model[j].zpos = 0;
					model[j].xrot = 90;
					model[j].yrot = 0;

				}

			}
			if (saveModeDance.get(i) == 0) {

				model[i].danceJump();
			} else if (saveModeDance.get(i) == 1) {
				model[i].danceFall();

			} else if (saveModeDance.get(i) == 2) {
				model[i].danceMove();

			} else if (saveModeDance.get(i) == 3) {
				model[i].danceRotate();

			}
		}
		timeDance++;
	}

	private void addTexture() {
		int idMarker = 1;
		Texture3D = new CustomObject[10];

		for (int j = 0; j < numberMode_2D + 2; j++) {
			if (j < numberMode_2D) {
				Texture3D[j] = new CustomObject("test", pattName[0], 80.0,
						new double[] { 0, 0 }, j);

				// b[j] =
				// BitmapFactory.decodeResource(AugmentedModelViewerActivity.this.getResources(),
				// ListView_CheckBoxActivity.model_2D_Picture[ListView_CheckBoxActivity.picture_2D_Choosed.get(j)]);
				Texture3D[j].xScale = 4.0f;
				Texture3D[j].yScale = 4.0f;
				Texture3D[j].zScale = 0.02f;
				try {
					artoolkit.registerARObject(Texture3D[j]);

				} catch (AndARException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();

				}
			} else {
				Texture3D[j] = new CustomObject("test", pattName[idMarker],
						80.0, new double[] { 0, 0 }, j);

				b[j] = BitmapFactory.decodeResource(
						AugmentedModelViewerActivity.this.getResources(),
						playTexture[idMarker - 1]);

				Texture3D[j].xScale = 3.5f;
				Texture3D[j].yScale = 3.5f;
				Texture3D[j].zScale = 0.02f;
				try {
					artoolkit.registerARObject(Texture3D[j]);
					// visible pause texture
					if (j == numberMode_2D + 1) {
						Texture3D[j].isDrawed = true;
					}

				} catch (AndARException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();

				}
				idMarker++;
			}
		}
	}

	public void displayModel() {
		loadedModel++;
		if (loadedModel < numberModel_3D)
			new ModelLoader().execute();

	}

	public void loadModel(String fileName) {
		BaseFileUtil fileUtil = null;
		String category = listMode3DPhoto.get(0).getCategory();
		if(checkIfInAssets(fileName,category)==true) {
			fileUtil = new AssetsFileUtil(getResources().getAssets());
			fileUtil.setBaseFolder("models/"+category+"/");
		}
		else {
		fileUtil = new SDCardFileUtil();

		File modelFile = Environment.getExternalStorageDirectory();

		fileUtil.setBaseFolder(modelFile.getName() + "/models/");
		}

		ObjParser parser = new ObjParser(fileUtil);
		BufferedReader fileReader = fileUtil.getReaderFromName(fileName);
		try {
			if (fileReader != null) {
				model[loadedModel] = parser.parse("Model", fileReader);

			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void loadModelMusic(int idMusic) {
		BaseFileUtil fileUtil = null;

		fileUtil = new AssetsFileUtil(getResources().getAssets());
		fileUtil.setBaseFolder("models/");
		ObjParser parser = new ObjParser(fileUtil);
		BufferedReader fileReader = fileUtil
				.getReaderFromName(musicFileName[idMusic]);
		try {
			if (fileReader != null) {
				music[idMusic] = parser.parse("Model", fileReader);

			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private class ModelLoader extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
         
			loadModel(listMode3DPhoto.get(loadedModel).getObjName());
			if (loadedModel == 0) {
				for (int i = 0; i < 3; i++) {
					loadModelMusic(i);

				}
			}

			return null;
		}

		@Override
		protected void onProgressUpdate(Void... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			waitDialog.dismiss();
			displayModel();
			if (loadedModel == 1) {
				startPreview();
				Size cameraSize = camera.getParameters().getPreviewSize();
				Display display = ((WindowManager) getSystemService(WINDOW_SERVICE))
						.getDefaultDisplay();
				xRatio = (float) display.getWidth() / (float) cameraSize.width;

				yRatio = (float) display.getHeight()
						/ (float) cameraSize.height;
				randomXModel3d = randPostion.getNextInt(-150, 150);
				randomYModel3d = randPostion.getNextInt(-150, 150);
				model3d[loadedModel - 1] = new Model3D(model[loadedModel - 1],
						pattName[0], 0, 0);

				VisibleMenuButton();
				//
				try {
					artoolkit.registerARObject(model3d[loadedModel - 1]);

					model3d[loadedModel - 1].isDrawed = true;

				} catch (AndARException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				for (int i = 0; i < 3; i++) {
					modelMusic[i] = new Model3D(music[i], pattName[1],
							randPostion.getNextInt(-50, 50),
							randPostion.getNextInt(-50, 50));
					music[i].zpos = 0;
					try {
						artoolkit.registerARObject(modelMusic[i]);
						modelMusic[i].isDrawed = true;
					} catch (AndARException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

			} else {
				randomXModel3d = randPostion.getNextInt(-150, 150);
				randomYModel3d = randPostion.getNextInt(-150, 150);
				model3d[loadedModel - 1] = new Model3D(model[loadedModel - 1],
						pattName[0], randomXModel3d, randomYModel3d);

				try {
					artoolkit.registerARObject(model3d[loadedModel - 1]);
					model3d[loadedModel - 1].isDrawed = true;

				} catch (AndARException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// model3d[loadedModel-1].pushIdModel(randomXModel3d,randomYModel3d,
				// loadedModel-1);
			}
			if (loadedModel == numberModel_3D) {
				// invisibleMenuButton();
				// startThread();
			}
			// imageView[loadedModel-1].setBackgroundResource(pics[getModelFromActivity.get(loadedModel-1)]);
		}
	}

	class TakeAsyncScreenshot extends AsyncTask<Void, Void, Void> {

		private String errorMsg = null;

		@Override
		protected Void doInBackground(Void... params) {
			Bitmap bm = takeScreenshot();
			FileOutputStream fos;
			try {
				fos = new FileOutputStream("/sdcard/AndARScreenshot"
						+ new Date().getTime() + ".png");
				bm.compress(CompressFormat.PNG, 100, fos);
				fos.flush();
				fos.close();
			} catch (FileNotFoundException e) {
				errorMsg = e.getMessage();
				e.printStackTrace();
			} catch (IOException e) {
				errorMsg = e.getMessage();
				e.printStackTrace();
			}
			return null;
		}

		protected void onPostExecute(Void result) {
			if (errorMsg == null)
				Toast.makeText(AugmentedModelViewerActivity.this,
						getResources().getText(R.string.app_name),
						Toast.LENGTH_SHORT).show();
			else
				Toast.makeText(AugmentedModelViewerActivity.this,
						getResources().getText(R.string.app_name) + errorMsg,
						Toast.LENGTH_SHORT).show();
		};

	}

	void startThread() {

		isrunning = true;

		threadDisplayInforModel = new Thread(new Runnable() {

			public void run() {

				MediaPlayer mp = null;
				mp3Player = new PlayMp3(getBaseContext(), mp);

				while (isrunning) {

					if (ARToolkit.detectMarker == true) {
						if (ARToolkit.idInteractive == 1
								&& detectMusic == false) {
							mp3Player.playMusic(mp3Player.album[0]);
							detectMusic = true;
							turnOnMusic = true;
						}
						if (ARToolkit.idInteractive == 1
								&& turnOnMusic == false) {
							mp3Player.resumeMusic();
							turnOnMusic = true;
						} else if (ARToolkit.idInteractive == 2
								&& detectMusic == true) {
							mp3Player.pauseMusic();
							turnOnMusic = false;
						}//
						if (turnOnMusic == true) {
							danceWithMusic();
							// musicDance();
							for (int i = 0; i < 3; i++) {
								if (timeDance2 % 1000 == 0) {
									musicMode *= -1;
									for (int j = 0; j < 3; j++) {
										music[j].xpos = 0;
										music[j].ypos = 0;
										music[j].zpos = 0;
										music[j].xrot = 90;
										music[j].yrot = 0;
									}
								}
								if (musicMode == 1) {
									music[i].danceRotate();
									// music[i].danceRotate();
									music[i].danceJump();
								} else {
									music[i].danceRotate();
									// music[i].danceRotate();
									music[i].danceFall();
								}

							}
							timeDance2++;
							// Texture3D[numberMode_2D+1].danceRotate();
						}

						// handler.sendMessage(handler.obtainMessage());

						for (int i = 0; i < numberModel_3D; i++) {
							for (int j = 0; j < 4; j++) {
								if (stateModelDance[i] != false) {
									// modeDance(modelStyleDance[i], i);
								}
							}
						}

						try {
							Thread.sleep(1);

						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

				}
			}
		});

		threadDisplayInforModel.start();
	}

	/*
	 * public void onTouchModel(int idTouch) { switch(idTouch) { case 0:
	 * getIdModel3D=0; if(model3d[getIdModel3D].checkOnModel==true) {
	 * model_on="Off"; ButtonMenu[2].setText(model_on);
	 * 
	 * VisibleMenuButton(); } else { model_on="On";
	 * ButtonMenu[2].setText(model_on);
	 * 
	 * VisibleMenuButton(); }
	 * 
	 * break; case 1: getIdModel3D=1;
	 * if(model3d[getIdModel3D].checkOnModel==true) { model_on="Off";
	 * ButtonMenu[2].setText(model_on);
	 * 
	 * VisibleMenuButton(); } else { model_on="On";
	 * ButtonMenu[2].setText(model_on);
	 * 
	 * VisibleMenuButton(); }
	 * 
	 * 
	 * break; case 2: getIdModel3D=2;
	 * if(model3d[getIdModel3D].checkOnModel==true) { model_on="Off";
	 * ButtonMenu[2].setText(model_on);
	 * 
	 * VisibleMenuButton(); } else { model_on="On";
	 * ButtonMenu[2].setText(model_on);
	 * 
	 * VisibleMenuButton(); }
	 * 
	 * break; case 3: getIdModel3D=3;
	 * if(model3d[getIdModel3D].checkOnModel==true) { model_on="Off";
	 * ButtonMenu[2].setText(model_on);
	 * 
	 * VisibleMenuButton(); } else { model_on="On";
	 * ButtonMenu[2].setText(model_on);
	 * 
	 * VisibleMenuButton(); }
	 * 
	 * break; case 4: getIdModel3D=4;
	 * if(model3d[getIdModel3D].checkOnModel==true) { model_on="Off";
	 * ButtonMenu[2].setText(model_on);
	 * 
	 * VisibleMenuButton(); } else { model_on="On";
	 * ButtonMenu[2].setText(model_on);
	 * 
	 * VisibleMenuButton(); }
	 * 
	 * break; case 5: getIdModel3D=5;
	 * if(model3d[getIdModel3D].checkOnModel==true) { model_on="Off";
	 * ButtonMenu[2].setText(model_on);
	 * 
	 * VisibleMenuButton(); } else { model_on="On";
	 * ButtonMenu[2].setText(model_on);
	 * 
	 * VisibleMenuButton(); }
	 * 
	 * break; case 6: getIdModel3D=6;
	 * if(model3d[getIdModel3D].checkOnModel==true) { model_on="Off";
	 * ButtonMenu[2].setText(model_on);
	 * 
	 * VisibleMenuButton(); } else { model_on="On";
	 * ButtonMenu[2].setText(model_on);
	 * 
	 * VisibleMenuButton(); }
	 * 
	 * break; case 7: getIdModel3D=7;
	 * if(model3d[getIdModel3D].checkOnModel==true) { model_on="Off";
	 * ButtonMenu[2].setText(model_on);
	 * 
	 * VisibleMenuButton(); } else { model_on="On";
	 * ButtonMenu[2].setText(model_on);
	 * 
	 * VisibleMenuButton(); }
	 * 
	 * break; case 8: getIdModel3D=8;
	 * if(model3d[getIdModel3D].checkOnModel==true) { model_on="Off";
	 * ButtonMenu[2].setText(model_on);
	 * 
	 * VisibleMenuButton(); } else { model_on="On";
	 * ButtonMenu[2].setText(model_on);
	 * 
	 * VisibleMenuButton(); }
	 * 
	 * 
	 * break; case 9: getIdModel3D=9;
	 * if(model3d[getIdModel3D].checkOnModel==true) { model_on="Off";
	 * ButtonMenu[2].setText(model_on);
	 * 
	 * VisibleMenuButton(); } else { model_on="On";
	 * ButtonMenu[2].setText(model_on);
	 * 
	 * VisibleMenuButton(); }
	 * 
	 * break;
	 * 
	 * case 10: getIdModel3D=10; if(model3d[getIdModel3D].checkOnModel==true) {
	 * model_on="Off"; ButtonMenu[2].setText(model_on);
	 * 
	 * VisibleMenuButton(); } else { model_on="On";
	 * ButtonMenu[2].setText(model_on);
	 * 
	 * VisibleMenuButton(); }
	 * 
	 * break; case 11: getIdModel3D=11;
	 * if(model3d[getIdModel3D].checkOnModel==true) { model_on="Off";
	 * ButtonMenu[2].setText(model_on);
	 * 
	 * VisibleMenuButton(); } else { model_on="On";
	 * ButtonMenu[2].setText(model_on);
	 * 
	 * VisibleMenuButton(); }
	 * 
	 * break; case 12: break; case 13: break; case 14: break;
	 * 
	 * 
	 * 
	 * } }
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch (v.getId()) {
		// option mode
		case 0:
			idMenuButton = 0;
			markerTouch = false;
			(AugmentedModelViewerActivity.this).openOptionsMenu();

			invisibleMenuButton();
			break;
		case 1:
			idMenuButton = 1;
			markerTouch = false;
			(AugmentedModelViewerActivity.this).openOptionsMenu();

			invisibleMenuButton();
			break;
		case 2:
			idMenuButton = 2;
			if (model3d[getIdModel3D].checkOnModel == true) {
				model3d[getIdModel3D].isDrawed = false;
				model3d[getIdModel3D].checkOnModel = false;

			} else {
				modeModel = true;
				model3d[getIdModel3D].isDrawed = true;
				model3d[getIdModel3D].checkOnModel = true;

			}
			invisibleMenuButton();
			break;
		case 3:
			idMenuButton = 3;
			markerTouch = false;
			(AugmentedModelViewerActivity.this).openOptionsMenu();
			invisibleMenuButton();
			break;
		case 4:
			markerTouch = false;
			// (AugmentedModelViewerActivity.this).openOptionsMenu();
			invisibleMenuButton();

			break;
		case 5:
			Texture3D[0].isDrawed = true;

			stringCarpetOnOff = "Carpet Off";
			carpetOnOff = true;
			changeTexture(0);
			hiden2DPicture();
			break;
		case 6:
			Texture3D[1].isDrawed = true;

			// brick_on="Brick Off";
			stringCarpetOnOff = "Carpet Off";
			carpetOnOff = true;
			changeTexture(1);
			hiden2DPicture();
			break;
		case 7:
			Texture3D[2].isDrawed = true;

			stringCarpetOnOff = "Carpet Off";
			carpetOnOff = true;
			changeTexture(2);
			hiden2DPicture();
			break;
		case 8:
			Texture3D[3].isDrawed = true;
			stringCarpetOnOff = "Carpet Off";
			carpetOnOff = true;
			changeTexture(3);
			hiden2DPicture();
			break;
		case 9:
			Texture3D[4].isDrawed = true;
			stringCarpetOnOff = "Carpet Off";
			carpetOnOff = true;
			changeTexture(4);
			hiden2DPicture();
			break;
		case 10:
			Texture3D[5].isDrawed = true;
			stringCarpetOnOff = "Carpet Off";
			carpetOnOff = true;
			changeTexture(5);
			hiden2DPicture();
			break;
		case 11:
			Texture3D[6].isDrawed = true;
			stringCarpetOnOff = "Carpet Off";
			carpetOnOff = true;
			changeTexture(6);
			break;
		case 12:
			Texture3D[7].isDrawed = true;

			stringCarpetOnOff = "Carpet Off";
			carpetOnOff = true;
			changeTexture(7);
			hiden2DPicture();
			break;
		case 13:
			Texture3D[8].isDrawed = true;
			stringCarpetOnOff = "Carpet Off";
			carpetOnOff = true;
			hiden2DPicture();
			break;
		case 14:
			Texture3D[9].isDrawed = true;
			stringCarpetOnOff = "Carpet Off";
			carpetOnOff = true;
			changeTexture(9);
			break;
		}
	}

	private void saveModeDanceModel() {
		stateModelDance = new boolean[numberModel_3D];
		modelStyleDance = new int[numberModel_3D];
		for (int i = 0; i < numberModel_3D; i++) {
			stateModelDance[i] = false;
			modelStyleDance[i] = -1;

		}

	}

	private void modeDance(int styleDance, int idModel) {
		switch (styleDance) {
		case 0:
			model[idModel].danceMove();
			break;
		case 1:
			model[idModel].danceRotate();
			break;
		case 2:
			model[idModel].danceFall();
			break;
		case 3:
			model[idModel].danceJump();
			break;
		case 5:
			// random dance
			break;

		}
	}

	public List<Integer> randomMode() {
		List<Integer> randomArray = new ArrayList<Integer>();
		for (int i = 0; i < numberModel_3D; i++) {
			randomArray.add(rand.nextInt(4));
		}
		return randomArray;
	}

	private void hidenTexture() {
		for (int i = 0; i < numberMode_2D; i++) {
			Texture3D[i].isDrawed = false;
		}
	}

	public void hiden3DPicture() {
		for (int i = 0; i < numberMode_2D; i++) {
			// imageView2[i].setVisibility(View.VISIBLE);
		}
		for (int i = 0; i < numberModel_3D; i++) {
			// imageView[i].setVisibility(View.INVISIBLE);
		}

	}

	private void changeTexture(int idTexture) {
		for (int i = 0; i < numberMode_2D; i++) {
			if (i != idTexture) {

				Texture3D[i].isDrawed = false;
			}
		}

	}

	private void hiden2DPicture() {
		for (int i = 0; i < numberModel_3D; i++) {
			// imageView[i].setVisibility(View.VISIBLE);
		}
		for (int i = 0; i < numberMode_2D; i++) {
			// imageView2[i].setVisibility(View.INVISIBLE);
		}
	}

	private void invisibleMenuButton() {
		for (int i = 0; i < 5; i++) {
			// ButtonMenu[i].setVisibility(View.INVISIBLE);

		}
	}

	private void VisibleMenuButton() {
		for (int i = 0; i < 5; i++) {
			// ButtonMenu[i].setVisibility(View.VISIBLE);
		}
	}

	void stopThread() {
		isrunning = false;
		threadDisplayInforModel.interrupt();
		try {
			threadDisplayInforModel.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/*
	 * @Override public void onBackPressed() { // TODO Auto-generated method
	 * stub stopThread(); if(detectMusic==true) { mp3Player.stopMusic();
	 * 
	 * } // }
	 * 
	 * 
	 * for(int i=0;i<numberModel_3D;i++) {
	 * artoolkit.unregisterARObject(model3d[i]); } super.onBackPressed(); }
	 */
	public boolean checkIfInAssets(String assetName,String category) {
	    if (mapList == null) {
	        am = getAssets();
	        try {
	            mapList = Arrays.asList(am.list("models/"+category));
	        } catch (IOException e) {
	        }
	    }
	    return mapList.contains(assetName) ? true : false;
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub

		if (keyCode == KeyEvent.KEYCODE_BACK) { // stopThread();
			if (detectMusic == true) {
				// mp3Player.stopMusic();

			}
		} else if (keyCode == KeyEvent.KEYCODE_HOME) {
			stopThread();
			if (detectMusic == true) {
				mp3Player.stopMusic();
				Log.d("hello", "how are you");

			}
		}
		return super.onKeyDown(keyCode, event);
	}

}
