/*
 * @author mcs10smn
 * Department Of Computing Science
 */

package aimaLocalizationPackage;

import java.util.ArrayList;
import org.json.simple.JSONArray;

import aimaJsonProcessingPackage.JsonRobotCalculateHeadingAngle;
import aimaJsonProcessingPackage.JsonRobotLaserParser;
import aimaJsonProcessingPackage.JsonRobotPoseParser;
import aimaKompaiMapperPackage.KompaiRobotController;


// TODO: Auto-generated Javadoc
/**
 * The Class PositionLocalizer.
 * This class is used for getting the current position of the robot
 */
public class PositionLocalizer 
{
	/** The json pose parser. */
	JsonRobotPoseParser jsonPoseParser=new JsonRobotPoseParser();
	
	/** The json laser parser. */
	JsonRobotLaserParser jsonLaserParser=new JsonRobotLaserParser();
	
	/** The heading calc obj. */
	JsonRobotCalculateHeadingAngle headingCalcObj=new JsonRobotCalculateHeadingAngle();
	
	/** The localized current pose. */
	String localizedCurrentPose= null;
	
	/** The localized current angles. */
	String localizedCurrentAngles= null;
	
	/** The localized current position x. */
	String localizedCurrentPositionX= null;
	
	/** The localized current position y. */
	String localizedCurrentPositionY= null;
	
	/** The distance of zero angle. */
	double distanceOfZeroAngle=0.0;
	
	
	/** The stop y pos. */
	double stopXPos=0.0,stopYPos=0.0;
	
	/** The controller object. */
	KompaiRobotController controllerObject=new KompaiRobotController();
	
	/**
	 * Instantiates a new position localizer.
	 */
	public PositionLocalizer()
	{
	}
	
	/**
	 * Current postion of kompai.
	 *
	 * @param json_string the json_string
	 * @return the string
	 */
	public String currentPostionOfKompai(String json_string)
	{
		////System.out.println("curPosiofkompai=" +json_string);
		return localizedCurrentPose= jsonPoseParser.getPose(json_string);
	}
	
	/**
	 * Gets the current x pos of robot.
	 *
	 * @return the current x pos of robot
	 */
	public double getCurrentXPosOfRobot()
	{
		
		String localizationdata = controllerObject.getLocalizationData();
		////System.out.println("lll= " + localizationdata);
		localizedCurrentPose=this.currentPostionOfKompai(localizationdata);
		localizedCurrentPositionX= jsonPoseParser.
					getCurrentXYPosition(localizedCurrentPose,"X");
		return new Double(localizedCurrentPositionX).doubleValue();
		
	}
	
	/**
	 * Gets the current y pos of robot.
	 *
	 * @return the current y pos of robot
	 */
	public double getCurrentYPosOfRobot()
	{
		
		String localizationdata = controllerObject.getLocalizationData();
		localizedCurrentPose=this.currentPostionOfKompai(localizationdata);
		localizedCurrentPositionY= jsonPoseParser.
					getCurrentXYPosition(localizedCurrentPose,"Y");
		return new Double(localizedCurrentPositionY).doubleValue();
	}
	
	/**
	 * Gets the laser data at acoustic axis.
	 *
	 * @return the laser data at acoustic axis
	 */
	public double getLaserDataAtAcousticAxis()
	{
		String laserdata = controllerObject.getLaserData();
		////System.out.println("laserdata- "+laserdata);
		distanceOfZeroAngle=this.getDistanceAtAcousticAxis(laserdata);
		//this.getCurrentZeroAnglePosition(localizedCurrentAngles);
		return distanceOfZeroAngle;
		
	}
	
	/**
	 * Gets the distance at acoustic axis.
	 *
	 * @param json_string the json_string
	 * @return the distance at acoustic axis
	 */
	public double getDistanceAtAcousticAxis(String json_string)
	{
		return distanceOfZeroAngle= jsonLaserParser.getAcousticDistanceFromRobot(json_string);
	}
	
	/**
	 * Gets the heading angle.
	 *
	 * @return the heading angle
	 */
	public double getHeadingAngle()
	{
		String localizationdata = controllerObject.getLocalizationData();
		////System.out.println("localizationdata- "+localizationdata);
		localizedCurrentPose=headingCalcObj.getPose(localizationdata);
		double headingAngle=headingCalcObj.calculateHeadingAngle(localizedCurrentPose);
		return headingAngle;
		
	}
	
	/**
	 * Gets the laser distance array.
	 *
	 * @return the laser distance array
	 */
	public JSONArray getLaserDistanceArray()
	{
		String laserdata = controllerObject.getLaserData();
		return jsonLaserParser.getLaserDistances(laserdata);
	}
	
	/**
	 * Gets the right angle laser distance array.
	 *
	 * @return the right angle laser distance array
	 */
	public JSONArray getRightAngleLaserDistanceArray()
	{
		String laserdata = controllerObject.getLaserData();
		return jsonLaserParser.getRightAngleLaserDistanceArray(laserdata);
	}
	
	/**
	 * Gets the hIMM laser array.
	 *
	 * @return the hIMM laser array
	 */
	public JSONArray getHIMMLaserArray()
	{
		String laserdata = controllerObject.getLaserData();
		//System.out.println("laserdata = " +laserdata);
		return jsonLaserParser.getHIMMLaserDistanceArray(laserdata);
	}
	
}
