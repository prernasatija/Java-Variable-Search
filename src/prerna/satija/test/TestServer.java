package prerna.satija.test;
//Author: Prerna Satija

import static org.junit.Assert.*;
import prerna.satija.code.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class TestServer {

	private void init(IndexConstruction index, List<Pair> pairs) {
		for(Pair p : pairs) {
			index.add(p.getName(), p.getScore());
		}
	}
	
	// Test Case 1:
	@Test
	public void testUnderscore1() {
		IndexConstruction index = new IndexConstruction();
		List<Pair> pairs = getPairsForUnderscoreTest();
		init(index, pairs);
		QueryServer trie = new QueryServer(index);
		
		// Expected Results
		List<Pair> expectedPairs = new ArrayList<>();
		expectedPairs.add(new Pair(0, "underscoreTest1", 70));
		expectedPairs.add(new Pair(2, "back_underscore_test3_", 50));
		expectedPairs.add(new Pair(3, "_forward_underscore_test", 40));
		expectedPairs.add(new Pair(4, "triple___underscore_test5", 30));
		expectedPairs.add(new Pair(5, "double__underscore_test6", 20));
		expectedPairs.add(new Pair(6, "single_underscore_test", 10));
		expectedPairs.add(new Pair(7, "__double_underscore_test6", 5));
		
		// Run Query
		List<Integer> ids = trie.find("under");
		List<Pair> outputPairs = index.getSortedPairs(ids);
		
		if(!checkResults(expectedPairs, outputPairs)) {
			fail("Underscore Testcase 1 Failed");
		}
		else {
			assertTrue("Underscore Testcase 1 Passed", true);
		}
	}
	
	// Test Case 2:
	@Test
	public void testUnderscore2() {
		IndexConstruction index = new IndexConstruction();
		List<Pair> pairs = getPairsForUnderscoreTest();
		init(index, pairs);
		QueryServer trie = new QueryServer(index);
		
		// Expected Results
		List<Pair> expectedPairs = new ArrayList<>();
		expectedPairs.add(new Pair(3, "_forward_underscore_test", 40));
		expectedPairs.add(new Pair(4, "triple___underscore_test5", 30));
		expectedPairs.add(new Pair(5, "double__underscore_test6", 20));
		expectedPairs.add(new Pair(7, "__double_underscore_test6", 5));
		
		// Run Query
		List<Integer> ids = trie.find("_");
		List<Pair> outputPairs = index.getSortedPairs(ids);
		
		if(!checkResults(expectedPairs, outputPairs)) {
			fail("Underscore Testcase 2 Failed");
		}
		else {
			assertTrue("Underscore Testcase 2 Passed", true);
		}
	}
	
	// Test Case 3:
	@Test
	public void testUnderscore3() {
		IndexConstruction index = new IndexConstruction();
		List<Pair> pairs = getPairsForUnderscoreTest();
		init(index, pairs);
		QueryServer trie = new QueryServer(index);
		
		// Expected Results
		List<Pair> expectedPairs = new ArrayList<>();
		expectedPairs.add(new Pair(4, "triple___underscore_test5", 30));
		expectedPairs.add(new Pair(7, "__double_underscore_test6", 5));
		
		// Run Query
		List<Integer> ids = trie.find("__");
		List<Pair> outputPairs = index.getSortedPairs(ids);
		
		if(!checkResults(expectedPairs, outputPairs)) {
			fail("Underscore Testcase 3 Failed");
		}
		else {
			assertTrue("Underscore Testcase 3 Passed", true);
		}
	}
	// Test Case 4:
	@Test
	public void testNumericQuery() {
		IndexConstruction index = new IndexConstruction();
		
		List<Pair> pairs = new ArrayList<>();
		pairs.add(new Pair(0, "que_123", 70));
		pairs.add(new Pair(1, "query1234", 60));
		init(index, pairs);
		QueryServer trie = new QueryServer(index);
		
		// Expected Results
		List<Pair> expectedPairs = new ArrayList<>();
		expectedPairs.add(new Pair(0, "que_123", 70));
	
		// Run Query
		List<Integer> ids = trie.find("1");
		List<Pair> outputPairs = index.getSortedPairs(ids);
		
		if(!checkResults(expectedPairs, outputPairs)) {
			fail("Numeric Testcase Failed");
		}
		else {
			assertTrue("Numeric Testcase Passed", true);
		}
	}
	
	// Test Case 5:
	@Test
	public void testNoMatch() {
		IndexConstruction index = new IndexConstruction();
		
		List<Pair> pairs = new ArrayList<>();
		pairs.add(new Pair(0, "query_123", 70));
		pairs.add(new Pair(1, "query1234", 60));
		init(index, pairs);
		QueryServer trie = new QueryServer(index);
		
		// Expected Results- should be empty
		List<Pair> expectedPairs = new ArrayList<>();
	
		// Run Query
		List<Integer> ids = trie.find("a");
		List<Pair> outputPairs = index.getSortedPairs(ids);
		
		if(!checkResults(expectedPairs, outputPairs)) {
			fail("No Match Testcase Failed");
		}
		else {
			assertTrue("No Match Testcase Passed", true);
		}
	}
	
	private List<Pair> getPairsForUnderscoreTest() {
		List<Pair> pairs = new ArrayList<>();
		pairs.add(new Pair(0, "underscoreTest1", 70));
		pairs.add(new Pair(1, "noUnderscoreTest2", 60));
		pairs.add(new Pair(2, "back_underscore_test3_", 50));
		pairs.add(new Pair(3, "_forward_underscore_test", 40));
		pairs.add(new Pair(4, "triple___underscore_test5", 30));
		pairs.add(new Pair(5, "double__underscore_test6", 20));
		pairs.add(new Pair(6, "single_underscore_test", 10));
		pairs.add(new Pair(7, "__double_underscore_test6", 5));
		
		
		return pairs;
	}
	
	// Comparing expected results with actual results
	private boolean checkResults(List<Pair> expected, List<Pair> actual) {
		if(expected.size() != actual.size()) {
			return false;
		}
		for(int i = 0; i < expected.size(); i++) {
			if(!expected.get(i).getName().equals(actual.get(i).getName())) {
				return false;
			}
		}
		return true;
	}

}
