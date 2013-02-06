package de.benjaminborbe.tools.html;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class HtmlContentIteratorUnitTest {

	@Test
	public void testEmptyText() throws Exception {
		{
			final HtmlContentIterator i = new HtmlContentIterator(null);
			assertThat(i.hasNext(), is(false));
			assertThat(i.hasNext(), is(false));
		}
		{
			final HtmlContentIterator i = new HtmlContentIterator("");
			assertThat(i.hasNext(), is(false));
			assertThat(i.hasNext(), is(false));
		}
		{
			final HtmlContentIterator i = new HtmlContentIterator(" ");
			assertThat(i.hasNext(), is(false));
			assertThat(i.hasNext(), is(false));
		}
		{
			final HtmlContentIterator i = new HtmlContentIterator("  ");
			assertThat(i.hasNext(), is(false));
			assertThat(i.hasNext(), is(false));
		}
		{
			final HtmlContentIterator i = new HtmlContentIterator(" \n\t\r ");
			assertThat(i.hasNext(), is(false));
			assertThat(i.hasNext(), is(false));
		}
	}

	@Test
	public void testPlainText() throws Exception {
		{
			final HtmlContentIterator i = new HtmlContentIterator("bla");
			assertThat(i.hasNext(), is(true));
			assertThat(i.hasNext(), is(true));
			assertThat(i.next(), is("bla"));
			assertThat(i.hasNext(), is(false));
			assertThat(i.hasNext(), is(false));
		}
		{
			final HtmlContentIterator i = new HtmlContentIterator("&uuml;");
			assertThat(i.hasNext(), is(true));
			assertThat(i.hasNext(), is(true));
			assertThat(i.next(), is("ü"));
			assertThat(i.hasNext(), is(false));
			assertThat(i.hasNext(), is(false));
		}
		{
			final HtmlContentIterator i = new HtmlContentIterator(" bla ");
			assertThat(i.hasNext(), is(true));
			assertThat(i.hasNext(), is(true));
			assertThat(i.next(), is("bla"));
			assertThat(i.hasNext(), is(false));
			assertThat(i.hasNext(), is(false));
		}
		{
			final HtmlContentIterator i = new HtmlContentIterator("foo baa");
			assertThat(i.hasNext(), is(true));
			assertThat(i.hasNext(), is(true));
			assertThat(i.next(), is("foo"));
			assertThat(i.hasNext(), is(true));
			assertThat(i.hasNext(), is(true));
			assertThat(i.next(), is("baa"));
			assertThat(i.hasNext(), is(false));
			assertThat(i.hasNext(), is(false));
		}
		{
			final HtmlContentIterator i = new HtmlContentIterator(" foo baa ");
			assertThat(i.hasNext(), is(true));
			assertThat(i.hasNext(), is(true));
			assertThat(i.next(), is("foo"));
			assertThat(i.hasNext(), is(true));
			assertThat(i.hasNext(), is(true));
			assertThat(i.next(), is("baa"));
			assertThat(i.hasNext(), is(false));
			assertThat(i.hasNext(), is(false));
		}
	}

	@Test
	public void testSimpleHtmlTag() throws Exception {
		{
			final HtmlContentIterator i = new HtmlContentIterator("<br>");
			assertThat(i.hasNext(), is(false));
			assertThat(i.hasNext(), is(false));
		}
		{
			final HtmlContentIterator i = new HtmlContentIterator("foo<br>bar");
			assertThat(i.hasNext(), is(true));
			assertThat(i.hasNext(), is(true));
			assertThat(i.next(), is("foo"));
			assertThat(i.hasNext(), is(true));
			assertThat(i.hasNext(), is(true));
			assertThat(i.next(), is("bar"));
			assertThat(i.hasNext(), is(false));
			assertThat(i.hasNext(), is(false));
		}
	}
}
