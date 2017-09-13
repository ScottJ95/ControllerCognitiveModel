package characterRecognition;

/**
 * ObjectIdentifier Class
 *
 * ObjectIdentifer takes in the key points that were sent by the ObjectLocator and uses 
 * those key points to identify the types of objects in those key points.
 * 
 * Object types can be specified as a Full Data Block (FDB), a Limited Data Block 
 * (LDB), the plane or feature of the plane (PLANE), or OTHER.
 * 
 * Once the object type has been identified, that information will be passed to the
 * DataParser to parse the information associated with that object.
 * 
 * This class assumes that the template image is in the same file location
 * of the application.
 * 
 * Status: In progress. Needs testing for confirmation.
 * 
 * Last update: 26 Apr. 2016
 * 
 * @author: Scott Jeffery
 * @version: 04.26.2016
*/

import java.io.IOException;
import java.util.LinkedList;
import java.util.HashSet;

import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import adt.IdentifiedObject;
import adt.ObjectType;





public class ObjectIdentifier 

{
	
	private LinkedList<MatOfPoint> contourList; 
	
	private LinkedList <IdentifiedObject> foundObjects;
	
	private LinkedList<Integer> indexesOfLDB; //Index locations of LDBs
	
 	private LinkedList<Integer> indexesOfFDB; //Index locations of FDBs.
	
	
	private String templateImage = System.getProperty("user.dir") + "\\tmp-2.png";
	

	/**
	 * Default constructor.
	 * Parameters are set to null.
	 * 
	 * @param pointsList list of points of be identified
	 */
	public ObjectIdentifier()
	
	{
		this.contourList = null;
		
		this.indexesOfFDB = new LinkedList<Integer>();
		
		this.indexesOfLDB = new LinkedList<Integer>();
		
		this.foundObjects = new LinkedList<IdentifiedObject>();
		
	}
	
	/**
	 * More specific constructor that takes in the list of key points as well as the
	 * path name for the image. 
	 * 
	 * @param pointsList list of points of be identified
	 * @param pathName   path name of image to find objects.
	 */
	public ObjectIdentifier(LinkedList<MatOfPoint> contourList)
	{
	
		this.contourList = contourList;
		
		this.indexesOfFDB = new LinkedList<Integer>();
		
		this.indexesOfLDB = new LinkedList<Integer>();
		
		this.foundObjects = new LinkedList<IdentifiedObject>();
		
	}
	
	/**
	 * Identifies the objects and places them into a list of IdentifiedObjects. 
	 * Each IdentifiedOject is also associated with an ObjectType, which defines what
	 * information is held by that object.
	 *  
	 * 
	 * @throws IOException 
	 * @return LinkedList of IdentifiedObjects.
	 * 
	 */
	public LinkedList <IdentifiedObject> identifyObjects() throws IOException
	
	{
		ContourLocator tempLocator = new ContourLocator();
		
		LinkedList<MatOfPoint> templateContours = tempLocator.findContours(templateImage,false);
		
		identifyLDB(templateContours);
		
		identifyFDB(templateContours);
		
		return this.foundObjects;
						
	}
	
	/**
	 * Constructor that takes an image of contours to draw as a display version.
	 * @param contourImage the image with the drawn contours.
	 * @return list of IdentifiedObjects.
	 * @throws IOException
	 */
	public LinkedList <IdentifiedObject> identifyObjects(Mat contourImage) throws IOException{
		
		ContourLocator tempLocator = new ContourLocator();
		
		LinkedList<MatOfPoint> templateContours = tempLocator.findContours(templateImage,false);
		
		identifyLDB(templateContours);
		
		identifyFDB(templateContours);
		
		String writePath = System.getProperty("user.dir") + "\\identifyResult.png";
		
		drawIdentifiedContours(contourImage, writePath);
		
		return this.foundObjects;
		
		
	}
		

