package main;

/**
 * System of linear equations. The class comprises of a simple solver.
 * 
 * @author Benno Schmidt
 */
public class LinearEquationSystem 
{
	private double m[][];
	private double eps = 0.000001;
	
	/**
	 * Constructor. For a system <tt>A x = b</tt> with <tt>n</tt> unknown variables 
	 * (vector <tt>x</tt>), the matrix M = [A | b] consisting of <tt>n</tt>
	 * columns and <tt>n + 1</tt> columns must be given.
	 * 
	 * @param m Matrix giving the equation coefficients 
	 */
	public LinearEquationSystem(double[][] m) {
		this.m = m;
	}

	/**
	 * solves the given system of linear equations. The implementation simply uses 
	 * Gaussian elemination method (with its well-known deficits!). If for the 
	 * given numerical accuracy (which can be controlled by the method 
	 * <tt>this.setEps()</tt>) no result can be computed, an exception will
	 * be thrown. 
	 * 
	 * @return Solution vector 
	 * @throws Exception
	 */
	public Double[] solve() throws Exception 
	{
		int i;
		//double det = 1.0;
		int dim = m.length;

		// this.testDump();
		
		for (int j = 0; j < dim; j++) {
			
			boolean uniqueSolution = false;
			for (i = j; i < dim; i++) {
				if (Math.abs(m[i][j]) > eps) {
					uniqueSolution = true;
		    	  break;
				}
			}
			if (!uniqueSolution)
				throw new Exception("Could not find a unique solution...");
		
			double xx;
			for (int k = 0; k < dim; k++) {
				xx = m[j][k]; m[j][k] = m[i][k]; m[i][k] = xx;
			}
			xx = m[j][dim]; m[j][dim] = m[i][dim]; m[i][dim] = xx;         
      
			//if (i != j) det *= -1.;
   
			double yy = 1. / m[j][j];
			for (i = 0; i < dim; i++) 
				m[j][i] *= yy;
			m[j][dim] *= yy;
			// det /= yy;

			for (i = 0; i < dim; i++) {
				if (i != j) {
					yy = -m[i][j];
					for (int k = 0; k < dim; k++)
						m[i][k] += yy * m[j][k]; 
					m[i][dim] += yy * m[j][dim]; 
				}
			} // for i
		} // for j 

		Double[] solution = new Double[dim];
		for (i = 0; i < dim; i++)
			solution[i] = new Double(m[i][dim]);
		return solution;
	}

	public void setEps(double eps) {
		this.eps = eps;
	}

	public String toString() 
	{
		StringBuilder str = new StringBuilder();
		int dim = m.length;
		for (int ii = 0; ii < dim; ii++) {
			for (int jj = 0; jj < dim; jj++) {
				str.append(m[ii][jj] + " ");
			}
			str.append("| " + m[ii][dim] + "\n");
		}
		return str.toString();
	}
}
