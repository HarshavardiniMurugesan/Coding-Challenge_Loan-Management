package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class LoanUtil {
	
	static Connection com;
	public static Connection getDBConn() throws SQLException {
		
		com=DriverManager.getConnection("jdbc:mysql://localhost:3306/loan","root","root");
		return com;
	}
public static void main(String[]args) throws SQLException {
	System.out.println(getDBConn());
}


}
