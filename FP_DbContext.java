/**Class: DbContext
 * Purpose: DbContext class manages the session connection to the database
 * Author: Carlton Branch
 * Date: July 22, 2017
 */
import java.sql.*;

public class DbContext {
	
	//private instance variables
	Connection conn;
	Statement stmt;


	//constructor
	public DbContext() 
	{
		String DbURL = "jdbc:mysql://localhost:3306/Face_Pamphlet?useSSL=false";
		String user = "root";
		String password = "password";
		
		try 
		{
			conn = DriverManager.getConnection(DbURL, user, password);
			stmt = conn.createStatement();
			
		}

		catch (SQLException e)
		{
			System.out.println("An error occured while opening the connection: "+ e.getMessage());
		}
	}
	
	Connection getConnection() {return conn;}
	Statement getStatement()   {return stmt;}
	
	
	/*
	 * @brief closes any open session objects
	 * @param none
	 * @return none
	 */
	public void close()
	{
		try 
		{
			stmt.close();
			conn.close();
		}
		catch (SQLException e)
		{
			System.out.println("An error occured while closing the connection: "+ e.getMessage());
		}
	}
	
}//End of DbContext class