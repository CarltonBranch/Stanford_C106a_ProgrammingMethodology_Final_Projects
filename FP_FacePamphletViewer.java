import java.awt.*; 
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.plaf.ColorUIResource;

import java.awt.event.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;

@SuppressWarnings("serial")
public class FacePamphletViewer extends JFrame 
{
 	private final int APPLICATION_WIDTH = 1000;
	private final int APPLICATION_HEIGHT = 700;
	private final String LOOK_AND_FEEL = "Nimbus";
	private Font nameTitleFont = new Font("Verdana", 20, 20);
	
	private ProfileViewModel currentProfile;
	private ProfileViewModel selectedProfile;
	
	private static SelectProfileView selectProfileView;

	JPanel basePanel;
	private JLabel currentUserLbl;
	private ProfileController controller;
	private MsgController msgController;
	private static FacePamphletViewer f;
	private ImagePanel currentUserImagePnl;
	private JLabel currentUserTxt;
	private JLabel joinedDateTxt;
	private JLabel oldStatusLbl;
	private HashMap<String, String> retrievedCollection;
	
	
	private class SearchFrame extends JFrame
	{
		//constructor
		public SearchFrame(HashMap<String, String> searchResultMap)
		{

			JPanel titlePnl = new JPanel();
			JLabel titleTxt = new JLabel("Search Results:");
			titlePnl.setBackground(Color.ORANGE);
			titlePnl.add(titleTxt);
			titlePnl.setLayout(new FlowLayout(FlowLayout.CENTER));
			retrievedCollection = searchResultMap;
			
			DefaultListModel<String> dlm = new DefaultListModel<String>();
		
			for (String key : searchResultMap.keySet())
			{
				dlm.addElement(searchResultMap.get(key));
			}
			
			JList<String> list = new JList<String>();
			list.setModel(dlm);
			
			list.addListSelectionListener(new ListSelectionListener() 
			{
				public void valueChanged(ListSelectionEvent e) 
				{
					for (String key : retrievedCollection.keySet())
					{
						if (retrievedCollection.get(key) == list.getSelectedValue())
						{
							try 
							{
								setSelectedProfile( controller.getProfileViewModelByProfileID(key) );
							} 
							catch (IOException | SQLException ex) 
							{
								ex.printStackTrace();
							}
							break;
						}
					}
					setVisible(false); 
					dispose(); 
				}
			});
			JScrollPane pane = new JScrollPane(list);
			this.add(titlePnl,BorderLayout.NORTH);
			this.add(pane);
			this.setSize(200,250);
			this.setVisible(true);
			this.setLocationRelativeTo(null);
		}
	}
	
	public void setCurrentProfile(ProfileViewModel pvm) throws IOException
	{
		currentProfile = pvm;
		String currentProfileName = currentProfile.getFirstName() + "  " + currentProfile.getLastName();
		oldStatusLbl.setText(msgController.getTopStatusMsg(currentProfile.getProfileID()).getMessage());
		oldStatusLbl.setFont(nameTitleFont);
		currentUserLbl.setText(currentProfileName);
		currentUserImagePnl.drawImage( pvm );
		currentUserImagePnl.setBackground(getBackground());
		currentUserImagePnl.setPreferredSize(new Dimension(100,100));
		joinedDateTxt.setText(currentProfile.getProfileCreationDate().toString());
	}

	public void setSelectedProfile(ProfileViewModel p) throws IOException, SQLException
	{
		selectedProfile = p;
		basePanel.remove(selectProfileView);

		//Validate and repaint
		basePanel.validate();
		basePanel.repaint();
		
		selectProfileView = new SelectProfileView(p, f);

		//Validate and repaint
		selectProfileView.validate();
		selectProfileView.repaint();
		
		basePanel.add(selectProfileView, BorderLayout.CENTER);

		//Validate and repaint
		basePanel.validate();
	}
	
	public void setLookAndFeel(String lookFeelType) 
	{
		try 
		{
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
			UIManager.put("nimbusBase", Color.GRAY);
			UIManager.put("Button.focus", new ColorUIResource(new Color(0, 0, 0, 0)));
	        UIManager.put("ToggleButton.focus", new ColorUIResource(new Color(0, 0, 0, 0)));
		} 
		catch (Exception e) {System.out.println(e.getMessage()); }
	}
	
