import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import org.junit.Test;
import bloom.dd.HybridEstimator;
import bloom.hash.Hash;
import bloom.hash.HashPseudoRandom;


public class HybridEstimatorTest {

	
	@Test
	public void testConstructors(){
		//TODO: Find a way to test if constructor works
		Hash hash = new HashPseudoRandom();
		HybridEstimator estimator = new HybridEstimator(hash);
		estimator = new HybridEstimator(randomSet(100), hash);
	}
	
	@Test
	public void testInsert(){
		//TODO: Find a way to test if inserts are succeeding
		Hash hash = new HashPseudoRandom();
		HybridEstimator estimator = new HybridEstimator(hash);
		estimator.insert(randomString());
		estimator.insert(randomSet(100));
	}
	
	@Test
	public void testEstimateDifference(){
		assertTrue(false);
	}
	
	
	private Set<String> randomSet(int size){
		Set<String> keys = new HashSet<String>(size);
		while (keys.size() < size)
			keys.add( UUID.randomUUID().toString().substring(0,10) );		
		return keys;
	}
	
	private String randomString(){
		return UUID.randomUUID().toString().substring(0,10);
	}
}
