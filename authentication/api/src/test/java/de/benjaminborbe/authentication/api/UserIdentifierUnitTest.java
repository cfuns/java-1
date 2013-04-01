package de.benjaminborbe.authentication.api;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UserIdentifierUnitTest {

	@Test
	public void testToString() {
		final String username = "foo";
		final UserIdentifier ui = new UserIdentifier(username);
		assertEquals(username, ui.getId());
		assertEquals(username, ui.toString());
	}

}
