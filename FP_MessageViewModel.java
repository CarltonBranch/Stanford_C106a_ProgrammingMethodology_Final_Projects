//view model class for passing both the date and the message to the view

public class MessageViewModel 
{
	private String profileId, message, date;
	
	public MessageViewModel(String profileID, String message, String date)
	{
		this.profileId = profileID;
		this.message = message;
		this.date = date;
	}
	
	public String getProfileId() {return profileId;}
	public String getMessage()  {return message;}
	
	public String getDate() { return date; }
	
	public void setProfileId(String pId) {this.profileId = pId;}
	public void setMessage(String msg) {this.message = msg;}
	public void setDate(String date) {this.date = date;}
	
}
