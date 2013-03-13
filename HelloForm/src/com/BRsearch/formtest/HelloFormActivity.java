package com.BRsearch.formtest;

import android.app.Activity;
import android.os.Bundle;
import android.widget.*;
import android.view.*;
public class HelloFormActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }
    public void onToggleClicked(View v) {
    	TextView tv=new TextView(this);
    	if (((ToggleButton) v).isChecked()) {
    		setContentView(tv);
            //Toast.makeText(HelloFormActivity.this, "Toggle on", Toast.LENGTH_SHORT).show();
        } else {
            //Toast.makeText(HelloFormActivity.this, "Toggle off", Toast.LENGTH_SHORT).show();
        }
    }
}