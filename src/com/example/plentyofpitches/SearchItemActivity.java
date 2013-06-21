package com.example.plentyofpitches;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;

public class SearchItemActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_item);
		
		Intent intent = getIntent();
		String itemType = intent.getStringExtra(MainActivity.ITEM_TYPE_KEY);

		TextView textView = (TextView) findViewById(R.id.textView1);
		textView.setText(itemType);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.search_item, menu);
		return true;
	}

}
