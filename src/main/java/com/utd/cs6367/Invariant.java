package com.utd.cs6367;

import java.util.ArrayList;

public class Invariant {
	private String name;
	private String pattern;
	private ArrayList<Object> constants;
	private int numOfInstances;
	
	public Invariant(String name, String patternName, ArrayList<Object> constants, int numOfInstances) {
		this.name = name;
		this.pattern = patternName;
		this.constants = constants;
		this.numOfInstances = numOfInstances;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	public ArrayList<Object> getConstants() {
		return constants;
	}

	public void setConstants(ArrayList<Object> constants) {
		this.constants = constants;
	}

	public int getNumOfInstances() {
		return numOfInstances;
	}

	public void setNumOfInstances(int numOfInstances) {
		this.numOfInstances = numOfInstances;
	}
}