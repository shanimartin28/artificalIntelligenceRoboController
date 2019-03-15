package aimaOccupancyGridProcessingPackage;


import java.util.ArrayList;

import org.json.simple.JSONArray;

import aimaKompaiMapperPackage.mapper;
import aimaKompaiMapperPackage.KompaiRobotController;
import aimaKompaiMapperPackage.KompaiRobotDirections;
import aimaLocalizationPackage.ElementLocalizer;
import aimaLocalizationPackage.PositionLocalizer;
import aimaMapPlottingPackage.ImageProcessing;
import aimaResources.AimaConstants;

public class FillOccupancyGrid
{
	PositionLocalizer positionObject=new PositionLocalizer();
	public static OccupancyGridMatrix[][] occupancyGRID=null;
	static int noOfCols = 0; 
	static int noOfrows = 0; 
	public static boolean startRobotElement=false;
	OccupancyGridMatrix startGRIDElementObject=null;
	
	public static int trackDirectionFlag=0;
	
	// To determine the movement
	public static boolean horizontal_direction=false;
	public static boolean vertical_forward=false;
	
	public static String robot_direction=null;
	public static String prev_robot_direction=null;
	
	public static double previousRobotXPos=0.0;
	public static double previousRobotYPos=0.0;
		
	public static double currentRobotXPos=0.0;
	public static double currentRobotYPos=0.0;
	
	public static ArrayList<ElementLocalizer> locationObjectsList=
						new ArrayList<ElementLocalizer>();
	
	ArrayList<Double> pathXList=new ArrayList<Double>();
	
	public static boolean turnOffZeroAcousticAxis=false;
	
	KompaiRobotController controllerObject=new KompaiRobotController();
	
	
	public FillOccupancyGrid()
	{
		
	}
	
	public void fillOccupancyGridAlgm()
	{
		//System.out.println("1. m here!!!- fillOccupancyGridAlgm");
		
		// For tracking all robot traversed positions
		//System.out.println("2. Store all robot traversed positions");
		ElementLocalizer locationStoreObject=new ElementLocalizer();
		// stores the current X and Y position of the robot
		locationStoreObject.setxPos(positionObject.getCurrentXPosOfRobot());
		locationStoreObject.setyPos(positionObject.getCurrentYPosOfRobot());
		locationObjectsList.add(locationStoreObject);
		
		// Get the latest Occupancy Grid
		// For the first time
		if(occupancyGRID==null)
		{
			//System.out.println("3. first time getting the occupancy grid");
			occupancyGRID=OccupancyGridAlgm.occupancy_grid_probability_matrix;
			noOfCols = OccupancyGridAlgm.noOfCols; 
			noOfrows = OccupancyGridAlgm.noOfrows; 
			for(int row = 0;row < noOfrows;row++)
		    {
		    	for(int col = 0; col < noOfCols;col++)
		    	{
		    		OccupancyGridMatrix elementObject=occupancyGRID[row][col];
		    		if(elementObject.getElement_symbol()!= null)
		    		{
		    			if(elementObject.getElement_symbol().equalsIgnoreCase
			    				(AimaConstants.P_START_OBJECT_SYMBOL))
			    		{
		    				//System.out.println("In the start position");
		    				//Since start
		    				robot_direction=AimaConstants.robot_horizontal_right;
		    				
		    				previousRobotXPos=positionObject.getCurrentXPosOfRobot();
		    				previousRobotYPos=positionObject.getCurrentYPosOfRobot();
		    				
			    			startGRIDElementObject=elementObject;
			    			startRobotElement=true;
			    		}
		    		}
		    	}
		    }
		}
		else
		{
			//System.out.println("You are not the start position!!!!");
			startRobotElement=false;
			currentRobotXPos=positionObject.getCurrentXPosOfRobot();
			currentRobotYPos=positionObject.getCurrentYPosOfRobot();
			
			pathXList.add(currentRobotXPos);
			
		}
		
		// Determine the direction of the robot
		if(horizontal_direction)
		{
			//System.out.println("horizontal_direction...");
			if(startRobotElement)
			{
				robot_direction=AimaConstants.robot_horizontal_right;
			}
			else if(DeliberativeBehavior.followWallStartTrack==false)
			{
				robot_direction=AimaConstants.robot_horizontal_right;	
			}
			else if(currentRobotYPos<previousRobotYPos)
			{
				robot_direction=AimaConstants.robot_horizontal_right;	
			}
			else if(currentRobotYPos>previousRobotYPos)
			{
				robot_direction=AimaConstants.robot_horizontal_left;	
			}
			
		}
		else if(horizontal_direction==false)
		{
			// Determine the direction by using 3 traversed values
			if(pathXList.size()>=10)
			{
				int listSize=pathXList.size();
				double xval1=pathXList.get(listSize-1);
				double xval2=pathXList.get(listSize-5);
				double xval3=pathXList.get(listSize-15);
				
				System.out.println("xval1= " +xval1);
				System.out.println("xval2= " +xval2);
				System.out.println("xval3= " +xval3);
				
				if(xval3>xval2
					|| xval2>xval1)
				{
					robot_direction=AimaConstants.robot_vertical_bwd;
					System.out.println("ZZZ. robot_direction = "+robot_direction);
				}
				else
				{
					robot_direction=AimaConstants.robot_vertical_fwd;
					System.out.println("ZZZ. robot_direction = "+robot_direction);
				}
			}
			else
			{
				robot_direction=AimaConstants.robot_vertical_fwd;
				System.out.println("ZZZ. robot_direction = "+robot_direction);
			}
							
		}
		
		System.out.println("fILLaLGM:Robot direction is ... " + robot_direction);
	    System.out.println("fILLaLGM:horizontal_direction is..... "+horizontal_direction);
		
		
		// Get the laser scan distances for the nearby obstacles
		// along the acoustic axis
		JSONArray HIMM_laser_array=null;
		ElementLocalizer elemLocObject=null;
		HIMM_laser_array= positionObject.getHIMMLaserArray();
		
		//System.out.println("Fill Algm: HIMM_laser_array = " + HIMM_laser_array);
		
		// Once the distances_array is got,
	    for(int i=0;i<HIMM_laser_array.size();i++)
	    {
	    	elemLocObject=(ElementLocalizer) HIMM_laser_array.get(i);
	    	// Along the acoustic axis
	    	if(elemLocObject.getAngleObjectSymbol().
	    			equalsIgnoreCase(AimaConstants.P_ZERO_ANGLE_SYMBOL))
	    	{
	    		//System.out.println("111");
	    		// x denotes the height, y denotes the width
	    		// then robot moving in horizontal direction
	    		// else vertical direction
	    		if(horizontal_direction)
	    		{
	    			
	    			//System.out.println("222");
	    			
	    			// then x value remains the same
	    			// and the y value changes
	    			if(startRobotElement)
	    			{
	    				//System.out.println("333");
	    				
	    				System.out.println("/////HORIZONTAL RIGHT/////");
	    				//this.horizontalRIGHT_AcousticZEROAxis(elemLocObject);
	    				System.out.println("/////HORIZONTAL RIGHT/////");
	    				
	    			}
	    			else
	    			{
	    				if(robot_direction.equalsIgnoreCase(AimaConstants.robot_horizontal_right))
		    			{
	    					
	    					//System.out.println("444");
	    					
	    					System.out.println("/////HORIZONTAL RIGHT/////");
		    				//this.horizontalRIGHT_AcousticZEROAxis(elemLocObject);
		    				System.out.println("/////HORIZONTAL RIGHT/////");
		    			}
	    				else if(robot_direction.equalsIgnoreCase(AimaConstants.robot_horizontal_left))
		    			{
	    					System.out.println("/////HORIZONTAL LEFT /////");
	    					//this.horizontalLEFT_AcousticZEROAxis(elemLocObject);
	    					System.out.println("/////HORIZONTAL LEFT/////");
		    			}
	    			}
	    		}
	    		// Then vertical direction
	    		else if(horizontal_direction==false)
	    		{
	    			//System.out.println("555.zero angle");
	    			
	    			////System.out.println(("2");
	    			if(trackDirectionFlag<=1)
	    			{
	    				if(robot_direction.equalsIgnoreCase(AimaConstants.robot_vertical_fwd))
		    			{
		    				System.out.println("/////VERTICAL FORWARD/////");
		    				this.verticalFORWARD_AcousticZEROAxis(elemLocObject);
		    				System.out.println("/////VERTICAL FORWARD/////");
		    			}
		    			else if(robot_direction.equalsIgnoreCase(AimaConstants.robot_vertical_bwd))
		    			{
		    				System.out.println("/////VERTICAL BACKWARD/////");
		    				//this.verticalBACKWARD_AcousticZEROAxis(elemLocObject);
		    				System.out.println("/////VERTICAL BACKWARD/////");
		    			}
	    			}
	    			
	    		}
	    	
	    	}
	    	else if(elemLocObject.getAngleObjectSymbol().
	    			equalsIgnoreCase(AimaConstants.P_RIGHT_ANGLE_SYMBOL))
	    	{
	    		if(horizontal_direction)
	    		{
	    			if(startRobotElement)
	    			{
	    				System.out.println("/////HORIZONTAL RIGHT/////");
	    				this.horizontalRIGHT_AcousticRIGHTAxis(elemLocObject);
	    				System.out.println("/////HORIZONTAL RIGHT/////");
	    			}
	    			else
	    			{
	    				if(robot_direction.equalsIgnoreCase(AimaConstants.robot_horizontal_right))
		    			{
	    					System.out.println("/////HORIZONTAL RIGHT/////");
		    				this.horizontalRIGHT_AcousticRIGHTAxis(elemLocObject);
		    				System.out.println("/////HORIZONTAL RIGHT/////");
		    			}
	    				else if(robot_direction.equalsIgnoreCase(AimaConstants.robot_horizontal_left))
		    			{
	    					System.out.println("/////HORIZONTAL LEFT /////");
		    				this.horizontalLEFT_AcousticRIGHTAxis(elemLocObject);
		    				System.out.println("/////HORIZONTAL LEFT /////");
		    			}
	    			}
	    		}
	    		// Then vertical direction
	    		else if(horizontal_direction==false)
	    		{
	    			//System.out.println("555.right angle");
	    			
	    			if(trackDirectionFlag<=1)
	    			{
	    				if(robot_direction.equalsIgnoreCase(AimaConstants.robot_vertical_fwd))
		    			{
		    				System.out.println("/////VERTICAL FORWARD/////");
		    				this.verticalFORWARD_AcousticRIGHTAxis(elemLocObject);
		    				System.out.println("/////VERTICAL FORWARD/////");
		    			}
		    			else if(robot_direction.equalsIgnoreCase(AimaConstants.robot_vertical_bwd))
		    			{
		    				System.out.println("/////VERTICAL BACKWARD/////");
		    				this.verticalBACKWARD_AcousticRIGHTAxis(elemLocObject);
		    				System.out.println("/////VERTICAL BACKWARD/////");
		    			}
	    			}
	    		}
	    	}
	    	else if(elemLocObject.getAngleObjectSymbol().
	    			equalsIgnoreCase(AimaConstants.P_LEFT_ANGLE_SYMBOL))
	    	{

	    		if(horizontal_direction)
	    		{
	    			if(startRobotElement)
	    			{
	    				System.out.println("/////HORIZONTAL RIGHT/////");
	    				this.horizontalRIGHT_AcousticLEFTAxis(elemLocObject);
	    				System.out.println("/////HORIZONTAL RIGHT/////");
	    			}
	    			else
	    			{
	    	
					if(robot_direction.equalsIgnoreCase(AimaConstants.robot_horizontal_right))
		    			{
							System.out.println("/////HORIZONTAL RIGHT/////");
		    				this.horizontalRIGHT_AcousticLEFTAxis(elemLocObject);
		    				System.out.println("/////HORIZONTAL RIGHT/////");
		    			}
	    				else if(robot_direction.equalsIgnoreCase(AimaConstants.robot_horizontal_left))
		    			{
	    					System.out.println("/////HORIZONTAL LEFT /////");
		    				this.horizontalLEFT_AcousticLEFTAxis(elemLocObject);
		    				System.out.println("/////HORIZONTAL LEFT /////");
		    			}
	    			}
	    		}
	    		// Then vertical direction
	    		else if(horizontal_direction==false)
	    		{
	    			//System.out.println("555.left angle");
	    			if(trackDirectionFlag<=1)
	    			{
	    				if(robot_direction.equalsIgnoreCase(AimaConstants.robot_vertical_fwd))
		    			{
		    				System.out.println("/////VERTICAL FORWARD/////");
		    				this.verticalFORWARD_AcousticLEFTAxis(elemLocObject);
		    				System.out.println("/////VERTICAL FORWARD/////");
		    			}
		    			else if(robot_direction.equalsIgnoreCase(AimaConstants.robot_vertical_bwd))
		    			{
		    				System.out.println("/////VERTICAL BACKWARD/////");
		    				this.verticalBACKWARD_AcousticLEFTAxis(elemLocObject);
		    				System.out.println("/////VERTICAL BACKWARD/////");
		    			}
	    			}
	    		}
	    	}
	    }
		
		
		
	    //displayGRID();
	    //displayGRIDInverse();
	    
	    	    
	    if(startRobotElement==false)
	    {
	    	previousRobotXPos=currentRobotXPos;
			previousRobotYPos=currentRobotYPos;
			prev_robot_direction=robot_direction;
	    }
	    
		//System.out.println("End of fill method");
		//System.out.println(" ------- ");
		//System.out.println("");
		
	}
		
	
	
