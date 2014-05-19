package edu.dhbw.andar;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import edu.dhbw.andar.database.SchemaHelper;
import edu.dhbw.andar.models.JsonModel;
import edu.dhbw.andar.service.ConstantValue;
import edu.dhbw.andar.service.JSONParser;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

public class CheckJson extends Activity{
	JSONParser jsonParser = new JSONParser();
	ArrayList<JsonModel> arrJsonModel;
	SchemaHelper helper;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		helper= new SchemaHelper(this);
		
		getModel a = new getModel();
		a.execute();
		
	}
	class getModel extends AsyncTask<Void, Void, Void> {
		 
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
           
 
        }
 
        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
        	 List<NameValuePair> params = new ArrayList<NameValuePair>();
             params.add(new BasicNameValuePair("pid", "architecture"));

             // getting product details by making HTTP request
             // Note that product details url will use GET request
             JSONObject json = jsonParser.makeHttpRequest(
                     ConstantValue.urlCategory, "GET", params);
             arrJsonModel = jsonParser.getCategoryModel(json, params);
             // check your log for json response
             Log.d("Single Product Details", json.toString());
            return null;
        }
 
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            JsonModel model;
            model= arrJsonModel.get(1);
            helper.addModel(model, 1);
            // Dismiss the progress dialog
            
           
        }
 
    
 
}
}
