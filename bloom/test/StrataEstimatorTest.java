import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import bloom.dd.StrataEstimator;
import bloom.hash.HashPseudoRandom;
import bloom.utils.Utilities;

public class StrataEstimatorTest {

	public static final int TRIALS = 100;
	public static final int HASH_COUNT = 3;
	public static final int STRATA = 32;
	public static final int NUMBER_OF_KEYS = 10000;
	public static final int IBFSIZE = 80; 
		
	
	@Test
	public void testConstructor() {
		HashPseudoRandom hash = new HashPseudoRandom();
		Set<String> keys = Utilities.createKeys(NUMBER_OF_KEYS);
		StrataEstimator se = new StrataEstimator(STRATA, keys, IBFSIZE, HASH_COUNT, hash);
		
		assertEquals(HASH_COUNT, se.getHashCount());
		assertEquals(STRATA, se.getStrata());
		assertEquals(IBFSIZE, se.getSize());
	}
	
	@Test
	public void testEstimateDifferenceWithEqualSets() throws Exception {
		HashPseudoRandom hash = new HashPseudoRandom();
		Set<String> keys = Utilities.createKeys(NUMBER_OF_KEYS);
		StrataEstimator se1 = new StrataEstimator(STRATA, keys, IBFSIZE, HASH_COUNT, hash);
		StrataEstimator se2 = new StrataEstimator(STRATA, keys, IBFSIZE, HASH_COUNT, hash);
				
		int difference = se1.estimateDifference(se2);
		assertEquals(0, difference);
	}
	
	@Test
	public void testEstimateDifferenceWithDifferentSets() throws Exception {
		HashPseudoRandom hash = new HashPseudoRandom();
		int additional1 = 100, additional2 = 0;
		Set<String> keys = Utilities.createKeys(NUMBER_OF_KEYS);
		Set<String> keys1 = Utilities.createKeys(additional1, keys);
		Set<String> keys2 = Utilities.createKeys(additional2, keys); 
		Set<String> keysdiff = new HashSet<String>(keys1);
		keysdiff.removeAll(keys2);
		
		StrataEstimator se1 = new StrataEstimator(STRATA, keys1, IBFSIZE, HASH_COUNT, hash);		
		StrataEstimator se2 = new StrataEstimator(STRATA, keys2, IBFSIZE, HASH_COUNT, hash);
		
		se1.printDistro();
		se2.printDistro();
		
		int difference = se1.estimateDifference(se2);
		System.out.println("Actual: "+keysdiff.size()+", Estimated: "+difference);
		assertEquals(keysdiff.size(), difference);
	}
}
