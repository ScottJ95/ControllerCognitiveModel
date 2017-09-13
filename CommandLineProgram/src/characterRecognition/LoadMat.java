/**
 * LoadMat
 * Loads a Matrix from file path
 * Status: Still working
 * Last Update: 4/26/2016
 * @author Nick Pieros
 * @purpose Loads Matrix
 * @version 1.0
 *
 */

package characterRecognition;

import org.opencv.core.*;
import java.io.*;

public class LoadMat 
{
	
	/**
	* Constructor for the loadMat class
	* @param: None
	* @return: None
	* @throws: None
	*/
	public LoadMat()
	{
		
	}
	
	
	/**
	 * method to load integer matrix
	 * @param String path	-	file path to load from
	 * @return Mat mat	-	mat that was loaded
	 * @return null
	 * @throws IOException | ClassNotFoundException | ClassCastException
	 */
	public final Mat loadMatInt(String path) 
	{
	    try 
	    {
			//number of columns for the matrix
	        int cols;
			
			//data for the matrix
	        int [] data;
	        
			//trying to open the file
	        try 
	        (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path))) 
	        {
				//reading in the number of columns
	        	cols = (int) ois.readObject();
				
				//reading the data from the matrix
	            data = (int []) ois.readObject();
	        }
			
			//creating the matrix to return
	        Mat mat = new Mat(data.length / cols, cols, CvType.CV_32S);
	        
			//filling the matrix with the data read
			mat.put(0, 0, data);
			
	        return mat;
	    } 
	    
	    catch (IOException | ClassNotFoundException | ClassCastException e) 
	    {
	        System.out.println(e.getMessage());
	    }
	    
	    return null;
	}
	
	/**
	 * Load Matrix of type Float
	 * @param String path	-	file path to load from
	 * @return Mat mat	-	matrix that was loaded
	 * @return null
	 * @throws IOException | ClassNotFoundException | ClassCastException
	 */
	public final Mat loadMatFloat(String path) 
	{
	    try 
	    {	
			//number of columns
	        int cols;
			
			//data to be read
	        float [] data;
	        
			//opening the file
	        try 
	        (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path))) 
	        {
				//reading the number of columns
	        	cols = (int) ois.readObject();
				
				//reading the data
	            data = (float []) ois.readObject();
	        }
	        
			//mat to store the read in information
	        Mat mat = new Mat(data.length / cols, cols, CvType.CV_32FC1);
			
			//storing the read in information
	        mat.put(0, 0, data);
	        return mat;
	    } 
	    
	    catch (IOException | ClassNotFoundException | ClassCastException e) 
	    {
	        System.out.println(e.getMessage());
	    }
	    
	    return null;
	}
	
}
