package de.benjaminborbe.authentication.api;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class UserIdentifierTest {

	@Test
	public void testToString() {
		final String username = "foo";
		final UserIdentifier ui = new UserIdentifier(username);
		assertEquals(username, ui.getId());
		assertEquals(username, ui.toString());
	}

}
