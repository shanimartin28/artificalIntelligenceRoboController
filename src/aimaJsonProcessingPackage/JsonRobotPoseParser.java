/*
 * @author mcs10smn
 * Department of computing science
 */

package aimaJsonProcessingPackage;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

// TODO: Auto-generated Javadoc
/**
 * The Class JsonParsing.
 */
public class JsonRobotPoseParser 
{
	
	JSONParser parser = new JSONParser();
	
	public JsonRobotPoseParser()
	{
	}

	
	/*  Localization-Position
		Provides the current position and orientation in JSON format. 
		The position is specified in a Euclidean coordinate system where 
		Z is the vertical. By experiments it could be verified that in our 
		system the Z value remains the same - "Z":0.07760079205036163.
		Only the x and y values alter.
	*/
	
	@SuppressWarnings("rawtypes")
	public String getPose(String json_string)
	{
		
		////System.out.println("1. In getPose");
		Map localizationJsonMap= new HashMap();
		JSONObject pose_json_object=null;
		
		try
		{
			////System.out.println("inside try block");
		    localizationJsonMap = (Map)parser.parse(json_string);
		    Iterator iter = localizationJsonMap.entrySet().iterator();
		    ////System.out.println("==iterate result(getPose)==");
		    while(iter.hasNext())
		    {
		      Map.Entry entry = (Map.Entry)iter.next();
		      String key = (String) entry.getKey();
		      ////System.out.println(key + "=>" + entry.getValue());
		      
		      if(key.equalsIgnoreCase("Pose"))
		      {
		    	  pose_json_object = (JSONObject) entry.getValue();
		      }               
		    
		    }
		  }
		  catch(ParseException pe)
		  {
		    ////System.out.println(pe);
		  }
		  
		  return this.parsePose(pose_json_object);
	}
	
	@SuppressWarnings("rawtypes")
	public String parsePose(JSONObject pose_json_object)
	{
		Map poseJsonMap= new HashMap();
		JSONObject position_json_object=null;
		
		try
		{
		    poseJsonMap = (Map)parser.parse(pose_json_object.toJSONString());
		    Iterator iter = poseJsonMap.entrySet().iterator();
		    //////System.out.println("==iterate result==");
		    while(iter.hasNext())
		    {
		      Map.Entry entry = (Map.Entry)iter.next();
		      String key = (String) entry.getKey();
		      ////System.out.println(key + "=>" + entry.getValue());
		      
		      if(key.equalsIgnoreCase("Position"))
		      {
		    	  position_json_object = (JSONObject) entry.getValue();
		      }               
		   /* ////System.out.println("==toJSONString()==");
		    ////System.out.println(JSONValue.toJSONString(poseJsonMap));*/
		    }
		  }
		  catch(ParseException pe)
		  {
		    ////System.out.println(pe);
		  }
		  //////System.out.println("Position");
		  //////System.out.println(position_json_object.toJSONString());
		  
		return position_json_object.toJSONString();
		  
	}
	
	@SuppressWarnings("rawtypes")
	public String getCurrentXYPosition(String json_string, String json_compare)
	{
		Map positionXYJsonMap= new HashMap();
		Double positionXY=null;
		
		// Fix for the class cast exception
		Long l = null;
	    double d = 0.0;
		
		try
		{
		    positionXYJsonMap = (Map)parser.parse(json_string);
		    Iterator iter = positionXYJsonMap.entrySet().iterator();
		    //////System.out.println("==iterate result==");
		    while(iter.hasNext())
		    {
		      Map.Entry entry = (Map.Entry)iter.next();
		      String key = (String) entry.getKey();
		      //////System.out.println(key + "=>" + entry.getValue());
		      
		      if(key.equalsIgnoreCase(json_compare))
		      {
		    	  //positionXY = new Double((Double) entry.getValue());
		    	  //l = new Long((Long) entry.getValue());
		    	  //d=l.doubleValue();
		    	  if(entry.getValue() instanceof Double)
		    	  {
		    		  //System.out.println("You are a double");
		    		  positionXY = new Double((Double) entry.getValue());
		    	  }
		    	  else if(entry.getValue() instanceof Long)
		    	  {
		    		  //System.out.println("You are a Long");
		    		  l = new Long((Long) entry.getValue());
			    	  d=l.doubleValue();
		    		  positionXY = new Double(d);
		    	  }
//		    	  ////System.out.println("==toJSONString()==");
//			      ////System.out.println(positionXY);
		      }               
		    }
		  }
		  catch(ParseException pe)
		  {
		    ////System.out.println(pe);
		  }
		  //////System.out.println("Position");
		  //////System.out.println(position_json_object.toJSONString());
		  
		  return positionXY.toString();
		  //return new Double(d).toString();
	}
	
}
