package bloom.filters;

import java.util.*;
import bloom.hash.HashFunction;

public class InvertibleBloomFilter {
	
	private int hashCount;
	private int size;
	private Cell[] filter;
	
	public InvertibleBloomFilter(int hashCount, int size){
		this.hashCount = hashCount;
		this.size = size;
		filter = new Cell[size];
		for(int i=0; i < size; ++i){
			filter[i] = new Cell();
		}
	}
	
	public int getSize(){
		return size;
	}
	
	public int getHashCount(){
		return hashCount;
	}
	
	public Cell getCell(int i){
		return filter[i];
	}
	
	public void setCell(int i, Cell c){
		filter[i] = c;
	}
	
	public boolean isEmpty(){
		for (Cell c : filter){
			//count can be 0 but elements exist, occurs after a subtraction
			if (c.count != 0 || c.hashSum != 0)
				return false;
		}
		return true;
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
	
	//TODO this function is destructive, either change or emphasize
	public ArrayList<String> getDifference() throws Exception{
		
		ArrayList<String> difference = new ArrayList<String>();
		ArrayList<Integer> pureCells = new ArrayList<Integer>();
		Integer index;
		String key;
			
		pureCells = getPureCells();
		while (!pureCells.isEmpty()){
			index = pureCells.remove(pureCells.size() - 1);
			key = getKey(index);
			remove(key);
			difference.add(key);
			pureCells = getPureCells();
		}

		
		if (!isEmpty())
			throw new Exception("Unable to determine difference.");
		
		return difference;
	}
	
	public static InvertibleBloomFilter subtract(InvertibleBloomFilter ibf1, InvertibleBloomFilter ibf2){
		assert ibf1.getSize() == ibf2.getSize();
		assert ibf1.getHashCount() == ibf2.getHashCount();
		
		InvertibleBloomFilter diff = new InvertibleBloomFilter(ibf1.getHashCount(), ibf1.getSize());
		
		for (int i=0; i < ibf1.getSize(); ++i)
			diff.setCell(i, Cell.subtract(ibf1.getCell(i), ibf2.getCell(i)));
		
		return diff;
	}
	
	public static boolean isEquivalent(InvertibleBloomFilter ibf1, InvertibleBloomFilter ibf2){
		return ibf1.getSize() == ibf2.getSize() && ibf1.getHashCount() == ibf2.getHashCount();
	}
	
	
	private ArrayList<Integer> getPureCells(){
		ArrayList<Integer> pureCells = new ArrayList<Integer>();
		for (int i=0; i < size; ++i){
			if (getCell(i).count == -1 || getCell(i).count == 1 )
				pureCells.add(i);
		}
		return pureCells;
	}
	
	private String getKey(int index){
		return getCell(index).extractPure();
	}
}
