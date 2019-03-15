package aimaJsonProcessingPackage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import aimaLocalizationPackage.ElementLocalizer;
import aimaResources.AimaConstants;


public class JsonRobotLaserParser 
{
	
	JSONParser parser = new JSONParser();
	
	public JsonRobotLaserParser()
	{
		
	}

	public double getAcousticDistanceFromRobot(String json_string)
	{
		////System.out.println("json_string = " +json_string);

		Map laserJsonMap= new HashMap();
		JSONArray angle_json_object=null;
		double distanceAtAngleZero=0.0;
		try
		{
		    laserJsonMap = (Map)parser.parse(json_string);
		    Iterator iter = laserJsonMap.entrySet().iterator();
		    ////System.out.println("==laserJsonMap== " +laserJsonMap);
		    JSONArray angles_array = null;
		    JSONArray distances_array=null;
		    while(iter.hasNext())
		    {
		      Map.Entry entry = (Map.Entry)iter.next();
		      String key = (String) entry.getKey();
		      //System.out.println(key + "=>" + entry.getValue());
		      
		      if(key.equalsIgnoreCase("Angles"))
		      {
		    	  angles_array = (JSONArray) laserJsonMap.get("Angles");
		    	  //System.out.println("angles_array" + "=>" + angles_array);
		      }   
		      else if(key.equalsIgnoreCase("Distances"))
		      {
		    	  distances_array = (JSONArray) laserJsonMap.get("Distances");
		    	  //System.out.println("distances_array" + "=>" + distances_array);
		      }
		    }
		    // Once the angles array is got,
		    // Get the index position where the angle is 0
		    int indexPosition=0;
		    for(int i=0;i<angles_array.size();i++)
		    {
		    	Object angleVal=angles_array.get(i);
		    	if(angleVal.toString().equalsIgnoreCase("0"))
		    	{
		    		indexPosition=i;
		    		////System.out.println("indexPosition="+indexPosition);
		    	}
		    }
		    
		    distanceAtAngleZero=(Double) distances_array.get(indexPosition);
		    ////System.out.println("distanceAtAngleZero= "+distanceAtAngleZero);
		    
		  }
		  catch(ParseException pe)
		  {
		    //System.out.println(pe);
		  }
		  
		  return distanceAtAngleZero;
	}
	
	
	public JSONArray getLaserDistances(String json_string)
	{
		double laserDistance=0.0;
		Map laserJsonMap= new HashMap();
		JSONArray angle_json_object=null;
		JSONArray distances_array=null;
		double distanceAtAngleZero=0.0;
		JSONArray angles_array = null;
		try
		{
		    laserJsonMap = (Map)parser.parse(json_string);
		    Iterator iter = laserJsonMap.entrySet().iterator();
		    
		    while(iter.hasNext())
		    {
		      Map.Entry entry = (Map.Entry)iter.next();
		      String key = (String) entry.getKey();
		      ////System.out.println(key + "=>" + entry.getValue());
		      if(key.equalsIgnoreCase("Distances"))
		      {
		    	  distances_array = (JSONArray) laserJsonMap.get("Distances");
		    	  ////System.out.println("distances_array" + "=>" + distances_array);
		      }
		    }
		    
		    ////System.out.println("distances_array.size()"+distances_array.size());
		    
		  }
		  catch(ParseException pe)
		  {
		    //System.out.println(pe);
		  }
		  
		  return distances_array;
	}
	
	
	public JSONArray getRightAngleLaserDistanceArray(String json_string)
	{
		
		////System.out.println("getRightAngleLaserDistanceArray");
		
		Map laserJsonMap= new HashMap();
		JSONArray distances_array=null;
		JSONArray rightAngle_distances_array=new JSONArray();
		JSONArray angles_array = null;
		try
		{
		    laserJsonMap = (Map)parser.parse(json_string);
		    Iterator iter = laserJsonMap.entrySet().iterator();
		    ////System.out.println("==laserJsonMap== " +laserJsonMap);
		    
		    while(iter.hasNext())
		    {
		      Map.Entry entry = (Map.Entry)iter.next();
		      String key = (String) entry.getKey();
		      ////System.out.println(key + "=>" + entry.getValue());
		      if(key.equalsIgnoreCase("Angles"))
		      {
		    	  angles_array = (JSONArray) laserJsonMap.get("Angles");
//		    	  //System.out.println("angles_array.size-"+angles_array.size());
//		    	  //System.out.println("angles_array" + "=>" + angles_array);
		      }  
		      if(key.equalsIgnoreCase("Distances"))
		      {
		    	  distances_array = (JSONArray) laserJsonMap.get("Distances");
//		    	  //System.out.println("distances_array.size-"+distances_array.size());
//		    	  //System.out.println("distances_array" + "=>" + distances_array);
		      }
		    }
		    
//		    for(int i=0;i<distances_array.size();i++)
//		    {
//		    	
//		    	Object distVal=distances_array.get(i);
//		    	double distance=new Double(distVal.toString()).doubleValue();
//		    	
//		    	if(distance>=0.0
//		    			 && distance <= 0.5)
//		    	{
//		    		//System.out.println("you r in!!-" + distance);
//		    		int indexPos=i;
//		    		Object angle_val=angles_array.get(indexPos);
//		    		//System.out.println("angle_val = " +angle_val.toString());
//		    	}
//		    }
		    
		    
		    for(int i=0;i<angles_array.size();i++)
		    {
		    	
		    	Object angleVal=angles_array.get(i);
		    	double angle=new Double(angleVal.toString()).doubleValue();
		    	
		    	if(angle>=AimaConstants.right_angle_range_min
		    			 && angle <= AimaConstants.right_angle_range_max)
		    	{
		    		////System.out.println("your angle =" + angle);
		    		int indexPos=i;
		    		Object distance_val=distances_array.get(indexPos);
		    		//System.out.println("your distance from wall =" + distance_val.toString());
		    		rightAngle_distances_array.add(distance_val);
		    	}
		    }
		    
		  }
		  catch(ParseException pe)
		  {
		    //System.out.println(pe);
		  }
		  
		  return rightAngle_distances_array;
	
	}
	
