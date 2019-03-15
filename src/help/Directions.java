package help;

import aimaKompaiMapperPackage.KompaiRobotController;
import aimaLocalizationPackage.PositionLocalizer;
import aimaMapPlottingPackage.ImageProcessing;
import aimaOccupancyGridProcessingPackage.DeliberativeBehavior;
import aimaOccupancyGridProcessingPackage.FillOccupancyGrid;
import aimaOccupancyGridProcessingPackage.ReactiveBehavior;
import aimaResources.AimaConstants;

/*
 * @author mcs10smn
 * Department of computing science
 */


// TODO: Auto-generated Javadoc
/**
 * The Class ControlDirections.
 */
public class Directions 
{

	KompaiRobotController controllerObject=new KompaiRobotController();
		
	public void turnKompaiLeft()
	{
		double angularSpeed = 0;
		double linearSpeed = 0;
		
		this.stopForReading();
		
		angularSpeed=angularSpeed+
					0.4;	
		controllerObject.setSpeeds
				(angularSpeed, linearSpeed);
	        
		this.stopForReading();
	}
	
	public void turnKompaiRight()
	{
		double angularSpeed = 0;
		double linearSpeed = 0;
		
		this.stopForReading();
	
		angularSpeed=angularSpeed-
					0.4;	
		controllerObject.setSpeeds
				(angularSpeed, linearSpeed);
	   
		this.stopForReading();
	}
	
	public void moveKompaiForward()
	{
		
		double angularSpeed = 0;
		double linearSpeed = 0;
		
		linearSpeed=linearSpeed+
					0.4;
		
		controllerObject.setSpeeds
			(angularSpeed, linearSpeed);
	}
	
	public void moveKompaiBackward()
	{
		double angularSpeed = 0;
		double linearSpeed = 0;
		
		linearSpeed=linearSpeed-
				0.4;
		
		controllerObject.setSpeeds
				(angularSpeed, linearSpeed);
	}
	
	
	
	public void stopForReading()
	{
	
		controllerObject.setSpeeds(0.0,0.0);
		
		PositionLocalizer positionObject=new PositionLocalizer();
		
		double currentRobotXPos=positionObject.getCurrentXPosOfRobot();
		double currentRobotYPos=positionObject.getCurrentYPosOfRobot();
		
		System.out.println("Stop currentRobotXPos=" + currentRobotXPos);
		System.out.println("Stop currentRobotYPos=" + currentRobotYPos);
	}
	
	
	public void stopAll()
	{

		FillOccupancyGrid fobj=new FillOccupancyGrid();
		fobj.displayGRIDInverse();
		
		controllerObject.setSpeeds(0.0,0.0);
		
		DeliberativeBehavior.createDisplayOccupancyGrid();
		
		ImageProcessing ioj=new ImageProcessing();
		ioj.createGIFImage(DeliberativeBehavior.displayOccupancyGRID);
		
		System.exit(0);
		
	}
	
	public void stopKompaiRobot()
	{
		
		FillOccupancyGrid fobj=new FillOccupancyGrid();
		fobj.displayGRIDInverse();
		
		controllerObject.setSpeeds(0.0,0.0);
		
		System.exit(0);
		
		
	}
	
}
