package edu.dhbw.andar.layout;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;
import edu.dhbw.andopenglcam.R;

public class CheckboxAdapter2 extends BaseAdapter {
	
	Context context;
	ArrayList<HashMap<String, Object>> listData;	

	HashMap<Integer, Boolean> state = new HashMap<Integer, Boolean>();		


	public CheckboxAdapter2(Context context,	ArrayList<HashMap<String, Object>> listData) {
		this.context = context;
		this.listData = listData;	
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listData.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return listData.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	// é‡�å†™View
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
	
		LayoutInflater mInflater = LayoutInflater.from(context);
		convertView = mInflater.inflate(R.layout.item_2d_picture, null);
		ImageView image = (ImageView) convertView.findViewById(R.id.model_image_2d);
		image.setBackgroundResource((Integer) listData.get(position).get("model_image_2d"));
		/*TextView username = (TextView) convertView.findViewById(R.id.model_name_2d);
		username.setText((String) listData.get(position).get("model_name_2d"));*/
		
		CheckBox check = (CheckBox) convertView.findViewById(R.id.selected_model_2d);		
		check.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					state.put(position, isChecked);					
				} else {
					state.remove(position);				
				}
			}
		});
		check.setChecked((state.get(position) == null ? false : true));
		return convertView;
	}
}