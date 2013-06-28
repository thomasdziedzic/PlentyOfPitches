package com.example.plentyofpitches;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.library.HttpUtils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class ExistingItemActivity extends Activity {
	private int id;
	private String itemType;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_existing_item);
		// Show the Up button in the action bar.
		setupActionBar();
		
		Intent intent = getIntent();
		itemType = intent.getStringExtra(MainActivity.ITEM_TYPE_KEY);
		id = intent.getIntExtra(NewItemActivity.ITEM_ID_KEY, 0);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
		AsyncHttpClient client = HttpUtils.getAsyncHttpClient();
		client.get("https://50.116.4.81/" + itemType + "/" + id, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String response) {
				try {
					JSONObject jObject = new JSONObject(response);
					String description = jObject.getString("description");
					
					TextView editText = (TextView) findViewById(R.id.textView2);
					editText.setText(description);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.existing_item, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void edit(View view) {
		Intent intent = new Intent(this, EditItemActivity.class);
		intent.putExtra(MainActivity.ITEM_TYPE_KEY, itemType);
		intent.putExtra(NewItemActivity.ITEM_ID_KEY, id);
    	startActivity(intent);
	}
	
	public void delete(View view) {
		AsyncHttpClient client = HttpUtils.getAsyncHttpClient();
		client.delete("https://50.116.4.81/" + itemType + "/" + id, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String response) {
				finish();
			}
		});
	}
}
