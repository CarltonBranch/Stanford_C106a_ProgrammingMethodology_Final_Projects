/*
 * MsgController
 * Purpose : Send user input as commands to the database and return database state to the view model
 * Author: Carlton L. Branch
 * Date: July 2017
 */

import java.sql.*;
import java.util.*;

public class MsgController 
{

	//private instance variables
	private DbContext _db;
	public Statement stmt;
	public ResultSet result;
	
	//constructor
	public MsgController() 
	{
		_db = new DbContext();
		stmt = _db.getStatement();
	}
	
	
	public String getCurrentDateTime() {
		return java.time.LocalDateTime.now().toString();
	}
	
	
	
	public ArrayList<MessageViewModel> getAllStatusMessages(String selectProfileID)
	{
			
		ArrayList<MessageViewModel> messageList = new ArrayList<MessageViewModel>();
		
		try 
		{
			String queryStr = "SELECT StatusMsg, MessagePostDate"
							+ " FROM statusmessage"
							+ " WHERE ProfileId_FK = ?";
			
			PreparedStatement prepStmt = _db.getConnection().prepareStatement(queryStr);
			prepStmt.setString(1, selectProfileID);
			
			result = prepStmt.executeQuery();
			while (result.next())
			{
				messageList.add(new MessageViewModel( selectProfileID, result.getString(1), result.getString(2)));
			}
			
		}
		catch (SQLException e)
		{
			System.out.println(e.getMessage());
		}
		
		return messageList;
	}
	
	
	public MessageViewModel getTopStatusMsg(String selectProfileID)
	{
		MessageViewModel topStatusMVM = new MessageViewModel(selectProfileID, "","");
		try 
		{
			String QueryStr = "SELECT StatusMsg, MessagePostDate"
							+ " FROM statusmessage"
							+ " WHERE ProfileId_FK = ?"
							+ " ORDER BY messagepostdate DESC"
							+ " LIMIT 1";
			
			PreparedStatement prepStmt = _db.getConnection().prepareStatement(QueryStr);
			prepStmt.setString(1, selectProfileID);
			result = prepStmt.executeQuery();
			
			if (result.next())
			{
				topStatusMVM = new MessageViewModel(selectProfileID, result.getString(1),result.getString(2) ) ;
			}
		}
		catch(SQLException e)
		{
			System.out.println(e.getMessage());
		}
		
		return topStatusMVM;
	}
	
	public void addNewStausUpdateMessage(MessageViewModel mvm)
	{	
		try {
			String updateStr = 
					  "INSERT INTO statusmessage "
					+ "( ProfileID_FK , StatusMsg , MessagePostDate ) "
					+ "VALUES ( ?, ? , ?) " 
					; 
			PreparedStatement prepStmt = _db.getConnection().prepareStatement(updateStr);

			prepStmt.setString (1, mvm.getProfileId() );
			prepStmt.setString (2, mvm.getMessage());
			prepStmt.setString (3, mvm.getDate());
			prepStmt.execute();
		}
		catch(SQLException e)
		{
			System.out.println(e.getMessage() );
		}
	}
	
	
	/*
	 * @brief Safely close the connection objects to the database
	 * @param none
	 * @return none
	 */
	public void close() throws SQLException 
	{
		result.close();
		stmt.close();
		_db.close();
	}
}
