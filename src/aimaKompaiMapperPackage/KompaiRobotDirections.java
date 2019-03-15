/*
 * @author mcs10smn
 * Department of computing science
 */
package aimaKompaiMapperPackage;

import aimaLocalizationPackage.PositionLocalizer;
import aimaOccupancyGridProcessingPackage.FillOccupancyGrid;
import aimaResources.AimaConstants;



// TODO: Auto-generated Javadoc
/**
 * The Class ControlDirections.
 */
public class KompaiRobotDirections 
{

	KompaiRobotController controllerObject=new KompaiRobotController();
	FillOccupancyGrid fillGRIDObject=new FillOccupancyGrid();
	
	
	public void startRobot()
	{
		double angularSpeed = 0;
		double linearSpeed = 0;
		
		angularSpeed=0;
		linearSpeed=linearSpeed+AimaConstants.robot_linear_speed_control;
		
		controllerObject.setSpeeds(angularSpeed, linearSpeed);
	}
	
	public void stopRobot()
	{
		controllerObject.setSpeeds(0.0,0.0);
	}
	
	public void turnKompaiLeft()
	{
		double angularSpeed = 0;
		double linearSpeed = 0;
		
		angularSpeed=angularSpeed+
					AimaConstants.robot_angular_speed_control;	
		
		controllerObject.setSpeeds
				(angularSpeed, linearSpeed);
	}
	
	public void turnKompaiRotateLeft()
	{
		double angularSpeed = 0;
		double linearSpeed = 0;
		
		angularSpeed=angularSpeed+
					AimaConstants.robot_rotate_angular_speed_control;
		
		controllerObject.setSpeeds
				(angularSpeed, linearSpeed);
	}
	
	
	public void turnKompaiRight()
	{
		double angularSpeed = 0;
		double linearSpeed = 0;
		
		angularSpeed=angularSpeed-
					AimaConstants.robot_angular_speed_control;
		
		controllerObject.setSpeeds
				(angularSpeed, linearSpeed);
	}
	
	public void turnKompaiRotateRight()
	{
		double angularSpeed = 0;
		double linearSpeed = 0;
		
		angularSpeed=angularSpeed-
					AimaConstants.robot_rotate_angular_speed_control;
		
		controllerObject.setSpeeds
				(angularSpeed, linearSpeed);
	}
	
	public void moveKompaiForward()
	{
		
		////System.out.println("In moveKompaiForward METHOD");
		
		double angularSpeed = 0;
		double linearSpeed = 0;
		
		linearSpeed=linearSpeed+
					AimaConstants.robot_linear_speed_control;
		
		try
		{
			// To fill the occupancy grid
			fillGRIDObject.fillOccupancyGridAlgm();
		}
		catch (Exception e) 
		{
			// TODO: handle exception
			//System.out.println("Exception= " +e);
		}
		
		//System.out.println("Finished filling!!");
		
		controllerObject.setSpeeds
			(angularSpeed, linearSpeed);
		
		////System.out.println(" END OF MOVE FORWARD");
	}
	
	
	public void moveKompaiReativeForward()
	{
		
		////System.out.println("In moveKompaiReativeForward METHOD");
		
		double angularSpeed = 0;
		double linearSpeed = 0;
		
		linearSpeed=linearSpeed+
					AimaConstants.robot_linear_speed_control;
		
		
			try
			{
				// To fill the occupancy grid
				fillGRIDObject.fillOccupancyGridAlgm();
			}
			catch (Exception e) 
			{
				// TODO: handle exception
				//System.out.println("Exception= " +e);
			}
					
			//System.out.println("Finished filling!!");
		
		
		controllerObject.setSpeeds
			(angularSpeed, linearSpeed);
		
		////System.out.println(" END OF MOVE FORWARD");
		
	}
	
	
	
	public void moveKompaiForwardRight()
	{
		double angularSpeed = 0;
		double linearSpeed = 0;
		
		linearSpeed=linearSpeed+0.1;
		angularSpeed=angularSpeed-0.5;
			
		controllerObject.setSpeeds
			(angularSpeed, linearSpeed);
	}
	
	public void moveKompaiForwardLeft()
	{
		double angularSpeed = 0;
		double linearSpeed = 0;
		
		linearSpeed=linearSpeed+0.1;
		angularSpeed=angularSpeed+0.5;
			
		controllerObject.setSpeeds
			(angularSpeed, linearSpeed);
	}
	
	
	public void moveKompaiBackward()
	{
		double angularSpeed = 0;
		double linearSpeed = 0;
		
		linearSpeed=linearSpeed-
					AimaConstants.robot_linear_speed_control;
		
		controllerObject.setSpeeds
				(angularSpeed, linearSpeed);
	}
	
	
	public void stopKompaiRobot()
	{
		controllerObject.setSpeeds(0.0,0.0);
		FillOccupancyGrid.displayGRIDInverse();
		
	}
	
}
