package com.rally.psw.bdnotify

import spock.lang.Specification

class BurndownHelperTest extends Specification {
		
	def "get total ToDo Days"() {
		
		def bdHelper = new BurndownHelper()
		
		when: "set todo = 25 days"
		bdHelper.setTotalTodo(25)
		
		then: "expect todo equal 25 days"
		bdHelper.getTotalTodo() == 25
	}
	
	def "get the remaining burndown"() {
		
		def bdHelper = new BurndownHelper()
		
		when: "set remaining bd = 29 days"
		bdHelper.setRemainingBurndown(29)
		
		then: "expect remaining bd equal 29 days"
		bdHelper.getRemainingBurndown() == 29
		
	}
}
