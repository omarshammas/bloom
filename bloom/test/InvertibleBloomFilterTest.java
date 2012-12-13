import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import org.junit.Test;

import bloom.filters.InvertibleBloomFilter;


public class InvertibleBloomFilterTest {
	
	public static final int TRIALS = 100;
	public static final int NUMBER_OF_KEYS = 100;
	public static final int SIZE = (int) (1.5*NUMBER_OF_KEYS);
	public static final int HASH_COUNT = 3;
	public static final String KEY = "teststring";
	
	
	@Test
	public void testConstructor() {
		InvertibleBloomFilter ibf = new InvertibleBloomFilter(HASH_COUNT, SIZE);
		
		assertTrue(ibf.isEmpty());
		assertEquals(SIZE, ibf.getSize());
		assertEquals(HASH_COUNT, ibf.getHashCount());
		assertEquals(0, ibf.getPureKeys().size());
	}
	
	@Test
	public void testIsEmpty(){
		InvertibleBloomFilter ibf = new InvertibleBloomFilter(HASH_COUNT, SIZE);
		assertEquals(true, ibf.isEmpty());
	}
	
	@Test
	public void testInsert() {
		InvertibleBloomFilter ibf = new InvertibleBloomFilter(HASH_COUNT, SIZE);
		
		ibf.insert(KEY);
		
		assertEquals(false, ibf.isEmpty());
		assertEquals(true, ibf.find(KEY));
	}
	
	@Test
	public void testRemove() {
		InvertibleBloomFilter ibf = new InvertibleBloomFilter(HASH_COUNT, SIZE);
		
		ibf.insert(KEY);
		ibf.remove(KEY);
		
		assertEquals(true, ibf.isEmpty());
		assertEquals(false, ibf.find(KEY));	
	}
	
	@Test
	public void testGetPureKeysWithSingleItem() {
		InvertibleBloomFilter ibf = new InvertibleBloomFilter(HASH_COUNT, SIZE);
		
		ibf.insert(KEY);
		
		Set<String> difference = ibf.getPureKeys();
		
		if(!ibf.isEmpty())
			fail("Failed to get the difference");
		
		assertEquals(1, difference.size());
		assertEquals(true, difference.contains(KEY));
	}
	
	@Test
	public void testGetPureKeysWithMultipleItems() {
		int counter = 0;
		Set<String> keys, diff;
		InvertibleBloomFilter ibf;
		
		for(int t=0; t < TRIALS; ++t){
			 keys = createKeys(NUMBER_OF_KEYS);
			 ibf = new InvertibleBloomFilter(HASH_COUNT, SIZE, keys);
			 
			 diff = ibf.getPureKeys();
			
			if (!ibf.isEmpty()){ //Was unable to decode the ibf, there are still elements
				counter++;
			} else {
				assertEquals(NUMBER_OF_KEYS, diff.size());
				assertEquals(true, diff.equals(keys));
			}
		}
		
		System.out.println("Unable to decode "+counter+" times out of "+TRIALS);
	}

	@Test
	public void testSubtract() throws Exception {
		InvertibleBloomFilter ibf1 = new InvertibleBloomFilter(HASH_COUNT, SIZE);
		InvertibleBloomFilter ibf2 = new InvertibleBloomFilter(HASH_COUNT, SIZE);
		InvertibleBloomFilter ibf_sub;
		
		ibf1.insert(KEY);
		ibf_sub = ibf1.subtract(ibf2);
		assertEquals(1, ibf_sub.getPureKeys().size());

		ibf2.insert(KEY);
		ibf_sub = ibf1.subtract(ibf2);
		assertEquals(0, ibf_sub.getPureKeys().size());
	}
	
	@Test
	public void testSubtractWithMultipleItems() {
		int counter = 0;
		Set<String> keys, diff;
		InvertibleBloomFilter ibf1, ibf2, ibf_sub;
		
		keys = createKeys(NUMBER_OF_KEYS);
		ibf1 = new InvertibleBloomFilter(HASH_COUNT, SIZE, keys);
		ibf2 = new InvertibleBloomFilter(HASH_COUNT, SIZE, keys);
		
		ibf_sub = ibf1.subtract(ibf2);
		assertEquals(0, ibf_sub.getPureKeys().size());
		
		//TODO test with differing sets		
	}
	
	@Test
	public void testIsEquivalent(){
		InvertibleBloomFilter ibf1 = new InvertibleBloomFilter(HASH_COUNT, SIZE);
		InvertibleBloomFilter ibf2 = new InvertibleBloomFilter(HASH_COUNT, SIZE);
		InvertibleBloomFilter ibf3 = new InvertibleBloomFilter(HASH_COUNT+1, SIZE);
		InvertibleBloomFilter ibf4 = new InvertibleBloomFilter(HASH_COUNT, SIZE+1);

		assertEquals(true, ibf1.isEquivalent(ibf2));
		assertEquals(false,  ibf1.isEquivalent(ibf3));
		assertEquals(false,  ibf1.isEquivalent(ibf4));
	}
}
