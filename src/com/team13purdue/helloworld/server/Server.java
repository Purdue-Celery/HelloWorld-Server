package com.team13purdue.helloworld.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;

import com.team13purdue.helloworld.database.DatabaseServer;

public class Server {
	ServerSocket server;
	static final int PORT_NUMBER = 1253;

	DatabaseServer myDBServer;
	
	public Server() {
		myDBServer = new DatabaseServer();
		this.listenSocket();
	}

	public void listenSocket() {
		try {
			server = new ServerSocket(PORT_NUMBER);
			System.out.println("IP:" + InetAddress.getLocalHost().getHostAddress());
			System.out.println("PORT:" + server.getLocalPort());
		} catch (IOException e) {
			System.out.println("Could not listen on port 4444");
			System.exit(-1);
		}
		while (true) {
			ClientWorker w;
			try {
				// server.accept returns a client connection
				w = new ClientWorker(server.accept(),myDBServer);
				Thread t = new Thread(w);
				t.start();
				
			} catch (IOException e) {
				System.out.println("Accept failed: " + PORT_NUMBER);
				System.exit(-1);
			}
		}
	}

	protected void finalize() {
		// Objects created in run method are finalized when
		// program terminates and thread exits
		try {
			server.close();
		} catch (IOException e) {
			System.out.println("Could not close socket");
			System.exit(-1);
		}
	}
}
