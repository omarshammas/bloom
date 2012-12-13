package bloom.dd;

import java.util.Set;

import bloom.hash.HashFunction;


public class MinWiseEstimator implements Estimator{
	
	//The number of hashes used in the Varghese paper
	private static final int K = 3840;
	
	// Private Members
	private int[] hashes;
	private int size;
		
	/**
	 * Default Constructor
	 */
	public MinWiseEstimator(){
		hashes = new int[K];
		size = 0;
		for(int i = 0; i < hashes.length; i++){
			hashes[i] = Integer.MAX_VALUE;
		}
	}
	
	/**
	 * Constructor taking in a value for K
	 */
	public MinWiseEstimator(int k){
		hashes = new int[k];
		size = 0;
		for(int i = 0; i < hashes.length; i++){
			hashes[i] = Integer.MAX_VALUE;
		}
	}
	
	/**
	 * Constructor that takes in a set of keys
	 * 
	 * @param	key	A set of keys to be added
	 */
	public MinWiseEstimator(Set<String> keys){
		this();
		insert(keys);
	}
	
	/**
	 * Constructor that takes in a set of keys and a value for k
	 * 
	 * @param	key	A set of keys to be added
	 * @param	number of hashes k
	 */
	public MinWiseEstimator(int k, Set<String> keys){
		this(k);
		insert(keys);
	}

	// Getters
	public int[] getHashes() {
		return hashes;
	}

	public int getSize() {
		return size;
	}
	
	/**
	 * Inserts a key into the estimator. The "hashes" array
	 * is recomputed for this new key
	 * 
	 * @param	key	A key to be added
	 */
	public void insert(String key) {
		size++;
		for (int kk = 0; kk < hashes.length; kk++) {
			int newHash = HashFunction.minWiseHash(key.hashCode(), kk);
			if (hashes[kk] > newHash)
				hashes[kk] = newHash;
		}
	}
	
	/**
	 * Inserts a set of keys to the estimator. The "hashes" 
	 * array is recomputed for each new key
	 * 
	 * @param	keys	A set of keys to be added
	 */
	public void insert(Set<String> keys) {
		size += keys.size();
		for (int kk = 0; kk < hashes.length; kk++) {
			int minValue = hashes[kk];
			hashes[kk] = getMinHashFromSet(keys,kk, minValue);
		}
	}
	
	/**
	 * Returns calculated set difference using self and
	 * input Min-wise estimator.
	 * 
	 * @param	estimator	a min-wise estimator object for a given set
	 * @return	the estimated set difference 
	 */
	public int estimateDifference(Estimator estimator) throws Exception{
		if(!(estimator instanceof MinWiseEstimator))
			throw new Exception("MinWiseEstimator type mismatch!");
		
		MinWiseEstimator minWise = (MinWiseEstimator) estimator;
		
		if(!isEquivalent(minWise)){
			throw new Exception("MinwiseEstimators not Equivalent!");
		}
		
		int[] hashesB = minWise.getHashes();
		float m = 0;
		for(int ii = 0; ii < hashesB.length; ii++){
			if(hashesB[ii] == hashes[ii])
				m++;
		}
		float r = m/hashes.length;
		return (int) ((1-r)/(1+r) * (minWise.getSize()+this.size));
	}
	
	private boolean isEquivalent(MinWiseEstimator minWise) {
		return hashes.length == minWise.getHashes().length;
	}

	/**
	 * Iterates through the set, finds the minimum hash of all the keys
	 * and returns it
	 * 
	 * @param keys			A set of keys to be analyzed
	 * @param k				k to be used as a modulus in the hash function
	 * @param currentMin	Current minimum in "hashes" array
	 * @return				Returns the minimum of hashes of all the keys
	 */
	private int getMinHashFromSet(Set<String> keys, int k, int currentMin) {
		for (String key : keys){
			int nextHash = HashFunction.minWiseHash(key.hashCode(), k);
			if(nextHash < currentMin)
				currentMin = nextHash;
		}
		return currentMin;
	}

}
