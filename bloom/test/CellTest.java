import static org.junit.Assert.*;

import org.junit.Test;

import bloom.filters.Cell;


public class CellTest {

	static String KEY = "teststring";
	
	@Test
	public void testConstructor(){
		Cell c = new Cell();
		assertEquals(c.count, 0);
		assertEquals(c.hashSum, 0);
	}
	
	@Test
	public void testAdd() {
		Cell c = new Cell();
		c.add(KEY);
		assertEquals(c.count, 1);
		assertEquals(c.hashSum, 0^KEY.hashCode());
		assertEquals(c.idSum, KEY); //idSum for some reason becomes capitalized
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
