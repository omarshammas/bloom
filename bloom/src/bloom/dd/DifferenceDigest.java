package bloom.dd;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import bloom.filters.InvertibleBloomFilter;

public class DifferenceDigest {
	// Members
	private HybridEstimator estimator;
	private InvertibleBloomFilter filter;
	private Set<String> keys;
	
	// Setters and Getters
	public HybridEstimator getEstimator() {
		return estimator;
	}

	public void setEstimator(HybridEstimator estimator) {
		this.estimator = estimator;
	}

	public InvertibleBloomFilter getFilter() {
		return filter;
	}

	public void setFilter(InvertibleBloomFilter filter) {
		this.filter = filter;
	}
	
	/**
	 * Default Constructor
	 */
	public DifferenceDigest(){
		this.keys = new HashSet<String>();
		estimator = new HybridEstimator();
	}
	
	/**
	 * Constructor initializes Difference Digest with a set of keys
	 *  
	 * @param keys	Set of keys to initialize Difference Digest with
	 */
	public DifferenceDigest(Set<String> keys){
		this.keys = new HashSet<String>(keys);
		estimator = new HybridEstimator(keys);
	}
	
	/***
	 * Adds the input key to the Difference Digest
	 * 
	 * @param keys Key to be added to the Difference Digest
	 */
	public void addKey(String key){
		this.keys.add(key);
		//TODO: Update other members
	}
	
	/***
	 * Adds keys to the Difference Digest
	 * 
	 * @param keys Set of keys to be added to the Difference Digest
	 */
	public void addKeys(Set<String> keys){
		this.keys.addAll(keys);
		//TODO: Update other members
	}
	
	/**
	 * Creates an Invertible Bloom Filter the size of the estimated set
	 * difference calculate from the input HybridEstimator
	 * 
	 * @param estimator HybridEstimator used to calculate the set difference size
	 * @return			Invertible Bloom Filter representing current set of keys
	 */
	public InvertibleBloomFilter getDifferenceIBF(HybridEstimator estimator){
		int d = this.estimator.estimateDifference(estimator);	
		InvertibleBloomFilter ibf = new InvertibleBloomFilter(4,d);
		for(String key: keys){
			ibf.insert(key);
		}
		return ibf;
	}
	
	/**
	 * Calculates SetB - SetA by subracting ibfB from bloom filter
	 * representing the current set of keys
	 * 
	 * @param ibfB 	Invertible Bloom Filter used to get the set difference
	 * @return 		Returns an ArrayList of string values representing SetB - SetA
	 */
	public ArrayList<String> getDifference(InvertibleBloomFilter ibfB){
		//TODO: Determine if we want to make filter on demand or store it
		InvertibleBloomFilter ibfA = new InvertibleBloomFilter(4,ibfB.getSize());
		for(String key: keys)
			ibfA.insert(key);
		InvertibleBloomFilter diff = InvertibleBloomFilter.subtract(ibfB, ibfA);
		try {
			return diff.getDifference();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
}
