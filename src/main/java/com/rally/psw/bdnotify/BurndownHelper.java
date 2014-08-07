package com.rally.psw.bdnotify;

public class BurndownHelper {
	private double totalToDo;
	private double remainingBurndown;
	
	public BurndownHelper() {}
	
	public BurndownHelper(double totalToDo, double remainingBurndown) {
		//this.totalToDo = totalToDo;
		//this.remainingBurndown = remainingBurndown;
		this.setTotalTodo(totalToDo);
		this.setRemainingBurndown(remainingBurndown);
	}
	
	public void setTotalTodo(double totalToDo) {
		this.totalToDo = totalToDo;
	}
	
	public void setRemainingBurndown(double remainingBurndown) {
		this.remainingBurndown = remainingBurndown;
	}
	
	public double getTotalTodo() {
		return this.totalToDo;
	}
	
	public double getRemainingBurndown() {
		return this.remainingBurndown;
	}
}
