package com.utd.cs6367;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class ListHelper {
	private ListHelper() {}
	
	public static List<String> getListFromHashSet (HashSet<String> hashSet) {
		Iterator<String> iterator = hashSet.iterator();
		List<String> list = new ArrayList<String>();
		while(iterator.hasNext()) {
			list.add(iterator.next());
		}
		
		return list;
	}
	
	public static void sortList (List<String> list) {
		Collections.sort(list, new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				if(o1.contains("[Test]")) {
					return -1;
				} else if (o2.contains("[Test]")) {
					return 1;
				}
				
				String o1Class = o1.substring(0, o1.indexOf(':'));
				String o2Class = o2.substring(0, o2.indexOf(':'));
				
				int classComparison = o1Class.compareTo(o2Class);
				
				if(classComparison == 0) {
					int o1Line = Integer.parseInt(o1.substring(o1.indexOf(':') + 1));
					int o2Line = Integer.parseInt(o2.substring(o2.indexOf(':') + 1));
					
					return o1Line - o2Line;
				} else {
					return classComparison;
				}
			}
		});
	}
	
	public static void writeListToFile (List<String> list, String fileName) {
		try {
			FileWriter fileWriter = new FileWriter(fileName, true);
			BufferedWriter buffredWriter = new BufferedWriter(fileWriter);
			PrintWriter printWriter = new PrintWriter(buffredWriter);
			
			for(String listItem : list) {
				printWriter.println(listItem);
	        }
			
			printWriter.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}