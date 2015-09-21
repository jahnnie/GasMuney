package com.example.gasmuney;

public class Person {
	private String name;
	private double amountOwed;
	private double accumulated;
	private int numRides;
	private boolean isRiding = false;
	
	public Person(String name, double amountOwed){
		this.setName(name);
		this.setAmountOwed(amountOwed);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getAmountOwed() {
		return amountOwed;
	}

	public void setAmountOwed(double amountOwed) {
		this.amountOwed = amountOwed;
	}
	
	public void reset(){
		this.amountOwed = 0;
	}

	public int getNumRides() {
		return numRides;
	}

	public void setNumRides(int numRides) {
		this.numRides = numRides;
	}

	public boolean isRiding() {
		return isRiding;
	}

	public void setRiding(boolean isRiding) {
		this.isRiding = isRiding;
	}

	public double getAccumulated() {
		return accumulated;
	}

	public void setAccumulated(double accumulated) {
		this.accumulated = accumulated;
	}
}
