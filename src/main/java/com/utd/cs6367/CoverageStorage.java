package com.utd.cs6367;

import java.util.HashSet;

public class CoverageStorage {
	private static HashSet<String> coverageList = new HashSet<String>();
	
	private CoverageStorage() {}
	
    public static HashSet<String> getCoverageList() {
        return coverageList;
    }
    
    public static void addToCoverageList(String stringToAdd) {
    	coverageList.add(stringToAdd);
    }
}