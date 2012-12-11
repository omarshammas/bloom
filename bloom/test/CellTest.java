import static org.junit.Assert.*;

import org.junit.Test;

import bloom.filters.Cell;


public class CellTest {

	static String KEY = "teststring";
	
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

}
