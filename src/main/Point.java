package main;

/**
 * 
 * Simple class that represents an arbitrary point in 2D cartesian space, representing
 * the x and y coordinates as double values (these are often reduced to integer representations
 * by other classes but we want the specificity here) though the toString() and equals()
 * functions use integer representations to simplify deciding equality given the usage of
 * objects of this class.
 * 
 * This class also does some logical operations such as measuring the distance between the
 * Point object and another provided Point object and generating a LineFormula object given
 * another Point object representing the y=mx+b formula derived between those two points.
 * 
 * There is also a method to calculate the position of a point a % of the way between this
 * Point object and another one that has been provided.
 * 
 * @author Reithger
 *
 */

public class Point implements Comparable<Point>{

//---  Instance Variables   -------------------------------------------------------------------
	
	private double x;
	private double y;
	
//---  Constructors   -------------------------------------------------------------------------
	
	public Point(double inX, double inY) {
		x = inX;
		y = inY;
	}

//---  Operations   ---------------------------------------------------------------------------
	
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

//---  Getter Methods   -----------------------------------------------------------------------
	
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}

//---  Mechanics   ----------------------------------------------------------------------------

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
