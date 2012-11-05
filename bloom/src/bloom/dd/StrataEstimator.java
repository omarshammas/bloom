package bloom.dd;

import java.util.ArrayList;
import java.util.Random;

import bloom.filters.InvertibleBloomFilter;

public class StrataEstimator {

	private InvertibleBloomFilter[] estimator;
	private int size;
	private int hashCount;
	private int strata;
	
	public StrataEstimator(int size, int hashCount, int strata, String[] files){
		this.size = size;
		this.hashCount = hashCount;
		this.strata = strata;
		estimator = new InvertibleBloomFilter[strata];
		for(int i = 0; i < strata ;i++){
			estimator[i] = new InvertibleBloomFilter(size, hashCount);
			ArrayList<String> sampledFiles = addFiles(files, 1.0/(Math.pow(2,(i+1))));
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
	 */
	public static int estimateDifference(StrataEstimator se1, StrataEstimator se2){
		
		assert se1.getSize() == se2.getSize(); //TODO add exception instead of assertion
		assert se1.getStrata() == se2.getStrata();
		int strata = se1.getStrata();
		
		ArrayList<String>[] difference = (ArrayList<String>[])new ArrayList[2];
		int d1_2, d2_1;
		for(int i=0; i < strata; i++){
			
			difference = InvertibleBloomFilter.subtract(se1.getIBF(i) , se2.getIBF(i));
			d1_2 = difference[0].size();
			d2_1 = difference[1].size();
			//TODO it will not always decode.
			//if unable to decode
			// continue
			return (2^(strata+1))*(d1_2+d1_2);
		}			
		
		//Unable to determine the difference
		return -1;
	}
}
