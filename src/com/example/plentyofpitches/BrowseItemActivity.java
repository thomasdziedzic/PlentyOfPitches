package com.example.plentyofpitches;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.library.HttpUtils;
import com.example.library.JArrayAdapter;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class BrowseItemActivity extends Activity {
	private String itemType;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_browse_item);
		
		Intent intent = getIntent();
		itemType = intent.getStringExtra(MainActivity.ITEM_TYPE_KEY);
		
		TextView textView = (TextView) findViewById(R.id.textView1);
		textView.setText(itemType);
		
		ListView listView = (ListView) findViewById(R.id.listView1);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
				final JSONObject value = (JSONObject) parent.getItemAtPosition(position);
				try {
					final int itemId = value.getInt("id");
					final Context ctx = parent.getContext();
					
					Intent intent = new Intent(ctx, ExistingItemActivity.class);
					intent.putExtra(MainActivity.ITEM_TYPE_KEY, itemType);
					intent.putExtra(NewItemActivity.ITEM_ID_KEY, itemId);
			    	startActivity(intent);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		final Context self = this;
		AsyncHttpClient client = HttpUtils.getAsyncHttpClient();
		client.get("https://50.116.4.81/" + itemType + "s", new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String response) {
				try {
					JSONArray jArray = new JSONArray(response);
					List<JSONObject> jObjects = new ArrayList<JSONObject>();
					for(Integer i = 0; i < jArray.length(); i++) {
						jObjects.add(jArray.getJSONObject(i));
					}
					
					JArrayAdapter adapter = new JArrayAdapter(self, jObjects);
					
					ListView listView = (ListView) findViewById(R.id.listView1);
					listView.setAdapter(adapter);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.browse_item, menu);
		return true;
	}
}
