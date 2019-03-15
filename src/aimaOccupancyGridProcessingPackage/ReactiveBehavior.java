package aimaOccupancyGridProcessingPackage;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import org.json.simple.JSONArray;

import aimaJsonProcessingPackage.JsonRobotLaserParser;
import aimaKompaiMapperPackage.mapper;
import aimaKompaiMapperPackage.KompaiRobotController;
import aimaKompaiMapperPackage.KompaiRobotDirections;
import aimaLocalizationPackage.PositionLocalizer;
import aimaResources.AimaConstants;


public class ReactiveBehavior 
{
	PositionLocalizer positionObject=new PositionLocalizer();
	KompaiRobotController controllerObject=new KompaiRobotController();
	KompaiRobotDirections directionsObject = new KompaiRobotDirections();
	
	public static int trackRightSpaceMovement=0;
	
	boolean continueRobotForwardTillMinWallReactive=false;
	boolean continueTurningLeftReactive=false;
	boolean continueRobotPositionToRightWall=false;
	public static Timer timerReactive = null;
	OccupancyGridMatrix Start_ElementObject=null;
	
	public static boolean horizontal_direction_reactiveTrack=false;
	
	public void reactiveObstacleAvoidance()
	{
		JSONArray distances_array=null;
		double laserDistance=0.0;
		distances_array= positionObject.getLaserDistanceArray();
		// Once the distances_array is got,
	    for(int i=0;i<distances_array.size();i++)
	    {
	    	if(i>AimaConstants.avoid_obstacle_laser_start_index && 
	    			i< AimaConstants.avoid_obstacle_laser_end_index)
	    	{
	    		Object distanceObj=distances_array.get(i);
	    		// Fix for the class cast exception
	    		Long l = null;
	    	    double d = 0.0;
	    		  
	    		  if(distanceObj instanceof Double)
		    	  {
		    		  ////System.out.println("You are a double");
		    		  laserDistance = new Double((Double) distanceObj);
		    		  //System.out.println("reactiveObstacleAvoidance_laserDistance = " + laserDistance);
		    		  //System.exit(0);
		    	  }
	    		  else if(distanceObj instanceof Long)
		    	  {
		    		  ////System.out.println("You are a Long");
		    		  l = new Long((Long) distanceObj);
			    	  d=l.doubleValue();
			    	  laserDistance = new Double((Double) d);
		    		  //System.out.println("reactiveObstacleAvoidance_laserDistance = " + laserDistance);
		    	  }
	    		
		    	if (laserDistance > AimaConstants.avoid_obstacle_distance)
		    	{
		    		// Get the start element object
		    		Start_ElementObject=OccupancyGridAlgm.startRobotPos_ElementObject;
		    		double CurrentRobotXPos=positionObject.getCurrentXPosOfRobot();
		    		double CurrentRobotYPos=positionObject.getCurrentYPosOfRobot();
		    		if(CurrentRobotXPos>= Start_ElementObject.getElement_squareHeight_s_distance() 
	    		    		 && CurrentRobotXPos<= Start_ElementObject.getElement_squareHeight_e_distance()
	    		    		 && CurrentRobotYPos<= Start_ElementObject.getElement_squareWidth_s_distance()
	    		    		 && CurrentRobotYPos>= Start_ElementObject.getElement_squareWidth_e_distance())
	    			{
		    			// Then we have reached the start position
		    			// So we can stop the robot
		    			//System.out.println("Reached!!!");
		    			directionsObject.stopKompaiRobot();
	    			}
		    		else
		    		{
		    			//System.out.println("reactiveObstacleAvoidance : Move robot forward by reactive....");
		    			
			    		directionsObject.moveKompaiReativeForward();
			    		
			    		//System.out.println("Reactive - Finished forward movement step");
			    		
			    		//System.out.println("reactiveObstacleAvoidance: horizontal_direction_reactiveTrack= " + horizontal_direction_reactiveTrack);
			    		
			    		// Need not turn right, to check if the robot is already near the exit boundary
			    		
			    		boolean nearSquareBoundary=this.ensureNoRightMoveNearBoundaryReactive();
			    		
			    		//boolean isFactoryExitSquare=this.factoryExit();
			    		
			    		if(horizontal_direction_reactiveTrack && 
			    				!nearSquareBoundary)
			    		{
			    			boolean rightSpaceMove=this.rightSpaceMovement();
				    		//System.out.println("rightSpaceMove= "+rightSpaceMove);
			    		}
			    		
		    		}
	            }
		    	else
		    	{
		    		//System.out.println("!!!!!reactive obstacle:danger:!!!" +laserDistance);
		    		directionsObject.stopKompaiRobot();
		    		this.rotateRobotLeft90Degrees();
		            break;    
		    	}
	    	}
	    }
		
	}
	
