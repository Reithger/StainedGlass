package main;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;

/**
 * 
 * Ways to generate the panes:
 *  - Random assortment of points that are laboriously connected to ensure desired properties of our shapes
 *  - Define entire image as one pane and repeatedly insert new point within that shape to 'fracture' the image over time
 *  - Define variety of points along the image border and randomly path out and back into empty space, creating new points along the way
 *  
 * Main consideration is an approach that doesn't allow for having lines between vertices cross over eachother
 * Need to generate the polygons progressively without any crossing over.
 * 
 * The fracturing approach would always involve inserting a new point into an empty space with its related points known to it, no crossing over
 *  - Does this achieve a desired affect, though? Will it look too structured?
 *  - Try it out and adjust as you go
 *  - PaneRegions need a 'fracture' method, then
 *  - Generates 1-2 internal points (these points are a connected line) that each vertice routes through back to itself
 *   - Can work in more variations of internal generations
 *   - For now, the 1-2 internal points should avoid a reduction to only triangles and also avoid crossing over lines (3 points could risk that)
 *   - Except we aren't making a round trip, so we need to insert a new point along the edge between two vertices as our end point
 *   - Introduce one new child PaneRegion per call that takes a chunk out, update the parent's vertices, repeat as long as it isn't too small
 * 
 * 
 * TODO: Detect if a shape is convex or concave so we can use the proper fracture algorithm(s); removes need to always ensure it ends convex
 * 
 * @author Reithger
 *
 */

public class PaneRegion implements Comparable<PaneRegion>, Comparator<PaneRegion>{

//---  Instance Variables   -------------------------------------------------------------------
	
	// Needs to be ordered list of vertices to define how we are drawing the shape
	private ArrayList<Point> vertices;
	
//---  Constructors   -------------------------------------------------------------------------
	
	public PaneRegion() {
		vertices = new ArrayList<Point>();
	}
	
//---  Operations   ---------------------------------------------------------------------------
	
	public void addPoint(Point in) {
		// Should maybe do a sanity check to ensure the shape doesn't get funky from this? We'll see
		vertices.add(in);
	}
	
	private void emptyPaneRegion() {
		vertices.clear();
	}
	
	/**
	 * 
	 * This function causes a 'fracture' that inserts 1-2 points inside of the current
	 * PaneRegion, derives a new internal PaneRegion using these new points, one of the
	 * vertices of the current PaneRegion object, and a point created along one of the
	 * current PaneRegion object's edges to define the vertices of a new PaneRegion.
	 * 
	 * It then cleans up/updates the vertices of this PaneRegion to account for the
	 * new points and the implicit removal of one of its original vertices.
	 * 
	 * TODO: Could we do a spiral effect in decreasing size by moving along the original edges and only ever fracturing the largest PaneRegion?
	 * TODO: Generate regular polygons somewhere within the PaneRegion and connect to vertices to ensure no concave shapes
	 * 
	 * @return
	 */
	
