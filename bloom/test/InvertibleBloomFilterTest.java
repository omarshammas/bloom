import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.UUID;

import org.junit.Test;

import bloom.filters.Cell;
import bloom.filters.InvertibleBloomFilter;


public class InvertibleBloomFilterTest {
	
	static int HASH_COUNT = 3;
	static int SIZE = 100;
	static String KEY = "teststring";
	static int NUMBER_OF_FILES = 10;
	
	@Test
	public void testConstructor() {
		InvertibleBloomFilter ibf = new InvertibleBloomFilter(HASH_COUNT, SIZE);
		
		assertTrue(ibf.isEmpty());
		assertEquals(SIZE, ibf.getSize());
		assertEquals(HASH_COUNT, ibf.getHashCount());
		try {
			assertEquals(0, ibf.getDifference().size());
		} catch (Exception e) {
			e.printStackTrace();
			fail("Failed to get the difference");
		}
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
	public void testGetDifferenceWithSingleItem() {
		InvertibleBloomFilter ibf = new InvertibleBloomFilter(HASH_COUNT, SIZE);
		
		ibf.insert(KEY);
		
		try {
			ArrayList<String> difference = ibf.getDifference();
			assertEquals(1, difference.size());
			assertEquals(KEY, difference.get(0));
		} catch (Exception e) {
			fail("Failed to get the difference");
		}
	}
	
	@Test
	public void testGetDifferenceWithMultipleItems() {
		InvertibleBloomFilter ibf = new InvertibleBloomFilter(HASH_COUNT, SIZE);
		
		String[] files = createFileNames(NUMBER_OF_FILES);
		for (String s : files)
			ibf.insert(s);
		
		try {
			ArrayList<String> difference = ibf.getDifference();
			assertEquals(NUMBER_OF_FILES, difference.size());
		} catch (Exception e) {
			e.printStackTrace();
			fail("Failed to get the difference");
		}	
	}

	@Test
	public void testSubtract() {
		InvertibleBloomFilter ibf1 = new InvertibleBloomFilter(HASH_COUNT, SIZE);
		InvertibleBloomFilter ibf2 = new InvertibleBloomFilter(HASH_COUNT, SIZE);
		InvertibleBloomFilter ibf_sub;
		
		ibf1.insert(KEY);
		ibf_sub = InvertibleBloomFilter.subtract(ibf1, ibf2);
		try {
			assertEquals(1, ibf_sub.getDifference().size());
		} catch (Exception e) {
			fail("Failed to get the difference");
		}
		
		ibf2.insert(KEY);
		ibf_sub = InvertibleBloomFilter.subtract(ibf1, ibf2);
		try {
			assertEquals(0, ibf_sub.getDifference().size());
		} catch (Exception e) {
			fail("Failed to get the difference");
		}
	}
	
	@Test
	public void testIsEquivalent(){
		InvertibleBloomFilter ibf1 = new InvertibleBloomFilter(HASH_COUNT, SIZE);
		InvertibleBloomFilter ibf2 = new InvertibleBloomFilter(HASH_COUNT, SIZE);

		assertEquals(true, InvertibleBloomFilter.isEquivalent(ibf1, ibf2));
		ibf1.insert(KEY);
		assertEquals(true, InvertibleBloomFilter.isEquivalent(ibf1, ibf2));		
	}
	
	private String[] createFileNames(int number){
		String[] files = new String[number];
		
		for (int i=0; i < number; ++i){
			files[i] = UUID.randomUUID().toString().substring(0,10);
		}
		
		return files;
	}
}
