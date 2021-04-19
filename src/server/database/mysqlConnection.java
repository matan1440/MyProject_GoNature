package server.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * This Class Making a Connection to the db , the class contains methods for select , insert and update queries .
 * @author Omer
 *
 */
public class mysqlConnection {

	/**
	 * Connection var
	 */
	private Connection conn;

	/**
	 * The method connects to DB and return the Connection
	 * @return Connection to the db
	 */
	public Connection connectToDB() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
			System.out.println("Driver definition succeed");
		} catch (Exception ex) {
			/* handle the error */
			System.out.println("Driver definition failed");
		}

		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost/gonature?serverTimezone=IST", "root",
					"Matan7651871");
			System.out.println("SQL connection succeed");

		} catch (SQLException ex) {
			/* handle any errors */
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
		return conn;
	}
	
	/**
	 * the method get query and return ResultSet of the query 
	 * @param query - which query to be performed
	 * @return ResultSet - the query result
	 */
	public ResultSet runQuery(String query) {
		Statement stmt;
		ResultSet rs = null;
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;
	}
	/**
	 * the method get query and make update/insert query in the db.
	 * @param query - which query to be performed
	 */
	public void runUpdateQuery(String query)
	{
		Statement stmt;
		try 
		{
			stmt = conn.createStatement();
			stmt.executeUpdate(query);
		} 
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
}
