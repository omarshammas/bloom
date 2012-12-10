import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.junit.Test;

import com.sun.tools.javac.code.Attribute.Array;

import bloom.filters.InvertibleBloomFilter;
import bloom.hash.HashFunction;


public class InvertibleBloomFilterTest {
	
	static int HASH_COUNT = 3;
	static int SIZE = 1000;
	static String KEY = "teststring";
	static int NUMBER_OF_FILES = 100;
	
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
		int counter = 0;
		for(int i=0; i < 1000; ++i){
			//size = 1.5d
			int size = (int) (NUMBER_OF_FILES*1.5);
			InvertibleBloomFilter ibf = new InvertibleBloomFilter(HASH_COUNT, size);
			
			ArrayList<String> files = createFileNames(NUMBER_OF_FILES);
			Set<String> already = new HashSet<String>();
			for (String s : files){
				if (!already.contains(s)){
					ibf.insert(s);
					already.add(s);
				}
			}
								
			try {
				ArrayList<String> difference = ibf.getDifference();
				assertEquals(NUMBER_OF_FILES, difference.size());
				Collections.sort(files);
				Collections.sort(difference);
				assertEquals(true, files.equals(difference));
				
			} catch (Exception e) {
//				e.printStackTrace();
//				fail("Failed to get the difference");
				counter ++;
			}
		}
		System.out.println("Unable to decode "+counter+" times out of 1000");
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
	
	private ArrayList<String> createFileNames(int number){
		ArrayList<String> files = new ArrayList<String>(number);
		
		for (int i=0; i < number; ++i){
			files.add( UUID.randomUUID().toString().substring(0,10) );
		}
		
		return files;
	}
}
