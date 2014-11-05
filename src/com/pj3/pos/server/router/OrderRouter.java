package com.pj3.pos.server.router;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;
import org.restlet.data.Form;
import org.restlet.representation.Representation;
import org.restlet.resource.*;
import org.restlet.ext.json.JsonRepresentation;
import android.content.Context;

import com.pj3.*;
import com.pj3.pos.res_public.*;
import com.pj3.pos.server.sqlite.*;

import com.pj3.pos.server.sqlite.DatabaseSource;

public class OrderRouter extends ServerResource {
	DatabaseSource db = new DatabaseSource(null);
	
	@Get
	public Representation doGet (Representation entity) {
		String tmp = getReference().getRemainingPart();
		String[] parts = tmp.split("/");
		String uidString = parts[1];
		Order ret = db.getBillTemp(Integer.parseInt(uidString));
		JSONObject jo1 = new JSONObject();
		JSONObject jo2 = new JSONObject();
		try {
			
			jo1.put("orderid", ret.getOrderId());
			jo1.put("tableid", ret.getTableId());
			JSONArray foodArray = new JSONArray();
			for (FoodTemprary t : ret.getFoodTemp()){
				jo2.put("fid", t.getFoodId());
				jo2.put("count", t.getCount());
				jo2.put("note", t.getNote());
				foodArray.put(jo2.toString());
			}
			jo1.put("foodarray", jo2);
		} catch(Exception e){
			e.printStackTrace();
			return new JsonRepresentation("{\"message\":\"error\"}");
		}
		return new JsonRepresentation(jo1);
		
	}
	
	
	@Post("json")
	public Representation doPost (Representation entity){
		try {
			JsonRepresentation  jsonRep  = new JsonRepresentation(entity);
			JSONObject 			jsonObj  = jsonRep.getJsonObject();
			String tableId = jsonObj.getString("tableid");
			JSONArray foodArray = jsonObj.getJSONArray("foodarray");
			
			Order order = new Order();
			order.setTableId(Integer.parseInt(tableId));
			
			for (int i = 0 ; i < foodArray.length(); i ++){
				FoodTemprary x = new FoodTemprary();
				String fid = foodArray.getJSONObject(i).getString("fid");
				x.setFoodId(Integer.parseInt(fid));
				String count = foodArray.getJSONObject(i).getString("count");
				x.setCount(Integer.parseInt(count));
				String note = foodArray.getJSONObject(i).getString("note");
				x.setNote(note);			
			}
			db.createBillTemp(order);
		}
		catch(Exception e){
			e.printStackTrace();
			return new JsonRepresentation("{\"message\":\"error\"}");
		}
		return new JsonRepresentation("{\"message\":\"done\"}");
	}
	
	
	
	@Put("json")
	public Representation doPut (Representation entity){
		String tmp = getReference().getRemainingPart();
		String[] parts = tmp.split("/");
		String uidString = parts[1];
		Order ret1 = db.getBillTemp(Integer.parseInt(uidString));
		try {
			JsonRepresentation  jsonRep  = new JsonRepresentation(entity);
			JSONObject 			jsonObj  = jsonRep.getJsonObject();
			JSONArray			ja	     = jsonObj.getJSONArray("foodarray");
			for( int i =0; i < ja.length(); i ++){
				JSONObject jo1 = ja.getJSONObject(i);
				FoodTemprary ft = new FoodTemprary();
				ft.setFoodId(Integer.parseInt(jo1.getString("fid")));
				ft.setCount(Integer.parseInt(jo1.getString("count")));
				ft.setNote(jo1.getString("note"));
				List<FoodTemprary> listFoodTemp = new ArrayList(ret1.getFoodTemp());
				listFoodTemp.add(ft);
				ret1.setFoodTemp(listFoodTemp);
				
			}
			
			db.updateBillTemp(ret1);
			return new JsonRepresentation("{\"message\":\"done\"}");
		}
		catch (IOException e1 ){
			e1.printStackTrace();
			return new JsonRepresentation("\"message\": \"not found\"}");
		}
		catch(JSONException e2){
			e2.printStackTrace();
		}
		return null;
	}
	@Delete
	public Representation doDelete(Representation entity){
		return null;
	}
}
//1f62bd5222c034466d58121e6e089e55