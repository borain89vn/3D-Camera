package edu.dhbw.andar.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import edu.dhbw.andar.database.SchemaHelper;
import edu.dhbw.andar.models.JsonModel;

public class JsonModelAsynTask extends AsyncTask<String, Void, Void> {
	JSONParser jsonParser = new JSONParser();
	public ArrayList<JsonModel> arrJsonModel;
	SchemaHelper sHelper;
	ProgressDialog dialog;
	Context context;
	public JsonModelAsynTask(SchemaHelper sHelper,Context context){
		this.sHelper = sHelper;
		this.context = context;
	
		
	}
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        // Showing progress dialog
        dialog = ProgressDialog.show(context, "Download Image", "downloading..");
       

    }

    @Override
    protected Void doInBackground(String... arg0) {
        // Creating service handler class instance
    	 List<NameValuePair> params = new ArrayList<NameValuePair>();
         params.add(new BasicNameValuePair("pid", arg0[0]));

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
        int size = arrJsonModel.size();
        Log.d("asdasda",arrJsonModel.get(0).getZip_link());
        dialog.dismiss();
        
       
    }
}
