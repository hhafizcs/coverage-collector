package com.utd.cs6367;

import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.RunListener;

public class CommonListener extends RunListener {
	
	public void testStarted(Description description) {
		String testName = "[Test] " + description.getClassName() + ":" + description.getMethodName();
		CoverageStorage.addTestToStmtCovList(testName);
    }
	
	public void testRunFinished(Result result) {
		CoverageStorage.writeStmtCovToFile("stmt-cov.txt");
		CoverageStorage.writeVarCovToFile("var-cov.txt");
		
		InvariantFinder invariantFinder = new InvariantFinder();
		invariantFinder.findInvariants(CoverageStorage.getVarCovList());
		invariantFinder.writeInvariantsToFile("invariants.txt");
    }
}