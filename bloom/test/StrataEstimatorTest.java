import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.junit.Test;

import bloom.dd.StrataEstimator;


public class StrataEstimatorTest {

	static int HASH_COUNT = 3;
	static int STRATA = 32;
	static int NUMBER_OF_FILES = 1000;
	static int SIZE = 80; 
	
	
	@Test
	public void testConstructor() {
		Set<String> files = createFileNames(NUMBER_OF_FILES);
		StrataEstimator se = new StrataEstimator(SIZE, HASH_COUNT, STRATA, files);
		
		assertEquals(HASH_COUNT, se.getHashCount());
		assertEquals(STRATA, se.getStrata());
		assertEquals(SIZE, se.getSize());
	}
	
	@Test
	public void testEstimateDifferenceWithEqualSets() {
		Set<String> files = createFileNames(NUMBER_OF_FILES);
		StrataEstimator se1 = new StrataEstimator(SIZE, HASH_COUNT, STRATA, files);
		StrataEstimator se2 = new StrataEstimator(SIZE, HASH_COUNT, STRATA, files);
				
		int difference = 0;
		try {
			difference = StrataEstimator.estimateDifference(se1, se2);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Failed to estimate the difference");
		}
		assertEquals(0, difference);
	}
	
	
	@Test
	public void testEstimateDifferenceWithDifferentSets() {
		int additional = 60;
		Set<String> files1 = createFileNames(NUMBER_OF_FILES);
		Set<String> files2 = createFileNames(additional);
//		Set<String> union = new HashSet<String>();
//		union.addAll(files1);
//		union.addAll(files2);
//		assertEquals(2*NUMBER_OF_FILES-union.size(), difference);
		
		StrataEstimator se1 = new StrataEstimator(SIZE, HASH_COUNT, STRATA, files1);
		StrataEstimator se2 = new StrataEstimator(SIZE, HASH_COUNT, STRATA, files1);
		for (String file : files2)
			se2.insert(file);
		
		se1.printDistro();
		se2.printDistro();
				
		int difference = 0;
		try {
			difference = StrataEstimator.estimateDifference(se1, se2);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Failed to estimate the difference");
		}
		System.out.println("Estimated: "+difference+", Actual: "+additional);
		assertEquals(additional, difference);
	}
	
	private Set<String> createFileNames(int number){
		Set<String> files = new HashSet<String>(number);
		while (files.size() < number){
			files.add( UUID.randomUUID().toString().substring(0,10) );	
		}	
		return files;
	}

}
