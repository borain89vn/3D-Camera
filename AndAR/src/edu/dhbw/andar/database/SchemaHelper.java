package edu.dhbw.andar.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import edu.dhbw.andar.models.JsonModel;
import edu.dhbw.andar.models.OBJ_PNG;

public class SchemaHelper extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "model_data.db";
	private static final int DATABASE_VERSION = 1;

	public SchemaHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		Log.d("fuck you","asdasda");
		db.execSQL("CREATE TABLE " +ModelTable.TABLE_NAME
				+
				" (" +ModelTable.ID +" INTEGER PRIMARY KEY " 
				+
				 "AUTOINCREMENT, "
				+ ModelTable.NAME + " TEXT, "
				+ ModelTable.CATEGORY + " TEXT, "
				+ ModelTable.IMAGE_LINK + " TEXT, "
				+ ModelTable.ZIP +" TExT," 
				+ ModelTable.DELETE_STATUS +" INTEGER); ");
		db.execSQL("CREATE TABLE " +StorageFileTable.TABLE_NAME
				+
				" (" +StorageFileTable.ID +" INTEGER PRIMARY KEY "
				+ "AUTOINCREMENT, "
				+StorageFileTable.NAME + " TEXT, "
				+StorageFileTable.CATEGORY + " TEXT, "
				+StorageFileTable.PNG_FILE + " TEXT, "
				+StorageFileTable.OBJ_FILE + " TEXT, "
				
				+StorageFileTable.CATEGORY_PATH +" TEXT ) ;"); 
				
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}
	public void addModel(JsonModel model,int i) {
		ContentValues cv = new ContentValues();
		cv.put(ModelTable.NAME,model.getName());
		cv.put(ModelTable.CATEGORY,model.getCategory());
		cv.put(ModelTable.IMAGE_LINK, model.getImg_link());
		cv.put(ModelTable.ZIP, model.getZip_link());
		cv.put(ModelTable.DELETE_STATUS, i);
		SQLiteDatabase sd = getWritableDatabase();
		sd.insert(ModelTable.TABLE_NAME, null, cv);
	}
	public void addStorageModel(String name,String category,OBJ_PNG OP) {
		ContentValues cv = new ContentValues();
		cv.put(StorageFileTable.NAME, name);
		cv.put(StorageFileTable.CATEGORY, category);
		cv.put(StorageFileTable.PNG_FILE, OP.getPngFile());
		cv.put(StorageFileTable.OBJ_FILE, OP.getObjFile());
		cv.put(StorageFileTable.CATEGORY_PATH, "models/"+category);
		SQLiteDatabase sd = getWritableDatabase();
		sd.insert(StorageFileTable.TABLE_NAME, null, cv);
	}
	

}
