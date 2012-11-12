package de.benjaminborbe.tools.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class StringUtilImplUnitTest {

	@Test
	public void testShorten() {
		final StringUtil s = new StringUtilImpl();
		assertEquals("ab", s.shorten("ab", 10));
		assertEquals("ab", s.shorten("ab", 3));
		assertEquals("ab", s.shorten("ab", 2));
		assertEquals("a", s.shorten("ab", 1));
	}

	@Test
	public void testShortenDots() {
		final StringUtil s = new StringUtilImpl();
		assertEquals("ab", s.shortenDots("ab", 10));
		assertEquals("ab", s.shortenDots("ab", 3));
		assertEquals("ab", s.shortenDots("ab", 2));
		assertEquals("a...", s.shortenDots("ab", 1));
	}
}
