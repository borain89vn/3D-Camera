package edu.dhbw.andar.layout;

import android.app.Activity;
import android.os.Bundle;
import edu.dhbw.andopenglcam.R;



public class CategoryActivity extends Activity{
   
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.category_list);
		Category category_data[] = new Category[]
		        {/*
		            new Category(R.drawable.movie_icon, "Ä�iá»‡n áº£nh"),
		            new Category(R.drawable.science_icon, "Khoa há»�c"),
		            new Category(R.drawable.englist_icon, "Tá»•ng há»£p"),
		            new Category(R.drawable.sport_icon, "Thá»ƒ thao"),
		            new Category(R.drawable.geography2_icon, "Ä�á»‹a lÃ½"),
		            new Category(R.drawable.geography_icon, "Tiáº¿ng anh")*/
		        };
		       
		        CategoryAdapter adapter = new CategoryAdapter(this,
		                R.layout.item_category, category_data);
	}
}
