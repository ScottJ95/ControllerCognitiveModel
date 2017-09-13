package main;

/**
 * Load Image Class is used for loading images for debugging purposes.
 * 
 * @author Scott Jeffery & Nick Pieros
 * @version 4/30/2016
 */
import javax.swing.*;

 
public class LoadImage extends JFrame
{  
	
	ImageIcon image;
	JLabel label1;
	JFrame frame = new JFrame("My GUI");
	public LoadImage(String imgStr)
	{
		//Imgcodecs.imwrite(imgStr,m);
		//frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frame.setResizable(true);
		frame.setLocationRelativeTo(null);
		
		// Inserts the image icon
		this.image = new ImageIcon(imgStr);
		image.getImage().flush();
		frame.setSize(image.getIconWidth()+10,image.getIconHeight()+35);
		// Draw the Image data into the BufferedImage
		JLabel label1 = new JLabel(image);
		//frame.getContentPane().add(label1);
		
		JScrollPane scroller = new JScrollPane(label1);// or add the "imageLabel"
		frame.add(scroller);
		
		frame.setDefaultCloseOperation(frame.DISPOSE_ON_CLOSE);
 
		frame.validate();
		frame.pack();
		frame.setVisible(true);} 
	
	
	public void update()
	{
		image.getImage().flush();
		frame.setSize(image.getIconWidth()+10,image.getIconHeight()+35);
		frame.validate();
		frame.pack();
		frame.setVisible(true);
		
	} 
	
	
		
	}




