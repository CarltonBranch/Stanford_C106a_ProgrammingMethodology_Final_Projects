import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.sql.SQLException;

import javax.swing.*;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class SelectProfileView extends JPanel
{
	ProfileViewModel currSelectProfile;
	ProfileController pController;
	MsgController msgController;
	ImageUtility iUtil;
	ImagePanel imagePnl;
	JPanel subBasePanel;
	private Font nameTitleFont = new Font("Verdana", 20, 20);
	FacePamphletViewer f;
	
	
	SelectProfileView(FacePamphletViewer f)
	{
		iUtil = new ImageUtility();
		this.setBackground(Color.WHITE);
		this.setLayout(new BorderLayout());
		JLabel noProfileLbl = new JLabel("no profile selected yet");
		noProfileLbl.setHorizontalAlignment(SwingConstants.CENTER);
		this.add(noProfileLbl);
	}
	
	public void resetProfile()
	{
		f.setSelectProfileToNull();
		this.remove(subBasePanel);
		this.validate();
		iUtil = new ImageUtility();
		this.setBackground(Color.WHITE);
		this.setLayout(new BorderLayout());
		JLabel noProfileLbl = new JLabel("no profile selected yet");
		noProfileLbl.setHorizontalAlignment(SwingConstants.CENTER);
		this.add(noProfileLbl);		
	}
	
	
	SelectProfileView (ProfileViewModel sp, FacePamphletViewer f) throws IOException, SQLException
	{
		this.f = f;
		iUtil = new ImageUtility();
		currSelectProfile = sp;
		msgController = new MsgController();
		pController = new ProfileController();
	
		JPanel basePanel = new JPanel();
		JPanel namePanel = new JPanel();
		JLabel nameLbl = new JLabel(currSelectProfile.getFirstName()+ " " + currSelectProfile.getLastName());
		nameLbl.setFont(nameTitleFont);
		namePanel.add(nameLbl);
		
		
		ImagePanel userImage = new ImagePanel(300);
		userImage.drawImage(currSelectProfile);
		JPanel topStatusPanel = new JPanel();
		MessageViewModel topStatusMVM = msgController.getTopStatusMsg( sp.getProfileID() ); 
		String insertMsg = topStatusMVM.getMessage();
		
		if (insertMsg != null)
			insertMsg = topStatusMVM.getMessage();
		else
			insertMsg = "No status updates have been made yet";
		
		JLabel topStatusMsgLbl = new JLabel( insertMsg );
		topStatusPanel.add(topStatusMsgLbl);
		
		JPanel setAsCurrentPanel = new JPanel();
		JButton setAsCurrentBtn = new JButton("Set as Current User");
		setAsCurrentPanel.add(setAsCurrentBtn);
		
		JLabel statusLbl = new JLabel("Past status updates:");
		JPanel statusPnl = new JPanel();
		statusPnl.setLayout(new FlowLayout(FlowLayout.CENTER));
		statusPnl.add(statusLbl);
		JPanel statusMsgListPanel = new JPanel();
		JList<String> list = new JList<String>();
		
		ArrayList<MessageViewModel> statusMsgList = new ArrayList<MessageViewModel>();
		statusMsgList = msgController.getAllStatusMessages(currSelectProfile.getProfileID());
		DefaultListModel<String> listModel = new DefaultListModel<String>();
		int count = 0;
		
		for ( MessageViewModel mvm : statusMsgList )
		{
			String timeStr;
			String[] outputStrArray = mvm.getDate().split("T");
			String dateStr = outputStrArray[0];
			timeStr = outputStrArray[1].substring(0,8);
			listModel.add(count, "\""+ mvm.getMessage() + "\"" + " on " + dateStr + " at " + timeStr );
			++count;
		}
		
		list.setModel(listModel);
		list.setBackground(new Color(210,210,210));
		JScrollPane statusPane = new JScrollPane(list);
		statusMsgListPanel.add(statusPane);
		
		
		
		
		
		
		JLabel friendsLbl = new JLabel("Friends");
		JPanel friendListPanel = new JPanel();
		
		JList<String> friendListBase = new JList<String>();
		ArrayList<ProfileViewModel> friendList = new ArrayList<ProfileViewModel>();
		friendList = pController.getAllFriends(currSelectProfile.getProfileID());
		
		if ( friendList.size() != 0 )
		{
			DefaultListModel<String> friendListModel = new DefaultListModel<String>();
		
			int count2 = 0;
			
			for ( ProfileViewModel pvm : friendList )
			{
				friendListModel.add(count2, pvm.getFirstName() + " " + pvm.getLastName() );;
				++count2;
			}
		
			friendListBase.setModel(friendListModel);
			friendListBase.setBackground(new Color(210,210,210));
		}
		JScrollPane statusFriendPane = new JScrollPane(friendListBase);
		friendListPanel.add(statusFriendPane);
		
		
		
		 	JPanel leftPnl = new JPanel();
		    LayoutManager layout = new BoxLayout(leftPnl, BoxLayout.Y_AXIS);
		    leftPnl.setLayout(layout);
		
		JPanel rightPnl = new JPanel();
	    LayoutManager layout2 = new BoxLayout(rightPnl, BoxLayout.Y_AXIS);
	    rightPnl.setLayout(layout2);
	  
		
	    leftPnl.add(namePanel); 
	    leftPnl.add(userImage);
	    leftPnl.add(setAsCurrentPanel); 
	    leftPnl.add(topStatusPanel); 
	    leftPnl.add(statusPnl);
	    leftPnl.add(statusMsgListPanel); 
	    rightPnl.add(friendsLbl);
		rightPnl.add(friendListPanel);
	    
	    basePanel.setLayout(new GridLayout(0,2));
	    basePanel.add(leftPnl);
		basePanel.add(rightPnl);
	    
		subBasePanel = new JPanel();
		
		subBasePanel.setLayout(new BorderLayout());
		subBasePanel.add(basePanel,BorderLayout.NORTH);
		//Action Listeners-------------------------------------------------------
		setAsCurrentBtn.addActionListener( 
				
				(ActionEvent ev)-> 
				{
					try 
					{
						f.setCurrentProfile(sp);
						this.resetProfile();

					} 
					catch (IOException e) 
					{
						e.printStackTrace();
					};
				});
		
		
		this.add(subBasePanel);

	}
}