	public JSONArray getHIMMLaserDistanceArray(String json_string)
	{
		
		Map laserJsonMap= new HashMap();
		JSONArray distances_array=null;
		JSONArray HIMM_element_array=new JSONArray();
		JSONArray angles_array = null;
		ElementLocalizer elemLocObject=null;
		try
		{
		    laserJsonMap = (Map)parser.parse(json_string);
		    Iterator iter = laserJsonMap.entrySet().iterator();
		   
		    while(iter.hasNext())
		    {
		      Map.Entry entry = (Map.Entry)iter.next();
		      String key = (String) entry.getKey();
		      ////System.out.println(key + "=>" + entry.getValue());
		      if(key.equalsIgnoreCase("Angles"))
		      {
		    	  angles_array = (JSONArray) laserJsonMap.get("Angles");
		    	  ////System.out.println("angles_array" + "=>" + angles_array);
		      }  
		      if(key.equalsIgnoreCase("Distances"))
		      {
		    	  distances_array = (JSONArray) laserJsonMap.get("Distances");
		    	  ////System.out.println("distances_array" + "=>" + distances_array);
		      }
		    }
		    
		    // Taking 40 neighbours at a time
//		    for(int i=0;i<distances_array.size();i++)
//		    {
//		    	if(i>=AimaConstants.HIMM_distance_array_start_index
//		    			 && i <= AimaConstants.HIMM_distance_array_end_index)
//		    	{
//		    		int indexPos=i;
//		    		Object distance_val=distances_array.get(indexPos);
//		    		Object angle_val=angles_array.get(indexPos);
//		    		elemLocObject=new ElementLocalizer();
//		    		elemLocObject.setDistance(new Double(distance_val.toString()).doubleValue());
//		    		elemLocObject.setAngle(new Double(angle_val.toString()).doubleValue());
//		    		//System.out.println("indexPos =" + indexPos);
//		    		//System.out.println("your distance from wall =" + distance_val.toString());
//		    		//System.out.println("your angle from wall =" + angle_val.toString());
//		    		HIMM_element_array.add(elemLocObject);
//		    	}
//		    }
		    
		    // Take the 0 angle(acoustic axis), right side, and left side values, at
		    // a particular instant of time
		    // Once the angles array is got,
		    // Get the index position where the angle is 0
		    
		    double distanceAtRightAngle=0.0,distanceAtAngleZero=0.0,distanceAtLeftAngle=0.0;
		    
		    int indexPosition=0;
		    for(int i=0;i<angles_array.size();i++)
		    {
		    	Object angleVal=angles_array.get(i);
		    	if(angleVal.toString().equalsIgnoreCase("0"))
		    	{
		    		indexPosition=i;
		    		// Fix for the class cast exception
		    		Long l = null;
		    	    double d = 0.0;
		    		  
		    		  Object distanceObj=distances_array.get(indexPosition);
		    		  if(distanceObj instanceof Double)
			    	  {
			    		  //System.out.println("You are a double");
			    		  distanceAtAngleZero = new Double((Double) distanceObj);
			    		  //System.out.println("distanceAtAngleZero = " + distanceAtAngleZero);
			    		  //System.exit(0);
			    	  }
		    		  else if(distanceObj instanceof Long)
			    	  {
			    		  //System.out.println("You are a Long");
			    		  l = new Long((Long) distanceObj);
				    	  d=l.doubleValue();
				    	  distanceAtAngleZero = new Double((Double) d);
			    		  //System.out.println("distanceAtAngleZero = " + distanceAtAngleZero);
			    	  }
		    		
		    		//distanceAtAngleZero=(Double) distances_array.get(indexPosition);
		    		elemLocObject=new ElementLocalizer();
		    		elemLocObject.setDistance(distanceAtAngleZero);
		    		elemLocObject.setAngle(new Double(angleVal.toString()).doubleValue());
		    		elemLocObject.setAngleObjectSymbol(AimaConstants.P_ZERO_ANGLE_SYMBOL);
		    		HIMM_element_array.add(elemLocObject);
		    	}
		    }
		    
		    
		    for(int i=0;i<angles_array.size();i++)
		    {
		    	
		    	Object angleVal=angles_array.get(i);
		    	double angle=new Double(angleVal.toString()).doubleValue();
		    	
		    	if(angle>=AimaConstants.right_angle_range_min
		    			 && angle <= AimaConstants.right_angle_range_max)
		    	{
		    		////System.out.println("your angle =" + angle);
		    		indexPosition=i;
		    		// Fix for the class cast exception
		    		Long l = null;
		    	    double d = 0.0;
		    		//Object distance_val=distances_array.get(indexPosition);
		    		//distanceAtRightAngle=(Double) distances_array.get(indexPosition);
		    		
		    		  Object distanceObj=distances_array.get(indexPosition);
		    		  if(distanceObj instanceof Double)
			    	  {
			    		  //System.out.println("You are a double");
			    		  distanceAtRightAngle = new Double((Double) distanceObj);
			    		  //System.out.println("distanceAtRightAngle = " + distanceAtRightAngle);
			    		  //System.exit(0);
			    	  }
		    		  else if(distanceObj instanceof Long)
			    	  {
			    		  //System.out.println("You are a Long");
			    		  l = new Long((Long) distanceObj);
				    	  d=l.doubleValue();
				    	  distanceAtRightAngle = new Double((Double) d);
			    		  //System.out.println("distanceAtRightAngle = " + distanceAtRightAngle);
			    	  }
		    		
		    		elemLocObject=new ElementLocalizer();
		    		elemLocObject.setDistance(distanceAtRightAngle);
		    		elemLocObject.setAngle(new Double(angleVal.toString()).doubleValue());
		    		elemLocObject.setAngleObjectSymbol(AimaConstants.P_RIGHT_ANGLE_SYMBOL);
		    		HIMM_element_array.add(elemLocObject);
		    	}
		    }
		    for(int i=0;i<angles_array.size();i++)
		    {
		    	
		    	Object angleVal=angles_array.get(i);
		    	double angle=new Double(angleVal.toString()).doubleValue();
		    	
		    	if(angle>=AimaConstants.left_angle_range_min
		    			 && angle <= AimaConstants.left_angle_range_max)
		    	{
		    		////System.out.println("your angle =" + angle);
		    		indexPosition=i;
		    		// Fix for the class cast exception
		    		Long l = null;
		    	    double d = 0.0;
		    		//Object distance_val=distances_array.get(indexPosition);
		    		//distanceAtLeftAngle=(Double) distances_array.get(indexPosition);
		    		
		    		  Object distanceObj=distances_array.get(indexPosition);
		    		  if(distanceObj instanceof Double)
			    	  {
			    		  //System.out.println("You are a double");
			    		  distanceAtLeftAngle = new Double((Double) distanceObj);
			    		  //System.out.println("distanceAtLeftAngle = " + distanceAtLeftAngle);
			    		  //System.exit(0);
			    	  }
		    		  else if(distanceObj instanceof Long)
			    	  {
			    		  //System.out.println("You are a Long");
			    		  l = new Long((Long) distanceObj);
				    	  d=l.doubleValue();
				    	  distanceAtLeftAngle = new Double((Double) d);
			    		  //System.out.println("distanceAtLeftAngle = " + distanceAtLeftAngle);
			    	  }
		    		
		    		elemLocObject=new ElementLocalizer();
		    		elemLocObject.setDistance(distanceAtLeftAngle);
		    		elemLocObject.setAngle(new Double(angleVal.toString()).doubleValue());
		    		elemLocObject.setAngleObjectSymbol(AimaConstants.P_LEFT_ANGLE_SYMBOL);
		    		HIMM_element_array.add(elemLocObject);
		    	}
		    }
		    
//		    //System.out.println("distanceAtAngleZero = " +distanceAtAngleZero);
//		    //System.out.println("distanceAtRightAngle = " +distanceAtRightAngle);
//		    //System.out.println("distanceAtLeftAngle = " +distanceAtLeftAngle);
		    //System.exit(0);
		    
		  }
		  catch(ParseException pe)
		  {
		    //System.out.println(pe);
		  }
		  
		  return HIMM_element_array;
	
	}
	
	
}
