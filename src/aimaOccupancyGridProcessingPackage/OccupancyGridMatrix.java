package aimaOccupancyGridProcessingPackage;

public class OccupancyGridMatrix 
{
	private int element_id= 0;
	private int element_rowNo= 0;
	private int element_colNo= 0;
	private double element_P_Occupied= 0.0;
	private double element_P_Empty= 0.0;
	private double element_squareWidth_s_distance= 0.0;
	private double element_squareWidth_e_distance= 0.0;
	private double element_squareHeight_s_distance= 0.0;
	private double element_squareHeight_e_distance= 0.0;
	private boolean robotOnElement=false;
	private String element_symbol=null;
	private int element_HIMM_Factor=0;
	
	public OccupancyGridMatrix()
	{
	}
	
	public int getElement_id() {
		return element_id;
	}
	public void setElement_id(int element_id) {
		this.element_id = element_id;
	}
	public int getElement_rowNo() {
		return element_rowNo;
	}
	public void setElement_rowNo(int element_rowNo) {
		this.element_rowNo = element_rowNo;
	}
	public int getElement_colNo() {
		return element_colNo;
	}
	public void setElement_colNo(int element_colNo) {
		this.element_colNo = element_colNo;
	}
	public double getElement_P_Occupied() {
		return element_P_Occupied;
	}
	public void setElement_P_Occupied(double element_P_Occupied) {
		this.element_P_Occupied = element_P_Occupied;
	}
	public double getElement_P_Empty() {
		return element_P_Empty;
	}
	public void setElement_P_Empty(double element_P_Empty) {
		this.element_P_Empty = element_P_Empty;
	}
	
	
	public double getElement_squareWidth_s_distance() {
		return element_squareWidth_s_distance;
	}

	public void setElement_squareWidth_s_distance(
			double element_squareWidth_s_distance) {
		this.element_squareWidth_s_distance = element_squareWidth_s_distance;
	}

	public double getElement_squareWidth_e_distance() {
		return element_squareWidth_e_distance;
	}

	public void setElement_squareWidth_e_distance(
			double element_squareWidth_e_distance) {
		this.element_squareWidth_e_distance = element_squareWidth_e_distance;
	}

	public double getElement_squareHeight_s_distance() {
		return element_squareHeight_s_distance;
	}

	public void setElement_squareHeight_s_distance(
			double element_squareHeight_s_distance) {
		this.element_squareHeight_s_distance = element_squareHeight_s_distance;
	}

	public double getElement_squareHeight_e_distance() {
		return element_squareHeight_e_distance;
	}

	public void setElement_squareHeight_e_distance(
			double element_squareHeight_e_distance) {
		this.element_squareHeight_e_distance = element_squareHeight_e_distance;
	}
	
	public boolean isRobotOnElement() {
		return robotOnElement;
	}

	public void setRobotOnElement(boolean robotOnElement) {
		this.robotOnElement = robotOnElement;
	}
	public int getElement_HIMM_Factor() {
		return element_HIMM_Factor;
	}

	public void setElement_HIMM_Factor(int element_HIMM_Factor) {
		this.element_HIMM_Factor = element_HIMM_Factor;
	}
	
	public String getElement_symbol() {
		return element_symbol;
	}

	public void setElement_symbol(String element_symbol) {
		this.element_symbol = element_symbol;
	}
	
	
}
