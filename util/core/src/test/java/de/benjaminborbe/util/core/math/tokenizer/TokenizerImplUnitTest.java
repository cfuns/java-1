package de.benjaminborbe.util.core.math.tokenizer;

import de.benjaminborbe.util.core.math.CompareUtil;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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
