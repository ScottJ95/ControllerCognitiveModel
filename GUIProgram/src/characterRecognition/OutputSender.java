/**
 * Name: Output Sender
 * Purpose: Output  class to run test data, and output a XML file. 
 * Status: Completed 
 * Last update: 04/28/2016
 * @author: Rithvik Mandalapu & Brett Seeberger 
 * @purpose Convert data into XML file 
 * @version: 04/28/2016
 */
package characterRecognition;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;

import java.io.File;
import java.util.Collection;
import java.util.LinkedList;

import adt.*;
import adt.ObjectType.ObjType;

public class OutputSender 
{
	private Flights listOfFlights = new Flights();
	
	private Point currCenterPoint = new Point();
	
	private boolean errorFlag = false;

	public void checkarray(LinkedList<IdentifiedObject> test)
	{
		for(int i=0;i<test.size();i++){
			for(int j=0;j<test.get(i).getDataText().length;j++)
				System.out.println("Array Spot" + i + ": " + test.get(i).getDataText()[j]);
		}
	}

	/**
	 * Generates XML file with the input of the list of flights
	 * @param ListOfFlights
	 * @exception JAXBException
	 */
	public void generateXML(Flights ListOfFlights)
	{
		try 
		{
			// Create a string of the file name to output the data to
			 String filename = 
					   System.getProperty("user.dir") + "\\output.xml";	
			File file = new File(filename);

			// instantiate the "JAXBContext" and the "Marshaller" class for XML parsing
			JAXBContext jaxbContext = JAXBContext.newInstance(Flights.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();				
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

			// parse the list of flights into the XML file
			jaxbMarshaller.marshal(ListOfFlights, file);
			jaxbMarshaller.marshal(ListOfFlights, System.out);
		} 

		catch (JAXBException e) 
		{
			e.printStackTrace();
		}
	}

	/**
	 * Formats the array to be used to convert to XML
	 * @param flightdata (LinkedList)
	 * @return LinkedList of flights
	 */
	public void formatArray(LinkedList<IdentifiedObject> flightdata)
	{
		for(int i=0;i<flightdata.size();i++)
		{
			this.errorFlag = false;
			if(flightdata.get(i).getTypeOfObj()== ObjectType.ObjType.FDB)
			{
				System.out.println(flightdata.get(i).getTypeOfObj());
				currCenterPoint = flightdata.get(i).getLocation();
				fulldatablock(flightdata.get(i).getDataText());
			}
			else
			{
				System.out.println(flightdata.get(i).getTypeOfObj());
				currCenterPoint = flightdata.get(i).getLocation();
				limdatablock(flightdata.get(i).getDataText());
			}
		}

		generateXML(listOfFlights);
	}
	
	/**
	 * adds data to list of flights
	 * @param limDB
	 */
	public void limdatablock(String limDB[])
	{
		int size = limDB.length;
		Flight f = new Flight();
		switch(size){
		case 1:			
			//null vals
			break;
		case 2: 
			limDB = lb_remove_excess_len2(limDB).clone();
			limDB = remove_excess_lines(limDB).clone();
			convertLDB(f,limDB);
			f.setErrorFlag(errorFlag);
			f.setCenterLocation(currCenterPoint);
			listOfFlights.add(f);
			System.out.println("after format len 2 LDB:");  printlist();
			break;
		case 3:
			limDB = lb_remove_excess_len3(limDB).clone();
			limDB = remove_excess_lines(limDB).clone();
			convertLDB(f,limDB);
			f.setErrorFlag(errorFlag);
			f.setCenterLocation(currCenterPoint);
			listOfFlights.add(f);
			System.out.println("after format len 2 LDB:");  printlist();
			break;
		default:
			System.out.println("Unable to process");
			break;
		}
	}
	
	private String[] lb_remove_excess_len3(String[] limDB) {
		
		System.out.println("before removing spaces and X: "); printdata(limDB);

		if(limDB[0].isEmpty())
		{
			limDB = remove(limDB,0).clone();
		}
		if(limDB[0].contains(" "))
		{
			limDB[0]=limDB[0].replaceAll("\\s+","");
		}
		if(limDB.length == 3)
		{
			//System.out.println("qrihoet");
			//nulls
		}
		else{
			for(int i = 0; i < limDB.length;i++)
			{
				//fullDB[i]=fullDB[i].replaceAll("\\s+","");
				boolean x = false;
				//fullDB[i]=fullDB[i].replaceAll("\\s+","");
				try{
					if(limDB[i].charAt(0)==('X'))
					{ 
						limDB[i] = limDB[i].substring(1,  limDB[i].length());
					}
					if(limDB[i].charAt(limDB[i].length()-1)==('X'))
					{
						limDB[i] = limDB[i].substring(0,limDB[i].length()-1);
					}	
					x = true;
				}
				catch(StringIndexOutOfBoundsException e){
					this.errorFlag = true;
				}
				
			}
		}
		System.out.println("after removing spaces and X: "); printdata(limDB);
		return limDB;
	}

	private void convertLDB(Flight f, String[] limDB) {
		handle_extra_chars(f,limDB);
		
		//AWE123(callsign)
		//263(Current Alt)
		if(limDB.length == 2)
		{
			f.setCallsign(limDB[0]);
			f.setCurrent_altitute(limDB[1]);
			String alt= null;
			String asc_or_des = null;
			String curralt = null;
			if(limDB[1].length()!=3)
			{
				// null vals 
			}
			else 
			{
				if(limDB[1].contains("C"))
				{
					alt = limDB[1].substring(0, limDB[1].indexOf("C")+1);
					curralt = limDB[1].substring(0, limDB[1].indexOf("C")+1);
					asc_or_des = "At assigned alt";
				}
				else if(limDB[1].length() > 3){
					if(limDB[1].charAt(3)== '5')
					{
						alt = limDB[1].substring(0, limDB[1].indexOf("5")) +"C";
						curralt = limDB[1].substring(0, limDB[1].indexOf("5")) +"C";
						asc_or_des = "At assigned alt";
					}
				}

				else if(limDB[1].charAt(0)=='Y')
				{
					alt = "Y";
				}
				else
				{
					alt = limDB[1].substring(0, 3);
					curralt = alt;
				}
				
				f.setAssigned_altitute(alt);
				f.setAscending_or_descending(asc_or_des);
				f.setCurrent_altitute(curralt);
			}
		}
		else
		{
			// nulls
		}
		
	}

	private String[] lb_remove_excess_len2(String[] limDB) {
		System.out.println();
		return remove_excess_len3or4(limDB);
	}

	/**
	 * adds data to list of flights
	 * @param fullDB
	 */
	public void fulldatablock(String fullDB[])
	{
		int size = fullDB.length;
		Flight f = new Flight();
		switch(size){
		case 3: case 4:			
			fullDB = remove_excess_len3or4(fullDB).clone();
			fullDB = remove_excess_lines(fullDB).clone();
			convertFDB(f,fullDB);
			f.setErrorFlag(errorFlag);
			f.setCenterLocation(currCenterPoint);
			listOfFlights.add(f);
			System.out.println("after format len 3 or 4 FDB:");  printlist();
			break;
		case 6:
			fullDB = remove_excess_len7(fullDB).clone();
			fullDB = remove_excess_lines(fullDB).clone();
			convertFDB(f,fullDB);
			f.setErrorFlag(errorFlag);
			f.setCenterLocation(currCenterPoint);
			listOfFlights.add(f);
			System.out.println("after format len 7 FDB:");  printlist();
			break;
		default:
			System.out.println("Unable to process");
			break;
		}
	}

	
	private void convertFDB(Flight f, String fullDB[]) {
		//fullDB = remove_excess_len3or4(fullDB).clone();
		handle_extra_chars(f,fullDB);
		boolean ytest = false;
		String regex = "[0-9]+";
		for(int i=0; i<fullDB.length;i++)
		{
			if((fullDB[i].length()==1 || fullDB[i]=="Y") && fullDB[i] == regex)
				{
					ytest = true;
					break;
				}				
		}
		if(ytest == false)
		{
			//KLM456(callsign)
			f.setCallsign(fullDB[0]); 

			//270^252(assigned altitude, ascending or descending, current altitude)
			String alt= null;
			String asc_or_des = null;
			String curralt = null;
			char x = '0';
			
			
			try{
				
			
			if(fullDB[1].contains("C"))
			{
				alt = fullDB[1].substring(0, fullDB[1].indexOf("C")+1);
				curralt = fullDB[1].substring(0, fullDB[1].indexOf("C")+1);
				asc_or_des = "At assigned alt";
			}
				
				
			
			else if(x == '5')
			{
				alt = fullDB[1].substring(0, fullDB[1].indexOf("5")) +"C";
				curralt = fullDB[1].substring(0, fullDB[1].indexOf("5")) +"C";
				asc_or_des = "At assigned alt";
			}
			
			else
			{
				alt = fullDB[1].substring(0, 3);
				String temp = fullDB[1].substring(3, 4);
				if(temp.equals("^"))
					asc_or_des = "ASC";
				else 
					asc_or_des = "DSC";
				curralt = fullDB[1].substring(4,fullDB[1].length());
			}
			}
			
			catch(StringIndexOutOfBoundsException e)
			{
				this.errorFlag = true;
			}
			
			f.setAssigned_altitute(alt);
			f.setAscending_or_descending(asc_or_des);
			f.setCurrent_altitute(curralt);


			//R525 450(computer generated ID, current ground speed in nautical miles)
			String id = null;
			String gspeed = null;
			try
			   {
			    id = fullDB[2].substring(0, fullDB[2].indexOf(" "));
			    gspeed = fullDB[2].substring(fullDB[2].indexOf(" "), fullDB[2].length()).trim(); 
			   }
			   catch(StringIndexOutOfBoundsException e)
			   {
			    id = fullDB[2];
			    gspeed = fullDB[2];
			    this.errorFlag = true;
			   }
			   catch(ArrayIndexOutOfBoundsException e)
				{
				   this.errorFlag = true;
			   }
			f.setId(id);
			f.setGround_speed(gspeed);
		}
		else
		{
			System.out.println("QH");
		}
		
	}
	
	private String[] remove_excess_lines(String[] DB){
		  
		  if(DB[0].contains("-"))
		  {
		   DB = remove(DB,0);
		  }
		  return DB;
		  
		 }
	
	private String[] remove_excess_len3or4(String[] fullDB) {
		//String[] temp = null;
		System.out.println("before removing spaces and X: "); printdata(fullDB);
		
		if(fullDB[0].isEmpty())
		{
			fullDB = remove(fullDB,0).clone();
		}
		if(fullDB[0].contains(" "))
		{
			fullDB[0]=fullDB[0].replaceAll("\\s+","");
		}
		for(int i = 0; i < fullDB.length;i++)
		{
			boolean x = false;
			//fullDB[i]=fullDB[i].replaceAll("\\s+","");
			try{
				if(fullDB[i].charAt(0)==('X'))
				{ 
					fullDB[i] = fullDB[i].substring(1,  fullDB[i].length());
				}
				if(fullDB[i].charAt(fullDB[i].length()-1)==('X'))
				{
					fullDB[i] = fullDB[i].substring(0,fullDB[i].length()-1);
				}	
				x = true;
			}
			catch(StringIndexOutOfBoundsException e){
				this.errorFlag = true;
			}
			finally{
				if(x == true)
				{
					// do nothing
				}
				else 
				{
					
				}
			}
			
		}
		System.out.println("after removing spaces and X: "); printdata(fullDB);
		return fullDB;
	}

	private  String[] remove_excess_len7(String[] fullDB) {
		//String[] temp = null;
		if(fullDB[0] == "")
		{			
			fullDB = remove(fullDB,0).clone();
		}
		for(int i=0;i<fullDB.length;i++)
		{
			if(testsame(fullDB[i])==true)
			{
				fullDB = remove(fullDB,i).clone();
				i--;
			}
		}
		if(fullDB[0].contains(" "))
		{
			fullDB[0]=fullDB[0].replaceAll("\\s+","");
		}
		
		for(int i = 0; i < fullDB.length;i++)
		{
			try
			{
				if(fullDB[i].charAt(0)==('X'))
				{ 
					fullDB[i] = fullDB[i].substring(1,  fullDB[i].length());
					fullDB[i] = fullDB[i].trim();
					i--;
				}
				if(fullDB[i].charAt(fullDB[i].length()-1)==('X'))
				{
					fullDB[i] = fullDB[i].substring(0,fullDB[i].length()-1);
					fullDB[i] = fullDB[i].trim();
					i--;				
				}		
			}
			catch(StringIndexOutOfBoundsException e){
				this.errorFlag = true;
			}
			finally{
				// donothing
			}
		}
		System.out.println("\nafter removing spaces and X: "); printdata(fullDB);
		return fullDB;
	}

	private static String[] remove(String[] temp,int i) {

		String[] n = new String[temp.length - 1];
		System.arraycopy(temp, 0, n, 0, i );
		System.arraycopy(temp, i+1, n, i, temp.length - i-1);
		return n;
	}
		
	private static boolean testsame(String fullDB)
	{
		fullDB=fullDB.replaceAll("\\s+","");
		for(int i = 0; i < fullDB.length(); i++)
	    {
	        if(fullDB.charAt(i) != fullDB.charAt(0))
	            return false;
	    }

	    return true;
	}
	
	/**
	 * Keeps track of the "extra characters" : "<",">", and "&"
	 * Adds to string extrachars in Flight class 
	 * @param Flight f
	 * @param String[] DB (array of data)
	 */
	private void handle_extra_chars(Flight f, String[] DB) {
		for(int i = 0; i<DB.length;i++)
		{
			if(DB[i].contains("&"))
			{
				if(f.getExtrachars() == null)
					f.setExtrachars("& at index " + DB[i].indexOf("&") + " of " +  spot(i));
				else
					f.setExtrachars(f.getExtrachars() + ", " + "& at index " + DB[i].indexOf("&") + " of " +  spot(i));
				DB[i] = DB[i].replaceAll("&", "");
			}
			if(DB[i].contains("<"))
			{	
				if(f.getExtrachars() == null)
					f.setExtrachars("< at index " + DB[i].indexOf("<") + " of " +  spot(i));
				else
					f.setExtrachars(f.getExtrachars() + ", " + "< at index " + DB[i].indexOf("<") + " of " +  spot(i));
				DB[i] = DB[i].replaceAll("<", "");
			}
			if(DB[i].contains(">"))
			{
				if(f.getExtrachars() == null)
					f.setExtrachars("> at index " + DB[i].indexOf(">") + " of " +  spot(i));
				else
					f.setExtrachars(f.getExtrachars() +  ", " + "> at index " + DB[i].indexOf(">") + " of " +  spot(i));
				DB[i] = DB[i].replaceAll(">", "");
			}

		}


	}

	//KLM456		callsign
	//270 up 252	assigned altitude, ascending or descending, current altitude
	//R525 450		computer generated ID, current ground speed in nautical miles

	/**
	 * returns the string containing what each array index corresponds to 
	 * @param i
	 * @return String
	 */
	private String spot(int i) {
		if(i==0)
			return "callsign";
		else if(i==1)
			return "assigned altitude";
		else if(i==2)
			return "ascending or descending";
		else if(i==3)
			return "current altitude";
		else if(i==4)
			return "ID";
		else if(i==5)
			return "ground speed";
		else
			//
			return null;
	}


	private void printlist() 
	{
		for(int i =0; i<listOfFlights.size(); i++){
			System.out.println(listOfFlights.getFlights().get(i).toString());}
	}
	public static void printdata(String flightdata[])
	{
		for(int i = 0; i < flightdata.length;i++)
			System.out.println(i+ ":" + flightdata[i]);
	}
}