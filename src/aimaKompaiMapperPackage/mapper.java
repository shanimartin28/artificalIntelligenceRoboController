/*
 * @author mcs10smn
 * Department Of Computing Science
 */

package aimaKompaiMapperPackage;


import help.KompaiRobotInteractor_HELP;


import aimaLocalizationPackage.PositionLocalizer;
import aimaMapImageDisplay.KompaiMapDisplay;
import aimaOccupancyGridProcessingPackage.OccupancyGridAlgm;


// TODO: Auto-generated Javadoc
/**
 * The Class ExecuteKompaiMapper.
 * The main class used for executing the mapper application
 */
public class mapper 
{
	// To track if robot has started to move
	/** The is robot moving. */
	static boolean isRobotMoving=false;
	
	// parameters to be passed while executing the application
	/** The url of mrds. */
	// public static String urlOfMRDS="http://localhost:50000"; 
	
	
	// note: usually the x coordinate value denotes the height parameter
	// and the y coordinate value denotes the width parameter
	// And the y values decrease as we move towards the right
	// For only the first room in which the robot is there
	
	// Test1 - Square1 where the robot is in the initial position
	/** The x1_lower_left_coordinate. */
//	public static double x1_lower_left_coordinate= -16.80762481689453; 
//	/** The y1_lower_left_coordinate. */
//	public static double y1_lower_left_coordinate= 12.179659843444824; 
//	/** The x2_top_right_coordinate. */
//	public static double x2_top_right_coordinate= 6.89419412612915; 
//	/** The y2_top_right_coordinate. */
//	public static double y2_top_right_coordinate= -11.694177627563477;
	
	// Test2 - number of squares 2x2, with robot in the same init position
//	/** The x1_lower_left_coordinate. */
//	public static double x1_lower_left_coordinate= -16.80; 
////	/** The y1_lower_left_coordinate. */
//	public static double y1_lower_left_coordinate= 35.05; 
////	/** The x2_top_right_coordinate. */
//	public static double x2_top_right_coordinate= 30.71; 
////	/** The y2_top_right_coordinate. */
//	public static double y2_top_right_coordinate= -11.34; 
////	
	
	// Test3 - number of squares 3x3, with robot in the same init position,
////	/** The x1_lower_left_coordinate. */
//	public static double x1_lower_left_coordinate= -16.69; 
////	/** The y1_lower_left_coordinate. */
//	public static double y1_lower_left_coordinate= 59.13; 
////	/** The x2_top_right_coordinate. */
//	public static double x2_top_right_coordinate= 53.14; 
////	/** The y2_top_right_coordinate. */
//	public static double y2_top_right_coordinate= -11.62; 
//	
	
	// Test4 - number of squares 5x5, with robot in the same init position,
	// whole factory environment- navigates full
////	/** The x1_lower_left_coordinate. */
//	public static double x1_lower_left_coordinate= -63.90; 
////	/** The y1_lower_left_coordinate. */
//	public static double y1_lower_left_coordinate= 59.53; 
////	/** The x2_top_right_coordinate. */
//	public static double x2_top_right_coordinate= 54.27; 
////	/** The y2_top_right_coordinate. */
//	public static double y2_top_right_coordinate= -58.59; 
//	
	
//	/** The no of cols_image. */
//	public static int noOfCols_image = 300; 
//	/** The no ofrows_image. */
//	public static int noOfrows_image = 300; 
	
	// determine the square area to be explored width and height
	/** The square_area_width. */
	public static double square_area_width= 0.0; 
	
	/** The square_area_heigth. */
	public static double square_area_heigth= 0.0;
	
	/** The element_ square side_width_distance. */
	public static double element_SquareSide_width_distance= 0.0;
	
	/** The element_ square side_height_distance. */
	public static double element_SquareSide_height_distance= 0.0;
	
	public static double hIMM_15GridElementsDistance= 0.0;
	
	// Get the localization information
	/** The localizer object. */
	static PositionLocalizer localizerObject=new PositionLocalizer();
	
	/** The cur robo y pos. */
	static double curRoboXPos=0.0,curRoboYPos=0.0;
	
