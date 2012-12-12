package bloom.filters;

import java.util.*;

import bloom.hash.HashFunction;

public class InvertibleBloomFilter {
	
	private int hashCount;
	private int size;
	private Cell[] filter;
	
	//TODO Remove, just used for debugging
	private int counter;
	private ArrayList<String> files;
		
	public InvertibleBloomFilter(int hashCount, int size){
		this.hashCount = hashCount;
		this.size = size;
		filter = new Cell[size];
		for(int i=0; i < size; ++i){
			filter[i] = new Cell();
		}
		
		//TODO Remove, just used for debugging
		this.counter = 0;
		this.files = new ArrayList<String>();
	}
	
	public InvertibleBloomFilter(int hashCount, int size, Set<String> keys){
		this(hashCount, size);
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
	
	public void setCell(int i, Cell c){
		filter[i] = c;
	}
		
	//TODO Remove, just used for debugging
	public int getCounter(){
		return counter;
	}
	/*
	//TODO Remove, just used for debugging
	public ArrayList<String> getFiles(){
		return files;
	}*/
	
	//TODO Remove, just used for debugging
		public void printShit(){
			Collections.sort(files);
			//System.out.println("Number of elements : "+counter);
			//System.out.println(files);
			for(Cell c : filter){
				System.out.print(c.count + ", ");
			}
			System.out.println("\n");
		}
	
	public boolean isEmpty(){
		for (Cell c : filter){
			//count can be 0 but elements exist, occurs after a subtraction
			if (!c.isEmpty()){
				return false;
			}
		}
		return true;
	}
		
	public boolean insert(String key){
		//TODO Remove, just used for debugging
		this.counter++;
		//this.files.add(key);
		
		int[] hashes = HashFunction.hash(key, hashCount, size);
		
		for (int i : hashes){
			filter[i].add(key);
		}
		return true;
	}
	
	public void insert(Set<String> keys){
		for(String key : keys)
			insert(key);

	}
	
	public boolean remove(String key){		
		//TODO Remove, just used for debugging
		this.counter--;
		//this.files.remove(key);
				
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
	public ArrayList<String> getDifference() { //TODO throws Exception{
		
		ArrayList<String> difference = new ArrayList<String>();
		ArrayList<Integer> pureCells = new ArrayList<Integer>();
		Integer index;
		String key, prior;
			
		int counter = 0;
		pureCells = getPureCells();
		while (!pureCells.isEmpty()){
			prior = pureCells.toString();
			index = pureCells.remove(pureCells.size() - 1);
			key = getKey(index);
			remove(key);
			difference.add(key);
			pureCells = getPureCells();
			assert prior.equals(pureCells.toString());
			counter++;
			if (counter > 100)
				break;
		}

//TODO		
//		if (!isEmpty())
//			throw new Exception("Unable to determine difference after " + counter +" tries.");
		
		return difference;
	}
	
	public static InvertibleBloomFilter subtract(InvertibleBloomFilter ibf1, InvertibleBloomFilter ibf2){
		assert isEquivalent(ibf1, ibf2);
		
		InvertibleBloomFilter diff = new InvertibleBloomFilter(ibf1.getHashCount(), ibf1.getSize());
		
		for (int i=0; i < ibf1.getSize(); ++i)
			diff.setCell(i, Cell.subtract(ibf1.getCell(i), ibf2.getCell(i)));
		
		return diff;
	}
	
	public static boolean isEquivalent(InvertibleBloomFilter ibf1, InvertibleBloomFilter ibf2){
		return ibf1.getSize() == ibf2.getSize() && ibf1.getHashCount() == ibf2.getHashCount();
	}
	
	
	//TODO make private
	public ArrayList<Integer> getPureCells(){
		ArrayList<Integer> pureCells = new ArrayList<Integer>();
		for (int i=0; i < size; ++i){
			if (getCell(i).isPure())
				pureCells.add(i);
		}
		return pureCells;
	}
	
	private String getKey(int index){
		return getCell(index).extractPure();
	}
}
