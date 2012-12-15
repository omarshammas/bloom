import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import bloom.dd.DifferenceDigest;
import bloom.dd.DifferenceDigest.EstimatorType;
import bloom.filters.InvertibleBloomFilter;
import bloom.hash.Hash;
import bloom.hash.HashPseudoRandom;
import bloom.utils.Utilities;


public class DifferenceDigestTest {
	
	public static final int NUMBER_OF_KEYS = 20000;
	
	@Test
	public void testStrataEstimator() throws Exception {
		Hash hash = new HashPseudoRandom();
		
		int additional1 = 1000, additional2 = 1000;
		Set<String> keys = Utilities.createKeys(NUMBER_OF_KEYS);
		Set<String> keys1 = Utilities.createKeys(additional1, keys);
		Set<String> keys2 = Utilities.createKeys(additional2, keys); 
		Set<String> keysdiff = Utilities.getSetDifference(keys1, keys2);
		
		DifferenceDigest dd1 = new DifferenceDigest(keys1, EstimatorType.STRATA, hash);
		DifferenceDigest dd2 = new DifferenceDigest(keys2, EstimatorType.STRATA, hash);
		
		System.out.println("Peer2 sends Peer1 its Estimator");
		InvertibleBloomFilter ibf = dd1.getDifferenceIBF( dd2.getEstimator() );
		assert(additional1+additional2 <= ibf.getSize());
		
		System.out.println("Peer2 receives Peer1's IBF of size d*ALPHA, and attempts to determine the differences.");
		Set<String> estimateddiff = dd2.getSetDifference(ibf);		
		assert(keysdiff.containsAll(estimateddiff));
		
		System.out.println("Actual Difference: "+keysdiff.size()+", Estimated Difference: "+estimateddiff.size());
		assert(keysdiff.size() == estimateddiff.size());
	
	}
}
