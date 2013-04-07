package de.benjaminborbe.virt.api;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class VirtIpAddressUnitTest {

	@Test
	public void testEquals() throws Exception {
		final VirtIpAddress virtIpAddress = new VirtIpAddress(1, 2, 3, 4);
		assertThat(virtIpAddress.equals(virtIpAddress), is(true));
		assertThat(new VirtIpAddress(1, 2, 3, 4).equals(new VirtIpAddress(1, 2, 3, 4)), is(true));
		assertThat(new VirtIpAddress(1, 2, 3, 4).equals(new VirtIpAddress(5, 4, 3, 2)), is(false));
	}
}
