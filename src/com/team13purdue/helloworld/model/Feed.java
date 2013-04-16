package com.team13purdue.helloworld.model;

import java.util.Date;

import org.json.*;

public class Feed {
	public int feed_id;
	public String username;
	public String content;
	public Date date;
	public double latitude;
	public double longitude;
	public int likes;
	public int dislikes;

	// public Location loc;

	public Feed(int feed_id, String username, String content, Date date,
			double latitude, double longitude, int likes, int dislikes) {
		this.feed_id = feed_id;
		this.username = username;
		this.content = content;
		this.date = date;
		this.latitude = latitude;
		this.longitude = longitude;
		this.likes = 0;
		this.dislikes = 0;
	}

	public String toString() {
		JSONObject obj = new JSONObject();

		try {
			obj.put("ID", this.feed_id);
			obj.put("username", this.username);
			obj.put("content", this.content);
			obj.put("date", this.date.toString());
			// TODO
			// obj.put("latitude", this.loc.getLatitude());
			obj.put("latitude", 0.0);
			// TODO
			// obj.put("longitude", this.loc.getLongitude());
			obj.put("longitude", 0.0);
			obj.put("likes", this.likes);
			obj.put("dislikes", this.dislikes);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return obj.toString();
	}

}