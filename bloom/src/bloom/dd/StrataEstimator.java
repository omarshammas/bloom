package bloom.dd;

import java.util.ArrayList;
import java.util.Random;

import bloom.filters.InvertibleBloomFilter;

public class StrataEstimator {

	static int HASH_COUNT = 3;
	static int STRATA = 10;
	
	private InvertibleBloomFilter[] estimator;
	private int size;
	private int hashCount;
	private int strata;
	
	public StrataEstimator(int size, String[] files){
		this(size, HASH_COUNT, STRATA, files);
	}
		
	public StrataEstimator(int size, int hashCount, int strata, String[] files){
		this.size = size;
		this.hashCount = hashCount;
		this.strata = strata;
		estimator = new InvertibleBloomFilter[strata];
		for(int i = 0; i < strata ;i++){
			estimator[i] = new InvertibleBloomFilter(hashCount, size);
			ArrayList<String> sampledFiles = addFiles(files, 1.0/Math.pow(2,i+1));
			for (String file : sampledFiles){
			    estimator[i].insert(file);
			}
		}
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
	
	/**
	 * Given an array of file names it returns a list of file names sampled with the specified probability
	 * @param files
	 * @param prob
	 * @return sampledFiles
	 */
	private ArrayList<String> addFiles(String[] files, double prob){
		ArrayList<String>sampledFiles = new  ArrayList<String>();
		double r;
		for (int i=0; i < files.length; i++){
			r = new Random().nextDouble();			
			if (r <= prob){
				sampledFiles.add(files[i]);
			}
		}
		return sampledFiles;
	}
	
	
	/**
	 * Given 2 StrataEstimators it will compute the set difference will estimated.
	 * @param se1
	 * @param se2
	 * @return estimated difference size
	 * @throws Exception 
	 */
	public static int estimateDifference(StrataEstimator se1, StrataEstimator se2) throws Exception{
		
		assert se1.getSize() == se2.getSize(); //TODO add exception instead of assertion
		assert se1.getStrata() == se2.getStrata();
		int strata = se1.getStrata();

		InvertibleBloomFilter ibf;
		ArrayList<String> difference;
		int d = 0;
		boolean decoded = false;
		for(int i=0; i < strata; i++){
			
			System.out.print(i);
			ibf = InvertibleBloomFilter.subtract(se1.getIBF(i) , se2.getIBF(i));
			
			try {
				difference = ibf.getDifference();
				System.out.println(" - decoded");
			} catch (Exception e) {
				System.out.println(" - unable to decode");
				continue;
			}
			
			if (decoded)
				d += difference.size();
			else
				d = (int) (Math.pow(2, i)*difference.size());
			
			decoded = true;
			System.out.println(i + " " + difference.size() + " " + d );
		}		
		
		if (!decoded)
			throw new Exception("Unable to estimate the difference");
		
		return d;
	}
}
