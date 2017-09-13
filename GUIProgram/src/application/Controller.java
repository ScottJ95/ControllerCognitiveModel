/**
 * Name: Controller
 * 
 * Purpose: The controller class is the main driver for the Java FX GUI used by the software.
 * This class handles all of the input and output information as well as
 * calls all of the other methods to perform their tasks.
 * 
 * Status: Completed, currently testing.
 * 
 * Last update: 04/26/2016
 * @author  Scott Jeffery & Rithvik Mandalapu
 * @version: 04/26/2016
*/

package application;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Optional;

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfPoint;
import org.opencv.imgcodecs.Imgcodecs;

import adt.IdentifiedObject;
import characterRecognition.*;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;


public class Controller
{
	// images to show in the view
	@FXML
	private ImageView originalImage;
	
	// the main stage
	
	private Stage stage;
	
	// the JavaFX file chooser
	
	private FileChooser fileChooser;
	
	// support variables
	
	private Mat image;
	
	private Mat grayScaleImage;
	
	//Java FX buttons
	
	@FXML
	private Button runbtn;
	
	@FXML
	private Button debugbtn;
	
	@FXML
	private Button loadbtn;

	
	/**
	 * Initialize the instance variables.
	 */
	protected void init()
	{
		this.fileChooser = new FileChooser();
		this.image = new Mat();
		this.grayScaleImage = new Mat();
		
	
	}
	
