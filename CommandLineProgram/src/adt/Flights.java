/**
 * Name: Flights
 * Purpose: Holds a list of instances of each Flight object
 * Status: Completed 
 * Last update: 04/12/2016
 * @author: Rithvik Mandalapu
 * @version: 04/12/2016
*/

package adt;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;




@XmlRootElement( name = "Aircraft List" )
public class Flights 
{

	ArrayList<Flight> flights;


	/**
	 * Adds flights to the flights list 
	 * @param flight
	 */
	public void add(Flight flight )
	{
		if( this.flights == null )
		{
			this.flights = new ArrayList<Flight>();
		}
			this.flights.add( flight );
	}

	/**
	 * Returns flights List
	 */
	public ArrayList<Flight> getFlights() 
	{
		return flights;
	}
	
	public int size()
	{
		return flights.size();
	}
	
	/**
	 * Sets the list of Flights 
	 * @param flight
	 */
	@XmlElement( name = "Aircraft" )
	public void setFlights( ArrayList<Flight> flight )
	{
		this.flights = flight;
	}


	
	
	
}
