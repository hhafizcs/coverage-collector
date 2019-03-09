package com.utd.cs6367;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.junit.runner.Description;
import org.junit.runner.notification.RunListener;

public class CommonListener extends RunListener {
	public void testStarted(Description description) {
		CoverageStorage.getCovLst().clear();
		CoverageStorage.addToCovLst("[Test] " + description.getClassName() + ":" + description.getMethodName());
		/*try {
			FileWriter fw = new FileWriter("stmt-cov.txt", true);
			BufferedWriter bw = new BufferedWriter(fw);
			PrintWriter writer = new PrintWriter(bw);
			writer.println("[Test] " + description.getClassName() + ":" + description.getMethodName());
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}*/
    }
	
	public void testFinished(Description description) {
		Iterator<String> itr = CoverageStorage.getCovLst().iterator();
		List<String> lst = new ArrayList<>();
		itr.forEachRemaining(lst::add);
		
		Collections.sort(lst, new Comparator<String>() {
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
		
		try {
			FileWriter fw = new FileWriter("stmt-cov.txt", true);
			BufferedWriter bw = new BufferedWriter(fw);
			PrintWriter writer = new PrintWriter(bw);
			
			for(String lstItm : lst) {
				writer.println(lstItm);
	        }
			
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
}