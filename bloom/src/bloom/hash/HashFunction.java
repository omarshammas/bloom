package bloom.hash;

import java.math.BigInteger;

public class HashFunction {

	private static final BigInteger FNV_PRIME =  new BigInteger("16777619");
	private static final BigInteger FNV_OFFSET_BASIS = new BigInteger("2166136261");
	private static final BigInteger MOD = new BigInteger("4294967296");
	
	
	public static int[] hash(String string,int n){
		int[] hashes = new int[3];
		long hash = fnv1Hash(string);
		hashes[0] = (int)(hash % n);
		hash = fnv1aHash(string);
		hashes[1] = (int)(hash % n);
		hash = MurmurHash(string);
		hashes[2] = (int)(hash % n);
		return hashes;
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
	
	private static long fnv1aHash(String string){
		byte[] data = string.getBytes();
		BigInteger hash = FNV_OFFSET_BASIS;
		for(int ii = 0; ii < data.length; ii++){
			int intOctet = data[ii] & 0xff;
			hash = hash.xor(BigInteger.valueOf(intOctet));
			hash = hash.multiply(FNV_PRIME).mod(MOD);
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
