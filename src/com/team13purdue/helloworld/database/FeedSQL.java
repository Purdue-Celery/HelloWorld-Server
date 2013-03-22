package com.team13purdue.helloworld.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class FeedSQL {
	private Connection connection = null;

	public FeedSQL(Connection connection) {
		this.connection=connection;
		if(this.connection.equals(null)){
			System.out.println("Connection is not ready when creating class User");
		}
	}
	
	public String getFeed(int feedID){
		String query1 = "SELECT * FROM chen869.feed WHERE feed_id=";
		String query = query1+feedID;
		System.out.println(query);
		try{
			Statement st = connection.createStatement();
			ResultSet rs = st.executeQuery(query);
			while(rs.next()){

			}
		}
		catch(SQLException e){
			System.out.println("getFeed has gone wrong");
			return null;
		}
		return null;
	}



}