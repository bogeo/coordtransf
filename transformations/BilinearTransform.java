package transformations;

import main.LinearEquationSystem;
import main.Point;
import main.Transform;

/**
 * Bilinear transformation. For four given identical point pairs (id-points) there
 * will be no residual mismatches ("4-point-transformation"). 
 *  
 * <tt><br/>
 *	x' = A x + B y + c x y + D<br/>
 *	y' = E x + F y + G x y + H<br/>
 * </tt>
 *
 * Exactly 4 pairs of identical points (id-points) must be given in the constructor.
 * The point <tt>from[i]</tt> given in the source coordinate-system corresponds 
 * to the point <tt>to[i]</tt> in the target coordinate-system.
 * 
 * @author Benno Schmidt
 */
public class BilinearTransform extends Transform
{
	private double A, B, C, D, E, F, G, H;

	/**
	 * Constructor.
	 * 
	 * @param from Id-points referring to the source coordinate-system
	 * @param to Id-points referring to the target coordinate-system
	 * @throws Exception
	 */
	public BilinearTransform(Point[] from, Point[] to) throws Exception 
	{
		this.from = from;
		this.to = to;
		
		this.calcTransformMatrix();
	}
	
	private void calcTransformMatrix() throws Exception 
	{
		if (from == null || from.length != 4) {
			throw new Exception("4 from-points expected!"); 
		}
		if (to == null || to.length != 4) {
			throw new Exception("4 to-points expected!"); 
		}
		
		double[][] m = new double[][] {
			{from[0].x, from[0].y, from[0].x * from[0].y, 1., 0., 0., 0., 0., to[0].x},
			{0., 0., 0., 0., from[0].x, from[0].y, from[0].x * from[0].y, 1., to[0].y},
			{from[1].x, from[1].y, from[1].x * from[1].y, 1., 0., 0., 0., 0., to[1].x},
			{0., 0., 0., 0., from[1].x, from[1].y, from[1].x * from[1].y, 1., to[1].y},
			{from[2].x, from[2].y, from[2].x * from[2].y, 1., 0., 0., 0., 0., to[2].x},
			{0., 0., 0., 0., from[2].x, from[2].y, from[2].x * from[2].y, 1., to[2].y},
			{from[3].x, from[3].y, from[3].x * from[3].y, 1., 0., 0., 0., 0., to[3].x},
			{0., 0., 0., 0., from[3].x, from[3].y, from[3].x * from[3].y, 1., to[3].y}
		};  
		
        LinearEquationSystem equations = new LinearEquationSystem(m);
		Double[] transfParam = equations.solve();
		if (transfParam.length != 8) {
			throw new Exception("Something went wrong..."); 
		}
		
		A = transfParam[0];
		B = transfParam[1];
		C = transfParam[2];
		D = transfParam[3];
		E = transfParam[4];
		F = transfParam[5];
		G = transfParam[6];
		H = transfParam[7];
	}
	
	public Point transform(Point p) 
	{
		double xn = A * p.x + B * p.y + C * p.x * p.y + D;
		double yn = E * p.x + F * p.y + G * p.x * p.y + H;
		
		return new Point(xn, yn);
	}
	
	public String toString() {
		return this.getClass().getSimpleName() + ": " +
				A + ", " + B + ", " + C + ", " + E + ", " + F + ", " + G + 
				", translation = (" + D + ", " + H + ")";
	}
}
