package bloom.filters;

public class Cell {

	public byte count;
	public String idSum;
	public int hashSum;
	public static String INITIAL_ID_SUM = "          ";
	public static int INITIAL_HASH_SUM = INITIAL_ID_SUM.hashCode();
	
	public Cell(){
		count = 0;
		idSum = INITIAL_ID_SUM;
		hashSum = INITIAL_HASH_SUM;
	}
	
	public void add(String key){
		count++;
		idSum = XOR(idSum,key);
		hashSum = hashSum ^ key.hashCode();
	}
	
	public void remove(String key){
		//TODO throw exception
		count--;
		idSum = XOR(idSum,key);
		hashSum = hashSum ^ key.hashCode();
	}
	
	public boolean isEmpty(){
		return idSum.equals(INITIAL_ID_SUM) && hashSum == INITIAL_HASH_SUM;
	}
	
	public boolean isPure(){
		return !idSum.equals(INITIAL_ID_SUM) && hashSum == (extractPure().hashCode()^INITIAL_HASH_SUM);
	}
	
	public String extractPure(){
		return XOR(INITIAL_ID_SUM, idSum);
	}	
	
	public static Cell subtract(Cell c1, Cell c2){
		Cell c = new Cell();
		c.count = (byte) (c1.count - c2.count);
		c.idSum = XOR(XOR(c1.idSum, c2.idSum), INITIAL_ID_SUM);
		c.hashSum = c1.hashSum ^ c2.hashSum ^ INITIAL_HASH_SUM;
		return c;
	}
	
	//TODO make private
	public static String XOR(String s1, String s2) {	
		StringBuilder sb = new StringBuilder();
		assert s1.length() == s2.length(); //TODO replace with exception
		
		for(int i=0; i<s1.length() && i<s2.length();i++){
		    sb.append((char)(s1.charAt(i) ^ s2.charAt(i)));
		}
		String result = sb.toString();
		return result;
	}
}
