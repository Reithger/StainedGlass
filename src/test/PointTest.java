package test;
import static org.junit.jupiter.api.Assertions.*;


import org.junit.jupiter.api.Test;

import main.LineFormula;
import main.Point;

class PointTest {

	
	@Test
	void testGetters() {
		Point a = new Point(5.5, 6.3);
		Point b = new Point(-3.2, 0.0);
		
		assert(a.getX() == 5.5);
		assert(a.getY() == 6.3);
		assert(b.getX() == -3.2);
		assert(b.getY() == 0.0);
		
		assert(a.getX() != b.getX());
		assert(a.getY() != b.getY());
	}
	
	@Test
	void testMeasureDistance() {
		Point a = new Point(1, 0);
		Point b = new Point(2, 0);
		
		assert(a.measureDistance(b) == 1);
		assert(b.measureDistance(a) == 1);
		
		a = new Point(-23, 33);
		b = new Point(153, 22);
		
		//TODO: Need to round off a certain amount given weirdness with doubles
		
		double expected = Math.pow(a.getX() - b.getX(), 2.0) + Math.pow(a.getY() - b.getY(), 2.0);
		expected = Math.sqrt(expected);
		
		assert(a.measureDistance(b) == expected);
		assert(b.measureDistance(a) == expected);
	}
	
	@Test
	void testPointBetween() {
		Point a = new Point(1, 0);
		Point b = new Point(3, 0);
		
		Point c = a.derivePointBetween(b, .5);
		
		assert(c.getX() == 2);
		assert(c.getY() == 0);
		
		c = b.derivePointBetween(a, .5);

		assert(c.getX() == 2);
		assert(c.getY() == 0);
		
		a = new Point(0, 1);
		b = new Point(0, 3);
		
		c = a.derivePointBetween(b, .5);
		
		assert(c.getX() == 0);
		assert(c.getY() == 2);
		
		c = b.derivePointBetween(a, .5);
		
		assert(c.getX() == 0);
		assert(c.getY() == 2);
		
		
		//TODO: More complex one with arbitrary numbers to show correctness outside of base cases
	}
	
	/**
	 * 
	 * As LineFormula is its own class, this just confirms basic object generation,
	 * not internal logical workings.
	 * 
	 */
	
	@Test
	void testLineFormula() {
		Point a = new Point(0, 0);
		Point b = new Point(2, 2);
		
		LineFormula c = a.deriveLineFormula(b);
		
		assert(c.getMinX() == 0);
		assert(c.getMaxX() == 2);
		assert(c.getSlopeM() == 1);
		assert(c.getSlopeB() == 0);
		
		c = b.deriveLineFormula(a);
		
		assert(c.getMinX() == 0);
		assert(c.getMaxX() == 2);
		assert(c.getSlopeM() == 1);
		assert(c.getSlopeB() == 0);
		
		a = new Point(2, 0);
		b = new Point(4, 2);
		
		c = a.deriveLineFormula(b);
		
		assert(c.getMinX() == 2);
		assert(c.getMaxX() == 4);
		assert(c.getSlopeM() == 1);
		assert(c.getSlopeB() == -2);
		
		c = b.deriveLineFormula(a);
		
		assert(c.getMinX() == 2);
		assert(c.getMaxX() == 4);
		assert(c.getSlopeM() == 1);
		assert(c.getSlopeB() == -2);
	}
	
	/**
	 * 
	 * Tests for toString() and equals() methods.
	 * 
	 */
	
	@Test
	void testMechanics() {
		Point a = new Point(5, 5);
		
		assert(a.toString().equals("(5, 5)"));
		
		Point b = new Point(5, 5);
		
		assert(a.equals(b));
		assert(b.equals(a));
		
		Point c = new Point(10, 10);
		
		assert(!a.equals(c));
		assert(!b.equals(c));
		assert(!c.equals(a));
		assert(!c.equals(b));
		
		assert(c.toString().equals("(10, 10)"));
	}

}
