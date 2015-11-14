package main;

/**
 * Point in 2D space.
 * 
 * @author Benno Schmidt
 */
public class Point {
	public double x, y;

	public Point(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public String toString() {
		return "(" + x + ", " + y + ")";
	}
}
