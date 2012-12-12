
import static org.junit.Assert.*;

import java.util.UUID;

import org.junit.Test;

import bloom.hash.HashFunction;



public class HashFunctionTest {
	
	private static final int M = 1000000;
	private static final int m = 100;
	private static final double ERROR = 0.05;
	
	@Test
	public void testMurmurHash(){
		// Initialize array of buckets
		int[] buckets = createFreqArray(m);
		// Fill in array with a running count of hash locations
		int index;
		for (int ii = 0; ii < M; ii++){
			index = HashFunction.murmurHash(UUID.randomUUID().toString().substring(0, 15), m);
			buckets[index] += 1;
		}
		// Test to make sure values are uniform with a 10% error
		float low = (float) ((M / buckets.length) * 0.95);
		float high = (float) ((M / buckets.length) * 1.05);
		System.out.println("Low: "+low);
		System.out.println("High: "+high);
		for (int ii = 0; ii < m; ii++){
			System.out.print(buckets[ii] + " ");
			assertTrue(buckets[ii] > low);
			assertTrue(buckets[ii] < high);
		}
		System.out.print("\n");
	}
	
	@Test
	public void test3Hashes(){
		int k = 3;
		kHashFunctions(k);
	}
	
	@Test
	public void test4Hashes(){
		int k = 4;
		kHashFunctions(k);
	}
	
	@Test
	public void testFnv1Hash(){
		int[] buckets = createFreqArray(m);
		// Fill in array with a running count of hash locations
		int index;
		for (int ii = 0; ii < M; ii++){
			index = HashFunction.fnv1Hash(UUID.randomUUID().toString().substring(0, 15), m);
			buckets[index] += 1;
		}
		// Test to make sure values are uniform with a 10% error
		float low = (float) ((M / buckets.length) * 0.95);
		float high = (float) ((M / buckets.length) * 1.05);
		System.out.println("Low: "+low);
		System.out.println("High: "+high);
		for (int ii = 0; ii < m; ii++){
			System.out.print(buckets[ii] + " ");
			assertTrue(buckets[ii] > low);
			assertTrue(buckets[ii] < high);
		}
		System.out.print("\n");
	}


	private void kHashFunctions(int k) {
		int[] buckets = createFreqArray(m);
		// Fill in array with a running count of hash locations
		for (int ii = 0; ii < M; ii++){
			int[] hashes = HashFunction.hash(UUID.randomUUID().toString().substring(0, 15), k, m);
			for(int jj = 0; jj < hashes.length; jj++)
				buckets[hashes[jj]] += 1;
		}
		float low = (float) ((M*k / buckets.length) * (1-ERROR));
		float high = (float) ((M*k / buckets.length) * (1+ERROR));
		System.out.println("Low: "+low);
		System.out.println("High: "+high);
		for (int ii = 0; ii < m; ii++){
			System.out.print(buckets[ii] + " ");
			assertTrue(buckets[ii] > low);
			assertTrue(buckets[ii] < high);
		}
		System.out.print("\n");
	}
	
	private int[] createFreqArray(int m) {
		int[] buckets = new int[m];
		for (int ii = 0; ii < m; ii++)
			buckets[ii] = 0;
		return buckets;
	}

}
