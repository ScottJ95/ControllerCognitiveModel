/**
 * TrainAndTest
 * This class does Character recognition of the data blocks
 * 
 * This code was translated and modified from Chris Dahm's KNN character recognition
 * 		git hub project which can be found here: 
 * https://github.com/MicrocontrollersAndMore/OpenCV_3_KNN_Character_Recognition_Cpp
 * 
 * Status: Complete but not throughly tested
 * Last update: 04/24/2016
 * @author: Nick Pieros
 * @version: 04/24/2016
 * 
*/
package characterRecognition;

import org.opencv.core.*;
import org.opencv.imgproc.*;
import org.opencv.ml.*;
import java.io.IOException;
import adt.IdentifiedObject;
import java.util.*;


public class GenData {

	//minimum area of the character
	private static final int MIN_CONTOUR_AREA = 0;
	
	private static final int MAX_CONTOUR_AREA = 59;
	
	//maximum area of the bounding rectangle
	private static final int MAX_BOUNDINGRECT_AREA = 6;

	//maximum highet of the bounding rectangle
	private static final int MAX_BOUNDINGRECT_HEIGHT = 12;
	
	//maximum width of the bounding rectangle
	private static final int MAX_BOUNDINGRECT_WIDTH = 8;
	
	//width in pixels of resized image
	private static final int RESIZED_IMAGE_WIDTH = 20;
	
	//height in pixels of resized image
	private static final int RESIZED_IMAGE_HEIGHT = 30;

	//comparator that is used to compare contours to order them
	//first compares them by the y position of their center point then by the x position
	private static Comparator<ContourWithData> comparator = 
			Comparator.comparingInt(ContourWithData::getCenterY).thenComparing
				(Comparator.comparingInt(ContourWithData::getCenterX));
	
	//matrix of the thresholded image
	private Mat matThresh = new Mat();
	   
	//creating a new LoadMatrix class that will be used to load the matrices
	private LoadMat matrixLoader = new LoadMat();

	//path of the classifications file

	private String classificationsPath = 
			System.getProperty("user.dir") + "\\classifications2.txt";
		    
	//path of the trainingImage file
	private String trainingImagesPath = System.getProperty("user.dir") + "\\images2.txt";

	//list of identified objects
	private LinkedList<IdentifiedObject> partialIdentifiedObjects = new LinkedList<IdentifiedObject>();
	
	//image to write to if in debug mode
	private Mat debugImage = new Mat();
	
	
	private Mat originalImg = new Mat();
	
	/*
	 * KNearest class that will be used to determine the characters
	 * 	using the K-Nearest Neighbors algorithm
	 * KNearest is trained using the matrix of training character images
	 * 	 and their ASCII character value
	 * After it is trained an image containing an unknown character is given
	 * 	to the KNN object. Upon receiving an image it tries to find the closest
	 * 	match to the provided image using the images it was trained on
	 */
	private KNearest knn = KNearest.create();
	
	private String output = "";

	
	/**
	* Constructor method for the TrainAndTest class.
	* @param: LinkedList<IdentifiedObject> listOfObjects	-	list of partially identified objects
	* @param: Mat img	-	Matrix of the image
	* @return: None
	* @throws: IOException, ClasNotFoundException, ClassCastException 	- if the training data could not be laoded
	*/
		
	public GenData(LinkedList<IdentifiedObject> listOfObjects, Mat img) throws IOException, ClassNotFoundException, ClassCastException
	{		    
	    //loading the classifications
	    Mat classifications = matrixLoader.loadMatInt( classificationsPath);
	    
	    //loading the training images
	    Mat trainingImages = matrixLoader.loadMatFloat(trainingImagesPath);
	    
	    this.partialIdentifiedObjects=listOfObjects;
	    
	    this.originalImg=img;
	    
	    //read the text from the image
		this.readFromImage(trainingImages, classifications, partialIdentifiedObjects, originalImg, false);
	}
	
	
	/**
	* Constructor method for the TrainAndTest class.
	*	takes an aditional boolean to determine if we're in test mode or not
	* @param: LinkedList<IdentifiedObject> listOfObjects	-	list of partially identified objects
	* @param: Mat img	-	Matrix of the image
	* @param: boolean debugMode	-	true or false value to determine if it is debug mode
	*									true when we want to run debug mode, false otherwise
	* @return: None
	* @throws: IOException, ClasNotFoundException, ClassCastException 	- if the training data could not be laoded
	*/
		
