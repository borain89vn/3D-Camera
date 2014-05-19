package edu.dhbw.andar.pub;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.graphics.Bitmap;
import android.graphics.Color;
import edu.dhbw.andar.util.GraphicsUtil;

public class SimpleBox {
	private FloatBuffer box;
	private FloatBuffer normals;
	private FloatBuffer tb;
	
	
	public SimpleBox() 
	
	
	{
		float boxf[] =  {
				// FRONT
				-25.0f, -25.0f,  25.0f,
				 25.0f, -25.0f,  25.0f,
				-25.0f,  25.0f,  25.0f,
				 25.0f,  25.0f,  25.0f,
				// BACK
				-25.0f, -25.0f, -25.0f,
				-25.0f,  25.0f, -25.0f,
				 25.0f, -25.0f, -25.0f,
				 25.0f,  25.0f, -25.0f,
				// LEFT
				-25.0f, -25.0f,  25.0f,
				-25.0f,  25.0f,  25.0f,
				-25.0f, -25.0f, -25.0f,
				-25.0f,  25.0f, -25.0f,
				// RIGHT
				 25.0f, -25.0f, -25.0f,
				 25.0f,  25.0f, -25.0f,
				 25.0f, -25.0f,  25.0f,
				 25.0f,  25.0f,  25.0f,
				// TOP
				-25.0f,  25.0f,  25.0f,
				 25.0f,  25.0f,  25.0f,
				 -25.0f,  25.0f, -25.0f,
				 25.0f,  25.0f, -25.0f,
				// BOTTOM
				-25.0f, -25.0f,  25.0f,
				-25.0f, -25.0f, -25.0f,
				 25.0f, -25.0f,  25.0f,
				 25.0f, -25.0f, -25.0f,
			};
		
		float normalsf[] =  {
				// FRONT
				0.0f, 0.0f,  1.0f,
				0.0f, 0.0f,  1.0f,
				0.0f, 0.0f,  1.0f,
				0.0f, 0.0f,  1.0f,
				// BACK
				0.0f, 0.0f,  -1.0f,
				0.0f, 0.0f,  -1.0f,
				0.0f, 0.0f,  -1.0f,
				0.0f, 0.0f,  -1.0f,
				// LEFT
				-1.0f, 0.0f,  0.0f,
				-1.0f, 0.0f,  0.0f,
				-1.0f, 0.0f,  0.0f,
				-1.0f, 0.0f,  0.0f,
				// RIGHT
				1.0f, 0.0f,  0.0f,
				1.0f, 0.0f,  0.0f,
				1.0f, 0.0f,  0.0f,
				1.0f, 0.0f,  0.0f,
				// TOP
				0.0f, 1.0f,  0.0f,
				0.0f, 1.0f,  0.0f,
				0.0f, 1.0f,  0.0f,
				0.0f, 1.0f,  0.0f,
				// BOTTOM
				0.0f, -1.0f,  0.0f,
				0.0f, -1.0f,  0.0f,
				0.0f, -1.0f,  0.0f,
				0.0f, -1.0f,  0.0f,
			};
		float[] tc = new float[]
				{
				1.0f, 1.0f,
				0.0f, 1.0f,
				1.0f, 0.0f,
				0.0f, 0.0f,

				1.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 0.0f, };
		tb = GraphicsUtil.makeFloatBuffer(tc);
		
		box = GraphicsUtil.makeFloatBuffer(boxf);
		normals = GraphicsUtil.makeFloatBuffer(normalsf);
	}
	
	
	public final void draw(GL10 gl,int id) {	
		Bitmap[] bitmap = AugmentedModelViewerActivity.b; //1- an explanation is appended
		gl.glEnable(GL10.GL_CULL_FACE); // Enable face culling.
		gl.glCullFace(GL10.GL_BACK); // What faces to remove with the face culling.
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glEnable(GL10.GL_TEXTURE_2D);
		loadTextureFromBitmap(gl, bitmap[id]); //2 - function is appended
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, box);
	gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, tb); //3 - values are appended
		gl.glNormalPointer(GL10.GL_FLOAT,0, normals);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 4, 4);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 8, 4);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 12, 4);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 16, 4);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 20, 4);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glDisableClientState(GL10.GL_NORMAL_ARRAY);
		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glDisable(GL10.GL_TEXTURE_2D);
		
	}
	public int loadTextureFromBitmap(GL10 gl, Bitmap bitmap)
	{
	int[] textures = new int[1];
	gl.glGenTextures(1, textures, 0);
	gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[0]);
	gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
	gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_NEAREST);
	ByteBuffer imageBuffer = ByteBuffer.allocateDirect(bitmap.getHeight() * bitmap.getWidth() * 4);
	imageBuffer.order(ByteOrder.nativeOrder());
	byte buffer[] = new byte[4];
	for(int i = 0; i < bitmap.getHeight(); i++)
	{
	for(int j = 0; j < bitmap.getWidth(); j++)
	{
	int color = bitmap.getPixel(j, i);
	buffer[0] = (byte)Color.red(color);
	buffer[1] = (byte)Color.green(color);
	buffer[2] = (byte)Color.blue(color);
	buffer[3] = (byte)Color.alpha(color);
	imageBuffer.put(buffer);
	}
	}
	imageBuffer.position(0);
	gl.glTexImage2D(GL10.GL_TEXTURE_2D, 0, GL10.GL_RGBA, bitmap.getWidth(), bitmap.getHeight(), 0, GL10.GL_RGBA, GL10.GL_UNSIGNED_BYTE, imageBuffer);
	return textures[0];
	}
}