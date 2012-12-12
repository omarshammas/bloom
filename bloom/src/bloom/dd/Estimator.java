package bloom.dd;

import java.util.Set;

public interface Estimator {
	
	public int getSize();
	
	public void addKey();
	
	public void addKeys(Set<String> keys);
	
	public int estimateDifference(Estimator estimator);

}
