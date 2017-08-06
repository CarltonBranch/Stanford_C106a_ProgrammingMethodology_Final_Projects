/*
 * Breakout Game (Applet)
 * Author: Carlton L. Branch
 * Date: July 2017
 */

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.Random;
import java.applet.*;
import javax.swing.*;
import java.util.ArrayList;

public class Breakout extends Applet
{
	private static final long serialVersionUID = 1L;
	private int LAG_TIME = 20; //time in milliseconds between repaints
	public Timer timer;
	private static int lifeCount = 0;
	private static int playerScore = 0;
	
	public ArrayList<Brick> brickList;
	
	public final int WIN_WIDTH = 450;
	public final int WIN_HEIGHT = 650;
	public final int PADDLE_WIDTH =  80;
	public final int PADDLE_HEIGHT = 20;
	public final int BALL_DIAMETER = 25;
	
	public Paddle p;
	public int paddleXPos;
	
	public Ball b;
	public int ballXPos;
	public int ballYPos;
	private double vx, vy;
	
	//Brick class
	public class Brick extends Polygon
	{
		private static final long serialVersionUID = 1L;
		public Color color;
		
		public Brick(Graphics g, int xPos, int yPos, int width, int height, Color c) 
		{
			this.color = c;
			this.addPoint(xPos, yPos);
			this.addPoint(xPos+ width, yPos);
			this.addPoint(xPos+width, yPos+height);
			this.addPoint(xPos, yPos+height);
			g.fillPolygon(this);
		}
	}
	
	//Ball class
	public class Ball 
	{
		int xPos = 0;
		int yPos = 0;
		int diameter = 0;
		
		public Ball(Graphics g, int xPos, int yPos, int diameter) 
		{	
			this.xPos = xPos;
			this.yPos = yPos;
			this.diameter = diameter;
			g.setColor(Color.red);
		
			g.fillOval(xPos, yPos, diameter, diameter);
			g.setColor(Color.white);
			g.fillOval(xPos + 5,  yPos + 4,  8 , 8 );
		}
		public Rectangle getBounds() { return new Rectangle(xPos,yPos, diameter, diameter); }
		public int getXPos() { return xPos;}
		public int getYPos() { return yPos;}
	}
	
	//Paddle class
	public class Paddle extends Polygon
	{
		private static final long serialVersionUID = 1L;
		public Paddle(Graphics g, int xPos, int yPos) 
		{
			g.setColor(Color.black);
			this.addPoint(xPos, yPos);
			this.addPoint(xPos + PADDLE_WIDTH, yPos);
			this.addPoint(xPos + PADDLE_WIDTH, yPos + PADDLE_HEIGHT);
			this.addPoint(xPos, yPos +PADDLE_HEIGHT);
			g.fillPolygon(this);
		}
	}
		
	//Tick Listener class - determines the applet repaint frequency
	private class TickListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			ballXPos += vx;
			ballYPos += vy;
		
			Rectangle rect = p.getBounds();
			
			if (
				 rect.contains(ballXPos, ballYPos + BALL_DIAMETER)
		      || rect.contains(ballXPos, ballYPos)
		      || rect.contains(ballXPos + BALL_DIAMETER, ballYPos)
			  || rect.contains(ballXPos + BALL_DIAMETER, ballYPos + BALL_DIAMETER)
			  )
			{
				
				vy *=-1;
			}
		
			for (int i = 0; i < brickList.size(); ++i)
			{
				Rectangle tempRect = brickList.get(i).getBounds();
				//Increase the speed and score based on which block has been destroyed
				if (tempRect.intersects(b.getBounds()))
				{	
					brickList.remove(i);
					vy *=-1;
					
					if (i <= 10)
					{
						vy *=1.0025;
						playerScore+=550;
					}
					if (10 < i && i <= 19)
					{
						playerScore+=500;
					}
					if (20 < i && i <= 39)
					{
						vy *=1.0015;
						playerScore+=400;
					}if (40 < i && i <= 59)
					{
						playerScore+=300;
					}if (60 < i && i <= 79)
					{
						vy *=1.0005;
						playerScore+=200;
					}if (80 < i && i <= 89)
					{
						playerScore+=100;
					}
					if (90 < i && i <= 99)
					{
						playerScore+=55;
					}
				}
			}
			
			if (ballXPos < 0 || ballXPos > WIN_WIDTH - BALL_DIAMETER)
				vx *= -1;
			
			if (ballYPos < 0 )
				vy *= -1;
			
