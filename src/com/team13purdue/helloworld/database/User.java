package com.team13purdue.helloworld.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class User {
	private Connection connection = null;

	public User(Connection connection) {
		this.connection=connection;
		if(this.connection.equals(null)){
			System.out.println("Connection is not ready when creating class User");
		}
	}
	
	public String getPW(String username){
		String query1 = "SELECT password FROM chen869.user WHERE username=";
		String query = query1+"\'"+username+"\'";
		System.out.println(query);
		String pw = null;
		try{
			Statement st = connection.createStatement();
			ResultSet rs = st.executeQuery(query);
			while(rs.next()){
				pw = rs.getString("password");
			}
		}
		catch(SQLException e){
			System.out.println("getPW has gone wrong");
			return null;
		}
		return pw;
	}

	
	public void insertUser(String username,String pw){
	
		
		String query = "INSERT INTO chen869.user (username, password) VALUES (\'"+username+"\',\'"+pw+"\')";
		try{
			Statement st = connection.createStatement();
			st.executeUpdate(query);
		}catch(SQLException e){
			System.out.println("InsertUser has gone wrong");
		}
			
		System.out.println(query);
		return;
	}
	
	public void deleteUser(String username){
		String query = "DELETE FROM chen869.user WHERE username = '"+username+"'";
		try{
			Statement st = connection.createStatement();
			st.executeUpdate(query);
		}catch(SQLException e){
			System.out.println("User deletion has gone wrong");
		}
			
	
		System.out.println(query);
		return;
	}

}
