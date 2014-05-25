package edu.dhbw.andar.layout;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;
import edu.dhbw.andar.models.Model3DPhoto;
import edu.dhbw.andar.util.AssetsFileUtil;
import edu.dhbw.andar.util.BaseFileUtil;
import edu.dhbw.andar.util.SDCardFileUtil;
import edu.dhbw.andopenglcam.R;

public class CheckboxAdapter extends BaseAdapter {

	Context context;
	ArrayList<Model3DPhoto> listData;
	AssetManager am;
	HashMap<Integer, Boolean> state;
	List<String> mapList = null;

	public CheckboxAdapter(Context context, ArrayList<Model3DPhoto> listData) {
		this.context = context;
		this.listData = listData;
		state = new HashMap<Integer, Boolean>();

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
		ViewHolder viewHolder = new ViewHolder();
		Model3DPhoto model = listData.get(position);
		if (convertView == null) {
			LayoutInflater mInflater = LayoutInflater.from(context);

			convertView = mInflater.inflate(R.layout.item, null);
			viewHolder.imgView = (ImageView) convertView
					.findViewById(R.id.model_image_3d);

			viewHolder.txtView = (TextView) convertView
					.findViewById(R.id.model_name_3d);
			viewHolder.txtView.setText((String) listData.get(position)
					.getName());

			viewHolder.chekBox = (CheckBox) convertView
					.findViewById(R.id.selected_model_3d);
			viewHolder.chekBox
					.setOnCheckedChangeListener(new OnCheckedChangeListener() {
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
			viewHolder.chekBox.setChecked((state.get(position) == null ? false
					: true));
			if (checkIfInAssets(model.getPngName(), model.getCategory())) {
				viewHolder.imgView.setBackgroundDrawable(model
						.getDrawable("models/" + model.getCategory() + "/"
								+ model.getPngName()));
			} else {
				BaseFileUtil fileUtil = null;

				fileUtil = new SDCardFileUtil();

				File modelFile = Environment.getExternalStorageDirectory();

				fileUtil.setBaseFolder(modelFile.getName() + "/models/"
						+ model.getCategory() + "/");
				viewHolder.imgView.setImageBitmap(fileUtil
						.getBitmapFromName(model.getPngName()));
			}

		}

		return convertView;
	}

	public boolean checkIfInAssets(String assetName, String category) {
		if (mapList == null) {
			am = context.getAssets();
			try {
				mapList = Arrays.asList(am.list("models/" + category));
			} catch (IOException e) {
			}
		}
		return mapList.contains(assetName) ? true : false;
	}

	class ViewHolder {
		ImageView imgView;
		TextView txtView;
		CheckBox chekBox;
	}
}