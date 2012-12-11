package bloom.dd;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import bloom.hash.HashFunction;


public class MinWiseEstimator {
	
	private static final int K = 400;

	private int[] hashes; 
	private Set<String> keys;
	
	public int[] getHashes() {
		return hashes;
	}

	public void setHashes(int[] hashes) {
		this.hashes = hashes;
	}

	public Set<String> getKeys() {
		return keys;
	}

	public void setKeys(Set<String> keys) {
		this.keys = keys;
	}
	
	public MinWiseEstimator(){
		hashes = new int[K];
	}
	
	public MinWiseEstimator(Set<String> keys){
		hashes = new int[K];
		this.keys = new HashSet<String>(keys);
		for(int kk= 0; kk < hashes.length; kk++){
			int minValue = Integer.MAX_VALUE;
			minValue = getMinHashFromSet(kk, minValue);
			hashes[kk] = minValue;
		}
	}
	
//	public void add(String key){
//		keys.add(key);
//		for(int kk= 0; kk < hashes.length; kk++){
//			int newHash = HashFunction.minWiseHash(key.hashCode(), kk);
//			if(hashes[kk] > newHash)
//				hashes[kk] = newHash;
//		}
//	}
//	
//	public void addKeys (Set<String> keys){
//		this.keys.addAll(keys);
//		for(int kk= 0; kk < hashes.length; kk++){
//			int minValue = hashes[kk];
//			hashes[kk] = getMinHashFromSet(kk, minValue);
//		}
//	}

	private int getMinHashFromSet(int k, int currentMin) {
		Iterator<String> it = this.keys.iterator();
		while(it.hasNext()){
			int nextHash = HashFunction.minWiseHash(it.next().hashCode(), k);
			if(nextHash < currentMin)
				currentMin = nextHash;
		}
		return currentMin;
	}
	
	public float difference(MinWiseEstimator estimator){
		Set<String> keysB = estimator.getKeys();
		int[] hashesB = estimator.getHashes();
		float m = 0;
		for(int ii = 0; ii < hashesB.length; ii++){
			if(hashesB[ii] == hashes[ii])
				m++;
		}
		float r = m/K;
		Set<String> union = new HashSet<String>(keys);
		Set<String> inter = new HashSet<String>(keys);
		union.addAll(keysB);
		inter.retainAll(keysB);
		float jCoeff = ((float)inter.size())/union.size();
		System.out.print(" Real J:"+jCoeff);
		System.out.print(" Calculated J:"+r+"\n");
		return (1-r)/(1+r) * keys.size();
		//return (((float)(1-r)/(1+r))*(keys.size() + keysB.size())); 
	}

}