	public boolean ensureNoRightMoveNearBoundaryReactive()
	{
		////System.out.println("in ensureWithinSquareinput");
		boolean exitBoundary=false;
		double CurrentRobotXPos=positionObject.getCurrentXPosOfRobot();
		// x-height, y-width
		
		// check for the top square boundary
		if(CurrentRobotXPos>= (mapper.x2_top_right_coordinate-AimaConstants.exit_boundary_check)-2)
		{
			//System.out.println("ensureNoRightMoveNearBoundaryReactive: near the top square boundary!!!");
			exitBoundary=true;
		}
		// check for the bottom square boundary
		else if(CurrentRobotXPos<= (mapper.x1_lower_left_coordinate+AimaConstants.exit_boundary_check))
		{
			//System.out.println("ZZZZZZ : ensureNoRightMoveNearBoundaryReactive: near the bottom square boundary!!!");
			//System.out.println("ZZZZZZ : CurrentRobotXPos = "+ CurrentRobotXPos);
			//System.out.println("ZZZZZZ : Check value = "+
					//(ExecuteKompaiMapper.x1_lower_left_coordinate+AimaConstants.exit_boundary_check));
			exitBoundary=true;
			//directionsObject.stopRobot();
			//System.exit(0);
		}
		
		return exitBoundary;
	}
	
	public boolean rightSpaceMovement()
	{
		boolean rightSpaceMove=false;
		
		//System.out.println("reactiveObstacleAvoidance: Checking if ----- rightSpaceMovement");
		
		JSONArray rightAngle_distances_array=null;
		double laserDistance=0.0;
		rightAngle_distances_array= positionObject.getRightAngleLaserDistanceArray();
		
		////System.out.println(" rightAngle_distances_array = " +rightAngle_distances_array);
			
		// Once the distances_array is got,
	    for(int i=0;i<rightAngle_distances_array.size();i++)
	    {
    		Object distanceObj=rightAngle_distances_array.get(i);    		
    		// Fix for the class cast exception
    		Long l = null;
    	    double d = 0.0;
    	    
    		  if(distanceObj instanceof Double)
	    	  {
	    		  ////System.out.println("rightSpaceMovement:You are a double");
	    		  laserDistance = new Double((Double) distanceObj);
	    		  //System.out.println("rightSpaceMovement:distanceAtRightWall = " + laserDistance);
	    		  //System.exit(0);
	    	  }
    		  else if(distanceObj instanceof Long)
	    	  {
	    		  ////System.out.println("rightSpaceMovement:You are a Long");
	    		  l = new Long((Long) distanceObj);
		    	  d=l.doubleValue();
		    	  laserDistance = new Double((Double) d);
	    		  //System.out.println("rightSpaceMovement:distanceAtRightWall = " + laserDistance);
	    	  }
    		  
	    	if (laserDistance > AimaConstants.right_space_distance)
	    	{
	    		
	    		//System.out.println("ALARM - inside rightSpaceMove");
	    		
	    		rightSpaceMove=true;
	    		
	    		FillOccupancyGrid.turnOffZeroAcousticAxis=true;
	    		
	    		trackRightSpaceMovement++;
	    		//System.out.println("ALARM - laserDistance RightSpaceMovement - " + laserDistance);
	    		//System.out.println("ALARM - trackRightSpaceMovement - " + trackRightSpaceMovement);
	    		
	    		// Move the robot forward for 5 seconds
	    		this.moveRobotForwardforConstantTime();
	    		
	    		// Position the robot towards the right wall
	    		this.positionRobotToRightWallAfterRightSpaceMove();
	    		
	    		// Move the robot forward for 2 seconds
	    		// this.moveRobotForwardforConstantTime2();
	    		//this.moveRobotForwardforConstantTime();
	    		
	    		FillOccupancyGrid.turnOffZeroAcousticAxis=false;
            }
	    	else
	    	{
	    		////System.out.println("do nothing"); 
	    	}
	    }
		return rightSpaceMove;
	    
	}
	
	public void positionRobotToRightWallAfterRightSpaceMove()
	{
		
		//System.out.println("positionRobotToRightWallAfterRightSpaceMove:");
		
		// Step1: Turn kompai towards the right wall
		this.rotateRobotRight90Degrees();
		
		directionsObject.stopRobot();
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
		while(continueTurningLeftReactive==false)
		{ 
			// Should terminate the program execution
		}
		this.rotateRobotLeft90Degrees();	
		
	}
	
	public void startRobotForwardTillMinWall()
	{
		////System.out.println("in RobotForwardTillMinWall");
		continueRobotForwardTillMinWallReactive=true;
		int delay = 0;   
    	int period = 2000;  //every sec
    	timerReactive = new Timer();
    	timerReactive.scheduleAtFixedRate(new TimerTask() 
    	{
    	        public void run() 
    	        {
    	        	if(continueRobotForwardTillMinWallReactive)
    	        	{
    	        		moveRobotForwardTillMinWallDistance();
    	        	}
    	        	else
    	        	{
    	        		this.cancel();
    	        	}
    	        	
    	        }
    	}, delay, period);
	}
	
