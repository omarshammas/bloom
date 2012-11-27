package bloom.filters;

public class Cell {

	protected byte count;
	protected String idSum;
	protected int hashSum;
	
	public Cell(){
		count = 0;
		idSum = "          ";
		hashSum = 0;
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
	
	public Cell subtract(Cell c1, Cell c2){
		Cell c = new Cell();
		c.count = (byte) (c1.count - c2.count);
		c.idSum = XOR(c1.idSum, c2.idSum);
		c.hashSum = c1.hashSum ^ c2.hashSum;
		return c;
	}
	
	private String XOR(String s1, String s2) {
		StringBuilder sb = new StringBuilder();
		assert s1.length() == s2.length(); //TODO replace with exception
		
		for(int i=0; i<s1.length() && i<s2.length();i++)
		    sb.append((char)(s1.charAt(i) ^ s2.charAt(i)));
		String result = sb.toString();
		return result;
	}
}
