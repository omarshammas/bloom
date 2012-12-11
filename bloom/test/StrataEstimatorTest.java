import static org.junit.Assert.*;

import java.util.UUID;

import org.junit.Test;

import bloom.dd.StrataEstimator;


public class StrataEstimatorTest {

	
	static int HASH_COUNT = 3;
	static int STRATA = 5;
	static int NUMBER_OF_FILES = 100;
	static int SIZE = (int) (HASH_COUNT*NUMBER_OF_FILES/Math.log(2)); 
	
	
	@Test
	public void testConstructor() {
		String[] files = createFileNames(NUMBER_OF_FILES);
		StrataEstimator se = new StrataEstimator(SIZE, HASH_COUNT, STRATA, files);
		
		assertEquals(HASH_COUNT, se.getHashCount());
		assertEquals(STRATA, se.getStrata());
		assertEquals(HASH_COUNT, se.getHashCount());
	}
	
	@Test
	public void testEstimateDifference() {
		String[] files = createFileNames(NUMBER_OF_FILES);
		StrataEstimator se1 = new StrataEstimator(SIZE, HASH_COUNT, STRATA, files);
		StrataEstimator se2 = new StrataEstimator(SIZE, HASH_COUNT, STRATA, files);
		
		int difference = 0;
		try {
			difference = StrataEstimator.estimateDifference(se1, se2);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Failed to estimate the difference");
		}
		
		System.out.println(difference);
		assertEquals(0, difference);
		
		
		int additional = 20;
		files = createFileNames(NUMBER_OF_FILES+additional);
		StrataEstimator se3 = new StrataEstimator(SIZE, HASH_COUNT, STRATA, files);
		
		try {
			difference = StrataEstimator.estimateDifference(se1, se3);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Failed to estimate the difference");
		}
		
		System.out.println(difference);
		assertEquals(additional, difference);
	}
	
	@Test
	public void testSize(){
		String[] files = createFileNames(NUMBER_OF_FILES);
		StrataEstimator se = new StrataEstimator(SIZE, HASH_COUNT, STRATA, files);
		assertEquals(se.getSize(),SIZE);
	}
	
	
	private String[] createFileNames(int number){
		String[] files = new String[number];
		for (int i=0; i < number; ++i){
			files[i] = UUID.randomUUID().toString().substring(0,10);
		}
		return files;
	}

}
