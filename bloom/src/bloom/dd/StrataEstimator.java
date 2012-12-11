package bloom.dd;

import java.util.ArrayList;
import java.util.Collection;

import bloom.filters.InvertibleBloomFilter;
import bloom.hash.HashFunction;

public class StrataEstimator {

	public static int HASH_COUNT = 3;
	public static int STRATA = 32;
	public static int CUTOFF = 10;
	
	private InvertibleBloomFilter[] estimator;
	private int size;
	private int hashCount;
	private int strata;
	private int[] distro;
	
	public StrataEstimator(int size, Collection<String> files){
		this(size, HASH_COUNT, STRATA, files);
	}
		
	public StrataEstimator(int size, int hashCount, int strata, Collection<String> files){
		this.size = size;
		this.hashCount = hashCount;
		this.strata = strata;
		
		//TODO remove, used only for debugging
		this.distro = new int[STRATA];
		for (int i=0; i<STRATA; ++i)
			this.distro[i] = 0;
			
		estimator = new InvertibleBloomFilter[strata];
		for(int i=0; i < strata; ++i){
			estimator[i] = new InvertibleBloomFilter(hashCount, size);
		}
				
		for (String file : files){
			insert(file);
		}
	}
	
	private int getNumberOfTrailingZeros(String file){
		int hashCode =  (int) (HashFunction.fnv1Hash(file)%Math.pow(2,STRATA));
		int counter = STRATA-1;
		while (hashCode > 0){
			hashCode = hashCode/2;
			counter--;
		}
		return counter;
	}
	
	public int getSize(){
		return size;
	}
	
	public int getStrata(){
		return strata;
	}
	
	public int getHashCount(){
		return hashCount;
	}
	
	public InvertibleBloomFilter getIBF(int index){
		assert index <= strata; //TODO throw exception
		return estimator[index];
	}

	public void insert(String key){
		int trailingZeros = getNumberOfTrailingZeros(key);
		estimator[trailingZeros].insert(key);
		this.distro[trailingZeros] += 1; //TODO remove
	}
	
	public void remove(String key){
		int trailingZeros = getNumberOfTrailingZeros(key);
		estimator[trailingZeros].remove(key);
		this.distro[trailingZeros] -= 1; //TODO remove
	}
	
	//TODO remove distro
	public void printDistro(){
		for (int i=0; i<STRATA; ++i)
			System.out.print(this.distro[i]+", ");
		System.out.println("");
	}
	
	
	/**
	 * Given 2 StrataEstimators it will compute the set difference will estimated.
	 * @param se1
	 * @param se2
	 * @return estimated difference size
	 * @throws Exception 
	 */
	public static int estimateDifference(StrataEstimator se1, StrataEstimator se2) throws Exception{
		assert isEquivalent(se1, se2);
		
		ArrayList<String> files;
		InvertibleBloomFilter ibf;
		int difference = 0;
		boolean decoded = false;
		for (int i=STRATA-1; i >= 0; --i){
			ibf = InvertibleBloomFilter.subtract(se1.getIBF(i), se2.getIBF(i));
			try {
				files = ibf.getDifference();
			} catch (Exception e) {
				System.out.println(i+" th bloom filter was not decoded.");
				System.out.println("Difference: "+difference+", multiply by "+Math.pow(2, i+1));
				difference *= Math.pow(2, i+1);
				break;
			}
			
			decoded = true;
			difference += files.size();
		}
		
		if (!decoded)
			throw new Exception("Unable to deocde");
			
		return difference;
	}
	
	
	public static boolean isEquivalent(StrataEstimator se1, StrataEstimator se2){
		return se1.getSize() == se2.getSize() && se1.getStrata() == se2.getStrata();
	}
}
