package bloom.dd;

import java.util.Set;

public class HybridEstimator {
	
	private StrataEstimator strataEst;
	private MinWiseEstimator minWiseEst;
	
	public HybridEstimator(){
		//strataEst = new StrataEstimator();
		minWiseEst = new MinWiseEstimator();
	}
	
	public HybridEstimator(Set<String> keys){
		//strataEst = new StrataEstimator(keys);
		minWiseEst = new MinWiseEstimator(keys);
	}
	
	
	

}
