/**
 * Name: InputParser
 * Purpose: Receive an input file from the user non GUI implementation 
 * Status: complete
 * Last Update: 4/26/2016
 * @author Bryan Monks & Rithvik Mandalapu
 * @version  4/26/2016
 */
package main;

import java.util.*;
import java.io.*;
import org.opencv.core.*;
import org.opencv.imgcodecs.*;
//imcodecs.imread

public class inputParserCmdLine 
{
	
	
	private static String fileName = null; //FileName for image.
	
	/**
	 * Main method that executes the application.
	 * @param args
	 * @throws IOException 
	 * @throws ClassCastException 
	 * @throws ClassNotFoundException 
	 */
	public static void main(String[] args) throws ClassNotFoundException, ClassCastException, IOException
	{
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		
		Scanner scan = null;
		Mat image; 
		String temp = null;
		
		boolean quit = false;
		
		try
		{
			System.out.println("Please input the file path for the image.");
			System.out.println("Or type 'QUIT' to quit.");
			scan = new Scanner(System.in);
			
			
			//Keep asking the user for a filename until a correct filename is given, 
			//or an error is thrown
			
			while(fileName == null && quit == false)
			{
				System.out.println("Enter the image location "
						+	"'C:\\location\\inputfile.jpg': ");
				
				//temp = "C:"+ File.separator + "Users" + File.separator+ "manda_000"
				//		+ File.separator +"Pictures" + File.separator +"tmp-18.png";
				
				temp = scan.next();
				
				if(temp != null)
				{
					
					if(temp == "QUIT")
					{
						System.exit(0);
					}
					
					
					//Check if the filename is an image file in the desired format
					else if(temp.endsWith(".png") || temp.endsWith(".jpg") || temp.endsWith(".tif"))
						{
						
							//Check if the filename given is an actual file
							Scanner checker = new Scanner(new BufferedReader(new FileReader(temp)));
								
							fileName = temp;
						}
					
					else
					{
						System.out.println("Incorrect filetype. Please try again.");
					}
				}		
				else
				{
					System.out.println("No file path was specified.");
				}
			}
			//convert image to a matrix
			
			Mat defaultImage = Imgcodecs.imread(fileName, Imgcodecs.CV_LOAD_IMAGE_UNCHANGED);
			
			Mat grayScaleImage = Imgcodecs.imread(fileName,Imgcodecs.CV_LOAD_IMAGE_GRAYSCALE);				
			
			CmdLineController controller = new CmdLineController(fileName, defaultImage, grayScaleImage);
			
			System.out.println("Done. Check generated XML file for results.");
		}

		// Catch exceptions
		catch (NoSuchElementException | FileNotFoundException e )
		{
			System.err.println("Not a valid input file.\n" + e.getMessage());
			System.exit(1);
		}


		
	}	
	



}