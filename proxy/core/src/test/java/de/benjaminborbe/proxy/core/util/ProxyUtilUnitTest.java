package de.benjaminborbe.proxy.core.util;

import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.tools.util.ParseUtilImpl;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ProxyUtilUnitTest {

	@Test
	public void testParseHostname() throws Exception {
		final ParseUtil parseUtil = new ParseUtilImpl();
		final ProxyUtil proxyUtil = new ProxyUtil(parseUtil);
		assertThat(proxyUtil.parseHostname("GET http://www.benjamin-borbe.de/bb"), is("www.benjamin-borbe.de"));
		assertThat(proxyUtil.parseHostname("GET http://www.benjamin-borbe.de"), is("www.benjamin-borbe.de"));
		assertThat(proxyUtil.parseHostname("GET http://www.benjamin-borbe.de:80/bb"), is("www.benjamin-borbe.de"));
		assertThat(proxyUtil.parseHostname("GET http://www.benjamin-borbe.de:80"), is("www.benjamin-borbe.de"));
	}

	@Test
	public void testParsePort() throws Exception {
		final ParseUtil parseUtil = new ParseUtilImpl();
		final ProxyUtil proxyUtil = new ProxyUtil(parseUtil);
		assertThat(proxyUtil.parsePort("GET http://www.benjamin-borbe.de/bb"), is(80));
		assertThat(proxyUtil.parsePort("GET http://www.benjamin-borbe.de"), is(80));
		assertThat(proxyUtil.parsePort("GET http://www.benjamin-borbe.de:81/bb"), is(81));
		assertThat(proxyUtil.parsePort("GET http://www.benjamin-borbe.de:81"), is(81));
	}

}
