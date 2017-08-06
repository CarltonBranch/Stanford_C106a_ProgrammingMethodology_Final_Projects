import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.*;

@SuppressWarnings("serial")
public class HangmanRemix extends Applet {
	private JTextArea txtArea;
	private HangmanLexicon lex; 
	private static int numGuesses;
	private String targetWord;
	private String originalWord;
	private ArrayList<Character> correctGuessList;
	private int currentHiddenCharacterCount;
	private StringBuilder outputString;
	private String userGuess;
	private String currentWordState;
	
	public int getUserGuessCount() {return numGuesses;}
	
	public void init() 
	{
		//instantiate HangmanLexicon object and other class variables 
		lex = new HangmanLexicon();
		outputString = new StringBuilder();
		correctGuessList = new ArrayList<Character>();
		numGuesses = 8;
		targetWord = "";
		originalWord = "";
		userGuess = "";
		currentWordState = "";
		
		//create base panels
		JPanel panelA = new JPanel();
		panelA.setBorder(BorderFactory.createTitledBorder("Play game"));
		HangmanScaffold panelB = new HangmanScaffold();
		panelB.setBorder(BorderFactory.createTitledBorder("Result"));
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(0,2));
			
		//create scrollable text area
		txtArea = new JTextArea(28,32);
		JScrollPane pane = new JScrollPane(txtArea);
		panelA.add(pane);
		
		panel.add(panelA);
		panel.add(panelB);
		panel.setBackground(Color.red);
		panel.setPreferredSize(new Dimension(800,500));
		this.add(panel);
		this.setSize(800,500);
		this.setVisible(true);
		
		
		//populate the lexicon list from a text file of words
		outputString.append(lex.populateLexiconListFromFile("10words.txt") );
		displayText(outputString.toString());
	
		//select a word from the list
		Random rndm = new Random();
		originalWord = lex.getWordAtIndex(rndm.nextInt(lex.getWordCount()));
		
		//process the selected word
		targetWord = originalWord.toLowerCase();
		currentHiddenCharacterCount = targetWord.length();
		
		txtArea.addKeyListener(new KeyListener() 
		{
			@Override
			public void keyPressed(KeyEvent k) 
			{
			   if (k.getKeyCode() == KeyEvent.VK_ENTER) 
			   {
		        if (numGuesses != 0)
				{
		        	//read in the user's guess
					userGuess = getUserInput();
					
					//update the viewer based on the current guess count
					panelB.setValue(numGuesses);
					panelB.repaint();
					
					if (userGuess.length() != 1)
					{
						outputString.append("\nInvalid input! Please guess again!");
						outputString.append("\nYour guess >> ");
						displayText(outputString.toString());
					}
	
					else
					
					{
						outputString.append(userGuess);
						char userGuessCharacter = userGuess.toLowerCase().charAt(0);
						
						if ( targetWord.contains( ""+ userGuessCharacter) )
						{
							if (!correctGuessList.contains(userGuessCharacter))
							correctGuessList.add(userGuessCharacter);
						}
						else
						{
							outputString.append("\nThere are no "+userGuess+"'s in the word.");
							displayText(outputString.toString());
							--numGuesses;
						}
					
						currentWordState = displayCurrentStateOfTargetWord();
						//test if user has solved the challenge
						if (!currentWordState.contains("-") || numGuesses == 0)
						{
							//update the viewer based on the current guess count
							panelB.setValue(numGuesses);
							panelB.repaint();
						
							stopProgram();
						}
						else 
						{
							outputString.append("\nThe word now looks like this: ");
							outputString.append(currentWordState);
							outputString.append("\nYou have " + numGuesses + " guesses left.");
							
							//update the viewer based on the current guess count
							panelB.setValue(numGuesses);
							panelB.repaint();
							
							outputString.append("\nYour guess >> ");
							displayText(outputString.toString());
						}
					}
				}
			} 
		}
			@Override
			public void keyTyped(KeyEvent arg0) {}
			@Override
			public void keyReleased(KeyEvent e) {}
		});	
	}
	
	public String getUserInput() 
	{
		String input = "";
				for (int i =  txtArea.getText().length() - 1; i >=0; --i)
					{
						Character tempChar = txtArea.getText().charAt(i);
						if (tempChar == '>')
							break;
						if (!Character.isWhitespace(tempChar) && tempChar != '\n')
						input = tempChar + input;
					}
				return input;
	}
	
	public void displayText(String inputString)
	{
		txtArea.setText(inputString);
		txtArea.setCaretPosition(inputString.length());
	}
	
	public String displayCurrentStateOfTargetWord()
	{
		StringBuilder currentState = new StringBuilder();
		currentHiddenCharacterCount = 0;
		
		for (int i = 0; i < targetWord.length(); ++i)
		{
			if (correctGuessList.contains(targetWord.charAt(i)))
			{
				currentState.append(""+originalWord.charAt(i));
			}
			else
				{
					currentState.append("-");
					++currentHiddenCharacterCount;
				}
			}
		return currentState.toString();
	}

	
	
	public void start() 
	{	//Display welcome message
		outputString.append("\n------------------------------------------------------------------------------------");
		outputString.append("\nWelcome to Hangman!\n");
		displayText(outputString.toString());	
		
		//Display initial state of word
		outputString.append("\nThe word now looks like this: ");
	    currentWordState = displayCurrentStateOfTargetWord(); 
		outputString.append(currentWordState);
		
		//Prompt user for input
		outputString.append("\nYour guess >> \n");
		displayText(outputString.toString());
	}
	
	public void stopProgram() {
		//Print final results to the console
		if (currentHiddenCharacterCount == 0)
		{
			String message = "You guessed the word: " + originalWord;
			message += "\nYou win.";
			displayText(outputString.toString());
			JOptionPane.showMessageDialog(this, message);
		}
		else
		{
			String message = "You're completely hung.";
			message = "\nThe word was: " + originalWord;
			message += "\nYou lose.";
			JOptionPane.showMessageDialog(this, message);
		}
		txtArea.setEditable(false);
		txtArea.setEnabled(false);
	}
	
	public void destroy() {}
}
