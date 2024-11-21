import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;

/**
 * 
 * I also want to make a version of this that makes each pane an average of the colors in the image beneath it, more of a mosaic then.
 * 
 * @author Reithger
 *
 */

public class StainGlass {

	
	private final static String IMAGE_PATH_ONE = "C:\\Users\\Reithger\\Pictures\\ada.png";
	private final static String IMAGE_PATH_THREE = "C:\\Users\\Reithger\\Pictures\\tobias funke.jpg";
	
	
	private final static String IMAGE_PATH = IMAGE_PATH_ONE;
	
	private final static String OUTPUT_PATH = "C:\\Users\\Reithger\\eclipse-workspace\\StainedGlass\\output";
	private final static String OUTPUT_TITLE = "test8";
	
	
	private final static double PANE_SIZE_THRESHOLD = .1;
	private final static Color FRACTURE_COLOR = new Color(0, 0, 0);
	private final static BasicStroke PEN_SIZE = new BasicStroke(5);  //TODO: This should be reactive to the image size so it doesn't kill the image
	
	private final static int TRANSPARENCY = 150;  //TODO Maybe this should be reactive to the underlying brightness?
	
	/** Color choices here reference this: https://www.color-hex.com/color-palette/5570 */
	private final static Color[] TINT_PALLETTE_ONE = {new Color(103, 40, 40, TRANSPARENCY),
	                                                  new Color(151, 41, 41, TRANSPARENCY),
	                                                  new Color(240, 112, 112, TRANSPARENCY),
	                                                  new Color(34, 122, 98, TRANSPARENCY),
	                                                  new Color(31, 51, 54, TRANSPARENCY)};

	/** Color choices here reference this: https://www.color-hex.com/color-palette/1035349 */
	private final static Color[] TINT_PALLETTE_TWO = {new Color(246, 107, 0, TRANSPARENCY),
	                                                  new Color(255, 142, 0, TRANSPARENCY),
	                                                  new Color(3, 130, 126, TRANSPARENCY),
	                                                  new Color(0, 107, 118, TRANSPARENCY),
	                                                  new Color(12, 77, 91, TRANSPARENCY)};
	
	/** Color choices here reference this: https://www.color-hex.com/color-palette/1017097 */
	private final static Color[] TINT_PALLETTE_THREE = {new Color(216, 1, 41, TRANSPARENCY),
	                                                  new Color(0, 145, 43, TRANSPARENCY),
	                                                  new Color(27, 81, 172, TRANSPARENCY),
	                                                  new Color(194, 192, 8, TRANSPARENCY),
	                                                  new Color(200, 78, 1, TRANSPARENCY)};
	
	
	
	private final static Color[] TINT_PALLETTE = TINT_PALLETTE_THREE;
	
	public static void main(String[] args) {
		
		ArrayList<PaneRegion> panes = new ArrayList<PaneRegion>();
		
		System.out.println("Starting with image: " + IMAGE_PATH);
		
		try {
			BufferedImage bI = ImageIO.read(new File(IMAGE_PATH));
			int wid = bI.getWidth();
			int hei = bI.getHeight();
			
			System.out.println("Image Width: " + wid);
			System.out.println("Image Height: " + hei);
			
			PaneRegion pane = new PaneRegion();
			
			pane.addPoint(new Point(0, 0));
			pane.addPoint(new Point(0, hei));
			pane.addPoint(new Point(wid, hei));
			pane.addPoint(new Point(wid, 0));
			
			panes.add(pane);
			
			boolean stillLargeEnough = true;
			
			while(stillLargeEnough) {
				stillLargeEnough = false;
				
				ArrayList<PaneRegion> newPanes = new ArrayList<PaneRegion>();
				
				for(PaneRegion paneRegion : panes) {
					System.out.println(paneRegion.toString() + ", " + paneRegion.calculateArea());
					if(paneRegion.calculateArea() > PANE_SIZE_THRESHOLD * (wid * hei) && paneRegion.getNumVertices() > 2) {
						stillLargeEnough = true;
						newPanes.addAll(paneRegion.fracture());
					}
				}
				
				panes.addAll(newPanes);
			}
			
			for(PaneRegion paneRegion : panes) {
				System.out.println(paneRegion.toString());
			}
			
			Graphics2D g = bI.createGraphics();
			
			Random rand = new Random();
			
			for(PaneRegion paneRegion : panes) {
				Polygon p = new Polygon();
				
				for(int i = 0; i < paneRegion.getNumVertices(); i++) {
					p.addPoint((int)paneRegion.getVertex(i).getX(), (int)paneRegion.getVertex(i).getY());
				}
				
				g.setColor(TINT_PALLETTE[rand.nextInt(TINT_PALLETTE.length)]);
				
				g.fillPolygon(p);
					
			}
			
			g.setStroke(PEN_SIZE);
			
			g.setColor(FRACTURE_COLOR);
			
			for(PaneRegion paneRegion : panes) {
				for(int i = 0; i < paneRegion.getNumVertices(); i++) {
					Point a = paneRegion.getVertex(i);
					Point b = paneRegion.getVertex(i + 1);
					
					
					g.drawLine((int)a.getX(),(int) a.getY(), (int)b.getX(), (int)b.getY());
				}
			}
			
			File out = new File(OUTPUT_PATH + "/" + OUTPUT_TITLE + ".png");
			
			ImageIO.write(bI, "png", out);
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
}
