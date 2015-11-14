package transformations;

import main.Point;

/**
 * Helmert-transformation. Characteristics: <i>Uniform scaling</i> for x- and 
 * y-direction, rotation, translation.
 *  
 * <tt><br/>
 *	x' = A x + B y + C<br/>
 *	y' = D x + E y + F<br/>
 * </tt>
 *
 * At least 3 pairs of identical points (id-points) must be given in the 
 * constructor. The point <tt>from[i]</tt> given in the source coordinate-system 
 * corresponds to the point <tt>to[i]</tt> in the target coordinate-system for 
 * i = 0, ..., N - 1 with N >= 3. For N = 3, an exact transformation will be
 * calculated. For N > 3 usually there will be residual mismatches.
 *
 * @author Benno Schmidt
 */
public class HelmertTransform extends AffineTransform
{
	/**
	 * Constructor.
	 * 
	 * @param from Id-points referring to the source coordinate-system
	 * @param to Id-points referring to the target coordinate-system
	 * @throws Exception
	 */
	public HelmertTransform(Point[] from, Point[] to) throws Exception 
	{
		super(from, to, true);
	}
	
	public String toString() 
	{
		double scale = Math.sqrt(_a * _a + _o * _o);
		double rotation = 200. * Math.atan(_o / _a) / Math.PI;

		return this.getClass().getSimpleName() + ": " +
				"scale = " + scale + ", " +
				"rotation = " + rotation + " gon, " +
				"translation = (" + C + ", " + F + ")";
	}
}
