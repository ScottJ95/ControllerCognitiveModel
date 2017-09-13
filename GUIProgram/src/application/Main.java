/**
 * Name: Main
 * Purpose:Runs the inputParser GUI.
 * Status: complete
 * Last Update: 4/26/2016
 * @author Rithvik Mandalapu
 * @version  4/26/2016
 */

package application;

import org.opencv.core.Core;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.fxml.FXMLLoader;


public class Main extends Application
{
	// the main stage
	private Stage primaryStage;
	
	@Override
	public void start(Stage primaryStage)
	{
		try
		{
			// load the FXML resource
			FXMLLoader loader = new FXMLLoader(getClass().getResource("ccg.fxml"));
			BorderPane root = (BorderPane) loader.load();
			
			// set a whitesmoke background
			root.setStyle("-fx-background-color: whitesmoke;");
			Scene scene = new Scene(root, 600, 600);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			
			// create the stage with the given title and the previously created
			// scene
			this.primaryStage = primaryStage;
			this.primaryStage.setTitle("Controller Cognitive Model");
			this.primaryStage.setScene(scene);
			this.primaryStage.show();
			
			// init the controller
			Controller controller = loader.getController();
			controller.setStage(this.primaryStage);
			controller.init();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args)
	{
		// load the native OpenCV library
		
		System.out.println(System.getProperty("java.library.path"));
		
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		
		launch(args);
	}
}