	public ArrayList<PaneRegion> fracture() {
		
		// These are 3 different written out algorithms for this, each inspired by realizing the former one was flawed.
		
		// Choose a vertice to be the one we are 'giving' to the new PaneRegion
		// Choose a point along one of its adjacent edges as the 'endpoint;
		// Generate 1-2 points within the bounds of this PaneRegion that are reasonable to the current two vertices
		//  - No extreme angles, sufficient internal surface area, doesn't bully out another vertice
		//  - Implicitly, this will make the original PaneRegion concave instead of convex, need to correct this
		// Generate a third point inside this PaneRegion to generate another PaneRegion along the adjacent edge
		
		// We have to generate n-1 new PaneRegions where n is the current PaneRegion's number of edges
		// Choose an edge (with vertices an ordered list, use 0 and 1)
		// Generate a new vertex along that edge (e)
		// Generate 1-m points (p1, p2,.., pm) (ensure convex angles) that connect 0 to the new edge vertex in a new PaneRegion
		// Using e and 1, either use pm from earlier or generate new point depending on the angle between e and pm
		//  - If angle of 1, e, pm is acute, we use pm in the next PaneRegion, otherwise 50/50 we do that or generate a new point
		// Generate a new vertex along the edge of 1 and 2 (e2)
		// Generate 1-m points to connect the polygon of 1, e, pm(?), ..., e2
		// For n sides of a shape, we repeat this to create n+1 
		
		// Along each edge of the PaneRegion, introduce a new vertex somewhere (maybe a chance for multiple?)
		// Either connect these directly or introduce new vertices within the PaneRegion they bounce along
		// Each connection should remove a vertice from the parent PaneRegion and create a new child PaneRegion
		// Ultimately, the parent PaneRegion should contain the vertices along each edge and any newly introduced vertices in space
		
		ArrayList<PaneRegion> out = new ArrayList<PaneRegion>();
		
		Random rand = new Random();
		
		if(rand.nextDouble() < .8) {
			out.addAll(centerPointPolygon());
		}
		else {
			if(getNumVertices() > 3) {
				out.addAll(cutInsideEdges());
			}
			else {
				out.addAll(centerPointSpiderweb());
			}
		}
		
		return out;
	}
	
	/**
	 * 
	 * One method of fracturing a stained glass is to put a point in the exact center of your PaneRegion and
	 * 'splinter' the PaneRegion into triangles made up of adjacent vertices and the new center point.
	 * 
	 * The parent PaneRegion disappears in this instance (could just become one of its children?)
	 * 
	 * @return
	 */
	
	protected ArrayList<PaneRegion> centerPointSpiderweb(){
		ArrayList<PaneRegion> out = new ArrayList<PaneRegion>();
		
		Point average = getAveragedCenter();
		
		System.out.println("Average: " + average.getX() + ", " + average.getY());
		
		for(int i = 0; i < getNumVertices(); i++) {
			PaneRegion pane = new PaneRegion();
			pane.addPoint(getVertex(i));
			pane.addPoint(getVertex(i-1));
			pane.addPoint(average);
			out.add(pane);
		}
		
		emptyPaneRegion();
		
		return out;
	}

	/**
	 * 
	 * One method of fracturing a stained glass is to cut across each corner; this required having at least 4
	 * edges to your polygon (and assumes it is convex).
	 * 
	 * Very simple approach, fails in many circumstances but is simple by assuming a simple environment.
	 * 
	 * This transforms the parent PaneRegion into a polygon made up of the points generated along each edge.
	 * 
	 * @return
	 */
	
	private ArrayList<PaneRegion> cutInsideEdges() {
		ArrayList<PaneRegion> out = new ArrayList<PaneRegion>();
		
		ArrayList<Point> edgePoints = new ArrayList<Point>();
		
		Random rand = new Random();
		
		for(int i = 0; i < getNumVertices(); i++) {
			Point v1 = getVertex(i);
			Point v2 = getVertex((i+1) % getNumVertices());
			Point edge = v1.derivePointBetween(v2, .15 + rand.nextDouble() * .7);
			edgePoints.add(edge);			
		}
		
		for(int i = 0; i < edgePoints.size(); i++) {
			PaneRegion pane = new PaneRegion();
			pane.addPoint(edgePoints.get(i));
			pane.addPoint(edgePoints.get((i + (edgePoints.size() - 1)) % edgePoints.size()));
			pane.addPoint(getVertex(i));
			out.add(pane);
		}
		
		updateVertices(edgePoints);
		return out;
	}
	
	/**
	 * 
	 * One method of fracturing a stained glass is to put a perfect polygon inside the PaneRegion and
	 * connect the PaneRegion vertices to the nearest points of the perfect polygon.
	 * 
	 * This will cause concave shapes, so may need to force a centerPointSpiderweb in each PaneRegion we generate.
	 * 
	 * Also creates the PaneRegion that is the perfect polygon itself; need to derive what its size should be based
	 * on the available space.
	 * 
	 * @return
	 */
	
