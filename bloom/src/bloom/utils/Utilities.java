package bloom.utils;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

public class Utilities {
	//Constants
	public static final int KEY_LENGTH = 4;
	public static final String characters = "abcdefghijklmnopqrstuvwxyz0123456789";
	
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
		int start = keys.size();
		while (keys.size() < (start+additional) ){
			keys.add( createKey(keyLength) );
		}	
		return keys;
	}
	
	public static String generateKey(){
		char[] text = new char[KEY_LENGTH];
		Random random = new Random();
		
	    for (int i = 0; i < KEY_LENGTH; i++){
	        text[i] = characters.charAt(random.nextInt(characters.length()));
	    }
	    return new String(text);
	}

	public static Set<String> generateKeys(int number){
		Set<String> keys = new HashSet<String>();
		String key = "";
		while(keys.size() < number){
			key = generateKey();
			if(!keys.contains(key)){
				keys.add(key);
			}
		}
		return keys;
	}
}
