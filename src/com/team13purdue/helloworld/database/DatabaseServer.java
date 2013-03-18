package com.team13purdue.helloworld.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

public class DatabaseServer {
	 
	// set up parameters:
	private final String url = "jdbc:mysql://mydb.ics.purdue.edu:3306/chen869";
	//private final String url = "jdbc:mysql://localhost:3306/test";
	private final String host = "chen869";
	private final String pw = "cs252";

	private Connection connection = null;
	private User user;
	private Book book;
	private Flashcard flashcard;


	public DatabaseServer() {
		// test to see if the driver is correctly found
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			System.out.println("Where is your MySQL JDBC Driver?");
			e.printStackTrace();
			return;
		}

		try {
			connection = DriverManager.getConnection(url, host, pw);
		} catch (SQLException e) {
			System.out.println("Connection Failed! Check output console");
			e.printStackTrace();
			return;
		}

		user = new User(connection);
		book = new Book(connection);
		flashcard = new Flashcard(connection);

	}

	public Connection getConnection() {
		return connection;
	}

	public boolean insertUser(String username, String password) {
		user.insertUser(username, password);
		return true;
	}

	public String getPassword(String username) {
		return user.getPW(username);
	}


}
