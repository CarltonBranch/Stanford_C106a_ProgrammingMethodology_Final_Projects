import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

@SuppressWarnings("serial")
public class NameSurfer extends JFrame implements NameSurferConstants{
	private NameSurferDatabase nameSurferDB;
	private NameSurferGraph graph;
	
	public NameSurfer()
	{
		//create the database 
		nameSurferDB = new NameSurferDatabase(NAMES_DATA_FILE);
		//test for success
		if (nameSurferDB.getNumRecords() == 0)
			System.out.println("Error! Unable to build database!");
		
		graph = new NameSurferGraph();
		
		//set up bottom panel 
		JPanel bottomPanel = new JPanel();
		bottomPanel.setBackground(Color.orange);
		JButton graphBtn = new JButton("Graph");
		JButton clearBtn = new JButton("Clear");
		JTextField nameFld = new JTextField(20);
		JLabel nameLbl = new JLabel("Name ");
		bottomPanel.add(nameLbl);
		bottomPanel.add(nameFld);
		bottomPanel.add(graphBtn);
		bottomPanel.add(clearBtn);
		
		//set up the menu
		JMenuBar bar = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		JMenu editMenu = new JMenu("Edit");
		bar.add(fileMenu);
		bar.add(editMenu);
		this.add(bar, BorderLayout.NORTH);
		
		//set up the main window
		this.add(graph);
		this.add(bottomPanel, BorderLayout.SOUTH);
		this.setSize(APPLICATION_WIDTH,APPLICATION_HEIGHT);
		this.setTitle("NameSurfer");
		this.setLocationRelativeTo(null);
		this.setVisible(true);

		//set up button listeners
		graphBtn.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{	
				NameSurferEntry tempEntry = null;
				
		        tempEntry = nameSurferDB.findEntry(nameFld.getText());
				
		        
		        if ( tempEntry !=  null)
					graph.addEntry(tempEntry);
		        
			}
		});	
		
		clearBtn.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				nameFld.setText("");
				graph.clear();
			}
		});
	
	}
	
	
	public static void main(String[] args)
	{
		new NameSurfer();
	}
}