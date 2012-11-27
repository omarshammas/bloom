import static org.junit.Assert.*;

import org.junit.Test;

import bloom.filters.Cell;


public class CellTest {

	static String KEY = "teststring";
	
	//@Test
	public void testConstructor(){
		Cell c = new Cell();
		assertEquals(c.count, 0);
		assertEquals(c.hashSum, 0);
	}
	
	@Test
	public void testAdd() {
		Cell c = new Cell();
		c.add(KEY);
		assertEquals(1, c.count);
		assertEquals(0^KEY.hashCode(), c.hashSum);
	}

	@Test
	public void testRemove() {
		Cell c = new Cell();
		c.add(KEY);
		c.remove(KEY);
		assertEquals(c.count, 0);
		assertEquals(c.hashSum, 0);
	}

}
