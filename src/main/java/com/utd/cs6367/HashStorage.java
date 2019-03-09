package com.utd.cs6367;

import java.util.HashSet;

public class HashStorage {
	private static HashSet<String> hashSet = new HashSet<String>();
	
	private HashStorage() {}
	
    public static HashSet<String> getHashSet() {
        return hashSet;
    }
    
    public static void addToHashSet(String stringToAdd) {
        hashSet.add(stringToAdd);
    }
}