import javax.swing.*;
import java.awt.*;

@SuppressWarnings("serial")
public class HangmanScaffold extends JPanel
{
	//instance of the HangmanRemix class
	HangmanRemix mix;
	public Graphics g;
	static int value = 8;
	
	//set the current value of the 
	public void setValue(int newVal)
	{
		value = newVal;
	}
	
	public HangmanScaffold()
	{
		mix = new HangmanRemix();
		 repaint();
	}//end constructor
	
	public void head(Graphics g){
		g.drawOval(210, 105, 80, 80);
	}
	
	public void body(Graphics g){
		g.drawLine(250, 188, 250, 300);
	}
	
	public void shoulders(Graphics g){
		g.drawLine(210, 220, 290, 220);
	}
	
	public void leftArm(Graphics g){
		g.drawLine( 290, 220, 310, 380);	
	}
	
	public void rightArm(Graphics g){
		g.drawLine( 210, 220, 190, 380);
	}
	
	public void hips(Graphics g ){
		g.drawLine(220, 300, 280, 300);
	}
	
	public void rightLeg(Graphics g){
		g.drawLine(220, 300, 230, 480);
		g.drawLine(230, 480, 225, 485);
	}
	
	public void leftLeg(Graphics g){
		g.drawLine(280, 300, 275, 480);
		g.drawLine(275, 480, 280, 485);
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		if ( value == 0 )
		{
			g.setColor(Color.red);
		}
		else
		{
			g.setColor(Color.BLUE);
		}
		
		if (value <= 7)
			head(g);
		if (value <= 6)
			body(g);
		if (value <= 5)
			shoulders(g);
		if (value <= 4)
			rightArm(g);
		if (value <= 3)
			leftArm(g);
		if (value <= 2)
			hips(g);
		if (value <= 1)
			rightLeg(g);
		if (value == 0)
		{	
			leftLeg(g);
		}		
		//Draw Scaffold
		Graphics2D g2 = (Graphics2D) g;
		g2.setStroke(new BasicStroke(10));
		g.setColor(Color.DARK_GRAY);
		g.drawLine(50,50, 50, 500);
		g.drawLine(50, 50,  250,  50);
		g.drawLine(250,  50,  250,  100);
	
	}
	//end method 
}