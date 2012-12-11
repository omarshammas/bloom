package bloom.hash;

import java.math.BigInteger;
import java.util.Random;

public class HashFunction {

	private static final BigInteger FNV_PRIME =  new BigInteger("16777619");
	private static final BigInteger FNV_OFFSET_BASIS = new BigInteger("2166136261");
	private static final BigInteger MOD = new BigInteger("4294967296");
	private static final int P = 2147483647;
	
	public static int hash(String key, int m){
		return (int)(MurmurHash(key) % m);
	}
	
	public static int[] hash(String key,int numberOfHashes, int m){
		int[] hashes = new int[numberOfHashes];
		for(int ii = 0; ii < numberOfHashes; ii++){
			long h1 = fnv1Hash(key);
			long h2 = MurmurHash(key);
			long axb = h1+(ii*h2);
			hashes[ii] = (int)(axb % m);
		}
		return hashes;
	}
	
	public static int minWiseHash(int hashCode, int k){
		Random random = new Random(k);
		long a = random.nextInt(Integer.MAX_VALUE-1)+1;
		long b = random.nextInt(Integer.MAX_VALUE);
		long axb = a * hashCode + b;
		return (int)(((axb % P)+P)%P);
	}
	
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

	public static long MurmurHash(String string) {
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
