package bloom;

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
    	
    }
}
