package bloom;

import bloom.hash.HashFunction;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {

    	String name = "Tunde";
    	int hash = HashFunction.MurmurHash(name.getBytes(), 0);
    	System.out.println(hash);
    	ArrayList<String> a = new ArrayList<String>();
    	a.add("tunde");
    	System.out.println(a.hashCode());
    	
    }
}
