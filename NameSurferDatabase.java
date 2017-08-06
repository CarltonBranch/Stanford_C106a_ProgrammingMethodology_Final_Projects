import java.io.*;
import java.util.*;

/*
 * File: NameSurferDatabase.java
 * -----------------------------
 * This class keeps track of the complete database of names.
 * The constructor reads in the database from a file, and
 * the only public method makes it possible to look up a
 * name and get back the corresponding NameSurferEntry.
 * Names are matched independent of case, so that "Eric"
 * and "ERIC" are the same names.
 */

public class NameSurferDatabase{
		
	ArrayList<NameSurferEntry> nameSurferSet;
	
	/* Constructor: NameSurferDataBase(filename) */
	public NameSurferDatabase(String filename)
	{
		nameSurferSet = new ArrayList<NameSurferEntry>();
		try 
		{
			BufferedReader br = new BufferedReader(new FileReader(filename));
			String line = "";
			
			while (true)
			{
			  line = br.readLine();
			  
			  if (line == null)
				break;
			 
			  nameSurferSet.add(new NameSurferEntry(line));
			}
			br.close();
		}
		catch(IOException e) {
		}
	}
	
	/* Method: findEntry(name) */
	public NameSurferEntry findEntry (String targetName)
	{
		for (NameSurferEntry e : nameSurferSet)
		{
			{
				if (e.getName().equals(targetName))
				{
					
					return e;
				}
			}
		}
		return null;
	}

	public int getNumRecords() 
	{
		return nameSurferSet.size();
	}
	
}//End of NameSurferDatabase class

