package com.pj3.pos.server.router;


import java.io.IOException;

import org.json.JSONObject;
import org.restlet.data.Form;
import org.restlet.representation.Representation;
import org.restlet.resource.*;
import org.restlet.ext.json.JsonRepresentation;
import android.content.Context;

import com.pj3.*;
import com.pj3.pos.res_public.*;
import com.pj3.pos.server.sqlite.*;
public class MenuRouter extends ServerResource {
	DatabaseSource db = new DatabaseSource(null);
	@Get
	public Representation doGet (Representation entity){
		String tmp = getReference().getRemainingPart();
		String[] parts = tmp.split("/");
		String uidString = parts[1];
		
		Food ret  = db.getFood(Integer.parseInt(uidString));
		JSONObject jo = new JSONObject();
		try{	
			jo.put("food_id", ret.getM_food_id());
			jo.put("foodname", ret.getM_name());
			jo.put("price", ret.getM_price());
			jo.put("image", ret.getM_price());
			jo.put("status", ret.getM_status());
		} catch(Exception e){
			e.printStackTrace();
			return new JsonRepresentation("{\"message\":\"error\"}");
		}
		return new JsonRepresentation(jo);
	}
	
	@Post("json")
	public Representation doPost (Representation entity){
		String foodname = "";
		String price  	= "";
		String image	= "";
		String status	= "";
		try{
		JsonRepresentation  jsonRep  = new JsonRepresentation(entity);
		JSONObject 			jsonObj  = jsonRep.getJsonObject();
		foodname  	= jsonObj.getString("foodname");
		price 		= jsonObj.getString("price");
		//image 		= jsonObj.getString("image");
		status  	= jsonObj.getString("status");
		db.createFood(new Food(foodname,Integer.parseInt(price),image,Boolean.parseBoolean(status)));
		} catch(Exception e){
			e.printStackTrace();
			return new JsonRepresentation("{\"message\":\"error\"}");
		}
		return new JsonRepresentation("{\"message\":\"done\"}");
	}
	
	@Put("json")
	public Representation doPut (Representation entity){
		String foodname = "";
		String price  	= "";
		String image	= "";
		String status	= "";
		String id		= "";
		
		String tmp = getReference().getRemainingPart();
		String[] parts = tmp.split("/");
		String uidString = parts[1];
		try{
		JsonRepresentation  jsonRep  = new JsonRepresentation(entity);
		JSONObject 			jsonObj  = jsonRep.getJsonObject();
		foodname  	= jsonObj.getString("foodname");
		price 		= jsonObj.getString("price");
		//image 		= jsonObj.getString("image");
		status  	= jsonObj.getString("status");
		db.updateMenu(new Food(Integer.parseInt(uidString),foodname,Integer.parseInt(price),image,Boolean.parseBoolean(status)));
		} catch(Exception e){
			e.printStackTrace();
			return new JsonRepresentation("{\"message\":\"error\"}");
		}
		return new JsonRepresentation("{\"message\":\"done\"}");
	}
	@Delete
	public Representation doDelete(Representation entity){
		String tmp = getReference().getRemainingPart();
		String[] parts = tmp.split("/");
		String uidString = parts[1];
		
		db.changeStatusFood(Integer.parseInt(uidString), false);
		return new JsonRepresentation("{\"message\":\"done\"}");
	}
}
//1f62bd5222c034466d58121e6e089e55
//ZG9pbmcgc2hpdG91dA==