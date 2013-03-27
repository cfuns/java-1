package de.benjaminborbe.tools.mapper;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class MapperListStringUnitTest {

	public class T {

		private List<String> field;

		public List<String> getField() {
			return field;
		}

		public void setField(final List<String> field) {
			this.field = field;
		}

	}

	@Test
	public void testMap() throws Exception {
		{
			final List<String> values = Arrays.asList("a", "b");
			final MapperListString map = new MapperListString();
			final String valuesString = map.toString(values);
			assertEquals(values, map.fromString(valuesString));
		}
		{
			final List<String> values = new ArrayList<String>();
			final MapperListString map = new MapperListString();
			final String valuesString = map.toString(values);
			assertEquals(values, map.fromString(valuesString));
		}
		{
			final List<String> values = Arrays.asList("a", "b,c", "\\", ",");
			final MapperListString map = new MapperListString();
			final String valuesString = map.toString(values);
			assertEquals(values, map.fromString(valuesString));
		}
	}

	@Test
	public void testToString() throws Exception {
		final MapperListString mapper = new MapperListString();
		assertThat(mapper.toString(null), is(nullValue()));
		assertThat(mapper.toString(new ArrayList<String>()), is(nullValue()));
		assertThat(mapper.toString(Arrays.asList("")), is(""));
		assertThat(mapper.toString(Arrays.asList("", "")), is(","));
		assertThat(mapper.toString(Arrays.asList("a")), is("a"));
		assertThat(mapper.toString(Arrays.asList(",")), is("\\,"));
		assertThat(mapper.toString(Arrays.asList("\\")), is("\\\\"));
	}

	@Test
	public void testfromString() throws Exception {
		final MapperListString mapper = new MapperListString();
		assertThat(mapper.fromString(null), is(asList()));
		assertThat(mapper.fromString(""), is(asList("")));
		assertThat(mapper.fromString("a"), is(asList("a")));
		assertThat(mapper.fromString("\\,"), is(asList(",")));
		assertThat(mapper.fromString("\\\\"), is(asList("\\")));
		assertThat(mapper.fromString("\\\\,"), is(asList("\\", "")));
	}

	private List<String> asList(final String... values) {
		return Arrays.asList(values);
	}
}
