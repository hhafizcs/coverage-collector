package com.utd.cs6367;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class ListHelper {
	private ListHelper() {}
	
	public static List<Integer> getListFromHashSet (HashSet<Integer> hashSet) {
		Iterator<Integer> iterator = hashSet.iterator();
		List<Integer> list = new ArrayList<Integer>();
		while(iterator.hasNext()) {
			list.add(iterator.next());
		}
		
		return list;
	}
	
	public static void sortList (List<Integer> list) {
		Collections.sort(list, new Comparator<Integer>() {
			@Override
			public int compare(Integer line1, Integer line2) {
				return line1 - line2;
			}
		});
	}
}