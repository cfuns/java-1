package de.benjaminborbe.poker.gamecreator;

import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.api.ValidationResult;
import de.benjaminborbe.lib.validation.ValidationExecutor;
import de.benjaminborbe.poker.api.PokerGameDto;
import de.benjaminborbe.poker.api.PokerGameIdentifier;
import de.benjaminborbe.poker.config.PokerCoreConfig;
import de.benjaminborbe.poker.game.PokerGameBean;
import de.benjaminborbe.poker.game.PokerGameDao;
import de.benjaminborbe.poker.game.PokerGameIdentifierGenerator;
import de.benjaminborbe.storage.api.StorageException;
import org.slf4j.Logger;

import javax.inject.Inject;

public class PokerGameCreator {

	public static final boolean DEFAULT_AUTO_JOIN_AND_RESTART = true;

	private static final Long DEFAULT_START_CREDITS = 10000L;

	private static final String DEFAULT_POKER_GAME_NAME = "Game 1";

	private static final Long DEFAULT_BIG_BLIND = 100L;

	private static final Long DEFAULT_AUTOFOLD_TIMEOUT = 1000L;

	private final Logger logger;

	private final PokerGameDao pokerGameDao;

	private final PokerGameIdentifierGenerator pokerGameIdentifierGenerator;

	private final PokerCoreConfig pokerCoreConfig;

	private final ValidationExecutor validationExecutor;

	@Inject
	public PokerGameCreator(
		final Logger logger,
		final PokerGameDao pokerGameDao,
		final PokerGameIdentifierGenerator pokerGameIdentifierGenerator,
		final PokerCoreConfig pokerCoreConfig,
		final ValidationExecutor validationExecutor
	) {
		this.logger = logger;
		this.pokerGameDao = pokerGameDao;
		this.pokerGameIdentifierGenerator = pokerGameIdentifierGenerator;
		this.pokerCoreConfig = pokerCoreConfig;
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
		bean.setStartCredits(pokerGameDto.getStartCredits());
		bean.setAutoJoinAndRestart(pokerGameDto.getAutoJoinAndRestart());

		bean.setSmallBlind(pokerGameDto.getBigBlind() != null ? (pokerGameDto.getBigBlind() / 2) : null);
		bean.setMaxBid(pokerGameDto.getMaxBid() != null ? pokerGameDto.getMaxBid() : pokerCoreConfig.getMaxBid());
		bean.setAutoFoldTimeout(pokerGameDto.getAutoFoldTimeout() != null ? pokerGameDto.getAutoFoldTimeout() : pokerCoreConfig.getAutoFoldTimeout());
		bean.setMaxRaiseFactor(pokerGameDto.getMaxRaiseFactor() != null ? pokerGameDto.getMaxRaiseFactor() : pokerCoreConfig.getMaxRaiseFactor());
		bean.setMinRaiseFactor(pokerGameDto.getMinRaiseFactor() != null ? pokerGameDto.getMinRaiseFactor() : pokerCoreConfig.getMinRaiseFactor());
		bean.setCreditsNegativeAllowed(pokerGameDto.getCreditsNegativeAllowed() != null ? pokerGameDto.getCreditsNegativeAllowed() : pokerCoreConfig.isCreditsNegativeAllowed());

		bean.setRunning(false);
		bean.setPot(0l);
		bean.setCardPosition(0);
		bean.setScore(0l);
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
		pokerGameDto.setBigBlind(DEFAULT_BIG_BLIND);
		pokerGameDto.setAutoFoldTimeout(DEFAULT_AUTOFOLD_TIMEOUT);
		pokerGameDto.setAutoJoinAndRestart(DEFAULT_AUTO_JOIN_AND_RESTART);
		return createGame(pokerGameDto);
	}
}
