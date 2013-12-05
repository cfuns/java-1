package de.benjaminborbe.poker.util;

import de.benjaminborbe.poker.game.PokerGameBean;
import de.benjaminborbe.poker.game.PokerGameDao;
import de.benjaminborbe.storage.tools.EntityIteratorList;
import org.easymock.EasyMock;
import org.junit.Test;
import org.slf4j.Logger;

public class PokerAutoCallAllGamesUnitTest {

	@Test
	public void testProcessAllGamesNoGames() throws Exception {
		final Logger logger = EasyMock.createNiceMock(Logger.class);
		final PokerGameDao pokerGameDao = EasyMock.createMock(PokerGameDao.class);
		final PokerAutoCallGame pokerAutoCallGame = EasyMock.createMock(PokerAutoCallGame.class);
		EasyMock.expect(pokerGameDao.getEntityIterator()).andReturn(new EntityIteratorList<PokerGameBean>());

		final Object[] mocks = new Object[]{logger, pokerAutoCallGame, pokerGameDao};
		EasyMock.replay(mocks);

		PokerAutoCallAllGames pokerAutoCallAllGames = new PokerAutoCallAllGames(logger, pokerGameDao, pokerAutoCallGame);
		pokerAutoCallAllGames.processAllGames();

		EasyMock.verify(mocks);
	}

	@Test
	public void testProcessAllGamesOneGames() throws Exception {
		final Logger logger = EasyMock.createNiceMock(Logger.class);
		final PokerGameDao pokerGameDao = EasyMock.createMock(PokerGameDao.class);
		final PokerAutoCallGame pokerAutoCallGame = EasyMock.createMock(PokerAutoCallGame.class);
		final PokerGameBean pokerGameBean = EasyMock.createMock(PokerGameBean.class);
		EasyMock.expect(pokerGameDao.getEntityIterator()).andReturn(new EntityIteratorList<PokerGameBean>(pokerGameBean));
		pokerAutoCallGame.processGame(pokerGameBean);

		final Object[] mocks = new Object[]{logger, pokerAutoCallGame, pokerGameDao, pokerGameBean};
		EasyMock.replay(mocks);

		PokerAutoCallAllGames pokerAutoCallAllGames = new PokerAutoCallAllGames(logger, pokerGameDao, pokerAutoCallGame);
		pokerAutoCallAllGames.processAllGames();

		EasyMock.verify(mocks);
	}
}
