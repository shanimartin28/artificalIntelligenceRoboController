/*
 * @author mcs10smn
 * Department Of Computing Science
 */

package aimaOccupancyGridProcessingPackage;

import java.awt.Color;
import java.util.ArrayList;

import aimaLocalizationPackage.PositionLocalizer;
import aimaResources.AimaConstants;

public class OccupancyGridAlgm 
{
	public static int noOfCols = 0; 
	public static int noOfrows = 0; 
	public double element_SquareSide_width_distance= 0.0;
	public double element_SquareSide_height_distance= 0.0;
	public double x1_lower_left_coordinate=0.0; 
	public double y1_lower_left_coordinate= 0.0; 
	public double x2_top_right_coordinate= 0.0; 
	public double y2_top_right_coordinate= 0.0; 
	
	public static double RobotStartXPos=0.0;
	public static double RobotStartYPos=0.0;
	
	
	public char[][] occupancy_grid;
	public static OccupancyGridMatrix[][] occupancy_grid_probability_matrix;
	public static ArrayList<OccupancyGridMatrix>occupancy_grid_probability_matrix_arrayList=new ArrayList<OccupancyGridMatrix>();
	public static OccupancyGridMatrix startRobotPos_ElementObject=null;
	
	
	
	public OccupancyGridAlgm(int noOfrows,int noOfCols,
			double SquareSide_width_distance,double SquareSide_height_distance,
			double x1_low, double y1_low, double x2_top, double x3_top)
	{
		this.noOfrows=noOfrows;
		this.noOfCols=noOfCols;
		this.element_SquareSide_width_distance=SquareSide_width_distance;
		this.element_SquareSide_height_distance=SquareSide_height_distance;
		this.x1_lower_left_coordinate=x1_low;
		this.y1_lower_left_coordinate=y1_low;
		this.x2_top_right_coordinate=x2_top;
		this.y2_top_right_coordinate=x3_top;
		
	}
	
