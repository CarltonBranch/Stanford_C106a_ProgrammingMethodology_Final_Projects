import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import javax.swing.*;
import java.util.*;

public class NameSurferGraph extends JPanel implements ComponentListener, NameSurferConstants{
	private static final long serialVersionUID = 1L;
	private ArrayList<NameSurferEntry> currentEntryList;
	
	/** The width of the application window */
	public static int APPLICATION_WIDTH = 800;

/** The height of the application window */
	public static int APPLICATION_HEIGHT = 600;
	
	
	public NameSurferGraph()
	{
		currentEntryList = new ArrayList<NameSurferEntry>();
		this.addComponentListener(this);
	}
	//clear the list of entries
	public void clear() 
	{
		currentEntryList.clear();
		paintComponent(getGraphics());
	}
	
	
	//add a new entry
	public void addEntry(NameSurferEntry entry) 
	{
		currentEntryList.add(entry);
		this.update(getGraphics());
	}
	
	public Color chooseRandomColor()
	{
		Random rndm = new Random();
		return new Color(rndm.nextInt(255), rndm.nextInt(255), rndm.nextInt(255));
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		g.drawLine(0, APPLICATION_HEIGHT-20, APPLICATION_WIDTH, APPLICATION_HEIGHT-20);
		g.drawLine(0, 20, APPLICATION_WIDTH, 20);

		//Draw horizontal bars
		int spanDist = 0;
		
		for ( int i = 0; i < NDECADES; ++i )
		{
			g.setColor( Color.gray );
			g.drawLine( spanDist + APPLICATION_WIDTH / NDECADES, 0, spanDist + APPLICATION_WIDTH / NDECADES , APPLICATION_HEIGHT);
			spanDist += APPLICATION_WIDTH/NDECADES;
		}
		spanDist = 0;
		
		//Write out the years
		for ( int i = 0 ; i < NDECADES ; ++i )
		{
			g.drawString(" "+ (START_DECADE + 10 * i ), spanDist, APPLICATION_HEIGHT-8);
			spanDist += APPLICATION_WIDTH/NDECADES;
		}
		
		for (int i = 0; i < currentEntryList.size(); ++i)
		{
			g.setColor(chooseRandomColor());
			spanDist = 0;
			int oldXPosition = 0;
			int oldYPosition = currentEntryList.get(i).getRank(0);
			
			for (int j = 0; j < NDECADES; ++j)
			{
				if (currentEntryList.get(i).getRank(j) == 0)
					g.drawString(currentEntryList.get(i).getName()+"*", spanDist, APPLICATION_HEIGHT - 22);
					else
					g.drawString(currentEntryList.get(i).getName()+" "+ currentEntryList.get(i).getRank(j), spanDist, ((currentEntryList.get(i).getRank(j))/2 )+40 );
				spanDist += APPLICATION_WIDTH/NDECADES;
			}

			for (int j = 0; j < NDECADES -1; ++j)
			{
				if (currentEntryList.get(i).getRank(j) == 0)
					{
					g.drawLine(oldXPosition, 1000, oldXPosition + APPLICATION_WIDTH / NDECADES, APPLICATION_HEIGHT);
					oldXPosition += APPLICATION_WIDTH / NDECADES;
					oldYPosition = 1000;
				
					}
				else
				{	g.drawLine(oldXPosition, oldYPosition, oldXPosition + APPLICATION_WIDTH / NDECADES, ((currentEntryList.get(i).getRank(j+1))/2)+40);
				oldXPosition += APPLICATION_WIDTH / NDECADES;
				oldYPosition = (currentEntryList.get(i).getRank( j + 1 ) / 2 ) + 40;
			}
		}
		}
	}

	@Override
	public void componentHidden(ComponentEvent arg0) {}
	@Override
	public void componentMoved(ComponentEvent arg0) {}
	@Override
	public void componentResized(ComponentEvent arg0) 
	{
		APPLICATION_WIDTH = this.getWidth();
		APPLICATION_HEIGHT = this.getHeight();
		paintComponent(getGraphics());
		this.update(getGraphics());

	
	}
	@Override
	public void componentShown(ComponentEvent arg0) {}
	
	
	
	
}