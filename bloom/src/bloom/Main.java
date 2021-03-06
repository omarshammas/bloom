package bloom;

import java.util.Set;

import bloom.dd.StrataEstimator;
import bloom.filters.InvertibleBloomFilter;
import bloom.hash.Hash;
import bloom.hash.HashFunction;
import bloom.hash.HashPseudoRandom;
import bloom.utils.Utilities;

public class Main {
	
	
	public static void tuningIBF(int num_hash_functions, int num_cells){
		int trials = 100;
		int[] keys_sizes = {100,1000,10000,100000};
		int[] deltas = {0,5,10,15,20,25,30,35,40,45,50};
		InvertibleBloomFilter ibf1, ibf2, ibf_sub=null;
		Hash hash;
		Set<String> keys1, keys2, keysdiff;
		double success, success_total;
		
		for (int i=0; i < keys_sizes.length; ++i){
			for (int j=0; j < deltas.length; ++j){
				
				success_total = 0;
				for (int t=0; t < trials; ++t){
					keys1 = Utilities.createKeys(keys_sizes[i]);
					keys2 = Utilities.createKeys(deltas[j], keys1);

					hash = new HashFunction();
					ibf1 = new InvertibleBloomFilter(num_hash_functions, num_cells, hash, keys1);
					ibf2 = new InvertibleBloomFilter(num_hash_functions, num_cells, hash, keys2);
					
					
					try {
						ibf_sub = ibf1.subtract(ibf2);
					} catch (Exception e) {
						e.printStackTrace();
					}
					
					keysdiff = ibf_sub.getPureKeys();
										
					
					if (deltas[j] == 0){
						success =  1 - (double) keysdiff.size();
					} else {
						success = 1 - (double) Math.abs( (double) keysdiff.size() - deltas[j])/(double) deltas[j] ;
					}
					success_total += success;
				}
				
				System.out.print( (success_total/trials) + ", ");
			}
			System.out.println("");
		}
				
	}
	
	//num keys shouldn't matter, as shown by the tuning IBF results, what matter is the difference between sets
	public static void tuningHashCount(int num_keys, int num_cells) {
		int trials = 1000;
		int[] deltas = {0,5,10,15,20,25,30,35,40,45,50};
		int[] hashCount = {2,3,4,5,6};
		Hash hash;
		InvertibleBloomFilter ibf1, ibf2, ibf_sub=null;
		Set<String> keys1, keys2, keysdiff;
		double[][] results = new double[deltas.length][hashCount.length];
		
		for (int i=0; i < deltas.length; ++i)
			for (int j=0; j < hashCount.length; ++j)
				results[i][j] = 0;
		
		for (int i=0; i < deltas.length; i++){	
			System.out.println("Computing Decoding Probability for Delta of " + deltas[i] + " for varying hash counts using " + num_keys + " keys.");
			for (int t=0; t < trials; ++t){			
				keys1 = Utilities.createKeys(num_keys);
				keys2 = Utilities.createKeys(deltas[i], keys1);
				
				for (int j=0; j < hashCount.length; ++j){
					hash = new HashPseudoRandom();
					ibf1 = new InvertibleBloomFilter(hashCount[j], num_cells, hash, keys1);
					ibf2 = new InvertibleBloomFilter(hashCount[j], num_cells, hash, keys2);
					
					try {
						ibf_sub = ibf1.subtract(ibf2);
					} catch (Exception e) {
						e.printStackTrace();
					}
					
					keysdiff = ibf_sub.getPureKeys();
					
					if (deltas[i] == 0)
						results[i][j] += 1 - (double) keysdiff.size(); //TODO set to 1 because we can't divide by 0 when deltas[0];
					else
						results[i][j] += 1 - (double) Math.abs( (double) keysdiff.size() - deltas[i])/(double) deltas[i];
				}
			}
		}
		
		System.out.println("-----Results----");
		for (int i=0; i < deltas.length; ++i){
			//System.out.println(deltas[i]+" Set Difference");
			for (int j=0; j < hashCount.length; ++j){
				results[i][j] /= (double) trials;
				System.out.print(results[i][j] + ", ");
			}
			System.out.println("");
		}
	}
	
