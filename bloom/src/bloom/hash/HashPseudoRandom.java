package bloom.hash;

import java.util.Hashtable;
import java.util.Random;

public class HashPseudoRandom implements Hash {
	
	private Hashtable<String, int[]> keys;
	private Hashtable<String, Integer> strata;
	
	public HashPseudoRandom(){
		this.keys = new Hashtable<String, int[]>();
		this.strata = new Hashtable<String, Integer>();
	}

	public int[] hash(String key, int k, int m) {

		if (!this.keys.containsKey(key)){

			int[] locations = new int[k];
			int i = 0, random_location;
			Random rand = new Random();

			while (i < k){
				random_location = rand.nextInt(m); 
				if ( notIncluded(locations, random_location) )
					locations[i++] = random_location;
			}
			this.keys.put(key, locations);
		}

		return this.keys.get(key);
	}

	public int getHashCode(String key){
		if (!this.strata.containsKey(key)){
			Random rand = new Random();
			this.strata.put(key, rand.nextInt((int) Math.pow(2, 32)));
		}
		return this.strata.get(key);
	}
	
	private boolean notIncluded(int[] locations, int random_location){
		for(int location : locations){
			if (location == random_location)
				return false;
		}
		return true;
	}

	public void print(){
		System.out.println("------PseudoRandomHash-------");
		System.out.println("Contains "+this.keys.size()+" items.");
		for (String key : this.keys.keySet()) {
		    System.out.print(key+" - ");
		    int[] locations = this.keys.get(key);
		    for (int i=0; i < locations.length; ++i)
		    	System.out.print(locations[i]+", ");
		    System.out.println("\n");		
		}
	}

}
