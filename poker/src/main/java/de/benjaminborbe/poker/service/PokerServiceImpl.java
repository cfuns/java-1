package de.benjaminborbe.poker.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.api.ValidationErrorSimple;
import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.api.ValidationResult;
import de.benjaminborbe.poker.api.PokerCardIdentifier;
import de.benjaminborbe.poker.api.PokerGame;
import de.benjaminborbe.poker.api.PokerGameIdentifier;
import de.benjaminborbe.poker.api.PokerPlayer;
import de.benjaminborbe.poker.api.PokerPlayerIdentifier;
import de.benjaminborbe.poker.api.PokerService;
import de.benjaminborbe.poker.api.PokerServiceException;
import de.benjaminborbe.poker.card.PokerCardFactory;
import de.benjaminborbe.poker.game.PokerGameBean;
import de.benjaminborbe.poker.game.PokerGameDao;
import de.benjaminborbe.poker.player.PokerPlayerBean;
import de.benjaminborbe.poker.player.PokerPlayerDao;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.tools.IdentifierIterator;
import de.benjaminborbe.storage.tools.IdentifierIteratorException;
import de.benjaminborbe.storage.tools.StorageValueList;
import de.benjaminborbe.tools.list.ListUtil;
import de.benjaminborbe.tools.util.IdGeneratorUUID;
import de.benjaminborbe.tools.validation.ValidationExecutor;
import de.benjaminborbe.tools.validation.ValidationResultImpl;

@Singleton
public class PokerServiceImpl implements PokerService {

	private static final int START_CARDS = 2;

	private static final long START_CREDITS = 1000l;

	private final Logger logger;

	private final PokerGameDao pokerGameDao;

	private final IdGeneratorUUID idGeneratorUUID;

	private final ValidationExecutor validationExecutor;

	private final PokerPlayerDao pokerPlayerDao;

	private final ListUtil listUtil;

	private final PokerCardFactory pokerCardFactory;

