package de.benjaminborbe.tools.fifo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;

public class FifoTest {

	@Test(expected = FifoIndexOutOfBoundsException.class)
	public void testOutOfBounds() throws Exception {
		final Fifo<Object> fifo = new Fifo<Object>();
		fifo.add(new Object());
		assertNotNull(fifo.get(0));
		fifo.get(1);
		fifo.get(2);
	}

	@Test
	public void testSize() {
		final Fifo<Object> fifo = new Fifo<Object>();
		assertEquals(0, fifo.size());
		fifo.add(new Object());
		assertEquals(1, fifo.size());
	}

	@Test
	public void testAddGet() throws Exception {
		final Fifo<Object> fifo = new Fifo<Object>();
		final Object o = new Object();
		fifo.add(o);
		assertEquals(o, fifo.get(0));
	}

	@Test
	public void testFirst() throws Exception {
		final Fifo<Object> fifo = new Fifo<Object>();
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
		final Fifo<Object> fifo = new Fifo<Object>();
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
}
