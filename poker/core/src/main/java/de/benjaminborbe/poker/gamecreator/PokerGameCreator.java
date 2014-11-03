package de.benjaminborbe.poker.gamecreator;

import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.api.ValidationResult;
import de.benjaminborbe.lib.validation.ValidationExecutor;
import de.benjaminborbe.poker.api.PokerGameDto;
import de.benjaminborbe.poker.api.PokerGameIdentifier;
import de.benjaminborbe.poker.config.PokerConfig;
import de.benjaminborbe.poker.game.PokerGameBean;
import de.benjaminborbe.poker.game.PokerGameDao;
import de.benjaminborbe.poker.game.PokerGameIdentifierGenerator;
import de.benjaminborbe.storage.api.StorageException;
import org.slf4j.Logger;

import javax.inject.Inject;

public class PokerGameCreator {

	private static final Long DEFAULT_START_CREDITS = 10000L;

	private static final String DEFAULT_POKER_GAME_NAME = "Game 1";

	private final Logger logger;

	private final PokerGameDao pokerGameDao;

	private final PokerGameIdentifierGenerator pokerGameIdentifierGenerator;

	private final PokerConfig pokerConfig;

	private final ValidationExecutor validationExecutor;

	@Inject
	public PokerGameCreator(
		final Logger logger,
		final PokerGameDao pokerGameDao,
		final PokerGameIdentifierGenerator pokerGameIdentifierGenerator,
		final PokerConfig pokerConfig,
		final ValidationExecutor validationExecutor
	) {
		this.logger = logger;
		this.pokerGameDao = pokerGameDao;
		this.pokerGameIdentifierGenerator = pokerGameIdentifierGenerator;
		this.pokerConfig = pokerConfig;
		this.validationExecutor = validationExecutor;
	}

	public PokerGameIdentifier createGame(final PokerGameDto pokerGameDto) throws StorageException, ValidationException {
		final PokerGameIdentifier id = createId();
		final PokerGameBean bean = createBean(pokerGameDto, id);
		validateBean(bean);
		saveBean(bean);
		return id;
	}

	private PokerGameIdentifier createId() {
		return pokerGameIdentifierGenerator.nextId();
	}

	private PokerGameBean createBean(final PokerGameDto pokerGameDto, final PokerGameIdentifier id) throws StorageException {
		final PokerGameBean bean = pokerGameDao.create();
		bean.setId(id);
		bean.setName(pokerGameDto.getName());
		bean.setBigBlind(pokerGameDto.getBigBlind());
		bean.setSmallBlind(pokerGameDto.getBigBlind() != null ? (pokerGameDto.getBigBlind() / 2) : null);
		bean.setStartCredits(pokerGameDto.getStartCredits());
		bean.setAutoJoinAndRestart(pokerGameDto.getAutoJoinAndRestart());
		bean.setRunning(false);
		bean.setPot(0l);
		bean.setCardPosition(0);
		bean.setScore(0l);
		bean.setMaxBid(pokerConfig.getMaxBid());
		bean.setAutoFoldTimeout(pokerConfig.getAutoFoldTimeout());
		bean.setCreditsNegativeAllowed(pokerConfig.isCreditsNegativeAllowed());
		bean.setMaxRaiseFactor(pokerConfig.getMaxRaiseFactor());
		bean.setMinRaiseFactor(pokerConfig.getMinRaiseFactor());
		return bean;
	}

	private void validateBean(final PokerGameBean bean) throws ValidationException {
		final ValidationResult errors = validationExecutor.validate(bean);
		if (errors.hasErrors()) {
			logger.warn(bean.getClass().getSimpleName() + " " + errors.toString());
			throw new ValidationException(errors);
		}
	}

	private void saveBean(final PokerGameBean bean) throws StorageException {
		pokerGameDao.save(bean);
	}

	public PokerGameIdentifier createDefaultGame() throws ValidationException, StorageException {
		final PokerGameDto pokerGameDto = new PokerGameDto();
		pokerGameDto.setName(DEFAULT_POKER_GAME_NAME);
		pokerGameDto.setStartCredits(DEFAULT_START_CREDITS);
		return createGame(pokerGameDto);
	}
}
