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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

// TODO we need to reload the list onResume from the
// server in case the user deletes an item in the list
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
		
		BrowseItem readItem = new BrowseItem(this);
		readItem.execute("http://50.116.4.81:5000/" + itemType + "s");
		
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
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.browse_item, menu);
		return true;
	}
	
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
				JSONArray jArray = new JSONArray(result);
				List<JSONObject> jObjects = new ArrayList<JSONObject>();
				List<String> descriptions = new ArrayList<String>();
				for(Integer i = 0; i < jArray.length(); i++) {
					jObjects.add(jArray.getJSONObject(i));
					descriptions.add(jArray.getJSONObject(i).getString("description"));
				}
				
				JArrayAdapter adapter = new JArrayAdapter(this.ctx, jObjects);
				
				ListView listView = (ListView) findViewById(R.id.listView1);
				listView.setAdapter(adapter);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
