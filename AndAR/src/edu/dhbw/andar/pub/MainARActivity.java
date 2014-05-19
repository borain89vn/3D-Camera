package edu.dhbw.andar.pub;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;

import android.app.ProgressDialog;
import android.content.res.Resources;
import android.hardware.Camera.Size;
import android.media.SoundPool;
import android.net.ParseException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import edu.dhbw.andar.ARToolkit;
import edu.dhbw.andar.AndARActivity;
import edu.dhbw.andar.MarkerInfo;
import edu.dhbw.andar.exceptions.AndARException;
import edu.dhbw.andar.graphics.LightingRenderer;
import edu.dhbw.andar.graphics.Model3D;
import edu.dhbw.andar.models.Model;
import edu.dhbw.andar.parser.ObjParser;
import edu.dhbw.andar.util.AssetsFileUtil;
import edu.dhbw.andar.util.BaseFileUtil;
import edu.dhbw.andopenglcam.R;

public class MainARActivity extends AndARActivity {
	private static final float THRESHOLD = 30.0f;
	private ARToolkit arToolkit;
	private ProgressDialog progressDialog;
	private GestureDetector gd;
	private SoundPool sp;
	private float xRatio;
	private float yRatio;

	private boolean touchModel=false;
	
	//get id model from model3d
	private Hashtable<Model3D, Integer> hModel3d;
	public int makerID;
	private Model[] model=new Model[6];
	private Model3D[] model3d=new Model3D[6];
	private final int MENU_ZOOM = 0;
	private final int MENU_ROTATE = 1;
	private final int MENU_MOVE = 2;
	public static Model getModel;
	
