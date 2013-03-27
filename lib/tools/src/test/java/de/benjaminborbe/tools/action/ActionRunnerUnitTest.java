package de.benjaminborbe.tools.action;

import org.easymock.EasyMock;
import org.junit.Test;
import org.slf4j.Logger;

public class ActionRunnerUnitTest {

	@Test
	public void testRunSuccess() throws Exception {
		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);

		final Action action = EasyMock.createMock(Action.class);
		EasyMock.expect(action.validateExecuteResult()).andReturn(false);
		action.executeOnce();
		EasyMock.expect(action.getWaitTimeout()).andReturn(0l);
		EasyMock.expect(action.getRetryDelay()).andReturn(0l);
		EasyMock.expect(action.validateExecuteResult()).andReturn(true);
		action.onSuccess();
		EasyMock.replay(action);

		final ActionRunner actionRunner = new ActionRunner(logger);
		actionRunner.run(action);

		EasyMock.verify(action);
	}

	@Test
	public void testRunFailure() throws Exception {
		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);

		final Action action = EasyMock.createMock(Action.class);
		EasyMock.expect(action.validateExecuteResult()).andReturn(false);
		action.executeOnce();
		EasyMock.expect(action.getWaitTimeout()).andReturn(0l);
		EasyMock.expect(action.getRetryDelay()).andReturn(0l);
		EasyMock.expect(action.validateExecuteResult()).andReturn(false);
		action.onFailure();
		EasyMock.replay(action);

		final ActionRunner actionRunner = new ActionRunner(logger);
		actionRunner.run(action);

		EasyMock.verify(action);
	}

	@Test
	public void testRunSuccessOneRetry() throws Exception {
		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);

		final Action action = EasyMock.createMock(Action.class);
		action.executeOnce();
		EasyMock.expect(action.getWaitTimeout()).andReturn(1l);
		EasyMock.expect(action.getRetryDelay()).andReturn(1l);
		EasyMock.expect(action.validateExecuteResult()).andReturn(false);
		EasyMock.expect(action.validateExecuteResult()).andReturn(true);
		action.onSuccess();
		EasyMock.replay(action);

		final ActionRunner actionRunner = new ActionRunner(logger);
		actionRunner.run(action);

		EasyMock.verify(action);
	}

	@Test
	public void testRunFailOneRetry() throws Exception {
		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);

		final Action action = EasyMock.createMock(Action.class);
		EasyMock.expect(action.validateExecuteResult()).andReturn(false);
		action.executeOnce();
		EasyMock.expect(action.getWaitTimeout()).andReturn(1l);
		EasyMock.expect(action.getRetryDelay()).andReturn(1l);
		EasyMock.expect(action.validateExecuteResult()).andReturn(false);
		action.executeRetry();
		EasyMock.expect(action.validateExecuteResult()).andReturn(false);
		action.onFailure();
		EasyMock.replay(action);

		final ActionRunner actionRunner = new ActionRunner(logger);
		actionRunner.run(action);

		EasyMock.verify(action);
	}
}
