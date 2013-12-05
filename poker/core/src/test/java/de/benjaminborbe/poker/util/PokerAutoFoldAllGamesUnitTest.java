package de.benjaminborbe.poker.util;

import de.benjaminborbe.poker.game.PokerGameBean;
import de.benjaminborbe.poker.game.PokerGameDao;
import de.benjaminborbe.storage.tools.EntityIteratorList;
import org.easymock.EasyMock;
import org.junit.Test;
import org.slf4j.Logger;

public class PokerAutoFoldAllGamesUnitTest {

	@Test
	public void testProcessAllGamesNoGames() throws Exception {
		final Logger logger = EasyMock.createNiceMock(Logger.class);
		final PokerGameDao pokerGameDao = EasyMock.createMock(PokerGameDao.class);
		final PokerAutoFoldGame pokerAutoFoldGame = EasyMock.createMock(PokerAutoFoldGame.class);
		EasyMock.expect(pokerGameDao.getEntityIterator()).andReturn(new EntityIteratorList<PokerGameBean>());

		final Object[] mocks = new Object[]{logger, pokerAutoFoldGame, pokerGameDao};
		EasyMock.replay(mocks);

		final PokerAutoFoldAllGames pokerAutoFoldAllGames = new PokerAutoFoldAllGames(logger, pokerGameDao, pokerAutoFoldGame);
		pokerAutoFoldAllGames.processAllGames();

		EasyMock.verify(mocks);
	}

	@Test
	public void testProcessAllGamesOneGames() throws Exception {
		final Logger logger = EasyMock.createNiceMock(Logger.class);
		final PokerGameDao pokerGameDao = EasyMock.createMock(PokerGameDao.class);
		final PokerAutoFoldGame pokerAutoFoldGame = EasyMock.createMock(PokerAutoFoldGame.class);
		final PokerGameBean pokerGameBean = EasyMock.createMock(PokerGameBean.class);
		EasyMock.expect(pokerGameDao.getEntityIterator()).andReturn(new EntityIteratorList<PokerGameBean>(pokerGameBean));
		pokerAutoFoldGame.processGame(pokerGameBean);

		final Object[] mocks = new Object[]{logger, pokerAutoFoldGame, pokerGameDao, pokerGameBean};
		EasyMock.replay(mocks);

		final PokerAutoFoldAllGames pokerAutoFoldAllGames = new PokerAutoFoldAllGames(logger, pokerGameDao, pokerAutoFoldGame);
		pokerAutoFoldAllGames.processAllGames();

		EasyMock.verify(mocks);
	}
}
