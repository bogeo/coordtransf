package main;

/**
 * Abstract base class for all transformations implemented in this library.
 * 
 * @author Benno Schmidt
 */
public abstract class Transform 
{
	/**
	 * Id-points referring to the source coordinate-system.
	 */	
	protected Point[] from;

	/**
	 * Id-points referring to the target coordinate-system.
	 */
	protected Point[] to;

	/**
	 * perform coordinate-transformation for a given point.
	 * 
	 * @param p Point given in the source-coordinate system
	 * @return Point referring to the target coordinate-system
	 */
	abstract public Point transform(Point p);
}
