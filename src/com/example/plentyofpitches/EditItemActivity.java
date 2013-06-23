package com.example.plentyofpitches;

import java.io.UnsupportedEncodingException;

import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class EditItemActivity extends Activity {
	private int id;
	private String itemType;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_item);
		
		Intent intent = getIntent();
		itemType = intent.getStringExtra(MainActivity.ITEM_TYPE_KEY);
		id = intent.getIntExtra(NewItemActivity.ITEM_ID_KEY, 0);
		
		TextView textView = (TextView) findViewById(R.id.textView1);
		textView.setText(itemType);
		
		AsyncHttpClient client = new AsyncHttpClient();
		client.get("http://50.116.4.81:5000/" + itemType + "/" + id, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String response) {
				try {
					JSONObject jObject = new JSONObject(response);
					String description = jObject.getString("description");
					
					TextView editText = (TextView) findViewById(R.id.editText1);
					editText.setText(description);
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
		getMenuInflater().inflate(R.menu.edit_item, menu);
		return true;
	}

	public void save(View view) {
		EditText editText = (EditText) findViewById(R.id.editText1);
		String description = editText.getText().toString();
		
		JSONObject postBody = new JSONObject();
		try {
			postBody.put("description", description);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		StringEntity postBodyEntity;
		try {
			postBodyEntity = new StringEntity(postBody.toString());
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return;
		}
		
		AsyncHttpClient client = new AsyncHttpClient();
		client.put(this, "http://50.116.4.81:5000/" + itemType + "/" + id, postBodyEntity, "application/json", new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String response) {
				finish();
			}
		});
	}
}
