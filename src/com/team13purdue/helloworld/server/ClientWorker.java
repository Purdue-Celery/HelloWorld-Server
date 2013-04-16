package com.team13purdue.helloworld.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.Date;
import java.util.Scanner;

import org.json.simple.*;

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
			str.replaceFirst("@add_feed ", "");
			Object obj = JSONValue.parse(str);
			JSONObject obj2 = (JSONObject) obj;
			String username = (String) obj2.get("username");
			String content = (String) obj2.get("content");
			Date date = (Date) obj2.get("date");
			double latitude = (Double) obj2.get("latitude");
			double longitude = (Double) obj2.get("longitude");
			int likes = (Integer) obj2.get("likes");
			int dislikes = (Integer) obj2.get("dislikes");
			System.out.println(username + "\n" + content + "\n" + date + "\n"
					+ latitude + "\n" + longitude + "\n" + likes + "\n"
					+ dislikes);

			int feed_id = 0;
			// feed_id = myDBServer.addFeed(username, content, date, latitude,
			// longitude, likes, dislikes);
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