package bloom.dd;

import java.util.Set;

public class HybridEstimator {
	
	private StrataEstimator strataEst;
	private MinWiseEstimator minWiseEst;
	
	public HybridEstimator(){
		//TODO: Implement constuctor
		//strataEst = new StrataEstimator();
		minWiseEst = new MinWiseEstimator();
	}
	
	public HybridEstimator(Set<String> keys){
		//TODO: Implement Constructor
		//strataEst = new StrataEstimator(keys);
		minWiseEst = new MinWiseEstimator(keys);
	}
	
	public int estimateDifference(HybridEstimator estimator){
		//TODO: Implement this function
		return 0;
	}
	

}
