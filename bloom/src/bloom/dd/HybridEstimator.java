package bloom.dd;

import java.util.Set;

public class HybridEstimator implements Estimator{
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
	 *  Constructor for specifying the strata
	 * @param strata A value for configuring the number of strata for the StartaEstimator
	 */
	public HybridEstimator(int strata){
		size = 0;
		strataEst = new StrataEstimator(strata);
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
	 * Constructor that takes in a set of keys
	 * @param keys	A set of keys to be added
	 * @param strata A value for configuring the number of strata for the StartaEstimator
	 */
	public HybridEstimator(int strata, Set<String> keys) {
		size = keys.size();
		strataEst = new StrataEstimator(strata, keys);
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
		
		strataEst.insert(keys);
		minWiseEst.insert(keys);
	}
	
	public int estimateDifference(Estimator estimator) throws Exception{
		if(!(estimator instanceof HybridEstimator))
			throw new Exception("Estimator type mismatch!"); 
		//TODO add code to return stuff
		return 0;
	}	
}
