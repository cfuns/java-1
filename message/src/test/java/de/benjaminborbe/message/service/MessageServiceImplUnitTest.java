package de.benjaminborbe.message.service;

import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.authorization.api.AuthorizationServiceException;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.message.api.MessageServiceException;
import de.benjaminborbe.message.util.MessageLock;
import org.easymock.EasyMock;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class MessageServiceImplUnitTest {

	@Test
	public void testGetLockName() throws Exception {
		String lockName = "testLock";
		SessionIdentifier sessionIdentifier = new SessionIdentifier("sid");

		AuthorizationService authorizationService = EasyMock.createMock(AuthorizationService.class);
		authorizationService.expectAdminRole(sessionIdentifier);
		EasyMock.replay(authorizationService);

		MessageLock messageLock = EasyMock.createMock(MessageLock.class);
		EasyMock.expect(messageLock.getLockName()).andReturn(lockName);
		EasyMock.replay(messageLock);

		MessageServiceImpl messageServiceImpl = new MessageServiceImpl(null, messageLock, null, null, null, null, null, null, authorizationService, null);
		assertThat(messageServiceImpl.getLockName(sessionIdentifier), is(lockName));
	}
}
