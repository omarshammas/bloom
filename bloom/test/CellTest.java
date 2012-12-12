import static org.junit.Assert.*;

import org.junit.Test;

import bloom.filters.Cell;


public class CellTest {

	static String KEY = "teststring";
	static String KEY2 = "TESTstring";
	
	@Test
	public void testConstructor(){
		Cell c = new Cell();
		assertEquals(c.count, 0);
		assertEquals(c.idSum, Cell.INITIAL_ID_SUM);
		assertEquals(c.hashSum, Cell.INITIAL_HASH_SUM);
	}
	
	@Test
	public void testAdd() {
		Cell c = new Cell();
		c.add(KEY);
		assertEquals(1, c.count);
		assertEquals(Cell.INITIAL_HASH_SUM^KEY.hashCode(), c.hashSum);
	}

	@Test
	public void testRemove() {
		Cell c = new Cell();
		c.add(KEY);
		c.remove(KEY);
		assertEquals(c.count, 0);
		assertEquals(c.idSum, Cell.INITIAL_ID_SUM);
		assertEquals(c.hashSum, Cell.INITIAL_HASH_SUM);
		
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
		assertEquals(KEY,  c.extractPure());
	}
	
	@Test
	public void testSubtract(){
		Cell c1 = new Cell();
		Cell c2 = new Cell();
		
		c1.add(KEY);
		c2.add(KEY2);
		
		Cell c = Cell.subtract(c1, c2);
		
		assertEquals(c.count, 0);
		assertEquals(c.hashSum, c1.hashSum ^ c2.hashSum ^ Cell.INITIAL_HASH_SUM);
		assertEquals(c.idSum, Cell.XOR(Cell.XOR(c1.idSum, c2.idSum), Cell.INITIAL_ID_SUM));
	}

}
