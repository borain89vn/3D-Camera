package edu.dhbw.andar.models;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import edu.dhbw.andopenglcam.R;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

public class Model3DPhoto {
	private String location;
	private String category;
	private String name;
	private OBJ_PNG obj_png;
	Context contex;
	public Model3DPhoto(Context c) {
		contex= c;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
    
	public void saveToSDCard(int idPhoto) {
		
		File myDir = new File(this.getLocation());
		myDir.mkdir();
		  // ImageView view= (ImageView)findViewById(R.id.cachesView);

        // view.buildDrawingCache();
		
		
	}
	public OBJ_PNG getObj_png() {
		return obj_png;
	}
	public void setObj_png(OBJ_PNG obj_png) {
		this.obj_png = obj_png;
	}
	public Drawable getDrawable(String path) {
		Drawable d = null;
		try {
			InputStream ims = contex.getAssets().open(path);
		d = Drawable.createFromStream(ims, null);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return d;
	}

}
