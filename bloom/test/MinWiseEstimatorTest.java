
public class MinWiseEstimatorTest {

	
//	@Test
//	public void test{
////		// Test and make sure it is uniform between 1 and m
////		int m = 100;
////		// Initialize array of buckets
////		int[] buckets = new int[m];
////		for (int ii = 0; ii < m; ii++)
////			buckets[ii] = 0;
////		// Fill in array with a running count of hash locations
////		int index;
////		for (int ii = 0; ii < 1000000; ii++){
////			index = HashFunction.hash(UUID.randomUUID().toString().substring(0, 15), m);
////			buckets[index] += 1;
////		}
////		System.out.print("[");
////		for (int ii = 0; ii < m; ii++)
////			System.out.print(buckets[ii]+", ");
////		System.out.print("]");
//
////		int size = 300000;
////		int diff = 500;
////		while (diff < 150000) {
////			diff = diff*2;
////			String key;
////			Set<String> setA = new HashSet<String>();
////			Set<String> setB = new HashSet<String>();
////			while (setA.size() < size) {
////				key = UUID.randomUUID().toString().substring(0, 15);
////				setA.add(key);
////				if (setA.size() < diff)
////					setB.add(key + "diff");
////				else if(setA.size() < (size - 1000))
////					setB.add(key);
////			}
////			System.out.print("SetA size: " + setA.size());
////			System.out.print(" SetB size: " + setB.size());
////			//System.out.print(" Real difference: " + diff);
////			MinWiseEstimator estimatorA = new MinWiseEstimator(setA);
////			MinWiseEstimator estimatorB = new MinWiseEstimator(setB);
////			float calcDiff = estimatorA.difference(estimatorB);
////			//System.out.print(" Calculated Difference: " + calcDiff);
////			//System.out.print(" Error: " + Math.abs(calcDiff-diff)+"\n");
////		}
//	}
}
