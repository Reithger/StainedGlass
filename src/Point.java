
public class Point implements Comparable<Point>{

	private double x;
	private double y;
	
	public Point(double inX, double inY) {
		x = inX;
		y = inY;
	}
	
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
	
	public double measureDistance(Point other) {
		double difX = Math.abs(x - other.getX());
		double difY = Math.abs(y - other.getY());
		return Math.sqrt(Math.pow(difX, 2.0) + Math.pow(difY, 2.0));
	}
	
	/**
	 * 
	 * Function to generate a point along the edge between two points, its
	 * position instructed by the percentage value 'progress' denoting how
	 * far along between the two points the new point should be.
	 * 
	 * @param other
	 * @param progress
	 * @return
	 */
	
	public Point derivePointBetween(Point other, double progress) {
		double difX = other.getX() - getX();
		double difY = other.getY() - getY();
		
		double newX = getX() + difX * progress;
		double newY = getY() + difY * progress;
		
		//System.out.println("Given " + toString() + ", " + other.toString() + ", with progress " + progress);
		//System.out.println("\t\tDerived: " + (int)newX + ", " + (int)newY);
		
		return new Point((int)newX, (int)newY);
	}
	
	public LineFormula deriveLineFormula(Point other) {
		double m = (other.getY() - getY()) / (other.getX() - getX());
		double b = getY() - m * getX();
		return new LineFormula(m, b, getX() < other.getX() ? getX() : other.getX(), getX() > other.getX() ? getX() : other.getX());
	}

	@Override
	public int compareTo(Point o) {
		if((int)getX() == (int)o.getX() && (int)getY() == (int)o.getY()) {
			return 0;
		}
		if((int)getX() < (int)o.getX()) {
			return -1;
		}
		else if((int)getX() == (int)o.getX()) {
			return 1;
		}
		else if((int)getY() < (int)o.getY()) {
			return -1;
		}
		return 1;
	}
	
	@Override
	public boolean equals(Object o) {
		return compareTo((Point)o) == 0;
	}
	
	@Override
	public String toString() {
		return "(" + (int)getX() + ", " + (int)getY() + ")";
	}
	
}
