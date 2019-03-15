/*
 * @author mcs10smn
 * Department of computing science
 */
package aimaKompaiMapperPackage;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class KompaiRobotController 
{
			
	/** The base url. */
	public static String baseURL=mapper.urlOfMRDS;
	
	
	/* Example code illustrating how to get localization data 
	 * Reads the current position and orientation from the MRDS server 
	 * and parses it into a dict*/
	/**
	 * Gets the localization data.
	 *
	 * @return the localization data
	 */
	public String getLocalizationData()
	{
		URL url = null;
		//Localization result;
		String localizationdata = "";
		String localizer="/lokarria/localization";
		
		
		try
		{
			baseURL=mapper.urlOfMRDS;
			System.out.println("baseURL = " +baseURL);
			url = new URL(baseURL + localizer);
			
		}
		catch(MalformedURLException e)
		{
			System.err.println("LokarriaController.getLocalization: Malformed URL");
		}
		
		try{
			HttpURLConnection request = (HttpURLConnection) url.openConnection();
			request.setRequestMethod("GET");
			request.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
			request.setRequestProperty("Accept","text/json");
			
			InputStream response = request.getInputStream();
			request.getResponseCode();
			BufferedReader reader = new BufferedReader(new InputStreamReader(response));
			localizationdata = reader.readLine();
		}
		catch(Exception e){
			System.err.println("LokarriaController.getLocalization: Exception, " + e.toString());
			/* DO SOMETHING APPROPRIATE */
		}
		return localizationdata;
	}
	
	/*
	 * Requests the current laser scan from the MRDS server and parses it into a dict
	 */
	/**
	 * Gets the laser.
	 *
	 * @return the laser
	 */
	public String getLaserData()
	{
		URL url = null;
		//Localization result;
		String laserdata = "";
		String laser="/lokarria/customizedlaser";
		try
		{
			baseURL=mapper.urlOfMRDS;
			url = new URL(baseURL + laser);
			
		}
		catch(MalformedURLException e)
		{
			System.err.println("LokarriaController.getLaser: Malformed URL");
			/* DO SOMETHING APPROPRIATE */
		}
		
		try{
			HttpURLConnection request = (HttpURLConnection) url.openConnection();
			request.setRequestMethod("GET");
			request.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
			request.setRequestProperty("Accept","text/json");
			
			InputStream response = request.getInputStream();
			request.getResponseCode();
			BufferedReader reader = new BufferedReader(new InputStreamReader(response));
			laserdata = reader.readLine();
			
		}
		catch(Exception e){
			System.err.println("LokarriaController.getLaser: Exception, " + e.toString());
			/* DO SOMETHING APPROPRIATE */
		}
		return laserdata;
	
	}
	
	/* Example code illustrating how to set the speeds of the robot 
	 * Sends a speed command to the MRDS server */
	/**
	 * Sets the speeds.
	 *
	 * @param angSpeed the ang speed
	 * @param linSpeed the lin speed
	 * @return the int
	 */
	public int setSpeeds(double angSpeed, double linSpeed)
	{
		String query = "AngularSpeed=" + angSpeed + "&LinearSpeed=" + linSpeed;
		int status = -1;
		URL url;
		String driver="/lokarria/differentialdrive";
		
		try
		{
			//url = new URL(baseURL + driver);
			baseURL=mapper.urlOfMRDS;
			url = new URL(baseURL+driver);
		}
		catch(MalformedURLException e)
		{
			System.err.println("LokarriaController.setSpeeds: Malformed URL");
			return status;
		}
		try
		{
			HttpURLConnection request = (HttpURLConnection) url.openConnection();
			request.setRequestMethod("POST");
			request.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
			request.setRequestProperty("Accept","text/json");
			request.setDoOutput(true);
			
			OutputStream output = null;
			
			output = request.getOutputStream();
			output.write(query.getBytes());
			
			request.getInputStream();
			status = request.getResponseCode();
		}
		catch(Exception e)
		{
			System.err.println("LokarriaController.setSpeed: Exception, " + e.toString());
			return status;
		}
		return status;
	  }
	
	

}