	private ArrayList<PaneRegion> centerPointPolygon(){
		ArrayList<PaneRegion> out = new ArrayList<PaneRegion>();
		
		double shortDist = shortestDistanceToCenter();
		Random rand = new Random();
		
		double radius = shortDist * .2 + shortDist * (rand.nextDouble() * .3);
		
		Point center = getAveragedCenter();
		
		//System.out.println("Center: " + center.getX() + " " + center.getY());
		//System.out.println("Diameter: " + radius);
		
		int numSides = 16 + rand.nextInt(6); //3 * getNumVertices() * (1 + rand.nextInt(2));//getNumVertices() + 1 + (1 + rand.nextInt(3));
		
		PaneRegion shape = generatePerfectPolygon(center, numSides, radius);
		
		while(!containsRegion(shape)) {
			radius = radius * 3.0 / 4.0;
			if(radius < 1) {
				return out;
			}
			shape = generatePerfectPolygon(center, numSides, radius);
			//System.out.println(radius);
		}
		
		out.add(shape);
		
		for(int i = 0; i < getNumVertices(); i++) {
			Point a = getVertex(i);
			Point nearest = shape.findNearestPoint(a);
			int posit = shape.getVertexPosition(nearest);
			
			PaneRegion next = new PaneRegion();
			
			next.addPoint(a);
			int canClaim = 3;

			
			for(int j = -1 * (canClaim / 2); j < (canClaim - 1); j++) {
				next.addPoint(shape.getVertex(posit - j));
			}
			
			out.addAll(next.centerPointSpiderweb());

			PaneRegion negativeSpace = new PaneRegion();
			
			negativeSpace.addPoint(a);
			
			Point nearestPrevVert = shape.findNearestPoint(getVertex(i-1));
			int positPrevVert = (shape.getVertexPosition(nearestPrevVert)) - 1;
			
			positPrevVert = positPrevVert < 0 ? shape.getNumVertices() -1 : positPrevVert;
			
			int walk = (posit + 1) % shape.getNumVertices();
			
			while(walk != positPrevVert) {
				negativeSpace.addPoint(shape.getVertex(walk));
				walk = (walk + 1) % shape.getNumVertices();
			}

			negativeSpace.addPoint(shape.getVertex(walk));
			
			negativeSpace.addPoint(getVertex(i-1));
			
			out.add(negativeSpace);
			//out.addAll(negativeSpace.centerPointSpiderweb());//.centerPointSpiderweb());
		}
		
		
		emptyPaneRegion();
		
		return out;
		
	}
	
	/**
	 * 
	 * Function that informs the index position of an arbitrary point in the internal list
	 * of Vertices for this PaneRegion; this internal list is specially ordered in the way
	 * that the shape would be drawn going counter-clockwise order.
	 * 
	 * @param in
	 * @return
	 */
	
	public int getVertexPosition(Point in) {
		for(int i = 0 ; i < getNumVertices(); i++) {
			if(getVertex(i).equals(in)) {
				return i;
			}
		}
		return -1;
	}
	
	private Point findNearestPoint(Point in) {
		int closest = 0;
		for(int i = 1; i < getNumVertices(); i++) {
			if(in.measureDistance(getVertex(closest)) > in.measureDistance(getVertex(i))){
				closest = i;
			}
		}
		return getVertex(closest);
	}
	
	private PaneRegion generatePerfectPolygon(Point location, int sides, double radius) {
		PaneRegion out = new PaneRegion();
		
		Random rand = new Random();
		
		int innerAngle = 360 / sides;
		int startAngle = rand.nextInt(360);
		
		for(int i = 0; i < sides; i++) {
			double y = radius * Math.cos(startAngle * Math.PI / 180.0);
			double x = radius * Math.sin(startAngle * Math.PI / 180.0);
			out.addPoint(new Point(x + location.getX(), y + location.getY()));
			startAngle -= innerAngle;
		}
		
		return out;
	}
	
//---  Getter/Calculator Methods   ------------------------------------------------------------
	
