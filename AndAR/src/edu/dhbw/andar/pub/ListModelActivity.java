package edu.dhbw.andar.pub;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class ListModelActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		startActivity(new Intent(this,MainARActivity.class));
	}
	

}
