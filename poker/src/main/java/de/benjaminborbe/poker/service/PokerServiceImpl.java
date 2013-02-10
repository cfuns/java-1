package de.benjaminborbe.poker.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.api.ValidationResult;
import de.benjaminborbe.poker.api.PokerCardIdentifier;
import de.benjaminborbe.poker.api.PokerGame;
import de.benjaminborbe.poker.api.PokerGameIdentifier;
import de.benjaminborbe.poker.api.PokerPlayer;
import de.benjaminborbe.poker.api.PokerPlayerIdentifier;
import de.benjaminborbe.poker.api.PokerService;
import de.benjaminborbe.poker.api.PokerServiceException;
import de.benjaminborbe.poker.game.PokerGameBean;
import de.benjaminborbe.poker.game.PokerGameDao;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.tools.IdentifierIterator;
import de.benjaminborbe.storage.tools.IdentifierIteratorException;
import de.benjaminborbe.tools.util.IdGeneratorUUID;
import de.benjaminborbe.tools.validation.ValidationExecutor;

@Singleton
public class PokerServiceImpl implements PokerService {

	private final Logger logger;

	private final PokerGameDao pokerGameDao;

	private final IdGeneratorUUID idGeneratorUUID;

	private final ValidationExecutor validationExecutor;

	@Inject
	public PokerServiceImpl(final Logger logger, final PokerGameDao pokerGameDao, final IdGeneratorUUID idGeneratorUUID, final ValidationExecutor validationExecutor) {
		this.logger = logger;
		this.pokerGameDao = pokerGameDao;
		this.idGeneratorUUID = idGeneratorUUID;
		this.validationExecutor = validationExecutor;
	}

	@Override
	public Collection<PokerGameIdentifier> getGames() throws PokerServiceException {
		try {
			logger.debug("getGames");
			final IdentifierIterator<PokerGameIdentifier> i = pokerGameDao.getIdentifierIterator();
			final List<PokerGameIdentifier> result = new ArrayList<PokerGameIdentifier>();
			while (i.hasNext()) {
				result.add(i.next());
			}
			return result;
		}
		catch (final IdentifierIteratorException e) {
			throw new PokerServiceException(e);
		}
		catch (final StorageException e) {
			throw new PokerServiceException(e);
		}
		finally {
		}
	}

	@Override
	public PokerGame getGame(final PokerGameIdentifier gameIdentifier) throws PokerServiceException {
		try {
			logger.debug("getGame - id: " + gameIdentifier);
			return pokerGameDao.load(gameIdentifier);
		}
		catch (final StorageException e) {
			throw new PokerServiceException(e);
		}
		finally {
		}
	}

	@Override
	public Collection<PokerPlayerIdentifier> getPlayers(final PokerGameIdentifier gameIdentifier) {
		return null;
	}

	@Override
	public PokerPlayer getPlayer(final PokerPlayerIdentifier playerIdentifier) {
		return null;
	}

	@Override
	public void startGame(final PokerGameIdentifier gameIdentifier) {
	}

	@Override
	public PokerGameIdentifier createGameIdentifier(final String gameId) throws PokerServiceException {
		return gameId != null ? new PokerGameIdentifier(gameId) : null;
	}

	@Override
	public Collection<PokerCardIdentifier> getCards(final PokerPlayerIdentifier playerIdentifier) throws PokerServiceException {
		return null;
	}

	@Override
	public Collection<PokerCardIdentifier> getCards(final PokerGameIdentifier gameIdentifier) throws PokerServiceException {
		return null;
	}

	@Override
	public PokerGameIdentifier createGame(final String name) throws PokerServiceException, ValidationException {
		try {
			logger.debug("createGame - name: " + name);

			final PokerGameIdentifier id = new PokerGameIdentifier(idGeneratorUUID.nextId());

			final PokerGameBean bean = pokerGameDao.create();
			bean.setId(id);
			bean.setName(name);

			final ValidationResult errors = validationExecutor.validate(bean);
			if (errors.hasErrors()) {
				logger.warn("TaskContext " + errors.toString());
				throw new ValidationException(errors);
			}

			pokerGameDao.save(bean);

			return id;
		}
		catch (final StorageException e) {
			throw new PokerServiceException(e);
		}
		finally {
		}
	}

	@Override
	public PokerPlayerIdentifier createPlayer(final String name) throws PokerServiceException {
		return null;
	}

	@Override
	public PokerPlayerIdentifier createPlayerIdentifier(final String id) throws PokerServiceException {
		return null;
	}

}
