package com.example.library;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.plentyofpitches.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class JArrayAdapter extends ArrayAdapter<JSONObject> {
	private final Context context;
	private final List<JSONObject> values;
	
	public JArrayAdapter(Context context, List<JSONObject> values) {
		super(context, R.layout.item_row, values);
		this.context = context;
		this.values = values;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater  inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.item_row, parent, false);
		TextView textView = (TextView) rowView.findViewById(R.id.textView1);
		JSONObject value = values.get(position);
		
		try {
			textView.setText(value.getString("description"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return rowView;
	}
}