package aimaJsonProcessingPackage;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.*;

public class JsonRobotCalculateHeadingAngle 
{
	
	JSONParser parser = new JSONParser();
	double X=0.0,Y=0.0,Z=0.0,W=0.0;
	
	public JsonRobotCalculateHeadingAngle()
	{
		
	}
	
	public String getPose(String json_string)
	{
		
		Map localizationJsonMap= new HashMap();
		JSONObject pose_json_object=null;
		
		try
		{
		    localizationJsonMap = (Map)parser.parse(json_string);
		    Iterator iter = localizationJsonMap.entrySet().iterator();
		    //////System.out.println("==iterate result==");
		    while(iter.hasNext())
		    {
		      Map.Entry entry = (Map.Entry)iter.next();
		      String key = (String) entry.getKey();
		      //////System.out.println(key + "=>" + entry.getValue());
		      
		      if(key.equalsIgnoreCase("Pose"))
		      {
		    	  pose_json_object = (JSONObject) entry.getValue();
		      }               
		      //////System.out.println("==toJSONString()==");
		      //////System.out.println(JSONValue.toJSONString(localizationJsonMap));
		    }
		  }
		  catch(ParseException pe)
		  {
		    ////System.out.println(pe);
		  }
		  
		  return pose_json_object.toString();
	}

	public double calculateHeadingAngle(String localizedCurrentPose)
	{
		double headingAngle=0.0;
		//////System.out.println("calculateHeadingAngle");

		Map localizationJsonMap= new HashMap();
		JSONObject orientation_json_object=null;
		
		try
		{
		    localizationJsonMap = (Map)parser.parse(localizedCurrentPose);
		    Iterator iter = localizationJsonMap.entrySet().iterator();
		    //////System.out.println("==iterate result==");
		    while(iter.hasNext())
		    {
		      Map.Entry entry = (Map.Entry)iter.next();
		      String key = (String) entry.getKey();
		      //////System.out.println(key + "=>" + entry.getValue());
		      
		      if(key.equalsIgnoreCase("Orientation"))
		      {
		    	  //////System.out.println("hhhh");
		    	  orientation_json_object = (JSONObject) entry.getValue();
		    	  headingAngle=this.getXYZWValues(orientation_json_object);
		      }               
		      
		      
		    }
		  }
		  catch(ParseException pe)
		  {
		    ////System.out.println(pe);
		  }
		return headingAngle;
		  
		 
	}
	
	public double getXYZWValues(JSONObject orientation_json_object)
	{
		double headingAngle=0.0;
		//////System.out.println("getXYZWValues");
		Map orientationJsonMap= new HashMap();
		JSONObject position_json_object=null;
		try
		{
		    orientationJsonMap = (Map)parser.parse(orientation_json_object.toJSONString());
		    Iterator iter = orientationJsonMap.entrySet().iterator();
		    //////System.out.println("==iterate result==");
		    while(iter.hasNext())
		    {
		      Map.Entry entry = (Map.Entry)iter.next();
		      String key = (String) entry.getKey();
		      //////System.out.println(key + "=>" + entry.getValue());
		      
		      if(key.equalsIgnoreCase("X"))
		      {
		    	  X = (Double) entry.getValue();
		      }  
		      else if(key.equalsIgnoreCase("Y"))
		      {
		    	  Y = (Double) entry.getValue();
		      }
		      else if(key.equalsIgnoreCase("Z"))
		      {
		    	  Z = (Double) entry.getValue();
		      }
		      else if(key.equalsIgnoreCase("W"))
		      {
		    	  W = (Double) entry.getValue();
		      }
		   
		    }
		    
		    headingAngle= this.heading(X,Y,Z,W);
		    //////System.out.println("headingAngle = " +headingAngle);
		    
		  }
		  catch(ParseException pe)
		  {
		    ////System.out.println(pe);
		  }
		return headingAngle;
	}
	
	// Converts a orientation dict to a heading angle (0 <= a <= 2pi)
	public double heading(double X,double Y,double Z,double W)
	{
		double att = this.attitude(X,Y,Z,W);
	    
	    if(W*W< 0.5)
	    {
	    	return Math.PI - att;
	    }
	    else if(att < 0)
	    {
	    	return 2*Math.PI+att;
	    }
	    else
	    {
	    	 return att;
	    }
	    
	}
	// Returns the attitude part of the Quaternion Orientation
	public double attitude(double X,double Y,double Z,double W)
	{
	    double v = X*Y+Z*W;
	    if(v > 0.499)
	    {
	    	//North pole
	        return Math.PI/-2;
	    }
	    else if(v < -0.499)
	    {
	    	//South pole
	        return Math.PI/2;
	    }
	    else
	    {
	    	return Math.asin(2*X*Y - 2*Z*W);
	    }
	    
	}
	
}
