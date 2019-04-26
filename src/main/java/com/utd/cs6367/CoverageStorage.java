package com.utd.cs6367;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CoverageStorage {
	private static String currentTestName = "";
	private static HashMap<String, HashMap<String, HashSet<Integer>>> stmtCovList = new HashMap<String, HashMap<String, HashSet<Integer>>>();
	private static HashMap<String, ArrayList<Object>> varCovList = new HashMap<String, ArrayList<Object>>();
	private static final Set<Class<?>> WRAPPER_TYPES = getWrapperTypes();
	//private static boolean allPrimitive;
	//private static String varToRemove;
	
	private CoverageStorage() {}
    
    public static void addTestToStmtCovList(String testName) {
    	currentTestName = testName;
    	HashMap<String, HashSet<Integer>> testMap = new HashMap<String, HashSet<Integer>>();
    	stmtCovList.put(currentTestName, testMap);
    }
    
    public static void addLineToStmtCovList(String className, int lineNumber) {
    	if(stmtCovList.get(currentTestName) == null) {
    		addTestToStmtCovList("Unknown");
    	} else if(stmtCovList.get(currentTestName).get(className) == null) {
    		HashSet<Integer> classLineNumbers = new HashSet<Integer>();
    		classLineNumbers.add(new Integer(lineNumber));
    		stmtCovList.get(currentTestName).put(className, classLineNumbers);
    	} else {
    		stmtCovList.get(currentTestName).get(className).add(new Integer(lineNumber));
    	}
    }
	
	public static void addVarsToVarCovList(Object[] params, String className, String methodName) {
		String fullMethodName = className + "." + methodName;
		
		int i = 0;
		for (Object param : params) {
			if(param != null && isWrapperType(param.getClass())) {
				String fullParamName = fullMethodName + ".arg" + i;
				
				if(varCovList.get(fullParamName) == null) {
		    		ArrayList<Object> paramValues = new ArrayList<Object>();
		    		paramValues.add(param);
		    		varCovList.put(fullParamName, paramValues);
		    	} else {
		    		varCovList.get(fullParamName).add(param);
		    	}
				
				i++;
			}
		}
		
		/*while(!checkAllVarsPrimitive()) {
			checkAllVarsPrimitive();
		}*/
		
    	/*if(varCovList.get(fullMethodName) == null) {
    		ArrayList<HashMap<String, Object>> methodRunInstances = new ArrayList<HashMap<String, Object>>();
    		methodRunInstances.add(currentMethodRunMap);
    		varCovList.put(fullMethodName, methodRunInstances);
    	} else {
    		varCovList.get(fullMethodName).add(currentMethodRunMap);
    	}*/
    }
	
	public static boolean isWrapperType(Class<?> clazz)
    {
        return WRAPPER_TYPES.contains(clazz);
    }

    private static Set<Class<?>> getWrapperTypes()
    {
        Set<Class<?>> ret = new HashSet<Class<?>>();
        ret.add(Boolean.class);
        ret.add(Character.class);
        ret.add(Byte.class);
        ret.add(Short.class);
        ret.add(Integer.class);
        ret.add(Long.class);
        ret.add(Float.class);
        ret.add(Double.class);
        return ret;
    }
	
	/*public static void addAllObjFields(Object obj, String namePrefix) {
		try {
			Class<?> objClass = obj.getClass();
			Field[] fields = objClass.getFields();
	
			for(Field field : fields) {
			    String varName = namePrefix + "." + field.getName();
			    Object var = null;
			    
				try {
					var = field.get(obj);
				} catch (Exception e) {}
			    
				if(var != null) {
					currentMethodRunMap.put(varName, var);
				}
			}
		} catch (Exception e) {}
	}*/
	
	/*public static boolean checkAllVarsPrimitive() {
		allPrimitive = true;
		
		currentMethodRunMap.forEach((varName, varValue) -> {
			try {
				if(allPrimitive && !isWrapperType(varValue.getClass())) {
					addAllObjFields(varValue, varName);
					allPrimitive = false;
					varToRemove = varName;
				}
			} catch (Exception e) {}
		});
		
		if(!allPrimitive) {
			currentMethodRunMap.remove(varToRemove);
		}
		
		return allPrimitive;
	}*/
	
	/*@SuppressWarnings("rawtypes")
	public static ArrayList<String> getMethodVarNames(String className, String methodName) {
		ArrayList<String> varNames = new ArrayList<String>();
		Class cls = null;
		
		try {
			cls = Class.forName(className);
		} catch (ClassNotFoundException e) {}
		
		if(cls != null) {
			Method[] methods = cls.getDeclaredMethods();
			Method method = null;
			
			for (int i = 0; i < methods.length; i++) {
				if(methods[i].getName().equals(methodName)) {
					method = methods[i];
					break;
				}
			}
			
			if(method != null) {
			    Parameter[] parameters = method.getParameters();

		        for (Parameter parameter : parameters) {
		            varNames.add(parameter.getName());
		        }
			}
		}
		
		return varNames;
	}*/
    
    public static void writeStmtCovToFile(String fileName) {
    	try {
			FileWriter fileWriter = new FileWriter(fileName, true);
			BufferedWriter buffredWriter = new BufferedWriter(fileWriter);
			PrintWriter printWriter = new PrintWriter(buffredWriter);
			
			stmtCovList.forEach((testName, testMap) -> {
				printWriter.println(testName);
	        	
				testMap.forEach((className, lineNumSet) -> {
	            	List<Integer> lineNumList = ListHelper.getListFromHashSet(lineNumSet);
	            	ListHelper.sortList(lineNumList);
	            	
	            	for(Integer lineNum : lineNumList) {
	    				printWriter.println(className + ":" + lineNum);
	    	        }
				});
			});
			
			printWriter.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    public static void writeVarCovToFile(String fileName) {
    	try {
			FileWriter fileWriter = new FileWriter(fileName, true);
			BufferedWriter buffredWriter = new BufferedWriter(fileWriter);
			PrintWriter printWriter = new PrintWriter(buffredWriter);
			
			varCovList.forEach((paramName, paramValues) -> {
				printWriter.println(paramName);
				
				for(int i = 0; i < paramValues.size(); i++) {
					printWriter.println(paramValues.get(i));
				}
			});
			
			printWriter.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
}