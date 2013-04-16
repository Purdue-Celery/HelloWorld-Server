package com.team13purdue.helloworld.model;

import java.sql.Date;

import org.json.*;

public class Reply {
	public int reply_id;
	public String username;
	public String content;
	public Date date;

	// public Location loc;

	public Reply(int reply_id, String username, String content, Date date) {
		this.reply_id = reply_id;
		this.username = username;
		this.content = content;
		this.date = date;
	}

	public String toString() {
		JSONObject obj = new JSONObject();

		try {
			obj.put("ID", this.reply_id);
			obj.put("username", this.username);
			obj.put("content", this.content);
			obj.put("date", this.date.toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return obj.toString();
	}

}
