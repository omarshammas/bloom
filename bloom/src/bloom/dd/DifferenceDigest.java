package bloom.dd;

import java.util.HashSet;
import java.util.Set;

import bloom.filters.InvertibleBloomFilter;

public class DifferenceDigest {
	//Enum of estimator type
	public enum EstimatorType {
		STRATA,
		MINWISE,
		HYBRID
	}
	//Constants
	public static final EstimatorType ESTIMATOR_TYPE = EstimatorType.HYBRID;
	public static final int HASH_COUNT = 4;
	
	// Members
	private Estimator estimator;
	private Set<String> keys;
	private EstimatorType estimatorType;

	/**
	 * Default Constructor
	 */
	public DifferenceDigest(){
		this(new HashSet<String>(),ESTIMATOR_TYPE);
	}
	
	/**
	 * Constructor initializes Difference Digest with a set of keys
	 *  
	 * @param keys	Set of keys to initialize Difference Digest with
	 */
	public DifferenceDigest(Set<String> keys){
		this(keys, ESTIMATOR_TYPE);
	}
	
	/**
	 * Constructor initializes Difference Digest with a set of keys
	 *  
	 * @param keys	Set of keys to initialize Difference Digest with
	 */
	public DifferenceDigest(Set<String> keys, EstimatorType type){
		this.keys = new HashSet<String>(keys);
		switch(type){
			case STRATA:
				//TODO: Modify constructor for new strata variable
				estimator = new StrataEstimator(7, keys);
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
		InvertibleBloomFilter ibf = new InvertibleBloomFilter(HASH_COUNT,d,keys);
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
		InvertibleBloomFilter ibfA = new InvertibleBloomFilter(HASH_COUNT,ibfB.getSize(),keys);
		
		InvertibleBloomFilter diff = InvertibleBloomFilter.subtract(ibfB, ibfA);
		
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
