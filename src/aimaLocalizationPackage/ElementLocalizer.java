package aimaLocalizationPackage;

public class ElementLocalizer 
{
	private double distance= 0.0;
	private double angle= 0.0;
	private String angleObjectSymbol= null;
	private double xPos= 0.0;
	private double yPos= 0.0;

	public ElementLocalizer()
	{
	}
	
	public double getDistance() {
		return distance;
	}
	public void setDistance(double distance) {
		this.distance = distance;
	}
	public double getAngle() {
		return angle;
	}
	public void setAngle(double angle) {
		this.angle = angle;
	}
	public String getAngleObjectSymbol() {
		return angleObjectSymbol;
	}

	public void setAngleObjectSymbol(String angleObjectSymbol) {
		this.angleObjectSymbol = angleObjectSymbol;
	}
	
	public double getxPos() {
		return xPos;
	}

	public void setxPos(double xPos) {
		this.xPos = xPos;
	}

	public double getyPos() {
		return yPos;
	}

	public void setyPos(double yPos) {
		this.yPos = yPos;
	}

}
