package de.benjaminborbe.virt.core.util;

import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.tools.util.ParseUtilImpl;
import de.benjaminborbe.virt.api.VirtIpAddress;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class MapperVirtIpAddressUnitTest {

	@Test
	public void testFromString() throws Exception {
		final ParseUtil parseUtil = new ParseUtilImpl();
		final MapperVirtIpAddress mapper = new MapperVirtIpAddress(parseUtil);
		assertThat(mapper.fromString(null), is(nullValue()));
		assertThat(mapper.fromString("1.2.3.4"), is(notNullValue()));
		assertThat(mapper.fromString("1.2.3.4"), is(new VirtIpAddress(1, 2, 3, 4)));
	}

	@Test
	public void testToString() throws Exception {
		final ParseUtil parseUtil = new ParseUtilImpl();
		final MapperVirtIpAddress mapper = new MapperVirtIpAddress(parseUtil);
		assertThat(mapper.toString(null), is(nullValue()));
		assertThat(mapper.toString(new VirtIpAddress(1, 2, 3, 4)), is(notNullValue()));
		assertThat(mapper.toString(new VirtIpAddress(1, 2, 3, 4)), is("1.2.3.4"));
	}
}
