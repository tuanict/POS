package com.pj3.pos.waiter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.zip.Inflater;

import android.app.Activity;
import android.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class Tab3Adapter extends ArrayAdapter<HashMap<String, Object>>{

	Context context;
	int resource;
	ArrayList<HashMap<String, Object>> tab3List = new ArrayList<HashMap<String,Object>>();
	
	public Tab3Adapter(Context context, int resource,
			ArrayList<HashMap<String, Object>> tab3List ) {
		super(context, resource, tab3List);
		// TODO Auto-generated constructor stub
		this.context = context;
		this.resource = resource;
		this.tab3List = tab3List;
	}
	public View getView(final int position, View convertView, ViewGroup parent) {	 
		if(convertView==null){
            // inflate the layout

            LayoutInflater inflater = ((Activity)context).getLayoutInflater();

            convertView = inflater.inflate(resource, parent, false);

        }
	   ImageView imgF=(ImageView) convertView.findViewById(com.pj3.pos.R.id.imgF);
	   TextView nameF=(TextView) convertView.findViewById(com.pj3.pos.R.id.nameF);
	   TextView qttyF=(TextView) convertView.findViewById(com.pj3.pos.R.id.qttyF);
	   TextView priceF = (TextView) convertView.findViewById(com.pj3.pos.R.id.priceF);
   
       imgF.setImageResource((Integer) tab3List.get(position).get("imgF"));
       nameF.setText((String) tab3List.get(position).get("nameF"));
       qttyF.setText((String) tab3List.get(position).get("qttyF"));
       priceF.setText((String) tab3List.get(position).get("priceF"));
	  
	   return convertView;
	}
	
}
