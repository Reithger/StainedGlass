package test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import main.LineFormula;
import main.Point;

class LineFormulaTest {

	/**
	 * 
	 * Test to ensure that the construction of a LineFormula
	 * object derived from two Point objects has the expected
	 * baseline values from a simple example.
	 * 
	 * Technically testing a behavior from Point but I don't
	 * mind the overlapping.
	 * 
	 */
	
	@Test
	void testConstructionFromPoints() {
		Point a = new Point(0, 0);
		Point b = new Point(2, 2);
		
		LineFormula c = a.deriveLineFormula(b);
		
		assert(c.getSlopeM() == 1);
		assert(c.getSlopeB() == 0);
		assert(c.getMinX() == 0);
		assert(c.getMaxX() == 2);
	}
	
	/**
	 * 
	 * Test to ensure that the expected values assigned
	 * to a new LineFormula object are as anticipated;
	 * very simple test of directly-accessing getter
	 * methods.
	 * 
	 * Also ensures no static nonsense; after testing a
	 * second object, re-checks that the first one's
	 * values have not changed.
	 * 
	 */
	
	@Test
	void testGetters() {
		LineFormula a = new LineFormula(2, 3, -4, 4);
		
		assert(a.getSlopeM() == 2);
		assert(a.getSlopeB() == 3);
		assert(a.getMinX() == -4);
		assert(a.getMaxX() == 4);
		
		LineFormula b = new LineFormula(5, 2, 33, 36);
		
		assert(b.getSlopeM() == 5);
		assert(b.getSlopeB() == 2);
		assert(b.getMinX() == 33);
		assert(b.getMaxX() == 36);
		
		assert(a.getSlopeM() == 2);
		assert(a.getSlopeB() == 3);
		assert(a.getMinX() == -4);
		assert(a.getMaxX() == 4);
	}
	
	/**
	 * 
	 * Tests the operation function getIntersectionX() 
	 * that calculates where two LineFormulas would intersect.
	 * 
	 * The function should return the X coordinate of that
	 * intersection as it can be used to derive the Y
	 * coordinate from either LineFormula trivially.
	 * 
	 * This function returns null if they are parallel and
	 * would never intersect.
	 * 
	 */
	
	@Test
	void testGetIntersectionX() {
		LineFormula a = new LineFormula(1, 1, -2, 2);
		LineFormula b = new LineFormula(-1, 1, -2, 2);
		
		assert(a.getIntersectionX(b) == 0);
	}
	
	/**
	 * 
	 * Tests the checkIntersects function which takes
	 * a second LineFormula object as an argument and
	 * decides whether the two would intersect in the
	 * domain of the calling LineFormula object given
	 * its established minX/maxX values.
	 * 
	 */
	
	@Test
	void testCheckIntersects() {
		
	}
	
	@Test
	void testCalculateYCoord() {
		
	}
	
}
