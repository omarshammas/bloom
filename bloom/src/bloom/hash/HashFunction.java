package bloom.hash;

import java.math.BigInteger;
import java.util.Random;

public class HashFunction {
	//Static constant members
	private static final BigInteger FNV_PRIME =  new BigInteger("16777619");
	private static final BigInteger FNV_OFFSET_BASIS = new BigInteger("2166136261");
	private static final BigInteger MOD = new BigInteger("4294967296");
	private static final int P = 2147483647;
	
	/**
	 * Computes a single hash value mod m using
	 * the MurmmurHash algorithm
	 * 
	 * @param key	Key in which the hash will be performed on
	 * @param m		Modulus value
	 * @return		Hash value [0, m-1]
	 */
	public static int murmurHash(String key, int m){
		return (int)(murmurHash(key) % m);
	}
	
	/**
	 * Computes a single hash value mod m using
	 * the Fnv1 hash algorithm
	 * 
	 * @param key	Key in which the hash will be performed on
	 * @param m		Modulus value
	 * @return		Hash value [0, m-1]
	 */
	public static int fnv1Hash(String key, int m){
		return (int)(fnv1Hash(key) % m);
	}
	
	/**
	 * Returns an array of k hashes by computing linear
	 * combinations of the fnv1 hash with the MurmurHash
	 * 
	 * @param key	Key in which the hash will be performed on
	 * @param k		Value specifying how many hash values to compute
	 * @param m		Modulus value
	 * @return		An array of size K with hash values [0, m-1]
	 */
	public static int[] hash(String key,int k, int m){
		int[] hashes = new int[k];
		for(int ii = 0; ii < k; ii++){
			long h1 = fnv1Hash(key);
			long h2 = murmurHash(key);
			long axb = h1+(ii*h2);
			hashes[ii] = (int)(axb % m);
		}
		return hashes;
	}
	
	/**
	 * Returns a single min wise independent hash value
	 * using a linear transformation
	 * 
	 * @param hashCode	Hash code of key
	 * @param k 		Seed
	 * @return			Hash value [0, P-1]
	 */
	public static int minWiseHash(int hashCode, int k){
		Random random = new Random(k);
		long a = random.nextInt(Integer.MAX_VALUE-1)+1;
		long b = random.nextInt(Integer.MAX_VALUE);
		long axb = a * hashCode + b;
		return (int)(((axb % P)+P)%P);
	}
	
	
	/**
	 * Returns a single hash value using the fnv1 hash
	 * 
	 * @param string	Key
	 * @return			Hash value long type [0,2^63]
	 */
	private static long fnv1Hash(String string){
		byte[] data = string.getBytes();
		BigInteger hash = FNV_OFFSET_BASIS;
		for(int ii = 0; ii < data.length; ii++){
			int intOctet = data[ii] & 0xff;
			hash = hash.multiply(FNV_PRIME).mod(MOD);
			hash = hash.xor(BigInteger.valueOf(intOctet));
		}
		return hash.longValue();
	}
	
	/**
	 * Computes a single hash value using
	 * the MurmmurHash algorithm
	 * 
	 * @param string	Key
	 * @return			Hash value long type [0,2^63]
	 */
	public static long murmurHash(String string) {
		// Initialize variables
		int seed = 283302;
		byte[] data = string.getBytes();
		int dataLength = data.length;
		int m = 0x5bd1e995;
		int r = 24;
		int hash = seed ^ dataLength;
		// Loop variables
		int lengthOf4ByteChunks = dataLength >> 2;
		int k;
		int indexOf4ByteChunks;
		for (int ii = 0; ii < lengthOf4ByteChunks; ii++) {
			indexOf4ByteChunks = ii << 2;
			k = data[indexOf4ByteChunks + 3];
			k = k << 8;
			k = k | (data[indexOf4ByteChunks + 2] & 0xff);
			k = k << 8;
			k = k | (data[indexOf4ByteChunks + 1] & 0xff);
			k = k << 8;
			k = k | (data[indexOf4ByteChunks + 0] & 0xff);
			k *= m;
			k ^= k >>> r;
			k *= m;
			hash *= m;
			hash ^= k;
		}

		int lengthOfM = lengthOf4ByteChunks << 2;
		int left = dataLength - lengthOfM;

		if (left != 0) {
			if (left >= 3) {
				hash ^= (int) data[dataLength - 3] << 16;
			}
			if (left >= 2) {
				hash ^= (int) data[dataLength - 2] << 8;
			}
			if (left >= 1) {
				hash ^= (int) data[dataLength - 1];
			}

			hash *= m;
		}

		hash ^= hash >>> 13;
		hash *= m;
		hash ^= hash >>> 15;
		
		return (long)(hash & 0x00000000ffffffffL);
	}


}