	/**
	 * Continue button runs the software and produces the final output.
	 * @throws IOException 
	 * @throws ClassCastException 
	 * @throws ClassNotFoundException 
	 */
	@FXML
	protected void runbtn() throws IOException, ClassNotFoundException, ClassCastException
	{
		
		if(image.empty())
		{
			
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("No Image");
			alert.setContentText("No Image has been loaded!");

			alert.showAndWait();
			
		}
		
		else
		
		{
		
			this.debugbtn.setDisable(true);

			this.loadbtn.setDisable(true);


			ContourLocator locator = new ContourLocator(grayScaleImage);

			LinkedList<MatOfPoint> contourList = locator.findContours(false);

			ObjectIdentifier identifier = new ObjectIdentifier(contourList);

			LinkedList<IdentifiedObject> foundObjects = identifier.identifyObjects();

			//identifier.drawIdentifiedContours(locator.getDrawnContours(), "E:/allTest.png");


			GenData generateData = new GenData(foundObjects, image, false);

			foundObjects = generateData.getIdentifiedObjects();

			for (int i = 0; i < foundObjects.size(); i++){

				IdentifiedObject object = foundObjects.get(i);

				System.out.println("Contour # " + i);

				for (int j = 0; j < object.getDataText().length; j++){

					System.out.println(object.getDataText()[j]);
				}



			}
			
			OutputSender sender = new OutputSender();
			   sender.checkarray(generateData.getIdentifiedObjects());
			   //x.printdata(generateData.getIdentifiedObjects().get(9).getDataText());
			   //x.limdatablock(generateData.getIdentifiedObjects().get(12).getDataText());
			   sender.formatArray(generateData.getIdentifiedObjects());



		}
		
		Alert alert4 = new Alert(AlertType.INFORMATION);
		alert4.setTitle("Completed");
		alert4.setHeaderText("Complete!");
		alert4.setContentText("Software Operations Completed. Check the XML File.");
		alert4.showAndWait();
		
		this.debugbtn.setDisable(false);
		
		this.loadbtn.setDisable(false);
		
	}
	/**
	 * Debug button will run the software and display the results of each step. 
	 * @throws IOException 
	 * @throws ClassCastException 
	 * @throws ClassNotFoundException 
	 * 
	 */
	@FXML
	protected void debugbtn() throws IOException, ClassNotFoundException, ClassCastException
	{
		//Disable other buttons
		
		this.runbtn.setDisable(true);
		
		this.loadbtn.setDisable(true);
		
		
		if(image.empty())
		{
			
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("No Image");
			alert.setContentText("No Image has been loaded!");

			alert.showAndWait();
			
		}
		
		else
		{
		
			boolean quit = false;
			
			int state = 1;
			
			//As long as the user wants to continue
			
			while(quit != true)
				
				
			
			{
				
				//Switch the current state.
				
				switch(state)
				{
				
				case 1:
					ContourLocator locator = new ContourLocator(grayScaleImage);

					LinkedList<MatOfPoint> contourList = locator.findContours(true);
				
					Alert alert1 = new Alert(AlertType.CONFIRMATION);
					alert1.setTitle("Step 1 Complete");
					alert1.setHeaderText("Contour Locator Complete");
					alert1.setContentText("Initial Contour Location Results Are Shown. "
						            + "Continue With Next Step?");
					Optional<ButtonType> result1 = alert1.showAndWait();
					if (result1.get() == ButtonType.OK)
					{
						// ... user chose OK 
						
						state = 2;
					} 
					else 
					{
						quit = true;
					}
						// ... user chose CANCEL or closed the dialog
					break;
					
				case 2:
					
					ContourLocator locator2 = new ContourLocator(grayScaleImage);
				
					LinkedList<MatOfPoint> contourList2 = locator2.findContours(false);

					ObjectIdentifier identifier = new ObjectIdentifier(contourList2);
				

					identifier.identifyObjects(locator2.getDrawnContours());
				
					Alert alert2 = new Alert(AlertType.CONFIRMATION);
					alert2.setTitle("Step 2 Complete");
					alert2.setHeaderText("Object Identification Complete");
					alert2.setContentText("Object Identifiation Results Shown." + 
									"Continue With Next Step?");

					Optional<ButtonType> result2 = alert2.showAndWait();
					if (result2.get() == ButtonType.OK)
					{
						
						
						state = 3;
						
					} 
			
					else
					{
						quit = true;
					}
					break;
					
				case 3:
					
					ContourLocator locator3 = new ContourLocator(grayScaleImage);
					
					LinkedList<MatOfPoint> contourList3 = locator3.findContours(false);

					ObjectIdentifier identifier2 = new ObjectIdentifier(contourList3);
			
					LinkedList<IdentifiedObject> foundObjects = identifier2.identifyObjects();
					
					GenData generateData = new GenData(foundObjects, image, true);
					
					String writePath = System.getProperty("user.dir") + "\\characterResult.png";
					
					Imgcodecs.imwrite(writePath, generateData.getDebugImage());
					
					LoadImage loader = new LoadImage(writePath);
					
					
					Alert alert3 = new Alert(AlertType.CONFIRMATION);
					alert3.setTitle("Step 3 Complete");
					alert3.setHeaderText("Character Recognition Complete");
					alert3.setContentText("Character Recognition Results Shown." + 
									"Continue With Next Step?");

					Optional<ButtonType> result3 = alert3.showAndWait();
					if (result3.get() == ButtonType.OK)
					{
						state = 4;
					
					} 
			
					else
					{
						quit = true;
					
					}
					
					break;
					
				case 4:
					runbtn();
					quit = true;
					break;
				default:
					break;
				
				}
				
			}	
		
		}
	
			
		
		this.runbtn.setDisable(false);
		
		this.loadbtn.setDisable(false);
	}
	
	
	/**
	 * Closes the application
	 */
	@FXML
	protected void cancelapp()
	{
		stage.close();
	}
	/**
	 * Load an image from disk
	 */
	@FXML
	protected void loadImage()
	{
		 // Set extension filter
        FileChooser.ExtensionFilter extFilter = 
                new FileChooser.ExtensionFilter("PNG files (*.png)", "*.png", "JPG files (*.jpg)", "*.jpg", "TIF files (*.tif)", "*.tif");
        fileChooser.getExtensionFilters().add(extFilter);		
		
		// show the open dialog window
		File file = this.fileChooser.showOpenDialog(this.stage);
		if (file != null)
		{
			
			// read the image in gray scale
			this.image = Imgcodecs.imread(file.getAbsolutePath(), Imgcodecs.CV_LOAD_IMAGE_UNCHANGED);
			this.grayScaleImage = Imgcodecs.imread(file.getAbsolutePath(), Imgcodecs.CV_LOAD_IMAGE_GRAYSCALE);
			// show the image
			this.originalImage.setImage(this.mat2Image(this.image));
			// set a fixed width
			this.originalImage.setFitWidth(500);
			// preserve image ratio
			this.originalImage.setPreserveRatio(true);

			
			
			
		}
	}
	
	/**
	 * Set the current stage (needed for the FileChooser modal window)
	 * @param stage
	 */
	public void setStage(Stage stage)
	{
		this.stage = stage;
	}
	
	/**
	 * Convert a Mat object (OpenCV) in the corresponding Image for JavaFX
	 * @param frame
	 * @return the Image to show
	 */
	private Image mat2Image(Mat frame)
	{
		// create a temporary buffer
		MatOfByte buffer = new MatOfByte();
		// encode the frame in the buffer, according to the PNG format
		Imgcodecs.imencode(".png", frame, buffer);
		// build and return an Image created from the image encoded in the
		// buffer
		return new Image(new ByteArrayInputStream(buffer.toArray()));
	}
}