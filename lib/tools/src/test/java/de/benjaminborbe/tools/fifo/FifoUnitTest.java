package de.benjaminborbe.tools.fifo;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

public class FifoUnitTest {

	@Test(expected = FifoIndexOutOfBoundsException.class)
	public void testOutOfBounds() throws Exception {
		final Fifo<Object> fifo = new Fifo<>();
		fifo.add(new Object());
		assertNotNull(fifo.get(0));
		fifo.get(1);
		fifo.get(2);
	}

	@Test
	public void testSize() {
		final Fifo<Object> fifo = new Fifo<>();
		assertEquals(0, fifo.size());
		fifo.add(new Object());
		assertEquals(1, fifo.size());
	}

	@Test
	public void testAddGet() throws Exception {
		final Fifo<Object> fifo = new Fifo<>();
		final Object o1 = "a";
		fifo.add(o1);
		assertEquals(o1, fifo.get(0));
		assertEquals(o1, fifo.get());
		final Object o2 = "b";
		fifo.add(o2);
		assertEquals(o1, fifo.get(0));
		assertEquals(o1, fifo.get());
		fifo.remove();
		assertEquals(o2, fifo.get(0));
		assertEquals(o2, fifo.get());
	}

	@Test
	public void testFirst() throws Exception {
		final Fifo<Object> fifo = new Fifo<>();
		final Object o1 = "1";
		fifo.add(o1);
		final Object o2 = "2";
		fifo.add(o2);
		final Object o3 = "3";
		fifo.add(o3);

		final List<Object> list = fifo.first(2);
		assertEquals(2, list.size());
		assertEquals(o1, list.get(0));
		assertEquals(o2, list.get(1));

		assertEquals(0, fifo.first(0).size());
		assertEquals(1, fifo.first(1).size());
		assertEquals(2, fifo.first(2).size());
		assertEquals(3, fifo.first(3).size());
	}

	@Test
	public void testLast() throws Exception {
		final Fifo<Object> fifo = new Fifo<>();
		final Object o1 = "1";
		fifo.add(o1);
		final Object o2 = "2";
		fifo.add(o2);
		final Object o3 = "3";
		fifo.add(o3);

		final List<Object> list = fifo.last(2);
		assertEquals(2, list.size());
		assertEquals(o3, list.get(0));
		assertEquals(o2, list.get(1));

		assertEquals(0, fifo.last(0).size());
		assertEquals(1, fifo.last(1).size());
		assertEquals(2, fifo.last(2).size());
		assertEquals(3, fifo.last(3).size());
	}

	public void testRemove() throws Exception {
		final Fifo<Object> fifo = new Fifo<>();
		final Object o1 = "1";
		fifo.add(o1);
		final Object o2 = "2";
		fifo.add(o2);
		assertEquals(2, fifo.size());
		fifo.remove();
		assertEquals(1, fifo.size());
		fifo.remove();
		assertEquals(0, fifo.size());
		try {
			fifo.remove();
			fail("execption expected");
		} catch (final FifoIndexOutOfBoundsException e) {
			assertNotNull(e);
		}
	}
}