	// For the occupancy grid calculations
	/** The occupancy object. */
	static OccupancyGridAlgm occupancyObject=null;
	
	public static String urlOfMRDS=null;
	public static double x1_lower_left_coordinate= 0.0; 
	public static double y1_lower_left_coordinate= 0.0; 
	public static double x2_top_right_coordinate= 0.0; 
	public static double y2_top_right_coordinate= 0.0; 
	public static int noOfCols_image = 0; 
	public static int noOfrows_image = 0; 
	
	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) 
	{
		// TODO Auto-generated method stub
		
		
		if (args.length > 0) 
		{
		    try 
		    {
		    	urlOfMRDS=args[0].toString();
		    	System.out.println("urlOfMRDS = " +urlOfMRDS);
		    	x1_lower_left_coordinate = Double.parseDouble(args[1]);
		    	y1_lower_left_coordinate = Double.parseDouble(args[2]);
		    	x2_top_right_coordinate = Double.parseDouble(args[3]);
		    	y2_top_right_coordinate = Double.parseDouble(args[4]);
		    	noOfCols_image=	Integer.parseInt(args[5]);
		    	noOfrows_image=	Integer.parseInt(args[6]);
		    } 
		    catch (Exception e) {
		        System.err.println("Enter Arguments in the following format :");
		        System.out.println("url x1 y1 x2 y2 cols rows");
		        System.exit(1);
		    }
		}
		else
		{
			System.err.println("Enter Arguments in the following format :");
	        System.out.println("url x1 y1 x2 y2 cols rows");
	        System.exit(1);
		}
		
		
		try
		{
			KompaiMapDisplay dispObj=new KompaiMapDisplay();
			dispObj.displayKompaiPanel();
		}
		catch (Exception e) {
			// TODO: handle exception
			System.out.println(" KompaiMapDisplay exception!! = " +e);
			System.exit(0);
		}
		
		
		System.out.println("In Execute Mapper");
		System.out.println("");
		
		// Determine the square area that the robot should parse
		calculateSquareArea();
		
		// Create a 2Dimensional grid to represent the occupancy grid
		// Based on the number of columns and rows
		occupancyObject=new OccupancyGridAlgm(noOfrows_image,noOfCols_image,
				element_SquareSide_width_distance , element_SquareSide_height_distance, 
				x1_lower_left_coordinate, y1_lower_left_coordinate,
				x2_top_right_coordinate, y2_top_right_coordinate);
		
		occupancyObject.createOccupancyGrid();
	}
	
	
	/**
	 * Calculate square area.
	 * This method determines the square area the robot should parse
	 */
	public static void calculateSquareArea()
	{
		square_area_heigth=x2_top_right_coordinate-x1_lower_left_coordinate;
		square_area_width=y1_lower_left_coordinate-y2_top_right_coordinate;

		System.out.println("Method:calculateSquareArea");
		System.out.println("1.x1_lower_left_coordinate = " +x1_lower_left_coordinate);
		System.out.println("2.y1_lower_left_coordinate = " +y1_lower_left_coordinate);
		System.out.println("3.x2_top_right_coordinate = " +x2_top_right_coordinate);
		System.out.println("4.y2_top_right_coordinate = " +y2_top_right_coordinate);
		
		System.out.println("5.noOfCols_image = " + noOfCols_image + 
				" noOfrows_image = " + noOfrows_image);
		
		System.out.println("6.square_area_width = " + square_area_width + 
				" square_area_heigth = " + square_area_heigth);
		
		element_SquareSide_width_distance=square_area_width/noOfCols_image;
		element_SquareSide_height_distance=square_area_heigth/noOfrows_image;
		
		System.out.println("7. element_SquareSide_width_distance = " + element_SquareSide_width_distance + 
				" element_SquareSide_height_distance = " + element_SquareSide_height_distance);
		
		hIMM_15GridElementsDistance=square_area_width/3;
		
		System.out.println("hIMM_15GridElementsDistance= " +hIMM_15GridElementsDistance);
		//System.exit(0);
		
		System.out.println("/////////////////////////////////////////////////");
		
		System.out.println("");
		
	}
	
	

}
