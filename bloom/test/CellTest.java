import static org.junit.Assert.*;

import org.junit.Test;

import bloom.filters.Cell;


public class CellTest {

	static String KEY = "test";
	static String KEY2 = "TEST";
	
	@Test
	public void testConstructor(){
		Cell c = new Cell();
		assertEquals(c.getIdSum(), Cell.INITIAL_ID_SUM);
		assertEquals(c.getHashSum(), Cell.INITIAL_HASH_SUM);
	}
	
	@Test
	public void testAdd() {
		Cell c = new Cell();
		c.add(KEY);
		assertEquals(Cell.INITIAL_HASH_SUM^KEY.hashCode(), c.getHashSum());
	}

	@Test
	public void testRemove() {
		Cell c = new Cell();
		c.add(KEY);
		c.remove(KEY);
		assertEquals(c.getIdSum(), Cell.INITIAL_ID_SUM);
		assertEquals(c.getHashSum(), Cell.INITIAL_HASH_SUM);
		
	}
	
	@Test
	public void testIsEmpty(){
		Cell c = new Cell();
		assertEquals(true, c.isEmpty());

		c.add(KEY);
		assertEquals(false, c.isEmpty());
		
		c.remove(KEY);
		assertEquals(true, c.isEmpty());
	}
	
	@Test
	public void testIsPure(){
		Cell c = new Cell();
		assertEquals(false, c.isPure());
		
		c.add(KEY);
		assertEquals(true,  c.isPure());
		
		c.add(KEY2);
		assertEquals(false,  c.isPure());
		
		c.remove(KEY2);
		assertEquals(true,  c.isPure());
		
		c.remove(KEY);
		assertEquals(false,  c.isPure());
	}
	
	@Test
	public void testExtractPure(){
		Cell c = new Cell();
		c.add(KEY);
		assertEquals(KEY,  c.extractKey());
	}
	
	@Test
	public void testSubtract(){
		Cell c1 = new Cell();
		Cell c2 = new Cell();
		
		c1.add(KEY);
		c2.add(KEY2);
		
		Cell c = Cell.subtract(c1, c2);
		
		assertEquals(c.getHashSum(), c1.getHashSum() ^ c2.getHashSum() ^ Cell.INITIAL_HASH_SUM);
		assertEquals(c.getIdSum(), Cell.XOR(Cell.XOR(c1.getIdSum(), c2.getIdSum()), Cell.INITIAL_ID_SUM));
	}

}