	public FacePamphletViewer()
	{
		controller = new ProfileController();
		msgController = new MsgController();
		
			currentUserImagePnl = new ImagePanel(100);
		

		selectProfileView = new SelectProfileView(f);
		
		setLookAndFeel(LOOK_AND_FEEL);
		
		//create a menu bar
		JMenuBar menuBar = new JMenuBar();
		JMenu FileMenu = new JMenu("File");
		JMenuItem ExitMenuItem = new JMenuItem("Exit");
		FileMenu.add(ExitMenuItem);
		menuBar.add(FileMenu);
		this.add(menuBar, BorderLayout.NORTH);

		//create base panel
		basePanel = new JPanel();
		basePanel.setLayout(new BorderLayout());

		//create action bar
		JPanel actionBarPanel = new JPanel(); 
		actionBarPanel.setBackground(Color.orange);
		JLabel actionCreateLbl = new JLabel("Create New Profile ");
		JButton actionCreateBtn = new JButton("Go");
		JLabel actionSearchLbl = new JLabel("   Profile Search");
		JTextField actionSearchFld = new JTextField(15);
		JButton actionSearchBtn = new JButton("Search");
		actionBarPanel.add(actionCreateLbl);
		actionBarPanel.add(actionCreateBtn);
		actionBarPanel.add(actionSearchLbl);
		actionBarPanel.add(actionSearchFld);
		actionBarPanel.add(actionSearchBtn);
		basePanel.add(actionBarPanel, BorderLayout.NORTH);
		
		//create side panel
		JPanel sideBarPanel = new JPanel();
		sideBarPanel.setBackground(Color.GRAY);
		sideBarPanel.setLayout(new GridLayout (3,0));
		
		JPanel currentUserPnl = new JPanel();
		currentUserLbl = new JLabel("   Current User Not Selected!   ");
		currentUserTxt = new JLabel();
		currentUserLbl.setFont(nameTitleFont );
		JButton addNewPictureBtn = new JButton("Add New Picture");
		JLabel joinedDateLbl = new JLabel("Face Pamphleter since: ");
		joinedDateLbl.setLayout(new FlowLayout(FlowLayout.CENTER,0,0));
		joinedDateTxt = new JLabel();
		joinedDateTxt.setLayout(new FlowLayout(FlowLayout.CENTER,0,0));
		currentUserImagePnl.setSize(50,50);
		currentUserImagePnl.setBackground(Color.WHITE);
		currentUserPnl.setLayout(new BorderLayout());
		
		JPanel currentUserSubPanel1 = new JPanel();
		currentUserSubPanel1.add(currentUserLbl);
		currentUserSubPanel1.add(currentUserTxt);
		currentUserPnl.add(currentUserSubPanel1, BorderLayout.NORTH);
		currentUserPnl.add(currentUserImagePnl, BorderLayout.CENTER);
		
		oldStatusLbl = new JLabel();
		
		FlowLayout flow = new FlowLayout(FlowLayout.CENTER);
		JPanel oldStatusPnl = new JPanel(flow);

		oldStatusPnl.add(oldStatusLbl);
		
		JPanel currentUserPnlsub1 = new JPanel();
		
	    LayoutManager layout = new BoxLayout(currentUserPnlsub1, BoxLayout.Y_AXIS);
	    currentUserPnlsub1.setLayout(layout);
		
	    
	    JPanel currentUserPnlsubA = new JPanel();
		currentUserPnlsubA.setLayout(new FlowLayout(FlowLayout.CENTER,0,0));
		currentUserPnlsubA.add(addNewPictureBtn);
		
		JPanel currentUserPnlsubB = new JPanel();
		currentUserPnlsubB.setLayout(new FlowLayout(FlowLayout.CENTER,0,0));
		currentUserPnlsubB.add(joinedDateLbl);
		currentUserPnlsubB.add(joinedDateTxt);
		
		JPanel currentUserPnlsubC = new JPanel();
		currentUserPnlsubC.setLayout(new FlowLayout(FlowLayout.CENTER,0,0));
		currentUserPnlsubC.add(oldStatusPnl);
		
		JPanel currentUserPnlsub3 = new JPanel();
		currentUserPnlsub3.setLayout(new GridLayout(3,0));
		currentUserPnlsub3.add(currentUserPnlsubA);
		currentUserPnlsub3.add(currentUserPnlsubB);
		currentUserPnlsub3.add(currentUserPnlsubC);
		
		
		currentUserPnl.add(currentUserPnlsub3,BorderLayout.SOUTH);
		
		JTextField enterNewStatusFld = new JTextField(10);
		JButton enterNewStatusBtn = new JButton("Update Status");
		JButton addSelectProfileAsFriendBtn = new JButton("Add this profile to Friend List");	
		JButton deleteSelectProfileAsFriendBtn = new JButton("Delete this profile from Friend List");
	
		JPanel modifyUserPnl = new JPanel();
		modifyUserPnl.setLayout(new GridLayout(6,0));
		modifyUserPnl.add(enterNewStatusFld);
		modifyUserPnl.add(enterNewStatusBtn);
		modifyUserPnl.add(addSelectProfileAsFriendBtn);
		
		JPanel deleteUserPnl = new JPanel();
		deleteUserPnl.add(deleteSelectProfileAsFriendBtn);
		
		sideBarPanel.add(currentUserPnl);
		sideBarPanel.add(modifyUserPnl);
		sideBarPanel.add(deleteUserPnl);
		
		basePanel.add(selectProfileView);
		basePanel.add(sideBarPanel,BorderLayout.WEST);
		
		//Action Listeners--------------------------------------------------------------------
		actionCreateBtn.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e) {
				new ViewCreateProfile(f);	
			}
		});
	
		enterNewStatusBtn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) 
			{
				String inputMessageTxt = enterNewStatusFld.getText();
				
				if (currentProfile != null && inputMessageTxt.length() != 0)
				{
					MessageViewModel mvm = new MessageViewModel(currentProfile.getProfileID(), inputMessageTxt, msgController.getCurrentDateTime() );
					msgController.addNewStausUpdateMessage(mvm);
					enterNewStatusFld.setText("");
					JOptionPane.showMessageDialog(null,"Your status has been updated!");
					oldStatusLbl.setText(msgController.getTopStatusMsg(currentProfile.getProfileID()).getMessage());
					
				}}
		});
		
		actionSearchBtn.addActionListener(new ActionListener() 
		{
			HashMap<String, String> profileIDNameMap = new HashMap<String, String>();
			
			public void actionPerformed(ActionEvent e) 
			{
				profileIDNameMap = controller.getProfilesByName(actionSearchFld.getText());
				new SearchFrame(profileIDNameMap);
			}
			
		});

		addSelectProfileAsFriendBtn.addActionListener(new ActionListener() 
		{
		
			public void actionPerformed(ActionEvent e) 
			{
				
				if (currentProfile != null && selectedProfile != null)
				{
					if (!controller.addToFriendsList (currentProfile, selectedProfile))
						JOptionPane.showMessageDialog(null, "Friend successfully added!");
					else
						JOptionPane.showMessageDialog(null, "Friend NOT successfully added!");
				}
			}
		});

		
		deleteSelectProfileAsFriendBtn.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				if (currentProfile != null && selectedProfile != null)
				{
					if (!controller.deleteFromFriendsList (currentProfile, selectedProfile))
						JOptionPane.showMessageDialog(null, "Friend successfully removed from your FriendList!");
					else
						JOptionPane.showMessageDialog(null, "Friend NOT successfully removed from your FriendList!");
				}
			}
		});
		
		//----------------------------------------------------------------------------------------
			
		
		this.add(basePanel);
		this.setTitle("FacePamphlet");
		this.setSize(APPLICATION_WIDTH, APPLICATION_HEIGHT);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		this.pack();
	}
	
	public static void main(String[] args)
	{
		f = new FacePamphletViewer();
	}

	public void setSelectProfileToNull() 
	{
		selectedProfile = null;
	}
	
}