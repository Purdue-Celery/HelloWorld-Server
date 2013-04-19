package com.team13purdue.helloworld.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.team13purdue.helloworld.database.DatabaseServer;

public class Server {
	
	private static final int PORT_NUMBER = 1253;
	private static final int THREADPOOL_SIZE = 25;
	
	private ExecutorService mExecutor;
	
	private ServerSocket server;
	private DatabaseServer myDBServer;
	
	public Server() {
		myDBServer = new DatabaseServer();
		mExecutor = Executors.newFixedThreadPool(THREADPOOL_SIZE);
		
		listenSocket();
	}

	public void listenSocket() {
		try {
			server = new ServerSocket(PORT_NUMBER);
			System.out.println("IP:" + InetAddress.getLocalHost().getHostAddress());
			System.out.println("PORT:" + server.getLocalPort());
		} catch (IOException e) {
			System.err.println("Could not listen on port 4444");
			System.exit(-1);
		}
		
		while (true) {
			ClientWorker w;
			try {
				// server.accept returns a client connection
				w = new ClientWorker(server.accept(), myDBServer);
				// Thread t = new Thread(w);
				// t.start();
				mExecutor.submit(w);
			} catch (IOException e) {
				System.err.println("Accept failed: " + PORT_NUMBER);
				// System.exit(-1);
			}
		}
	}

	@Override
	protected void finalize() {
		// Objects created in run method are finalized when
		// program terminates and thread exits
		try {
			server.close();
		} catch (IOException e) {
			System.err.println("Could not close socket");
			System.exit(-1);
		}
	}
	
}