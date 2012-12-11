import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import org.junit.Test;
import bloom.dd.MinWiseEstimator;


public class MinWiseEstimatorTest {

	@Test
	public void testEstimatorExhaustively(){
		int size = 100000;
		System.out.println("Real J | Calc J | Real Diff | Calc Diff | Overhead\n");
		String key;
		Set<String> setA = new HashSet<String>();
		Set<String> setB = new HashSet<String>();
		for (int ii = 0; ii < size; ii++) {
			key = UUID.randomUUID().toString().substring(0, 15);
			setA.add(key);
			setB.add(key);
		}
		MinWiseEstimator estimatorA = new MinWiseEstimator(setA);
		MinWiseEstimator estimatorB = new MinWiseEstimator(setB);
		key = null;
		for(int ii= 0; ii < size; ii+=100){
			for(int jj = 0; jj < 100;jj++){
				key = UUID.randomUUID().toString().substring(0, 15);
				setA.add(key);
				estimatorA.add(key);
			}
			Set<String> union = new HashSet<String>(setA);
			Set<String> inter = new HashSet<String>(setA);
			union.addAll(setB);
			inter.retainAll(setB);
			float jCoeff = ((float)inter.size())/union.size();
			System.out.print(jCoeff);
			float d = estimatorA.difference(estimatorB);
			Set<String> diffA = new HashSet<String>(setA);
			diffA.removeAll(setB);
			System.out.print(" | "+diffA.size());
			System.out.print(" | "+d);
			System.out.print(" | "+(diffA.size()/d)+"\n");
		}
	}
	
}
