package de.benjaminborbe.message.service;

import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.message.util.MessageLock;
import org.easymock.EasyMock;
import org.junit.Test;
import org.slf4j.Logger;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class MessageServiceImplUnitTest {

	@Test
	public void testGetLockName() throws Exception {
		final String lockName = "testLock";
		final SessionIdentifier sessionIdentifier = new SessionIdentifier("sid");
		final MessageLock messageLock = EasyMock.createMock(MessageLock.class);
		final Logger logger = EasyMock.createNiceMock(Logger.class);
		final AuthorizationService authorizationService = EasyMock.createMock(AuthorizationService.class);

		authorizationService.expectAdminRole(sessionIdentifier);
		EasyMock.expect(messageLock.getLockName()).andReturn(lockName);

		final Object[] mocks = new Object[]{logger, messageLock, authorizationService};
		EasyMock.replay(mocks);

		final MessageServiceImpl messageServiceImpl = new MessageServiceImpl(logger, messageLock, null, null, null, null, null, null, authorizationService, null);
		assertThat(messageServiceImpl.getLockName(sessionIdentifier), is(lockName));

		EasyMock.verify(mocks);
	}
}
