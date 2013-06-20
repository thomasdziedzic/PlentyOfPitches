package com.example.plentyofpitches;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
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
import android.widget.TextView;

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
		
		ReadItem readItem = new ReadItem(this);
		readItem.execute("http://50.116.4.81:5000/" + itemType + "/" + id);
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
	
	public void delete(View view) {
		DeleteItem deleteItem = new DeleteItem(this);
		deleteItem.execute("http://50.116.4.81:5000/" + itemType + "/" + id);
	}
	
	public class DeleteItem extends AsyncTask<String, Void, String> {
		Context ctx;
		
		DeleteItem(Context ctx) {
			super();
			
			this.ctx = ctx;
		}
		protected String doInBackground(String... urls) {
			String url = urls[0];
			
			HttpClient client = new DefaultHttpClient();
			HttpDelete delete = new HttpDelete(url);
			
			String ret = "";
			
			try {
				HttpResponse response = client.execute(delete);
				
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
			finish();
		}
	}

	public class ReadItem extends AsyncTask<String, Void, String> {
		Context ctx;
		
		ReadItem(Context ctx) {
			super();
			
			this.ctx = ctx;
		}
		protected String doInBackground(String... urls) {
			String url = urls[0];
			
			HttpClient client = new DefaultHttpClient();
			HttpGet get = new HttpGet(url);
			
			String ret = "";
			
			try {
				HttpResponse response = client.execute(get);
				
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
				String description = jObject.getString("description");
				
				TextView editText = (TextView) findViewById(R.id.textView2);
				editText.setText(description);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
