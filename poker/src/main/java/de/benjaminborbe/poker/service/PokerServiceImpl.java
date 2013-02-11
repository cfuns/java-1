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
import de.benjaminborbe.storage.tools.EntityIterator;
import de.benjaminborbe.storage.tools.EntityIteratorException;
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
	public Collection<PokerGameIdentifier> getGameIdentifiers() throws PokerServiceException {
		try {
			logger.debug("getGameIdentifiers");
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
			game.setButtonPosition(0);
			game.setRound(0l);

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

			pokerGameDao.save(game, new StorageValueList(pokerGameDao.getEncoding()).add("running").add("cards").add("cardPosition").add("players").add("buttonPosition").add("round"));
			for (final PokerPlayerBean player : players) {
				pokerPlayerDao.save(player, new StorageValueList(pokerPlayerDao.getEncoding()).add("cards"));
			}

			nextRound(gameIdentifier);
		}
		catch (final StorageException e) {
			throw new PokerServiceException(e);
		}
		finally {
		}
	}

	@Override
	public void nextRound(final PokerGameIdentifier gameIdentifier) throws PokerServiceException {
		try {
			final PokerGameBean game = pokerGameDao.load(gameIdentifier);
			nextRound(game);
		}
		catch (final StorageException e) {
			throw new PokerServiceException(e);
		}
		finally {
		}
	}

	private void bid(final PokerGameBean game, final PokerPlayerBean player, final long value) {
		player.setAmount(player.getAmount() - value);
		player.setBet(value);
		game.setPot(game.getPot() + value);
	}

	@Override
	public Collection<PokerCardIdentifier> getHandCards(final PokerPlayerIdentifier playerIdentifier) throws PokerServiceException {
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
	public Collection<PokerCardIdentifier> getBoardCards(final PokerGameIdentifier gameIdentifier) throws PokerServiceException {
		try {
			final PokerGameBean game = pokerGameDao.load(gameIdentifier);
			return game.getBoardCards();
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

	@Override
	public PokerPlayerIdentifier getActivePlayer(final PokerGameIdentifier gameIdentifier) throws PokerServiceException {
		try {
			final PokerGameBean game = pokerGameDao.load(gameIdentifier);
			final Integer pos = game.getActivePosition();
			if (pos != null) {
				return game.getPlayers().get(pos);
			}
			else {
				return null;
			}
		}
		catch (final StorageException e) {
			throw new PokerServiceException(e);
		}
		finally {
		}
	}

	private PokerCardIdentifier getCard(final PokerGameBean game) throws StorageException {
		final List<PokerCardIdentifier> cards = game.getCards();
		final int pos = game.getCardPosition();
		final PokerCardIdentifier result = cards.get(pos);
		game.setCardPosition(pos + 1);
		return result;
	}

	@Override
	public void call(final PokerGameIdentifier gameIdentifier, final PokerPlayerIdentifier playerIdentifier) throws PokerServiceException, ValidationException {
		try {
			logger.debug("call");
			if (!getActivePlayer(gameIdentifier).equals(playerIdentifier)) {
				throw new ValidationException(new ValidationResultImpl(new ValidationErrorSimple("not active player")));
			}

			final PokerGameBean game = pokerGameDao.load(gameIdentifier);
			final PokerPlayerBean player = pokerPlayerDao.load(playerIdentifier);
			bid(game, player, game.getBet() - player.getBet());
			game.setActivePosition(game.getActivePosition() + 1 % (game.getActivePlayers().size() - 1));

			pokerGameDao.save(game, new StorageValueList(pokerGameDao.getEncoding()).add("pot").add("activePosition"));
			pokerPlayerDao.save(player, new StorageValueList(pokerPlayerDao.getEncoding()).add("amount"));

			completeTurn(game);
		}
		catch (final StorageException e) {
			throw new PokerServiceException(e);
		}
		finally {
		}
	}

	private void completeTurn(final PokerGameBean game) throws StorageException {
		logger.debug("completeTurn");
		for (final PokerPlayerIdentifier playerIdentifier : game.getActivePlayers()) {
			final PokerPlayerBean player = pokerPlayerDao.load(playerIdentifier);
			if (!player.getBet().equals(game.getBet())) {
				logger.debug("completeTurn => false");
				return;
			}
		}
		logger.debug("completeTurn => true");
		nextTurn(game);
	}

	private void nextTurn(final PokerGameBean game) throws StorageException {
		logger.debug("nextTurn");
		if (game.getBoardCards().size() == 0) {
			game.getBoardCards().add(getCard(game));
			game.getBoardCards().add(getCard(game));
			game.getBoardCards().add(getCard(game));
			pokerGameDao.save(game, new StorageValueList(pokerGameDao.getEncoding()).add("cardPosition").add("boardCards"));
		}
		else if (game.getBoardCards().size() == 3) {
			game.getBoardCards().add(getCard(game));
		}
		else if (game.getBoardCards().size() == 4) {
			game.getBoardCards().add(getCard(game));
		}
		else {
		}
	}

	private void nextRound(final PokerGameBean game) throws StorageException {
		final int playerAmount = game.getPlayers().size();

		game.setActivePlayers(game.getPlayers());

		// set player bet
		for (final PokerPlayerBean player : pokerPlayerDao.load(game.getPlayers())) {
			player.setBet(0l);
			pokerPlayerDao.save(player, new StorageValueList(pokerPlayerDao.getEncoding()).add("bet"));
		}

		// increase button position
		final int position = game.getButtonPosition();
		game.setButtonPosition(position + 1 % playerAmount);

		// increase round
		game.setRound(game.getRound() + 1);

		// small blind
		{
			final PokerPlayerIdentifier playerIdentifier = game.getPlayers().get(game.getButtonPosition() + 1 % (playerAmount - 1));
			final PokerPlayerBean player = pokerPlayerDao.load(playerIdentifier);
			bid(game, player, game.getSmallBlind());
			pokerPlayerDao.save(player, new StorageValueList(pokerPlayerDao.getEncoding()).add("amount").add("bet"));
		}

		// big blind
		{
			final PokerPlayerIdentifier playerIdentifier = game.getPlayers().get(game.getButtonPosition() + 2 % (playerAmount - 1));
			final PokerPlayerBean player = pokerPlayerDao.load(playerIdentifier);
			bid(game, player, game.getBigBlind());
			pokerPlayerDao.save(player, new StorageValueList(pokerPlayerDao.getEncoding()).add("amount").add("bet"));
		}

		// active player
		game.setActivePosition(position + 3 % (playerAmount - 1));

		// set game bet
		game.setBet(game.getBigBlind());

		pokerGameDao.save(game, new StorageValueList(pokerGameDao.getEncoding()).add("buttonPosition").add("round").add("activePosition").add("pot").add("bet").add("activePlayers"));
	}

	@Override
	public void raise(final PokerGameIdentifier gameIdentifier, final PokerPlayerIdentifier playerIdentifier, final long amount) throws PokerServiceException, ValidationException {
		try {
			logger.debug("raise");
			if (!getActivePlayer(gameIdentifier).equals(playerIdentifier)) {
				throw new ValidationException(new ValidationResultImpl(new ValidationErrorSimple("not active player")));
			}

			final PokerGameBean game = pokerGameDao.load(gameIdentifier);
			final PokerPlayerBean player = pokerPlayerDao.load(playerIdentifier);
			bid(game, player, amount - player.getBet());
			game.setBet(amount);
			game.setActivePosition(game.getActivePosition() + 1 % (game.getActivePlayers().size() - 1));

			pokerGameDao.save(game, new StorageValueList(pokerGameDao.getEncoding()).add("pot").add("activePosition").add("bet"));
			pokerPlayerDao.save(player, new StorageValueList(pokerPlayerDao.getEncoding()).add("amount"));

			completeTurn(game);
		}
		catch (final StorageException e) {
			throw new PokerServiceException(e);
		}
		finally {
		}
	}

	@Override
	public void fold(final PokerGameIdentifier gameIdentifier, final PokerPlayerIdentifier playerIdentifier) throws PokerServiceException, ValidationException {
		try {
			logger.debug("fold");
			if (!getActivePlayer(gameIdentifier).equals(playerIdentifier)) {
				throw new ValidationException(new ValidationResultImpl(new ValidationErrorSimple("not active player")));
			}

			final PokerGameBean game = pokerGameDao.load(gameIdentifier);
			game.getActivePlayers().remove(playerIdentifier);
			game.setActivePosition(game.getActivePosition() % (game.getActivePlayers().size() - 1));

			pokerGameDao.save(game, new StorageValueList(pokerGameDao.getEncoding()).add("activePosition").add("activePlayers"));

			completeTurn(game);
		}
		catch (final StorageException e) {
			throw new PokerServiceException(e);
		}
		finally {
		}
	}

	@Override
	public Collection<PokerGame> getGames() throws PokerServiceException {
		try {
			logger.debug("getGames");
			final EntityIterator<PokerGameBean> i = pokerGameDao.getEntityIterator();
			final List<PokerGame> result = new ArrayList<PokerGame>();
			while (i.hasNext()) {
				result.add(i.next());
			}
			return result;
		}
		catch (final StorageException e) {
			throw new PokerServiceException(e);
		}
		catch (final EntityIteratorException e) {
			throw new PokerServiceException(e);
		}
		finally {
		}
	}

	@Override
	public Collection<PokerPlayer> getPlayers() throws PokerServiceException {
		try {
			logger.debug("getPlayers");
			final EntityIterator<PokerPlayerBean> i = pokerPlayerDao.getEntityIterator();
			final List<PokerPlayer> result = new ArrayList<PokerPlayer>();
			while (i.hasNext()) {
				result.add(i.next());
			}
			return result;
		}
		catch (final StorageException e) {
			throw new PokerServiceException(e);
		}
		catch (final EntityIteratorException e) {
			throw new PokerServiceException(e);
		}
		finally {
		}
	}
}