	public void moveRobotForwardTillMinWallDistance()
	{
		JSONArray distances_array=null;
		double laserDistance=0.0;
		distances_array= positionObject.getLaserDistanceArray();
		// Once the distances_array is got,
	    for(int i=0;i<distances_array.size();i++)
	    {
	    	if(continueRobotForwardTillMinWallReactive)
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
		            	continueRobotForwardTillMinWallReactive=false;
		            	timerReactive.cancel();
		            	directionsObject.stopRobot();
		            	continueTurningLeftReactive=true;
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
	
	
	public void moveRobotForwardforConstantTime()
	{
		
		//System.out.println("In moveRobotForwardforConstantTime");
		
		long startTime,endTime;
		startTime = System.currentTimeMillis();   
		while(startTime>0)
		{
			directionsObject.moveKompaiForward();
	        endTime = System.currentTimeMillis();
	        // break the loop after 1 second
	        if(endTime-startTime > AimaConstants.right_space_robot_forward)
	        	break;
		}
		directionsObject.stopKompaiRobot();
	}
	
	
	public void moveRobotForwardforConstantTime2()
	{
		
		//System.out.println("In moveRobotForwardforConstantTime2");
		boolean continueMovement=true;
		
		long startTime,endTime;
		startTime = System.currentTimeMillis();   
		while(startTime>0)
		{
        	double CurrentRobotXPos=positionObject.getCurrentXPosOfRobot();
    		double CurrentRobotYPos=positionObject.getCurrentYPosOfRobot();
    		if(CurrentRobotXPos<= 
    			mapper.x2_top_right_coordinate-AimaConstants.min_check_boundary_condition)
    		{
    			if(mapper.y1_lower_left_coordinate >
							mapper.y2_top_right_coordinate)
				{
					//System.out.println("moving right-val decreases");
					if(CurrentRobotYPos>= 
						mapper.y2_top_right_coordinate-AimaConstants.min_check_boundary_condition)
    	    		{
						continueMovement=true;
						continueRobotPositionToRightWall=true;
    	    		}
				}
				else if(mapper.y1_lower_left_coordinate <
								mapper.y2_top_right_coordinate)
				{
					//System.out.println("moving left-val increases");
					if(CurrentRobotYPos<= 
						mapper.y2_top_right_coordinate-AimaConstants.min_check_boundary_condition)
    	    		{
						continueMovement=true;
						continueRobotPositionToRightWall=true;
    	    		}
				}
				else
				{
					//System.out.println("trying to exit boundary!!!");
					continueMovement=true;
					continueRobotPositionToRightWall=false;
					rotateRobotLeft90Degrees();
				}
    		}
    		else
    		{
    			//System.out.println("trying to exit boundary!!!");
				continueMovement=true;
				continueRobotPositionToRightWall=false;
				rotateRobotLeft90Degrees();
    		}
        
			if(continueMovement)
			{ 
				directionsObject.moveKompaiForward();
		        endTime = System.currentTimeMillis();
		        // break the loop after 1 second
		        if(endTime-startTime > AimaConstants.right_space_robot_forward2)
		        	break;
			}
			
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
		
		// code fix. for tracking the right space movement
		// only during horizontal movement
		if(horizontal_direction_reactiveTrack==false)
		{
			horizontal_direction_reactiveTrack=true;
		}
		else
		{
			horizontal_direction_reactiveTrack=false;
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
		
		// code fix. for tracking the right space movement
		// only during horizontal movement
		if(horizontal_direction_reactiveTrack==false)
		{
			horizontal_direction_reactiveTrack=true;
		}
		else
		{
			horizontal_direction_reactiveTrack=false;
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
	
	
	public boolean factoryExit()
	{
		
		boolean factoryExitFlag=false;
		double CurrentRobotXPos=positionObject.getCurrentXPosOfRobot();
		double CurrentRobotYPos=positionObject.getCurrentYPosOfRobot();
		
		// Right Factory Exit Boundary
		// To avoid condition where, +16 and -16 are thought to be near
		// both are negative
		if(CurrentRobotYPos<0 && AimaConstants.y2_right_factoryExit_coordinate<0)
		{
			double difference2=(Math.abs(CurrentRobotYPos)-Math.abs(AimaConstants.y2_right_factoryExit_coordinate));
			if(Math.abs(difference2)<=3)
			{
				System.out.println("factoryExit : crosses right boundary,difference2=!!!"+difference2);
				factoryExitFlag=true;
			}
		}
		// both are positive
		else if(CurrentRobotYPos>0 && AimaConstants.y2_right_factoryExit_coordinate>0)
		{
			double difference2=(Math.abs(CurrentRobotYPos)-Math.abs(AimaConstants.y2_right_factoryExit_coordinate));
			if(Math.abs(difference2)<=3)
			{
				System.out.println("factoryExit: crosses right boundary,difference2=!!!"+difference2);
				factoryExitFlag=true;
			}
		}
		// One is positive and other negative, then dont need abs 
		else
		{
			double difference2=(CurrentRobotYPos)-(AimaConstants.y2_right_factoryExit_coordinate);
			if((difference2)<=3)
			{
				System.out.println("factoryExit: ensureWithinSquareinputReactive: crosses right boundary,difference2=!!!"+difference2);
				factoryExitFlag=true;
			}
		}
		return factoryExitFlag;
	}
	
}
