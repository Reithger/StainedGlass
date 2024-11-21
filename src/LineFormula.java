
public class LineFormula {

	private double m;
	private double b;

	private double minX;
	private double maxX;
	
	public LineFormula(double mIn, double bIn, double inMinX, double inMaxX) {
		m = mIn;
		b = bIn;
		minX = inMinX;
		maxX = inMaxX;
	}
	
	public double getSlopeM() {
		return m;
	}
	
	public double getSlopeB() {
		return b;
	}
	
	public double getMinX() {
		return minX;
	}
	
	public double getMaxX() {
		return maxX;
	}
	
	
	//y = mx+b, 0 = m * x + -1 * y + b
	//              a * x +  b * y + c
	public Double getIntersectionX(LineFormula other) {
		double a1 = m;
		double a2 = other.getSlopeM();
		double b = -1;
		double c1 = getSlopeB();
		double c2 = other.getSlopeB();
		
		double out = b * c2 - b * c1;
		double denom = a1 * b - a2 * b;
		
		if(denom == 0) {
			return null;
		}
		
		out /= denom;
		
		return out;
	}
	
	/**
	 * 
	 * Using the defined min/max X values for this LineFormula, this function
	 * checks whether another line would intersect with this line in its domain.
	 * 
	 * @param other
	 * @return
	 */
	
	public boolean checkIntersects(LineFormula other) {
		Double intersectX = getIntersectionX(other);
		if(intersectX == null || intersectX < minX || intersectX > maxX) {
			return false;
		}
		return true;
	}
	
}
