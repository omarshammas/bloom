package bloom.filters;

import java.util.ArrayList;
import bloom.hash.HashFunction;

public class InvertibleBloomFilter {
	
	private int hashCount;
	private int size;
	private Cell[] filter;
	
	
	public int getSize(){
		return size;
	}
	
	public int getHashCount(){
		return hashCount;
	}
	
	public cell getCell(int i){
		return filter[i];
	}
	
	public void setCell(int i, Cell c){
		filter[i] = c;
	}
	
	public static boolean isEquivalent(InvertibleBloomFilter ibf1, InvertibleBloomFilter ibf2){
		return ibf1.getSize() == ibf2.getSize() && ibf1.getHashCount() == ibf2.getHashCount();
	}
	
	public InvertibleBloomFilter(int hashCount, int size){
		this.hashCount = hashCount;
		this.size = size;
		filter = new Cell[size];
		for (Cell c : filter){
			c = new cell();
		}
	}
	
	public boolean insert(String key){
		int[] hashes = HashFunction.hash(key, hashCount, size);
		for (int i : hashes){
			filter[i].add(key);
		}
		return true;
	}
	

	
	public boolean remove(String key){
		int[] hashes = HashFunction.hash(key, hashCount, size);
		
		for (int i : hashes){
			filter[i].remove(key);
		}
		return true;
	}
	
	public boolean find(String key){
		int[] hashes = HashFunction.hash(key, hashCount, size);
		for (int i : hashes){
			if (filter[i].count < 1)
				return false;
		}
		return true;
	}
	
	public InvertibleBloomFilter subtract(InvertibleBloomFilter ibf){
		
		InvertibleBloomFilter diff = new InvertibleBloomFilter(hashCount, size);
		
		for (int i=0; i < size; ++i){
			diff.setCell( filter[i].cell.subtract(ibf.getCell(i)))
		}
		
		return ibf;
	}
	
	public static InvertibleBloomFilter subtract(InvertibleBloomFilter ibf1, InvertibleBloomFilter ibf2){
		assert ibf1.getSize() == ibf2.getSize();
		assert ibf1.getHashCount() == ibf2.getHashCount();
		
		InvertibleBloomFilter diff = new InvertibleBloomFilter(ibf1., size);
		
		
	}

}
