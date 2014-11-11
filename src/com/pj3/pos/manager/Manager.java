package com.pj3.pos.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pj3.pos.MainActivity;
import com.pj3.pos.R;
import com.pj3.pos.res_public.Employee;

import android.media.Image;
import android.os.Bundle;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

public class Manager extends Activity implements OnClickListener{
	TabHost tabHost;
	List<Employee> employees;
	Spinner spPosition;
	GridLayout gridEmployees;
	PopupWindow popupWindow;
	Map<LinearLayout, Employee> itemEmployee;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_manager);
		employees = new ArrayList<Employee>();
		itemEmployee = new HashMap<LinearLayout, Employee>();
		loadTab();
		loadSpPosition();
		loadGridEmployees(loadEmploy());
	}
	
	public List<Employee> loadEmploy(){
		List<Employee> employeess = new ArrayList<Employee>();
		Employee e1 = new Employee("Tran van a", "a@gmail.com", "12345", "a.jpg", 13467, 1);
		Employee e2 = new Employee("Tran van b", "a@gmail.com", "874356", "a.jpg", 13467, 2);
		Employee e3 = new Employee("Tran van b", "a@gmail.com", "34567", "a.jpg", 13467, 3);
		employeess.add(e1);
		employeess.add(e3);
		employeess.add(e2);
		return employeess;
		
	}
	
	public void loadEmployee(List<Employee> employees){
//		GridView gridEmployee = (GridView) findViewById(R.id.gridEmployee);
//		for(Employee e:employees){
//			ImageView e_image = new ImageView(gridEmployee.getContext());
//			e_image.setTag(e);
//			gridEmployee.addView(e_image);
//		}
		
	}
	
	public void loadSpPosition(){
		String[] stringPostions = {"Quản lí","Bồi bàn","Nhà bếp"};
		ArrayAdapter<String> adapterPostion = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, stringPostions);
		spPosition = (Spinner) findViewById(R.id.spPosition);
		spPosition.setAdapter(adapterPostion);
		
	}
	
	public void loadGridEmployees(List<Employee> employees){
		gridEmployees = (GridLayout) findViewById(R.id.gridEmployee);
		gridEmployees.setPadding(10, 10, 10, 10);
		int numberEmployee_current = employees.size();
		for(int i = 0; i < numberEmployee_current; i++){
			LinearLayout item = new LinearLayout(Manager.this);
			item.setOrientation(LinearLayout.VERTICAL);
			item.setPadding(50, 50, 50, 50);
			
			item.setWeightSum(3);
			
			itemEmployee.put(item, employees.get(i));
			registerForContextMenu(item);
			
			ImageView image = new ImageView(Manager.this);
			image.setBackgroundResource(R.drawable.man_brown);
			
			TextView name = new TextView(Manager.this);
			name.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			name.setText(employees.get(i).getE_name());
			
			TextView phoneNumber = new TextView(Manager.this);
			phoneNumber.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			phoneNumber.setText(""+employees.get(i).getE_phone_number());
			
			item.addView(image);
			item.addView(name);
			item.addView(phoneNumber);
			
			gridEmployees.addView(item);
		}
		
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		super.onCreateContextMenu(menu, v, menuInfo);
		getMenuInflater().inflate(R.menu.manager_employee_context_button, menu);
	}
	
	

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		final Dialog dialogView = new Dialog(Manager.this);
		dialogView.setContentView(R.layout.e_info_action);
		dialogView.setTitle("Xem nhân viên");
		
		final Dialog dialogEdit = new Dialog(Manager.this);
		dialogEdit.setContentView(R.layout.e_info_action);
		dialogEdit.setTitle("Chỉnh sửa nhân viên");
		
		switch(item.getItemId()){
			case R.id.m_e_view:
//				Intent i = new Intent(Manager.this, DialogInfoEmployee.class);
//				i.putExtra(name, value)
				dialogView.show();
				break;
			case R.id.m_e_edit:
				Button cancelButton = (Button) dialogEdit.findViewById(R.id.m_e_cancel_button);
				cancelButton.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						dialogEdit.dismiss();
						
					}
				});
				
				dialogEdit.show();
				break;
			case R.id.m_e_delete:
				break;
		}
		
		return super.onContextItemSelected(item);	
	}


	public void loadTab(){
		tabHost = (TabHost) findViewById(android.R.id.tabhost);
		tabHost.setup();
		
		TabSpec spec;
		
		spec = tabHost.newTabSpec("tab1");
		spec.setIndicator("Thanh Toán");
		spec.setContent(R.id.payment);
		tabHost.addTab(spec);
		
		spec = tabHost.newTabSpec("tab2");
		spec.setIndicator("Menu");
		spec.setContent(R.id.menu);
		tabHost.addTab(spec);
		
		spec = tabHost.newTabSpec("tab3");
		spec.setIndicator("Nhân viên");
		spec.setContent(R.id.employee);
		tabHost.addTab(spec);
		
		spec = tabHost.newTabSpec("tab4");
		spec.setIndicator("Thống kê");
		spec.setContent(R.id.statistic);
		tabHost.addTab(spec);
		
		tabHost.setCurrentTab(0);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.manager, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		}
		
	}

}
