package de.benjaminborbe.tools.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import de.benjaminborbe.tools.guice.ToolModules;

public class ParseUtilTest {

	@Test
	public void testSingleton() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new ToolModules());
		final ParseUtil a = injector.getInstance(ParseUtil.class);
		final ParseUtil b = injector.getInstance(ParseUtil.class);
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
		assertEquals(a, b);
	}

	@Test
	public void testParseBoolean() throws ParseException {
		final ParseUtil parseUtil = new ParseUtilImpl();
		assertEquals(true, parseUtil.parseBoolean("true"));
		assertEquals(false, parseUtil.parseBoolean("false"));
		assertEquals(true, parseUtil.parseBoolean("1"));
		assertEquals(false, parseUtil.parseBoolean("0"));
		assertEquals(true, parseUtil.parseBoolean("tRuE"));
		assertEquals(false, parseUtil.parseBoolean("FaLsE"));
		try {
			parseUtil.parseBoolean("doerteIstDerBeste");
			fail("ParseException expected");
		}
		catch (final ParseException e) {
			assertNotNull(e);
		}
	}

	@Test
	public void testParseBooleanDefault() throws ParseException {
		final ParseUtil parseUtil = new ParseUtilImpl();
		assertEquals(true, parseUtil.parseBoolean("true", false));
		assertEquals(false, parseUtil.parseBoolean("false", true));
		assertEquals(true, parseUtil.parseBoolean("1", false));
		assertEquals(false, parseUtil.parseBoolean("0", true));
		assertEquals(true, parseUtil.parseBoolean("tRuE", false));
		assertEquals(false, parseUtil.parseBoolean("FaLsE", true));
		assertEquals(true, parseUtil.parseBoolean("doerteIstDerBeste", true));
		assertEquals(false, parseUtil.parseBoolean("doerteIstDerBeste", false));
	}

	@Test
	public void testParseEnum() throws ParseException {
		final ParseUtil parseUtil = new ParseUtilImpl();
		assertEquals(TestEnum.A, parseUtil.parseEnum(TestEnum.class, "A"));
		assertEquals(TestEnum.B, parseUtil.parseEnum(TestEnum.class, "B"));
		assertEquals(TestEnum.C, parseUtil.parseEnum(TestEnum.class, "C"));

		try {
			parseUtil.parseEnum(TestEnum.class, "D");
			fail("ParseException expected");
		}
		catch (final ParseException e) {
			assertNotNull(e);
		}

		try {
			parseUtil.parseEnum(TestEnum.class, null);
			fail("ParseException expected");
		}
		catch (final ParseException e) {
			assertNotNull(e);
		}
	}

	@Test
	public void testParseEnumDefault() {
		final ParseUtil parseUtil = new ParseUtilImpl();
		assertEquals(TestEnum.A, parseUtil.parseEnum(TestEnum.class, "A", TestEnum.A));
		assertEquals(TestEnum.B, parseUtil.parseEnum(TestEnum.class, "B", TestEnum.A));
		assertEquals(TestEnum.C, parseUtil.parseEnum(TestEnum.class, "C", TestEnum.A));
		assertEquals(TestEnum.A, parseUtil.parseEnum(TestEnum.class, "D", TestEnum.A));
		assertEquals(TestEnum.A, parseUtil.parseEnum(TestEnum.class, null, TestEnum.A));
	}

	public void testParseUrl() throws Exception {
		final ParseUtil parseUtil = new ParseUtilImpl();
		assertEquals("http://www.google.de", parseUtil.parseURL("http://www.google.de").toExternalForm());
		try {
			parseUtil.parseURL(null);
			fail("ParseException expected");
		}
		catch (final ParseException e) {
		}
		try {
			parseUtil.parseURL("");
			fail("ParseException expected");
		}
		catch (final ParseException e) {
		}
		try {
			parseUtil.parseURL("www.google.de");
			fail("ParseException expected");
		}
		catch (final ParseException e) {
		}
	}

	public void testParseUrlDefault() throws MalformedURLException {
		final ParseUtil parseUtil = new ParseUtilImpl();
		final URL defaultUrl = new URL("http://www.yahoo.de");
		assertEquals("http://www.google.de", parseUtil.parseURL("http://www.google.de", defaultUrl).toExternalForm());
		assertEquals(defaultUrl.toExternalForm(), parseUtil.parseURL(null, defaultUrl).toExternalForm());
		assertEquals(defaultUrl.toExternalForm(), parseUtil.parseURL("", defaultUrl).toExternalForm());
		assertEquals(defaultUrl.toExternalForm(), parseUtil.parseURL("www.google.de", defaultUrl).toExternalForm());
	}
}

enum TestEnum {
	A,
	B,
	C
}
