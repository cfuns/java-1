package de.benjaminborbe.util.core.math;

import de.benjaminborbe.util.core.math.constant.Constants;
import de.benjaminborbe.util.core.math.function.Functions;
import de.benjaminborbe.util.core.math.operation.Operations;
import de.benjaminborbe.util.core.math.tokenizer.Tokenizer;
import de.benjaminborbe.util.core.math.tokenizer.TokenizerImpl;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

public class FormularParserImplUnitTest {

	@Test
	public void testParse() throws Exception {
		final FormularParser parser = buildParser();
		try {
			parser.parse(null);
			fail("ExpressionParseException expected");
		} catch (final FormularParseException e) {
			assertNotNull(e);
		}
		try {
			parser.parse("");
			fail("ExpressionParseException expected");
		} catch (final FormularParseException e) {
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

		assertEquals(5d, parser.parse("2+3").getValue(), 0.1d);
		assertEquals(6d, parser.parse("2*3").getValue(), 0.1d);
		assertEquals(1d, parser.parse("3-2").getValue(), 0.1d);
		assertEquals(2d, parser.parse("4/2").getValue(), 0.1d);

		assertEquals(5d, parser.parse("(2+3)").getValue(), 0.1d);
		assertEquals(5d, parser.parse("(2)+(3)").getValue(), 0.1d);
		assertEquals(5d, parser.parse("((2)+(3))").getValue(), 0.1d);

		assertEquals(3.14159d, parser.parse("pi").getValue(), 0.1d);
		assertEquals(4.14159d, parser.parse("pi+1").getValue(), 0.1d);
		assertEquals(4.14159d, parser.parse("1+pi").getValue(), 0.1d);

		assertEquals(3d, parser.parse("1+2").getValue(), 0.1d);
		assertEquals(6d, parser.parse("1+2+3").getValue(), 0.1d);
		assertEquals(3d, parser.parse(" 1 + 2 ").getValue(), 0.1d);
		assertEquals(6d, parser.parse(" 1 + 2 + 3 ").getValue(), 0.1d);

		assertEquals(6d, parser.parse("2*3").getValue(), 0.1d);
		assertEquals(24d, parser.parse("2*3*4").getValue(), 0.1d);
		assertEquals(6d, parser.parse(" 2 * 3 ").getValue(), 0.1d);
		assertEquals(24d, parser.parse(" 2 * 3 * 4 ").getValue(), 0.1d);

		assertEquals(1d, parser.parse("3-2").getValue(), 0.1d);
		assertEquals(1d, parser.parse("6-3-2").getValue(), 0.1d);
		assertEquals(1d, parser.parse(" 3 - 2 ").getValue(), 0.1d);
		assertEquals(1d, parser.parse(" 6 - 3 - 2 ").getValue(), 0.1d);

		assertEquals(2d, parser.parse("4/2").getValue(), 0.1d);
		assertEquals(1d, parser.parse("8/4/2").getValue(), 0.1d);
		assertEquals(2d, parser.parse(" 4 / 2 ").getValue(), 0.1d);
		assertEquals(1d, parser.parse(" 8 / 4 / 2 ").getValue(), 0.1d);
	}

	protected FormularParser buildParser() {
		final CompareUtil compareUtil = new CompareUtil();
		final Tokenizer tokenizer = new TokenizerImpl(compareUtil);
		final Functions functions = new Functions();
		final Operations operations = new Operations();
		final Constants constants = new Constants();
		final FormularParser parser = new FormularParserImpl(tokenizer, compareUtil, functions, operations, constants);
		return parser;
	}
}
