package de.benjaminborbe.authentication.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class SessionBeanTest {

	@Test
	public void testId() throws Exception {
		final String sessionId = "abc";
		final SessionBean session = new SessionBean();
		assertNull(session.getId());
		session.setId(sessionId);
		assertEquals(sessionId, session.getId());
	}

	@Test
	public void testCurrentUser() throws Exception {
		final String currentUser = "abc";
		final SessionBean session = new SessionBean();
		assertNull(session.getCurrentUser());
		session.setCurrentUser(currentUser);
		assertEquals(currentUser, session.getCurrentUser());
	}

}
