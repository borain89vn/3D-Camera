/**
	Copyright (C) 2009,2010  Tobias Domhan

    This file is part of AndOpenGLCam.

    AndObjViewer is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    AndObjViewer is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with AndObjViewer.  If not, see <http://www.gnu.org/licenses/>.
 
 */
package edu.dhbw.andar;

import java.io.IOException;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.hardware.Camera;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Debug;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Gallery.LayoutParams;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import edu.dhbw.andar.exceptions.AndARRuntimeException;
import edu.dhbw.andar.interfaces.OpenGLRenderer;
import edu.dhbw.andar.layout.ListView_CheckBoxActivity;
import edu.dhbw.andar.pub.AugmentedModelViewerActivity;
import edu.dhbw.andar.util.IO;
import edu.dhbw.andopenglcam.R;


public abstract class AndARActivity extends Activity implements Callback, UncaughtExceptionHandler{
	private GLSurfaceView glSurfaceView;
	protected Camera camera;
	private AndARRenderer renderer;
	private Resources res;
	private CameraPreviewHandler cameraHandler;
	private boolean mPausing = false;
	private ARToolkit artoolkit;
	private CameraStatus camStatus = new CameraStatus();
	private boolean surfaceCreated = false;
	private SurfaceHolder mSurfaceHolder = null;
	private Preview previewSurface;
	private boolean startPreviewRightAway;
	Display display ;
	float leftMarginCenter;
	public  ImageView[] imageView;
	public  Button[] ButtonMenu;
	// bitmap texture for customObject
	public  Bitmap []b=new Bitmap[10];
	//2d picture
	public  ImageView[] imageView2;
	private String[] nameButtonMenu={"Animate","Motion","Off","Option","Exit"};
	public List<Integer> getModelFromActivityParent;
	public List<Integer> getPictureFromActivityParent;
	public AndARActivity() {
		startPreviewRightAway = true;
	}
	
	public AndARActivity(boolean startPreviewRightAway) {
		this.startPreviewRightAway = startPreviewRightAway;
	}

	int numberImage;
	int numberImage2d;
	public Bundle bundle;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.currentThread().setUncaughtExceptionHandler(this);
        res = getResources();
        bundle = getIntent().getExtras();
        if(bundle!=null) {
        getModelFromActivityParent=  bundle.getIntegerArrayList("picture_3d_choose");
        getPictureFromActivityParent = bundle.getIntegerArrayList("picture_2d_choose");
     numberImage=   getModelFromActivityParent.size();
     numberImage2d=  getPictureFromActivityParent.size();
        }
     //AugmentedModelViewerActivity params
     
     imageView=new ImageView[numberImage];
   
     ButtonMenu=new Button[5];
     imageView2=new ImageView[numberImage2d];

        artoolkit = new ARToolkit(res, getFilesDir());
        setFullscreen();
        disableScreenTurnOff();
        //orientation is set via the manifest
        
        try {
			IO.transferFilesToPrivateFS(getFilesDir(),res);
		} catch (IOException e) {
			e.printStackTrace();
			throw new AndARRuntimeException(e.getMessage());
		}
        display = getWindowManager().getDefaultDisplay(); 
        leftMarginCenter=display.getWidth()/2;
    	
       // imageView=new ImageView[5];
		FrameLayout frame = new FrameLayout(this);
		//create gallery image  model
		
		
		
		
		
		
		previewSurface = new Preview(this);
				
        glSurfaceView = new GLSurfaceView(this);
		renderer = new AndARRenderer(res, artoolkit, this);
		cameraHandler = new CameraPreviewHandler(glSurfaceView, renderer, res, artoolkit, camStatus);
        glSurfaceView.setRenderer(renderer);
        glSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        glSurfaceView.getHolder().addCallback(this);
       // 
       
        frame.addView(glSurfaceView);
        frame.addView(previewSurface);
     
        addImageToFrame(frame);
        addButtonMenuToFrame(frame);
       addImage2DToFrame(frame);
        
