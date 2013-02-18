package de.benjaminborbe.tools.search;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Ignore;
import org.junit.Test;

public class SearchUtilUnitTest {

	@Test
	public void testParse() throws Exception {
		final SearchUtil searchUtil = new SearchUtil();
		assertThat(searchUtil.buildSearchParts(null).size(), is(0));
		assertThat(searchUtil.buildSearchParts("").size(), is(0));
		assertThat(searchUtil.buildSearchParts("   ").size(), is(0));
		assertThat(searchUtil.buildSearchParts(" a b  ").size(), is(2));
		assertThat(searchUtil.buildSearchParts(" für ").size(), is(1));
		assertThat(searchUtil.buildSearchParts(" FÜR ").size(), is(1));

		assertThat(searchUtil.buildSearchParts("foo bar"), is(hasItem("foo")));
		assertThat(searchUtil.buildSearchParts("foo bar"), is(hasItem("bar")));

		assertThat(searchUtil.buildSearchParts("Foo Bar"), is(hasItem("foo")));
		assertThat(searchUtil.buildSearchParts("Foo Bar"), is(hasItem("bar")));

	}

	@Ignore("TODO")
	@Test
	public void testParseCamelCase() throws Exception {
		final SearchUtil searchUtil = new SearchUtil();

		assertThat(searchUtil.buildSearchParts("FooBar"), is(hasItem("foo")));
		assertThat(searchUtil.buildSearchParts("FooBar"), is(hasItem("bar")));
	}
}
