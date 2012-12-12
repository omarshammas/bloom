package bloom;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import bloom.dd.StrataEstimator;
import bloom.filters.InvertibleBloomFilter;
import bloom.hash.HashFunction;

public class Main {

	public static Set<String> createFileNames(int number){
		return createFileNames(number, new HashSet<String>(number));
	}
	
	public static Set<String> createFileNames(int additional, Set<String> files){
		int start = files.size();
		while (files.size() < (start+additional) ){
			files.add( UUID.randomUUID().toString().substring(0,10) );	//TODO allow for generic size, not just 10
		}	
		return files;
	}
	
	
	public static void ibfTuning(int num_hash_functions, int num_cells){
		int trials = 100;
		int[] keys_sizes = {100,1000,10000,100000,1000000};
		int[] difference_sizes = {0,5,10,15,20,25,30,35,40,45,50};
		
		Set<String> files_base, files_different;
		InvertibleBloomFilter ibf1, ibf2, ibf_sub;
		ArrayList<String> difference;
		int realDiff, calcDiff;
		float success, success_total;
		
		for(int i=0; i < keys_sizes.length; ++i){			
			for(int j=0; j < difference_sizes.length; ++j){
				
				success_total = 0;
				for (int t=0; t < trials; ++t){
					
					files_base = createFileNames(keys_sizes[i]);
					files_different = new HashSet<String>(files_base);
					ibf1 = new InvertibleBloomFilter(num_hash_functions, num_cells, files_base);
					
					files_different = new HashSet<String>(files_base);
					files_different = createFileNames(difference_sizes[j], files_different);
					ibf2 = new InvertibleBloomFilter(num_hash_functions, num_cells, files_different);
					
					ibf_sub = InvertibleBloomFilter.subtract(ibf1, ibf2);
	
					difference = ibf_sub.getDifference();
					realDiff = files_different.size() - files_base.size();
					calcDiff = difference.size();
					success = 1 - Math.abs(realDiff-calcDiff)/(float)realDiff;
					//System.out.println("Decoded a set of "+keys_sizes[i]+" with a difference of "+realDiff+". Estimate "+calcDiff + ". % Success "+success);
					//System.out.println(success);
					if(success == Double.NaN)
						success = 1;
					success_total += success;
				}
				System.out.println(success_total/trials);
			}
			
			System.out.println("---------------------------");
		}	
		
	}
	
    public static void main(String[] args) {

    	
    	ibfTuning(4,50);
    	
    	
    	
//    	
//    	
//    	String name = "Tunde";
//    	System.out.println(name);
//    	int[] hashes = HashFunction.hash(name, 4, 100);
//    	for(int ii=0; ii < hashes.length; ii++){
//    		System.out.println(hashes[ii]);
//    	}
//    	name = "Omar";
//    	System.out.println(name);
//    	hashes = HashFunction.hash(name, 4, 100);
//    	for(int ii=0; ii < hashes.length; ii++){
//    		System.out.println(hashes[ii]);
//    	}
//    	name = "Shashank";
//    	System.out.println(name);
//    	hashes = HashFunction.hash(name, 4, 100);
//    	for(int ii=0; ii < hashes.length; ii++){
//    		System.out.println(hashes[ii]);
//    	}
//    	
//    	int size = 100;
//    	ArrayList<String> files = new ArrayList<String>();
//    	files.add("Omar Shamm");
//    	files.add("Tunde Agbo");
//    	files.add("Sunkavalli");
//    	
//    	StrataEstimator s1 = new StrataEstimator(size, files);
//    	StrataEstimator s2 = new StrataEstimator(size, files);
//   
//    	int difference = -1;
//    	try {
//    		 difference = StrataEstimator.estimateDifference(s1, s2);
//    	} catch (Exception e){
//    		System.out.println(e.getMessage());
//    	}
//    	
//    	System.out.println(difference);
    	
    }
}
