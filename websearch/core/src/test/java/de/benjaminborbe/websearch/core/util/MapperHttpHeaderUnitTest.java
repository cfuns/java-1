package de.benjaminborbe.websearch.core.util;

import de.benjaminborbe.httpdownloader.api.HttpHeader;
import org.easymock.EasyMock;
import org.junit.Test;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class MapperHttpHeaderUnitTest {

	@Test
	public void testToString() throws Exception {
		MapperHttpHeader mapper = new MapperHttpHeader();

		HttpHeader httpHeader = EasyMock.createMock(HttpHeader.class);
		EasyMock.expect(httpHeader.getKeys()).andReturn(Arrays.asList("keyA", "keyB"));
		EasyMock.expect(httpHeader.getValues("keyA")).andReturn(Arrays.asList("valueA1", "valueA2"));
		EasyMock.expect(httpHeader.getValues("keyB")).andReturn(Arrays.asList("valueB1", "valueB2"));
		EasyMock.replay(httpHeader);

		final String string = mapper.toString(httpHeader);
		assertThat(string, is(notNullValue()));
		assertThat(string, is("{\"keyA\":[\"valueA1\",\"valueA2\"],\"keyB\":[\"valueB1\",\"valueB2\"]}"));
	}

	@Test
	public void testToStringNullKey() throws Exception {
		MapperHttpHeader mapper = new MapperHttpHeader();

		HttpHeader httpHeader = EasyMock.createMock(HttpHeader.class);
		EasyMock.expect(httpHeader.getKeys()).andReturn(Arrays.asList("keyA", null, "keyB"));
		EasyMock.expect(httpHeader.getValues("keyA")).andReturn(Arrays.asList("valueA1", "valueA2"));
		EasyMock.expect(httpHeader.getValues("keyB")).andReturn(Arrays.asList("valueB1", "valueB2"));
		EasyMock.replay(httpHeader);

		final String string = mapper.toString(httpHeader);
		assertThat(string, is(notNullValue()));
		assertThat(string, is("{\"keyA\":[\"valueA1\",\"valueA2\"],\"keyB\":[\"valueB1\",\"valueB2\"]}"));
	}

	@Test
	public void testFromString() throws Exception {
		MapperHttpHeader mapper = new MapperHttpHeader();

		String string = "{\"keyA\":[\"valueA1\",\"valueA2\"],\"keyB\":[\"valueB1\",\"valueB2\"]}";
		final HttpHeader httpHeader = mapper.fromString(string);
		assertThat(httpHeader, is(notNullValue()));
		assertThat(httpHeader.getKeys(), is(notNullValue()));
		assertThat(httpHeader.getKeys().size(), is(2));
		assertThat(httpHeader.getKeys(), is(hasItem("keyA")));
		assertThat(httpHeader.getKeys(), is(hasItem("keyB")));

		assertThat(httpHeader.getValues("keyA"), is(notNullValue()));
		assertThat(httpHeader.getValues("keyA").size(), is(2));
		assertThat(httpHeader.getValues("keyA"), is(hasItem("valueA1")));
		assertThat(httpHeader.getValues("keyA"), is(hasItem("valueA2")));

		assertThat(httpHeader.getValues("keyB"), is(notNullValue()));
		assertThat(httpHeader.getValues("keyB").size(), is(2));
		assertThat(httpHeader.getValues("keyB").size(), is(2));
		assertThat(httpHeader.getValues("keyB"), is(hasItem("valueB1")));
		assertThat(httpHeader.getValues("keyB"), is(hasItem("valueB2")));
	}

}
