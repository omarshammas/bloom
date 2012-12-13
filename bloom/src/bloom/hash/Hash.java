package bloom.hash;

public interface Hash {

	int[] hash(String key,int k, int m);

	void print();
	
	int getHashCode(String key);

}