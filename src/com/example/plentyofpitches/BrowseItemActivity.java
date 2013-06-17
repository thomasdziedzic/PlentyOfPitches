package com.example.plentyofpitches;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class BrowseItemActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_browse_item);
		
		Intent intent = getIntent();
		String itemType = intent.getStringExtra(MainActivity.ITEM_TYPE_KEY);
		
		TextView textView = (TextView) findViewById(R.id.textView1);
		textView.setText(itemType);
		
		BrowseItem readItem = new BrowseItem(this);
		readItem.execute("http://50.116.4.81:5000/" + itemType + "s");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.browse_item, menu);
		return true;
	}

	public class BrowseItem extends AsyncTask<String, Void, String> {
		Context ctx;
		
		BrowseItem(Context ctx) {
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
				final String[] numbers = new String[] { "one", "two", "three",
		            "four", "five", "six", "seven", "eight", "nine", "ten", "eleven",
		            "twelve", "thirteen", "fourteen", "fifteen", "sixteen",
		            "seventeen", "eighteen", "nineteen", "twenty", "twenty one",
		            "twenty two" };
				
				JSONArray jArray = new JSONArray(result);
				List<JSONObject> jObjects = new ArrayList<JSONObject>();
				for(Integer i = 0; i < jArray.length(); i++) {
					jObjects.add(jArray.getJSONObject(i));
				}
				
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.ctx, android.R.layout.simple_list_item_1, numbers);
				//adapter.addAll(jObjects);	
				
				ListView listView = (ListView) findViewById(R.id.listView1);
				listView.setAdapter(adapter);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
