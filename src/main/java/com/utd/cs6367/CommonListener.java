package com.utd.cs6367;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.junit.runner.Description;
import org.junit.runner.notification.RunListener;

public class CommonListener extends RunListener {
	public void testStarted(Description description) {
		HashStorage.getHashSet().clear();
		HashStorage.addToHashSet("[Test] " + description.getClassName() + ":" + description.getMethodName());
    }
	
	public void testFinished(Description description) {
		Iterator<String> itr = HashStorage.getHashSet().iterator();
		List<String> lst = new ArrayList<>();
		itr.forEachRemaining(lst::add);
		Collections.reverse(lst);
		
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