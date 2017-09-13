/**
 * ContourLocator Class
 *
 * ContourLocator finds the locations of contours in a particular image.
 * The methods defined in this class handle all operations when finding contours
 * in a specified image.
 * 
 * Status: Code finished, needs testing and exception handling.
 * 
 * Last update: Apr. 26 2016
 * 
 * @author: Scott Jeffery & Stephen Pham
 * @version: 04.26.2016
*/
package characterRecognition;

import java.io.IOException;
import java.util.LinkedList;

import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import main.LoadImage;

import org.opencv.core.Scalar;

public class ContourLocator 

{
	
	private LinkedList<MatOfPoint> foundContours; //The list of contours when found
	
	private Mat drawnContours; //The matrix containing the image with the drawn contours
	
	private Mat image;
	
	/**
	 * Default constructor. Sets all variables to null.
	 */
	public ContourLocator()
	{
		foundContours = null;
		drawnContours = null;
	    setImage(null);
		
	}
	
	/**
	 * Constructor that specifies a pathname for the image.
	 * @param pathName
	 */
	public ContourLocator(Mat image)
	{
	    foundContours = null;
	    drawnContours = null;
	    this.image = image; 
		
		
	}
	
	/**
	 * findContours finds and then draws the contours in a specified image.
	 * The image path name is assumed to be the absolute path name of the image.
	 * If the pathname is null, then the method will return null.
	 * 
	 * 
	 * 
	 * @author Scott Jeffery
	 * 
	 * @param imgPathName the absolute pathname of the image.
	 * @param displayResult if the result wants to be displayed.
	 * @return the found list if contours.
	 * @throws IOException
	 */
	public LinkedList<MatOfPoint> findContours(String imgPathName, boolean displayResult) throws IOException 
	{
			
		if(imgPathName == null)
			{
				return null;
			}
			
			else
		{
		
		
		//I'm wondering if this method should return the list instead.
		
		//Load the image as a grayscale
		
		Mat initialImage = Imgcodecs.imread(imgPathName, 
						   Imgcodecs.CV_LOAD_IMAGE_GRAYSCALE);
		
		Mat threshImage = new Mat();
		
		//The following lines will black out the top buttons of the image.
		
		//These are errors of the image that we do not care about.
		
		Rect roi = new Rect(0,0,1210,83); //ROI of top buttons
		
		Rect roi2 = new Rect(773,60,118,30);//ROI of top middle rectangle thing.
		
		Mat  roiMat= new Mat(initialImage, roi);
		
		Mat roiMat2 = new Mat(initialImage, roi2);
		
		Scalar black = new Scalar(0,0,0);
		
		roiMat2.setTo(black);
		
		roiMat.setTo(black);
		
		//Image after applied threshold.
			
		
		/*
		 * This is important. This applies a THRESHOLD to the image in order it to detect 
		 * a specific range of brightness.
		 * 
		 * This threshold is also used by the blob detection. Here's how it works:
		 * 
		 * Image: The image we want to apply the thresholds to.
		 * threshImage: The output image after the thresholds are applied.
		 * 
		 * minvalue: The minimum threshold value.
		 * maxvalue: The maximum threshold value. 
		 * 			  THIS SHOULD ALWAYS BE 255 FOR A GRAYSCALE IMAGE.
		 * 
		 * method: The method of thresholding.
		 * Reccommmend either 0 (default) or THRESH_BINARY. 
		 */
		
		double ret,  //Ret and thresh are both initalized here.
			   thresh = Imgproc.threshold(initialImage, threshImage, 105, 255, Imgproc.THRESH_BINARY);
		
		Mat hierarchy = new Mat(); //We need this for later.
		
		Mat blurredImage = new Mat(); //The image after we blurred it.
		
		Size size = new Size(19,19); //Size used for blurring.
		
		Imgproc.GaussianBlur(threshImage, blurredImage, size, 0);
		
		//If sigma is 0 for the above line, it is auto generated based on size.
		
		LinkedList<MatOfPoint> contours = new LinkedList<MatOfPoint>();
		
		//LinkedList MatOfPoint is what is returned from findContours.
		
		Imgproc.findContours(blurredImage, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
			
		
		if(displayResult == true) //If we want, we can display the results of the locator
			{
			
				Scalar color = new Scalar(255,0,0);
			
				Imgproc.drawContours(blurredImage, contours, -1, color);
			
				String writeFile = System.getProperty("user.dir") + "\\locateResult.png";
				
				Imgcodecs.imwrite(writeFile, blurredImage);
			
				LoadImage loader = new LoadImage(writeFile);
			
			}
		
		setDrawnContours(blurredImage);
		
		setContours(contours);
		
		return this.foundContours;
		}
		
		
	}
	
	/**
	 * Find contours using the image specified in the locator. 
	 * If null, the method will return null.
	 * @param imgPathName
	 * @return the found list of contours.
	 * @throws IOException
	 */
	public LinkedList<MatOfPoint> findContours(boolean displayResult) throws IOException 
	{
		if(this.image.empty())
		{
			return null;
		}
		
		else
			
		{
		
		//I'm wondering if this method should return the list instead.
		
		//Load the image as a grayscale
		
		Mat initialImage = this.image;
		
		Mat threshImage = new Mat();
		
		//The following lines will black out the top buttons of the image.
		
		//These are errors of the image that we do not care about.
		
		Rect roi = new Rect(0,0,1210,83); //ROI of top buttons
		
		Rect roi2 = new Rect(773,60,118,30);//ROI of top middle rectangle thing.
		
		Mat  roiMat= new Mat(initialImage, roi);
		
		Mat roiMat2 = new Mat(initialImage, roi2);
		
		Scalar black = new Scalar(0,0,0);
		
		roiMat2.setTo(black);
		
		roiMat.setTo(black);
		
		//Image after applied threshold.
			
		
		/*
		 * This is important. This applies a THRESHOLD to the image in order it to detect 
		 * a specific range of brightness.
		 * 
		 * This threshold is also used by the blob detection. Here's how it works:
		 * 
		 * Image: The image we want to apply the thresholds to.
		 * threshImage: The output image after the thresholds are applied.
		 * 
		 * minvalue: The minimum threshold value.
		 * maxvalue: The maximum threshold value. 
		 * 			  THIS SHOULD ALWAYS BE 255 FOR A GRAYSCALE IMAGE.
		 * 
		 * method: The method of thresholding.
		 * Reccommmend either 0 (default) or THRESH_BINARY. 
		 */
		
		double ret,  //Ret and thresh are both initalized here.
			   thresh = Imgproc.threshold(initialImage, threshImage, 105, 255, Imgproc.THRESH_BINARY);
		
		Mat hierarchy = new Mat(); //We need this for later.
		
		Mat blurredImage = new Mat(); //The image after we blurred it.
		
		Size size = new Size(19,19); //Size used for blurring.
		
		Imgproc.GaussianBlur(threshImage, blurredImage, size, 0);
		
		//If sigma is 0 for the above line, it is auto generated based on size.
		
		LinkedList<MatOfPoint> contours = new LinkedList<MatOfPoint>();
		
		//LinkedList MatOfPoint is what is returned from findContours.
		
		Imgproc.findContours(blurredImage, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
			
	
		if(displayResult == true) //If we want, we can display the results of the locator
		{
		
			Scalar color = new Scalar(255,0,0);
		
			Imgproc.drawContours(blurredImage, contours, -1, color);
		
			String writeFile = System.getProperty("user.dir") + "\\locateResult.png";
			
			Imgcodecs.imwrite(writeFile, blurredImage);
		
			LoadImage loader = new LoadImage(writeFile);
		
		}
		
		
		setDrawnContours(blurredImage);
			
		setContours(contours);
		
		
		
		return this.foundContours;
		}
		
	}


	/**
	 * Get the found list of contours.
	 * @return list of contours. Returns null if contours were never found.
	 */
	public LinkedList<MatOfPoint> getContours() 
	{
		return foundContours;
	}


	/**
	 * Set the found list of contours to a specific list of contours.
	 * @param contours LikedList of new contour values.
	 */
	public void setContours(LinkedList<MatOfPoint> contours) 
	{
		this.foundContours = contours;
	}

	/**
	 * Returns the Matrix containing the image with the drawn contours.
	 * @return image Matrix of drawn contours.
	 */
	public Mat getDrawnContours() 
	{
		return drawnContours;
	}


	/**
	 * Sets the current image Matrix containing the image with drawn contours
	 * to a different image Matrix.
	 * 
	 * @param Mat drawnContours
	 */
	public void setDrawnContours(Mat drawnContours) 
	{
		this.drawnContours = drawnContours;
	}

	/**
	 * Gets the current pathname of the image.
	 * @return pathname of image
	 */
	public Mat getImage() 
	{
		return this.image;
	}

	/**
	 * Sets the current pathname of the image.
	 * @param pathName
	 */
	public void setImage(Mat image) 
	{
		this.image = image;
	}
	
	

}