	// Identify the grid elements along the acoustic axis
	// Calculate each grid element based on the current position 
	// of the robot
//	public void horizontalRIGHT_AcousticZEROAxis(ElementLocalizer elemLocObject)
//	{
//
//		System.out.println("/////ZERO AXIS/////");
//		
//		double CurrentRobotXPos=positionObject.getCurrentXPosOfRobot();
//		double CurrentRobotYPos=positionObject.getCurrentYPosOfRobot();
//		
//		// As the robot moves right the y value decreases
//		double obstaclePosition=(CurrentRobotYPos-elemLocObject.getDistance());
//		// Since moving right(to reduce the - value,add to it)
//		// obstaclePosition=obstaclePosition+AimaConstants.tolerance_for_boundary_grid_elem_width;
//
//		System.out.println("ZERO AXIS:CurrentRobotYPos =" +CurrentRobotYPos);
//		System.out.println("ZERO AXIS:obstaclePosition =" +obstaclePosition);
//		
//		// So get all the grid elements with the x value as the same(since the height 
//		// would be the same) and
//		// the y value between current position and obstacle distance
//		int count=0;
//		for(int row = 0;row < noOfrows;row++)
//	    {
//	    	for(int col = 0; col < noOfCols;col++)
//	    	{
//	    		OccupancyGridMatrix elementObject=occupancyGRID[row][col];
//	    		
//	    		// HEIGHT WOULD BE SAME
//	    		if(CurrentRobotXPos>= elementObject.getElement_squareHeight_s_distance()
//    		    		 && CurrentRobotXPos<= elementObject.getElement_squareHeight_e_distance())
//    			{
//	    			//Calculate the obstacle position
//	    			// As we move right the values decreases
//	    			
//	    			if(obstaclePosition<0)
//	    			{
//	    				if(elementObject.getElement_squareWidth_s_distance()>=obstaclePosition
//		    					&& obstaclePosition>= elementObject.getElement_squareWidth_e_distance())
//		    			{
//	    					
//		    				System.out.println("ZERO AXIS:obtained obstacle at!!! -" +obstaclePosition);
//		    				System.out.println("ZERO AXIS:getElement_squareWidth_s_distance =" +elementObject.getElement_squareWidth_s_distance());
//		    				System.out.println("ZERO AXIS:getElement_squareWidth_e_distance =" +elementObject.getElement_squareWidth_e_distance());
//		    				System.out.println(" ");
//		    				elementObject.setElement_symbol(AimaConstants.P_OCCUPIED_OBJECT_SYMBOL);
//		    				elementObject.setElement_symbol(" [hr.z] ");
//		    				elementObject.setElement_HIMM_Factor
//		    								(elementObject.getElement_HIMM_Factor()+3);
//		    				//System.exit(0);
//		    			}
//	    			}
//	    			else if(obstaclePosition>=0)
//	    			{
//	    				if(elementObject.getElement_squareWidth_s_distance()<=obstaclePosition
//		    					&& obstaclePosition>= elementObject.getElement_squareWidth_e_distance())
//		    			{
//		    				System.out.println("ZERO AXIS:obtained obstacle at!!! -" +obstaclePosition);
//		    				System.out.println("ZERO AXIS:getElement_squareWidth_s_distance =" +elementObject.getElement_squareWidth_s_distance());
//		    				System.out.println("ZERO AXIS:getElement_squareWidth_e_distance =" +elementObject.getElement_squareWidth_e_distance());
//		    				System.out.println(" ");
//		    				elementObject.setElement_symbol(AimaConstants.P_OCCUPIED_OBJECT_SYMBOL);
//		    				elementObject.setElement_symbol(" [hr.z] ");
//		    				elementObject.setElement_HIMM_Factor
//		    								(elementObject.getElement_HIMM_Factor()+3);
//		    			}
//	    			}
//    			}
//	    	}
//	    }
//		System.out.println("ZERO AXIS:End of method!!");
//	}
	
