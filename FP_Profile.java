import java.sql.Blob;

public class Profile 
{
	//private instance variables
	private String profileID;
	private String firstName; 
	private String lastName;
	private String birthday;
	private Blob blobImage;
	private FriendList friendList;
	private String profileCreationDate;
	
	//getters
	public String getProfileID() {return profileID;}
	public String getFirstName() {return firstName;}
	public String getLastName() {return lastName;}
	public String getBirthday() {return birthday;}
	public Blob getImageBlob() {return blobImage;}
	public FriendList getFriendList() { return friendList;}
	public String getProfileCreationDate() { return profileCreationDate.toString();}
	
	//setters
	public void setProfileID(String pID) { profileID = pID;}
	public void setFirstName(String fn) {firstName = fn;}
	public void setLastName(String ln)  { lastName = ln;}
	public void setBirthday(String bd) {birthday = bd;}
	public void setProfileCreationDate(String pcDate) {this.profileCreationDate = pcDate;}
	public void setImageBlob(Blob bin) {blobImage = bin;}
	
	
	//constructor
	public Profile()
	{
		this.firstName = "";
		this.lastName = "";
		this.birthday = null;
		this.blobImage = null;
		this.profileCreationDate = null;
	}
	
	public Profile(String firstName, String lastName, String birthday,  Blob image, String pcDate)
	{
		this.firstName = firstName;
		this.lastName = lastName;
		this.birthday = birthday;
		this.blobImage = image;
		this.profileCreationDate = pcDate;
	}
	
}