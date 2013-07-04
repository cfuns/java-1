package de.benjaminborbe.tools.html;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class HtmlTokenIteratorUnitTest {

	@Test
	public void testEmpty() throws Exception {
		{
			final HtmlTokenIterator i = new HtmlTokenIterator(null);
			assertThat(i.hasNext(), is(false));
			assertThat(i.hasNext(), is(false));
		}
		{
			final HtmlTokenIterator i = new HtmlTokenIterator("");
			assertThat(i.hasNext(), is(false));
			assertThat(i.hasNext(), is(false));
		}
		{
			final HtmlTokenIterator i = new HtmlTokenIterator(" ");
			assertThat(i.hasNext(), is(false));
			assertThat(i.hasNext(), is(false));
		}
		{
			final HtmlTokenIterator i = new HtmlTokenIterator("\r");
			assertThat(i.hasNext(), is(false));
			assertThat(i.hasNext(), is(false));
		}
		{
			final HtmlTokenIterator i = new HtmlTokenIterator("\n");
			assertThat(i.hasNext(), is(false));
			assertThat(i.hasNext(), is(false));
		}
		{
			final HtmlTokenIterator i = new HtmlTokenIterator("\t");
			assertThat(i.hasNext(), is(false));
			assertThat(i.hasNext(), is(false));
		}
	}

	@Test
	public void testSingleWord() throws Exception {
		{
			final HtmlTokenIterator i = new HtmlTokenIterator("foo");
			assertThat(i.hasNext(), is(true));
			assertThat(i.hasNext(), is(true));
			assertThat(i.next(), is("foo"));
			assertThat(i.hasNext(), is(false));
			assertThat(i.hasNext(), is(false));
		}
		{
			final HtmlTokenIterator i = new HtmlTokenIterator(" foo ");
			assertThat(i.hasNext(), is(true));
			assertThat(i.hasNext(), is(true));
			assertThat(i.next(), is("foo"));
			assertThat(i.hasNext(), is(false));
			assertThat(i.hasNext(), is(false));
		}
	}

	@Test
	public void testSingleTag() throws Exception {
		{
			final HtmlTokenIterator i = new HtmlTokenIterator("<br>");
			assertThat(i.hasNext(), is(true));
			assertThat(i.hasNext(), is(true));
			assertThat(i.next(), is("<br>"));
			assertThat(i.hasNext(), is(false));
			assertThat(i.hasNext(), is(false));
		}
		{
			final HtmlTokenIterator i = new HtmlTokenIterator(" <br> ");
			assertThat(i.hasNext(), is(true));
			assertThat(i.hasNext(), is(true));
			assertThat(i.next(), is("<br>"));
			assertThat(i.hasNext(), is(false));
			assertThat(i.hasNext(), is(false));
		}
		{
			final HtmlTokenIterator i = new HtmlTokenIterator("<br/>");
			assertThat(i.hasNext(), is(true));
			assertThat(i.hasNext(), is(true));
			assertThat(i.next(), is("<br/>"));
			assertThat(i.hasNext(), is(false));
			assertThat(i.hasNext(), is(false));
		}
		{
			final HtmlTokenIterator i = new HtmlTokenIterator(" <br/> ");
			assertThat(i.hasNext(), is(true));
			assertThat(i.hasNext(), is(true));
			assertThat(i.next(), is("<br/>"));
			assertThat(i.hasNext(), is(false));
			assertThat(i.hasNext(), is(false));
		}
	}

	@Test
	public void testSingleTagWithAttribute() throws Exception {
		{
			final HtmlTokenIterator i = new HtmlTokenIterator("<br class=\"clear\">");
			assertThat(i.hasNext(), is(true));
			assertThat(i.hasNext(), is(true));
			assertThat(i.next(), is("<br class=\"clear\">"));
			assertThat(i.hasNext(), is(false));
			assertThat(i.hasNext(), is(false));
		}
	}

	@Test
	public void testWordTagMixed() throws Exception {
		{
			final HtmlTokenIterator i = new HtmlTokenIterator("foo<br>");
			assertThat(i.hasNext(), is(true));
			assertThat(i.hasNext(), is(true));
			assertThat(i.next(), is("foo"));
			assertThat(i.hasNext(), is(true));
			assertThat(i.hasNext(), is(true));
			assertThat(i.next(), is("<br>"));
			assertThat(i.hasNext(), is(false));
			assertThat(i.hasNext(), is(false));
		}
		{
			final HtmlTokenIterator i = new HtmlTokenIterator("foo<br>");
			assertThat(i.hasNext(), is(true));
			assertThat(i.hasNext(), is(true));
			assertThat(i.next(), is("foo"));
			assertThat(i.hasNext(), is(true));
			assertThat(i.hasNext(), is(true));
			assertThat(i.next(), is("<br>"));
			assertThat(i.hasNext(), is(false));
			assertThat(i.hasNext(), is(false));
		}
		{
			final HtmlTokenIterator i = new HtmlTokenIterator("foo<br>bar");
			assertThat(i.hasNext(), is(true));
			assertThat(i.hasNext(), is(true));
			assertThat(i.next(), is("foo"));
			assertThat(i.hasNext(), is(true));
			assertThat(i.hasNext(), is(true));
			assertThat(i.next(), is("<br>"));
			assertThat(i.hasNext(), is(true));
			assertThat(i.hasNext(), is(true));
			assertThat(i.next(), is("bar"));
			assertThat(i.hasNext(), is(false));
			assertThat(i.hasNext(), is(false));
		}
		{
			final HtmlTokenIterator i = new HtmlTokenIterator("<br>foo<br>");
			assertThat(i.hasNext(), is(true));
			assertThat(i.hasNext(), is(true));
			assertThat(i.next(), is("<br>"));
			assertThat(i.hasNext(), is(true));
			assertThat(i.hasNext(), is(true));
			assertThat(i.next(), is("foo"));
			assertThat(i.hasNext(), is(true));
			assertThat(i.hasNext(), is(true));
			assertThat(i.next(), is("<br>"));
			assertThat(i.hasNext(), is(false));
			assertThat(i.hasNext(), is(false));
		}
		{
			final HtmlTokenIterator i = new HtmlTokenIterator("<script type=\"syntaxhighlighter\"><![CDATA[hello world]]></script>");
			assertThat(i.hasNext(), is(true));
			assertThat(i.hasNext(), is(true));
			assertThat(i.next(), is("<script type=\"syntaxhighlighter\">"));
			assertThat(i.hasNext(), is(true));
			assertThat(i.hasNext(), is(true));
			assertThat(i.next(), is("<![CDATA[hello world]]>"));
			assertThat(i.hasNext(), is(true));
			assertThat(i.hasNext(), is(true));
			assertThat(i.next(), is("</script>"));
			assertThat(i.hasNext(), is(false));
			assertThat(i.hasNext(), is(false));
		}

	}
}