	public void horizontalRIGHT_AcousticRIGHTAxis(ElementLocalizer elemLocObject)
	{
		System.out.println("///// RIGHT AXIS/////");
		
		double CurrentRobotXPos=positionObject.getCurrentXPosOfRobot();
		double CurrentRobotYPos=positionObject.getCurrentYPosOfRobot();
	
		// For calculating the right angle to the robot current position
		// here the x value(height) changes, while y value(width)remains same
		double obstaclePosition=(CurrentRobotXPos-elemLocObject.getDistance());
		//obstaclePosition=obstaclePosition+AimaConstants.tolerance_for_boundary_grid_elem_width;

		// Check for only 15 grid elements
		if(Math.abs(elemLocObject.getDistance())<=mapper.hIMM_15GridElementsDistance)
		{
			System.out.println("RIGHT AXIS:CurrentRobotXPos =" +CurrentRobotXPos);
			System.out.println("RIGHT AXIS:CurrentRobotYPos =" +CurrentRobotYPos);
			System.out.println("RIGHT AXIS:obstaclePosition =" +obstaclePosition);
			
			// So get all the grid elements with the y value as the same and
			// the x value between current position and obstacle distance
			for(int row = 0;row < noOfrows;row++)
		    {
		    	for(int col = 0; col < noOfCols;col++)
		    	{
		    		OccupancyGridMatrix elementObject=occupancyGRID[row][col];
		    		if(CurrentRobotYPos<= elementObject.getElement_squareWidth_s_distance()
	    		    		 && CurrentRobotYPos>= elementObject.getElement_squareWidth_e_distance())
	    			{
		    			//Calculate the obstacle position
		    			
		    			if(obstaclePosition<0)
		    			{
		    				if(obstaclePosition<= elementObject.getElement_squareHeight_s_distance()
			    					&& obstaclePosition>= elementObject.getElement_squareHeight_e_distance())
			    			{
			    				System.out.println("RIGHT AXIS:getElement_squareHeight_s_distance =" +elementObject.getElement_squareHeight_s_distance());
			    				System.out.println("RIGHT AXIS:getElement_squareHeight_e_distance =" +elementObject.getElement_squareHeight_e_distance());
			    				System.out.println("RIGHT AXIS:obstaclePosition =" +obstaclePosition);
			    				elementObject.setElement_symbol(AimaConstants.P_OCCUPIED_OBJECT_SYMBOL);
			    				elementObject.setElement_symbol(" [hr.r] ");
			    				elementObject.setElement_HIMM_Factor
			    								(elementObject.getElement_HIMM_Factor()+3);
			    			}
		    			}
		    			else if(obstaclePosition>=0)
		    			{
		    				if(obstaclePosition>= elementObject.getElement_squareHeight_s_distance()
			    					&& obstaclePosition<= elementObject.getElement_squareHeight_e_distance())
			    			{
			    				System.out.println("RIGHT AXIS:getElement_squareHeight_s_distance =" +elementObject.getElement_squareHeight_s_distance());
			    				System.out.println("RIGHT AXIS:getElement_squareHeight_e_distance =" +elementObject.getElement_squareHeight_e_distance());
			    				System.out.println("RIGHT AXIS:obstaclePosition =" +obstaclePosition);
			    				elementObject.setElement_symbol(AimaConstants.P_OCCUPIED_OBJECT_SYMBOL);
			    				elementObject.setElement_symbol(" [hr.r] ");
			    				elementObject.setElement_HIMM_Factor
			    								(elementObject.getElement_HIMM_Factor()+3);
			    			}
		    			}
	    			}
		    	}
		    }
		}
		
		
	}
	
	
	public void horizontalRIGHT_AcousticLEFTAxis(ElementLocalizer elemLocObject)
	{
		System.out.println("///// LEFT AXIS /////");
		
		double CurrentRobotXPos=positionObject.getCurrentXPosOfRobot();
		double CurrentRobotYPos=positionObject.getCurrentYPosOfRobot();
		
		// For calculating the left angle to the robot current position
		// here the x value(height) changes, while y value(width)remains same
		double obstaclePosition=(elemLocObject.getDistance()-CurrentRobotXPos);
		//obstaclePosition=obstaclePosition-AimaConstants.tolerance_for_boundary_grid_elem_height;
		
		if(Math.abs(elemLocObject.getDistance())<=mapper.hIMM_15GridElementsDistance)
		{
			System.out.println("LEFT AXIS:CurrentRobotXPos =" +CurrentRobotXPos);
			System.out.println("LEFT AXIS:CurrentRobotYPos =" +CurrentRobotYPos);
			System.out.println("LEFT AXIS:obstaclePosition =" +obstaclePosition);
			
			// So get all the grid elements with the y value as the same and
			// the x value between current position and obstacle distance
			for(int row = 0;row < noOfrows;row++)
		    {
		    	for(int col = 0; col < noOfCols;col++)
		    	{
		    		OccupancyGridMatrix elementObject=occupancyGRID[row][col];
		    		if(CurrentRobotYPos<= elementObject.getElement_squareWidth_s_distance()
	    		    		 && CurrentRobotYPos>= elementObject.getElement_squareWidth_e_distance())
	    			{
		    			//Calculate the obstacle position
		    			
		    			if(obstaclePosition<0)
		    			{
		    				if(obstaclePosition<=elementObject.getElement_squareHeight_s_distance()
			    					&& obstaclePosition>=elementObject.getElement_squareHeight_e_distance())
			    			{
			    				System.out.println("LEFT AXIS:getElement_squareHeight_s_distance =" +elementObject.getElement_squareHeight_s_distance());
			    				System.out.println("LEFT AXIS:getElement_squareHeight_e_distance =" +elementObject.getElement_squareHeight_e_distance());
			    				System.out.println("LEFT AXIS:obstaclePosition obtained!!! =" +obstaclePosition);
			    				
			    				elementObject.setElement_symbol(AimaConstants.P_OCCUPIED_OBJECT_SYMBOL);
			    				elementObject.setElement_symbol(" [hr.l] ");
			    				elementObject.setElement_HIMM_Factor
			    								(elementObject.getElement_HIMM_Factor()+3);
			    			}
		    			}
		    			else if(obstaclePosition>=0)
		    			{
		    				if(obstaclePosition>=elementObject.getElement_squareHeight_s_distance()
			    					&& obstaclePosition<=elementObject.getElement_squareHeight_e_distance())
			    			{
			    				System.out.println("LEFT AXIS:getElement_squareHeight_s_distance =" +elementObject.getElement_squareHeight_s_distance());
			    				System.out.println("LEFT AXIS:getElement_squareHeight_e_distance =" +elementObject.getElement_squareHeight_e_distance());
			    				System.out.println("LEFT AXIS:obstaclePosition obtained!!! =" +obstaclePosition);
			    				
			    				elementObject.setElement_symbol(AimaConstants.P_OCCUPIED_OBJECT_SYMBOL);
			    				elementObject.setElement_symbol(" [hr.l] ");
			    				elementObject.setElement_HIMM_Factor
			    								(elementObject.getElement_HIMM_Factor()+3);
			    			}
		    			}
	    			}
		    	}
		    }
			System.out.println("End of LEFT AXIS");	
		}
		
		
	}
	
