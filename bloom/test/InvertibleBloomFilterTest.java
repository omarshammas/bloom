import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Set;

import org.junit.Test;


import bloom.filters.InvertibleBloomFilter;
import bloom.hash.HashPseudoRandom;
import bloom.utils.Utilities;


public class InvertibleBloomFilterTest {
	
	public static final int TRIALS = 1000;
	public static final int NUMBER_OF_KEYS = 100;
	public static final int SIZE = (int) (1.5*NUMBER_OF_KEYS);
	public static final int HASH_COUNT = 3;
	
	
	@Test
	public void testConstructor() {
		HashPseudoRandom hash = new HashPseudoRandom();
		InvertibleBloomFilter ibf = new InvertibleBloomFilter(HASH_COUNT, SIZE, hash);
		
		assertTrue(ibf.isEmpty());
		assertEquals(SIZE, ibf.getSize());
		assertEquals(HASH_COUNT, ibf.getHashCount());
		assertEquals(0, ibf.getPureKeys().size());
	}
	
	@Test
	public void testIsEmpty(){
		HashPseudoRandom hash = new HashPseudoRandom();
		InvertibleBloomFilter ibf = new InvertibleBloomFilter(HASH_COUNT, SIZE, hash);
		assertEquals(true, ibf.isEmpty());
	}
	
	@Test
	public void testInsert() {
		String key = Utilities.createKey();
		HashPseudoRandom hash = new HashPseudoRandom();
		InvertibleBloomFilter ibf = new InvertibleBloomFilter(HASH_COUNT, SIZE, hash);
		
		ibf.insert(key);
		
		assertEquals(false, ibf.isEmpty());
		assertEquals(true, ibf.find(key));
	}
	
	@Test
	public void testRemove() {
		String key = Utilities.createKey();
		HashPseudoRandom hash = new HashPseudoRandom();
		InvertibleBloomFilter ibf = new InvertibleBloomFilter(HASH_COUNT, SIZE, hash);
		
		ibf.insert(key);
		ibf.remove(key);
		
		assertEquals(true, ibf.isEmpty());
		assertEquals(false, ibf.find(key));	
	}
	
	@Test
	public void testGetPureCells(){
		String key;
		InvertibleBloomFilter ibf;
		ArrayList<Integer> difference;
		Set<Integer> hashes;
		HashPseudoRandom hash;
		
		for (int i=0; i < 10000; ++i){
			hash = new HashPseudoRandom();
			key = Utilities.createKey();
			ibf = new InvertibleBloomFilter(HASH_COUNT, SIZE, hash);
			difference = ibf.getPureCells();
			assertEquals(0, difference.size());
			
			ibf.insert(key);
			
			difference = ibf.getPureCells();
			hashes = Utilities.getNumberOfOddItems(hash.hash(key, HASH_COUNT, SIZE));
			assertEquals(hashes.size(), difference.size());		
		}
	}
	
	@Test
	public void testGetPureKeysWithSingleItem() {
		String key = Utilities.createKey();
		HashPseudoRandom hash = new HashPseudoRandom();
		InvertibleBloomFilter ibf = new InvertibleBloomFilter(HASH_COUNT, SIZE, hash);
		
		ibf.insert(key);
		
		Set<String> difference = ibf.getPureKeys();
		
		if(!ibf.isEmpty())
			fail("Failed to get the difference");
		
		assertEquals(1, difference.size());
		assertEquals(true, difference.contains(key));
	}
	
	@Test
	public void testGetPureKeysWithMultipleItems() {
		int counter = 0;
		Set<String> keys, diff;
		InvertibleBloomFilter ibf;
		HashPseudoRandom hash;
		
		for(int t=0; t < TRIALS; ++t){
 			hash = new HashPseudoRandom(); 
			keys = Utilities.createKeys(NUMBER_OF_KEYS);
			 
			ibf = new InvertibleBloomFilter(HASH_COUNT, SIZE, hash, keys);
			 
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
		String key = Utilities.createKey();
		HashPseudoRandom hash = new HashPseudoRandom();
		InvertibleBloomFilter ibf1 = new InvertibleBloomFilter(HASH_COUNT, SIZE, hash);
		InvertibleBloomFilter ibf2 = new InvertibleBloomFilter(HASH_COUNT, SIZE, hash);
		InvertibleBloomFilter ibf_sub;
		
		ibf1.insert(key);
		ibf_sub = ibf1.subtract(ibf2);
		assertEquals(1, ibf_sub.getPureKeys().size());

		ibf2.insert(key);
		ibf_sub = ibf1.subtract(ibf2);
		assertEquals(0, ibf_sub.getPureKeys().size());
	}
	
	@Test
	public void testSubtractWithMultipleItems() throws Exception {
		int additional = 10;
		Set<String> keys;
		InvertibleBloomFilter ibf1, ibf2, ibf_sub;
		HashPseudoRandom hash = new HashPseudoRandom();
		
		keys = Utilities.createKeys(NUMBER_OF_KEYS);
		ibf1 = new InvertibleBloomFilter(HASH_COUNT, SIZE, hash, keys);
		ibf2 = new InvertibleBloomFilter(HASH_COUNT, SIZE, hash, keys);
		
		ibf_sub = ibf1.subtract(ibf2);
		assertEquals(0, ibf_sub.getPureKeys().size());
		
		keys = Utilities.createKeys(additional, keys);
		ibf2 = new InvertibleBloomFilter(HASH_COUNT, SIZE, hash, keys);

		ibf_sub = ibf1.subtract(ibf2);
		assertEquals(additional, ibf_sub.getPureKeys().size());
	}
	
	@Test
	public void testIsEquivalent(){
		HashPseudoRandom hash = new HashPseudoRandom();
		InvertibleBloomFilter ibf1 = new InvertibleBloomFilter(HASH_COUNT, SIZE, hash);
		InvertibleBloomFilter ibf2 = new InvertibleBloomFilter(HASH_COUNT, SIZE, hash);
		InvertibleBloomFilter ibf3 = new InvertibleBloomFilter(HASH_COUNT+1, SIZE, hash);
		InvertibleBloomFilter ibf4 = new InvertibleBloomFilter(HASH_COUNT, SIZE+1, hash);

		assertEquals(true, ibf1.isEquivalent(ibf2));
		assertEquals(false,  ibf1.isEquivalent(ibf3));
		assertEquals(false,  ibf1.isEquivalent(ibf4));
	}
}
