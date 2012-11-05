package bloom.hash;

public class HashFunction {
	
	  public static int MurmurHash(byte[] data, int seed) {
		    int dataLength = data.length;
		  	int m = 0x5bd1e995;
		    int r = 24;
		    int hash = seed ^ dataLength;

		    int lengthOf4BitChunks = dataLength >> 2;
		    int k;
		    int indexOf4BitChunks;
		    for (int ii = 0; ii < lengthOf4BitChunks; ii++) {
		      indexOf4BitChunks = ii << 2;
		      k = data[indexOf4BitChunks + 3];
		      k = k << 8;
		      k = k | (data[indexOf4BitChunks + 2] & 0xff);
		      k = k << 8;
		      k = k | (data[indexOf4BitChunks + 1] & 0xff);
		      k = k << 8;
		      k = k | (data[indexOf4BitChunks + 0] & 0xff);
		      k *= m;
		      k ^= k >>> r;
		      k *= m;
		      hash *= m;
		      hash ^= k;
		    }

		    int lengthOfM = lengthOf4BitChunks << 2;
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

		    return hash;
		  }

}