	public GenData(LinkedList<IdentifiedObject> listOfObjects, Mat img, boolean debugMode) 
			throws IOException, ClassNotFoundException, ClassCastException
	{		    
	    //loading the classifications
	    Mat classifications = matrixLoader.loadMatInt( classificationsPath);
	    
	    //loading the training images
	    Mat trainingImages = matrixLoader.loadMatFloat(trainingImagesPath);
	    
	    this.partialIdentifiedObjects=listOfObjects;
	    
	    this.originalImg=img;
	    
	    //read the text from the image
		this.readFromImage(trainingImages, classifications, partialIdentifiedObjects, originalImg, debugMode);
	}
	
	/**
	* getter method for the threshold matrix.
	* @param: None
	* @return: Mat matTresh	-	matrix containing the threshold image
	* @throws: None
	*/

	private Mat getMatThresh() {
		return matThresh;
	}

	/**
	* setter method for the threshold matrix.
	* @param: Mat matThresh	-	matrix containing the threshold image to be used
	* @return: None
	* @throws: None	-	This method is only used after the threshold image is obtained
	*/

	private void setMatThresh(Mat matThresh)
	{
		this.matThresh = matThresh;
	}
	
	/**
	* getter method for the list of identifed objects.
	* @param: None
	* @return: LinkedList<IdentifiedObject> 	-	list of identified objects
	* @throws: None	-	This method is only used after the threshold image is obtained
	*/
	public LinkedList<IdentifiedObject> getIdentifiedObjects()
	{
		return partialIdentifiedObjects;
	}
	
	
	/**
	* Method that is called to read the image
	* 	Drives the other methods
	* @param: Mat trainingImages	-	training images for the KNN
	* @param: Mat classifications	-	matrix of classification values for the training
	* 										images to be used in the KNN
	* @param: boolean debugMode		-	boolean value for determining if we're running
										debug mode. true if we are, false otherwise
	* @param: Mat currentImage	-	current image being read
	* @return: None
	* @throws: None
	*/
	private void readFromImage(Mat trainingImages, Mat classifications, 
			LinkedList<IdentifiedObject> listOfObjects, Mat img, boolean debugMode)
	{
		
		//training the KNN with the training images and classifications
		this.knn.train(trainingImages, Ml.ROW_SAMPLE, classifications);
		
		int size = listOfObjects.size();
		
		//if we're in debug mode clone the original image
		if(debugMode)
		{
			debugImage = img.clone();
		}
		
		for(int i=0; i< size; i++)
		{
			MatOfPoint currentContour = listOfObjects.get(i).getContour();
			
			Rect boundingRec = Imgproc.boundingRect(currentContour);
			
			Mat currentImage = img.submat(boundingRec);
			
			ContourWithData currContour = new ContourWithData(currentContour, boundingRec);
			
			//if we're in debug mode draw a rectangle around the current contour
			if(debugMode)
			{
				//getting the points to draw the rectangle
				Point upperLeft = new Point(currContour.getXPos(), currContour.getYPos());
		    	Point lowerRight = new Point(currContour.getXPos() + boundingRec.width, currContour.getYPos() + boundingRec.height);
				
				
				// draw a red rect around the current contour
		    	Imgproc.rectangle(debugImage,
								upperLeft,
		        				lowerRight,
								new Scalar(0, 0, 255),//red
								2);// thickness			
			}
			//obtaining the list of contours
			LinkedList<MatOfPoint> listOfPointContours = 
					this.setUpImage(currentImage);
			
			//obtaining the valid contours
			LinkedList<ContourWithData> listOfValidContours = 
				this.getValidContours(listOfPointContours);
		
			//detecting the characters
			String[] output = this.detectChars(listOfValidContours, debugMode);
		
			//saving the output
			listOfObjects.get(i).setDataText(output);
		}
	}
	
