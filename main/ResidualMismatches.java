package main;

/**
 * Residual mismatches for the id-points underlying a concrete transformation.
 * 
 * @author Benno Schmidt
 */
public class ResidualMismatches 
{
	private Transform t;
	
	/**
	 * Constructor.
	 * 
	 * @param t Transformation (including id-points)
	 */
	public ResidualMismatches(Transform t) {
		this.t = t;
	}

	public String toString() 
	{
		StringBuilder str = new StringBuilder();
		str.append("Original point -> transformed point:\n");
    
		for (int i = 0; i < t.from.length; i++)
		{
			Point u = t.transform(t.from[i]);
			double vx = t.to[i].x - u.x;
		    double vy = t.to[i].y - u.y;
			str.append(t.from[i] + " -> " + u + ": (" + vx + ", " + vy + "), delta = " + Math.sqrt(vx * vx + vy * vy) + "\n");			
		}
		
		return str.toString();
  }
}
