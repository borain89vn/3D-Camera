package edu.dhbw.andar.database;

import java.io.File;
import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import edu.dhbw.andar.models.Model3DPhoto;

public class StorageFileTable {
	public static final String ID = "_id";
	public static final String NAME = "name";
	public static final String CATEGORY = "category";
	public static final String PNG_FILE = "png_file";
	public static final String OBJ_FILE = "obj_file";
	public static final String CATEGORY_PATH = "category_path";
	public static final String TABLE_NAME = "storage";
	

	public static ArrayList<Model3DPhoto> getModelByCategory(SchemaHelper helper,String category,Context context) {

		String sql = "SELECT *from " + TABLE_NAME + " where category = " +"'"+category+"'";
		ArrayList<Model3DPhoto> listModel = new ArrayList<Model3DPhoto>();
		SQLiteDatabase sqlWriter = helper.getWritableDatabase();
		Cursor c = sqlWriter.rawQuery(sql, null);
		while (c.moveToNext()) {
			Model3DPhoto model = new Model3DPhoto();
			model.context = context;
			model.setCategory(c.getString(c.getColumnIndex(CATEGORY)));
			model.setName(c.getString(c.getColumnIndex(NAME)));
			model.setObjName(c.getString(c.getColumnIndex(OBJ_FILE)));
			model.setPngName(c.getString(c.getColumnIndex(PNG_FILE)));
			listModel.add(model);
		}

		c.close();
		sqlWriter.close();
		helper.close();
		return listModel;
	}
	public static boolean getModelByName(SchemaHelper helper,String category,Context context,String name) {
		boolean isNull = false;
		String sql = "SELECT *from " + TABLE_NAME + " where category = " +"'"+category+"' "+" AND name="+"'"+name+"'";
	    SQLiteDatabase sqlWriter = helper.getWritableDatabase();
	    Cursor c = sqlWriter.rawQuery(sql, null);
	    if(c.moveToFirst()==true)
	    {
	    	isNull = true;
	    }
	    return isNull;
	    
	    
	}
	public static void deleteModel(Model3DPhoto model,SchemaHelper sHelper,String category,Context context,String objName){
		String sql = "DELETE  from " + TABLE_NAME + " where category = " +"'"+category+"' "+" AND obj_file ="+"'"+objName+"'";
		SQLiteDatabase sqlWriter = sHelper.getWritableDatabase();
		sqlWriter.execSQL(sql);
		sqlWriter.close();
		File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/models/"+model.getCategory()+"/"+model.getName());
		file.delete();
	}
}
