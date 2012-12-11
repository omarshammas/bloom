package bloom.dd;

import java.util.HashSet;
import java.util.Set;

import bloom.filters.InvertibleBloomFilter;

public class DifferenceDigest {
	
	private HybridEstimator estimator;
	private InvertibleBloomFilter filter;
	
	public DifferenceDigest(){
		estimator = new HybridEstimator();
	}
	
	public DifferenceDigest(Set<String> keys){
		estimator = new HybridEstimator(keys);
	}
	
	public boolean add(String key){
		return false;
	}
	
	public boolean remove(String key){
		return false;
	}
	
}
