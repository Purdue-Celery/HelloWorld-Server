package com.team13purdue.helloworld.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.Date;
import java.util.Scanner;

import org.json.*;

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
		} else if (str.startsWith("@add_feed")) {
			// out.println("addfeed request receieved");
			System.out.println("****inside");
			str = str.replaceFirst("@add_feed ", "");
			System.out.println("str: " + str);
			// Object obj = JSONValue.parse(str);
			JSONObject obj2 = null;
			try {
				obj2 = new JSONObject(str);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			int feed_id = 0;
			try {
				String username = obj2.getString("username");
				System.out.println("name: " + username);
				String content = obj2.getString("content");
				System.out.println("content: " + content);

				double latitude = obj2.getDouble("latitude");
				System.out.println("latitude: " + latitude);
				double longitude = obj2.getDouble("longitude");
				System.out.println("longitude: " + longitude);
				int likes = obj2.getInt("likes");
				int dislikes = obj2.getInt("dislikes");
				Date date = Date.valueOf((String) obj2.get("date"));
				System.out.println("date: " + date.toString());
				System.out.println(username + "\n" + content + "\n" + date
						+ "\n" + 0 + "\n" + 0 + "\n" + 0 + "\n" + 0);

				feed_id = myDBServer.addFeed(username, content, date, latitude, longitude, likes,
						dislikes);
				System.out.println("feed_id:"+feed_id);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			out.println(feed_id);
		} else if (str.startsWith("@get_feed")) {
			out.println("getfeed request receieved");

		} else if (str.startsWith("@add_reply")) {
			out.println("addreply request receieved");

			// myDBServer.addReply(1, "aaa", Date.valueOf("2013-04-02"));
		} else if (str.startsWith("@get_reply_list")) {

			out.println("");

		} else if (str.startsWith("@")) {
			out.println("");

		} else if (str.startsWith("@")) {
			out.println("");

		} else if (str.startsWith("@")) {
			out.println("");

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