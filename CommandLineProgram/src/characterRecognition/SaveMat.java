/**
 * saveMat
 * Saves the matrix to the specified path
 * Status: Still working
 * Last Update: 4/26/2016
 * @author Nick Pieros
 * @purpose Saves matrix
 * @version 1.0
 *
 */

package characterRecognition;

import org.opencv.core.*;
import java.io.File;
import java.io.*;
import org.opencv.core.CvType;

public class SaveMat 
{
	/**
	 * Constructor method for saving the matrix
	 * @param String path    -    the file path to save the matrix to 
	 * @param Mat mat    -    matrix to save
	 * @param int type    -    type of the matrix
     * @return none
     * @throws none
	 */
	public SaveMat(String path, Mat mat, int type) 
	{
	   //checking the type of the matrix 
		if(type == CvType.CV_32S)
		{
			saveMatInt(path, mat);
		}
		else if (type == CvType.CV_32FC1)
		{
			saveMatFloat(path,mat);
		}
	}
	
	/**
	 * Method to save integer matrix
	 * @param String path    -    file path to save the matrix
	 * @param Mat mat    -    matrix to save
	 * @throws IOException, ClassCastException
	 */
	public void saveMatInt(String path, Mat mat)
	{
    
        //creating a file at the absolute file path
		File file = new File(path).getAbsoluteFile();
        
        //makes the directory if needed
	    file.getParentFile().mkdirs();
        
	    try 
	    {
            //getting the number of columns for the matrix
	        int cols = mat.cols();
            
            //creating an array to store the data in the matrix
	        int[] data = new int[(int) mat.total() * mat.channels()];
	        
            //getting the data from the matrix
            mat.get(0, 0, data);
	        
            //creating the file
	        try 
	        (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path))) 
	        {
                //saving the number of columns to the file
	            oos.writeObject(cols);
                
                //saving the data from the matrix
	            oos.writeObject(data);
                
                //closing the file
	            oos.close();
	        }
	    } 
	    catch (IOException | ClassCastException ex) 
	    {
	        System.err.println("ERROR: Could not save mat to file: " + path);
	    }
	}
	
	/**
	 * saving float matrix
	 * @param String path    -    the file path to save the matrix to
	 * @param Mat mat    -    matrix to save
	 * @throws IO exception, ClassCastException
	 */
	public void saveMatFloat(String path, Mat mat)
	{
        //creating a file at the absolute file path
		File file = new File(path).getAbsoluteFile();
        
        //makes the directory if needed
	    file.getParentFile().mkdirs();
	    try 
	    {
            //getting the number of columns for the matrix
	        int cols = mat.cols();
            
             //creating an array to store the data in the matrix
	        float[] data = new float[(int) mat.total() * mat.channels()];

            //getting the data from the matrix
	        mat.get(0, 0, data);
	        
            //creating the file
	        try 
	        (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path))) 
	        {
                //saving the number of columns to the file
	            oos.writeObject(cols);
                
                //saving the data from the matrix
	            oos.writeObject(data);
                
                //closing the file
	            oos.close();
	        }
	  
	    }
	    
	    catch (IOException | ClassCastException ex) 
	    {
	        System.err.println("ERROR: Could not save mat to file: " + path);
	    }
	}
	
}
