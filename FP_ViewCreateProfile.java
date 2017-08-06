import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.*;
import javax.swing.*;
import org.jdatepicker.impl.*;

@SuppressWarnings("serial")
public class ViewCreateProfile extends JFrame{	
	private final int WINDOW_HEIGHT = 350;
	private final int WINDOW_WIDTH = 400;
	private ProfileController controller;
	private Blob imgDataBlob;
	ProfileViewModel retrievedProfile;
	private ImageUtility imgUtil;
	private File file;
	
	
	
	public ProfileViewModel getRetrievedProfile() {return retrievedProfile;}
	
	public ViewCreateProfile (FacePamphletViewer f)
	{
		imgUtil = new ImageUtility();
		retrievedProfile = null;
		setImgDataBlob(null);
	    controller = new ProfileController();
	
		JPanel topPnl = new JPanel();
		JLabel createProfileLbl = new JLabel("Create Profile");
		topPnl.setBackground(Color.orange);
		topPnl.add(createProfileLbl);
		
		JPanel bottomPnl = new JPanel();
		JButton createProfileBtn = new JButton("Done");
		bottomPnl.add(createProfileBtn);
		
		JLabel firstNameLbl = new JLabel("First name");
		JTextField firstNameFld = new JTextField (15);
		JLabel lastNameLbl = new JLabel("Last name");
		JTextField lastNameFld = new JTextField (15);
		JLabel birthdayLbl = new JLabel("Birthday");

		SqlDateModel model = new SqlDateModel();
		Properties p = new Properties();
		p.put("text.today", "Today");
		p.put("text.month", "Month");
		p.put("text.year", "Year");
		JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
		JDatePickerImpl datePicker = new JDatePickerImpl(datePanel, new DateComponentFormatter());
		
		JLabel uploadImageLbl = new JLabel("Upload image");
		JButton uploadImageBtn = new JButton("Choose file...");
		JLabel selectImageResultLbl = new JLabel("Image selected: " );
		JFileChooser fileChooser = new JFileChooser();
		uploadImageLbl.add(uploadImageBtn);		
		
		JPanel centerPanel = new JPanel();
		
		JPanel firstNamePnl = new JPanel();
		firstNamePnl.add(firstNameLbl);
		firstNamePnl.add(firstNameFld);

		JPanel lastNamePnl = new JPanel();
		lastNamePnl.add(lastNameLbl);
		lastNamePnl.add(lastNameFld);
		
		JPanel chooseDatePnl = new JPanel();
		chooseDatePnl.add(birthdayLbl);
		chooseDatePnl.add(datePicker);
		
		JPanel uploadImagePnl = new JPanel();
		JPanel uploadImagePnlsub1 = new JPanel();
		uploadImagePnlsub1.add(uploadImageLbl);
		uploadImagePnlsub1.add(uploadImageBtn);
		JPanel uploadImagePnlsub2 = new JPanel();
		uploadImagePnlsub2.add(selectImageResultLbl);
		uploadImagePnl.setLayout(new GridLayout(2,0));
		uploadImagePnl.add(uploadImagePnlsub1);
		uploadImagePnl.add(uploadImagePnlsub2);
		
		JPanel centerPanelsub1 = new JPanel();
		centerPanelsub1.setLayout(new GridLayout(3,0));
		centerPanelsub1.add(firstNamePnl);
		centerPanelsub1.add(lastNamePnl);
		centerPanelsub1.add(chooseDatePnl);
		
		JPanel centerPanelsub2 = new JPanel();
		centerPanelsub2.add(uploadImagePnl);
		
		centerPanel.setLayout(new BorderLayout());
		centerPanel.add(centerPanelsub1, BorderLayout.CENTER);
		centerPanel.add(centerPanelsub2, BorderLayout.SOUTH);
		
		uploadImageBtn.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e)
			{
				fileChooser.showOpenDialog(null);
				
				if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
				{	
					try 
						{
							file = new File(fileChooser.getSelectedFile().getAbsolutePath() );
							setImgDataBlob(imgUtil.convertFilePathToBlob(file.getPath() ));
						} 
						catch (SQLException | IOException ex) 
						{ 
							ex.printStackTrace(); 
						}
				}
			}
		});
			
		
		createProfileBtn.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) {
				String firstName = firstNameFld.getText();
				String lastName  = lastNameFld.getText();
				java.sql.Date birthdayDate  = (java.sql.Date) datePicker.getModel().getValue();
				String birthdayStr = birthdayDate.toString();
			
				if (file == null)
				file = new File("imgNotFound.png");
				
				
					FileInputStream fin = null;
				
					try {
						fin = new FileInputStream(file);
					} catch (FileNotFoundException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}
				    
				   
				
				
				
				ProfileViewModel insertProfile = new ProfileViewModel(firstName, lastName, birthdayStr, fin, controller.getCurrentDate());
				retrievedProfile = controller.addNewProfileReturnProfile(insertProfile);
				
				if (retrievedProfile != null )
				{
					try {
						f.setCurrentProfile(retrievedProfile);
					}
					catch (IOException e1) 
					{
						e1.printStackTrace();
					}//good pop-up	
					setVisible(false); 
					dispose(); 
				}
				else
				{
					//bad pop-up
				}
				
			}
			
		});
		
		this.add(centerPanel);
		this.add(bottomPnl,BorderLayout.SOUTH);
		this.add(topPnl, BorderLayout.NORTH);
		this.setVisible(true);
		this.setTitle("Create a new Profile");
		this.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		this.setLocationRelativeTo(null);
	}

	public Blob getImgDataBlob() {
		return imgDataBlob;
	}

	public void setImgDataBlob(Blob imgDataBlob) {
		this.imgDataBlob = imgDataBlob;
	}	
}