			//if the ball falls below the screen or all the bricks are gone - test for end of game status
			if (ballYPos > WIN_HEIGHT + BALL_DIAMETER || brickList.size() == 0)
				{
					if (lifeCount == 2 || brickList.size() == 0)
					{
						
						JFrame frame = new JFrame("Results");
						JPanel resultsPnl = new JPanel();
						
						JLabel resultsLbl = new JLabel("  Final Score: " + playerScore);
						resultsLbl.setFont(new Font("Verdana", 20,20));
						resultsLbl.setForeground(Color.WHITE);
						resultsLbl.setLayout(new FlowLayout(FlowLayout.CENTER));
						String messageString = "";
						
						//If all the bricks have been destroyed - end the game
						if (brickList.size() == 0)
						{
							resultsPnl.setBackground(Color.GREEN);
							messageString = "     You Won!!";
						}
						else
						{
							resultsPnl.setBackground(Color.RED);
							messageString = "     You Lost!!";
						}
						
						JLabel messageLbl = new JLabel(messageString);
						messageLbl.setFont(new Font("Verdana", 20,20));
						messageLbl.setForeground(Color.WHITE);
						messageLbl.setLayout(new FlowLayout(FlowLayout.CENTER));
						
						resultsPnl.add(resultsLbl);
						resultsPnl.add(messageLbl);
						resultsPnl.setLayout(new GridLayout(2,0) );
						frame.add(resultsPnl);
						frame.setSize(250, 200);
						frame.setVisible(true);
						frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
						frame.setLocationRelativeTo(null);
						return;
					}
					ballXPos = 225-BALL_DIAMETER/2;
					ballYPos = 250-BALL_DIAMETER/2;			
					lifeCount++;
				}
				
			repaint();
		}//end actionPerformed()
	}//end inner class

	
	public void init()
	{
		brickList = new ArrayList<Brick>();
		generateBricks(getGraphics() );
		paddleXPos = 225-33;
		ballXPos = 225-BALL_DIAMETER/2;
		ballYPos = 250-BALL_DIAMETER/2;
		Random rndm = new Random();
		
		vx = rndm.nextInt(3)+2;
		vy = 8;
		
		this.timer = new Timer(LAG_TIME, new TickListener() );//registers a listener to hear "ticks"
			timer.start();
		
		this.setSize(WIN_WIDTH, WIN_HEIGHT);
		this.setBackground(Color.GRAY);
		this.addMouseMotionListener( new MouseMotionListener() {

			@Override
			public void mouseDragged(MouseEvent arg0) {}

			@Override
			public void mouseMoved(MouseEvent e) {
				if ( e.getX() > WIN_WIDTH - PADDLE_WIDTH )
					paddleXPos = WIN_WIDTH - PADDLE_WIDTH;
				else
					paddleXPos = e.getX();
			}
		});
	}
	
	//Method creates initial brick stack
	public void generateBricks(Graphics g)
	{
		final int UPPER_GAP = 80;
		displayBrickList(g, 3 + UPPER_GAP, Color.red);
		displayBrickList(g, 30 + UPPER_GAP, Color.orange);
		displayBrickList(g, 60 + UPPER_GAP, Color.yellow);
		displayBrickList(g, 90 + UPPER_GAP, Color.green);
		displayBrickList(g, 120 + UPPER_GAP, Color.cyan);	
	}
	
	//Paints the current brick stack to the screen
	public void displayBrickList(Graphics g,  int YPos, Color c)
	{
		int hGap = 0;
		int vGap = 0;
		int startX = 10;
		int startY = YPos;
		int brickWidth = 40;
		int brickHeight = 12;
		g.setColor(c);
		
		for (int j = 0; j < 2; ++j)
		{
			for (int i = 0; i < 10; ++i)
			{
				brickList.add(new Brick(g, startX + hGap , startY + vGap, brickWidth, brickHeight, c));
				hGap += 3 + brickWidth;
			}
			hGap = 0;
			vGap += 3+ brickHeight;
		}	
	}
	
	public void paint(Graphics g) 
	{
		for (int i = 0; i < brickList.size(); ++i)
		{
			g.setColor(brickList.get(i).color);
			g.fillPolygon(brickList.get(i));
		}
		 g.setColor(Color.GREEN);
		 
		 //create Paddle 
		 p = new Paddle(g , paddleXPos ,500);
	
		 //create Ball
		 int diameter = 25;
		 b = new Ball(g, ballXPos, ballYPos, diameter);
		 g.setColor(Color.black);
		 
		 //create life meter
		 Graphics2D g2 = (Graphics2D)g;
		 int livesRemaining = 2 - lifeCount;
		 g2.setFont(new Font ("Verdana", Font.BOLD, 12));
		 g2.setColor(Color.black);
		 g2.fillRect(5, 5, 155, 20);
		 
		 switch (livesRemaining) 
		 {
			 case 2:
				 g2.setColor(Color.yellow);
				 break;
			 case 1:
				 g2.setColor(Color.orange);
				 break;
			 case 0:
				 g2.setColor(Color.red);
				 break;
		 }
		 g2.drawString("Lives Remaining: "+ livesRemaining,  10,  20);
		 g2.setColor(Color.black);
		 g2.fillRect(295, 5, 155, 20);
		 g2.setColor(Color.white);
		 g2.drawString("Player Score: " + playerScore, 300, 20);
	}	
}
