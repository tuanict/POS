package com.pj3.pos.manager;

import com.pj3.pos.R;
import com.pj3.pos.R.layout;
import com.pj3.pos.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class DialogInfoEmployee extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.e_info_action);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.dialog_info_employee, menu);
		return true;
	}

}
