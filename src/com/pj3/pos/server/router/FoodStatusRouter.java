package com.pj3.pos.server.router;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.data.Form;
import org.restlet.representation.Representation;
import org.restlet.resource.*;
import org.restlet.ext.json.JsonRepresentation;
import android.content.Context;
import java.util.*;
import com.pj3.*;
import com.pj3.pos.res_public.*;
import com.pj3.pos.server.sqlite.*;

public class FoodStatusRouter extends ServerResource {
	DatabaseSource db = new DatabaseSource(null);
	
	@Get
	public Representation doGet (Representation entity){
		String tmp = getReference().getRemainingPart();
		String[] parts = tmp.split("/");
		String uidString = parts[1];
		if(uidString == null) return null;
		JSONObject jo1 = new JSONObject();
		JSONArray ja1 = new JSONArray();
		
		
		List<Order> orderList = db.getOrderList();
		try{
	
			for( Order z : orderList){
				List<FoodTemprary> t = z.getFoodTemp();
				for (FoodTemprary q: t){
					
					if((q.getStatus() == FoodTemprary.FOOD_STATUS_WAIT && uidString.equalsIgnoreCase("cook")) ||
						(q.getStatus() == FoodTemprary.FOOD_STATUS_WAIT && uidString.equalsIgnoreCase("waiter"))	){
						JSONObject jo2 = new JSONObject();
						jo2.put("fid", Integer.toString(q.getFoodId()));
						jo2.put("fcount", Integer.toString(q.getCount()));
						jo2.put("note", q.getNote());						
						ja1.put(jo2);
					}
					
				}
			} 
			jo1.put("array", ja1);
		}catch(JSONException e){
				e.printStackTrace();
				return new JsonRepresentation("{\"message\":\"error\"}");
		}
		return new JsonRepresentation(jo1);
			
		
	}
	
	
	
	@Post
	public Representation doPost (Representation entity){
		int orderId=0;
		int fid = 0;
		int status =0;
		try{
			JsonRepresentation  jsonRep  = new JsonRepresentation(entity);
			JSONObject 			jsonObj  = jsonRep.getJsonObject();
			orderId = Integer.parseInt(jsonObj.getString("orderid"));
			fid = Integer.parseInt(jsonObj.getString("fid"));
			status = Integer.parseInt(jsonObj.getString("status"));
			Order order = db.getBillTemp(orderId);
			List<FoodTemprary> foodList = order.getFoodTemp();
			for( FoodTemprary t : foodList){
				if(t.getFoodId() == fid){
					t.setStatus(status);
				}
			}
			order.setFoodTemp(foodList);
			db.updateBillTemp(order);
			return new JsonRepresentation("{\"message\":\"done\"}");
		} catch(Exception e){
			e.printStackTrace();
			return new JsonRepresentation("{\"message\":\"error\"}");
		}
		
	}
	
}
//1f62bd5222c034466d58121e6e089e55