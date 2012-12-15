package bloom.dd;

import java.util.HashSet;
import java.util.Set;

import bloom.filters.InvertibleBloomFilter;
import bloom.hash.Hash;

public class StrataEstimator implements Estimator{

	public static final int HASH_COUNT = 3;
	public static final int STRATA = 32;
	public static final int CUTOFF = 10;
	public static final int IBFSIZE = 80;
	
	private InvertibleBloomFilter[] bloomfilters;
	private int ibfSize;
	private int hashCount;
	private int strata;
	private Hash hash;
	
	public StrataEstimator(Hash hash){
		this(STRATA, new HashSet<String>(), IBFSIZE, HASH_COUNT, hash);
	}
	
	public StrataEstimator(int strata, Hash hash){
		this(strata, new HashSet<String>(), IBFSIZE, HASH_COUNT, hash);
	}
	
	public StrataEstimator(int strata, Set<String> keys, Hash hash){
		this(strata, keys, IBFSIZE, HASH_COUNT, hash);
	}
	
	public StrataEstimator(int strata, Set<String> keys, int ibfSize, Hash hash){
		this(strata, keys, ibfSize, HASH_COUNT, hash);
	}
		
	public StrataEstimator(int strata, Set<String> keys, int ibfSize, int hashCount, Hash hash){
		this.ibfSize = ibfSize;
		this.hashCount = hashCount;
		this.strata = strata;
		this.hash = hash;
		bloomfilters = new InvertibleBloomFilter[strata];
		for(int i=0; i < strata; ++i){
			bloomfilters[i] = new InvertibleBloomFilter(hashCount, ibfSize, hash);
		}
		insert(keys);
	}
		
	public int getSize(){
		return ibfSize;
	}
	
	public int getStrata(){
		return strata;
	}
	
	public int getHashCount(){
		return hashCount;
	}
	
	public Hash getHash(){
		return hash;
	}
	
	public InvertibleBloomFilter getIBF(int index){
		assert index < this.strata; //TODO throw exception
		return bloomfilters[index];
	}

	public void insert(String key){
		int trailingZeros = getNumberOfTrailingZeros(key);
		if (trailingZeros < this.getStrata()){
			bloomfilters[trailingZeros].insert(key);
		}
	}
	
	public void insert(Set<String> keys){
		for(String key : keys){
			insert(key);
		}
	}
	
	public void remove(String key){
		int trailingZeros = getNumberOfTrailingZeros(key);
		if (trailingZeros <  this.getStrata()){
			bloomfilters[trailingZeros].remove(key);
		}
	}

	private int getNumberOfTrailingZeros(String key){
		int hashCode = this.getHash().getHashCode(key);
		int counter = STRATA-1;
		while (hashCode > 0){
			hashCode = hashCode/2;
			counter--;
		}
		
		return counter;
	}
	/**
	 * 
	 * @param estimator 
	 * @return
	 * @throws Exception
	 */
	public int estimateDifference(Estimator estimator) throws Exception{
		if(!(estimator instanceof StrataEstimator))
			throw new Exception("StrataEstimator type mismatch!");
		StrataEstimator se = (StrataEstimator) estimator;
		if(!isEquivalent(se)){
			throw new Exception("StrataEstimators are not equivalent!");
		}

		Set<String> keys;
		InvertibleBloomFilter ibf;
		int difference = 0;
		
		for(int i = this.getStrata()-1; i >= 0; i--){
			ibf = this.getIBF(i).subtract(se.getIBF(i));
			keys = ibf.getPureKeys();
			
			if(!ibf.isEmpty()){
				difference *= Math.pow(2, i+1);
				break;
			}			
			difference += keys.size();
		}
		return difference; 
	}
	
	
	public boolean isEquivalent(StrataEstimator se){
		return this.getSize() == se.getSize() && this.getStrata() == se.getStrata() && this.getHashCount() == se.getHashCount();
	}
}
