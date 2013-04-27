package de.benjaminborbe.tools.url;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

public class MapParameterUnitTest {

	@Test
	public void testValue() throws Exception {
		final MapParameter mapParameter = new MapParameter();
		assertThat(mapParameter.get("a"), is(nullValue()));
		mapParameter.add("a", "b");
		assertThat(mapParameter.get("a"), is(not(nullValue())));
		assertThat(mapParameter.get("a").length, is(1));
	}

	@Test
	public void testValues() throws Exception {
		final MapParameter mapParameter = new MapParameter();
		assertThat(mapParameter.get("a"), is(nullValue()));
		mapParameter.add("a", new String[]{"b", "c"});
		assertThat(mapParameter.get("a"), is(not(nullValue())));
		assertThat(mapParameter.get("a").length, is(2));
	}

	@Test
	public void testNullValue() throws Exception {
		final MapParameter mapParameter = new MapParameter();
		assertThat(mapParameter.get("a"), is(nullValue()));
		mapParameter.add("a", (String) null);
		assertThat(mapParameter.get("a"), is(not(nullValue())));
		assertThat(mapParameter.get("a").length, is(0));
	}
}
