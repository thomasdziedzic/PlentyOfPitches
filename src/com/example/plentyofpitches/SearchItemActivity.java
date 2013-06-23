package com.example.plentyofpitches;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.library.JArrayAdapter;

public class SearchItemActivity extends Activity {
	String itemType;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_item);
		
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
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.search_item, menu);
		return true;
	}

	public void search(View view) {
		EditText queryEditText = (EditText) findViewById(R.id.editText1);
		String query = queryEditText.getText().toString();
		
		SearchItem searchItem = new SearchItem(this, query);
		searchItem.execute("http://50.116.4.81:5000/search/" + itemType + "s");
	}
	
	public class SearchItem extends AsyncTask<String, Void, String> {
		Context ctx;
		String postBody;
		
		SearchItem(Context ctx, String query) {
			super();
			
			this.ctx = ctx;
			
			JSONObject postBody = new JSONObject();
			try {
				postBody.put("query", query);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			this.postBody = postBody.toString();
			System.out.println(this.postBody);
		}
		
		protected String doInBackground(String... urls) {
			String url = urls[0];
			
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(url);
			try {
				post.setEntity(new StringEntity(this.postBody));
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			String ret = "";
			
			try {
				HttpResponse response = client.execute(post);
				
				StatusLine statusLine = response.getStatusLine();
				int statusCode = statusLine.getStatusCode();
				
				System.out.println("The status code is: " + statusCode);
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
			
			System.out.println(ret);
			System.out.flush();

			return ret;
		}
		
		protected void onPostExecute(String result) {
			System.out.println(result);
			System.out.flush();
			try {
				JSONArray jArray = new JSONArray(result);
				List<JSONObject> jObjects = new ArrayList<JSONObject>();
				for(Integer i = 0; i < jArray.length(); i++) {
					jObjects.add(jArray.getJSONObject(i));
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
