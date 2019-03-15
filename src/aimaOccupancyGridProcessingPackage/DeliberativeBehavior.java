package aimaOccupancyGridProcessingPackage;

import java.util.Timer;
import java.util.TimerTask;

import org.json.simple.JSONArray;

import aimaJsonProcessingPackage.JsonRobotLaserParser;
import aimaKompaiMapperPackage.mapper;
import aimaKompaiMapperPackage.KompaiRobotController;
import aimaKompaiMapperPackage.KompaiRobotDirections;
import aimaLocalizationPackage.PositionLocalizer;
import aimaMapPlottingPackage.ImageProcessing;
import aimaResources.AimaConstants;

public class DeliberativeBehavior 
{
	
	public static OccupancyGridMatrix[][] displayOccupancyGRID=null;
	
	KompaiRobotDirections directionsObject = new KompaiRobotDirections();
	PositionLocalizer positionObject=new PositionLocalizer();
	ReactiveBehavior reactiveObject=new ReactiveBehavior();
	KompaiRobotController controllerObject=new KompaiRobotController();
	JsonRobotLaserParser laserObject=new JsonRobotLaserParser();
	
	boolean continueRobotForwardTillMinWall=false;
	boolean continueTurningLeft=false;
	public static Timer timer = new Timer();
	public static Timer timerFollowWall = new Timer();
	
	public static boolean followWallStartTrack=false;
	
	public DeliberativeBehavior()
	{
	}
	
	public void followRightWallAlgorithm()
	{
		
		//System.out.println();
		//System.out.println("###########################");
		//System.out.println("followRightWallAlgorithm : start");
		
		followWallStartTrack=true;
		
		// Step1: Move the robot forward
		// code fix
		// directionsObject.moveKompaiForward();
		
		// Step2: Call the reactive behavior to ensure
		// obstacle avoidance
		this.startFollowRightWallBehavior();	
		
		//System.out.println("###########################");
		//System.out.println("followRightWallAlgorithm : end");
		//System.out.println();
	}
	
	// Changed this method...if need to revert changes refer other class
	public void startFollowRightWallBehavior()
	{
		//System.out.println("In startFollowRightWallBehavior");
		int delay = 0;   
		// code fix, waiting for the other code to be executed
    	int period = 2000;  //every 2 sec
    	timerFollowWall = new Timer();

    	timerFollowWall.scheduleAtFixedRate(new TimerTask() 
    	{
    	        public void run() 
    	        {	
    	        	boolean exitBoundary=ensureWithinSquareinputReactive();
	        		if(exitBoundary)
	        		{
	        			//System.out.println("DeliberativeBehavior: inside exitBoundary loop ");
	        			//this.cancel();
    	        		directionsObject.stopRobot();
    	        		// try to move back for some time 
    	        		moveRobotBackwardforConstantTime();
    	        		// the rotate 90 degree
    	        		reactiveObject.rotateRobotLeft90Degrees();
    	        		directionsObject.stopRobot();
    	        		reactiveObject.reactiveObstacleAvoidance();
	        		}
	        		else
	        		{
	        			// Stops every 5 seconds to check for reactive obstacle avoidance
	        			
	        			directionsObject.stopRobot();
	        			reactiveObject.reactiveObstacleAvoidance();
	        		}
    	        }
    	}, delay, period);
	}
	
	
	public void positionRobotToRightWall()
	{
		
		//System.out.println("positionRobotToRightWall:");
		
		// Step1: Turn kompai towards the right wall
		this.rotateRobotRight90Degrees();
		// Step2: Move kompai towards the right wall,
		// till the distance is less than the minimum distance
		this.startRobotForwardTillMinWall();
		
		// Step3: Position the robot to the correct start position
		////System.out.println("");
		////System.out.println("check left!!");
		while(continueTurningLeft==false)
		{ 
			// Should terminate the program execution
		}
		this.rotateRobotLeft90Degrees();	
		
	}
	
	public void rotateRobotRight90Degrees()
	{
		
		if(FillOccupancyGrid.horizontal_direction==false)
		{
			FillOccupancyGrid.horizontal_direction=true;
		}
		else
		{
			FillOccupancyGrid.horizontal_direction=false;
		}
		
		long startTime,endTime;
		startTime = System.currentTimeMillis();   
		while(startTime>0)
		{
			
			directionsObject.turnKompaiRotateRight();
	        endTime = System.currentTimeMillis();
	        // break the loop after 1 second
	        if(endTime-startTime > AimaConstants.robot_rotate_turn_time)
	        	break;
		}
		directionsObject.stopKompaiRobot();
	}
	
