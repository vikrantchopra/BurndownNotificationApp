package com.rally.psw.slope

import spock.lang.Specification

class CalculateSlopeTest extends Specification {

	private def slope
	private List<Double> bdList = new ArrayList<Double>();
	private int startDay
	private int lastDay
	private int sampleSize
	
	def setup() {
		slope = new CalculateSlope()
		this.startDay = 1
		this.lastDay = 5
		this.sampleSize = 5
		
		this.bdList.add(27.5)
		this.bdList.add(23)
		this.bdList.add(20.5)
		this.bdList.add(16)
		this.bdList.add(12.5)
		
	}
	
	
	
	def "adder-test"() {
		given: "a new Adder class is created"
		//def slope = new CalculateSlope()
 
		expect: "Adding two numbers to return the sum"
		//adder.add(3, 4) == 7
		slope.add(3, 4) == 7	
		
		}
	
	def "xSummation - Sum of days"() {		
		assert (1 + 2 + 3 + 4 + 5) == (this.startDay + this.lastDay) * this.sampleSize / 2
	}
}
