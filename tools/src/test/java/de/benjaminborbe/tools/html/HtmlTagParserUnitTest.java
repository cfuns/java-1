package de.benjaminborbe.tools.html;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class HtmlTagParserUnitTest {

	@Test
	public void testParseEmpty() throws Exception {
		final HtmlTagParser htmlTagParser = new HtmlTagParser();
		assertThat(htmlTagParser.parse(null), is(nullValue()));
		assertThat(htmlTagParser.parse(""), is(nullValue()));
		assertThat(htmlTagParser.parse("bla"), is(nullValue()));
	}

	@Test
	public void testParseSimpleTag() throws Exception {
		final HtmlTagParser htmlTagParser = new HtmlTagParser();
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
	}
}
