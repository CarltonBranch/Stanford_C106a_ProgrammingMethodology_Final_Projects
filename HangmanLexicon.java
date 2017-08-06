/*
 * Hangman Lexicon 
 * Purpose: class reads the text file and manages the word collection for the game
 * Author: Carlton L. Branch
 * Date: July 2017
 */

import java.io.*;
import java.util.*;

public class HangmanLexicon {

	ArrayList<String> wordList;
	
	public HangmanLexicon() {
		wordList = new ArrayList<String>();
	}
	
	public int getWordCount()
	{
		return wordList.size();
	}
	
	public String getWordAtIndex(int idx)
	{
		return wordList.get(idx);
	}
	public String populateLexiconListFromFile(String filename) 
	{
		String message = "";
	
		try 
		{
			FileReader inFile = new FileReader(filename);
			BufferedReader br = new BufferedReader(inFile);
			String line ="";
			
			while ( true )	
			{	
				line = br.readLine();
			
				if (line == null)
					break;
				
				wordList.add(line);
			}
			br.close();
		}
		catch(IOException e)
		{
			e.getMessage();
		}
		
		//Return result message
		if (wordList.size()!=0)
			message = "Word Lexicon added successfully - " + wordList.size() + " words added to lexicon";
		else
			message = "Error!";
		
		return message;
	}	
}//End of HangmanLexicon class
