package bloom.dd;

import java.util.Set;

public class HybridEstimator {
	//Constants
	public static int STRATA = 10;
	//Private Members
	private int size;
	private StrataEstimator strataEst;
	private MinWiseEstimator minWiseEst;
	//Getters
	public int getSize() {
		return size;
	}
	
	/**
	 *  Default Constructor
	 */
	public HybridEstimator(){
		size = 0;
		strataEst = new StrataEstimator(STRATA);
		minWiseEst = new MinWiseEstimator();
	}
	
	/**
	 * Constructor that takes in a set of keys
	 * @param keys	A set of keys to be added
	 */
	public HybridEstimator(Set<String> keys){
		size = keys.size();
		strataEst = new StrataEstimator(STRATA, keys);
		minWiseEst = new MinWiseEstimator(keys);
	}
	
	/**
	 * Inserts a key into the estimator
	 * 
	 * @param key	A key to be inserted
	 */
	public void insert(String key){
		size++;
		strataEst.insert(key);
		minWiseEst.insert(key);
	}
	
	/**
	 * Inserts a set of keys into the estimator
	 * 
	 * @param keys	A set of keys to be added
	 */
	public void insert(Set<String> keys){
		size += keys.size();
		//TODO: Insert a set of keys into the strata estimator
		minWiseEst.insert(keys);
	}
	
	/**
	 * 
	 * @param estimator
	 * @return
	 */
	public int estimateDifference(HybridEstimator estimator){
		//TODO: Implement this function
		return 0;
	}
	

}