	/**
	 * 
	 * Hang on, this needs us to arrange our points in counter-clockwise ordering for how we would connect
	 * the points in drawing the shape itself.
	 * 
	 * @return
	 */
	
	public double calculateArea() {
		double out = 0;
		
		for(int i = 0; i < getNumVertices(); i++) {
			Point a = getVertex(i);
			Point b = getVertex(i + 1);
			double add = (a.getX() * b.getY() - b.getX() * a.getY());
			//System.out.println(add);
			out += add;
		}
		
		return Math.abs(out / 2);
	}
	
	/**
	 * 
	 * This refers to an arbitrary point in space, not whether it's one of our vertices
	 * 
	 * @param check
	 * @return
	 */
	
	public boolean containsPoint(Point check) {
		return containsPoint(check.getX(), check.getY());
	}
	
	public boolean containsPoint(double x, double y) {
		ArrayList<LineFormula> edges = new ArrayList<LineFormula>();
		
		for(int i = 0; i < getNumVertices(); i++) {
			edges.add(getVertex(i-1).deriveLineFormula(getVertex(i)));
		}
		
		Point compare = new Point(x, y);
		LineFormula ray = compare.deriveLineFormula(new Point(x + 5000, y));
		
		int intersections = 0;
		
		for(LineFormula edge : edges) {
			intersections += edge.checkIntersects(ray) ? 1 : 0;
		}
		
		return intersections % 2 == 1;
	}
	
	/**
	 * 
	 * This is not an accurate solution for this, potentially the edges between
	 * vertices may not be bounded within the larger region if the larger region
	 * is a very unnatural shape.
	 * 
	 * @param region
	 * @return
	 */
	
	public boolean containsRegion(PaneRegion region) {
		for(int i = 0; i < region.getNumVertices(); i++) {
			if(!containsPoint(region.getVertex(i))) {
				return false;
			}
		}
		return true;
	}
	
	public boolean containsVertice(Point check) {
		return vertices.contains(check);
	}

	public Point getAveragedCenter() {
		Point a = getVertex(0);
		double minX = a.getX();
		double maxX = a.getX();
		double minY = a.getY();
		double maxY = a.getY();
		
		for(int i = 1; i < getNumVertices(); i++) {
			Point b = getVertex(i);
			double x = b.getX();
			double y = b.getY();
			minX = x < minX ? x : minX;
			maxX = x > maxX ? x : maxX;
			minY = y < minY ? y : minY;
			maxY = y > maxY ? y : maxY;
		}
		
		return new Point(minX + (maxX - minX) / 2, minY + (maxY - minY) / 2);
	}
	
	public int getNumVertices() {
		return vertices.size();
	}
	
	public Point getVertex(int posit){
		posit = posit < 0 ? posit + getNumVertices() : posit;
		return vertices.get(posit % getNumVertices());
	}
	
	private double shortestDistanceToCenter() {
		Point center = getAveragedCenter();
		
		double distance = -1;
		
		for(int i = 0; i < getNumVertices(); i++) {
			Point a = getVertex(i);
			double newDist = center.measureDistance(a);
			if(distance == -1 || distance > newDist) {
				distance = newDist;
			}
		}
		
		return distance;
	}
	
//---  Setter Methods   -----------------------------------------------------------------------
	
	public void updateVertices(ArrayList<Point> points) {
		vertices = points;
	}

//---  Mechanics   ----------------------------------------------------------------------------
	
	@Override
	public String toString() {
		StringBuilder out = new StringBuilder();
		for(Point p : vertices) {
			out.append(p.toString());
		}
		return out.toString();
	}

	@Override
	public boolean equals(Object o) {
		return toString().equals(o.toString());
	}

	@Override
	public int compareTo(PaneRegion o) {
		return equals(o) ? 0 : -1;
	}

	@Override
	public int compare(PaneRegion o1, PaneRegion o2) {
		return o1.compareTo(o2);
	}
	
	
}
