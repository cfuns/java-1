package de.benjaminborbe.poker.gamecreator;

import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.api.ValidationResult;
import de.benjaminborbe.lib.validation.ValidationExecutor;
import de.benjaminborbe.poker.api.PokerGameDto;
import de.benjaminborbe.poker.api.PokerGameIdentifier;
import de.benjaminborbe.poker.config.PokerConfig;
import de.benjaminborbe.poker.game.PokerGameBean;
import de.benjaminborbe.poker.game.PokerGameDao;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.tools.util.IdGeneratorUUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

public class PokerGameCreator {

	private static final Logger logger = LoggerFactory.getLogger(PokerGameCreator.class);

	private final PokerGameDao pokerGameDao;

	private final IdGeneratorUUID idGeneratorUUID;

	private final PokerConfig pokerConfig;

	private final ValidationExecutor validationExecutor;

	@Inject
	public PokerGameCreator(
		final PokerGameDao pokerGameDao,
		final IdGeneratorUUID idGeneratorUUID,
		PokerConfig pokerConfig,
		ValidationExecutor validationExecutor
	) {
		this.pokerGameDao = pokerGameDao;
		this.idGeneratorUUID = idGeneratorUUID;
		this.pokerConfig = pokerConfig;
		this.validationExecutor = validationExecutor;
	}

	public PokerGameIdentifier createGame(final PokerGameDto pokerGameDto) throws StorageException, ValidationException {
		final PokerGameIdentifier id = new PokerGameIdentifier(idGeneratorUUID.nextId());

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

		final ValidationResult errors = validationExecutor.validate(bean);
		if (errors.hasErrors()) {
			logger.warn(bean.getClass().getSimpleName() + " " + errors.toString());
			throw new ValidationException(errors);
		}

		pokerGameDao.save(bean);

		return id;
	}
}
