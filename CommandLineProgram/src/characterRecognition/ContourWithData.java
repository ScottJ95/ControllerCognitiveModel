package characterRecognition;

import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;

public class ContourWithData
{
    //the contour
	private MatOfPoint contour;
	
	//bounding rectangle for the contour
	private Rect boundingRect;
	
	//xPos of the bounding rectangle
	private int xPos=0;
	
	//y position of the bounding rectangle
	private int yPos=0;
	
	/**
	* Constructor method for ContourWithData
	* @param: MatOfPoint newContour	-	matrix containing the contour
	* @param: newBoundingRect	-	bounding rectangle
	* @return: None
	* @throws: None	-	This method is only used after the threshold image is obtained
	*/
	public ContourWithData(MatOfPoint newContour, Rect newBoundingRect)
	{
		this.contour = newContour;
		this.boundingRect = newBoundingRect;
		xPos=boundingRect.x;
		yPos=boundingRect.y;
	}
	
	/**
	* getter for the upper left x point of the bounding rectangle
	* @param: None
	* @return: int xPos	-	x point for the upper left corner of the bounding rectangle
	* @throws: None
	*/
	public int getXPos()
	{
		return xPos;
	}

	/**
	* getter for the upper left y point of the bounding rectangle
	* @param: None
	* @return: int yPos	-	y point for the upper left corner of the bounding rectangle
	* @throws: None
	*/
	public int getYPos()
	{
		return yPos;
	}
	
	/**
	* getter for the center x point of the bounding rectangle
	* @param: None
	* @return: int centerXPos	-	x point for the centerXPos of the bounding rectangle
	* @throws: None
	*/
	public int getCenterX()
	{
		 return Math.abs(boundingRect.x + boundingRect.width)/2;
		    
	}

	/**
	* getter for the center y point of the bounding rectangle
	* @param: None
	* @return: int centerYPos	-	y point for the centerYPos of the bounding rectangle
	* @throws: None
	*/
	public int getCenterY()
	{
		return Math.abs(boundingRect.y + boundingRect.height)/2;
	}
	

	/**
	* getter for the bounding rectangle
	* @param: None
	* @return: Rect boundingRect	-	bounding rectangle for the contour
	* @throws: None
	*/
	public Rect getBoundingRect()
	{
		return boundingRect;
	}
	

	/**
	* getter for the contour
	* @param: None
	* @return: MatOfPoint contour	-	the contour
	* @throws: None
	*/
	
	public MatOfPoint getContour()
	{
		return contour;
	}
	
}