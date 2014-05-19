package edu.dhbw.andar.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import edu.dhbw.andar.models.OBJ_PNG;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

public class ManageAssetsFile {

	
   static boolean isObjFile = false;
    static boolean isPngFile = false;
	

	

	
	public static ArrayList<OBJ_PNG> getPNGName(String category,Context context) {

        ArrayList<OBJ_PNG> ObjPng = new ArrayList<OBJ_PNG>();
		// To get names of all files inside the "Files" folder
		try {
			String[] files = context.getAssets().list("models/"+category);
			OBJ_PNG obj = new OBJ_PNG();
			for (int i = 0; i < files.length; i++) {
			
				if(files[i].endsWith("obj")) {
					obj.setObjFile(files[i]);
					isObjFile = true;
				}
				else if(files[i].endsWith("png")) {
					obj.setPngFile(files[i]);
					isPngFile = true;
				}
				if(isObjFile==true&&isPngFile==true) {
				   ObjPng.add(obj);
				   isObjFile = false;
				   isPngFile = false;
				   obj = new OBJ_PNG();
				}
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return ObjPng;
	}
	
}

