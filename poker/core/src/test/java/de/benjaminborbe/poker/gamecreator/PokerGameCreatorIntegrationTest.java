package de.benjaminborbe.poker.gamecreator;

import de.benjaminborbe.EasyMockHelper;
import de.benjaminborbe.lib.validation.ValidationExecutor;
import de.benjaminborbe.lib.validation.ValidationResultImpl;
import de.benjaminborbe.poker.api.PokerGameDto;
import de.benjaminborbe.poker.api.PokerGameIdentifier;
import de.benjaminborbe.poker.config.PokerConfig;
import de.benjaminborbe.poker.game.PokerGameBean;
import de.benjaminborbe.poker.game.PokerGameDao;
import de.benjaminborbe.poker.game.PokerGameIdentifierGenerator;
import org.easymock.EasyMock;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class PokerGameCreatorIntegrationTest {

	private final EasyMockHelper easyMockHelper = new EasyMockHelper();

	@Test
	public void testCreateGame() throws Exception {
		final PokerGameIdentifier pokerGameIdentifier = new PokerGameIdentifier("123");
		final PokerGameIdentifierGenerator pokerGameIdentifierGenerator = easyMockHelper.createMock(PokerGameIdentifierGenerator.class);
		EasyMock.expect(pokerGameIdentifierGenerator.nextId()).andReturn(pokerGameIdentifier);
		final PokerGameDao pokerGameDao = easyMockHelper.createNiceMock(PokerGameDao.class);
		final PokerGameBean pokerGameBean = new PokerGameBean();
		EasyMock.expect(pokerGameDao.create()).andReturn(pokerGameBean);
		final PokerConfig pokerConfig = easyMockHelper.createNiceMock(PokerConfig.class);
		final ValidationExecutor validationExecutor = easyMockHelper.createMock(ValidationExecutor.class);
		EasyMock.expect(validationExecutor.validate(pokerGameBean)).andReturn(new ValidationResultImpl());

		easyMockHelper.replay();

		final PokerGameCreator pokerGameCreator = new PokerGameCreator(pokerGameDao, pokerGameIdentifierGenerator, pokerConfig, validationExecutor);
		final PokerGameDto pokerGameDto = new PokerGameDto();
		assertThat(pokerGameCreator.createGame(pokerGameDto), is(pokerGameIdentifier));

		easyMockHelper.verify();
	}

	@Test
	public void testCreateDefaultGame() throws Exception {
		final PokerGameIdentifier pokerGameIdentifier = new PokerGameIdentifier("123");
		final PokerGameIdentifierGenerator pokerGameIdentifierGenerator = easyMockHelper.createMock(PokerGameIdentifierGenerator.class);
		EasyMock.expect(pokerGameIdentifierGenerator.nextId()).andReturn(pokerGameIdentifier);
		final PokerGameDao pokerGameDao = easyMockHelper.createNiceMock(PokerGameDao.class);
		final PokerGameBean pokerGameBean = new PokerGameBean();
		EasyMock.expect(pokerGameDao.create()).andReturn(pokerGameBean);
		final PokerConfig pokerConfig = easyMockHelper.createNiceMock(PokerConfig.class);
		final ValidationExecutor validationExecutor = easyMockHelper.createMock(ValidationExecutor.class);
		EasyMock.expect(validationExecutor.validate(pokerGameBean)).andReturn(new ValidationResultImpl());

		easyMockHelper.replay();

		final PokerGameCreator pokerGameCreator = new PokerGameCreator(pokerGameDao, pokerGameIdentifierGenerator, pokerConfig, validationExecutor);

		assertThat(pokerGameCreator.createDefaultGame(), is(pokerGameIdentifier));

		easyMockHelper.verify();
	}
}
