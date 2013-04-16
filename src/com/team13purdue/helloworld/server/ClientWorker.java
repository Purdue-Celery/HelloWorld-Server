package com.team13purdue.helloworld.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.Date;
import java.util.Scanner;

import org.json.JSONException;
import org.json.JSONObject;

import com.team13purdue.helloworld.database.DatabaseServer;

class ClientWorker implements Runnable {
	private Socket client;
	private DatabaseServer myDBServer;
	Scanner in = null;
	PrintWriter out = null;

	ClientWorker(Socket client, DatabaseServer myDBServer) {
		this.client = client;
		this.myDBServer = myDBServer;
	}

	public void run() {
		System.out.println("new client");
		String line;

		try {
			try {
				InputStream inStream = client.getInputStream();
				OutputStream outStream = client.getOutputStream();
				in = new Scanner(inStream);
				out = new PrintWriter(outStream, true);

				// out.println("Hello!");

				boolean done = false;
				while (!done && in.hasNextLine()) {
					line = in.nextLine();
					// out.println("Echo:" + line);
					System.out.println(line);
					processString(line);
					// out.println("@end");
					if (line.trim().equals("@done"))
						done = true;
				}
			} finally {
				client.close();
			}
		} catch (IOException e) {
			System.out.println("in or out failed");
			System.exit(-1);
		}

	}

	public void processString(String str) {
		if (str.startsWith("@register")) {
			String[] register = str.split(" ");

			if (register.length == 3) {
				String username = register[1];
				String password = register[2];
				myDBServer.insertUser(username, password);
				out.println("success");
			}
		} else if (str.startsWith("@login")) {
			String[] login = str.split(" ");

			if (login.length == 3) {
				String username = login[1];
				String password = login[2];
				if (myDBServer.verifyLogin(username, password))
					out.println("success");
				else
					out.println("fail");
			}
		} else if (str.startsWith("@update_current_feeds")) {
			out.println("");
			// TODO

		} else if (str.startsWith("@add_feed")) {
			str = str.replaceFirst("@add_feed ", "");
			// System.out.println("str: " + str);
			JSONObject obj = null;
			try {
				obj = new JSONObject(str);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			int feed_id = 0;
			try {
				String username = obj.getString("username");
				String content = obj.getString("content");
				double latitude = obj.getDouble("latitude");
				double longitude = obj.getDouble("longitude");
				int likes = obj.getInt("likes");
				int dislikes = obj.getInt("dislikes");
				Date date = Date.valueOf((String) obj.get("date"));

				feed_id = myDBServer.addFeed(username, content, date, latitude,
						longitude, likes, dislikes);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			out.println(feed_id);
		} else if (str.startsWith("@get_feed")) {
			out.println("getfeed request receieved");

		} else if (str.startsWith("@add_reply")) {
			// out.println("addreply request receieved");
			str = str.replaceFirst("@add_reply ", "");
			System.out.println("str: " + str);
			JSONObject obj = null;
			try {
				obj = new JSONObject(str);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				out.println("fail");
				return;
			}
			try {
				int feed_id = obj.getInt("feedID");
				String username = obj.getString("username");
				String content = obj.getString("content");
				Date date = Date.valueOf((String) obj.get("date"));
				if (myDBServer.addReply(feed_id, username, content, date))
					out.println("success");
				else
					out.println("fail");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				out.println("fail");
				return;
			}

		} else if (str.startsWith("@get_reply_list")) {
			String[] command = str.split(" ");
			if (command.length == 2) {
				int feed_id = Integer.valueOf(command[1]);

				String list = myDBServer.getReplyList(feed_id);
				out.println(list);
			} else
				out.println("@error request not recognized");
		} else if (str.startsWith("@increment_like")) {
			String[] command = str.split(" ");
			if (command.length == 2) {
				int feed_id = Integer.valueOf(command[1]);
				if (myDBServer.incrementLike(feed_id))
					out.println("success");
				else
					out.println("fail");
			} else
				out.println("@error request not recognized");

		} else if (str.startsWith("@increment_dislike")) {
			String[] command = str.split(" ");
			if (command.length == 2) {
				int feed_id = Integer.valueOf(command[1]);
				if (myDBServer.incrementDislike(feed_id))
					out.println("success");
				else
					out.println("fail");
			} else
				out.println("@error request not recognized");

		} else if (str.startsWith("@")) {
			out.println("");

		} else if (str.startsWith("@")) {
			out.println("");

		} else if (str.startsWith("@")) {
			out.println("");

		} else if (str.equals("@done")) {
			out.println("closed");
		} else {
			out.println("@error request not recognized");
		}
	}

}