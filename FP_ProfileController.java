/*
 * ProfileController
 * Purpose : Send user input as commands to the database and return database state to the view model
 * Author: Carlton L. Branch
 * Date: July 2017
 */

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class ProfileController {

	//private instance variables
	private DbContext _db;
	private ProfileViewModel currentProfile;
	private ProfileViewModel selectedProfile;
	public Statement stmt;
	public ResultSet result;
	
	//constructor
	public ProfileController() 
	{
		_db = new DbContext();
		stmt = _db.getStatement();
	}

	public ProfileViewModel getCurrentProfile () { return currentProfile; }
	public ProfileViewModel getSelectedProfile () { return selectedProfile; }
	public void setCurrentProfile (ProfileViewModel _currentProfile) { currentProfile = _currentProfile; }
	public void setSelectedProfile (ProfileViewModel _selectedProfile) { selectedProfile = _selectedProfile; }
	
	/*
	 * @brief returns a string representing the current system date
	 * @param none
	 * @return String
	 */
	public String getCurrentDate() {
		return java.time.LocalDate.now().toString();
	}
	
	
	public ProfileViewModel getProfileViewModelByProfileID(String targetProfileID)
	{
		
		selectedProfile = new ProfileViewModel();
		String sqlQuery = "SELECT ProfileID , FirstName , LastName , Birthday , Image, DateJoined "
						+ "FROM profile "
						+ "WHERE profileId = '"+ targetProfileID+"'"; 
		try 
		{
			PreparedStatement prepStmt = _db.getConnection().prepareStatement(sqlQuery);
			result = prepStmt.executeQuery();
		
			if (result.next() )
			{
				selectedProfile = new ProfileViewModel();
				selectedProfile.setProfileID(result.getString(1));
				selectedProfile.setFirstName(result.getString(2));
				selectedProfile.setLastName(result.getString(3));
				selectedProfile.setBirthday(result.getString(4));
				selectedProfile.setImageBlob(result.getBlob(5)); 
				selectedProfile.setProfileCreationDate(result.getString(6));
			}
		} 
		catch (SQLException e) 
		{
			System.out.println ( e.getMessage() );
		}
		
		return selectedProfile;
	}
		/*
	 * @brief Inserts a new Profile into the database
	 * @param Profile object
	 * @return Profile created 
	 */
	public ProfileViewModel addNewProfileReturnProfile(ProfileViewModel pvm)
	{
		String newProfileID = "";
		try 
		{
			result = stmt.executeQuery("SELECT MAX( ProfileID ) from Profile" );
			result.next();
			newProfileID = result.getString(1);
			
			//ensure consistent formatting of profileID
			if (newProfileID == null)
			{
				newProfileID = "FPH00000";
			}
			else
			{
				String subNumStr = newProfileID.substring(3);
				int subNumInt = Integer.parseInt(subNumStr);
				subNumInt++;
				String formatSubNumIntStr = ""+subNumInt;
				
				//pad the numeric substring as needed to fill 8 characters
				while (formatSubNumIntStr.length() < 5)
				{
					formatSubNumIntStr = "0"+ formatSubNumIntStr;
				}
				newProfileID = "FPH" + formatSubNumIntStr;
			}
			
			//set the newly created profile ID to the new profile
			pvm.setProfileID(newProfileID);
			
		   String inputStr = " INSERT INTO profile "
					+ " ( ProfileID , FirstName , LastName , Birthday , Image , DateJoined )"
					+ " VALUES ( ?, ?, ?, ?, ?, ?)"; 

		   java.sql.PreparedStatement prepStmt = _db.getConnection().prepareStatement(inputStr);
		    
		   prepStmt.setString(1, pvm.getProfileID() );
		   prepStmt.setString(2,  pvm.getFirstName() );
		   prepStmt.setString(3, pvm.getLastName() );
		   prepStmt.setString(4, pvm.getBirthday() );
		   prepStmt.setBinaryStream( 5 , pvm.getImageFile() , pvm.getImageFile().available() );
		   prepStmt.setString(6, getCurrentDate() );
		    
		   boolean retFlag = prepStmt.execute();
			
			//check to ensure that the profile has been inserted into the database correctly
			if (retFlag !=  false)
				pvm = null;
		}
		catch( SQLException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return (pvm);	
	}
	
	public Blob getProfileImageBlob(ProfileViewModel pvm)
	{
		Blob profileImageBlob = null;
		String retrieveStr =  " SELECT Image FROM profile WHERE ProfileId = ?";
		PreparedStatement prepStmt;
		
		try 
		{
			prepStmt = _db.getConnection().prepareStatement(retrieveStr);
			prepStmt.setString(1,pvm.getProfileID());
			ResultSet result = prepStmt.executeQuery();			
			result.next();
			profileImageBlob = result.getBlob(1) ;
	    } 
	    catch (SQLException e) 
	    {
			e.printStackTrace();
		}
	    
	    return profileImageBlob;
	}
	
	
	/*
	 * @brief Delete a profile from the database by profile ID
	 * @param Profile object
	 * @return Boolean
	 */
	
	public boolean deleteProfileReturnBoolean(Profile p)
	{
		int retVal = 0;
		try 
		{
			retVal = stmt.executeUpdate("DELETE FROM Profile WHERE ProfileId == '" + p.getProfileID() +"'");
		}
		catch(SQLException e) { e.getMessage(); }
		
		return (retVal == 1);
	}
	
	/*
	 * @brief Wild card search for profiles with matching first and/or last name to target string
	 * @param String
	 * @return HashMap<String, String>
	 */	
	public HashMap<String, String> getProfilesByName(String targetName)
	{
		HashMap<String, String> profileIDNameMap = new HashMap<String, String>();
		try
		{
			result = stmt.executeQuery
					(
		  			  "SELECT ProfileId, FirstName, LastName "
					+ "FROM Profile "
					+ "WHERE FirstName LIKE '%" + targetName + "%'"
					+ "OR LastName LIKE '%" + targetName + "%'" 
					+ "ORDER BY FirstName"
					);
			
			while (result.next())
			{
				profileIDNameMap.put(result.getString(1),result.getString(2) + result.getString(3) );
			}
		}
		catch(SQLException e)
		{
			e.getMessage();
		}
		
		return profileIDNameMap;
	}



	public ArrayList<ProfileViewModel> getAllFriends(String profileID) 
	{
		
		ArrayList<ProfileViewModel> friendList = new ArrayList<ProfileViewModel>();
 		if (profileID == null || profileID.length() == 0)
			return friendList;
		try 
		{
			String inputStr = " SELECT FriendId"
							+ " FROM Profile"
							+ " INNER JOIN Profile_Friend ON"
							+ "	ProfileId_FK = ProfileId"
							+ " WHERE ProfileId_FK = ?";
			
			PreparedStatement prepStmt = _db.getConnection().prepareStatement(inputStr);
			prepStmt.setString(1, profileID);
			result = prepStmt.executeQuery();
			
			while ( result.next() )
			{
				String tempID = result.getString(1);
				ProfileViewModel pvm = this.getProfileViewModelByProfileID(tempID);
				friendList.add(pvm);
			}
			
		}
		catch(SQLException e)
		{
			System.out.println(e.getMessage());
		}
		
		
		
		return friendList;
	}
	
	
	
	
	public boolean addToFriendsList(ProfileViewModel cProfile, ProfileViewModel sProfile) 
	{
		boolean resultFlag = true;
		String inputStr = "INSERT INTO Profile_Friend"
						+ " (ProfileId_FK, FriendId)"
						+ " VALUES (?, ?)";
		try 
		{
			PreparedStatement prepStmt = _db.getConnection().prepareStatement(inputStr);
		
			prepStmt.setString(1,  cProfile.getProfileID());
			prepStmt.setString(2,  sProfile.getProfileID());
	
			resultFlag = prepStmt.execute();
		} 
		catch (SQLException e) 
		{
			System.out.println(e.getMessage()); 
		}
		return resultFlag;
	}
	
	public boolean deleteFromFriendsList(ProfileViewModel cProfile, ProfileViewModel sProfile) 
	{
		boolean resultFlag = true;
		String inputStr = "DELETE FROM Profile_Friend"
						+ " WHERE ProfileId_FK = ? AND FriendId = ?";
		try 
		{
			PreparedStatement prepStmt = _db.getConnection().prepareStatement(inputStr);
		
			prepStmt.setString(1,  cProfile.getProfileID());
			prepStmt.setString(2,  sProfile.getProfileID());
	
			resultFlag = prepStmt.execute();
		} 
		catch (SQLException e) 
		{
			System.out.println(e.getMessage()); 
		}
		return resultFlag;
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
