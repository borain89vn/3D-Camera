package edu.dhbw.andar.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class ServiceHandler {
	
	String response = null;
	static final int GET = 1;

	
	public void makeCallService(String url,int method,int id) {
		try {
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpEntity httpEntity = null;
		HttpResponse httpRespone = null;
		if(method== GET) {
			HttpGet httpGet = new HttpGet(url+String.valueOf(id));
			httpRespone = httpClient.execute(httpGet);
		}
		httpEntity = httpRespone.getEntity();
		response = EntityUtils.toString(httpEntity);
		} catch(UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}

}
