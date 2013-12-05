package de.benjaminborbe.poker.util;

import de.benjaminborbe.poker.api.PokerGameIdentifier;
import de.benjaminborbe.poker.api.PokerPlayerIdentifier;
import de.benjaminborbe.poker.api.PokerService;
import de.benjaminborbe.poker.game.PokerGameBean;
import org.easymock.EasyMock;
import org.junit.Test;
import org.slf4j.Logger;

import java.util.Calendar;

public class PokerAutoFoldGameUnitTest {

	@Test
	public void testGameNotRunning() throws Exception {
		final Logger logger = EasyMock.createNiceMock(Logger.class);
		final PokerService pokerService = EasyMock.createMock(PokerService.class);
		final PokerGameBean pokerGameBean = EasyMock.createMock(PokerGameBean.class);
		final PokerGameIdentifier pokerGameIdentifier = EasyMock.createMock(PokerGameIdentifier.class);
		final PokerTimecheck pokerTimecheck = EasyMock.createMock(PokerTimecheck.class);
		EasyMock.expect(pokerGameBean.getId()).andReturn(pokerGameIdentifier);
		EasyMock.expect(pokerGameBean.getRunning()).andReturn(false);

		final Object[] mocks = new Object[]{pokerService, pokerGameBean, logger, pokerGameIdentifier, pokerTimecheck};
		EasyMock.replay(mocks);

		final PokerAutoFoldGame pokerAutoFoldGame = new PokerAutoFoldGame(logger, pokerService, pokerTimecheck);
		pokerAutoFoldGame.processGame(pokerGameBean);

		EasyMock.verify(mocks);
	}

	@Test
	public void testGameRunningTimeoutNotReached() throws Exception {
		final Long autoCallTimeout = 123L;
		final Long time = 10000L;
		final Long activePositionTime = 10000L;
		final Calendar getActivePosition = EasyMock.createMock(Calendar.class);
		final Logger logger = EasyMock.createNiceMock(Logger.class);
		final PokerService pokerService = EasyMock.createMock(PokerService.class);
		final PokerGameBean pokerGameBean = EasyMock.createMock(PokerGameBean.class);
		final PokerGameIdentifier pokerGameIdentifier = EasyMock.createMock(PokerGameIdentifier.class);
		final PokerPlayerIdentifier playerId = EasyMock.createMock(PokerPlayerIdentifier.class);
		final PokerTimecheck pokerTimecheck = EasyMock.createMock(PokerTimecheck.class);

		EasyMock.expect(pokerGameBean.getId()).andReturn(pokerGameIdentifier);
		EasyMock.expect(pokerGameBean.getRunning()).andReturn(true);
		EasyMock.expect(pokerGameBean.getActivePositionTime()).andReturn(getActivePosition);
		EasyMock.expect(pokerGameBean.getAutoFoldTimeout()).andReturn(autoCallTimeout);
		EasyMock.expect(pokerTimecheck.timeoutReached(getActivePosition, autoCallTimeout)).andReturn(false);

		final Object[] mocks = new Object[]{pokerService, pokerGameBean, logger, pokerGameIdentifier, getActivePosition, playerId};
		EasyMock.replay(mocks);

		final PokerAutoFoldGame pokerAutoFoldGame = new PokerAutoFoldGame(logger, pokerService, pokerTimecheck);
		pokerAutoFoldGame.processGame(pokerGameBean);

		EasyMock.verify(mocks);
	}

	@Test
	public void testGameRunningTimeoutReached() throws Exception {
		final Long autoCallTimeout = 123L;
		final Long time = 10000L;
		final Long activePositionTime = 9000L;
		final Calendar getActivePosition = EasyMock.createMock(Calendar.class);
		final Logger logger = EasyMock.createNiceMock(Logger.class);
		final PokerService pokerService = EasyMock.createMock(PokerService.class);
		final PokerGameBean pokerGameBean = EasyMock.createMock(PokerGameBean.class);
		final PokerGameIdentifier pokerGameIdentifier = EasyMock.createMock(PokerGameIdentifier.class);
		final PokerPlayerIdentifier playerId = EasyMock.createMock(PokerPlayerIdentifier.class);
		final PokerTimecheck pokerTimecheck = EasyMock.createMock(PokerTimecheck.class);

		EasyMock.expect(pokerGameBean.getId()).andReturn(pokerGameIdentifier);
		EasyMock.expect(pokerGameBean.getRunning()).andReturn(true);
		EasyMock.expect(pokerGameBean.getActivePositionTime()).andReturn(getActivePosition);
		EasyMock.expect(pokerGameBean.getAutoFoldTimeout()).andReturn(autoCallTimeout);
		EasyMock.expect(pokerTimecheck.timeoutReached(getActivePosition, autoCallTimeout)).andReturn(true);
		EasyMock.expect(pokerService.getActivePlayer(pokerGameIdentifier)).andReturn(playerId);
		pokerService.fold(pokerGameIdentifier, playerId);

		final Object[] mocks = new Object[]{pokerService, pokerGameBean, logger, pokerGameIdentifier, getActivePosition, playerId, pokerTimecheck};
		EasyMock.replay(mocks);

		final PokerAutoFoldGame pokerAutoFoldGame = new PokerAutoFoldGame(logger, pokerService, pokerTimecheck);
		pokerAutoFoldGame.processGame(pokerGameBean);

		EasyMock.verify(mocks);
	}
}
