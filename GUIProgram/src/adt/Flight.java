/**
 * Name: Flight
 * Purpose: Holds the data of each Flight
 * Status: Completed 
 * Last update: 04/12/2016
 * @author: Rithvik Mandalapu
 * @version: 4/12/2016
*/

package adt;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.opencv.core.Point;


@XmlRootElement( name = "Aircraft" )
@XmlType(propOrder={"callsign","id","current_altitute","assigned_altitute",
					"ascending_or_descending","ground_speed", 
					"centerLocation", "extrachars", "errorFlag"})
public class Flight 
{
	// data of each flight 
	private String id;
	private String callsign;
	private String current_altitute;
	private String assigned_altitute;
	private String ascending_or_descending;
	private String ground_speed;
	private String extrachars;
	private Point centerLocation;
	private boolean errorFlag;

	
	public Flight() {
		this.id = null;
		this.callsign = null;
		this.current_altitute = null;
		this.assigned_altitute = null;
		this.ascending_or_descending = null;
		this.ground_speed = null;
		this.extrachars = null;
		this.centerLocation = null;
		this.setErrorFlag(false);
	}
	
	
	public Flight(String id, String callsign, String current_altitute, String assigned_altitute,
			String ascending_or_descending, String ground_speed, String extrachars, Point centerLocation, boolean errorFlag) {
		this.id = id;
		this.callsign = callsign;
		this.current_altitute = current_altitute;
		this.assigned_altitute = assigned_altitute;
		this.ascending_or_descending = ascending_or_descending;
		this.ground_speed = ground_speed;
		this.extrachars = extrachars;
		this.centerLocation = centerLocation;
		this.setErrorFlag(errorFlag);
	}

	////////////////////////Setters///////////////////////////////
	/**
	 * Sets value of callsign
	 * @param callsign
	 */
	@XmlElement( name = "Callsign" )
	public void setCallsign(String callsign) 
	{
		this.callsign = callsign;
	}

	/**
	 * Sets value of ground speed
	 * @param ground_speed
	 */
	@XmlElement( name = "Ground Speed" )
	public void setGround_speed(String ground_speed) 
	{
		this.ground_speed = ground_speed;
	}

	/**
	 * @param current_altitute the current_altitute to set
	 */
	@XmlElement( name = "Current Altitude" )
	public void setCurrent_altitute(String current_altitute) {
		this.current_altitute = current_altitute;
	}

	/**
	 * @param assigned_altitute the assigned_altitute to set
	 */
	@XmlElement( name = "Assigned Altitute" )
	public void setAssigned_altitute(String assigned_altitute) {
		this.assigned_altitute = assigned_altitute;
	}

	/**
	 * @param ascending_or_descending the ascending_or_descending to set
	 */
	@XmlElement( name = "Ascending or Descending" )
	public void setAscending_or_descending(String ascending_or_descending) {
		this.ascending_or_descending = ascending_or_descending;
	}

	/**
	 * @param id the id to set
	 */
	@XmlElement( name = "ID" )
	public void setId(String id) {
		this.id = id;
	}
	

	/**
	 * @param extrachars the extrachars to set
	 */
	@XmlElement( name = "Extra Characters" )
	public void setExtrachars(String extrachars) {
		this.extrachars = extrachars;
	}
	
	/**
	 * 
	 * @param centerLocation
	 */
	@XmlElement( name = "Center Location")
	public void setCenterLocation(Point centerLocation) {
		this.centerLocation = centerLocation;
	}
	
	/**
	 * 
	 * @param errorFlag
	 */
	@XmlElement( name = "Possible Error")
	public void setErrorFlag(boolean errorFlag) {
		this.errorFlag = errorFlag;
	}
	
	////////////////////////GETTERS///////////////////////////////
	/**
	 * @return the ascending_or_descending
	 */
	public String getAscending_or_descending() {
		return ascending_or_descending;
	}
	/**
	 * Return callsign
	 * @return callsign
	 */
	public String getCallsign() 
	{
		return callsign;
	}
	/**
	 * Return ground speed
	 * @return ground_speed
	 */
	public String getGround_speed() 
	{
		return ground_speed;
	}

	/**
	 * @return the current_altitute
	 */
	public String getCurrent_altitute() {
		return current_altitute;
	}
	/**
	 * @return the assigned_altitute
	 */
	public String getAssigned_altitute() {
		return assigned_altitute;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @return the extrachars
	 */
	public String getExtrachars() {
		return extrachars;
	}
	/**
	 * 
	 * @return center point
	 */
	public Point getCenterLocation() {
		return centerLocation;
	}
	
	/**
	 * 
	 * @return error flag
	 */
	public boolean isErrorFlag() {
		return errorFlag;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Flight [id=" + id + ", callsign=" + callsign + ", current_altitute=" + current_altitute
				+ ", assigned_altitute=" + assigned_altitute + ", ascending_or_descending=" + ascending_or_descending
				+ ", ground_speed=" + ground_speed + "\n extra chars= " + extrachars + "]";
	}

	
	


	


	
	
}
