package com.team13purdue.helloworld.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;

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
					out.println("@end");
					if (line.trim().equals("BYE"))
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
				out.println("@register success");
			}
		} else if (str.startsWith("@login")) {
			String[] login = str.split(" ");

			if (login.length == 3) {
				String username = login[1];
				String password = login[2];
				if (myDBServer.getPassword(username) != null
						&& myDBServer.getPassword(username).equals(password))
					out.println("@login valid");
				else
					out.println("@login invalid");
			}
		} else if (str.equals("BYE")) {
			out.println("closed");
		} else {
			out.println("@error request not recognized");
		}
	}

}