	public void rotateRobotLeft90Degrees()
	{
		if(FillOccupancyGrid.horizontal_direction==false)
		{
			FillOccupancyGrid.horizontal_direction=true;
		}
		else
		{
			FillOccupancyGrid.horizontal_direction=false;
		}
		
		long startTime,endTime;
		startTime = System.currentTimeMillis();   
		while(startTime>0)
		{
			
			directionsObject.turnKompaiRotateLeft();
	        endTime = System.currentTimeMillis();
	        // break the loop after 1 second
	        if(endTime-startTime > AimaConstants.robot_rotate_turn_time)
	        	break;
		}
		directionsObject.stopKompaiRobot();
	}
	
	public void startRobotForwardTillMinWall()
	{
		//System.out.println("DeliberativeBehavior.startRobotForwardTillMinWall");
		continueRobotForwardTillMinWall=true;
		int delay = 0;   
    	int period = 2000;  //every 2 sec
    	timer.scheduleAtFixedRate(new TimerTask() 
    	{
    	        public void run() 
    	        {
    	        	if(continueRobotForwardTillMinWall)
    	        	{
    	        		// here before this, need to check if, the robot is within the square
    	        		// input being provided
    	        		boolean exitBoundary=ensureWithinSquareinput();
    	        		if(exitBoundary)
    	        		{
    	        			////System.out.println("in 1");
    	        			this.cancel();
        	        		continueRobotForwardTillMinWall=false;
        	        		directionsObject.stopRobot();
    		            	continueTurningLeft=true;
    	        		}
    	        		else
    	        		{
    	        			////System.out.println("in 2");
    	        			moveRobotForwardTillMinWallDistance();
    	        		}
    	        	}
    	        	else
    	        	{
    	        		////System.out.println("in 3");
    	        		this.cancel();
    	        		continueRobotForwardTillMinWall=false;
    	        	}
    	        	
    	        }
    	}, delay, period);
	}

	public void moveRobotForwardTillMinWallDistance()
	{
		//System.out.println("DeliberativeBehavior.moveRobotForwardTillMinWallDistance");		
		JSONArray distances_array=null;
		double laserDistance=0.0;
		distances_array= positionObject.getLaserDistanceArray();
		// Once the distances_array is got,
	    for(int i=0;i<distances_array.size();i++)
	    {
	    	////System.out.println("continueRobotForwardTillMinWall="+continueRobotForwardTillMinWall);
	    	if(continueRobotForwardTillMinWall)
	    	{
	    		if(i>AimaConstants.avoid_obstacle_laser_start_index && 
		    			i< AimaConstants.avoid_obstacle_laser_end_index)
		    	{
		    		Object distanceVal=distances_array.get(i);
			    	laserDistance=new Double(distanceVal.toString()).doubleValue();
			    	////System.out.println("cclaserDistance lll > " +laserDistance);
			    	if (laserDistance > AimaConstants.avoid_obstacle_distance)
			    	{
			    		////System.out.println("yess " +laserDistance);
			    		directionsObject.moveKompaiForward();
		            }
			    	else
			    	{
			    		////System.out.println("   ");
			    		//System.out.println("!!!!!danger:!!!" +laserDistance);
			    		////System.out.println("   ");
		            	continueRobotForwardTillMinWall=false;
		            	timer.cancel();
		            	directionsObject.stopRobot();
		            	continueTurningLeft=true;
			            break;    
			    	}
		    	}
	    	}
	    	else
	    	{
	    		break;
	    	}
	    }
		
	}

	public boolean ensureWithinSquareinput()
	{
		////System.out.println("in ensureWithinSquareinput");
		boolean exitBoundary=false;
		double CurrentRobotXPos=positionObject.getCurrentXPosOfRobot();
		double CurrentRobotYPos=positionObject.getCurrentYPosOfRobot();
		if(CurrentRobotXPos<= mapper.x2_top_right_coordinate)
		{
			if(mapper.y1_lower_left_coordinate >
						mapper.y2_top_right_coordinate)
			{
				//System.out.println("moving right-val decreases");
				if(CurrentRobotYPos>= mapper.y2_top_right_coordinate)
	    		{
					////System.out.println("Within the square!!");
	    		}
			}
			else
			{
				//System.out.println("trying to exit square boundary!!!");
				exitBoundary=true;
			}
		}
		return exitBoundary;
	}
	
