package com.utd.cs6367;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class CoverageStorage {
	private static String currentTestName = "";
	private static HashMap<String, HashMap<String, HashSet<Integer>>> stmtCovList = new HashMap<String, HashMap<String, HashSet<Integer>>>();
	
	private CoverageStorage() {}
	
    public static HashMap<String, HashMap<String, HashSet<Integer>>> getStmtCovList() {
        return stmtCovList;
    }
    
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
}