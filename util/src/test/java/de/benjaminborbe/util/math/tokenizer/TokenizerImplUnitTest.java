package de.benjaminborbe.util.math.tokenizer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import de.benjaminborbe.util.math.CompareUtil;
import de.benjaminborbe.util.math.tokenizer.TokenizerImpl;

public class TokenizerImplUnitTest {

	@Test
	public void testTokenize() throws Exception {
		final CompareUtil compareUtil = new CompareUtil();
		final TokenizerImpl tokenizer = new TokenizerImpl(compareUtil);

		{
			final List<String> result = tokenizer.tokenize("");
			assertNotNull(result);
			assertEquals(Arrays.asList(), result);
		}
		{
			final List<String> result = tokenizer.tokenize("1");
			assertNotNull(result);
			assertEquals(Arrays.asList("1"), result);
		}
		{
			final List<String> result = tokenizer.tokenize(" 1 ");
			assertNotNull(result);
			assertEquals(Arrays.asList("1"), result);
		}
		{
			final List<String> result = tokenizer.tokenize(" 1 2 ");
			assertNotNull(result);
			assertEquals(Arrays.asList("1", "2"), result);
		}
		{
			final List<String> result = tokenizer.tokenize(" ( 1 2 ) ");
			assertNotNull(result);
			assertEquals(Arrays.asList("(", "1", "2", ")"), result);
		}
		{
			final List<String> result = tokenizer.tokenize("1+2");
			assertNotNull(result);
			assertEquals(Arrays.asList("1", "+", "2"), result);
		}
	}
}