	//num keys shouldn't matter, as shown by the tuning IBF results, what matter is the difference between sets
	public static void correctionOverhead(int num_keys, int num_strata, int num_hash_functions){
		int trials = 200;
		int[] deltas = {10, 100, 1000, 10000}; //100000};
		int[] cell_sizes = {20, 30, 40, 50, 60, 70, 80, 90, 100, 110, 120, 130, 140, 150, 160};
		StrataEstimator se1, se2;
		Hash hash;
		Set<String> keys1, keys2;
		double difference = 0;
		double[][] results = new double[deltas.length][cell_sizes.length];
		
		
		for (int i=0; i < deltas.length; ++i)
			for (int j=0; j < cell_sizes.length; ++j)
				results[i][j] = 0;
		
		for (int i=0; i < deltas.length; ++i){
			
			System.out.println("Computing Correction Overhead for Delta of " + deltas[i] + " for Strata Estimators with varying IBF sizes.");
			for (int t=0; t < trials; ++t){
				keys1 = Utilities.createKeys(num_keys);
				keys2 = Utilities.createKeys(deltas[i], keys1);
				
				for (int j=0; j < cell_sizes.length; ++j){
					hash = new HashPseudoRandom();
					se1 = new StrataEstimator(num_strata, keys1, cell_sizes[j], num_hash_functions, hash);
					se2 = new StrataEstimator(num_strata, keys2, cell_sizes[j], num_hash_functions, hash);
					
					difference = 0;
					try {
						difference = se1.estimateDifference(se2);
					} catch (Exception e) {
						System.out.println(deltas[i]+" - "+cell_sizes[j]+" : Strata estimator was unable to deocde");
					}
					
					results[i][j] += (float) deltas[i] / (float) difference;				
				}
			}
		}
		
		System.out.println("-----Results----");
		for (int i=0; i < deltas.length; ++i){
			for (int j=0; j < cell_sizes.length; ++j){
				results[i][j] /= (double) trials;
				System.out.print(results[i][j] + ", ");
			}
			System.out.println("");
		}		
	}
	
	public static void correctionOverheadFixedStrata(int num_keys, int num_strata, int num_hash_functions, int ibfsize){
		int trials = 50, difference;
		int[] deltas = {10, 100, 1000, 10000, 100000};
		double[] results = new double[deltas.length];
		Hash hash;
		Set<String> keys1, keys2;
		StrataEstimator se1, se2;
		
		for (int i=0; i < results.length; ++i){
			results[i] = 0;
		} 
		
		
		for (int i=0; i < deltas.length; ++i){
			for (int t=0; t < trials; ++t){
				System.out.print(t+", ");
				
				keys1 = Utilities.createKeys(num_keys);
				keys2 = Utilities.createKeys(deltas[i], keys1);
				
				hash = new HashPseudoRandom();
				se1 = new StrataEstimator(num_strata, keys1, ibfsize, num_hash_functions, hash);
				se2 = new StrataEstimator(num_strata, keys2, ibfsize, num_hash_functions, hash);
				
				difference = 0;
				try {
					difference = se1.estimateDifference(se2);
				} catch (Exception e) {
					System.out.println(deltas[i]+" - Strata estimator was unable to deocde");
				}
				
				results[i] += (float) deltas[i] / (float) difference;					
			}
			System.out.println("\n");
		}
		
		System.out.println("-----Results----");
		for (int i=0; i < results.length; ++i){
			results[i] /= (double) trials;
			System.out.println(results[i]);
		} 
		
	}
		
	
    public static void main(String[] args) {
    	//tuningIBF(4,50);   	
    	//tuningHashCount(100, 50);
		//correctionOverhead(100, 32, 4);
    	
    	correctionOverheadFixedStrata(100000, 16, 4, 80);
    }
}
