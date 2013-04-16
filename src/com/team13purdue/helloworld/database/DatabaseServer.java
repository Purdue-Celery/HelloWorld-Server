package com.team13purdue.helloworld.database;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DatabaseServer {

	// set up parameters:
	private static final String url = "jdbc:mysql://mydb.ics.purdue.edu:3306/chen869";
	// private final String url = "jdbc:mysql://localhost:3306/test";
	private static final String host = "chen869";
	private static final String pw = "cs252";

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

	public void printResultTable(String queryString) {
		String query = queryString;
		try {
			Statement st = connection.createStatement();
			ResultSet rs = st.executeQuery(query);
			ResultSetMetaData rsmd = rs.getMetaData();

			int numberOfColumns = rsmd.getColumnCount();
			for (int i = 1; i <= numberOfColumns; i++) {
				if (i > 1)
					System.out.print("\t");
				String columnName = rsmd.getColumnName(i);
				System.out.print(columnName);
			}
			System.out.println();

			while (rs.next()) {
				for (int i = 1; i <= numberOfColumns; i++) {
					if (i > 1)
						System.out.print("\t");
					String columnValue = rs.getString(i);
					System.out.print(columnValue);
				}
				System.out.println();
			}
			st.close();
		} catch (SQLException e) {
			System.out.println("Something is wrong");
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
			System.out.println("Something is wrong");
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

	public int addFeed(String username, String content, Date date,
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

		int feed_id = 0;
		query = "SELECT feed_id FROM chen869.feed WHERE username = '"
				+ username + "' AND content = '" + content + "' AND date = '"
				+ date.toString() + "'";
		System.out.println(query);
		try {
			Statement st = connection.createStatement();
			ResultSet rs = st.executeQuery(query);
			while (rs.next()) {
				feed_id = rs.getInt("feed_id");
			}
			st.close();
		} catch (SQLException e) {
			System.out.println("Something is wrong");
			e.printStackTrace();
		}

		return feed_id;

	}

	public boolean addReply(int feed_id, String username, String content,
			Date date) {
		String query = "INSERT INTO chen869.reply (feed_id, username, content, date) VALUES ("
				+ feed_id
				+ ", '"
				+ username
				+ "', '"
				+ content
				+ "','"
				+ date
				+ "' )";
		System.out.println(query);
		try {
			Statement st = connection.createStatement();
			st.executeUpdate(query);
			st.close();
		} catch (SQLException e) {
			System.out.println("Something is wrong");
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public String getFeed(int feed_id) {
		String query = "SELECT * FROM chen869.feed WHERE feed_id=" + feed_id;
		System.out.println(query);
		try {
			Statement st = connection.createStatement();
			ResultSet rs = st.executeQuery(query);
			while (rs.next()) {
				// feed_id = rs.getInt("feed_id");
				String username = rs.getString("username");
				String content = rs.getString("content");
				Date date = rs.getDate("date");
				double latitude = rs.getDouble("latitude");
				double longitude = rs.getDouble("longitude");
				int likes = rs.getInt("likes");
				int dislikes = rs.getInt("dislikes");
				// Feed feed = new Feed(feed_id, username, content, date,
				// latitude, longitude, likes, dislikes);
				// return feed.toString();

			}
		} catch (SQLException e) {
			System.out.println("Something is wrong");
			return null;
		}
		return null;
	}

	public String getReplyList(int feed_id) {
		JSONArray array = new JSONArray();

		String query = "SELECT * FROM chen869.reply WHERE feed_id = " + feed_id
				+ " ORDER BY date";
		try {
			Statement st = connection.createStatement();
			ResultSet rs = st.executeQuery(query);
			while (rs.next()) {
				int reply_id = rs.getInt("reply_id");
				String username = rs.getString("username");
				String content = rs.getString("content");
				Date date = rs.getDate("date");
				JSONObject obj = new JSONObject();
				try {
					obj.put("ID", reply_id);
					obj.put("feedID", feed_id);
					obj.put("username", username);
					obj.put("content", content);
					obj.put("date", date.toString());

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				array.put(obj);
			}
		} catch (SQLException e) {
			System.out.println("Something is wrong");
			return null;
		}
		return array.toString();
	}

	public boolean incrementLike(int feed_id) {
		String query = "UPDATE chen869.feed SET likes = likes + 1 WHERE feed_id = "
				+ feed_id;
		try {
			Statement st = connection.createStatement();
			st.executeUpdate(query);
			st.close();
		} catch (SQLException e) {
			System.out.println("Something is wrong");
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public boolean incrementDislike(int feed_id) {
		String query = "UPDATE chen869.feed SET dislikes = dislikes + 1 WHERE feed_id = "
				+ feed_id;
		try {
			Statement st = connection.createStatement();
			st.executeUpdate(query);
			st.close();
		} catch (SQLException e) {
			System.out.println("Something is wrong");
			e.printStackTrace();
			return false;
		}
		return true;
	}
}