	/**
	 * This is a test method that will draw the identified contours of the passed image
	 * and write that drawn contours to the writePath.
	 * 
	 * @param Mat contourImage : an image with drawn contours.
	 * @param String writePath
	 * @return true if the contours were able to be drawn. Otherwise, false.
	 * @throws IOException 
	 */
	public void drawIdentifiedContours(Mat contourImage, String writePath) throws IOException{
		
		
		
		Scalar white = new Scalar(255,0,0);
		
		for(int index = 0; index <= indexesOfLDB.size() - 1; index++){
			
			Imgproc.drawContours(contourImage, contourList, indexesOfLDB.get(index), white);
			
		}
		
		for(int index = 0; index <= indexesOfFDB.size() -1 ; index++){
			
			Imgproc.drawContours(contourImage, contourList, indexesOfFDB.get(index), white);
			
		}
		
		Imgcodecs.imwrite(writePath, contourImage);
		
		LoadImage loader = new LoadImage(writePath);
		
		
	}
	
	
	
	/**
	 * Identifies the full data blocks in the image and adds them to
	 * the corressponding list.
	 * @param templateContours the contours to match against.
	 */
	private void identifyFDB(LinkedList<MatOfPoint> templateContours) {
		
		int[] fdbTemplateIndex = new int[7];
		
		HashSet<Integer> indexSet = new HashSet<Integer>();
		
		//Set up template contours.
		
		fdbTemplateIndex[0] = 1;
		fdbTemplateIndex[1] = 2;
		fdbTemplateIndex[2] = 3;
		fdbTemplateIndex[3] = 6;
		fdbTemplateIndex[4] = 8;
		fdbTemplateIndex[5] = 11;
		fdbTemplateIndex[6] = 18;
		
		for (int tempCount = 0; tempCount < fdbTemplateIndex.length; tempCount++)
			
		{
			
			//System.out.println("Template Index: " + tempCount);
			Mat templateContour = templateContours.get(fdbTemplateIndex[tempCount]);
			
			double templateArea = Imgproc.contourArea(templateContour);
			
			//Get the template contour as well as its area.
			
			for (int contCount = 0; contCount < contourList.size() -1; contCount++)
			
			{
				
				//For each contour.
				
				double result = Imgproc.matchShapes(contourList.get(contCount), 
													templateContour, 1, 0.0);
				
				double areaResult = Imgproc.contourArea(contourList.get(contCount));
				
				//Get the shape match result and the area result.
				
				//System.out.println("Area of testee: " + areaResult + " ");
				
				//System.out.println("Index " + contCount + " result: " + result);
				
				//Above lines used for debugging.
				
				double areaSubtract = templateArea - areaResult;
				
				//System.out.println("Area difference " + areaSubtract);
				
				//Check the result of the shape matching and area difference.
				
				if (result <= 0.75 && areaSubtract < 1034 && areaSubtract > -1500)
				
				{
					//System.out.println("Index " + contCount + " is a match.");
					if(!indexSet.contains(contCount))
					{
						//Set up the identified object and add the contour to the set.
						
						//System.out.println(contCount + " is not in the set!");
						
						
						
						Point center = new Point();
						
						float[] radius = new float[1];
						
						MatOfPoint2f circleMat = new MatOfPoint2f();
						
						circleMat.fromList(contourList.get(contCount).toList()); 
						
						Imgproc.minEnclosingCircle(circleMat, center, radius);
						
						//Get the min enclosing circle for this contour to get the center
						
						IdentifiedObject FDBObject = new IdentifiedObject(center, 
							  ObjectType.ObjType.FDB, contourList.get(contCount));
						
						foundObjects.add(FDBObject);
						
						//Set up the found object.
					
						indexSet.add(contCount);
						indexesOfFDB.add(contCount);
						
						//Add to the set and to the index list.
						
					}
					
					else
					{
						//System.out.println(contCount + " Is already in the set!");
						
					}
					 //Add that contour index to the list.
					//Might set up identified objects here instead.
				}
			}
		}
		
	}
		