	/**
	* Method that gets the threshold image and the list of point contours
	* 	uses the Preprocess class
	* @param: Mat originalImage	-	image being used
	* @return: LinkedList<MatOfPoint> pointContours		
	* 	list of point contours from the image. 
	* 	These are the characters that are going to be read
	* @throws: None
	*/

	private LinkedList<MatOfPoint> setUpImage(Mat originalImage)
	{
		//preprocess object that will get the grayscale and threshold of the image
		Preprocess preProcessor = new Preprocess(originalImage);
		
		//matrix to hold the threshold image
		Mat tmpThresh = new Mat();
		Mat copyTmpThresh = new Mat();
		
		//getting the threshold of the image
		tmpThresh= preProcessor.getGrayImg();
		
		//saving the threshold image as it will be modified in a second
		this.setMatThresh(tmpThresh);
		copyTmpThresh = tmpThresh.clone();
	    
		//linked list to store the contours of the characters
		LinkedList<MatOfPoint> pointContours = new LinkedList<MatOfPoint>();
		
	    //matrix to contain the hierarchy 
	    //not used but needed for the findContours method
	    Mat contoursHierarchy = new Mat();

	    //OpenCV method to find the contours in the image
	    //these contours will be the characters to be detected
	
	    Imgproc.findContours(copyTmpThresh,
	        pointContours,                        
	        contoursHierarchy,                          
	        Imgproc.RETR_EXTERNAL,                      
	        Imgproc.CHAIN_APPROX_SIMPLE);
	    
	    return pointContours;
	}

	
	/**
	* Method that gets the valid point contours based on their area
	* @param: LinkedList<MatOfPoint> pointContours	-	list of contours in the image
	* @return: LinkedList<ContourWithData> validPointContours	- list of contours
	* 																that are valid
	* @throws: None
	*/
	private LinkedList<ContourWithData> getValidContours
											(LinkedList<MatOfPoint> pointContours)
	{
		//list of valid point contours to be filled
		LinkedList<ContourWithData> validPointContours
										= new LinkedList<ContourWithData>();
	    //size of the list of contours detected
	    int size = pointContours.size();
	    
	    //looping through the list of contours
	    for (int i = 0; i < size; i++) 
	    {               
	    	//current contour to be checked
	        MatOfPoint currContour = pointContours.get(i);                                                    
	        
	        //checking to see if the current contour has an area larger than the 
	        //minimum allowed area
	        double area = Imgproc.contourArea(currContour);
	        if (area>= MIN_CONTOUR_AREA && area <MAX_CONTOUR_AREA)	       
	        {
	        	//bounding rectangle for the current contour
	        	Rect boundingRect = Imgproc.boundingRect(currContour);
	        	
	        	//creating a ContourWithData object to store the contour 
	        	//	and some other information
	        	ContourWithData newContour = 
	        			new ContourWithData(currContour, boundingRect);
	        	
	        	//adding the contour to the list of valid contours
	        	//this will be the list that the characters are read from
	        	validPointContours.add(newContour);
	  	    }//end if
	    }//end for
	    
	    //sorts the list of possible characters
	    validPointContours.sort(comparator);
	    
	    return validPointContours;
	    
	}
	
	/**
	* Method that determines the characters in the image
	* @param: LinkedList<ContourWithData> validContours	- list of contours that are valid
	* @return: String outputString	-	characters that were read
	* @throws: None
	*/
	