	public void verticalFORWARD_AcousticZEROAxis(ElementLocalizer elemLocObject)
	{

		System.out.println("/////ZERO AXIS/////");
		
		double CurrentRobotXPos=positionObject.getCurrentXPosOfRobot();
		double CurrentRobotYPos=positionObject.getCurrentYPosOfRobot();
		double obstaclePosition=0.0;
		
		// For calculating the zero angle to the robot current position
		// here the x value(height) changes, while y value(width)remains same
		//double obstaclePosition=(elemLocObject.getDistance()-CurrentRobotXPos);
		//obstaclePosition=obstaclePosition-AimaConstants.tolerance_for_boundary_grid_elem_height;
		
		// X is the height
		if(CurrentRobotXPos<0)
		{
			//////System.out.println(("negative");
			obstaclePosition=(elemLocObject.getDistance()+CurrentRobotXPos);
			
		}
		else
		{
			//////System.out.println(("positive");
			obstaclePosition=(elemLocObject.getDistance()+CurrentRobotXPos);
			
		}
		
		if(Math.abs(elemLocObject.getDistance())<=mapper.hIMM_15GridElementsDistance)
		{
			System.out.println("ZERO AXIS:CurrentRobotXPos =" +CurrentRobotXPos);
			System.out.println("ZERO AXIS:CurrentRobotYPos =" +CurrentRobotYPos);
			System.out.println("ZERO AXIS:obstaclePosition =" +obstaclePosition);
			
			// So get all the grid elements with the y value as the same and
			// the x value between current position and obstacle distance
			for(int row = 0;row < noOfrows;row++)
		    {
		    	for(int col = 0; col < noOfCols;col++)
		    	{
		    		OccupancyGridMatrix elementObject=occupancyGRID[row][col];
		    		if(CurrentRobotYPos<= elementObject.getElement_squareWidth_s_distance()
	    		    		 && CurrentRobotYPos>= elementObject.getElement_squareWidth_e_distance())
	    			{
		    			
		    			//////////System.out.println(("a. " +elementObject.getElement_squareHeight_s_distance());
		    			//Calculate the obstacle position
		    			
		    			if(obstaclePosition<0)
		    			{
		    				if(obstaclePosition<=elementObject.getElement_squareHeight_s_distance()
			    					&& obstaclePosition>=elementObject.getElement_squareHeight_e_distance())
			    			{
			    				System.out.println("ZERO AXIS:getElement_squareHeight_s_distance =" +elementObject.getElement_squareHeight_s_distance());
			    				System.out.println("ZERO AXIS:getElement_squareHeight_e_distance =" +elementObject.getElement_squareHeight_e_distance());
			    				System.out.println("ZERO AXIS: Obtained!!! obstaclePosition [vf.z] =" +obstaclePosition);
			    				
			    				elementObject.setElement_symbol(AimaConstants.P_OCCUPIED_OBJECT_SYMBOL);
			    				elementObject.setElement_symbol(" [vf.z] ");
			    				elementObject.setElement_HIMM_Factor
			    								(elementObject.getElement_HIMM_Factor()+3);
			    			}
			    			
		    			}
		    			else if(obstaclePosition>=0)
		    			{
		    				if(obstaclePosition>=elementObject.getElement_squareHeight_s_distance()
			    					&& obstaclePosition<=elementObject.getElement_squareHeight_e_distance())
			    			{
			    				System.out.println("ZERO AXIS:getElement_squareHeight_s_distance =" +elementObject.getElement_squareHeight_s_distance());
			    				System.out.println("ZERO AXIS:getElement_squareHeight_e_distance =" +elementObject.getElement_squareHeight_e_distance());
			    				System.out.println("ZERO AXIS: Obtained!!! obstaclePosition [vf.z] =" +obstaclePosition);
			    				
			    				elementObject.setElement_symbol(AimaConstants.P_OCCUPIED_OBJECT_SYMBOL);
			    				elementObject.setElement_symbol(" [vf.z] ");
			    				elementObject.setElement_HIMM_Factor
			    								(elementObject.getElement_HIMM_Factor()+3);
			    			}
			    			
		    			}
	    			}
		    	}
		    }
			System.out.println("End of ZERO AXIS................");
		}
	}
	
	// Here Y is same - X changes (increases)
	public void verticalFORWARD_AcousticRIGHTAxis(ElementLocalizer elemLocObject)
	{
		System.out.println("/////RIGHT AXIS/////");
		
		double CurrentRobotXPos=positionObject.getCurrentXPosOfRobot();
		double CurrentRobotYPos=positionObject.getCurrentYPosOfRobot();
	
		double obstaclePosition=0.0;
		obstaclePosition=(CurrentRobotYPos-elemLocObject.getDistance());
		//obstaclePosition=obstaclePosition-AimaConstants.tolerance_for_boundary_grid_elem_width;
			
		if(Math.abs(elemLocObject.getDistance())<=mapper.hIMM_15GridElementsDistance)
		{
			System.out.println("RIGHT AXIS: CurrentRobotXPos =" +CurrentRobotXPos);
			System.out.println("RIGHT AXIS: CurrentRobotYPos =" +CurrentRobotYPos);
			System.out.println("RIGHT AXIS: obstaclePosition = " +obstaclePosition);
			
			// So get all the grid elements with the y value as the same and
			// the x value between current position and obstacle distance
			for(int row = 0;row < noOfrows;row++)
		    {
		    	for(int col = 0; col < noOfCols;col++)
		    	{
		    		OccupancyGridMatrix elementObject=occupancyGRID[row][col];
		    		
		    		if(CurrentRobotXPos>= elementObject.getElement_squareHeight_s_distance()
		   		    		 && CurrentRobotXPos<= elementObject.getElement_squareHeight_e_distance())
	    			{
		    			////System.out.println("555.RightAngle.a.col obj= " +elementObject);
		    			
		    			////////////System.out.println(("a. " +elementObject.getElement_squareHeight_s_distance());
		    			//Calculate the obstacle position
		    			
		    			if(obstaclePosition<0)
		    			{
		    				if(obstaclePosition<=elementObject.getElement_squareWidth_s_distance()
			    					&& obstaclePosition>=elementObject.getElement_squareWidth_e_distance())
			    			{
			    				System.out.println("RIGHT AXIS: getElement_squareHeight_s_distance =" +elementObject.getElement_squareHeight_s_distance());
			    				System.out.println("RIGHT AXIS: getElement_squareHeight_e_distance =" +elementObject.getElement_squareHeight_e_distance());
			    				System.out.println("RIGHT AXIS: obstaclePosition =" +obstaclePosition);
			    				
			    				elementObject.setElement_symbol(AimaConstants.P_OCCUPIED_OBJECT_SYMBOL);
			    				elementObject.setElement_symbol(" [vf.r] ");
			    				elementObject.setElement_HIMM_Factor
			    								(elementObject.getElement_HIMM_Factor()+3);
			    			}
		    			}
		    			else if(obstaclePosition>=0)
		    			{
		    				if(obstaclePosition<=elementObject.getElement_squareWidth_s_distance()
			    					&& obstaclePosition>=elementObject.getElement_squareWidth_e_distance())
			    			{
		    					System.out.println("RIGHT AXIS: getElement_squareHeight_s_distance =" +elementObject.getElement_squareHeight_s_distance());
			    				System.out.println("RIGHT AXIS: getElement_squareHeight_e_distance =" +elementObject.getElement_squareHeight_e_distance());
			    				System.out.println("RIGHT AXIS: obstaclePosition =" +obstaclePosition);
			    				
			    				elementObject.setElement_symbol(AimaConstants.P_OCCUPIED_OBJECT_SYMBOL);
			    				elementObject.setElement_symbol(" [vf.r] ");
			    				elementObject.setElement_HIMM_Factor
			    								(elementObject.getElement_HIMM_Factor()+3);
			    			}
		    			}
		    			
	    			}
		    	}
		    }
			System.out.println("End of RIGHT AXIS");
		}
		
		
	}
	
