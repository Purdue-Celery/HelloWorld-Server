package com.team13purdue.helloworld.database;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseServer {

	// set up parameters:
	private static final String url = "jdbc:mysql://mydb.ics.purdue.edu:3306/chen869";
	// private final String url = "jdbc:mysql://localhost:3306/test";
	private static final String host = "chen869";
	private final String pw = "cs252";

	private Connection connection = null;

	public DatabaseServer() {
		createConnection();
	}

	public void createConnection() {
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
			e.printStackTrace();
			return;
		}
	}

	public Connection getConnection() {
		return this.connection;
	}

	public void closeConnection() {
		try {
			this.connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public boolean insertUser(String username, String password) {
		String query = "INSERT INTO chen869.user (username, password) VALUES ('"
				+ username + "','" + pw + "')";
		try {
			Statement st = connection.createStatement();
			st.executeUpdate(query);
			return true;
		} catch (SQLException e) {
			System.out.println("InsertUser has gone wrong");
			return false;
		}
		// System.out.println(query);

	}

	public boolean verifyLogin(String username, String password) {
		String query = "SELECT password FROM chen869.user WHERE username='"
				+ username + "'";
		// System.out.println(query);
		String pw = null;
		try {
			Statement st = connection.createStatement();
			ResultSet rs = st.executeQuery(query);
			if (rs == null) {
				System.out.println("rs is null");
				return false;
			} else {
				while (rs.next()) {
					pw = rs.getString("password");
				}
				if (pw == null) {
					return false;
				} else if (pw.equals(password)) {
					return true;
				} else
					return false;
			}

		} catch (SQLException e) {
			System.out.println("Something is wrong");
			return false;
		}
	}

	public void addFeed(String username, String content, Date date,
			double latitude, double longitude, int likes, int dislikes) {
		// addFeed("aaa", "heng", Date.valueOf("2013-04-02"), 0,0,0,0)
		String query = "INSERT INTO chen869.feed (username, content, date, latitude, longitude, likes, dislikes) VALUES ('"
				+ username
				+ "', '"
				+ content
				+ "', '"
				+ date
				+ "', "
				+ latitude
				+ ", "
				+ longitude
				+ ", "
				+ likes
				+ ", "
				+ dislikes
				+ ")";
		System.out.println(query);
		try {
			Statement st = connection.createStatement();
			st.executeUpdate(query);
			st.close();
		} catch (SQLException e) {
			System.out.println("Something is wrong");
			e.printStackTrace();
		}
	}

	public void addReply(int feed_id, String username, Date date) {
		String query = "INSERT INTO chen869.reply (feed_id, username, date) VALUES ("
				+ feed_id + ", '" + username + "', '" + date + "' )";
		System.out.println(query);
		try {
			Statement st = connection.createStatement();
			st.executeUpdate(query);
			st.close();
		} catch (SQLException e) {
			System.out.println("Something is wrong");
			e.printStackTrace();
		}
	}

	public String getFeed(int feedID) {
		String query1 = "SELECT * FROM chen869.feed WHERE feed_id=";
		String query = query1 + feedID;
		System.out.println(query);
		try {
			Statement st = connection.createStatement();
			ResultSet rs = st.executeQuery(query);
			while (rs.next()) {

			}
		} catch (SQLException e) {
			System.out.println("getFeed has gone wrong");
			return null;
		}
		return null;
	}
}
