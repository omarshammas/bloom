package bloom;

import bloom.dd.StrataEstimator;
import bloom.hash.HashFunction;

public class Main {

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
    	String[] files = new String[3];
    	files[0] = "Omar Shamm";
    	files[1] = "Sunkavalli";
    	files[2] = "Tunde Agbo";
    	
    	StrataEstimator s1 = new StrataEstimator(size, files);
    	StrataEstimator s2 = new StrataEstimator(size, files);
   
    	int difference = -1;
    	try {
    		 difference = StrataEstimator.estimateDifference(s1, s2);
    	} catch (Exception e){
    		System.out.println(e.getMessage());
    	}
    	
    	System.out.println(difference);
    	
    }
}
