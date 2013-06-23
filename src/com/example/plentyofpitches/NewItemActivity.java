package com.example.plentyofpitches;

import java.io.IOException;

import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class NewItemActivity extends Activity {
	private String urlItem;
	public final static String ITEM_ID_KEY = "com.example.plentyofpitches.ITEM_ID_KEY";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_item);
		// Show the Up button in the action bar.
		setupActionBar();
		
		Intent intent = getIntent();
		String message = intent.getStringExtra(MainActivity.ITEM_TYPE_KEY);
		
		TextView itemType = (TextView) findViewById(R.id.itemType);
		itemType.setText(message);
		
		urlItem = message;
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
		getMenuInflater().inflate(R.menu.new_item, menu);
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

	public void save(View view) throws JSONException, IOException {
		EditText descriptionEditText = (EditText) findViewById(R.id.description);
		String description = descriptionEditText.getText().toString();

		JSONObject requestObject = new JSONObject();
		try {
			requestObject.put("description", description);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String requestString = requestObject.toString();
		
		final Context self = this;
		AsyncHttpClient client = new AsyncHttpClient();
		client.post(this, "http://50.116.4.81:5000/" + urlItem, new StringEntity(requestString), "application/json", new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String response) {
				try {
					JSONObject jObject = new JSONObject(response);
					int id = jObject.getInt("id");
					
					Intent intent = new Intent(self, ExistingItemActivity.class);
					intent.putExtra(MainActivity.ITEM_TYPE_KEY, urlItem);
					intent.putExtra(NewItemActivity.ITEM_ID_KEY, id);
			    	startActivity(intent);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}
}
