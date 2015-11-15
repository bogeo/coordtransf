package transformations;

import main.*;

/**
 * Affine transformation. Characteristics: Scaling in x- and y-direction, 
 * rotation, translation, shear mapping.
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
 * <br/>
 * Bibliographical reference: Gruber, F. J. & R. Joeckel (2014): Formelsammlung 
 * für das Vermessungswesen. 17. Aufl., Wiesbaden: Springer.
 * 
 * @author Benno Schmidt
 */
public class AffineTransform extends Transform
{
	protected double A, B, C, D, E, F;
	protected double _o, _a; // just needed to provide control output
	
	// This class realizes both an affine and a Helmert transformation. The
	// following flag controls operation mode:
	protected boolean helmert = false; 
	
	private double eps = 0.000001;
		
	/**
	 * Constructor.
	 * 
	 * @param from Id-points referring to the source coordinate-system
	 * @param to Id-points referring to the target coordinate-system
	 * @throws Exception
	 */
	public AffineTransform(Point[] from, Point[] to) 
			throws Exception 
	{
		this.helmert = false;
		this.start(from, to);
	}

	protected AffineTransform(Point[] from, Point[] to, boolean helmert) 
			throws Exception 
	{
		this.helmert = helmert;
		this.start(from, to);
	}

	private void start(Point[] from, Point[] to) throws Exception 
	{
		this.from = from;
		this.to = to;

		if (from == null || to == null || from.length < 3) {
			throw new Exception("Id-points expected!"); 
		}
		if (from.length != to.length) {
			throw new Exception("Numbers of from-points and to-points must be equal!"); 
		}

		if (from.length == 3)
			this.calcTransformMatrix3();	
		else
			this.calcTransformMatrixN();
	}

	protected void calcTransformMatrix3() throws Exception 
	{
		double[][] m = new double[][] {
				{from[0].x, from[0].y, 1., 0., 0., 0., to[0].x},
				{0., 0., 0., from[0].x, from[0].y, 1., to[0].y},
				{from[1].x, from[1].y, 1., 0., 0., 0., to[1].x},
				{0., 0., 0., from[1].x, from[1].y, 1., to[1].y},
				{from[2].x, from[2].y, 1., 0., 0., 0., to[2].x},
				{0., 0., 0., from[2].x, from[2].y, 1., to[2].y}};  
			
        LinearEquationSystem equations = new LinearEquationSystem(m);
		Double[] transfParam = equations.solve();
		
		A = transfParam[0];
		B = transfParam[1];
		C = transfParam[2];
		D = transfParam[3];
		E = transfParam[4];
		F = transfParam[5];
	}

	protected void calcTransformMatrixN() throws Exception 
	{
		int N = from.length;
		
		if (N == 3) {
			return; 
		}
		
		// Calculate center-point:	
		double sumXOld = 0., sumYOld = 0., sumXNew = 0., sumYNew = 0.;   
		for (int i = 0; i < N; i++) {
			sumXOld += from[i].x;
		    sumYOld += from[i].y;
		    sumXNew += to[i].x;
		    sumYNew += to[i].y;
		}

		// Center-point in old/new coordinate-system:
		Point spOld = new Point(sumXOld / N, sumYOld / N);
		Point spNew = new Point(sumXNew / N, sumYNew / N);

		// Reduction to center-point: 
		double[] sum = new double[7];
		for (int i = 0; i < 7; i++) sum[i] = 0.0;
		double dxOld, dyOld, dxNew, dyNew;
		for (int i = 0; i < N; i++) {
			dxOld = from[i].x - spOld.x;
			dyOld = from[i].y - spOld.y;
			dxNew = to[i].x - spNew.x;
			dyNew = to[i].y - spNew.y;
			 
			if (helmert) { // Helmert transformation
				sum[0] += dxOld * dyNew;
				sum[1] += dyOld * dxNew;
				sum[2] += dyOld * dyOld;
				sum[3] += dxOld * dxOld;
				sum[4] += dxOld * dxNew;
				sum[5] += dyOld * dyNew;
			}
			else { // 6-parameter affine transf.
				sum[0] += dyOld * dyOld;
				sum[1] += dxOld * dxOld;
				sum[2] += dxOld * dyOld;
				sum[3] += dxOld * dxNew;
				sum[4] += dyOld * dyNew;
				sum[5] += dxOld * dyNew;
				sum[6] += dyOld * dxNew;
			}

			// Determine transformation parameters: 
			if (helmert) {
				double divisor = sum[2] + sum[3]; 
				if (Math.abs(divisor) < eps) {
					throw new Exception("Numerical error!");
				}
		   
				_o = (sum[0] - sum[1]) / divisor;
				_a = (sum[4] + sum[5]) / divisor;
		   
				// Transformation matrix:
				A = _a;
				B = -_o;
				D = _o;
				E = _a;

				// Translation vector:
				C = spNew.x - _a * spOld.x + _o * spOld.y;
				F = spNew.y - _o * spOld.x - _a * spOld.y;
			} 
			else {
				double divisor = sum[0] * sum[1] - sum[2] * sum[2];
				if (Math.abs(divisor) < eps) {
					throw new Exception("Numerical error! " + divisor);
				}

				// Transformation matrix:
				A =  (sum[0] * sum[3] - sum[2] * sum[6]) / divisor;
				B = -(sum[2] * sum[3] - sum[1] * sum[6]) / divisor;
				D =  (sum[0] * sum[5] - sum[2] * sum[4]) / divisor;
				E =  (sum[1] * sum[4] - sum[2] * sum[5]) / divisor;

				// Translation vector:
				C = spNew.x - A * spOld.x - B * spOld.y;
				F = spNew.y - D * spOld.x - E * spOld.y;
		   }
		}
	}
	
	public Point transform(Point p) 
	{
		double xn = A * p.x + B * p.y + C;
		double yn = D * p.x + E * p.y + F;
		
		return new Point(xn, yn);
	}

	public void setEps(double eps) {
		this.eps = eps;
	}

	/**
	 * calculates the standard deviation for the transformation. Note that the number
	 * of id-point must be > 3; otherwise the return-value will be 0.
	 * 
	 * @return Standard deviation
	 */
	public double standardDeviation() 
	{
		int N = from.length;
		if (N <= 3)
			return 0.;

		double sum = 0.;
		for (int i = 0; i < N; i++) {
			Point u = transform(from[i]);
			double vx = to[i].x - u.x;
			double vy = to[i].y - u.y;
			sum += (vx * vx) + (vy * vy);
		}
		
		double s;
		if (this instanceof HelmertTransform || this.helmert) 
		{
			s = Math.sqrt(sum / (2 * N - 4));
		} 
		else { // 6-parameter affine
			s = Math.sqrt(sum / (2 * N - 6));
		}
			
		return s;
	}
	
	public String toString() 
	{
		double mx = Math.sqrt(D * D + A * A);
		double my = Math.sqrt(B * B + E * E);
		double alphaX = Math.atan2(D, A) * 200./Math.PI;
		double alphaY = Math.atan2(-B, E) * 200./Math.PI;

		return this.getClass().getSimpleName() + ": " +
				"scale = (" + mx + ", " + my + "), " +
				"rotation = (" + alphaX + " gon, " + alphaY + " gon), " +
				"translation = (" + C + ", " + F + ")";
	}
}
