package com.example.gaterestapi;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GaterestapiApplication {

	public static void main(String[] args) {
                executeQueryforUpdate("create table if not exists ontology (id INTEGER PRIMARY KEY AUTOINCREMENT, id_file string, url_file string,name string, create_at DATETIME DEFAULT (strftime('%Y-%m-%dT%H:%M:%fZ', 'now')));");
		SpringApplication.run(GaterestapiApplication.class, args);
                
	}
         public static Connection connectDB() {
	        try {
	            Connection con = DriverManager.getConnection("jdbc:sqlite:baseontology.db");
	            return con;

	        } catch (SQLException e) {
	            e.printStackTrace();
	            return null;
	        }
	    }


	    public static ResultSet executeQueryforRS(String statement){
	        Connection connection;
	        Statement st;
	        ResultSet rs = null;
	        try {
	            connection = GaterestapiApplication.connectDB();
	            st = connection.createStatement();
	            rs = st.executeQuery(statement);

	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        return rs;

	    }

	    public static void executeQueryforUpdate(String statement){
	        Connection connection;
	        Statement st;
	        try {
	            connection = GaterestapiApplication.connectDB();
	            st = connection.createStatement();
	            st.executeUpdate(statement);

	        } catch (SQLException e) {
	            e.printStackTrace();
	        }


	    }

}