	private String[] detectChars(LinkedList<ContourWithData> validContours, boolean debug)
	{
		String [] outputList;
	    
	    //gets the size of the list
	    int size = validContours.size();
	    
	    //output string to be used to store the text read
	    String output = "";
	    
	    //center position of the current character
	    //stored to help detect line breaks
	    int currCenterY = validContours.get(0).getCenterY();
	    int currentCenterX = validContours.get(0).getCenterX();
	    //looping through the list of valid contours
	    for (int i = 0; i < size; i++)
		{
		
			//getting the current contour
			ContourWithData currContour = validContours.get(i);
	    	
			//creating the bounding rectangle
			Rect boundingRect = currContour.getBoundingRect();
	    	
			//getting the area of the bounding rectangle
			double area = boundingRect.area();
			
			//getting the height of the bounding rectangle
	    	int boundingRectHeight = boundingRect.height;
			
			//getting the widht of the bounding rectangle
	    	int boundingRectWidth = boundingRect.width;
			
	    	if(area > MAX_BOUNDINGRECT_AREA && boundingRectHeight<MAX_BOUNDINGRECT_HEIGHT && boundingRectWidth < MAX_BOUNDINGRECT_WIDTH)
	    	{
			
				
	    		//getting the center y axis point for the character
	    		int centerY = currContour.getCenterY();
	    	
				int centerX = currContour.getCenterX();
	    			  
				//matrix that stores only the part of the original image 
				//	that is in the bounding rectangle
		        Mat matROI = this.getMatThresh().submat(boundingRect);
	
		        //matrix to store the resized region of interest
		        Mat matROIResized = new Mat();
				
				// resize image, this will be more consistent for recognition and storage
				Imgproc.resize(matROI, matROIResized, new Size(RESIZED_IMAGE_WIDTH, RESIZED_IMAGE_HEIGHT));
			
				//matrix to store the current image as a float matrix
		        Mat matROIFloat = new Mat();
	        
		        //converting the current image to a float matrix
		        matROIResized.convertTo(matROIFloat, CvType.CV_32FC1);

		        //reshaping the mat
		        Mat matROIFlattenedFloat = matROIFloat.reshape(1, 1);

		        //setting up the matrix to store the value of
		        //	the detected character
		        Mat matCurrentChar = new Mat(0, 0, CvType.CV_32F);
	        
		        //calling the KNN object to attempt to detect the current character
		        knn.findNearest(matROIFlattenedFloat, 1, matCurrentChar);

	    	    //getting the value of the current character from the matrix
	        	//needs to be an array due to the return type of the get method for matrices
	        	double [] curChar = matCurrentChar.get(0, 0);
	        
	        	//converting the value to a float
	        	float fltCurrentChar = (float)curChar[0];
	        
	        	
				//checking to see if the character is X
				//a lot of the dashed lines on the radar image get picked up as X so we try to filter
				//them out as much as possible
	        	if(!((int)(fltCurrentChar) == 'X' && boundingRectHeight <11 && boundingRectWidth < 7))
	        	{
				
					//drawing the rectangles for the characters if it's in debug mode
					if(debug)
					{	
						//getting the points to draw the rectangle
						Point upperLeft = new Point(currContour.getXPos(), currContour.getYPos());
		    			Point lowerRight = new Point(currContour.getXPos() + boundingRect.width, currContour.getYPos() + boundingRect.height);
				
				
						// draw a red rect around the current contour
		    			Imgproc.rectangle(debugImage,
										upperLeft,
		        						lowerRight,
										new Scalar(0, 0, 255),//red
										2);// thickness			
					}//end if
	    	
				
	        	
					//check to see if the character is on a new line
	        		if(currCenterY + 6 < centerY)
	        		{
	        			output = output + "\n";
	        			currCenterY = centerY;
	        		}
					
					//check to see if there is enough space between the two characters to be a space
	        		else if(currentCenterX+7<centerX)
	        		{
	        			output= output + " ";
	        		}
	        	
	        		//appending the current character to the output string
	        		output = output + (char)((int)(fltCurrentChar));
					
	        		currentCenterX = centerX;
	    		
	        	}//end if
	    	}//end if
		}//end for
	    		outputList = output.split("\n");
	    		return outputList;
		
	}//end method


	public Mat getDebugImage() {
		return debugImage;
	}


	public void setDebugImage(Mat debugImage) {
		this.debugImage = debugImage;
	}
	
}//end class