package bloom.dd;

import java.util.HashSet;
import java.util.Set;

import bloom.filters.InvertibleBloomFilter;
import bloom.hash.Hash;
import bloom.hash.HashPseudoRandom;

public class DifferenceDigest {
	//Enumeration of EstimatorType
	public enum EstimatorType {
		STRATA,
		MINWISE,
		HYBRID
	}
	//Constants
	public static final EstimatorType ESTIMATOR_TYPE = EstimatorType.HYBRID;
	public static final int HASH_COUNT = 4;
	public static final double ALPHA = 1.5;
	// Members
	private Estimator estimator;
	private Set<String> keys;
	private EstimatorType estimatorType;
	private Hash hash;
	
	/**
	 * Default Constructor
	 */
	public DifferenceDigest(Hash hash){
		this(new HashSet<String>(),ESTIMATOR_TYPE, hash);
	}
	
	/**
	 * Constructor initializes Difference Digest with a set of keys
	 *  
	 * @param keys	Set of keys to initialize Difference Digest with
	 */
	public DifferenceDigest(Set<String> keys, Hash hash){
		this(keys, ESTIMATOR_TYPE, hash);
	}
	
	/**
	 * Constructor initializes Difference Digest with a set of keys
	 *  
	 * @param keys	Set of keys to initialize Difference Digest with
	 */
	public DifferenceDigest(Set<String> keys, EstimatorType type, Hash hash){
		this.keys = new HashSet<String>(keys);
		this.hash = hash;
		switch(type){
			case STRATA:
				//TODO: Modify constructor for new strata variable
				estimator = new StrataEstimator(StrataEstimator.STRATA, keys, StrataEstimator.IBFSIZE, StrataEstimator.HASH_COUNT, this.hash);
				break;
			case MINWISE:
				estimator = new MinWiseEstimator(keys);
				break;
			default:
				estimator = new MinWiseEstimator(keys);
				break;
		}
	}
	
	/***
	 * Adds the input key to the Difference Digest
	 * 
	 * @param keys Key to be added to the Difference Digest
	 */
	public void addKey(String key){
		this.keys.add(key);
		this.estimator.insert(key);
	}
	
	/***
	 * Adds keys to the Difference Digest
	 * 
	 * @param keys Set of keys to be added to the Difference Digest
	 */
	public void addKeys(Set<String> keys){
		this.keys.addAll(keys);
		this.estimator.insert(keys);
	}
	
	/**
	 * Creates an Invertible Bloom Filter of the size of the estimated set
	 * difference calculated from the input Estimator
	 * 
	 * @param estimator Estimator is used to calculate the set difference size
	 * @return			Invertible Bloom Filter representing current set of keys
	 * @throws Exception 
	 */
	public InvertibleBloomFilter getDifferenceIBF(Estimator estimator) throws Exception{
		int d = this.estimator.estimateDifference(estimator);
		InvertibleBloomFilter ibf = new InvertibleBloomFilter(HASH_COUNT, (int) (d*ALPHA), new HashPseudoRandom(), this.keys);
		return ibf;
	}
	
	/**
	 * Calculates SetB - SetA by subtracting ibfB from bloom filter
	 * representing the current set of keys
	 * 
	 * @param ibfB 	Invertible Bloom Filter used to get the set difference
	 * @return 		Returns a Set of string values representing SetB - SetA
	 */
	public Set<String> getSetDifference(InvertibleBloomFilter ibfB){
		InvertibleBloomFilter ibfA = new InvertibleBloomFilter(ibfB.getHashCount(),ibfB.getSize(),ibfB.getHash(),keys);
		
		InvertibleBloomFilter diff=null;
		try {
			diff = ibfA.subtract(ibfB);
		} catch (Exception e) {
			//Should not throw an error, because we are creating an IBF based on the parameters of ibfB which we are subtracting
			e.printStackTrace();
		}
		
		return diff.getPureKeys();
	}
	
	// Setters and Getters
	public Estimator getEstimator() {
		return estimator;
	}

	public void setEstimator(Estimator estimator) {
		this.estimator = estimator;
	}
	
	public EstimatorType getEstimatorType() {
		return estimatorType;
	}

	public void setEstimatorType(EstimatorType estimatorType) {
		this.estimatorType = estimatorType;
	}
	
}
