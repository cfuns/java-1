package de.benjaminborbe.tools.search;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

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

		assertThat(searchUtil.buildSearchParts(" a ").size(), is(1));
		assertThat(searchUtil.buildSearchParts(" A ").size(), is(1));
		assertThat(searchUtil.buildSearchParts(" 0 ").size(), is(1));
		assertThat(searchUtil.buildSearchParts(" ö ").size(), is(1));
		assertThat(searchUtil.buildSearchParts(" ä ").size(), is(1));
		assertThat(searchUtil.buildSearchParts(" ü ").size(), is(1));
		assertThat(searchUtil.buildSearchParts(" Ö ").size(), is(1));
		assertThat(searchUtil.buildSearchParts(" Ä ").size(), is(1));
		assertThat(searchUtil.buildSearchParts(" Ü ").size(), is(1));
		assertThat(searchUtil.buildSearchParts(" ß ").size(), is(1));

		assertThat(searchUtil.buildSearchParts("foo bar"), is(hasItem("foo")));
		assertThat(searchUtil.buildSearchParts("foo bar"), is(hasItem("bar")));

		assertThat(searchUtil.buildSearchParts("Foo Bar"), is(hasItem("foo")));
		assertThat(searchUtil.buildSearchParts("Foo Bar"), is(hasItem("bar")));

		assertThat(searchUtil.buildSearchParts("it'll").size(), is(1));
		assertThat(searchUtil.buildSearchParts("it'll"), is(hasItem("it'll")));

		assertThat(searchUtil.buildSearchParts("'foo").size(), is(1));
		assertThat(searchUtil.buildSearchParts("'foo"), is(hasItem("foo")));
	}

	@Test
	public void testParseMultibleWords() {
		final SearchUtil searchUtil = new SearchUtil();

		final String term = "Cool Portal Bla";

		assertThat(searchUtil.buildSearchParts(term).size(), is(3));
		assertThat(searchUtil.buildSearchParts(term), is(hasItem("cool")));
		assertThat(searchUtil.buildSearchParts(term), is(hasItem("portal")));
		assertThat(searchUtil.buildSearchParts(term), is(hasItem("bla")));
	}

	@Test
	public void testParseCamelCase() throws Exception {
		final SearchUtil searchUtil = new SearchUtil();

		assertThat(searchUtil.buildSearchParts("FooBar"), is(hasItem("foo")));
		assertThat(searchUtil.buildSearchParts("FooBar"), is(hasItem("bar")));

		assertThat(searchUtil.buildSearchParts(" FooBar "), is(hasItem("foo")));
		assertThat(searchUtil.buildSearchParts(" FooBar "), is(hasItem("bar")));

		assertThat(searchUtil.buildSearchParts(" foobar "), is(hasItem("foobar")));
	}
}
