package com.example.expandablelistcheckbox;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;

public class MainActivity extends FragmentActivity {
	
	// reference to the fragment
	ExpListFragment explistfrag;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// Check that the activity is using the layout version with
        // the fragment_container FrameLayout
        if (findViewById(R.id.fragment_container) != null) {

	    // Create a new Fragment to be placed in the activity layout
        	explistfrag = new ExpListFragment();
	        
	        // In case this activity was started with special instructions from an
	        // Intent, pass the Intent's extras to the fragment as arguments
        	explistfrag.setArguments(getIntent().getExtras());
	        
	        // Add the fragment to the 'fragment_container' FrameLayout
	        getSupportFragmentManager().beginTransaction()
	                .add(R.id.fragment_container, explistfrag).commit();
	        
        }
        
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
