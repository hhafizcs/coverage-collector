package com.utd.cs6367;

import java.util.List;
import org.junit.runner.Description;
import org.junit.runner.notification.RunListener;

public class CommonListener extends RunListener {
	public void testStarted(Description description) {
		CoverageStorage.getCoverageList().clear();
		CoverageStorage.addToCoverageList("[Test] " + description.getClassName() + ":" + description.getMethodName());
    }
	
	public void testFinished(Description description) {
		List<String> coverageList = ListHelper.getListFromHashSet(CoverageStorage.getCoverageList());
		ListHelper.sortList(coverageList);
		ListHelper.writeListToFile(coverageList, "stmt-cov.txt");
    }
}