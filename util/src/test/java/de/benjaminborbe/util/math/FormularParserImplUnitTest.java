package de.benjaminborbe.util.math;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.junit.Test;

public class FormularParserImplUnitTest {

	@Test
	public void testParse() throws Exception {
		final CompareUtil compareUtil = new CompareUtil();
		final Tokenizer tokenizer = new TokenizerImpl(compareUtil);
		final Functions functions = new Functions();
		final Operations operations = new Operations();
		final Constants constants = new Constants();
		final FormularParser parser = new FormularParserImpl(tokenizer, compareUtil, functions, operations, constants);
		try {
			parser.parse(null);
			fail("ExpressionParseException expected");
		}
		catch (final FormularParseException e) {
			assertNotNull(e);
		}
		try {
			parser.parse("");
			fail("ExpressionParseException expected");
		}
		catch (final FormularParseException e) {
			assertNotNull(e);
		}
		assertEquals(0d, parser.parse("0").getValue(), 0.1d);
		assertEquals(1d, parser.parse("1").getValue(), 0.1d);
		assertEquals(12d, parser.parse("12").getValue(), 0.1d);
		assertEquals(123d, parser.parse("123").getValue(), 0.1d);
		for (long i = 0; i < 100; i++) {
			assertEquals(1d * i, parser.parse(String.valueOf(i)).getValue(), 0.1d);
		}
		assertEquals(0l, parser.parse(" 0 ").getValue(), 0.1d);
		assertEquals(1d, parser.parse(" 1 ").getValue(), 0.1d);
		assertEquals(12d, parser.parse(" 12 ").getValue(), 0.1d);
		assertEquals(123d, parser.parse(" 123 ").getValue(), 0.1d);
		assertEquals(123d, parser.parse(" 123 ").getValue(), 0.1d);

		assertEquals(1d, parser.parse("1").getValue(), 0.1d);
		assertEquals(1d, parser.parse("(1)").getValue(), 0.1d);
		assertEquals(1d, parser.parse("((1))").getValue(), 0.1d);
		assertEquals(1d, parser.parse("(((1)))").getValue(), 0.1d);

		// assertEquals(3d, parser.parse("1+2").getValue(), 0.1d);
		// assertEquals(6d, parser.parse("1+2+3").getValue(), 0.1d);
		// assertEquals(3d, parser.parse(" 1 + 2 ").getValue(), 0.1d);
		// assertEquals(6d, parser.parse(" 1 + 2 + 3 ").getValue(), 0.1d);
	}
}
