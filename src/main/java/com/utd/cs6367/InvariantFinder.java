package com.utd.cs6367;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class InvariantFinder {
	private static final Set<Class<?>> NUMERIC_WRAPPER_TYPES = getNumericWrapperTypes();
	private HashMap<String, ArrayList<Invariant>> allInvariantsList = new HashMap<String, ArrayList<Invariant>>();
	
	public void findInvariants(HashMap<String, ArrayList<Object>> varCovList) {
		varCovList.forEach((paramName, paramValues) -> {
			for(int i = 0; i < paramValues.size(); i++) {
				ArrayList<Invariant> invariants = new ArrayList<Invariant>();
				Invariant invariant = null;
				boolean foundInvariant = false;
				boolean foundConstantInvariant = false;
				boolean checkForNZ = false;
				boolean checkForCV = true;
				boolean checkForSVS = false;
				boolean isNumeric = isNumericWrapperType(paramValues.get(i).getClass());
				
				if(checkForNZ && isNumeric) {
					invariant = checkForNonZero(paramValues);
					if(invariant != null) {
						foundInvariant = true;
						invariants.add(invariant);
						allInvariantsList.put(paramName, invariants);
					}
				}
				
				if(checkForCV) {
					invariant = checkForConstantValue(paramValues);
					if(invariant != null) {
						if(foundInvariant) {
							allInvariantsList.get(paramName).add(invariant);
						} else {
							foundConstantInvariant = true;
							invariants.add(invariant);
							allInvariantsList.put(paramName, invariants);
						}
					}
				}
				
				if(!foundConstantInvariant && checkForSVS) {
					invariant = checkForSmallValueSet(paramValues);
					if(invariant != null) {
						if(foundInvariant) {
							allInvariantsList.get(paramName).add(invariant);
						} else {
							invariants.add(invariant);
							allInvariantsList.put(paramName, invariants);
						}
					}
				}
			}
		});
	}
	
	public Invariant checkForNonZero(ArrayList<Object> vars) {
		if(vars.size() == 0) {
			return null;
		}
		
		ArrayList<Object> constants = new ArrayList<Object>();
		
		if(vars.get(0).getClass().equals(Integer.class)) {
			for(int i = 0; i < vars.size(); i++) {
				if((Integer)vars.get(i) == 0) {
					return null;
				}
			}
		} else if(vars.get(0).getClass().equals(Long.class)) {
			for(int i = 0; i < vars.size(); i++) {
				if((Long)vars.get(i) == 0) {
					return null;
				}
			}
		} else if(vars.get(0).getClass().equals(Short.class)) {
			for(int i = 0; i < vars.size(); i++) {
				if((Short)vars.get(i) == 0) {
					return null;
				}
			}
		} else if(vars.get(0).getClass().equals(Double.class)) {
			for(int i = 0; i < vars.size(); i++) {
				if((Double)vars.get(i) == 0) {
					return null;
				}
			}
		} else if(vars.get(0).getClass().equals(Float.class)) {
			for(int i = 0; i < vars.size(); i++) {
				if((Float)vars.get(i) == 0) {
					return null;
				}
			}
		}
		
		Invariant invariant = new Invariant("Non-zero", "", constants, vars.size());
		
		return invariant;
	}
	
	public Invariant checkForConstantValue(ArrayList<Object> vars) {
		if(vars.size() == 0) {
			return null;
		}
		
		ArrayList<Object> constants = new ArrayList<Object>();
		constants.add(vars.get(0));
		
		for(int i = 1; i < vars.size(); i++) {
			if(!vars.get(i).equals(constants.get(0))) {
				return null;
			}
		}
		
		Invariant invariant = new Invariant("Constant Value", "", constants, vars.size());
		
		return invariant;
	}
	
	public Invariant checkForSmallValueSet(ArrayList<Object> vars) {
		if(vars.size() < 4) {
			return null;
		}
		
		ArrayList<Object> constants = new ArrayList<Object>();
		boolean sizeOne = true;
		boolean sizeTwo = false;
		Object constantOne = vars.get(0);
		Object constantTwo = null;
		constants.add(constantOne);
		
		for(int i = 1; i < vars.size(); i++) {
			Object var = vars.get(i);
			
			if (sizeTwo) {
				if(!var.equals(constantOne) && !var.equals(constantTwo)) {
					return null;
				}
			} else if(sizeOne) {
				if(!var.equals(constantOne)) {
					sizeOne = false;
					sizeTwo = true;
					constantTwo = var;
					constants.add(constantTwo);
				}
			}
		}
		
		Invariant invariant = new Invariant("Small Value Set", "", constants, vars.size());
		
		return invariant;
	}
	
	public void writeInvariantsToFile(String fileName) {
    	try {
			FileWriter fileWriter = new FileWriter(fileName, true);
			BufferedWriter buffredWriter = new BufferedWriter(fileWriter);
			PrintWriter printWriter = new PrintWriter(buffredWriter);
			
			allInvariantsList.forEach((paramName, paramInvariants) -> {
				printWriter.println(paramName);
				
				for(int i = 0; i < paramInvariants.size(); i++) {
					Invariant invariant = paramInvariants.get(i);
					ArrayList<Object> invariantConstants = invariant.getConstants();
					String invariantPattern = invariant.getPattern();
					String invariantOutput = "";
					
					invariantOutput += invariant.getName();
					
					if(!invariantPattern.equals("")) {
						invariantOutput += ", " + invariantPattern;
					}
					
					if(invariantConstants.size() == 3) {
						invariantOutput += ", a = " + invariantConstants.get(0);
						invariantOutput += ", b = " + invariantConstants.get(1);
						invariantOutput += ", c = " + invariantConstants.get(2);
					} else if (invariantConstants.size() == 2) {
						invariantOutput += ", a = " + invariantConstants.get(0);
						invariantOutput += ", b = " + invariantConstants.get(1);
					} else if (invariantConstants.size() == 1) {
						invariantOutput += ", a = " + invariantConstants.get(0);
					}
					
					invariantOutput += ", NumofInstances = " + invariant.getNumOfInstances();
					
					printWriter.println(invariantOutput);
				}
			});
			
			printWriter.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
	
	public static boolean isNumericWrapperType(Class<?> clazz)
    {
        return NUMERIC_WRAPPER_TYPES.contains(clazz);
    }

    private static Set<Class<?>> getNumericWrapperTypes()
    {
        Set<Class<?>> ret = new HashSet<Class<?>>();
        ret.add(Short.class);
        ret.add(Integer.class);
        ret.add(Long.class);
        ret.add(Float.class);
        ret.add(Double.class);
        return ret;
    }
}