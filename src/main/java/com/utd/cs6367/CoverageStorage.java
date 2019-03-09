package com.utd.cs6367;

import java.util.HashSet;

public class CoverageStorage {
	private static HashSet<String> covLst = new HashSet<String>();
	
	private CoverageStorage() {}
	
    public static HashSet<String> getCovLst() {
        return covLst;
    }
    
    public static void addToCovLst(String stringToAdd) {
    	covLst.add(stringToAdd);
    }
}