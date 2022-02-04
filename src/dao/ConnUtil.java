package dao;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnUtil {
	
	static {
		try {
			Class.forName("oracle.jdbc.OracleDriver");
			
		}catch(ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public static Connection getConnection()throws SQLException {
		return DriverManager.getConnection(
				"jdbc:oracle:thin:@localhost:1521/XEPDB1",
				"mytest", "mytest");
				
	}

}
