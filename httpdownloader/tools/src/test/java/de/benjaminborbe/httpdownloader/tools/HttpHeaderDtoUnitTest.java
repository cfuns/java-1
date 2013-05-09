package de.benjaminborbe.httpdownloader.tools;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class HttpHeaderDtoUnitTest {

	@Test
	public void testEquals() throws Exception {
		{
			final HttpHeaderDto a = new HttpHeaderDto();
			assertThat(a.equals(a), is(true));
		}
		{
			final HttpHeaderDto a = new HttpHeaderDto();
			final HttpHeaderDto b = new HttpHeaderDto();
			assertThat(a.equals(b), is(true));
		}
		{
			final HttpHeaderDto a = new HttpHeaderDto();
			a.setHeader("foo", "bar");
			final HttpHeaderDto b = new HttpHeaderDto();
			b.setHeader("foo", "bar");
			assertThat(a.equals(b), is(true));
		}
		{
			final HttpHeaderDto a = new HttpHeaderDto();
			a.setHeader("keyA", "bar");
			final HttpHeaderDto b = new HttpHeaderDto();
			b.setHeader("keyB", "bar");
			assertThat(a.equals(b), is(false));
		}
		{
			final HttpHeaderDto a = new HttpHeaderDto();
			a.setHeader("foo", "valueA");
			final HttpHeaderDto b = new HttpHeaderDto();
			b.setHeader("foo", "valueA");
			assertThat(a.equals(b), is(true));
		}
	}
}