	public void verticalFORWARD_AcousticLEFTAxis(ElementLocalizer elemLocObject)
	{

		System.out.println("///// LEFT AXIS /////");
		
		double CurrentRobotXPos=positionObject.getCurrentXPosOfRobot();
		double CurrentRobotYPos=positionObject.getCurrentYPosOfRobot();
		double obstaclePosition=0.0;
		
		if(CurrentRobotYPos<0)
		{
			//////System.out.println(("negative");
			obstaclePosition=(elemLocObject.getDistance()+CurrentRobotYPos);
		}
		else
		{
			//////System.out.println(("positive");
			obstaclePosition=(elemLocObject.getDistance()+CurrentRobotYPos);
		}
		
		if(Math.abs(elemLocObject.getDistance())<=mapper.hIMM_15GridElementsDistance)
		{
			System.out.println("LEFT AXIS : CurrentRobotXPos =" +CurrentRobotXPos);
			System.out.println("LEFT AXIS : CurrentRobotYPos =" +CurrentRobotYPos);
			System.out.println("LEFT AXIS : ObstaclePosition =" +obstaclePosition);
			
			
			// So get all the grid elements with the y value as the same and
			// the x value between current position and obstacle distance
			for(int row = 0;row < noOfrows;row++)
		    {
		    	for(int col = 0; col < noOfCols;col++)
		    	{
		    		OccupancyGridMatrix elementObject=occupancyGRID[row][col];
		    		if(CurrentRobotXPos>= elementObject.getElement_squareHeight_s_distance()
		   		    		 && CurrentRobotXPos<= elementObject.getElement_squareHeight_e_distance())
	    			{
		    			if(obstaclePosition<0)
		    			{
		    				if(elementObject.getElement_squareWidth_s_distance()<=obstaclePosition
		    						&& obstaclePosition>=elementObject.getElement_squareWidth_e_distance())
			    			{
			    				System.out.println("LEFT AXIS : getElement_squareHeight_s_distance =" +elementObject.getElement_squareHeight_s_distance());
			    				System.out.println("LEFT AXIS : getElement_squareHeight_e_distance =" +elementObject.getElement_squareHeight_e_distance());
			    				System.out.println("LEFT AXIS : ObstaclePosition OBTAINED!!! =" +obstaclePosition);
			    				
			    				elementObject.setElement_symbol(AimaConstants.P_OCCUPIED_OBJECT_SYMBOL);
			    				elementObject.setElement_symbol(" [vf.l] ");
			    				elementObject.setElement_HIMM_Factor
			    								(elementObject.getElement_HIMM_Factor()+3);
			    			}
		    			}
		    			else if(obstaclePosition>=0)
		    			{
		    				if(elementObject.getElement_squareWidth_s_distance()>=obstaclePosition
		    						&& obstaclePosition>=elementObject.getElement_squareWidth_e_distance())
			    			{
			    				System.out.println("LEFT AXIS : getElement_squareHeight_s_distance =" +elementObject.getElement_squareHeight_s_distance());
			    				System.out.println("LEFT AXIS : getElement_squareHeight_e_distance =" +elementObject.getElement_squareHeight_e_distance());
			    				System.out.println("LEFT AXIS : ObstaclePosition OBTAINED!!! =" +obstaclePosition);
			    				
			    				elementObject.setElement_symbol(AimaConstants.P_OCCUPIED_OBJECT_SYMBOL);
			    				elementObject.setElement_symbol(" [vf.l] ");
			    				elementObject.setElement_HIMM_Factor
			    								(elementObject.getElement_HIMM_Factor()+3);
			    			}
		    			}	
	    			}
		    	}
		    }
			System.out.println(" End of LEFT AXIS");
		}
		
		
	}
	
//	public void horizontalLEFT_AcousticZEROAxis(ElementLocalizer elemLocObject)
//	{
//		
//		System.out.println("/////ZERO AXIS/////");
//
//		double CurrentRobotXPos=positionObject.getCurrentXPosOfRobot();
//		double CurrentRobotYPos=positionObject.getCurrentYPosOfRobot();
//		
//		// As the robot moves left the y value increases
//		double obstaclePosition=0.0;
//		if(CurrentRobotYPos<0)
//		{
//			obstaclePosition=(CurrentRobotYPos+elemLocObject.getDistance());
//			
//		}
//		else
//		{
//			obstaclePosition=(elemLocObject.getDistance()-CurrentRobotYPos);
//		}
//		
//		System.out.println("ZERO AXIS : CurrentRobotXPos= " +CurrentRobotXPos);
//		System.out.println("ZERO AXIS : CurrentRobotYPos= " +CurrentRobotYPos);
//		System.out.println("ZERO AXIS : obstaclePosition= " +obstaclePosition);
//		
//		// So get all the grid elements with the x value as the same and
//		// the y value between current position and obstacle distance
//		int count=0;
//		for(int row = 0;row < noOfrows;row++)
//	    {
//	    	for(int col = 0; col < noOfCols;col++)
//	    	{
//	    		OccupancyGridMatrix elementObject=occupancyGRID[row][col];
//	    		if(CurrentRobotXPos>= elementObject.getElement_squareHeight_s_distance()
//    		    		 && CurrentRobotXPos<= elementObject.getElement_squareHeight_e_distance())
//    			{
//	    			//Calculate the obstacle position
//	    			if(obstaclePosition<0)
//	    			{
//	    				if(elementObject.getElement_squareWidth_s_distance()>=obstaclePosition &&
//		    					obstaclePosition>= elementObject.getElement_squareWidth_e_distance())
//		    			{
//		    				
//		    				System.out.println("ZERO AXIS : obstaclePosition =" +obstaclePosition);
//		    				System.out.println("ZERO AXIS : getElement_squareWidth_s_distance =" +elementObject.getElement_squareWidth_s_distance());
//		    				System.out.println("ZERO AXIS : getElement_squareWidth_e_distance =" +elementObject.getElement_squareWidth_e_distance());
//		    				elementObject.setElement_symbol(AimaConstants.P_OCCUPIED_OBJECT_SYMBOL);
//		    				elementObject.setElement_symbol(" [hl.z] ");
//		    				elementObject.setElement_HIMM_Factor
//		    								(elementObject.getElement_HIMM_Factor()+3);
//		    			}
//		    			
//	    			}
//	    			else if(obstaclePosition>=0)
//	    			{
//	    				if(elementObject.getElement_squareWidth_s_distance()>=obstaclePosition &&
//		    					obstaclePosition<= elementObject.getElement_squareWidth_e_distance())
//		    			{
//		    				
//		    				System.out.println("ZERO AXIS : obstaclePosition =" +obstaclePosition);
//		    				System.out.println("ZERO AXIS : getElement_squareWidth_s_distance =" +elementObject.getElement_squareWidth_s_distance());
//		    				System.out.println("ZERO AXIS : getElement_squareWidth_e_distance =" +elementObject.getElement_squareWidth_e_distance());
//		    				elementObject.setElement_symbol(AimaConstants.P_OCCUPIED_OBJECT_SYMBOL);
//		    				elementObject.setElement_symbol(" [hl.z] ");
//		    				elementObject.setElement_HIMM_Factor
//		    								(elementObject.getElement_HIMM_Factor()+3);
//		    			}
//		    			
//	    			}
//	    			
//    			}
//	    	}
//	    }
//		
//		System.out.println("End of ZERO AXIS");
//		
//	}
	
