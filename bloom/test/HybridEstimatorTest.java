import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import org.junit.Test;
import bloom.dd.HybridEstimator;


public class HybridEstimatorTest {

	
	@Test
	public void testConstructors(){
		//TODO: Find a way to test if constructor works
		HybridEstimator estimator = new HybridEstimator();
		estimator = new HybridEstimator(randomSet(100));
	}
	
	@Test
	public void testInsert(){
		//TODO: Find a way to test if inserts are succeeding
		HybridEstimator estimator = new HybridEstimator();
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
