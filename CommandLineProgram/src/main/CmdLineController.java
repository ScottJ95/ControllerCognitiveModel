package main;

import java.io.IOException;
import java.util.LinkedList;

import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;

import adt.IdentifiedObject;
import characterRecognition.ContourLocator;
import characterRecognition.GenData;
import characterRecognition.ObjectIdentifier;
import characterRecognition.OutputSender;

/**
 * CMD Line Controller handles all of the main operations for the software.
 * This class is used by the inputParserCmdLine to execute the program.
 * 
 * Status: Finished and working.
 * 
 * @author Scott Jeffery
 * @version 4/30/2016
 *
 */
public class CmdLineController {
	
	private String filePath;
	
	private Mat defaultImage;
	
	private Mat grayScaleImage;
	
	/**
	 * Default constructor.
	 * @param filePath
	 * @param originalImage
	 * @param grayScaleImage
	 * @throws IOException 
	 * @throws ClassCastException 
	 * @throws ClassNotFoundException 
	 */
	protected CmdLineController(String filePath, Mat defaultImage, Mat grayScaleImage) 
	throws IOException, ClassNotFoundException, ClassCastException
	
	{
		
		this.setFilePath(filePath);
		
		this.defaultImage = defaultImage;
		
		this.grayScaleImage = grayScaleImage;
		
		execute();
		
	}
			
	
	
	
	/**
	 * Execute the program.
	 * @throws IOException 
	 * @throws ClassCastException 
	 * @throws ClassNotFoundException 
	 */
	private void execute() throws IOException, ClassNotFoundException, ClassCastException
	{
		
		
		ContourLocator locator = new ContourLocator(grayScaleImage);

		LinkedList<MatOfPoint> contourList = locator.findContours(false);

		ObjectIdentifier identifier = new ObjectIdentifier(contourList);

		LinkedList<IdentifiedObject> foundObjects = identifier.identifyObjects();
		
		GenData generateData = new GenData(foundObjects, defaultImage, false);

		foundObjects = generateData.getIdentifiedObjects();

		/*for (int i = 0; i < foundObjects.size(); i++){

			IdentifiedObject object = foundObjects.get(i);

			System.out.println("Contour # " + i);

			for (int j = 0; j < object.getDataText().length; j++){

				System.out.println(object.getDataText()[j]);
			}
			

		}
		*/
		
		OutputSender sender = new OutputSender();
		   sender.formatArray(generateData.getIdentifiedObjects());
	}



	/**
	 * Get the current file path.
	 * @return file path
	 */
	public String getFilePath() {
		return filePath;
	}



	/**
	 * Set the current file path.
	 * @param filePath
	 */
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}



	/**
	 * Get the default, unchanged, image.
	 * @return
	 */
	public Mat getDefaultImage() {
		return defaultImage;
	}



	/**
	 * Set the default, unchanged, image.
	 * @param defaultImage
	 */
	public void setDefaultImage(Mat defaultImage) {
		this.defaultImage = defaultImage;
	}



	/**
	 * Get the grayScaleImage.
	 * @return
	 */
	public Mat getGrayScaleImage() {
		return grayScaleImage;
	}



	/**
	 * Set the grayScaleImage.
	 * @param grayScaleImage
	 */
	public void setGrayScaleImage(Mat grayScaleImage) {
		this.grayScaleImage = grayScaleImage;
	}
	

}