	public void horizontalLEFT_AcousticRIGHTAxis(ElementLocalizer elemLocObject)
	{
		
		System.out.println("///// RIGHT AXIS /////");
		
		double CurrentRobotXPos=positionObject.getCurrentXPosOfRobot();
		double CurrentRobotYPos=positionObject.getCurrentYPosOfRobot();
		
		// For calculating the right angle to the robot current position
		// here the x value(height) changes, while y value(width)remains same
		//////System.out.println(("elemLocObject.getDistance()" +elemLocObject.getDistance());
		double obstaclePosition=0.0;
		if(CurrentRobotXPos>elemLocObject.getDistance())
		{
			obstaclePosition=(CurrentRobotXPos+elemLocObject.getDistance());
		}
		else
		{
			obstaclePosition=(elemLocObject.getDistance()+CurrentRobotXPos);
		}
		
		
		if(Math.abs(elemLocObject.getDistance())<=mapper.hIMM_15GridElementsDistance)
		{
			System.out.println("RIGHT AXIS :CurrentRobotXPos =" +CurrentRobotXPos);
			System.out.println("RIGHT AXIS :CurrentRobotYPos =" +CurrentRobotYPos);
			System.out.println("RIGHT AXIS :obstaclePosition =" +obstaclePosition);
			
			// So get all the grid elements with the y value as the same and
			// the x value between current position and obstacle distance
			for(int row = 0;row < noOfrows;row++)
		    {
		    	for(int col = 0; col < noOfCols;col++)
		    	{
		    		OccupancyGridMatrix elementObject=occupancyGRID[row][col];
		    		if(CurrentRobotYPos<= elementObject.getElement_squareWidth_s_distance()
	    		    		 && CurrentRobotYPos>= elementObject.getElement_squareWidth_e_distance())
	    			{
		    			
		    			//////System.out.println(("a. " +elementObject.getElement_squareHeight_s_distance());
		    			//Calculate the obstacle position
		    			
		    			if(obstaclePosition<0)
		    			{
		    				if(obstaclePosition<=elementObject.getElement_squareHeight_s_distance()
			    					&& obstaclePosition>=elementObject.getElement_squareHeight_e_distance())
			    			{
			    				System.out.println("RIGHT AXIS :getElement_squareHeight_s_distance =" +elementObject.getElement_squareHeight_s_distance());
			    				System.out.println("RIGHT AXIS :getElement_squareHeight_e_distance =" +elementObject.getElement_squareHeight_e_distance());
			    				System.out.println("RIGHT AXIS :obstaclePosition =" +obstaclePosition);
			    				
			    				elementObject.setElement_symbol(AimaConstants.P_OCCUPIED_OBJECT_SYMBOL);
			    				elementObject.setElement_symbol(" [hl.r] ");
			    				elementObject.setElement_HIMM_Factor
			    								(elementObject.getElement_HIMM_Factor()+3);
			    			}
		    			}
		    			else if(obstaclePosition>=0)
		    			{
		    				if(obstaclePosition>=elementObject.getElement_squareHeight_s_distance()
			    					&& obstaclePosition<=elementObject.getElement_squareHeight_e_distance())
			    			{
			    				System.out.println("RIGHT AXIS :getElement_squareHeight_s_distance =" +elementObject.getElement_squareHeight_s_distance());
			    				System.out.println("RIGHT AXIS :getElement_squareHeight_e_distance =" +elementObject.getElement_squareHeight_e_distance());
			    				System.out.println("RIGHT AXIS :obstaclePosition =" +obstaclePosition);
			    				elementObject.setElement_symbol(AimaConstants.P_OCCUPIED_OBJECT_SYMBOL);
			    				elementObject.setElement_symbol(" [hl.r] ");
			    				elementObject.setElement_HIMM_Factor
			    								(elementObject.getElement_HIMM_Factor()+3);
			    			}
		    			}
		    			
	    			}
		    	}
		    }
			System.out.println("End of RIGHT AXIS");
		}
		
			
	}
	
