package bloom;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import bloom.dd.StrataEstimator;
import bloom.filters.InvertibleBloomFilter;
import bloom.hash.HashFunction;

public class Main {

	private static Set<String> createFileNames(int number){
		Set<String> files = new HashSet<String>(number);
		
		while (files.size() < number){
			files.add( UUID.randomUUID().toString().substring(0,10) );	
		}
		
		return files;
	}
	
	public static void tuneHashCount(){
		int[] deltas = {0,5,10,15,20,25,30,35,40,45,50};
		int[] hashCount = {2,3,4,5,6};
		int ibfSize = 50;
		int filesCount = 100;
		int trialCount = 1000;
		
		//initialize each of the 
		for(int i = 0; i < deltas.length; i++){
			System.out.println("=================================");
			System.out.println("Delta : "+deltas[i]);
			
			Set<String> baseFiles = new HashSet<String>();
			Set<String> diffFiles1 = new HashSet<String>();
			Set<String> diffFiles2 = new HashSet<String>();
						
			for(int j = 0; j < hashCount.length; j++){
				System.out.println("=================================");
				System.out.println("Hash Count : "+hashCount[j]);
				int totalDifference = 0;
				int failedCount = 0;
				
				for (int k=0; k < trialCount; k++){	
					baseFiles = createFileNames(filesCount-deltas[i]);
					diffFiles1 = createFileNames(deltas[i]);
					diffFiles2 = createFileNames(deltas[i]);
					diffFiles1.addAll(baseFiles);
					diffFiles2.addAll(baseFiles);
					
					InvertibleBloomFilter ibf1 = new InvertibleBloomFilter(hashCount[j], ibfSize);
					InvertibleBloomFilter ibf2 = new InvertibleBloomFilter(hashCount[j], ibfSize);
					
					for (String f : diffFiles1)
						ibf1.insert(f);
					for (String f : diffFiles2)
						ibf2.insert(f);
					
					InvertibleBloomFilter difference = InvertibleBloomFilter.subtract(ibf1, ibf2);
					try {
						totalDifference += difference.getDifference().size();
					} catch (Exception e) {
						failedCount++;
						//System.out.println("Failed to estimate difference");
						// TODO Auto-generated catch block
						//e.printStackTrace();
					}
				}
				System.out.println("Average estimated delta : "+(totalDifference/(trialCount-failedCount)));
				System.out.println("Failed "+failedCount+" times out of "+trialCount);
			}
		}
		
		
	}

    public static void main(String[] args) {

    	String name = "Tunde";
    	System.out.println(name);
    	int[] hashes = HashFunction.hash(name, 4, 100);
    	for(int ii=0; ii < hashes.length; ii++){
    		System.out.println(hashes[ii]);
    	}
    	name = "Omar";
    	System.out.println(name);
    	hashes = HashFunction.hash(name, 4, 100);
    	for(int ii=0; ii < hashes.length; ii++){
    		System.out.println(hashes[ii]);
    	}
    	name = "Shashank";
    	System.out.println(name);
    	hashes = HashFunction.hash(name, 4, 100);
    	for(int ii=0; ii < hashes.length; ii++){
    		System.out.println(hashes[ii]);
    	}
    	
    	int size = 100;
    	ArrayList<String> files = new ArrayList<String>();
    	files.add("Omar Shamm");
    	files.add("Tunde Agbo");
    	files.add("Sunkavalli");
    	
    	StrataEstimator s1 = new StrataEstimator(size, files);
    	StrataEstimator s2 = new StrataEstimator(size, files);
   
    	int difference = -1;
    	try {
    		 difference = StrataEstimator.estimateDifference(s1, s2);
    	} catch (Exception e){
    		System.out.println(e.getMessage());
    	}
    	
    	System.out.println(difference);
    	
    	tuneHashCount();
    	
    }
}