	@Inject
	public PokerServiceImpl(
			final Logger logger,
			final ListUtil listUtil,
			final PokerCardFactory pokerCardFactory,
			final PokerGameDao pokerGameDao,
			final IdGeneratorUUID idGeneratorUUID,
			final ValidationExecutor validationExecutor,
			final PokerPlayerDao pokerPlayerDao) {
		this.logger = logger;
		this.pokerCardFactory = pokerCardFactory;
		this.listUtil = listUtil;
		this.pokerGameDao = pokerGameDao;
		this.idGeneratorUUID = idGeneratorUUID;
		this.validationExecutor = validationExecutor;
		this.pokerPlayerDao = pokerPlayerDao;
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
	public PokerPlayer getPlayer(final PokerPlayerIdentifier playerIdentifier) throws PokerServiceException {
		try {
			logger.debug("getPlayer - id: " + playerIdentifier);
			return pokerPlayerDao.load(playerIdentifier);
		}
		catch (final StorageException e) {
			throw new PokerServiceException(e);
		}
		finally {
		}
	}

	@Override
	public PokerGameIdentifier createGame(final String name, final long blind) throws PokerServiceException, ValidationException {
		try {
			logger.debug("createGame - name: " + name);

			final PokerGameIdentifier id = new PokerGameIdentifier(idGeneratorUUID.nextId());

			final PokerGameBean bean = pokerGameDao.create();
			bean.setId(id);
			bean.setName(name);
			bean.setBigBlind(blind);
			bean.setSmallBlind(blind / 2);
			bean.setRunning(false);
			bean.setPot(0l);
			bean.setCardPosition(0);

			final ValidationResult errors = validationExecutor.validate(bean);
			if (errors.hasErrors()) {
				logger.warn(bean.getClass().getSimpleName() + " " + errors.toString());
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
	public PokerPlayerIdentifier createPlayer(final String name) throws PokerServiceException, ValidationException {
		try {
			logger.debug("createPlayer - name: " + name);

			final PokerPlayerIdentifier id = new PokerPlayerIdentifier(idGeneratorUUID.nextId());

			final PokerPlayerBean bean = pokerPlayerDao.create();
			bean.setId(id);
			bean.setName(name);
			bean.setAmount(START_CREDITS);

			final ValidationResult errors = validationExecutor.validate(bean);
			if (errors.hasErrors()) {
				logger.warn(bean.getClass().getSimpleName() + " " + errors.toString());
				throw new ValidationException(errors);
			}

			pokerPlayerDao.save(bean);

			return id;
		}
		catch (final StorageException e) {
			throw new PokerServiceException(e);
		}
		finally {
		}
	}

	@Override
	public PokerPlayerIdentifier createPlayerIdentifier(final String id) throws PokerServiceException {
		try {
			return id != null ? new PokerPlayerIdentifier(id) : null;
		}
		finally {
		}
	}

	@Override
	public Collection<PokerPlayerIdentifier> getPlayers(final PokerGameIdentifier gameIdentifier) throws PokerServiceException {
		try {
			final PokerGameBean game = pokerGameDao.load(gameIdentifier);
			return game.getPlayers();
		}
		catch (final StorageException e) {
			throw new PokerServiceException(e);
		}
		finally {
		}
	}

	@Override
	public PokerGameIdentifier createGameIdentifier(final String gameId) throws PokerServiceException {
		try {
			return gameId != null ? new PokerGameIdentifier(gameId) : null;
		}
		finally {
		}
	}

	@Override
	public void startGame(final PokerGameIdentifier gameIdentifier) throws PokerServiceException, ValidationException {
		try {
			final Collection<PokerPlayerIdentifier> playerIdentifiers = getPlayers(gameIdentifier);
			if (playerIdentifiers.size() < 2) {
				throw new ValidationException(new ValidationResultImpl(new ValidationErrorSimple("need at least 2 player to start game")));
			}
			final PokerGameBean game = pokerGameDao.load(gameIdentifier);
			if (Boolean.TRUE.equals(game.getRunning())) {
				throw new ValidationException(new ValidationResultImpl(new ValidationErrorSimple("game already started")));
			}

			game.setRunning(true);
			game.setPlayers(listUtil.randomize(game.getPlayers()));
			game.setCards(listUtil.randomize(pokerCardFactory.getCards()));
			game.setActivePlayer(game.getPlayers().get(0));

			final Collection<PokerPlayerBean> players = pokerPlayerDao.load(game.getPlayers());
			for (final PokerPlayerBean player : players) {
				for (int i = 0; i < START_CARDS; ++i) {
					player.getCards().add(getCard(game));
				}
			}

			final ValidationResult errors = validationExecutor.validate(game);
			if (errors.hasErrors()) {
				logger.warn(game.getClass().getSimpleName() + " " + errors.toString());
				throw new ValidationException(errors);
			}

			pokerGameDao.save(game, new StorageValueList(pokerGameDao.getEncoding()).add("running").add("cards").add("cardPosition").add("players").add("activePlayer"));
			for (final PokerPlayerBean player : players) {
				pokerPlayerDao.save(player, new StorageValueList(pokerPlayerDao.getEncoding()).add("cards"));
			}
		}
		catch (final StorageException e) {
			throw new PokerServiceException(e);
		}
		finally {
		}
	}

	@Override
	public Collection<PokerCardIdentifier> getCards(final PokerPlayerIdentifier playerIdentifier) throws PokerServiceException {
		try {
			final PokerPlayerBean player = pokerPlayerDao.load(playerIdentifier);
			return player.getCards();
		}
		catch (final StorageException e) {
			throw new PokerServiceException(e);
		}
		finally {
		}
	}

	@Override
	public Collection<PokerCardIdentifier> getCards(final PokerGameIdentifier gameIdentifier) throws PokerServiceException {
		try {
			final PokerGameBean game = pokerGameDao.load(gameIdentifier);
			return game.getCards();
		}
		catch (final StorageException e) {
			throw new PokerServiceException(e);
		}
		finally {
		}
	}

	@Override
	public void joinGame(final PokerGameIdentifier gameIdentifier, final PokerPlayerIdentifier playerIdentifier) throws PokerServiceException, ValidationException {
		try {
			final PokerGameBean game = pokerGameDao.load(gameIdentifier);
			if (Boolean.TRUE.equals(game.getRunning())) {
				throw new ValidationException(new ValidationResultImpl(new ValidationErrorSimple("game already started")));
			}

			final PokerPlayerBean player = pokerPlayerDao.load(playerIdentifier);
			if (player.getGame() != null) {
				throw new ValidationException(new ValidationResultImpl(new ValidationErrorSimple("already joined a game")));
			}

			if (player.getAmount() <= 0) {
				throw new ValidationException(new ValidationResultImpl(new ValidationErrorSimple("can't join game with credits")));
			}

			player.setGame(gameIdentifier);
			game.getPlayers().add(playerIdentifier);

			pokerGameDao.save(game, new StorageValueList(pokerGameDao.getEncoding()).add("players"));
			pokerPlayerDao.save(player, new StorageValueList(pokerPlayerDao.getEncoding()).add("game"));
		}
		catch (final StorageException e) {
			throw new PokerServiceException(e);
		}
		finally {
		}
	}

	@Override
	public void leaveGame(final PokerGameIdentifier gameIdentifier, final PokerPlayerIdentifier playerIdentifier) throws PokerServiceException, ValidationException {
		try {
			final PokerGameBean game = pokerGameDao.load(gameIdentifier);
			if (Boolean.TRUE.equals(game.getRunning())) {
				throw new ValidationException(new ValidationResultImpl(new ValidationErrorSimple("game already started")));
			}

			final PokerPlayerBean player = pokerPlayerDao.load(playerIdentifier);
			if (player.getGame() == null) {
				throw new ValidationException(new ValidationResultImpl(new ValidationErrorSimple("not joined a game")));
			}

			player.setGame(null);
			game.getPlayers().remove(playerIdentifier);

			pokerGameDao.save(game, new StorageValueList(pokerGameDao.getEncoding()).add("players"));
			pokerPlayerDao.save(player, new StorageValueList(pokerPlayerDao.getEncoding()).add("game"));
		}
		catch (final StorageException e) {
			throw new PokerServiceException(e);
		}
		finally {
		}
	}

	private PokerCardIdentifier getCard(final PokerGameIdentifier pokerGameIdentifier) throws StorageException {
		final PokerGameBean game = pokerGameDao.load(pokerGameIdentifier);
		final PokerCardIdentifier result = getCard(game);
		pokerGameDao.save(game, new StorageValueList(pokerGameDao.getEncoding()).add("cardPosition"));
		return result;
	}

	private PokerCardIdentifier getCard(final PokerGameBean game) throws StorageException {
		final List<PokerCardIdentifier> cards = game.getCards();
		final int pos = game.getCardPosition();
		final PokerCardIdentifier result = cards.get(pos);
		game.setCardPosition(pos + 1);
		return result;
	}
}
