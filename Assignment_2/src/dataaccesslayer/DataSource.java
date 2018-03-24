package dataaccesslayer;
/*
 * File : DataSource
 * Author : John Ferguson
 * Written : March 5th
 * Description : Purpose is to open a connection to a MySql database
 * and return a reference to that connection object
 * 
 * */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataSource {

	private Connection conn = null;
	private final String connectionString = "jdbc:mysql://localhost/assignment2";
	private final String username = "assignment2";
	private final String password = "password";
	
	
	public Connection getConnection() throws SQLException{
		try{
			if(conn != null){
				System.out.println("Cannot create a new connection, one exists already");
			}else{
				conn = DriverManager.getConnection(connectionString, username, password);
			}
		}catch(SQLException ex){
			System.out.println(ex.getMessage());
			throw ex;
		}
		return conn;		
	}
	
	
}
