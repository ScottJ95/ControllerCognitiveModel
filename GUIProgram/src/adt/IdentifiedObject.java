package adt;
import java.util.Arrays;

import org.opencv.core.Point;

import adt.ObjectType.ObjType;

import org.opencv.core.MatOfPoint;

public class IdentifiedObject 
{

	Point location;//The location of the object.Point is an x,y coordinate pair in OpenCV

	ObjType typeOfObj;//The identified type of the radar object
	
	MatOfPoint contour = new MatOfPoint();//The matrix of contour
	
	String[]dataText;//String array containing the lines of data read off the radar image 
	                 //for a particular element

	
	/**
	 * Default Constructor 
	 */
	public IdentifiedObject() 
	{
		this.location = null;
		this.typeOfObj = null;
		this.dataText = null;
	}
	
	/**
	 * Constructor
	 * @param location
	 * @param typeOfObj
	 * @param dataText
	 */
	public IdentifiedObject(Point location, ObjType typeOfObj,MatOfPoint contour) 
	{
		this.location = location;
		this.typeOfObj = typeOfObj;
		this.contour = contour;
	}

	/**
	 * Return the location of the point 
	 * @return the location
	 */
	public Point getLocation() 
	{
		return location;
	}

	/**
	 * set the location of the point
	 * @param location the location to set
	 */
	public void setLocation(Point location) 
	{
		this.location = location;
	}

	/**
	 * get the object type 
	 * @return the typeOfObj
	 */
	public ObjType getTypeOfObj() 
	{
		return typeOfObj;
	}

	/**
	 * set the object type
	 * @param typeOfObj the typeOfObj to set
	 */
	public void setTypeOfObj(ObjType typeOfObj) 
	{
		this.typeOfObj = typeOfObj;
	}
	

	/**
	 * return the array of data
	 * @return the dataText
	 */
	public String[] getDataText() 
	{
		return dataText;
	}

	/**
	 * set the array of data
	 * @param dataText the dataText to set
	 */
	public void setDataText(String[] dataText) 
	{
		this.dataText = dataText;
	}
	
	public MatOfPoint getContour()
	{
		return contour;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() 
	{
		return "IdentifiedObject location=" + location + ", typeOfObj=" + typeOfObj 
				+ ", dataText=" + Arrays.toString(dataText)+"\n";
	}
	
	
	
	
}