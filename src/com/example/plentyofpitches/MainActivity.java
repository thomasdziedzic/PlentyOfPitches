package com.example.plentyofpitches;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;

public class MainActivity extends Activity implements OnCheckedChangeListener {
	private String itemType = PROBLEM_ITEM;

	public final static String ITEM_TYPE_KEY = "com.example.plentyofpitches.ITEM_TYPE_KEY";
	public final static String PROBLEM_ITEM = "problem";
	public final static String IDEA_ITEM = "idea";
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        Switch problemOrIdea = (Switch) findViewById(R.id.switch1);
        problemOrIdea.setOnCheckedChangeListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    public void newItem(View view) {
    	Intent intent = new Intent(this, NewItemActivity.class);
    	intent.putExtra(ITEM_TYPE_KEY, itemType);
    	startActivity(intent);
    }
    
    public void browseItem(View view) {
    	Intent intent = new Intent(this, BrowseItemActivity.class);
    	intent.putExtra(ITEM_TYPE_KEY, itemType);
    	startActivity(intent);
    }
    
    public void search(View view) {
    	Intent intent = new Intent(this, SearchItemActivity.class);
    	intent.putExtra(ITEM_TYPE_KEY, itemType);
    	startActivity(intent);
    }
    
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
    	itemType = !isChecked ? PROBLEM_ITEM : IDEA_ITEM;
    }
}
