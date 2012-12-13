import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import org.junit.Test;

import bloom.dd.HybridEstimator;
import bloom.dd.MinWiseEstimator;


public class MinWiseEstimatorTest {
	
//Tests	
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
	
	@Test
	public void testEstimatorExhaustively(){
		int size = 100000;
		System.out.println("Real J | Calc J | Real Diff | Calc Diff | Overhead\n");
		String key;
		Set<String> setA = new HashSet<String>();
		Set<String> setB = new HashSet<String>();
		for (int ii = 0; ii < size; ii++) {
			key = UUID.randomUUID().toString().substring(0, 15);
			setA.add(key);
			setB.add(key);
		}
		MinWiseEstimator estimatorA = new MinWiseEstimator(setA);
		MinWiseEstimator estimatorB = new MinWiseEstimator(setB);
		key = null;
		for(int ii= 0; ii < size; ii+=100){
			for(int jj = 0; jj < 100;jj++){
				key = UUID.randomUUID().toString().substring(0, 15);
				setA.add(key);
				estimatorA.insert(key);
			}
			Set<String> union = new HashSet<String>(setA);
			Set<String> inter = new HashSet<String>(setA);
			union.addAll(setB);
			inter.retainAll(setB);
			float jCoeff = ((float)inter.size())/union.size();
			System.out.print(jCoeff);
			int d = -1;
			try {
				d = estimatorA.estimateDifference(estimatorB);
			} catch (Exception e) {
				e.printStackTrace();
				assertTrue(false);
			}
			Set<String> diffA = new HashSet<String>(setA);
			diffA.removeAll(setB);
			System.out.print(" | "+diffA.size());
			System.out.print(" | "+d);
			System.out.print(" | "+(diffA.size()/d)+"\n");
		}
	}
	
//Private Methods
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
