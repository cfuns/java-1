package de.benjaminborbe.tools.util;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class StringIteratorUnitTest {

	@Test
	public void testEmpty() throws Exception {
		{
			final StringIterator si = new StringIterator(null);
			assertThat(si.hasCurrentCharacter(), is(false));
			assertThat(si.hasNextCharacter(), is(false));
		}
		{
			final StringIterator si = new StringIterator("");
			assertThat(si.hasCurrentCharacter(), is(false));
			assertThat(si.hasNextCharacter(), is(false));
		}
	}

	@Test
	public void testString() {
		final StringIterator si = new StringIterator("abcd");
		assertThat(si.hasCurrentCharacter(), is(true));
		assertThat(si.hasNextCharacter(), is(true));
		assertThat(si.getCurrentCharacter(), is('a'));
		assertThat(si.getNextCharacter(), is('b'));
		assertThat(si.getCurrentPosition(), is(0));

		si.next();
		assertThat(si.hasCurrentCharacter(), is(true));
		assertThat(si.hasNextCharacter(), is(true));
		assertThat(si.getCurrentCharacter(), is('b'));
		assertThat(si.getNextCharacter(), is('c'));
		assertThat(si.getCurrentPosition(), is(1));
		si.next();

		assertThat(si.hasCurrentCharacter(), is(true));
		assertThat(si.hasNextCharacter(), is(true));
		assertThat(si.getCurrentCharacter(), is('c'));
		assertThat(si.getNextCharacter(), is('d'));
		assertThat(si.getCurrentPosition(), is(2));
		si.next();

		assertThat(si.hasCurrentCharacter(), is(true));
		assertThat(si.hasNextCharacter(), is(false));
		assertThat(si.getCurrentCharacter(), is('d'));
		assertThat(si.getCurrentPosition(), is(3));
		si.next();

		assertThat(si.hasCurrentCharacter(), is(false));
		assertThat(si.hasNextCharacter(), is(false));
		assertThat(si.getCurrentPosition(), is(4));
	}
}