	public void createOccupancyGrid()
	{
		// Step 1: Initialize the Occupancy Grid.
		// The grid is an array of size noOfrows x noOfCols, 
		// with 2 grid elements per unit of distance
		occupancy_grid_probability_matrix = new OccupancyGridMatrix[noOfrows][noOfCols];
		
		//Step 2: An element has equal chances of being occupied
		//or empty. This translates to P(Occupied) = P(Empty) = 0:5. Every
		//element in the grid would start with (0.5, 0.5)
		int counter = 0;
		for(int row = 0;row < noOfrows;row++)
	    {	
			double element_width_s_dist=0.0;
			double element_width_e_dist=0.0;
			double element_height_s_dist=0.0;
			double element_height_e_dist=0.0;
			// Calculating here since, the height will remain the same for 
			// all elements in the entire row
			if(occupancy_grid_probability_matrix_arrayList.size()==0)
    		{
    			// First element to be added
    			// For the first row 
				if(row == 0)
				{
					element_height_s_dist=x1_lower_left_coordinate;
					element_height_e_dist=element_height_s_dist+this.element_SquareSide_height_distance;
				}
    		}
			else
    		{
    			OccupancyGridMatrix previous_ElementObject = occupancy_grid_probability_matrix_arrayList.get(counter-1);
				element_height_s_dist=previous_ElementObject.getElement_squareHeight_e_distance();
				element_height_e_dist=element_height_s_dist+this.element_SquareSide_height_distance;
    		}
			
	    	for(int col = 0; col < noOfCols;col++)
	    	{		
	    		if(occupancy_grid_probability_matrix_arrayList.size()==0)
	    		{
	    			// first element to be added
	    			// For the first col in every row the width would start
					if(col == 0)
					{
						// Bcoz the x axis values are denoted by Y coordinate
						if(y1_lower_left_coordinate>y2_top_right_coordinate)
						{
							////System.out.println("moving right-val decreases");
							element_width_s_dist=y1_lower_left_coordinate;
							element_width_e_dist= element_width_s_dist-
														this.element_SquareSide_width_distance;
						}
						else
						{
							////System.out.println("moving left-val increases");
							element_width_s_dist=y1_lower_left_coordinate;
							element_width_e_dist= element_width_s_dist+
														this.element_SquareSide_width_distance;
						}
					}
	    		}
	    		else
	    		{
	    			// first element to be added
	    			// For the first col in every row the width would start
					if(col == 0)
					{
						// Bcoz the x axis values are denoted by Y coordinate
						if(y1_lower_left_coordinate>y2_top_right_coordinate)
						{
							////System.out.println("moving right-val decreases");
							element_width_s_dist=y1_lower_left_coordinate;
							element_width_e_dist= element_width_s_dist-
														this.element_SquareSide_width_distance;
						}
						else
						{
							////System.out.println("moving left-val increases");
							element_width_s_dist=y1_lower_left_coordinate;
							element_width_e_dist= element_width_s_dist+
														this.element_SquareSide_width_distance;
						}
					}
					// Otherwise add to the previous element
					else
					{
						OccupancyGridMatrix previous_ElementObject = occupancy_grid_probability_matrix_arrayList.get(counter-1);
		    			if(y1_lower_left_coordinate>y2_top_right_coordinate)
						{
		    				element_width_s_dist=previous_ElementObject.getElement_squareWidth_e_distance();
							element_width_e_dist= element_width_s_dist-this.element_SquareSide_width_distance;
						}
		    			else
		    			{
		    				element_width_s_dist=previous_ElementObject.getElement_squareWidth_e_distance();
							element_width_e_dist= element_width_s_dist+this.element_SquareSide_width_distance;
		    			}
					}
	    		}
	    		
	    		
	    		OccupancyGridMatrix elementObject = new OccupancyGridMatrix();
	    		elementObject.setElement_id(counter);
	    		elementObject.setElement_rowNo(row);
	    		elementObject.setElement_colNo(col);
	    		elementObject.setElement_P_Empty(0.5);
	    		elementObject.setElement_P_Occupied(0.5);
	    		elementObject.setElement_HIMM_Factor(0);
	    		elementObject.setElement_squareWidth_s_distance(element_width_s_dist);
	    		elementObject.setElement_squareWidth_e_distance(element_width_e_dist);
	    		elementObject.setElement_squareHeight_s_distance(element_height_s_dist);
	    		elementObject.setElement_squareHeight_e_distance(element_height_e_dist);
	    		elementObject.setElement_symbol(AimaConstants.P_EMPTY_OBJECT_SYMBOL);
	    		
	    		occupancy_grid_probability_matrix[row][col]=elementObject;
	    		occupancy_grid_probability_matrix_arrayList.add(elementObject);
	    		
	    		counter++;
	    	}
	    }
		
		// For any given square input, mark the boundary as walls,
		// for mapping purposes
		for(int row = 0;row < noOfrows;row++)
	    {
	    	for(int col = 0; col < noOfCols;col++)
	    	{
	    		if(row==0 || 
	    				col==0 ||
	    					row==(noOfrows-1) ||
	    						col==(noOfCols-1))
	    		{
	    			OccupancyGridMatrix elementObject=occupancy_grid_probability_matrix[row][col];
		    		elementObject.setElement_symbol(AimaConstants.P_OCCUPIED_OBJECT_SYMBOL);
		    		occupancy_grid_probability_matrix[row][col]=elementObject;
	    		}
	    	}
	    }
		
		// Display the initialized symbol values
//		for(int row = 0;row < noOfrows;row++)
//	    {
//			String element="";
//	    	for(int col = 0; col < noOfCols;col++)
//	    	{
//	    		OccupancyGridMatrix elementObject=occupancy_grid_probability_matrix[row][col];
//	    		element=element+elementObject.getElement_symbol();
//	    	}
//	    	System.out.print(element);
//	    	System.out.print("\n");
//	    }
		
        // Step3: Display the occupancy grid initialized values
//		for(int row = 0;row < noOfrows;row++)
//	    {
//			String element="";
//	    	for(int col = 0; col < noOfCols;col++)
//	    	{
//	    		OccupancyGridMatrix elementObject=occupancy_grid_probability_matrix[row][col];
//	    		element=element+
//	    		" obj=" +elementObject.getElement_id()+" ;"+
//	    		" r=" + elementObject.getElement_rowNo()+" ;"+
//	    		" c=" + elementObject.getElement_colNo()+" ;"+
//	    		" widthS=" + elementObject.getElement_squareWidth_s_distance()+" ;"+
//	    		" widthE=" + elementObject.getElement_squareWidth_e_distance()+" ;"+
//	    		" heightS=" + elementObject.getElement_squareHeight_s_distance()+" ;"+
//	    		" heightE=" + elementObject.getElement_squareHeight_e_distance()+" ;"+
//	    		"|";
//	    	}
//	    	System.out.print(element);
//	    	System.out.print("\n");
//	    }
		
		// Step4: Calculate the robot position in the grid
		PositionLocalizer positionObject=new PositionLocalizer();
		double CurrentRobotXPos=positionObject.getCurrentXPosOfRobot();
		double CurrentRobotYPos=positionObject.getCurrentYPosOfRobot();
		
		RobotStartXPos=CurrentRobotXPos;
		RobotStartYPos=CurrentRobotYPos;
		
		//System.out.println(	" CurrentRobotXPos = " + CurrentRobotXPos + 
		//					" CurrentRobotYPos = " + CurrentRobotYPos);
		
		//System.out.println("CurrentRobotXPos>= elementObject.getElement_squareHeight_s_distance() &&" +
		//					"CurrentRobotXPos<= elementObject.getElement_squareHeight_e_distance()"+
		//					"&& CurrentRobotYPos>= elementObject.getElement_squareWidth_s_distance()"+
	    //		 			"&& CurrentRobotYPos>= elementObject.getElement_squareWidth_e_distance()");
		
		for(int row = 0;row < noOfrows;row++)
	    {
	    	for(int col = 0; col < noOfCols;col++)
	    	{
	    		OccupancyGridMatrix elementObject=occupancy_grid_probability_matrix[row][col];
	    		if(y1_lower_left_coordinate>y2_top_right_coordinate)
	    		{
	    			if(CurrentRobotXPos>= elementObject.getElement_squareHeight_s_distance()
	    		    		 && CurrentRobotXPos<= elementObject.getElement_squareHeight_e_distance()
	    		    		 && CurrentRobotYPos<= elementObject.getElement_squareWidth_s_distance()
	    		    		 && CurrentRobotYPos>= elementObject.getElement_squareWidth_e_distance())
	    			{
	    				//System.out.println(" helloooooo");
	    				//System.out.println(" elementObject=" +elementObject.getElement_id());
	    				//System.out.println(" getElement_rowNo=" +elementObject.getElement_rowNo());
	    				//System.out.println(" getElement_colNo=" +elementObject.getElement_colNo());
	    				//System.out.println(" getElement_squareWidth_s_distance=" +elementObject.getElement_squareWidth_s_distance());
	    				//System.out.println(" getElement_squareWidth_e_distance=" +elementObject.getElement_squareWidth_e_distance());
	    				//System.out.println(" getElement_squareHeight_s_distance=" +elementObject.getElement_squareHeight_s_distance());
	    				//System.out.println(" getElement_squareHeight_e_distance=" +elementObject.getElement_squareHeight_e_distance());
	    				// Mark that position as robot on the grid
	    				elementObject.setRobotOnElement(true);
	    				elementObject.setElement_symbol(AimaConstants.P_START_OBJECT_SYMBOL);
	    				startRobotPos_ElementObject=elementObject;
	    				occupancy_grid_probability_matrix[row][col]=elementObject;
	    			}
	    		}
	    	}
	    }
		
		
//		// Display the object symbol values
//		for(int row = 0;row < noOfrows;row++)
//	    {
//			String element="";
//	    	for(int col = 0; col < noOfCols;col++)
//	    	{
//	    		OccupancyGridMatrix elementObject=occupancy_grid_probability_matrix[row][col];
//	    		element=element+elementObject.getElement_symbol();
//	    	}
//	    	System.out.print(element);
//	    	System.out.print("\n");
//	    }
		
		//System.exit(0);
		
		// Step5: Position the robot towards the right wall 
		DeliberativeBehavior deliberativeObject=new DeliberativeBehavior();
		try
		{
			deliberativeObject.positionRobotToRightWall();
		}
		catch (Exception e) 
		{
			// TODO: handle exception
			//System.out.println("Exception - in logic flow111");
		}
		//System.exit(0);
		
		// Step6: The robot is made to follow the right wall,
		// inside the given boundary 
		try
		{
			deliberativeObject.followRightWallAlgorithm();
		}
		catch (Exception e) 
		{
			// TODO: handle exception
			//System.out.println("Exception - in logic flow222");
		}
		// Do not exit, because the control is trying to exit here,
		// before finishing the flow
		//System.exit(0);
		
		
		
		
		// Step6: Rotate the robot 360 degrees to get the full laser scan values
		//deliberativeObject.rotateRobot360Degrees();
		
		
		
		
		
		
		
		
	}
	
}
