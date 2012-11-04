package de.benjaminborbe.tools.util;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class ComparatorBaseUnitTest {

	private class ComparatorString extends ComparatorBase<String, String> {

		@Override
		public String getValue(final String o) {
			return o;
		}

	}

	@Test
	public void testSortStringArray() {
		final List<String> list = Arrays.asList("a", "c", "b");
		Collections.sort(list, new ComparatorString());
		assertEquals("a", list.get(0));
		assertEquals("b", list.get(1));
		assertEquals("c", list.get(2));
	}
}
