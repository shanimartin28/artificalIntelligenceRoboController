package aimaMapPlottingPackage;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import aimaKompaiMapperPackage.mapper;
import aimaOccupancyGridProcessingPackage.OccupancyGridMatrix;
import aimaResources.AimaConstants;

public class ImageProcessing 
{
	
	final static int NUM_ITER = 1500;
	
	public ImageProcessing()
	{
	}
	
	public void createGIFImage(OccupancyGridMatrix[][] occupancyGRID)
	{
		System.out.println("createGIFImage!!");
	    BufferedImage bi;
	    //bi = new BufferedImage(ExecuteKompaiMapper.noOfCols_image, ExecuteKompaiMapper.noOfrows_image, BufferedImage.TYPE_INT_RGB);
	    
	    bi = new BufferedImage(500, 500, BufferedImage.TYPE_INT_RGB);
	    
	    
	    Graphics g = bi.getGraphics();
	    g.setColor(Color.WHITE);
	    
	    /*for(int row = 0;row < ExecuteKompaiMapper.noOfrows_image;row++)
	    {
			String element="";
	    	for(int col = 0; col < ExecuteKompaiMapper.noOfCols_image;col++)
	    	{
	    		OccupancyGridMatrix elementObject=occupancyGRID[row][col];
	    		
	    		if(elementObject.getElement_symbol().equalsIgnoreCase(AimaConstants.P_OCCUPIED_OBJECT_SYMBOL))
	    		{
	    			 g.setColor(Color.BLACK);
	    			 //g.drawLine(row, col, row+1, col + 1);
	    			 g.drawRect(col, row, 
	    					 (int)ExecuteKompaiMapper.element_SquareSide_width_distance, 
	    					 (int)ExecuteKompaiMapper.element_SquareSide_height_distance);
	    		}
	    		else
	    		{
	    			g.setColor(Color.WHITE);
		   			g.drawRect(col, row, 
		   					 (int)ExecuteKompaiMapper.element_SquareSide_width_distance, 
		   					 (int)ExecuteKompaiMapper.element_SquareSide_height_distance);
	    		}
	    		
	    	}
	    }*/
	    
	    System.out.println("ExecuteKompaiMapper.element_SquareSide_width_distance= " +mapper.element_SquareSide_width_distance);
	    System.out.println("ExecuteKompaiMapper.element_SquareSide_height_distance= "+mapper.element_SquareSide_height_distance);
	   
	    int rectWidth = 50;
	    int rectHeight = 50;
	    
	    for(int row = 0 ;row<mapper.noOfrows_image ;row++)
	    {
			String element="";
	    	for(int col = 0 ;col<mapper.noOfCols_image ;col++)
	    	{
	    		OccupancyGridMatrix elementObject=occupancyGRID[row][col];
	    		
	    			/*if(elementObject.getElement_symbol().equalsIgnoreCase(AimaConstants.P_EMPTY_OBJECT_SYMBOL))
		    		{
	    				g.setColor(Color.GRAY);
		    			g.fillRect(row+1, col+1,
		                         rectWidth - 2, rectHeight - 2);
		    		}
	    			else if(elementObject.getElement_symbol().equalsIgnoreCase(AimaConstants.P_START_OBJECT_SYMBOL))
		    		{
		    			System.out.println("P_START_OBJECT_SYMBOL....");
		    			 g.setColor(Color.RED);
		    			 g.fillRect(row+1, col+1,
		                         rectWidth - 2, rectHeight - 2);
		    		}
		    		else
		    		{
		    			System.out.println("P_OCCUPIED_OBJECT_SYMBOL....");
		    			 g.setColor(Color.BLACK);
		    			 g.fillRect(row+1, col+1,
		                         rectWidth - 2, rectHeight - 2);
		    		}*/
	    		
	    		if(elementObject.getElement_symbol().equalsIgnoreCase(AimaConstants.P_EMPTY_OBJECT_SYMBOL))
	    		{
    				g.setColor(Color.GRAY);
	    			g.fillRect(col+1, row+1,
	                         rectWidth, rectHeight);
	    		}
    			else if(elementObject.getElement_symbol().equalsIgnoreCase(AimaConstants.P_START_OBJECT_SYMBOL))
	    		{
	    			//System.out.println("P_START_OBJECT_SYMBOL....");
	    			 g.setColor(Color.RED);
	    			 g.fillRect(col+1, row+1,
	                         rectWidth, rectHeight);
	    		}
	    		else
	    		{
	    			//System.out.println("P_OCCUPIED_OBJECT_SYMBOL....");
	    			 g.setColor(Color.BLACK);
	    			 g.fillRect(col+1, row+1,
	                         rectWidth, rectHeight);
	    		}

	    	}
	    }
	    
	    
	    g.dispose();
	   
	    try 
	    {
			//ImageIO.write(bi, "gif", 
			//		new File("H:/edu/3.ArtificialIntelligenceAndApplications/Workspace/mcs10smn-AIMA(2)/image/map_image.gif"));
		
	    
	
ImageIO.write(bi, "gif", new File(AimaConstants.output_map_path));
		
			System.out.println("Saved the Image!!!");
			System.out.println("mapper.hIMM_15GridElementsDistance = " + mapper.hIMM_15GridElementsDistance);
			
	    } 
	    catch (IOException e) 
		{
	    	System.out.println("Image exception");
			e.printStackTrace();
		}
	}
	
	
}
