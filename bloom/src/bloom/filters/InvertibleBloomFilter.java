package bloom.filters;

import java.util.*;

import bloom.hash.HashFunction;

public class InvertibleBloomFilter {
	//Constants
	public static final int HASH_COUNT = 4;
	public static final int SIZE = 80;
	
	//Private Members
	private int hashCount;
	private int size;
	private Cell[] filter;
	
	/**
	 * Default Constructor
	 */
	public InvertibleBloomFilter(){
		this(HASH_COUNT, SIZE);
	}
	
	/**
	 * Constructor that takes in hashCount and size
	 * @param hashCount
	 * @param size
	 */
	public InvertibleBloomFilter(int hashCount, int size){
		this(hashCount, size, new HashSet<String>());
	}
	
	public InvertibleBloomFilter(int hashCount, int size, Set<String> keys){
		this.hashCount = hashCount;
		this.size = size;
		filter = new Cell[size];
		for(int i=0; i < size; ++i){
			filter[i] = new Cell();
		}
		this.insert(keys);
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
	
	public void setCell(int i, Cell cell){
		filter[i] = cell;
	}
		
	public boolean isEmpty(){
		for (Cell cell : filter){
			if (!cell.isEmpty()){
				return false;
			}
		}
		return true;
	}
		
	public void insert(String key){		
		int[] hashes = HashFunction.hash(key, hashCount, size);
		
		for (int i : hashes){
			filter[i].add(key);
		}
	}
	
	public void insert(Set<String> keys){
		for(String key : keys)
			insert(key);

	}
	
	public void remove(String key){				
		int[] hashes = HashFunction.hash(key, hashCount, size);
		
		for (int i : hashes){
			filter[i].remove(key);
		}
	}
	
	public boolean find(String key){
		int[] hashes = HashFunction.hash(key, hashCount, size);
		for (int i : hashes){
			if (filter[i].isEmpty())
				return false;
		}
		return true;
	}
	
	//TODO this function is destructive, either change or emphasize
	public Set<String> getPureKeys() {
		Set<String> difference = new HashSet<String>();
		ArrayList<Integer> pureCells = new ArrayList<Integer>();
		Integer index;
		String key;
		
		int counter = 0;//REMOVE
		pureCells = getPureCells();
		while (!pureCells.isEmpty()){
			index = pureCells.remove(pureCells.size() - 1);
			key = getCell(index).extractKey();
			remove(key);
			difference.add(key);
			pureCells = getPureCells();
			
			//REMOVE
			counter++; System.out.println(pureCells.toString());
			if (counter > 100)
				System.exit(0);
		}		
		return difference;
	}
	
	public InvertibleBloomFilter subtract(InvertibleBloomFilter ibf) throws Exception{
		if(!this.isEquivalent(ibf))
			throw new Exception("IBFs are not equivalent!");
		
		InvertibleBloomFilter diff = new InvertibleBloomFilter(this.getHashCount(), this.getSize());
		for(int i=0; i < ibf.getSize(); ++i)
			diff.setCell(i, Cell.subtract( this.getCell(i), ibf.getCell(i)));
		
		return diff;
	}
	
	public boolean isEquivalent(InvertibleBloomFilter ibf){
		return this.getSize() == ibf.getSize() && this.getHashCount() == ibf.getHashCount();
	}
	
	private ArrayList<Integer> getPureCells(){
		ArrayList<Integer> pureCells = new ArrayList<Integer>();
		for (int i=0; i < getSize(); ++i){
			if (getCell(i).isPure())
				pureCells.add(i);
		}
		return pureCells;
	}

}
