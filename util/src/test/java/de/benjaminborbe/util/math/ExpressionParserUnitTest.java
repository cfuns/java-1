package de.benjaminborbe.util.math;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.junit.Test;

public class ExpressionParserUnitTest {

	@Test
	public void testParse() throws Exception {
		final ExpressionParser parser = new ExpressionParser();
		try {
			parser.parse(null);
			fail("ExpressionParseException expected");
		}
		catch (final ExpressionParseException e) {
			assertNotNull(e);
		}
		try {
			parser.parse("");
			fail("ExpressionParseException expected");
		}
		catch (final ExpressionParseException e) {
			assertNotNull(e);
		}
		assertEquals(0d, parser.parse("0"), 0.1d);
		assertEquals(1d, parser.parse("1"), 0.1d);
		assertEquals(12d, parser.parse("12"), 0.1d);
		assertEquals(123d, parser.parse("123"), 0.1d);
		for (long i = 0; i < 100; i++) {
			assertEquals(1d * i, parser.parse(String.valueOf(i)), 0.1d);
		}
		assertEquals(0l, parser.parse(" 0 "), 0.1d);
		assertEquals(1d, parser.parse(" 1 "), 0.1d);
		assertEquals(12d, parser.parse(" 12 "), 0.1d);
		assertEquals(123d, parser.parse(" 123 "), 0.1d);
		assertEquals(123d, parser.parse(" 123 "), 0.1d);
		assertEquals(3d, parser.parse("1+2"), 0.1d);
		assertEquals(6d, parser.parse("1+2+3"), 0.1d);
		assertEquals(3d, parser.parse(" 1 + 2 "), 0.1d);
		assertEquals(6d, parser.parse(" 1 + 2 + 3 "), 0.1d);
	}
}
