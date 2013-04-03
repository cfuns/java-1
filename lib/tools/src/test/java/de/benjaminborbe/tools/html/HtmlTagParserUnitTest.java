package de.benjaminborbe.tools.html;

import org.easymock.EasyMock;
import org.junit.Test;
import org.slf4j.Logger;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

public class HtmlTagParserUnitTest {

	@Test
	public void testParseEmpty() throws Exception {
		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);

		final HtmlTagParser htmlTagParser = new HtmlTagParser(logger);
		assertThat(htmlTagParser.parse(null), is(nullValue()));
		assertThat(htmlTagParser.parse(""), is(nullValue()));
		assertThat(htmlTagParser.parse("bla"), is(nullValue()));
		assertThat(htmlTagParser.parse(" <bla>"), is(nullValue()));
		assertThat(htmlTagParser.parse("<bla> "), is(nullValue()));
	}

	@Test
	public void testParseSimpleTag() throws Exception {
		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);

		final HtmlTagParser htmlTagParser = new HtmlTagParser(logger);
		{
			assertThat(htmlTagParser.parse("<br>"), is(not(nullValue())));
			assertThat(htmlTagParser.parse("<br>").getName(), is("br"));
			assertThat(htmlTagParser.parse("<br>").isOpening(), is(true));
			assertThat(htmlTagParser.parse("<br>").isClosing(), is(false));
		}
		{
			assertThat(htmlTagParser.parse("<br >"), is(not(nullValue())));
			assertThat(htmlTagParser.parse("<br >").getName(), is("br"));
			assertThat(htmlTagParser.parse("<br >").isOpening(), is(true));
			assertThat(htmlTagParser.parse("<br >").isClosing(), is(false));
		}
		{
			assertThat(htmlTagParser.parse("<br/>"), is(not(nullValue())));
			assertThat(htmlTagParser.parse("<br/>").getName(), is("br"));
			assertThat(htmlTagParser.parse("<br/>").isOpening(), is(true));
			assertThat(htmlTagParser.parse("<br/>").isClosing(), is(true));
		}
		{
			assertThat(htmlTagParser.parse("</br>"), is(not(nullValue())));
			assertThat(htmlTagParser.parse("</br>").getName(), is("br"));
			assertThat(htmlTagParser.parse("</br>").isOpening(), is(false));
			assertThat(htmlTagParser.parse("</br>").isClosing(), is(true));
		}
		{
			assertThat(htmlTagParser.parse("<br\t>"), is(not(nullValue())));
			assertThat(htmlTagParser.parse("<br\t>").getName(), is("br"));
			assertThat(htmlTagParser.parse("<br\t>").isOpening(), is(true));
			assertThat(htmlTagParser.parse("<br\t>").isClosing(), is(false));
		}
		{
			assertThat(htmlTagParser.parse("<br\r>"), is(not(nullValue())));
			assertThat(htmlTagParser.parse("<br\r>").getName(), is("br"));
			assertThat(htmlTagParser.parse("<br\r>").isOpening(), is(true));
			assertThat(htmlTagParser.parse("<br\r>").isClosing(), is(false));
		}
		{
			assertThat(htmlTagParser.parse("<br\n>"), is(not(nullValue())));
			assertThat(htmlTagParser.parse("<br\n>").getName(), is("br"));
			assertThat(htmlTagParser.parse("<br\n>").isOpening(), is(true));
			assertThat(htmlTagParser.parse("<br\n>").isClosing(), is(false));
		}
	}

	@Test
	public void testParseTagWithAttribute() {
		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);

		final HtmlTagParser htmlTagParser = new HtmlTagParser(logger);
		{
			assertThat(htmlTagParser.parse("<br class=\"foo\">"), is(not(nullValue())));
			assertThat(htmlTagParser.parse("<br class=\"foo\">").getName(), is("br"));
			assertThat(htmlTagParser.parse("<br class=\"foo\">").isOpening(), is(true));
			assertThat(htmlTagParser.parse("<br class=\"foo\">").isClosing(), is(false));
			assertThat(htmlTagParser.parse("<br class=\"foo\">").getAttribute("class"), is("foo"));
		}
		{
			assertThat(htmlTagParser.parse("<br class=\"foo\" >"), is(not(nullValue())));
			assertThat(htmlTagParser.parse("<br class=\"foo\" >").getName(), is("br"));
			assertThat(htmlTagParser.parse("<br class=\"foo\" >").isOpening(), is(true));
			assertThat(htmlTagParser.parse("<br class=\"foo\" >").isClosing(), is(false));
			assertThat(htmlTagParser.parse("<br class=\"foo\" >").getAttribute("class"), is("foo"));
		}
		{
			assertThat(htmlTagParser.parse("<br class='foo'>"), is(not(nullValue())));
			assertThat(htmlTagParser.parse("<br class='foo'>").getName(), is("br"));
			assertThat(htmlTagParser.parse("<br class='foo'>").isOpening(), is(true));
			assertThat(htmlTagParser.parse("<br class='foo'>").isClosing(), is(false));
			assertThat(htmlTagParser.parse("<br class='foo'>").getAttribute("class"), is("foo"));
		}
		{
			assertThat(htmlTagParser.parse("<br class='foo' >"), is(not(nullValue())));
			assertThat(htmlTagParser.parse("<br class='foo' >").getName(), is("br"));
			assertThat(htmlTagParser.parse("<br class='foo' >").isOpening(), is(true));
			assertThat(htmlTagParser.parse("<br class='foo' >").isClosing(), is(false));
			assertThat(htmlTagParser.parse("<br class='foo' >").getAttribute("class"), is("foo"));
		}
		{
			assertThat(htmlTagParser.parse("<br class=foo>"), is(not(nullValue())));
			assertThat(htmlTagParser.parse("<br class=foo'>").getName(), is("br"));
			assertThat(htmlTagParser.parse("<br class=foo>").isOpening(), is(true));
			assertThat(htmlTagParser.parse("<br class=foo>").isClosing(), is(false));
			assertThat(htmlTagParser.parse("<br class=foo>").getAttribute("class"), is("foo"));
		}
		{
			assertThat(htmlTagParser.parse("<br class=foo >"), is(not(nullValue())));
			assertThat(htmlTagParser.parse("<br class=foo >").getName(), is("br"));
			assertThat(htmlTagParser.parse("<br class=foo >").isOpening(), is(true));
			assertThat(htmlTagParser.parse("<br class=foo >").isClosing(), is(false));
			assertThat(htmlTagParser.parse("<br class=foo >").getAttribute("class"), is("foo"));
		}
		{
			assertThat(htmlTagParser.parse("<br class = foo >"), is(not(nullValue())));
			assertThat(htmlTagParser.parse("<br class = foo >").getName(), is("br"));
			assertThat(htmlTagParser.parse("<br class = foo >").isOpening(), is(true));
			assertThat(htmlTagParser.parse("<br class = foo >").isClosing(), is(false));
			assertThat(htmlTagParser.parse("<br class = foo >").getAttribute("class"), is("foo"));
		}
	}

	@Test
	public void testCorruptTagValue() {
		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);

		final HtmlTagParser htmlTagParser = new HtmlTagParser(logger);

		final String content = "<a bla='>";
		assertThat(htmlTagParser.parse(content), is(not(nullValue())));
		assertThat(htmlTagParser.parse(content).getName(), is("a"));
		assertThat(htmlTagParser.parse(content).isOpening(), is(true));
		assertThat(htmlTagParser.parse(content).isClosing(), is(false));
		assertThat(htmlTagParser.parse(content).getAttribute("bla"), is(notNullValue()));
	}
}
