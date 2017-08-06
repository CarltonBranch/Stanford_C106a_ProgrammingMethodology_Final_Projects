/**Class: FriendList
 * Purpose: Collection class for managing profiles in a FriendList 
 * Author: Carlton Branch
 * Date: July 22, 2017
 */

import java.util.ArrayList;

public class FriendList {

	private ArrayList<Profile>container;

	// default constructor
	public FriendList() 
	{
		container = new ArrayList<Profile>();
	}

	public boolean addFriend (Profile p)
	{
		//check to make sure the profile is not already in the list
		if (!container.contains(p))
		{
			container.add(p);
			return true;
		}
		return false;
	}
	
	public ArrayList<Profile> getFriendsByName(String targetName)
	{
		ArrayList<Profile> resultList = new ArrayList<Profile>();
		String[] tokenizeTargetNameArray = targetName.split(" ");
		
		for (Profile each : container)
		{
			//try to match any of the user's input against each profile's first name or last name
			for (int i = 0; i < tokenizeTargetNameArray.length; ++i)
				if (    each.getFirstName().contains(tokenizeTargetNameArray[i])
					|| 	each.getFirstName().contains(tokenizeTargetNameArray[i])
				   )
				{
					resultList.add(each);
					break;
				}
		}
		return resultList;
	}
	
	public boolean removeFriendByProfileID(String searchProfileID)
	{
		for(Profile profile : container)
		{
			if (profile.getProfileID().equals(searchProfileID))
			{
				container.remove(profile);
				return true;
			}
		}
		return false;
	}
	
}//End of FriendList class
