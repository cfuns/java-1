package de.benjaminborbe.authentication.core.util;

import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.authentication.core.dao.SessionBean;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

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
