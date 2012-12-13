package bloom.utils;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Utilities {
	//Constants
	public static final int KEY_LENGTH = 4;
	
	public static String createKey(){
		return createKey(KEY_LENGTH);
	}
	
	public static String createKey(int keyLength){
		return UUID.randomUUID().toString().substring(0,keyLength);
	}
	
	public static Set<String> createKeys(int number){
		return createKeys(number, new HashSet<String>(number));
	}

	public static Set<String> createKeys(int number, int keyLength){
		return createKeys(number, new HashSet<String>(number), keyLength);
	}
	
	public static Set<String> createKeys(int additional, Set<String> keys){
		return createKeys(additional, keys, KEY_LENGTH);
	}
	
	public static Set<String> createKeys(int additional, Set<String> keys, int keyLength){
		Set<String> new_keys = new HashSet<String>(keys);
		int start = new_keys.size();
		while (new_keys.size() < (start+additional) ){
			new_keys.add( createKey(keyLength) );
		}	
		return new_keys;
	}

}
