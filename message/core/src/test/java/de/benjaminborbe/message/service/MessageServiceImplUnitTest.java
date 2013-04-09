package de.benjaminborbe.message.service;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import org.easymock.EasyMock;
import org.junit.Test;

import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.message.util.MessageLock;

public class MessageServiceImplUnitTest {

	@Test
	public void testGetLockName() throws Exception {
		final String lockName = "testLock";
		final SessionIdentifier sessionIdentifier = new SessionIdentifier("sid");

		final AuthorizationService authorizationService = EasyMock.createMock(AuthorizationService.class);
		authorizationService.expectAdminRole(sessionIdentifier);
		EasyMock.replay(authorizationService);

		final MessageLock messageLock = EasyMock.createMock(MessageLock.class);
		EasyMock.expect(messageLock.getLockName()).andReturn(lockName);
		EasyMock.replay(messageLock);

		final MessageServiceImpl messageServiceImpl = new MessageServiceImpl(null, messageLock, null, null, null, null, null, null, authorizationService, null);
		assertThat(messageServiceImpl.getLockName(sessionIdentifier), is(lockName));
	}
}