	// TO DOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO
	public void horizontalLEFT_AcousticLEFTAxis(ElementLocalizer elemLocObject)
	{
		System.out.println("///// LEFT AXIS /////");
		
		double CurrentRobotXPos=positionObject.getCurrentXPosOfRobot();
		double CurrentRobotYPos=positionObject.getCurrentYPosOfRobot();

		// For calculating the left angle to the robot current position
		// here the x value(height) changes, while y value(width)remains same, as that of the robot
		//////System.out.println(("elemLocObject.getDistance()= " +elemLocObject.getDistance());
		
		double obstaclePosition=(CurrentRobotXPos-elemLocObject.getDistance());
		//////System.out.println(("1obstaclePosition =" +obstaclePosition);
		//obstaclePosition=obstaclePosition+AimaConstants.tolerance_for_boundary_grid_elem_width;
//		//////System.out.println(("2obstaclePosition =" +obstaclePosition);
		
		if(Math.abs(elemLocObject.getDistance())<=mapper.hIMM_15GridElementsDistance)
		{
			System.out.println("LEFT AXIS : CurrentRobotXPos =" +CurrentRobotXPos);
			System.out.println("LEFT AXIS : CurrentRobotYPos =" +CurrentRobotYPos);
		    System.out.println("LEFT AXIS : obstaclePosition =" +obstaclePosition);
			
			// So get all the grid elements with the y value as the same and
			// the x value between current position and obstacle distance
			for(int row = 0;row < noOfrows;row++)
		    {
		    	for(int col = 0; col < noOfCols;col++)
		    	{
		    		OccupancyGridMatrix elementObject=occupancyGRID[row][col];
		    		if(CurrentRobotYPos<= elementObject.getElement_squareWidth_s_distance()
	    		    		 && CurrentRobotYPos>= elementObject.getElement_squareWidth_e_distance())
	    			{
		    			
		    			//////System.out.println(("a. " +elementObject.getElement_squareHeight_s_distance());
		    			//Calculate the obstacle position
		    			
		    			if(obstaclePosition<0)
		    			{
		    				if(elementObject.getElement_squareHeight_s_distance()>=obstaclePosition
			    					&& elementObject.getElement_squareHeight_e_distance()<=obstaclePosition)
			    			{
			    				System.out.println(" LEFT AXIS : getElement_squareHeight_s_distance =" +elementObject.getElement_squareHeight_s_distance());
			    				System.out.println(" LEFT AXIS : getElement_squareHeight_e_distance =" +elementObject.getElement_squareHeight_e_distance());
			    				System.out.println(" LEFT AXIS : obstaclePosition =" +obstaclePosition);
			    				
			    				elementObject.setElement_symbol(AimaConstants.P_OCCUPIED_OBJECT_SYMBOL);
			    				elementObject.setElement_symbol(" [hl.l] ");
			    				elementObject.setElement_HIMM_Factor
			    								(elementObject.getElement_HIMM_Factor()+3);
			    			}
		    			}
		    			else if(obstaclePosition>=0)
		    			{
		    				if(elementObject.getElement_squareHeight_s_distance()<=obstaclePosition
			    					&& elementObject.getElement_squareHeight_e_distance()>=obstaclePosition)
			    			{
			    				System.out.println(" LEFT AXIS : getElement_squareHeight_s_distance =" +elementObject.getElement_squareHeight_s_distance());
			    				System.out.println(" LEFT AXIS : getElement_squareHeight_e_distance =" +elementObject.getElement_squareHeight_e_distance());
			    				System.out.println(" LEFT AXIS : obstaclePosition =" +obstaclePosition);
			    				
			    				elementObject.setElement_symbol(AimaConstants.P_OCCUPIED_OBJECT_SYMBOL);
			    				elementObject.setElement_symbol(" [hl.l] ");
			    				elementObject.setElement_HIMM_Factor
			    								(elementObject.getElement_HIMM_Factor()+3);
			    			}
		    			}
	    			}
		    	}
		    }
			System.out.println("End of LEFT AXIS");	
		}
		
		
		
	}
	
//	public void verticalBACKWARD_AcousticZEROAxis(ElementLocalizer elemLocObject)
//	{
//
//		System.out.println("/////ZERO AXIS/////");
//		
//		double CurrentRobotXPos=positionObject.getCurrentXPosOfRobot();
//		double CurrentRobotYPos=positionObject.getCurrentYPosOfRobot();
//		double obstaclePosition=0.0;
//		
//		// For calculating the zero angle to the robot current position
//		// here the x value(height) changes, while y value(width)remains same
//		//double obstaclePosition=(elemLocObject.getDistance()-CurrentRobotXPos);
//		//obstaclePosition=obstaclePosition-AimaConstants.tolerance_for_boundary_grid_elem_height;
//		
//		// X is the height
//		if(CurrentRobotXPos<0)
//		{
//			//////System.out.println(("negative");
//			obstaclePosition=(CurrentRobotXPos-elemLocObject.getDistance());
//			
//		}
//		else
//		{
//			//////System.out.println(("positive");
//			obstaclePosition=(CurrentRobotXPos-elemLocObject.getDistance());
//			
//		}
//		
//		
//		System.out.println("ZERO AXIS:CurrentRobotXPos =" +CurrentRobotXPos);
//		System.out.println("ZERO AXIS:CurrentRobotYPos =" +CurrentRobotYPos);
//		System.out.println("ZERO AXIS:obstaclePosition =" +obstaclePosition);
//		
//		// So get all the grid elements with the y value as the same and
//		// the x value between current position and obstacle distance
//		for(int row = 0;row < noOfrows;row++)
//	    {
//	    	for(int col = 0; col < noOfCols;col++)
//	    	{
//	    		OccupancyGridMatrix elementObject=occupancyGRID[row][col];
//	    		if(CurrentRobotYPos<= elementObject.getElement_squareWidth_s_distance()
//    		    		 && CurrentRobotYPos>= elementObject.getElement_squareWidth_e_distance())
//    			{
//	    			
//	    			//////////System.out.println(("a. " +elementObject.getElement_squareHeight_s_distance());
//	    			//Calculate the obstacle position
//	    			
//	    			if(obstaclePosition<0)
//	    			{
//	    				if(obstaclePosition<=elementObject.getElement_squareHeight_s_distance()
//		    					&& obstaclePosition>=elementObject.getElement_squareHeight_e_distance())
//		    			{
//		    				System.out.println("ZERO AXIS:getElement_squareHeight_s_distance =" +elementObject.getElement_squareHeight_s_distance());
//		    				System.out.println("ZERO AXIS:getElement_squareHeight_e_distance =" +elementObject.getElement_squareHeight_e_distance());
//		    				System.out.println("ZERO AXIS: Obtained!!! obstaclePosition [vf.z] =" +obstaclePosition);
//		    				
//		    				elementObject.setElement_symbol(AimaConstants.P_OCCUPIED_OBJECT_SYMBOL);
//		    				elementObject.setElement_symbol(" [vb.z] ");
//		    				elementObject.setElement_HIMM_Factor
//		    								(elementObject.getElement_HIMM_Factor()+3);
//		    			}
//		    			
//	    			}
//	    			else if(obstaclePosition>=0)
//	    			{
//	    				if(obstaclePosition>=elementObject.getElement_squareHeight_s_distance()
//		    					&& obstaclePosition<=elementObject.getElement_squareHeight_e_distance())
//		    			{
//		    				System.out.println("ZERO AXIS:getElement_squareHeight_s_distance =" +elementObject.getElement_squareHeight_s_distance());
//		    				System.out.println("ZERO AXIS:getElement_squareHeight_e_distance =" +elementObject.getElement_squareHeight_e_distance());
//		    				System.out.println("ZERO AXIS: Obtained!!! obstaclePosition [vf.z] =" +obstaclePosition);
//		    				
//		    				elementObject.setElement_symbol(AimaConstants.P_OCCUPIED_OBJECT_SYMBOL);
//		    				elementObject.setElement_symbol(" [vb.z] ");
//		    				elementObject.setElement_HIMM_Factor
//		    								(elementObject.getElement_HIMM_Factor()+3);
//		    			}
//		    			
//	    			}
//    			}
//	    	}
//	    }
//		System.out.println("End of ZERO AXIS................");
//	}
	
	
	public void verticalBACKWARD_AcousticRIGHTAxis(ElementLocalizer elemLocObject)
	{
		System.out.println("/////RIGHT AXIS/////");
		
		double CurrentRobotXPos=positionObject.getCurrentXPosOfRobot();
		double CurrentRobotYPos=positionObject.getCurrentYPosOfRobot();
	
		double obstaclePosition=0.0;
		if(CurrentRobotYPos<0)
		{
			obstaclePosition=(elemLocObject.getDistance()+CurrentRobotYPos);
			//obstaclePosition=obstaclePosition+AimaConstants.tolerance_for_boundary_grid_elem_width;
		}
		else
		{
			obstaclePosition=(elemLocObject.getDistance()+CurrentRobotYPos);
			//obstaclePosition=obstaclePosition+AimaConstants.tolerance_for_boundary_grid_elem_width;
		}
		
		if(Math.abs(elemLocObject.getDistance())<=mapper.hIMM_15GridElementsDistance)
		{
			System.out.println("RIGHT AXIS: CurrentRobotXPos =" +CurrentRobotXPos);
			System.out.println("RIGHT AXIS: CurrentRobotYPos =" +CurrentRobotYPos);
			System.out.println("RIGHT AXIS: obstaclePosition = " +obstaclePosition);
			
			// So get all the grid elements with the y value as the same and
			// the x value between current position and obstacle distance
			for(int row = 0;row < noOfrows;row++)
		    {
		    	for(int col = 0; col < noOfCols;col++)
		    	{
		    		OccupancyGridMatrix elementObject=occupancyGRID[row][col];
		    		
		    		if(CurrentRobotXPos>= elementObject.getElement_squareHeight_s_distance()
		   		    		 && CurrentRobotXPos<= elementObject.getElement_squareHeight_e_distance())
	    			{
		    			////System.out.println("555.RightAngle.a.col obj= " +elementObject);
		    			
		    			////////////System.out.println(("a. " +elementObject.getElement_squareHeight_s_distance());
		    			//Calculate the obstacle position
		    			
		    			if(obstaclePosition<0)
		    			{
		    				if(obstaclePosition<=elementObject.getElement_squareWidth_s_distance()
			    					&& obstaclePosition>=elementObject.getElement_squareWidth_e_distance())
			    			{
			    				System.out.println("RIGHT AXIS: getElement_squareWidth_s_distance =" +elementObject.getElement_squareWidth_s_distance());
			    				System.out.println("RIGHT AXIS: getElement_squareWidth_e_distance =" +elementObject.getElement_squareWidth_e_distance());
			    				System.out.println("RIGHT AXIS: obstaclePosition!!!  [vb.r] =" +obstaclePosition);
			    				
			    				elementObject.setElement_symbol(AimaConstants.P_OCCUPIED_OBJECT_SYMBOL);
			    				elementObject.setElement_symbol(" [vb.r] ");
			    				elementObject.setElement_HIMM_Factor
			    								(elementObject.getElement_HIMM_Factor()+3);
			    			}
		    			}
		    			else if(obstaclePosition>=0)
		    			{
		    				if(obstaclePosition<=elementObject.getElement_squareWidth_s_distance()
			    					&& obstaclePosition>=elementObject.getElement_squareWidth_e_distance())
			    			{
		    					System.out.println("RIGHT AXIS: getElement_squareWidth_s_distance =" +elementObject.getElement_squareWidth_s_distance());
			    				System.out.println("RIGHT AXIS: getElement_squareWidth_e_distance =" +elementObject.getElement_squareWidth_e_distance());
			    				System.out.println("RIGHT AXIS: obstaclePosition!!! [vb.r] =" +obstaclePosition);
			    				
			    				elementObject.setElement_symbol(AimaConstants.P_OCCUPIED_OBJECT_SYMBOL);
			    				elementObject.setElement_symbol(" [vb.r] ");
			    				elementObject.setElement_HIMM_Factor
			    								(elementObject.getElement_HIMM_Factor()+3);
			    			}
		    			}
		    			
	    			}
		    	}
		    }
			
			//this.displayGRIDInverse();
			//System.exit(0);
			
			System.out.println("End of RIGHT AXIS");
		}
			
		
	}
	
