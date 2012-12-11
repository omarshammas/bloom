package bloom.dd;

import java.util.Iterator;
import java.util.Set;

import bloom.hash.HashFunction;


public class MinWiseEstimator {
	
	//The number of hashes used in the Varghese paper
	private static final int K = 3840;
	private int[] hashes;
	private int size;
	
	//Getter and Setters
	public int[] getHashes() {
		return hashes;
	}

	public void setHashes(int[] hashes) {
		this.hashes = hashes;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}
	
	/**
	 * Default Constructor
	 */
	public MinWiseEstimator(){
		hashes = new int[K];
		size = 0;
	}
	
	/**
	 * Constructor that takes in a set of keys
	 * 
	 * @param	key	A set of keys to be added
	 */
	public MinWiseEstimator(Set<String> keys){
		size = keys.size();
		hashes = new int[K];
		for(int kk= 0; kk < hashes.length; kk++){
			int minValue = Integer.MAX_VALUE;
			minValue = getMinHashFromSet(keys, kk, minValue);
			hashes[kk] = minValue;
		}
	}
	
	/**
	 * Adds a key to the estimator. The "hashes" array
	 * is recomputed for this new key
	 * 
	 * @param	key	A key to be added
	 */
	public void add(String key) {
		size++;
		for (int kk = 0; kk < hashes.length; kk++) {
			int newHash = HashFunction.minWiseHash(key.hashCode(), kk);
			if (hashes[kk] > newHash)
				hashes[kk] = newHash;
		}
	}
	
	/**
	 * Adds a set of keys to the estimator. The "hashes" array
	 * is recomputed for each new key
	 * 
	 * @param	keys	A set of keys to be added
	 */
	public void addKeys(Set<String> keys) {
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
	 * @return				the estimated set difference 
	 */
	public float difference(MinWiseEstimator estimator){
		int[] hashesB = estimator.getHashes();
		float m = 0;
		for(int ii = 0; ii < hashesB.length; ii++){
			if(hashesB[ii] == hashes[ii])
				m++;
		}
		float r = m/K;
		return (1-r)/(1+r) * (estimator.getSize()+this.size);
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
		Iterator<String> it = keys.iterator();
		while(it.hasNext()){
			int nextHash = HashFunction.minWiseHash(it.next().hashCode(), k);
			if(nextHash < currentMin)
				currentMin = nextHash;
		}
		return currentMin;
	}

}
