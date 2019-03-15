package aimaResources;


public class AimaConstants 
{
	
	// object symbol constants
	public static final String P_OCCUPIED_OBJECT_SYMBOL = "X";
	public static final String P_EMPTY_OBJECT_SYMBOL = "e";
	public static final String P_START_OBJECT_SYMBOL = "(START)";
	public static final String P_RIGHT_ANGLE_SYMBOL = "R";
	public static final String P_LEFT_ANGLE_SYMBOL = "L";
	public static final String P_ZERO_ANGLE_SYMBOL = "Z";
	
	// average speed control
	
	public static double robot_linear_speed_control=0.6;
	public static double robot_angular_speed_control=0.4;
	//robot turn speed control
	public static double robot_rotate_angular_speed_control=0.4;
	public static int robot_rotate_turn_time=4000; // 8secs
	//robot obstacle avoid
	public static final double avoid_obstacle_distance = 3.0;
	public static final int avoid_obstacle_laser_start_index = 110;
	public static final int avoid_obstacle_laser_end_index = 160;
	public static final double avoid_obstacle_angular_speed = 0.5;
	public static final double avoid_obstacle_linear_speed = 0.01;
	
	public static final double right_angle_range_min=-1.55999;
	public static final double right_angle_range_max=-1.55000;
	public static final double right_space_distance=5;
	public static int right_space_robot_forward=5000; // 3sec
	public static int right_space_robot_forward2=5000; // 5sec
	
	public static int back_space_robot_backward=2000; // 3sec
		
	public static final double min_check_boundary_condition=0.5;
	public static final double robot_start_check_tolerance=0.5;
	
//	public static final double HIMM_angle_range_min=-0.08;
//	public static final double HIMM_angle_range_max=0.08;
	public static final int HIMM_distance_array_start_index = 110;
	public static final int HIMM_distance_array_end_index = 150;
	
	//,-0.08726646259971647,-0.06981317007977318,-0.05235987755982989,-0.03490658503988659,-0.017453292519943295,0,0.017453292519943295,0.03490658503988659,0.05235987755982989,0.06981317007977318,0.08726646259971647,
	public static final double left_angle_range_min=1.55000;
	public static final double left_angle_range_max=1.55999;
	
	// Robot directions
	public static final String robot_horizontal_right = "HR";
	public static final String robot_horizontal_left = "HL";
	public static final String robot_vertical_fwd = "VF";
	public static final String robot_vertical_bwd = "BF";
	public static final double init_distance_to_wall=0.7193822264671326;
	
	//To get the last grid element
	public static final double tolerance_for_boundary_grid_elem_width=0.3;
	public static final double tolerance_for_boundary_grid_elem_height=0.5;
	public static final double tolerance_for_boundary_grid_elem_width_left=0.8;
	
	// To stop the robot estimate
	public static final double stop_robot_estimate=0.5;
	
	// exit boundary check
	public static final double exit_boundary_check=5;
	
	// Factory exit1- right
	public static double y2_right_factoryExit_coordinate= -1.39; 
	
	
	public static final String output_map_path ="H:/edu/5DV122/lab2/image";
	
	
	
	// applet constants
	//public static int applet_spacing = 8;
	public static int applet_spacing = 10;
	public static int applet_width = 800;
	public static int applet_height = 800;
	public static int applet_robot_movement_x_label = 1;
	public static int applet_robot_movement_y_label = 2;
	public static int applet_robot_movement_x_coord = 0;
	public static int applet_robot_movement_y_coord = 4;
	public static int applet_robot_movement_coord_spacing = 2;
	
	// astar algorithm constants
	public static int astar_orthogonal_cost = 10; 
	public static int astar_diagnol_cost = 14; 
	public static int astar_bounding_box = 8; 
	public static boolean astar_allowDiagonals=true;
	
	// object symbol constants
	public static final char START_OBJECT_SYMBOL = 'S';
	public static final char GOAL_OBJECT_SYMBOL = 'E';
	public static final char WALL_OBJECT_SYMBOL = 'X';
	public static final char BOUNDARY_OBJECT_SYMBOL = 'B';
	
	// initial start and goal values
	public static String init_start = "start";
	public static String init_goal = "end";
	
	// heuristic types
	//public static String heuristicType="Manhattan";
	/* For generating the optimal(staircase) path and following it */
	//public static String heuristicType="Euclidean";
	/* For generating the sub-optimal path and following it */
	public static String heuristicType="DiagonalShortcut";
	/* For generating the optimal(staircase) path and following it
	 * with the turn angle for movement without jerks */
	//public static String heuristicType="EuclideanTurn";
	
	// For StairCase algorithm
	public static int max_staircase_node_count=6;
	public static int min_staircase_node_count=1;
	
	// Guide robot along a path
	
	public static int robot_turn_control=8000; // 5sec
	public static int robot_diagnol_turn_control=4000; // 2sec
	
	// in cm
	public static double environment_vertical_distance=1004.82;
	public static double environment_horizontal_distance=1623.98;
	
	// Boundary Growth constants
	public static int robot_width_nodeCount_ForObjectBoundaryGrowth=2;
	public static int boundary_connected_boxes = 8; 
	
	// Action commands for button press events
	public static String start_button_command="start";
	public static String stop_button_command="stop";
	public static String forward_button_command="forward";
	public static String top_left_diagnol_button_command="topleftdiagnol";
	public static String top_right_diagnol_button_command="toprightdiagnol";
	public static String bottom_left_diagnol_button_command="bottomleftdiagnol";
	public static String bottom_right_diagnol_button_command="bottomrightdiagnol";
	public static String backward_button_command="backward";
	public static String right_button_command="right";
	public static String left_button_command="left";
	public static String initialize_button_command="initialize";
	
	// File for debug
	public static String debug_file="H:/edu/1.CognitiveRobotics/" +
			"Workspace-Cognitive/mcs10smn-TaxisCognitiveRobot/" +
			"debug_files/debug";
	
	//Image File for processing - the 164x125 is the approximately correct
	public static String image_file="H:/edu/1.CognitiveRobotics/" +
	"Workspace-Cognitive/mcs10smn-TaxisCognitiveRobot/" +
	"images/" +
	"map164x125.png";
	
	//To store the serialized color grid after map processing
	public static String fileStore="H:/edu/1.CognitiveRobotics/" +
		"Workspace-Cognitive/" +
		"mcs10smn-TaxisCognitiveRobot" +
		"/output_files/kompai_serializedColorGrid";
	
	//To store the map of the color grid in text file format
	public static String mapColorGridFile="H:/edu/1.CognitiveRobotics/Workspace-Cognitive/" +
		"mcs10smn-TaxisCognitiveRobot/output_files/" +
		"kompai_mapColorGrid";
	
	//Get the color grid file for applet processing
	public static String colorGrid_ImageFile="H:/edu/1.CognitiveRobotics/" +
	"Workspace-Cognitive/mcs10smn-TaxisCognitiveRobot/" +
	"output_files/kompai_mapColorGrid";
}
