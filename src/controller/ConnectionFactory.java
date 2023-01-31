package controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;



public class ConnectionFactory {

	/*
	 * Local Server 
	 **/
	

  
//public static final String URL = "jdbc:mysql://localhost:3306/emm_chanakya_staging";
//public static final String USER = "root";
//public static final String PASS = "root2019";

	/*
	 * Production Server 
	 **/

//public static final String URL = "jdbc:mysql://localhost:3306/edushielddb";
//public static final String USER = "root";
//public static final String PASS = "Jr2QM25g13";

	/*
	 * Staging Server 
	 **/
		
public static final String URL = "jdbc:mysql://localhost:3306/edushielddb_stagingtesting";
public static final String USER = "root";
public static final String PASS = "Jr2QM25g13";


/*
 * Production Server - 2.0
 **/

/*public static final String URL = "jdbc:mysql://localhost:3306/edushielddb_EduDay2.0";
public static final String USER = "root";
public static final String PASS = "Jr2QM25g13";*/

/*
 * Production Server - 3.0
 **/

//public static final String URL = "jdbc:mysql://localhost:3306/edushielddb_EduDay3.0";
//public static final String USER = "root";
//public static final String PASS = "Jr2QM25g13";


public static Connection getConnection() {
    try {
       Class.forName("com.mysql.jdbc.Driver");
       Connection con = DriverManager.getConnection(URL, USER, PASS);
       return con;
    } catch (SQLException | ClassNotFoundException ex) {
        throw new RuntimeException("Error connecting to the database", ex);
    }
}
}
