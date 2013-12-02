package de.benjaminborbe.lib.servlet.mock;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;

public class HttpServletRequestMockTest {

	@Test
	public void testParameterNotExisting() {
		final HttpServletRequestMock httpServletRequestMock = new HttpServletRequestMock();
		assertThat(httpServletRequestMock.getParameter("key"), is(nullValue()));
	}

	@Test
	public void testParameterExisting() {
		final HttpServletRequestMock httpServletRequestMock = new HttpServletRequestMock();
		final String key = "key123";
		final String value = "value123";
		httpServletRequestMock.setParameter(key, value);
		assertThat(httpServletRequestMock.getParameter(key), is(value));
	}
}
