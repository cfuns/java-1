package de.benjaminborbe.websearch.core.util;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class WebsearchRobotsTxtUtilUnitTest {

	@Test
	public void testBuildRobotsTxtUrl() {
		final WebsearchRobotsTxtUtil u = new WebsearchRobotsTxtUtil(null, null, null, null);
		assertThat(u.buildRobotsTxtUrl("http://example.com"), is("http://example.com/robots.txt"));
		assertThat(u.buildRobotsTxtUrl("http://example.com/"), is("http://example.com/robots.txt"));
		assertThat(u.buildRobotsTxtUrl("http://example.com/bla"), is("http://example.com/robots.txt"));
	}
}
