/*
 * NameSurfeEntry
 * Purpose: Retrieves local file data based on user input
 * Author: Carlton L. Branch
 * Date: July 2017
 */

public class NameSurferEntry implements NameSurferConstants{
	private String entryName;
	private int[] rankByDecadeList;

	/* Constructor: NameSurferEntry(line) */
	public NameSurferEntry(String line)
	{
		String[] tokenizeLine = line.split(" ");
		
		entryName = tokenizeLine[0];
		rankByDecadeList = new int[NDECADES];
		
		for (int i = 1; i < tokenizeLine.length; ++i)
		{
			rankByDecadeList[i-1]= Integer.parseInt(tokenizeLine[i]);
		}
	}

	/* Method: getName() */
	public String getName() 
	{
		return this.entryName;
	}
	
	/* Method: getRank(decade) */
	public int getRank(int decade)
	{
		if (0 <= decade && decade < NDECADES)
		{
			Object resultValue = this.rankByDecadeList[decade];
			return (int)resultValue;
		}
		else
			return 0;
	}
	
	/* Method: toString() */
	public String toString() 
	{
		StringBuilder resultStr = new StringBuilder();
		
		resultStr.append( this.entryName + " [ ");
		
		for ( int i = 0; i < NDECADES; ++i )
		{
			resultStr.append(rankByDecadeList[i]);
			resultStr.append(" ");
		}
			
		resultStr.append("]");
		
		return resultStr.toString();
	}	
}