	public void verticalBACKWARD_AcousticLEFTAxis(ElementLocalizer elemLocObject)
	{

		System.out.println("///// LEFT AXIS /////");
		
		double CurrentRobotXPos=positionObject.getCurrentXPosOfRobot();
		double CurrentRobotYPos=positionObject.getCurrentYPosOfRobot();
		double obstaclePosition=0.0;
		
		if(CurrentRobotYPos<0)
		{
			//////System.out.println(("negative");
			obstaclePosition=(CurrentRobotYPos-elemLocObject.getDistance());
			//obstaclePosition=obstaclePosition-AimaConstants.tolerance_for_boundary_grid_elem_height;
		}
		else
		{
			//////System.out.println(("positive");
			obstaclePosition=(CurrentRobotYPos-elemLocObject.getDistance());
			//obstaclePosition=obstaclePosition-AimaConstants.tolerance_for_boundary_grid_elem_height;
		}
		
		if(Math.abs(elemLocObject.getDistance())<=mapper.hIMM_15GridElementsDistance)
		{
			System.out.println("LEFT AXIS : CurrentRobotXPos =" +CurrentRobotXPos);
			System.out.println("LEFT AXIS : CurrentRobotYPos =" +CurrentRobotYPos);
			System.out.println("LEFT AXIS : ObstaclePosition =" +obstaclePosition);
			
			
			// So get all the grid elements with the y value as the same and
			// the x value between current position and obstacle distance
			for(int row = 0;row < noOfrows;row++)
		    {
		    	for(int col = 0; col < noOfCols;col++)
		    	{
		    		OccupancyGridMatrix elementObject=occupancyGRID[row][col];
		    		if(CurrentRobotXPos>= elementObject.getElement_squareHeight_s_distance()
		   		    		 && CurrentRobotXPos<= elementObject.getElement_squareHeight_e_distance())
	    			{
		    			if(obstaclePosition<0)
		    			{
		    				if(elementObject.getElement_squareWidth_s_distance()>=obstaclePosition
		    						&& obstaclePosition>=elementObject.getElement_squareWidth_e_distance())
			    			{
			    				System.out.println("LEFT AXIS : getElement_squareHeight_s_distance =" +elementObject.getElement_squareWidth_s_distance());
			    				System.out.println("LEFT AXIS : getElement_squareWidth_e_distance =" +elementObject.getElement_squareWidth_e_distance());
			    				System.out.println("LEFT AXIS : ObstaclePosition OBTAINED [vb.l] !!! =" +obstaclePosition);
			    				
			    				elementObject.setElement_symbol(AimaConstants.P_OCCUPIED_OBJECT_SYMBOL);
			    				elementObject.setElement_symbol(" [vb.l] ");
			    				elementObject.setElement_HIMM_Factor
			    								(elementObject.getElement_HIMM_Factor()+3);
			    			}
		    			}
		    			else if(obstaclePosition>=0)
		    			{
		    				if(elementObject.getElement_squareWidth_s_distance()>=obstaclePosition
		    						&& obstaclePosition>=elementObject.getElement_squareWidth_e_distance())
			    			{
			    				System.out.println("LEFT AXIS : getElement_squareWidth_s_distance =" +elementObject.getElement_squareWidth_s_distance());
			    				System.out.println("LEFT AXIS : getElement_squareWidth_e_distance =" +elementObject.getElement_squareWidth_e_distance());
			    				System.out.println("LEFT AXIS : ObstaclePosition OBTAINED [vb.l] !!! =" +obstaclePosition);
			    				
			    				elementObject.setElement_symbol(AimaConstants.P_OCCUPIED_OBJECT_SYMBOL);
			    				elementObject.setElement_symbol(" [vb.l] ");
			    				elementObject.setElement_HIMM_Factor
			    								(elementObject.getElement_HIMM_Factor()+3);
			    			}
		    			}	
	    			}
		    	}
		    }
			System.out.println(" End of LEFT AXIS");
		}
		
		
		
	}
	
	
	
	public void endOfFollowBehavior()
	{
		//System.out.println("endOfFollowBehavior!!!!");
		
			// Here just check if robot reached the top left points(approx),
			// means the robot has finished its explore behavior
			//displayGRID();
		
			//System.out.println("Reached home!!");
			
			////System.out.println(("Stop the robot................");
			
			controllerObject.setSpeeds(0.0,0.0);
			try
			{
				ImageProcessing imageProcObject=new ImageProcessing();
				imageProcObject.createGIFImage(occupancyGRID);
			}
			catch (Exception e) {
				// TODO: handle exception
				System.out.println("Image Exc caught!! = " +e);
			}
			
			
			
			System.exit(0);			
	}
	
//	public static void displayGRID()
//	{
//		// Display the Occupancy grid
//		for(int row = 0;row < noOfrows;row++)
//	    {
//			String element="";
//	    	for(int col = 0; col < noOfCols;col++)
//	    	{
//	    		OccupancyGridMatrix elementObject=occupancyGRID[row][col];
//	    		element=element+elementObject.getElement_HIMM_Factor();
//	    	}
//	    	System.out.print(element);
//	    	System.out.print("\n");
//	    }
//		
//		for(int row = 0;row < noOfrows;row++)
//	    {
//			String element="";
//	    	for(int col = 0; col < noOfCols;col++)
//	    	{
//	    		OccupancyGridMatrix elementObject=occupancyGRID[row][col];
//	    		element=element+elementObject.getElement_symbol();
//	    	}
//	    	System.out.print(element);
//	    	System.out.print("\n");
//	    }
//		
//	}
	
	public static void displayGRIDInverse()
	{
		// Display the Occupancy grid
		System.out.println("--------------------------------------------------");
		System.out.println("mapper.hIMM_15GridElementsDistance = " + mapper.hIMM_15GridElementsDistance);
		System.out.println("GRID");
		for(int row = noOfrows-1 ;row >= 0 ;row--)
	    {
			String element="";
	    	for(int col = 0; col < noOfCols;col++)
	    	{
	    		OccupancyGridMatrix elementObject=occupancyGRID[row][col];
	    		element=element+elementObject.getElement_symbol();
	    	}
	    	System.out.print(element);
	    	System.out.print("\n");
	    }
		System.out.println("--------------------------------------------------");
		
	}
	
}