	/**
	 * Identifies the limited data blocks in the image and adds them to the 
	 * corresponding list.
	 * @param templateContours the contours to match against.
	 */
	private void identifyLDB(LinkedList<MatOfPoint> templateContours) 
	{
		
		//System.out.println("LDB Start");
		
		
		HashSet <Integer> indexSet = new HashSet<Integer>();
	
		int[] ldbTemplateIndex = new int[2]; //Index of templates for templateImage.
		
		ldbTemplateIndex[0] = 22; //Set template indexes.
		ldbTemplateIndex[1] = 4;
		
		//For each template
		for (int tempCount = 0; tempCount < ldbTemplateIndex.length; tempCount++)
		
		{
			Mat templateContour = templateContours.get(ldbTemplateIndex[tempCount]);
			
			double templateArea = Imgproc.contourArea(templateContour);
			
			//Get the template contour as well as its area.
			
			for (int contCount = 0; contCount < contourList.size() -1; contCount++)
			
			{
				
				//For each contour.
				
				double result = Imgproc.matchShapes(contourList.get(contCount), 
													templateContour, 1, 0.0);
				
				double areaResult = Imgproc.contourArea(contourList.get(contCount));
				
				//Get the shape match result and the area result.
				
				//System.out.println("Area of testee: " + areaResult + " ");
				
				//System.out.println("Index " + contCount + " result: " + result);
				
				//Above lines used for debugging.
				
				double areaSubtract = templateArea - areaResult;
				
				//System.out.println("Area difference " + areaSubtract);
				
				//Check the result of the shape matching and area difference.
				
				if (result <= 0.75 && areaSubtract < 710 && areaSubtract > -1500)
				
				{
					//System.out.println("Index " + contCount + " is a match.");
					if(!indexSet.contains(contCount))
					{
						//Set up the identified object and add the contour to the set.
						
						//System.out.println(contCount + " is not in the set!");
						
						
						
						Point center = new Point();
						
						float[] radius = new float[1];
						
						MatOfPoint2f circleMat = new MatOfPoint2f();
						
						circleMat.fromList(contourList.get(contCount).toList()); 
						
						Imgproc.minEnclosingCircle(circleMat, center, radius);
						
						//Get the min enclosing circle for this contour to get the center
						
						IdentifiedObject LDBObject = new IdentifiedObject(center, 
								  ObjectType.ObjType.LDB, contourList.get(contCount));
						
						foundObjects.add(LDBObject);
						
						//Set up the found object.
					
						indexSet.add(contCount);
						indexesOfLDB.add(contCount);
						
						//Add to the set and to the index list.
						
					}
					
					else
					{
						//System.out.println(contCount + " Is already in the set!");
						
					}
				}
			}
		}
		
	}

	/**
	 * Get the list of indexes of Limited Data Blocks found.
	 * @return  indexes of LDBs.
	 */
	public LinkedList<Integer> getIndexesOfLDB() 
	{
		return indexesOfLDB;
	}
	
	/**
	 * Setter for the list of LDB indexes.
	 * @param indexesOfLDB
	 */
	public void setIndexesOfLDB(LinkedList<Integer> indexesOfLDB)
	{
		this.indexesOfLDB = indexesOfLDB;
	}

	/**
	 * Get the indexes of the Full Data Blocks found.
	 * @return indexes of FDBs.
	 */
	public LinkedList<Integer> getIndexesOfFDB() 
	{
		return indexesOfFDB;
	}

	/**
	 * Setter for the list of FBD indexes.
	 * @param indexesOfFDB
	 */
	public void setIndexesOfFDB(LinkedList<Integer> indexesOfFDB) 
	{
		this.indexesOfFDB = indexesOfFDB;
	}

	/**
	 * Get the contour list for the current image.
	 * @return list of MatOfPoint contours.
	 */
	public LinkedList<MatOfPoint> getContourList() 
	{
		return contourList;
	}

	/**
	 * Setter for the image contour list.
	 * @param contourList
	 */
	public void setContourList(LinkedList<MatOfPoint> contourList)
	{
		this.contourList = contourList;
	}

	/**
	 * Get the list of the identified objects.
	 * @return list of identified objects.
	 */
	public LinkedList <IdentifiedObject> getFoundObjects() 
	{
		return foundObjects;
	}

	/**
	 * Setter for the list of identified objects.
	 * @param foundObjects
	 */
	public void setFoundObjects(LinkedList <IdentifiedObject> foundObjects) 
	{
		this.foundObjects = foundObjects;
	}
	
	
}
