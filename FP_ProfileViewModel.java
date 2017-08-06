/**Class: ProfileViewModel 
 * Purpose: view model class for passing both the date and the message to the view
 * Author: Carlton Branch
 * Date: July 22, 2017
 */

import java.io.FileInputStream;
import java.sql.Blob;

public class ProfileViewModel {

	private String profileID;
	private String firstName; 
	private String lastName;
	private String birthday;
	private FileInputStream inStream;
	private String profileCreationDate;
	private Blob blob;
	
	public ProfileViewModel()
	{
	}
	
	
	public ProfileViewModel(String firstName, String lastName, String birthday,  Blob imageBlob,  String pcDate)
	{
		this.firstName = firstName;
		this.lastName = lastName;
		this.birthday = birthday;
		this.setBlob(imageBlob); 
		this.profileCreationDate = pcDate;
	}
	
	
	public ProfileViewModel(String firstName, String lastName, String birthday,  FileInputStream inStream,  String pcDate)
	{
		this.firstName = firstName;
		this.lastName = lastName;
		this.birthday = birthday;
		this.inStream = inStream;
		this.profileCreationDate = pcDate;
	}
	
	//getters
	public String getProfileID() {return profileID;}
	public String getFirstName() {return firstName;}
	public String getLastName() {return lastName;}
	public String getBirthday() {return birthday;}
	public FileInputStream getImageFile() {return inStream;}
	public String getProfileCreationDate() { return profileCreationDate.toString();}
	public Blob getBlob() {	return blob; }
	
	//setters
	public void setProfileID(String pID) { profileID = pID;}
	public void setFirstName(String fn) {firstName = fn;}
	public void setLastName(String ln)  { lastName = ln;}
	public void setBirthday(String bd) {birthday = bd;}
	public void setProfileCreationDate(String pcDate) {this.profileCreationDate = pcDate;}
	public void setImage(FileInputStream bin) {inStream = bin;}
	public void setImageBlob(Blob blob ) {this.setBlob(blob);}
	public void setBlob(Blob blob) {this.blob = blob;}
				
	}
