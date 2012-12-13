import static org.junit.Assert.*;

import org.junit.Test;

import bloom.filters.Cell;
import bloom.utils.Utilities;


public class CellTest {

	
	public static final int TRIALS = 10000;
	
	@Test
	public void testConstructor(){
		Cell c = new Cell();
		assertEquals(c.getIdSum(), Cell.INITIAL_ID_SUM);
		assertEquals(c.getHashSum(), Cell.INITIAL_HASH_SUM);
	}
	
	@Test
	public void testAdd() {
		String key = Utilities.createKey();
		Cell c = new Cell();
		c.add(key);
		assertEquals(Cell.INITIAL_HASH_SUM^key.hashCode(), c.getHashSum());
	}

	@Test
	public void testRemove() {
		String key = Utilities.createKey();
		Cell c = new Cell();
		c.add(key);
		c.remove(key);
		assertEquals(c.getIdSum(), Cell.INITIAL_ID_SUM);
		assertEquals(c.getHashSum(), Cell.INITIAL_HASH_SUM);
		
	}
	
	@Test
	public void testIsEmpty(){
		String key = Utilities.createKey();
		Cell c = new Cell();
		assertEquals(true, c.isEmpty());

		c.add(key);
		assertEquals(false, c.isEmpty());
		
		c.remove(key);
		assertEquals(true, c.isEmpty());
	}
	
	@Test
	public void testIsPure(){
		String key1, key2;
		Cell c;
		
		for (int t=0; t < TRIALS; ++t){
			key1 = Utilities.createKey();
			key2 = Utilities.createKey();
			
			c = new Cell();
			assertEquals(false, c.isPure());

			c.add(key1);
			assertEquals(true,  c.isPure());
			
			c.add(key2);
			assertEquals(false,  c.isPure());
			
			c.remove(key2);
			assertEquals(true,  c.isPure());
			
			c.remove(key1);
			assertEquals(false,  c.isPure());
			
		}	
	}
	
	@Test
	public void testExtractPure(){
		String key;
		Cell c;
		
		for (int t=0; t < TRIALS; ++t){
			key = Utilities.createKey();
			c = new Cell();
			c.add(key);
			assertEquals(key,  c.extractKey());
		}
	}
	
	@Test
	public void testSubtract(){
		String key1 = Utilities.createKey();
		String key2 = Utilities.createKey();
		Cell c1 = new Cell();
		Cell c2 = new Cell();
		
		c1.add(key1);
		c2.add(key2);
		
		Cell c = Cell.subtract(c1, c2);
		
		assertEquals(c.getHashSum(), c1.getHashSum() ^ c2.getHashSum() ^ Cell.INITIAL_HASH_SUM);
		assertEquals(c.getIdSum(), Cell.XOR(Cell.XOR(c1.getIdSum(), c2.getIdSum()), Cell.INITIAL_ID_SUM));
	}

}
