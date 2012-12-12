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
	
	static int HASH_COUNT = 3;
	static int SIZE = 1000;
	static String KEY = "teststring";
	static int NUMBER_OF_FILES = 200;
	
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
			
			Set<String> files = createFileNames(NUMBER_OF_FILES);
			for (String s : files){
				ibf.insert(s);
			}
								
			try {
				ArrayList<String> difference = ibf.getDifference();
				assertEquals(NUMBER_OF_FILES, difference.size());
				assertEquals(true, ibf.isEmpty());
				
				
				ArrayList<String> files_list = new ArrayList<String>();
				for (String s : files)
					files_list.add(s);
				
				Collections.sort(files_list);
				Collections.sort(difference);
				assertEquals(true, files_list.equals(difference));
				
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
	public void testSubstractWithMultipleItems() {
		//size = 1.5d
		int size = (int) (NUMBER_OF_FILES*1.5);
		InvertibleBloomFilter ibf1 = new InvertibleBloomFilter(HASH_COUNT, size);
		InvertibleBloomFilter ibf2 = new InvertibleBloomFilter(HASH_COUNT, size);
		InvertibleBloomFilter ibf3 = new InvertibleBloomFilter(HASH_COUNT, size);
		
		Set<String> files = createFileNames(NUMBER_OF_FILES);
		for (String s : files){
			ibf1.insert(s);
			ibf2.insert(s);
		}
		
		InvertibleBloomFilter ibf_sub = InvertibleBloomFilter.subtract(ibf1, ibf2);
		try {
			assertEquals(0, ibf_sub.getDifference().size());
		} catch (Exception e) {
			fail("Failed to get the difference");
		}		
		
		Random rand = new Random();
		int counter = 0, num = rand.nextInt(NUMBER_OF_FILES);
		for (String s : files){
			if (counter >= num)
				break;
			
			ibf3.insert(s);
			counter++;
		}
		
		ibf_sub = InvertibleBloomFilter.subtract(ibf1, ibf3);
		try {
			ArrayList<String> difference = ibf_sub.getDifference();
			assertEquals(NUMBER_OF_FILES-num, difference.size());
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
	
	private Set<String> createFileNames(int number){
		Set<String> files = new HashSet<String>(number);
		
		while (files.size() < number){
			files.add( UUID.randomUUID().toString().substring(0,10) );	
		}
		
		return files;
	}
}
