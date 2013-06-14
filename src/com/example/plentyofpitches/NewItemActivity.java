package com.example.plentyofpitches;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

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
		CreateItem google = new CreateItem(this, urlItem);
		google.execute("http://50.116.4.81:5000/" + urlItem);
	}
	
	public class CreateItem extends AsyncTask<String, Void, String> {
		Context ctx;
		String itemType;
		
		CreateItem(Context ctx, String itemType) {
			super();
			
			this.ctx = ctx;
			this.itemType = itemType;
		}
		protected String doInBackground(String... urls) {
			String url = urls[0];
			
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(url);
			
			String ret = "";
			
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
			
			try {
				post.setEntity(new StringEntity(requestString));
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			try {
				HttpResponse response = client.execute(post);
				
				StatusLine statusLine = response.getStatusLine();
				int statusCode = statusLine.getStatusCode();
				
				if (statusCode == 200) {
					String line = "";

					BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
					while ((line = rd.readLine()) != null) {
	                    ret += line;
	                }
				}
			} catch (ClientProtocolException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
			
			return ret;
		}
		
		protected void onPostExecute(String result) {
			try {
				JSONObject jObject = new JSONObject(result);
				int id = jObject.getInt("id");
				
				Intent intent = new Intent(ctx, ExistingItemActivity.class);
				intent.putExtra(MainActivity.ITEM_TYPE_KEY, this.itemType);
				intent.putExtra(NewItemActivity.ITEM_ID_KEY, id);
		    	startActivity(intent);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
}
