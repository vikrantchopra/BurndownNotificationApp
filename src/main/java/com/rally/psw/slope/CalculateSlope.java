package com.rally.psw.slope;

import java.util.ArrayList;
import java.util.List;

public class CalculateSlope {

	private List<Double> bdList = new ArrayList<Double>();
	
	private double xSummation;
	private double ySummation;
	private double xSquareSummation;
	private double xSummationSquare;
	private double xySummation;
	private int sampleSize;
	private double slope;
	
	public CalculateSlope() {}
	
	public CalculateSlope(List<Double> list) {
		this.bdList = list;
		this.sampleSize = list.size();
		
		this.calculateXSummation(list);
		this.calculateYSummation(list);
		this.calculateXYSummation(list);
		this.calculateXSquareSummation(list);
		this.calculateXSummationSquare();
	}
	
	public void calculateXSummation(List<Double> list) {
		// this is just the summation of the days 
		int start = 1; // any sprint starts with day 1
		int last = list.size(); 
		
		
		this.xSummation = (start + last) * sampleSize / 2;
		
		//System.out.println("X-Summation: " + this.xSummation);
	}
	
	public void calculateYSummation(List<Double> list) {
		for(Double d : list) {
			this.ySummation += d.doubleValue();
		}
		
		//System.out.println("Y-Summation: " + this.ySummation);
	}
	
	public void calculateXYSummation(List<Double> list) {
		for(int i = 0; i < list.size(); i++) {
			this.xySummation += (i + 1) * list.get(i);
		}
		
		//System.out.println("XY-Summation: " + this.xySummation);
	}
		
	public void calculateXSquareSummation(List<Double> list) {
		for(int i = 0; i < sampleSize; i++) {
			this.xSquareSummation += (i + 1) * (i + 1);
		}
		
		System.out.println("X Square Summation: " + this.xSquareSummation);
	}
	
	public void calculateXSummationSquare() {
		this.xSummationSquare = this.xSummation * this.xSummation;
		
		System.out.println("X Summation Square: " + this.xSummationSquare);
	}
	
	public void findSlope() {
		double num = (this.xySummation - (this.xSummation) * (this.ySummation) / this.sampleSize);
		double den = (this.xSquareSummation - this.xSummationSquare / this.sampleSize);
		
		this.slope = num / den;
		
		System.out.println("Slope of the function: " + this.slope);
	}
	
	public void findIntercept() {
		// y = mx + b => b = y - mx
	}
	
	public double add(double a, double b) {
		return (a + b);
	}
}