	public boolean ensureWithinSquareinputReactive()
	{
		//System.out.println("DeliberativeBehavior: inside ensureWithinSquareinputReactive ");
		////System.out.println("in ensureWithinSquareinputReactive------------------------------");
		boolean exitBoundary=false;
		double CurrentRobotXPos=positionObject.getCurrentXPosOfRobot();
		double CurrentRobotYPos=positionObject.getCurrentYPosOfRobot();
		// x-height, y-width
		if(CurrentRobotXPos>= (mapper.x2_top_right_coordinate-AimaConstants.exit_boundary_check))
		{
			//System.out.println("DeliberativeBehavior: ensureWithinSquareinputReactive: crosses top boundary!!!");
			exitBoundary=true;
		}
		
		//System.out.println("DeliberativeBehavior: Math.abs(CurrentRobotYPos) = " +Math.abs(CurrentRobotYPos));
		//System.out.println("DeliberativeBehavior: Math.abs(ExecuteKompaiMapper.y1_lower_left_coordinate) = " +Math.abs(ExecuteKompaiMapper.y1_lower_left_coordinate));
		//System.out.println("DeliberativeBehavior: Math.abs(ExecuteKompaiMapper.y2_top_right_coordinate) = " +Math.abs(ExecuteKompaiMapper.y2_top_right_coordinate));
		//System.out.println("");
		
		// Left Boundary
		// To avoid condition where, +16 and -16 are thought to be near
		// both are negative
		
		//	double difference1=(Math.abs(CurrentRobotYPos)-Math.abs(ExecuteKompaiMapper.y1_lower_left_coordinate));
		//	if(Math.abs(difference1)<=3)
		//	{
		//		//System.out.println("DeliberativeBehavior: ensureWithinSquareinputReactive: crosses left boundary, difference1=!!!"+difference1);
		//		exitBoundary=true;
		//	}
		
		if(CurrentRobotYPos<0 && mapper.y1_lower_left_coordinate<0)
		{
			double difference1=(Math.abs(CurrentRobotYPos)-Math.abs(mapper.y1_lower_left_coordinate));
			if(Math.abs(difference1)<=3)
			{
				//System.out.println("DeliberativeBehavior: ensureWithinSquareinputReactive: crosses left boundary,difference1=!!!"+difference1);
				exitBoundary=true;
			}
		}
		// both are positive
		else if(CurrentRobotYPos>0 && mapper.y1_lower_left_coordinate>0)
		{
			double difference1=(Math.abs(CurrentRobotYPos)-Math.abs(mapper.y1_lower_left_coordinate));
			if(Math.abs(difference1)<=3)
			{
				//System.out.println("DeliberativeBehavior: ensureWithinSquareinputReactive: crosses left boundary,difference1=!!!"+difference1);
				exitBoundary=true;
			}
		}
		// One is positive and other negative, then dont need abs 
		else
		{
			// since as u go to left the value increases
			double difference1=(mapper.y1_lower_left_coordinate)-(CurrentRobotYPos);
			if((difference1)<=3)
			{
				//System.out.println("DeliberativeBehavior: ensureWithinSquareinputReactive: crosses left boundary,difference1=!!!"+difference1);
				exitBoundary=true;
			}
		}

		// Right Boundary
		// To avoid condition where, +16 and -16 are thought to be near
		// both are negative
		if(CurrentRobotYPos<0 && mapper.y2_top_right_coordinate<0)
		{
			double difference2=(Math.abs(CurrentRobotYPos)-Math.abs(mapper.y2_top_right_coordinate));
			if(Math.abs(difference2)<=3)
			{
				//System.out.println("DeliberativeBehavior: ensureWithinSquareinputReactive: crosses right boundary,difference2=!!!"+difference2);
				exitBoundary=true;
			}
		}
		// both are positive
		else if(CurrentRobotYPos>0 && mapper.y2_top_right_coordinate>0)
		{
			double difference2=(Math.abs(CurrentRobotYPos)-Math.abs(mapper.y2_top_right_coordinate));
			if(Math.abs(difference2)<=3)
			{
				//System.out.println("DeliberativeBehavior: ensureWithinSquareinputReactive: crosses right boundary,difference2=!!!"+difference2);
				exitBoundary=true;
			}
		}
		// One is positive and other negative, then dont need abs 
		else
		{
			double difference2=(CurrentRobotYPos)-(mapper.y2_top_right_coordinate);
			if((difference2)<=3)
			{
				//System.out.println("DeliberativeBehavior: ensureWithinSquareinputReactive: crosses right boundary,difference2=!!!"+difference2);
				exitBoundary=true;
			}
		}
		
		// Bottom boundary
		//To avoid condition where, +16 and -16 are thought to be near
		// both are negative
		if(CurrentRobotXPos<0 && mapper.x1_lower_left_coordinate<0)
		{
			double difference3=(Math.abs(CurrentRobotXPos)-Math.abs(mapper.x1_lower_left_coordinate));
			if(Math.abs(difference3)<=3)
			{
				//System.out.println("DeliberativeBehavior: ensureWithinSquareinputReactive: crosses bottom boundary,difference3=!!!"+difference3);
				//System.out.println("CurrentRobotXPos = " + CurrentRobotXPos);
				//System.out.println("1.difference3=" +difference3);
				directionsObject.stopRobot();
				//System.exit(0);
				exitBoundary=true;
			}
		}
		// both are positive
		else if(CurrentRobotXPos>0 && mapper.x1_lower_left_coordinate>0)
		{
			double difference3=(Math.abs(CurrentRobotXPos)-Math.abs(mapper.x1_lower_left_coordinate));
			if(Math.abs(difference3)<=3)
			{
				//System.out.println("DeliberativeBehavior: ensureWithinSquareinputReactive: crosses bottom boundary,difference3=!!!"+difference3);
				//System.out.println("CurrentRobotXPos = " + CurrentRobotXPos);
				//System.out.println("2.difference3=" +difference3);
				directionsObject.stopRobot();
				//System.exit(0);
				exitBoundary=true;
			}
		}
		// One is positive and other negative, then dont need abs 
		else
		{
			double difference3=(CurrentRobotXPos)-(mapper.x1_lower_left_coordinate);
			if((difference3)<=3)
			{
				//System.out.println("DeliberativeBehavior: ensureWithinSquareinputReactive: crosses bottom boundary,difference3=!!!"+difference3);
				//System.out.println("CurrentRobotXPos = " + CurrentRobotXPos);
				//System.out.println("3.difference3=" +difference3);
				directionsObject.stopRobot();
				//System.exit(0);
				exitBoundary=true;
			}
		}
		
		
		// Determine the exit square
		// To avoid condition where, +16 and -16 are thought to be near
		// both are negative
		if(CurrentRobotYPos<0 && 
				CurrentRobotXPos<0 &&
				mapper.y2_top_right_coordinate <0 &&
				mapper.x1_lower_left_coordinate <0)
		{
			double exitDiff1=(Math.abs(CurrentRobotYPos)-Math.abs(mapper.y2_top_right_coordinate));
			double exitDiff2=(Math.abs(CurrentRobotXPos)-Math.abs(mapper.x1_lower_left_coordinate));
			if(Math.abs(exitDiff1)<=5 && 
					Math.abs(exitDiff2)<=5 )
			{
				//System.out.println("DeliberativeBehavior: ensureWithinSquareinputReactive: crosses right boundary,difference2=!!!"+difference2);
				System.out.println("At END Square: condition1");
				directionsObject.stopKompaiRobot();
				FillOccupancyGrid.displayGRIDInverse();
				
				this.createDisplayOccupancyGrid();
				ImageProcessing imageProcObject=new ImageProcessing();
				imageProcObject.createGIFImage(displayOccupancyGRID);
				
				System.exit(0);
			}
		}
		// both are positive
		else if(CurrentRobotYPos>0 && 
				CurrentRobotXPos>0 &&
				mapper.y2_top_right_coordinate>0 &&
				mapper.x1_lower_left_coordinate>0)
		{
			double exitDiff1=(Math.abs(CurrentRobotYPos)-Math.abs(mapper.y2_top_right_coordinate));
			double exitDiff2=(Math.abs(CurrentRobotXPos)-Math.abs(mapper.x1_lower_left_coordinate));
			if(Math.abs(exitDiff1)<=3 && 
					Math.abs(exitDiff2)<=3 )
			{
				//System.out.println("DeliberativeBehavior: ensureWithinSquareinputReactive: crosses right boundary,difference2=!!!"+difference2);
				System.out.println("At END Square: condition2");
				directionsObject.stopKompaiRobot();
				FillOccupancyGrid.displayGRIDInverse();
				
				this.createDisplayOccupancyGrid();
				ImageProcessing imageProcObject=new ImageProcessing();
				imageProcObject.createGIFImage(displayOccupancyGRID);
				
				System.exit(0);
			}
		}
		// One is positive and other negative, then dont need abs 
		else
		{
			double exitDiff1=(CurrentRobotYPos)-(mapper.y2_top_right_coordinate);
			double exitDiff2=(CurrentRobotXPos)-(mapper.x1_lower_left_coordinate);
			if(Math.abs(exitDiff1)<=5 && 
					Math.abs(exitDiff2)<=5 )
			{
				//System.out.println("DeliberativeBehavior: ensureWithinSquareinputReactive: crosses right boundary,difference2=!!!"+difference2);
				System.out.println("At END Square: condition3");
				directionsObject.stopKompaiRobot();
				FillOccupancyGrid.displayGRIDInverse();
				
				this.createDisplayOccupancyGrid();
				ImageProcessing imageProcObject=new ImageProcessing();
				imageProcObject.createGIFImage(displayOccupancyGRID);
				
				System.exit(0);
			}
		}
		
		return exitBoundary;
	}
	
	
	public void moveRobotBackwardforConstantTime()
	{
		
		//System.out.println("DeliberativeBehavior: moveRobotBackwardforConstantTime");
		
		long startTime,endTime;
		startTime = System.currentTimeMillis();   
		while(startTime>0)
		{
			directionsObject.moveKompaiBackward();
	        endTime = System.currentTimeMillis();
	        // break the loop after 1 second
	        if(endTime-startTime > AimaConstants.back_space_robot_backward)
	        	break;
		}
		directionsObject.stopKompaiRobot();
	}
	
	
	public static void createDisplayOccupancyGrid()
	{
		
		displayOccupancyGRID= 
			new OccupancyGridMatrix[mapper.noOfrows_image][mapper.noOfCols_image];;
		
		int dRow=0;
		for(int row = mapper.noOfrows_image-1 ;row >= 0 ;row--)
	    {
			String element="";
			
	    	for(int col = 0; col < mapper.noOfCols_image;col++)
	    	{
	    		OccupancyGridMatrix elementObject=FillOccupancyGrid.occupancyGRID[row][col];
	    		if(elementObject.getElement_symbol().equalsIgnoreCase(AimaConstants.P_EMPTY_OBJECT_SYMBOL))
	    		{
	    			//element=element+elementObject.getElement_symbol();
	    			elementObject.setElement_symbol(AimaConstants.P_EMPTY_OBJECT_SYMBOL);
	    		}
	    		else if(elementObject.getElement_symbol().equalsIgnoreCase(AimaConstants.P_START_OBJECT_SYMBOL))
	    		{
	    			//element=element+elementObject.getElement_symbol();
	    			elementObject.setElement_symbol(AimaConstants.P_START_OBJECT_SYMBOL);
	    		}
	    		else
	    		{
	    			//element=element+AimaConstants.P_OCCUPIED_OBJECT_SYMBOL;
	    			elementObject.setElement_symbol(AimaConstants.P_OCCUPIED_OBJECT_SYMBOL);
	    		}
	    		displayOccupancyGRID[dRow][col]=elementObject;
	    	}
	    	dRow++;
	    	System.out.print(element);
	    }
	}
	
//	public void rotateRobot360Degrees()
//	{
//		long startTime,endTime;
//		startTime = System.currentTimeMillis();   
//		while(startTime>0)
//		{
//			
//			// Step1: Get the laser data from the current position
//			// And determine the obstacle distance 
//		    double obstacleDistanceAtAcousticAxis=positionObject.getLaserDataAtAcousticAxis();
//			////System.out.println("5. obstacleDistanceAtAcousticAxis = "+ obstacleDistanceAtAcousticAxis );
//			// Step2: Get the grid elements with this distance
//			
//			
//			directionsObject.turnKompaiRotateRight();
//	        endTime = System.currentTimeMillis();
//	        // break the loop after 1 second
//	        if(endTime-startTime > AimaConstants.robot_rotate_turn_time)
//	        	break;
//		}
//		directionsObject.stopKompaiRobot();
//	}

}
