/**
 * Preprocess
 * This class does the preprocessing of the image 
 * 		in preparation for the character recognition
 * 
 * This code was translated and modified from Chris Dahm's License Plate recognition
 * 		git hub project which can be found here: 
 * https://github.com/MicrocontrollersAndMore/OpenCV_3_License_Plate_Recognition_Cpp
 * 
 * Status: Complete but not throughly tested
 * Last update: 04/11/2016
 * @author: Nick Pieros
 * @version: 04/11/2016
 * 
*/

package characterRecognition;

import org.opencv.core.*;
import org.opencv.imgproc.*;
import java.util.*;

public class Preprocess {

	//Matrix to contain the gray scaled image
	private Mat grayscaleImg = new Mat();
	
	//Matrix to contain the thresholded image
	private Mat thresholdImg = new Mat();
	
	/**
	* Constructor method for the Preprocess class.
	* @param: originalImg	-	Matrix containing the image to be processed.
	* @return: None
	* @throws: NullPointerException 	-	If the provided matrix is empty.
	*/
	public Preprocess(Mat originalImg)
	{
		preprocessImage(originalImg);
		
	}
	
	/**
	* Getter method for the gray scale image - returns the original image gray scaled
	* @param: None
	* @return: Mat grayscaleImg	-	returns the matrix containing the gray scaled image
	* @throws: None
	*/
	protected Mat getGrayImg()
	{
		return this.grayscaleImg;
	}
	
	/**
	* Getter method for the threshold image 
	* 		gets the matrix containing the thresholded image.
	* @param: None
	* @return: Mat thresholdImg	-	returns the matrix containing the 
	* 									threshold of the original image
	* @throws: None
	*/
	protected Mat getThreshImg()
	{
		return this.thresholdImg;
	}
	
	/**
	* Method that does the actual preprocessing of the image.
	* 		calls the maxContrast method to get the max contrast of the grayscale image
	* 		as well as the extractValue method to extract the HSV channel values
	* 		to make the image grayscaled.
	* 		After the max contrast grayscale is retrieved a gaussian blur is done
	* 		followed by an adaptive threshold on the result of the gaussian blur.
	* @param: Mat originalImg	-	Matrix containing the image to be processed.
	* @return: None
	* @throws: NullPointerException 	-	If the provided matrix is empty.
	*/	private void preprocessImage(Mat originalImg)
	{
		if(originalImg.empty() == true)
		{
			//throws a null pointer exception if the image matrix is empty
			throw new NullPointerException("Image is empty, please load a valid image");
			
		}
		else
		{
			//size to be used in the blurring process
			Size blurredSize = new Size(3,3);
		
			//maximum color value for pixels that pass the threshold
			double threshMaxVal = 255.0;
			
			//Size of a pixel neighborhood that is used 
			//to calculate a threshold value for the pixel
			int blockSize = 19;
		
			//constant to be used in the formula for calculating the threshold
//see http://docs.opencv.org/2.4/modules/imgproc/doc/miscellaneous_transformations.html
			double constValForThresh = 9.0;
		
			//Matrix that will contain the gray scale image with maximum contrast
			//This will be used to get the threshold image
			Mat grayscaleMaxContrast =new Mat();
		
			//matrix to store the blurred image 
			//This will be used in the thresholding process
			Mat bluredImg = new Mat();
		
			//call the extractValue method to create the 
			//gray scale image from HSV channels
			this.grayscaleImg = extractValue(originalImg);
		
			//maximizes the contrast of the gray scale image
			//This will be used as the source image for the blurring
			grayscaleMaxContrast = getMaxContrast(grayscaleImg);
	
			//Built in OpenCV method to do a Gaussian Blur to the image
			//takes the maximum contrast grayscale as the source and stores
			//the blured image in the matrix bluredImg.
			Imgproc.GaussianBlur(grayscaleMaxContrast, bluredImg, blurredSize, 0);
		
			//Built in OpenCV method to do an addaptive threshold.
			//takes the bluredImg matrix as the source and stores the threshold in the
			//thresholdImg matrix.
			//Uses the gaussian adaptive thresholding method and 
			//generates a binary inverse threshold.
			Imgproc.adaptiveThreshold(bluredImg, this.thresholdImg, threshMaxVal, 
					Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C, Imgproc.THRESH_BINARY_INV, 
					blockSize, constValForThresh);
			
		}
	}
	
	/**
	* Method that extracts the HSV values of the image. 
	* 		This is used to generate the gray scale image.
	* @param: origImg	-	Matrix containing the image to be gray scaled.
	* @return: Mat imgValue	- 	Matrix containing the gray scaled image
	* @throws: None.
	*/
	private Mat extractValue(Mat origImg)
	{
		//Matrix to hold the hue saturation value of the image
		Mat imgHSV= new Mat();
		
		//list to store the HSVs for the image
		ArrayList<Mat> listOfHSVimgs = new ArrayList<Mat>();
		
		//Matrix containing the color values for the image
		Mat imgValue;
		
		//Recolors the image from RGB to HSV
		Imgproc.cvtColor(origImg, imgHSV, Imgproc.COLOR_BGR2HSV); 
	
		//splits the HSV of the image into the array list
		Core.split(imgHSV, listOfHSVimgs); 
		
		//gets the HSV value for the image
		imgValue = listOfHSVimgs.get(2);
		
		return(imgValue);
	}
	
	/**
	* Method that gets the maximum contrast for the provided image
	* @param: Mat grayImg	-	matrix containing the grayscaled image
	* 			to get the maximum contrast for
	* @return: Mat imgGrayAndTopMinusBlack	- 	image containing the 
	* 			maximum contrast gray scale image.
	* @throws: None.
	*/
	private Mat getMaxContrast(Mat grayImg)
	{
		//Matrix to contain the top hat image transformation
		Mat imgTopHat = new Mat();
		
		//Matrix to contain the black hat image transformation
		Mat imgBlackHat = new Mat();
		
		//Matrix to contain the gray scale image matrix plus 
		//						the tophat transformation matrix
		Mat imgGrayAndTop = new Mat();
		
		//Matrix to contain the gray scale image plus tophat transform 
		//						minus the black hat transform
		Mat imgGrayAndTopMinusBlack = new Mat();
		
		//gets the structuring element to do the image transformation
		Mat structuringElement = 
				Imgproc.getStructuringElement(Imgproc.CV_SHAPE_RECT, new Size(3,3));

		//tophat transformation
		Imgproc.morphologyEx(grayImg, imgTopHat, Imgproc.MORPH_TOPHAT, 
				structuringElement);
		
				//blackhat transformation
		Imgproc.morphologyEx(grayImg, imgBlackHat, Imgproc.MORPH_BLACKHAT, 
				structuringElement);
		
		//adding the gray scaled and tophat matrices 
		Core.add(grayImg, imgTopHat, imgGrayAndTop);
		
		//subtracting the blackhat matrix from the sum of the grayscale and tophat matrix
		Core.subtract(imgGrayAndTop,imgBlackHat,imgGrayAndTopMinusBlack);
		
		return imgGrayAndTopMinusBlack;
	}
	
}