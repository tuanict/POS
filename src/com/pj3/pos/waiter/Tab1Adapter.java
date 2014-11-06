package com.pj3.pos.waiter;

import java.util.ArrayList;
import java.util.HashMap;

import com.pj3.pos.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;


public class Tab1Adapter extends BaseExpandableListAdapter{

	Context context;
	ArrayList<HashMap<String, Object>> groupList;
	ArrayList<ArrayList<HashMap<String, Object>>> childList;
	
	public Tab1Adapter(Context context, ArrayList<HashMap<String, Object>> groupList,ArrayList<ArrayList<HashMap<String, Object>>> childList){
		this.context = context;
		this.groupList = groupList;
		this.childList = childList;
	}
	
	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		
		return groupList.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		return childList.get(groupPosition).size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return groupList.get(groupPosition);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return childList.get(groupPosition).get(childPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return childPosition;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View gList = convertView;
		if(gList == null){
			gList = LayoutInflater.from(context).inflate(R.layout.tab1_item_table, parent,false);
		}
		TextView txtTable = (TextView)gList.findViewById(R.id.TextTable);
		//CheckBox chkTable = (CheckBox)gList.findViewById(R.id.checkTab);
		//chkTable.setChecked((Boolean) groupList.get(groupPosition).get("chkTable"));
		txtTable.setText((String) groupList.get(groupPosition).get("txtTable"));
		return gList;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View chList = convertView;
		if(chList == null){
			chList = LayoutInflater.from(context).inflate(R.layout.tab1_sub_list_item_food, parent,false);
		}
		ImageView imgFood = (ImageView)chList.findViewById(R.id.imgFood);
		TextView txtFood = (TextView)chList.findViewById(R.id.textFood);
		TextView txtQuantity = (TextView)chList.findViewById(R.id.textQuantity);
		imgFood.setImageResource((Integer) childList.get(groupPosition).get(childPosition).get("imgFood"));
		txtFood.setText((String) childList.get(groupPosition).get(childPosition).get("txtFood"));
		txtQuantity.setText((String) childList.get(groupPosition).get(childPosition).get("txtQuantity"));
		return chList;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return true;
	}
	

}
