package edu.dhbw.andar.models;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

public class Model3DPhoto implements Parcelable{
	private String location;
	private String category;
	private String name;

	private String objName;
	private String pngName;
	public  Context context;

	public Model3DPhoto() {
	
	}
	
    public Model3DPhoto(Parcel in) {
    	category = in.readString();
    	name = in.readString();
    	objName = in.readString();
    	pngName = in.readString();
    	
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

	public Drawable getDrawable(String path) {
		Drawable d = null;
		try {

			InputStream ims = context.getAssets().open(path);
			
				d = Drawable.createFromStream(ims, null);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return d;
	}

	public String getObjName() {
		return objName;
	}

	public void setObjName(String objName) {
		this.objName = objName;
	}

	public String getPngName() {
		return pngName;
	}

	public void setPngName(String pngName) {
		this.pngName = pngName;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(category);
		dest.writeString(name);
		dest.writeString(objName);
		dest.writeString(pngName);
	}
	public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
		
        public Model3DPhoto[] newArray(int size) {
        	return new Model3DPhoto[size];
        }
		@Override
		public Object createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			return new Model3DPhoto(source);
		}
	
	};
}
