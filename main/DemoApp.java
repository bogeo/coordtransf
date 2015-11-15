package main;

import transformations.ScaleAndTranslateTransform;
import transformations.AffineTransform;
import transformations.HelmertTransform;
import transformations.BilinearTransform;

/**
 * Simple demo that shows how to use the CoordTransf library.
 * 
 * @author Benno Schmidt
 */
public class DemoApp {

	public static void main(String[] args) throws Exception 
	{
		DemoApp app = new DemoApp();
		
		app.runDemo1();
		System.out.println("--------------------------------------------");
		app.runDemo2();
		System.out.println("--------------------------------------------");
		app.runDemo3();
		System.out.println("--------------------------------------------");
		app.runDemo4();
	}
	
	public void runDemo1() throws Exception 
	{
		Point[] from = {
				new Point(0., 0.),  
				new Point(200., 200.)};
		
		Point[] to = {
				new Point(3526000., 5730000.),  
				new Point(3528000., 5732000.)};
		
		Transform t = new ScaleAndTranslateTransform(from, to);
		System.out.println(t);
		
		System.out.println(t.transform( 
				new Point(100., 100.)));
	}
	
	public void runDemo2() throws Exception 
	{
		Point[] from = {
				new Point(5.75, 7.25),  
				new Point(15.75, 8.),  
				//new Point(18.5, 15.),  
				new Point(7.25, 13.75)};
		
		Point[] to = {
				new Point(100., 100.),  
				new Point(200., 100.),  
				//new Point(200., 150.),  
				new Point(100., 150.)};
		
		Transform t = new AffineTransform(from, to);
		System.out.println(t);
		System.out.println(new ResidualMismatches(t));

		System.out.println(t.transform( 
				new Point(18., 14.75)));
		System.out.println(t.transform( 
				new Point(11.25, 11.)));
	}

	public void runDemo3() throws Exception 
	{
		Point[] from = {
				new Point(0., 0.),  
				new Point(100., 0.),  
				new Point(100., 100.),  
				new Point(0., 100.)};
		
		Point[] to = {
				new Point(-5.1, -5.),  
				new Point(0., -4.9),  
				new Point(0.1, 0),  
				new Point(-5., -0.1)};
		
		HelmertTransform t = new HelmertTransform(from, to);
		System.out.println(t);
		System.out.println(new ResidualMismatches(t));
		System.out.println("Standard deviation: " + t.standardDeviation());

		System.out.println(t.transform( 
				new Point(-2.5, -2.5)));
	}
	
	public void runDemo4() throws Exception 
	{
		Point[] from = {
				new Point(5.75, 7.25),  
				new Point(15.75, 8.),  
				new Point(18.5, 15.),  
				new Point(7.25, 13.75)};

		Point[] to = {
				new Point(100., 100.),  
				new Point(200., 100.),  
				new Point(200., 150.),  
				new Point(100., 150.)};
		
		Transform t = new BilinearTransform(from, to);
		System.out.println(t);
		System.out.println(new ResidualMismatches(t));

		System.out.println(t.transform( 
				new Point(18., 14.75)));
		System.out.println(t.transform( 
				new Point(11.25, 11.)));
	}
}