	private final int MENU_CHANGE_MODEL=3;
	private final int MENU_SCREENSHOT = 4;
	private Resources res;
	private int mode = MENU_MOVE;
	//@SuppressWarnings("unchecked")
	/*@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
     //   gd = new GestureDetector(this, onGestureListener);
        
        super.setNonARRenderer(new LightingRenderer());
        res=getResources();
        arToolkit = super.getArtoolkit();
        model3d[0] = new Model3D(getModel, "patt.kanji");
        try {
			arToolkit.registerARObject(model3d[0]);
		} catch (AndARException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       
		progressDialog = ProgressDialog.show(this, "Loading Model", getResources().getText(R.string.app_name), true);
		progressDialog.show();
		getSurfaceView().setOnTouchListener(new TouchEventHandler());
		
		
		new ModelLoader().execute();
	
    }
	
	  

	


		
	    
	@Override
	protected void onResume() {
		 hModel3d=new Hashtable<Model3D, Integer>();
		super.onResume();
		
	}
	
	@Override
	protected void onPause() {
		sp.release();
		super.onPause();
	}

	
	
    
  
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		menu.add(0, MENU_MOVE, 0, res.getText(R.string.menu_move))
		.setIcon(R.drawable.icon);
    menu.add(0, MENU_ROTATE, 0, res.getText(R.string.menu_rotate))
    	.setIcon(R.drawable.icon);
    menu.add(0, MENU_ZOOM, 0, res.getText(R.string.menu_zoom))
    	.setIcon(R.drawable.icon);   
    menu.add(0,MENU_CHANGE_MODEL,0,res.getText(R.string.menu_Change_model))
    .setIcon(R.drawable.icon);
    menu.add(0, MENU_SCREENSHOT, 0, res.getText(R.string.menu_takePhoto))
		.setIcon(R.drawable.icon); 
   ;
    return true;
		//return super.onCreateOptionsMenu(menu);
	}




	 public boolean onOptionsItemSelected(MenuItem item) {
	        switch (item.getItemId()) {
		        case MENU_ZOOM:
		            mode = MENU_ZOOM;
		            return true;
		        case MENU_ROTATE:
		        	mode = MENU_ROTATE;
		            return true;
		        case MENU_MOVE:
		        	mode = MENU_MOVE;
		            return true;
		        case MENU_CHANGE_MODEL:
		        	mode=MENU_CHANGE_MODEL;
		        	return true;
		        case MENU_SCREENSHOT:
		        	//new TakeAsyncScreenshot().execute();
		        	return true;
	        }
	        return false;
	    }



	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		Log.e("AR_Speeker", ex.getMessage());
		finish();
	}
class TouchEventHandler implements OnTouchListener {
    	
    	private float lastX=0;
    	private float lastY=0;

		String patternName;
		 
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if(model!=null) {
				switch(event.getAction()) {
					//Action started
					default:
					case MotionEvent.ACTION_DOWN:
					lastX = event.getX();
						lastY = event.getY();
						Map<Integer, MarkerInfo> markerInfos = arToolkit.getMarkerInfos();
						for (MarkerInfo markerInfo : markerInfos.values()) {
							float markerX = markerInfo.getPos()[0];
							float markerY = markerInfo.getPos()[1];
							if (Math.abs(markerX*xRatio - lastX) < THRESHOLD && Math.abs(markerY*yRatio - lastY) < THRESHOLD) {
								Log.d("AR_Speeker", String.format("marker %s is touched", markerInfo.getFileName()));
								Log.d("toa do x cua maker :",String.valueOf(markerX*xRatio));
								Log.d(" toa do x t:",String.valueOf(lastX));
								
								makerID=markerInfo.getId();
								if(makerID==0)
								{
									patternName="patt.kanji";
									arToolkit.unregisterARObject(model3d[makerID]);
									model3d[makerID]=new Model3D(model[2],patternName );
									try {
										arToolkit.registerARObject(model3d[makerID]);
									} catch (AndARException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
								else
								{
									patternName="patt.hiro";
								arToolkit.unregisterARObject(model3d[makerID]);
								model3d[makerID]=new Model3D(model[2],patternName );
								try {
									arToolkit.registerARObject(model3d[makerID]);
								} catch (AndARException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								}
								
								touchModel=true;
								
								
							}
						}
						break;
					//Action ongoing
					
					case MotionEvent.ACTION_MOVE:
						float dX = lastX - event.getX();
						float dY = lastY - event.getY();
						lastX = event.getX();
						lastY = event.getY();
						if(model[makerID] != null) {
							switch(mode) {
								case MENU_ZOOM:
									model[makerID].setScale(dY/100.0f);
						            break;
						        case MENU_ROTATE:
						        	model[makerID].setXrot(-1*dX);//dY-> Rotation um die X-Achse
									model[2].setYrot(-1*dY);//dX-> Rotation um die Y-Achse
						            break;
						        case MENU_MOVE:
						        	model[makerID].setXpos(dX/10f);
									model[makerID].setYpos(dY/10f);
						        	break;
							}		
						}
						break;
					//Action ended
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
		
	private class ModelLoader extends AsyncTask<Void, Void, Void>{
		
		@Override
    	protected Void doInBackground(Void... params) {
    		
		
			String[] modelFileName={"Porl.obj","plant.obj","bench.obj","chair.obj","sofa.obj","superman.obj"};
			int numMakers=modelFileName.length;
			BaseFileUtil fileUtil= null;
			File modelFile=null;
			
				fileUtil = new AssetsFileUtil(getResources().getAssets());
				fileUtil.setBaseFolder("models/");
			
				ObjParser parser =new ObjParser(fileUtil);
				try {
					if(fileUtil != null) {
						BufferedReader fileReader = fileUtil.getReaderFromName(modelFileName[0]);
						BufferedReader fileReader1 = fileUtil.getReaderFromName(modelFileName[1]);
						BufferedReader fileReader2 = fileUtil.getReaderFromName(modelFileName[2]);
						BufferedReader fileReader3 = fileUtil.getReaderFromName(modelFileName[3]);
						BufferedReader fileReader4 = fileUtil.getReaderFromName(modelFileName[4]);
						BufferedReader fileReader5 = fileUtil.getReaderFromName(modelFileName[5]);
						//if(fileReader != null) {

                            
							try {
								model[0] = parser.parse("Model", fileReader);
								model[1] = parser.parse("Model", fileReader1);
							model[2] = parser.parse("Model", fileReader2);
								model[3] = parser.parse("Model", fileReader3);
							} catch (edu.dhbw.andar.parser.ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
							
							//model3d = new Model3D(model[2],"patt.hiro");
							//model3d=new Model3D(model[1],"patt.kanji");
						//}
					}
					
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ParseException e) {
					e.printStackTrace();
				}
			//}
			//}
    		return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			progressDialog.dismiss();
		
    		model3d[0] = new Model3D(model[0], "patt.kanji");
    		
			
				
				
    		 try {
				arToolkit.registerARObject(model3d[0]);
				model3d[1]=new Model3D(model[0],"patt.hiro");
				arToolkit.registerARObject(model3d[1]);
				
			} catch (AndARException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		 //model3d[1]=new Model3D(model[0],"patt.hiro");
    		 //arToolkit.registerARObject(model3d[0]);
    		
			Size cameraSize = camera.getParameters().getPreviewSize();
			Display display = ((WindowManager)getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
			xRatio = (float)display.getWidth()/(float)cameraSize.width;
			yRatio = (float)display.getHeight()/(float)cameraSize.height;
			
		}*/
	@Override
	public void uncaughtException(Thread arg0, Throwable arg1) {
		// TODO Auto-generated method stub
		
	}
	//}
}