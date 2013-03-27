package de.benjaminborbe.tools.util;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class ComparatorBaseUnitTest {

	private class ComparatorString extends ComparatorBase<String, String> {

		private final boolean inverted;

		private final boolean nullfirst;

		public ComparatorString(final boolean inverted, final boolean nullfirst) {
			this.inverted = inverted;
			this.nullfirst = nullfirst;
		}

		@Override
		public String getValue(final String o) {
			return o;
		}

		@Override
		public boolean inverted() {
			return inverted;
		}

		@Override
		public boolean nullFirst() {
			return nullfirst;
		}
	}

	@Test
	public void testSortStringArrayNotInvertedNullfirst() {
		final List<String> list = Arrays.asList("a", "c", "b", null);
		Collections.sort(list, new ComparatorString(false, true));
		assertEquals(null, list.get(0));
		assertEquals("a", list.get(1));
		assertEquals("b", list.get(2));
		assertEquals("c", list.get(3));
	}

	@Test
	public void testSortStringArrayInvertedNullfirst() {
		final List<String> list = Arrays.asList("a", "c", "b", null);
		Collections.sort(list, new ComparatorString(true, true));
		assertEquals(null, list.get(0));
		assertEquals("c", list.get(1));
		assertEquals("b", list.get(2));
		assertEquals("a", list.get(3));
	}

	@Test
	public void testSortStringArrayNotInvertedNullnotfirst() {
		final List<String> list = Arrays.asList("a", "c", "b", null);
		Collections.sort(list, new ComparatorString(false, false));
		assertEquals("a", list.get(0));
		assertEquals("b", list.get(1));
		assertEquals("c", list.get(2));
		assertEquals(null, list.get(3));
	}

	@Test
	public void testSortStringArrayInvertedNullnotfirst() {
		final List<String> list = Arrays.asList("a", "c", "b", null);
		Collections.sort(list, new ComparatorString(true, false));
		assertEquals("c", list.get(0));
		assertEquals("b", list.get(1));
		assertEquals("a", list.get(2));
		assertEquals(null, list.get(3));
	}
}
