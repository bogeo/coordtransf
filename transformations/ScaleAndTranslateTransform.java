package transformations;

import main.LinearEquationSystem;
import main.Point;
import main.Transform;

/**
 * Simple linear transformation offering scaling in x- and y-direction and 
 * a translation ("2-point-transformation").
 *  
 * <tt><br/>
 *	x' = A x + B<br/>
 *	y' = C y + D<br/>
 * </tt>
 *
 * Two pairs of identical points (id-points) must be given in the constructor. 
 * For i = 0 and i = 1, the point <tt>from[i]</tt> given in the source coordinate 
 * system corresponds to the point <tt>to[i]</tt> in the target coordinate-system.
 * 
 * @author Benno Schmidt
 */
public class ScaleAndTranslateTransform extends Transform
{
	private double A, B, C, D;

	/**
	 * Constructor.
	 * 
	 * @param from Id-points referring to the source coordinate-system
	 * @param to Id-points referring to the target coordinate-system
	 * @throws Exception
	 */
	public ScaleAndTranslateTransform(Point[] from, Point[] to) throws Exception 
	{
		this.from = from;
		this.to = to;
		
		this.calcTransformMatrix();
	}
	
	private void calcTransformMatrix() throws Exception 
	{
		if (from == null || from.length != 2) {
			throw new Exception("2 from-points expected!"); 
		}
		if (to == null || to.length != 2) {
			throw new Exception("2 to-points expected!"); 
		}
		
		double[][] m = new double[][] {
			{from[0].x, 1.,        0., 0., to[0].x},
			{      0.0, 0., from[0].y, 1., to[0].y},
			{from[1].x, 1.,        0., 0., to[1].x},
			{      0.0, 0., from[1].y, 1., to[1].y}
		};  
		
        LinearEquationSystem equations = new LinearEquationSystem(m);
		Double[] transfParam = equations.solve();
		if (transfParam.length != 4) {
			throw new Exception("Something went wrong..."); 
		}
		
		A = transfParam[0];
		B = transfParam[1];
		C = transfParam[2];
		D = transfParam[3];
	}
	
	public Point transform(Point p) 
	{
		double xn = A * p.x + B;
		double yn = C * p.y + D;
		
		return new Point(xn, yn);
	}
	
	public String toString() {
		return this.getClass().getSimpleName() + ": " +
				"scale = (" + A + ", " + B + "), " +
				"translation = (" + C + ", " + D + ")";
	}
}
