package com.pj3.pos.manager;

import com.pj3.pos.R;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class Manager extends Activity {
	TabHost tabHost;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_manager);
		loadTab();
	}
	
	public void loadTab(){
		tabHost = (TabHost) findViewById(android.R.id.tabhost);
		tabHost.setup();
		
		TabSpec spec;
		
		spec = tabHost.newTabSpec("tab1");
//		paymentTab = tabHost.newTabSpec("Thanh toán");
		spec.setContent(R.id.payment);
		tabHost.addTab(spec);
		
//		menuTab = tabHost.newTabSpec("Menu");
//		employeeTab = tabHost.newTabSpec("Nhân viên");
//		statisticTab = tabHost.newTabSpec("Thống kê");
//		
//		spec.setContent(R.id.payment);
//		menuTab.setContent(R.id.menu);
//		employeeTab.setContent(R.id.employee);
//		statisticTab.setContent(R.id.statistic);
		
//		tabHost.addTab(paymentTab);
//		tabHost.addTab(menuTab);
//		tabHost.addTab(employeeTab);
//		tabHost.addTab(statisticTab);
//		
		tabHost.setCurrentTab(0);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.manager, menu);
		return true;
	}

}
