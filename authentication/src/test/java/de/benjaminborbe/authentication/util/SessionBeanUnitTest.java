package de.benjaminborbe.authentication.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.authentication.dao.SessionBean;

public class SessionBeanUnitTest {

	@Test
	public void testId() throws Exception {
		final String sessionId = "abc";
		final SessionBean session = new SessionBean();
		assertNull(session.getId());
		session.setId(new SessionIdentifier(sessionId));
		assertEquals(sessionId, session.getId().getId());
	}

	@Test
	public void testCurrentUser() throws Exception {
		final String currentUser = "abc";
		final SessionBean session = new SessionBean();
		assertNull(session.getCurrentUser());
		session.setCurrentUser(new UserIdentifier(currentUser));
		assertEquals(currentUser, session.getCurrentUser().getId());
	}

}
