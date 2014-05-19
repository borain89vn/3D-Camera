package edu.dhbw.andar.pub;

import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL10Ext;

import android.opengl.GLU;
import android.opengl.GLUtils;

import edu.dhbw.andar.ARObject;
import edu.dhbw.andar.AndARRenderer;
import edu.dhbw.andar.util.GraphicsUtil;

/**
 * An example of an AR object being drawn on a marker.
 * @author tobi
 *
 */
public class CustomObject extends ARObject {

	public int idTexture;
	public boolean checkOnOff=false;
	
	
	public float xrot = 90;
    public float yrot = 0;
    public float zrot = 0;
    public float xpos = 0;
    public float ypos = 0;
    public float zpos = 0;
    public float scale = 5f;
    public float xScale;
    public float yScale;
    public float zScale;
    int speedFall=1;
    int speedJump=2;
    public int jumpHeigh;
    public int moveDistance;
	public CustomObject(String name, String patternName,
			double markerWidth, double[] markerCenter,int idtexture) {
		super(name, patternName, markerWidth, markerCenter);
		idTexture =idtexture;
		float   mat_ambientf[]     = {0f, 1.0f, 0f, 1.0f};
		float   mat_flashf[]       = {0f, 1.0f, 0f, 1.0f};
		float   mat_diffusef[]       = {0f, 1.0f, 0f, 1.0f};
		float   mat_flash_shinyf[] = {50.0f};

		mat_ambient = GraphicsUtil.makeFloatBuffer(mat_ambientf);
		mat_flash = GraphicsUtil.makeFloatBuffer(mat_flashf);
		mat_flash_shiny = GraphicsUtil.makeFloatBuffer(mat_flash_shinyf);
		mat_diffuse = GraphicsUtil.makeFloatBuffer(mat_diffusef);
		
	}
	public CustomObject(String name, String patternName,
			double markerWidth, double[] markerCenter, float[] customColor) {
		super(name, patternName, markerWidth, markerCenter);
		float   mat_flash_shinyf[] = {50.0f};

		mat_ambient = GraphicsUtil.makeFloatBuffer(customColor);
		mat_flash = GraphicsUtil.makeFloatBuffer(customColor);
		mat_flash_shiny = GraphicsUtil.makeFloatBuffer(mat_flash_shinyf);
		mat_diffuse = GraphicsUtil.makeFloatBuffer(customColor);
		
	}
	
	private SimpleBox box = new SimpleBox();
	private FloatBuffer mat_flash;
	private FloatBuffer mat_ambient;
	private FloatBuffer mat_flash_shiny;
	private FloatBuffer mat_diffuse;
	
	/**
	 * Everything drawn here will be drawn directly onto the marker,
	 * as the corresponding translation matrix will already be applied.
	 */
	@Override
	public final void draw(GL10 gl) {
		super.draw(gl);
		/*
		gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_SPECULAR,mat_flash);
		gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_SHININESS, mat_flash_shiny);	
		gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_DIFFUSE, mat_diffuse);	
		gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_AMBIENT, mat_ambient);
*/
	    //draw cube
		//gl.glScalef(5.0f, 5.0f, 0.02f);
		gl.glScalef(this.xScale, this.yScale, this.zScale);
		gl.glTranslatef(this.xpos, this.ypos, this.zpos);
		gl.glRotatef(this.xrot, 1, 0, 0);
		gl.glRotatef(this.yrot, 0, 1, 0);
		gl.glRotatef(this.zrot, 0, 0, 1);
	    gl.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
	    gl.glTranslatef( 0.0f, 0.0f, 12.5f );
	    
	    
	    box.draw(gl,idTexture);
	}
	public void setScale(float f) {
		this.scale += f;
		if(this.scale < 0.0001f)
			this.scale = 0.0001f;
	}

	public void setXrot(float dY) {
		this.xrot += dY;
	}

	public void setYrot(float dX) {
		this.yrot += dX;
	}

	public void setXpos(float f) {
		this.xpos += f;
	}

	public void setYpos(float f) {
		this.ypos += f;
	}
	public void setZpos(float f)
	{
		this.zpos+=f;
	}
	public void danceRotate( )
	{
		
		this.setXrot(0);
		this.setYrot(-0.5f);
		
		
	}
	public void danceFall()
	{
		this.setXrot(-1*speedFall);
		this.setYrot(0);
		if(this.xrot<0||this.xrot>90)
		{
			speedFall*=-1;
			
		}
	}
	public void danceJump( )
	{
		this.zpos=this.zpos+speedJump;
		if(this.zpos<0||this.zpos>jumpHeigh)
		{
			speedJump*=-1;
			jumpHeigh=AugmentedModelViewerActivity.randomHeigh;
			
		}
	}
	
	
	
	
	
	public void danceMove()
	{
		this.xpos=this.xpos+speedJump;
		if(this.xpos<-moveDistance||this.xpos>moveDistance)
		{
			speedJump*=-1;
			moveDistance=AugmentedModelViewerActivity.randomHeigh;
		}
	}
	@Override
	public void init(GL10 gl) {
		// TODO Auto-generated method stub
		
	}
}