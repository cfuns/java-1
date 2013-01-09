package de.benjaminborbe.websearch.util;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class WebsearchRobotsTxtUnitTest {

	@Test
	public void testIsAllowed() throws Exception {
		final WebsearchRobotsTxt robotsTxt = new WebsearchRobotsTxt();
		assertThat(robotsTxt.isAllowed("foo", "/bla"), is(true));
		robotsTxt.add("*", "/bla");
		assertThat(robotsTxt.isAllowed("foo", "/bla"), is(false));
		robotsTxt.add("bar", "/foo");
		assertThat(robotsTxt.isAllowed("bar", "/foo"), is(false));
		assertThat(robotsTxt.isAllowed("bar", "/foo/"), is(false));
		assertThat(robotsTxt.isAllowed("bar", "/foo/bla"), is(false));
	}
}
