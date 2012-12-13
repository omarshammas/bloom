package bloom.dd;

import java.util.Set;

public interface Estimator {
	
	public int getSize();
	
	public void insert(String key);
	
	public void insert(Set<String> keys);
	
	public int estimateDifference(Estimator estimator) throws Exception;

}
