package de.benjaminborbe.poker.gamecreator;

import de.benjaminborbe.lib.test.mock.EasyMockHelper;
import de.benjaminborbe.lib.validation.ValidationExecutor;
import de.benjaminborbe.lib.validation.ValidationResultImpl;
import de.benjaminborbe.poker.api.PokerGameDto;
import de.benjaminborbe.poker.api.PokerGameIdentifier;
import de.benjaminborbe.poker.config.PokerCoreConfig;
import de.benjaminborbe.poker.game.PokerGameBean;
import de.benjaminborbe.poker.game.PokerGameDao;
import de.benjaminborbe.poker.game.PokerGameIdentifierGenerator;
import org.easymock.EasyMock;
import org.junit.Test;
import org.slf4j.Logger;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class PokerGameCreatorIntegrationTest {

	private final EasyMockHelper easyMockHelper = new EasyMockHelper();

	@Test
	public void testCreateGame() throws Exception {
		final Logger logger = easyMockHelper.createNiceMock(Logger.class);
		final PokerGameIdentifier pokerGameIdentifier = new PokerGameIdentifier("123");
		final PokerGameIdentifierGenerator pokerGameIdentifierGenerator = easyMockHelper.createMock(PokerGameIdentifierGenerator.class);
		EasyMock.expect(pokerGameIdentifierGenerator.nextId()).andReturn(pokerGameIdentifier);
		final PokerGameDao pokerGameDao = easyMockHelper.createNiceMock(PokerGameDao.class);
		final PokerGameBean pokerGameBean = new PokerGameBean();
		EasyMock.expect(pokerGameDao.create()).andReturn(pokerGameBean);
		final PokerCoreConfig pokerCoreConfig = easyMockHelper.createNiceMock(PokerCoreConfig.class);
		final ValidationExecutor validationExecutor = easyMockHelper.createMock(ValidationExecutor.class);
		EasyMock.expect(validationExecutor.validate(pokerGameBean)).andReturn(new ValidationResultImpl());

		easyMockHelper.replay();

		final PokerGameCreator pokerGameCreator = new PokerGameCreator(logger, pokerGameDao, pokerGameIdentifierGenerator, pokerCoreConfig, validationExecutor);
		final PokerGameDto pokerGameDto = new PokerGameDto();
		assertThat(pokerGameCreator.createGame(pokerGameDto), is(pokerGameIdentifier));

		easyMockHelper.verify();
	}

	@Test
	public void testCreateDefaultGame() throws Exception {
		final Logger logger = easyMockHelper.createNiceMock(Logger.class);
		final PokerGameIdentifier pokerGameIdentifier = new PokerGameIdentifier("123");
		final PokerGameIdentifierGenerator pokerGameIdentifierGenerator = easyMockHelper.createMock(PokerGameIdentifierGenerator.class);
		EasyMock.expect(pokerGameIdentifierGenerator.nextId()).andReturn(pokerGameIdentifier);
		final PokerGameDao pokerGameDao = easyMockHelper.createNiceMock(PokerGameDao.class);
		final PokerGameBean pokerGameBean = new PokerGameBean();
		EasyMock.expect(pokerGameDao.create()).andReturn(pokerGameBean);
		final PokerCoreConfig pokerCoreConfig = easyMockHelper.createNiceMock(PokerCoreConfig.class);
		final ValidationExecutor validationExecutor = easyMockHelper.createMock(ValidationExecutor.class);
		EasyMock.expect(validationExecutor.validate(pokerGameBean)).andReturn(new ValidationResultImpl());

		easyMockHelper.replay();

		final PokerGameCreator pokerGameCreator = new PokerGameCreator(logger, pokerGameDao, pokerGameIdentifierGenerator, pokerCoreConfig, validationExecutor);

		assertThat(pokerGameCreator.createDefaultGame(), is(pokerGameIdentifier));

		easyMockHelper.verify();
	}
}
