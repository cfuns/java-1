package de.benjaminborbe.proxy.core.util;

import de.benjaminborbe.tools.util.ParseException;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.tools.util.ParseUtilImpl;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

public class ProxyLineParserUnitTest {

	@Test
	public void testParseHostname() throws Exception {
		final ParseUtil parseUtil = new ParseUtilImpl();
		final ProxyLineParser proxyLineParser = new ProxyLineParser(parseUtil);
		assertThat(proxyLineParser.parseHostname("GET http://www.benjamin-borbe.de/bb"), is("www.benjamin-borbe.de"));
		assertThat(proxyLineParser.parseHostname("GET http://www.benjamin-borbe.de"), is("www.benjamin-borbe.de"));
		assertThat(proxyLineParser.parseHostname("GET http://www.benjamin-borbe.de:80/bb"), is("www.benjamin-borbe.de"));
		assertThat(proxyLineParser.parseHostname("GET http://www.benjamin-borbe.de:80"), is("www.benjamin-borbe.de"));
	}

	@Test
	public void testParsePort() throws Exception {
		final ParseUtil parseUtil = new ParseUtilImpl();
		final ProxyLineParser proxyLineParser = new ProxyLineParser(parseUtil);
		assertThat(proxyLineParser.parsePort("GET http://www.benjamin-borbe.de/bb"), is(80));
		assertThat(proxyLineParser.parsePort("GET http://www.benjamin-borbe.de"), is(80));
		assertThat(proxyLineParser.parsePort("GET http://www.benjamin-borbe.de:81/bb"), is(81));
		assertThat(proxyLineParser.parsePort("GET http://www.benjamin-borbe.de:81"), is(81));
		assertThat(proxyLineParser.parsePort("GET http://www.benjamin-borbe.de/bla:81"), is(80));
	}

	@Test
	public void testGetUrl() throws ParseException {
		final ParseUtil parseUtil = new ParseUtilImpl();
		final ProxyLineParser proxyLineParser = new ProxyLineParser(parseUtil);
		assertThat(proxyLineParser.parseUrl("GET http://www.benjamin-borbe.de/bb"), is("http://www.benjamin-borbe.de/bb"));
		assertThat(proxyLineParser.parseUrl("GET http://www.benjamin-borbe.de/bb HTTP/1.0"), is("http://www.benjamin-borbe.de/bb"));
	}

	@Test
	public void testParseReturnCode() throws ParseException {
		final ParseUtil parseUtil = new ParseUtilImpl();
		final ProxyLineParser proxyLineParser = new ProxyLineParser(parseUtil);
		assertThat(proxyLineParser.parseReturnCode("HTTP/1.0 200 OK"), is(200));
	}

	@Test
	public void testParseHeaderLine() throws Exception {
		final ParseUtil parseUtil = new ParseUtilImpl();
		final ProxyLineParser proxyLineParser = new ProxyLineParser(parseUtil);
		assertThat(proxyLineParser.parseHeaderLine("Key: Value"), is(notNullValue()));
		assertThat(proxyLineParser.parseHeaderLine("Key: Value").size(), is(1));
		assertThat(proxyLineParser.parseHeaderLine("Key: Value").get("Key").size(), is(1));
		assertThat(proxyLineParser.parseHeaderLine("Key: Value").get("Key").get(0), is("Value"));

		assertThat(proxyLineParser.parseHeaderLine("Key: Value1, Value2"), is(notNullValue()));
		assertThat(proxyLineParser.parseHeaderLine("Key: Value1, Value2").size(), is(1));
		assertThat(proxyLineParser.parseHeaderLine("Key: Value1, Value2").get("Key").size(), is(2));
		assertThat(proxyLineParser.parseHeaderLine("Key: Value1, Value2").get("Key").get(0), is("Value1"));
		assertThat(proxyLineParser.parseHeaderLine("Key: Value1, Value2").get("Key").get(1), is("Value2"));
	}
}