        setContentView(frame);
        if(Config.DEBUG)
        	Debug.startMethodTracing("AndAR");
    }
    
    
    /**
     * Set a renderer that draws non AR stuff. Optional, may be set to null or omited.
     * and setups lighting stuff.
     * @param customRenderer
     */
    public void setNonARRenderer(OpenGLRenderer customRenderer) {
		renderer.setNonARRenderer(customRenderer);
	}

    /**
     * Avoid that the screen get's turned off by the system.
     */
	public void disableScreenTurnOff() {
    	getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
    			WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }
    
	/**
	 * Set's the orientation to landscape, as this is needed by AndAR.
	 */
    public void setOrientation()  {
    	setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }
    
    /**
     * Maximize the application.
     */
    public void setFullscreen() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
   
    public void setNoTitle() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
    } 
    
    @Override
    protected void onPause() {
    	mPausing = true;
        this.glSurfaceView.onPause();
        super.onPause();
        finish();
        if(cameraHandler != null)
        	cameraHandler.stopThreads();
    }
    
    @Override
    protected void onDestroy() {
    	super.onDestroy();    	
    	System.runFinalization();
    	if(Config.DEBUG)
    		Debug.stopMethodTracing();
    }
    
    

    @Override
    protected void onResume() {
    	mPausing = false;
    	glSurfaceView.onResume();
        super.onResume();
    }
    
    /* (non-Javadoc)
     * @see android.app.Activity#onStop()
     */
    @Override
    protected void onStop() {
    	super.onStop();
    }
    
    /**
     * Open the camera.
     */
    private void openCamera()  {
    	if (camera == null) {
	    	//camera = Camera.open();
    		camera = CameraHolder.instance().open();
    		   		    		
    		
    		
    		try {
				camera.setPreviewDisplay(mSurfaceHolder);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
			CameraParameters.setCameraParameters(camera, 
					previewSurface.getWidth(), previewSurface.getHeight());
	        
	        if(!Config.USE_ONE_SHOT_PREVIEW) {
	        	camera.setPreviewCallback(cameraHandler);	 
	        } 
			try {
				cameraHandler.init(camera);
			} catch (Exception e) {
				e.printStackTrace();
			}			
    	}
    }
    
    private void closeCamera() {
        if (camera != null) {
        	CameraHolder.instance().keep();
        	CameraHolder.instance().release();
        	camera = null;
        	camStatus.previewing = false;
        }
    }
    
    /**
     * Open the camera and start detecting markers.
     * note: You must assure that the preview surface already exists!
     */
    public void startPreview() {
    	if(!surfaceCreated) return;
    	if(mPausing || isFinishing()) return;
    	if (camStatus.previewing) stopPreview();
    	openCamera();
		camera.startPreview();
		camStatus.previewing = true;
    }
    
    /**
     * Close the camera and stop detecting markers.
     */
    private void stopPreview() {
    	if (camera != null && camStatus.previewing ) {
    		camStatus.previewing = false;
            camera.stopPreview();
         }
    	
    }

	/* The GLSurfaceView changed
	 * @see android.view.SurfaceHolder.Callback#surfaceChanged(android.view.SurfaceHolder, int, int, int)
	 */
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		
	}

	/* The GLSurfaceView was created
	 * The camera will be opened and the preview started 
	 * @see android.view.SurfaceHolder.Callback#surfaceCreated(android.view.SurfaceHolder)
	 */
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		surfaceCreated = true;			
	}

	/* GLSurfaceView was destroyed
	 * The camera will be closed and the preview stopped.
	 * @see android.view.SurfaceHolder.Callback#surfaceDestroyed(android.view.SurfaceHolder)
	 */
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {

	}
	
	/**
	 * @return  a the instance of the ARToolkit.
	 */
	public ARToolkit getArtoolkit() {
		return artoolkit;
	}	
	
	/**
	 * Take a screenshot. Must not be called from the GUI thread, e.g. from methods like
	 * onCreateOptionsMenu and onOptionsItemSelected. You have to use a asynctask for this purpose.
	 * @return the screenshot
	 */
	public Bitmap takeScreenshot() {
		return renderer.takeScreenshot();
	}	
	
	/**
	 * 
	 * @return the OpenGL surface.
	 */
	public SurfaceView getSurfaceView() {
		return glSurfaceView;
	}
	
	class Preview extends SurfaceView implements SurfaceHolder.Callback {
	    SurfaceHolder mHolder;
	    Camera mCamera;
	    private int w;
	    private int h;
	    
	    Preview(Context context) {
	        super(context);
	        
	        // Install a SurfaceHolder.Callback so we get notified when the
	        // underlying surface is created and destroyed.
	        mHolder = getHolder();
	        mHolder.addCallback(this);
	        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	    }

	    public void surfaceCreated(SurfaceHolder holder) {
	    }

	    public void surfaceDestroyed(SurfaceHolder holder) {
	        // Surface will be destroyed when we return, so stop the preview.
	        // Because the CameraDevice object is not a shared resource, it's very
	        // important to release it when the activity is paused.
	        stopPreview();
	        closeCamera();
	        mSurfaceHolder = null;
	    }

	    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
	    	this.w=w;
	    	this.h=h;
	    	mSurfaceHolder = holder;
	    	if(startPreviewRightAway)
	    		startPreview();
	    }
	    
	    public int getScreenWidth() {
	    	return w;
	    }
	    
	    public int getScreenHeight() {
	    	return h;
	    }

	}
	public void addButtonMenuToFrame(FrameLayout frame)
	{
		RelativeLayout layout1 = new RelativeLayout(this);  

		
        layout1.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT)); 
        layout1.setId(1);
        float screenWidth =display.getWidth();
        int buttonWidth=(int)(screenWidth-10)/5;
        for(int i=0;i<5;i++)
        {
        	ButtonMenu[i]=new Button(this);
        	RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams(buttonWidth-5,45);
        	params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        	 params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        	 params.bottomMargin=10;
       
            	
            	
            	if(i==0)
            	{
    			params.leftMargin=8;
            	}
            	else
            		params.leftMargin=buttonWidth*i+8;
            	
        	
        	ButtonMenu[i].setLayoutParams(params);
        	ButtonMenu[i].setText(nameButtonMenu[i]);
        	ButtonMenu[i].setTextColor(Color.WHITE);
        	ButtonMenu[i].setBackgroundResource(R.drawable.button_press);
        	ButtonMenu[i].setId(i);
        	ButtonMenu[i].setVisibility(View.INVISIBLE);
        	layout1.addView(ButtonMenu[i]);
        }
        frame.addView(layout1);
		
	}
	
	public void addImageToFrame(FrameLayout frame)
	{
		/*Display display = getWindowManager().getDefaultDisplay(); 
		float leftMarginCenter=display.getWidth()/2;*/
		RelativeLayout layout1 = new RelativeLayout(this);  
        layout1.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT)); 
        
        float lenght=numberImage/2;
      
		for(int i=0;i<numberImage;i++)
		{
			imageView[i]=new ImageView(this);
			RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams(70,70);
			//params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
			params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
			params.topMargin=10;
			if(numberImage%2!=0)
			{
			
			  params.leftMargin=(int) ((leftMarginCenter-(70*lenght+35))+70*i);
			}
			else
			params.leftMargin=(int)((leftMarginCenter-(70*lenght))+70*i);
			
			
			
			
	
			imageView[i].setLayoutParams(params);
			
			
			imageView[i].setId(i);
		
			layout1.addView(imageView[i]);
			
			
		}
		
		
	
        frame.addView(layout1);
		
	
		
	}

	public void addImage2DToFrame(FrameLayout frame)
	{
		/*Display display = getWindowManager().getDefaultDisplay(); 
		float leftMarginCenter=display.getWidth()/2;*/
		RelativeLayout layout1 = new RelativeLayout(this);  
        layout1.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT)); 
        
        float lenght=numberImage2d/2;
      
		for(int i=0;i<numberImage2d;i++)
		{
			imageView2[i]=new ImageView(this);
			RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams(50,50);
			//params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
			params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
			params.topMargin=10;
			if(numberImage2d%2!=0)
			{
			
			  params.leftMargin=(int) ((leftMarginCenter-(50*lenght+25))+50*i);
			}
			else
			params.leftMargin=(int)((leftMarginCenter-(50*lenght))+50*i);
			
			
			
	
			imageView2[i].setLayoutParams(params);
			
			
			imageView2[i].setId(i+5);
			
			
			//AugmentedModelViewerActivity.imageView2[i].setBackgroundResource(ListView_CheckBoxActivity.model_2D_Picture[ListView_CheckBoxActivity.picture_2D_Choosed.get(i)]);
			imageView2[i].setVisibility(View.INVISIBLE);
			layout1.addView(imageView2[i]);
			
			
		}
		
		
	
        frame.addView(layout1);
	}
		
}