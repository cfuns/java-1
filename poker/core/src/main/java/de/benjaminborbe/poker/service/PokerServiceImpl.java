package de.benjaminborbe.poker.service;

import de.benjaminborbe.analytics.api.AnalyticsService;
import de.benjaminborbe.analytics.api.AnalyticsServiceException;
import de.benjaminborbe.api.ValidationErrorSimple;
import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.api.ValidationResult;
import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.AuthenticationServiceException;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.authorization.api.AuthorizationServiceException;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.authorization.api.PermissionIdentifier;
import de.benjaminborbe.configuration.api.ConfigurationServiceException;
import de.benjaminborbe.cron.api.CronControllerException;
import de.benjaminborbe.lib.validation.ValidationExecutor;
import de.benjaminborbe.lib.validation.ValidationResultImpl;
import de.benjaminborbe.poker.api.PokerCardIdentifier;
import de.benjaminborbe.poker.api.PokerGame;
import de.benjaminborbe.poker.api.PokerGameDto;
import de.benjaminborbe.poker.api.PokerGameIdentifier;
import de.benjaminborbe.poker.api.PokerPlayer;
import de.benjaminborbe.poker.api.PokerPlayerDto;
import de.benjaminborbe.poker.api.PokerPlayerIdentifier;
import de.benjaminborbe.poker.api.PokerService;
import de.benjaminborbe.poker.api.PokerServiceException;
import de.benjaminborbe.poker.card.PokerCardFactory;
import de.benjaminborbe.poker.config.PokerCoreConfig;
import de.benjaminborbe.poker.event.AnalyticsReportUtil;
import de.benjaminborbe.poker.game.PokerGameBean;
import de.benjaminborbe.poker.game.PokerGameBeanMapper;
import de.benjaminborbe.poker.game.PokerGameDao;
import de.benjaminborbe.poker.gamecreator.PokerGameCreator;
import de.benjaminborbe.poker.player.PokerPlayerBean;
import de.benjaminborbe.poker.player.PokerPlayerBeanMapper;
import de.benjaminborbe.poker.player.PokerPlayerDao;
import de.benjaminborbe.poker.reset.PokerEventReseter;
import de.benjaminborbe.poker.util.PokerEventPoster;
import de.benjaminborbe.poker.util.PokerWinnerCalculator;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.tools.EntityIterator;
import de.benjaminborbe.storage.tools.EntityIteratorException;
import de.benjaminborbe.storage.tools.IdentifierIterator;
import de.benjaminborbe.storage.tools.IdentifierIteratorException;
import de.benjaminborbe.storage.tools.StorageValueList;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.list.ListUtil;
import de.benjaminborbe.tools.util.Duration;
import de.benjaminborbe.tools.util.DurationUtil;
import de.benjaminborbe.tools.util.IdGeneratorUUID;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Singleton
public class PokerServiceImpl implements PokerService {

	private static final int DURATION_WARN = 300;

	private static final int START_CARDS = 2;

	private final Logger logger;

	private final PokerGameDao pokerGameDao;

	private final IdGeneratorUUID idGeneratorUUID;

	private final ValidationExecutor validationExecutor;

	private final PokerPlayerDao pokerPlayerDao;

	private final DurationUtil durationUtil;

	private final PokerEventReseter pokerEventReseter;

	private final PokerGameCreator pokerGameCreator;

	private final AnalyticsReportUtil analyticsReportUtil;

	private final PokerEventPoster pokerEventPoster;

	private final ListUtil listUtil;

	private final PokerCardFactory pokerCardFactory;

	private final PokerWinnerCalculator pokerWinnerCalculator;

	private final AuthorizationService authorizationService;

	private final PokerCoreConfig pokerCoreConfig;

	private final AnalyticsService analyticsService;

	private final AuthenticationService authenticationService;

	private final CalendarUtil calendarUtil;

	@Inject
	public PokerServiceImpl(
		final Logger logger,
		final AuthenticationService authenticationService,
		final AuthorizationService authorizationService,
		final CalendarUtil calendarUtil,
		final AnalyticsService analyticsService,
		final PokerCoreConfig pokerCoreConfig,
		final PokerWinnerCalculator pokerWinnerCalculator,
		final ListUtil listUtil,
		final PokerCardFactory pokerCardFactory,
		final PokerGameDao pokerGameDao,
		final IdGeneratorUUID idGeneratorUUID,
		final ValidationExecutor validationExecutor,
		final PokerPlayerDao pokerPlayerDao,
		final DurationUtil durationUtil,
		final PokerEventReseter pokerEventReseter,
		final PokerGameCreator pokerGameCreator,
		final AnalyticsReportUtil analyticsReportUtil,
		final PokerEventPoster pokerEventPoster
	) {
		this.logger = logger;
		this.authenticationService = authenticationService;
		this.calendarUtil = calendarUtil;
		this.analyticsService = analyticsService;
		this.pokerCoreConfig = pokerCoreConfig;
		this.authorizationService = authorizationService;
		this.pokerWinnerCalculator = pokerWinnerCalculator;
		this.pokerCardFactory = pokerCardFactory;
		this.listUtil = listUtil;
		this.pokerGameDao = pokerGameDao;
		this.idGeneratorUUID = idGeneratorUUID;
		this.validationExecutor = validationExecutor;
		this.pokerPlayerDao = pokerPlayerDao;
		this.durationUtil = durationUtil;
		this.pokerEventReseter = pokerEventReseter;
		this.pokerGameCreator = pokerGameCreator;
		this.analyticsReportUtil = analyticsReportUtil;
		this.pokerEventPoster = pokerEventPoster;
	}

	@Override
	public Collection<PokerGameIdentifier> getGameIdentifiers() throws PokerServiceException {
		final Duration duration = durationUtil.getDuration();
		try {
			logger.debug("getGameIdentifiers");
			final IdentifierIterator<PokerGameIdentifier> i = pokerGameDao.getIdentifierIterator();
			final List<PokerGameIdentifier> result = new ArrayList<PokerGameIdentifier>();
			while (i.hasNext()) {
				result.add(i.next());
			}
			return result;
		} catch (final IdentifierIteratorException e) {
			throw new PokerServiceException(e);
		} catch (StorageException e) {
			throw new PokerServiceException(e);
		} finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public PokerGame getGame(final PokerGameIdentifier gameIdentifier) throws PokerServiceException {
		final Duration duration = durationUtil.getDuration();
		try {
			logger.trace("getGame - id: " + gameIdentifier);
			return pokerGameDao.load(gameIdentifier);
		} catch (final StorageException e) {
			throw new PokerServiceException(e);
		} finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public PokerPlayer getPlayer(final PokerPlayerIdentifier playerIdentifier) throws PokerServiceException {
		final Duration duration = durationUtil.getDuration();
		try {
			logger.trace("getPlayer - id: " + playerIdentifier);
			return pokerPlayerDao.load(playerIdentifier);
		} catch (final StorageException e) {
			throw new PokerServiceException(e);
		} finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public PokerGameIdentifier createGame(final PokerGameDto pokerGameDto) throws PokerServiceException, ValidationException {
		final Duration duration = durationUtil.getDuration();
		try {
			logger.debug("createGame - name: " + pokerGameDto.getName());
			return pokerGameCreator.createGame(pokerGameDto);
		} catch (final StorageException e) {
			throw new PokerServiceException(e);
		} finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public PokerPlayerIdentifier createPlayer(
		final SessionIdentifier sessionIdentifier,
		final PokerPlayerDto pokerPlayerDto
	) throws PokerServiceException, ValidationException, LoginRequiredException, PermissionDeniedException {
		final Duration duration = durationUtil.getDuration();
		try {
			logger.debug("createPlayer - name: " + pokerPlayerDto.getName());

			final PokerPlayerIdentifier id = new PokerPlayerIdentifier(idGeneratorUUID.nextId());

			final PokerPlayerBean bean = pokerPlayerDao.create();
			bean.setId(id);
			bean.setName(pokerPlayerDto.getName());
			bean.setAmount(pokerPlayerDto.getAmount());
			bean.setOwners(pokerPlayerDto.getOwners());
			bean.setToken(idGeneratorUUID.nextId());
			bean.setBet(0l);
			bean.setScore(0l);
			bean.setCards(new ArrayList<PokerCardIdentifier>());

			final ValidationResult errors = validationExecutor.validate(bean);
			if (errors.hasErrors()) {
				logger.warn(bean.getClass().getSimpleName() + " " + errors.toString());
				throw new ValidationException(errors);
			}
			pokerPlayerDao.save(bean);
			pokerEventPoster.firePokerPlayerCreatedEvent(bean);
			return id;
		} catch (final StorageException e) {
			throw new PokerServiceException(e);
		} finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public PokerPlayerIdentifier createPlayerIdentifier(final String id) throws PokerServiceException {
		final Duration duration = durationUtil.getDuration();
		try {
			return id != null ? new PokerPlayerIdentifier(id) : null;
		} finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public Collection<PokerPlayerIdentifier> getPlayers(final PokerGameIdentifier gameIdentifier) throws PokerServiceException {
		final Duration duration = durationUtil.getDuration();
		try {
			final PokerGameBean game = pokerGameDao.load(gameIdentifier);
			return game.getPlayers();
		} catch (final StorageException e) {
			throw new PokerServiceException(e);
		} finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public Collection<PokerPlayerIdentifier> getActivePlayers(final PokerGameIdentifier gameIdentifier) throws PokerServiceException {
		final Duration duration = durationUtil.getDuration();
		try {
			final PokerGameBean game = pokerGameDao.load(gameIdentifier);
			return game.getActivePlayers();
		} catch (final StorageException e) {
			throw new PokerServiceException(e);
		} finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public PokerGameIdentifier createGameIdentifier(final String gameId) throws PokerServiceException {
		final Duration duration = durationUtil.getDuration();
		try {
			return gameId != null ? new PokerGameIdentifier(gameId) : null;
		} finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public void startGame(final PokerGameIdentifier gameIdentifier) throws PokerServiceException, ValidationException {
		final Duration duration = durationUtil.getDuration();
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
			game.setButtonPosition(0);
			game.setRound(0l);
			game.setScore(new Long(playerIdentifiers.size()));
			game.setSmallBlind(game.getBigBlind() != null ? (game.getBigBlind() / 2) : null);

			final ValidationResult errors = validationExecutor.validate(game);
			if (errors.hasErrors()) {
				logger.warn(game.getClass().getSimpleName() + " " + errors.toString());
				throw new ValidationException(errors);
			}

			pokerGameDao.save(game, new StorageValueList(pokerGameDao.getEncoding()).add(PokerGameBeanMapper.RUNNING).add(PokerGameBeanMapper.SCORE).add(PokerGameBeanMapper.SMALL_BLIND).add(PokerGameBeanMapper.PLAYERS).add(PokerGameBeanMapper.BUTTON_POSITION).add(PokerGameBeanMapper.ROUND));

			final List<String> playerNames = new ArrayList<String>();
			for (final PokerPlayerIdentifier playerIdentifier : playerIdentifiers) {
				final PokerPlayerBean player = pokerPlayerDao.load(playerIdentifier);
				updatePlayerScore(player, toLong(player.getScore()) - 1);
				player.setAmount(game.getStartCredits());
				playerNames.add(player.getName());
				pokerPlayerDao.save(player, new StorageValueList(pokerGameDao.getEncoding()).add(PokerPlayerBeanMapper.SCORE).add(PokerPlayerBeanMapper.AMOUNT));
			}
			logger.info("start new game " + game.getName() + " with players: " + playerNames);

			nextRound(gameIdentifier);
		} catch (final StorageException e) {
			throw new PokerServiceException(e);
		} finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	private void updatePlayerScore(final PokerPlayerBean player, final long score) {
		player.setScore(score);
		pokerEventPoster.firePokerPlayerScoreChangedEvent(player, score);
	}

	private long toLong(final Long number) {
		return number != null ? number : 0;
	}

	private void nextRound(final PokerGameIdentifier gameIdentifier) throws PokerServiceException, ValidationException {
		try {
			final PokerGameBean game = pokerGameDao.load(gameIdentifier);
			nextRound(game);
		} catch (final StorageException e) {
			throw new PokerServiceException(e);
		} finally {
		}
	}

	private void bid(final PokerGameBean game, final PokerPlayerBean player, final long value) throws ValidationException {
		logger.debug("bid - amount: " + value);

		final long maxBid = pokerCoreConfig.getMaxBid();
		if (maxBid > 0 && value > maxBid) {
			throw new ValidationException(new ValidationResultImpl(new ValidationErrorSimple("bid higher than " + maxBid + " not allowed")));
		}

		final long minBid = isCreditsNegativeAllowed(game) ? game.getBet() : Math.min(player.getAmount(), game.getBet());
		if (value < minBid) {
			throw new ValidationException(new ValidationResultImpl(new ValidationErrorSimple("bid lower than " + minBid + " not allowed")));
		}

		final long diff = isCreditsNegativeAllowed(game) ? (value - player.getBet()) : Math.min(player.getAmount(), (value - player.getBet()));
		logger.debug("remove " + diff + " from player " + player.getId());
		player.setAmount(player.getAmount() - diff);
		logger.debug("add " + diff + " to pot");
		game.setPot(game.getPot() + diff);
		player.setBet(player.getBet() + diff);
		game.setBet(Math.max(game.getBet(), player.getBet()));
	}

	private boolean isCreditsNegativeAllowed(final PokerGameBean game) {
		return Boolean.TRUE.equals(game.getCreditsNegativeAllowed());
	}

	@Override
	public Collection<PokerCardIdentifier> getHandCards(final PokerPlayerIdentifier playerIdentifier) throws PokerServiceException {
		final Duration duration = durationUtil.getDuration();
		try {
			final PokerPlayerBean player = pokerPlayerDao.load(playerIdentifier);
			return player.getCards();
		} catch (final StorageException e) {
			throw new PokerServiceException(e);
		} finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public Collection<PokerCardIdentifier> getBoardCards(final PokerGameIdentifier gameIdentifier) throws PokerServiceException {
		final Duration duration = durationUtil.getDuration();
		try {
			final PokerGameBean game = pokerGameDao.load(gameIdentifier);
			return game.getBoardCards();
		} catch (final StorageException e) {
			throw new PokerServiceException(e);
		} finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public void joinGame(
		final PokerGameIdentifier gameIdentifier,
		final PokerPlayerIdentifier playerIdentifier
	) throws PokerServiceException, ValidationException {
		final Duration duration = durationUtil.getDuration();
		try {
			final PokerGameBean game = pokerGameDao.load(gameIdentifier);
			if (Boolean.TRUE.equals(game.getRunning())) {
				throw new ValidationException(new ValidationResultImpl(new ValidationErrorSimple("game already started")));
			}

			final PokerPlayerBean player = pokerPlayerDao.load(playerIdentifier);
			if (player.getGame() != null) {
				throw new ValidationException(new ValidationResultImpl(new ValidationErrorSimple("already joined a game")));
			}

			player.setGame(gameIdentifier);
			game.getPlayers().add(playerIdentifier);

			pokerGameDao.save(game, new StorageValueList(pokerGameDao.getEncoding()).add(PokerGameBeanMapper.PLAYERS));
			pokerPlayerDao.save(player, new StorageValueList(pokerPlayerDao.getEncoding()).add(PokerPlayerBeanMapper.GAME_ID));

			logger.info("player: " + player.getName() + " join game " + game.getName());
		} catch (final StorageException e) {
			throw new PokerServiceException(e);
		} finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public void leaveGame(
		final PokerGameIdentifier gameIdentifier,
		final PokerPlayerIdentifier playerIdentifier
	) throws PokerServiceException, ValidationException {
		final Duration duration = durationUtil.getDuration();
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

			pokerGameDao.save(game, new StorageValueList(pokerGameDao.getEncoding()).add(PokerGameBeanMapper.PLAYERS));
			pokerPlayerDao.save(player, new StorageValueList(pokerPlayerDao.getEncoding()).add(PokerPlayerBeanMapper.GAME_ID));

			logger.info("player: " + player.getName() + " leave game " + game.getName());
		} catch (final StorageException e) {
			throw new PokerServiceException(e);
		} finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public PokerPlayerIdentifier getActivePlayer(final PokerGameIdentifier gameIdentifier) throws PokerServiceException {
		final Duration duration = durationUtil.getDuration();
		try {
			final PokerGameBean game = pokerGameDao.load(gameIdentifier);
			return getActivePlayer(game);
		} catch (final StorageException e) {
			throw new PokerServiceException(e);
		} finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	protected PokerPlayerIdentifier getActivePlayer(final PokerGameBean game) {
		final Integer pos = game.getActivePosition();
		final List<PokerPlayerIdentifier> activePlayers = game.getActivePlayers();
		if (pos != null && pos < activePlayers.size()) {
			return activePlayers.get(pos);
		} else {
			return null;
		}
	}

	@Override
	public PokerPlayerIdentifier getButtonPlayer(final PokerGameIdentifier gameIdentifier) throws PokerServiceException {
		final Duration duration = durationUtil.getDuration();
		try {
			final PokerGameBean game = pokerGameDao.load(gameIdentifier);
			if (game.getButtonPosition() != null) {
				return game.getPlayers().get(game.getButtonPosition());
			} else {
				return null;
			}
		} catch (final StorageException e) {
			throw new PokerServiceException(e);
		} finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public PokerPlayerIdentifier getSmallBlindPlayer(final PokerGameIdentifier gameIdentifier) throws PokerServiceException {
		final Duration duration = durationUtil.getDuration();
		try {
			final PokerGameBean game = pokerGameDao.load(gameIdentifier);
			if (game.getButtonPosition() != null) {
				final int pos = (game.getButtonPosition() + 1) % game.getPlayers().size();
				return game.getPlayers().get(pos);
			} else {
				return null;
			}
		} catch (final StorageException e) {
			throw new PokerServiceException(e);
		} finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public PokerPlayerIdentifier getBigBlindPlayer(final PokerGameIdentifier gameIdentifier) throws PokerServiceException {
		final Duration duration = durationUtil.getDuration();
		try {
			final PokerGameBean game = pokerGameDao.load(gameIdentifier);
			if (game.getButtonPosition() != null) {
				final int pos = (game.getButtonPosition() + 2) % game.getPlayers().size();
				return game.getPlayers().get(pos);
			} else {
				return null;
			}
		} catch (final StorageException e) {
			throw new PokerServiceException(e);
		} finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
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
		final Duration duration = durationUtil.getDuration();
		try {
			logger.debug("call - game: " + gameIdentifier + " player: " + playerIdentifier);
			if (!getActivePlayer(gameIdentifier).equals(playerIdentifier)) {
				throw new ValidationException(new ValidationResultImpl(new ValidationErrorSimple("not active player")));
			}

			final PokerGameBean game = pokerGameDao.load(gameIdentifier);
			final PokerPlayerBean player = pokerPlayerDao.load(playerIdentifier);
			bid(game, player, game.getBet());
			game.setActivePosition((game.getActivePosition() + 1) % (game.getActivePlayers().size()));
			game.setActivePositionTime(calendarUtil.now());

			pokerGameDao.save(game, new StorageValueList(pokerGameDao.getEncoding()).add(PokerGameBeanMapper.POT).add(PokerGameBeanMapper.ACTIVE_POSITION).add(PokerGameBeanMapper.ACTIVE_POSITION_TIME).add(PokerGameBeanMapper.BET));
			pokerPlayerDao.save(player, new StorageValueList(pokerPlayerDao.getEncoding()).add(PokerPlayerBeanMapper.AMOUNT).add(PokerPlayerBeanMapper.BET));

			logger.info("call - game: " + game.getName() + " player: " + player.getName());

			completeTurn(game);

			pokerEventPoster.firePokerPlayerAmountChangedEvent(player);
		} catch (final StorageException e) {
			throw new PokerServiceException(e);
		} finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	private void completeTurn(final PokerGameBean game) throws StorageException, ValidationException, PokerServiceException {
		logger.debug("completeTurn - game: " + game.getId());
		if (game.getActivePosition() == (game.getButtonPosition() + 1) % game.getActivePlayers().size()) {
			for (final PokerPlayerIdentifier playerIdentifier : game.getActivePlayers()) {
				final PokerPlayerBean player = pokerPlayerDao.load(playerIdentifier);
				if (player.getAmount() != 0 && !player.getBet().equals(game.getBet())) {
					logger.debug("completeTurn => false");
					return;
				}
			}
			logger.debug("completeTurn => true");
			nextTurn(game);
		} else {
			logger.debug("completeTurn => false");
		}
	}

	private void nextTurn(final PokerGameBean game) throws StorageException, ValidationException, PokerServiceException {
		logger.debug("nextTurn - cards = " + game.getBoardCards().size());
		// flop
		if (game.getActivePlayers().size() > 1 && game.getBoardCards().size() == 0) {
			logger.debug("flop - add three cards to board");
			for (int i = 0; i < 3; ++i) {
				addCardToBoard(game);
			}
			pokerGameDao.save(game, new StorageValueList(pokerGameDao.getEncoding()).add(PokerGameBeanMapper.CARD_POSITION).add(PokerGameBeanMapper.BOARD_CARDS));
		}
		// turn
		else if (game.getActivePlayers().size() > 1 && game.getBoardCards().size() == 3) {
			logger.debug("turn - add one card to board");
			addCardToBoard(game);
			pokerGameDao.save(game, new StorageValueList(pokerGameDao.getEncoding()).add(PokerGameBeanMapper.CARD_POSITION).add(PokerGameBeanMapper.BOARD_CARDS));
		}
		// river
		else if (game.getActivePlayers().size() > 1 && game.getBoardCards().size() == 4) {
			logger.debug("river - add one card to board");
			addCardToBoard(game);
			pokerGameDao.save(game, new StorageValueList(pokerGameDao.getEncoding()).add(PokerGameBeanMapper.CARD_POSITION).add(PokerGameBeanMapper.BOARD_CARDS));
		}
		// showdown
		else {
			logger.debug("showdown - calc winnners");
			final Map<PokerPlayerIdentifier, Collection<PokerCardIdentifier>> playerCards = buildPlayerCards(game);
			final Collection<PokerPlayerIdentifier> winners = pokerWinnerCalculator.getWinners(playerCards);
			logger.debug("winners: " + winners.size());
			logger.info("showdown - pot: " + game.getPot() + " winnners: " + winners.size());
			for (final PokerPlayerIdentifier winner : winners) {
				final long amount = game.getPot() / winners.size();
				final PokerPlayerBean player = pokerPlayerDao.load(winner);
				player.setAmount(player.getAmount() + amount);
				logger.info("add " + amount + " to player " + player.getName() + " at game: " + game.getName());
				pokerPlayerDao.save(player, new StorageValueList(pokerPlayerDao.getEncoding()).add(PokerPlayerBeanMapper.AMOUNT));
				pokerEventPoster.firePokerPlayerAmountChangedEvent(player);
			}
			nextRound(game);
		}
	}

	private Map<PokerPlayerIdentifier, Collection<PokerCardIdentifier>> buildPlayerCards(final PokerGameBean game) throws StorageException {
		final Map<PokerPlayerIdentifier, Collection<PokerCardIdentifier>> playerCards = new HashMap<PokerPlayerIdentifier, Collection<PokerCardIdentifier>>();
		for (final PokerPlayerIdentifier playerIdentifier : game.getActivePlayers()) {
			final PokerPlayerBean player = pokerPlayerDao.load(playerIdentifier);
			final List<PokerCardIdentifier> cards = new ArrayList<PokerCardIdentifier>();
			cards.addAll(game.getBoardCards());
			cards.addAll(player.getCards());
			playerCards.put(playerIdentifier, cards);
		}
		return playerCards;
	}

	private void addCardToBoard(final PokerGameBean game) throws StorageException {
		final PokerCardIdentifier card = getCard(game);
		game.getBoardCards().add(card);
		logger.info("add card to board - game: " + game.getName() + " card: " + card);
	}

	private void nextRound(final PokerGameBean game) throws StorageException, ValidationException, PokerServiceException {

		// remove players without credits
		if (!isCreditsNegativeAllowed(game)) {
			for (final PokerPlayerBean player : pokerPlayerDao.load(game.getPlayers())) {
				if (player.getAmount() <= 0) {
					player.setGame(null);
					player.setBet(0l);
					game.getPlayers().remove(player.getId());

					pokerGameDao.save(game, new StorageValueList(pokerGameDao.getEncoding()).add(PokerGameBeanMapper.PLAYERS));
					pokerPlayerDao.save(player, new StorageValueList(pokerPlayerDao.getEncoding()).add(PokerPlayerBeanMapper.GAME_ID).add(PokerPlayerBeanMapper.BET));
					logger.info("player: " + player.getName() + " removed from game " + game.getName());
				}
			}
		}

		final int playerAmount = game.getPlayers().size();
		if (playerAmount < 2) {
			endGame(game.getId());
			return;
		}

		game.setActivePlayers(game.getPlayers());
		game.setCards(listUtil.randomize(pokerCardFactory.getCards()));
		game.setBoardCards(new ArrayList<PokerCardIdentifier>());
		game.setCardPosition(0);
		game.setPot(0l);
		game.setBet(0l);

		// set player bet
		for (final PokerPlayerBean player : pokerPlayerDao.load(game.getPlayers())) {
			player.setBet(0l);
			player.setCards(new ArrayList<PokerCardIdentifier>());
			for (int i = 0; i < START_CARDS; ++i) {
				final PokerCardIdentifier card = getCard(game);
				player.getCards().add(card);
				logger.info("add card to player - game: " + game.getName() + " player: " + player.getName() + " card: " + card);
			}
			pokerPlayerDao.save(player, new StorageValueList(pokerPlayerDao.getEncoding()).add(PokerPlayerBeanMapper.BET).add(PokerPlayerBeanMapper.CARDS));
		}

		// increase button position
		final int position = game.getButtonPosition();
		game.setButtonPosition((position + 1) % playerAmount);

		// increase round
		game.setRound(game.getRound() + 1);

		// small blind
		{
			final PokerPlayerIdentifier playerIdentifier = game.getPlayers().get((game.getButtonPosition() + 1) % (playerAmount));
			final PokerPlayerBean player = pokerPlayerDao.load(playerIdentifier);
			bid(game, player, game.getSmallBlind());
			pokerPlayerDao.save(player, new StorageValueList(pokerPlayerDao.getEncoding()).add(PokerPlayerBeanMapper.AMOUNT).add(PokerPlayerBeanMapper.BET));
			pokerEventPoster.firePokerPlayerAmountChangedEvent(player);
		}

		// big blind
		{
			final PokerPlayerIdentifier playerIdentifier = game.getPlayers().get((game.getButtonPosition() + 2) % (playerAmount));
			final PokerPlayerBean player = pokerPlayerDao.load(playerIdentifier);
			bid(game, player, game.getBigBlind());
			pokerPlayerDao.save(player, new StorageValueList(pokerPlayerDao.getEncoding()).add(PokerPlayerBeanMapper.AMOUNT).add(PokerPlayerBeanMapper.BET));
			pokerEventPoster.firePokerPlayerAmountChangedEvent(player);
		}

		// active player
		game.setActivePosition((game.getButtonPosition() + 3) % (playerAmount));
		game.setActivePositionTime(calendarUtil.now());

		// set game bet
		game.setBet(game.getBigBlind());

		pokerGameDao.save(game, new StorageValueList(pokerGameDao.getEncoding()).add(PokerGameBeanMapper.BUTTON_POSITION).add(PokerGameBeanMapper.CARDS).add(PokerGameBeanMapper.CARD_POSITION).add(PokerGameBeanMapper.ROUND).add(PokerGameBeanMapper.ACTIVE_POSITION).add(PokerGameBeanMapper.ACTIVE_POSITION_TIME).add(PokerGameBeanMapper.POT)
			.add(PokerGameBeanMapper.BET).add(PokerGameBeanMapper.ACTIVE_PLAYERS).add(PokerGameBeanMapper.BOARD_CARDS));
	}

	private void endGame(final PokerGameIdentifier gameIdentifier) throws PokerServiceException, ValidationException, StorageException {
		stopGame(gameIdentifier);
		final PokerGameBean game = pokerGameDao.load(gameIdentifier);
		if (Boolean.TRUE.equals(game.getAutoJoinAndRestart())) {
			addAllAvailablePlayers(gameIdentifier);
			startGame(gameIdentifier);
		}
	}

	@Override
	public void raise(
		final PokerGameIdentifier gameIdentifier,
		final PokerPlayerIdentifier playerIdentifier,
		final long amount
	) throws PokerServiceException, ValidationException {
		final Duration duration = durationUtil.getDuration();
		try {
			logger.debug("raise - game: " + gameIdentifier + " player: " + playerIdentifier + " amount: " + amount);
			if (!getActivePlayer(gameIdentifier).equals(playerIdentifier)) {
				throw new ValidationException(new ValidationResultImpl(new ValidationErrorSimple("not active player")));
			}

			final PokerGameBean game = pokerGameDao.load(gameIdentifier);

			final Double minRaiseFactor = game.getMinRaiseFactor();
			if (minRaiseFactor != null && minRaiseFactor >= 1 && game.getBet() * minRaiseFactor > amount) {
				throw new ValidationException(new ValidationResultImpl(new ValidationErrorSimple("raise to low!")));
			}

			final Double maxRaiseFactor = game.getMaxRaiseFactor();
			if (maxRaiseFactor != null && maxRaiseFactor >= 1 && game.getBet() * maxRaiseFactor < amount) {
				throw new ValidationException(new ValidationResultImpl(new ValidationErrorSimple("raise to high!")));
			}

			final PokerPlayerBean player = pokerPlayerDao.load(playerIdentifier);
			bid(game, player, amount);
			game.setActivePosition((game.getActivePosition() + 1) % (game.getActivePlayers().size()));
			game.setActivePositionTime(calendarUtil.now());

			pokerGameDao.save(game, new StorageValueList(pokerGameDao.getEncoding()).add(PokerGameBeanMapper.POT).add(PokerGameBeanMapper.ACTIVE_POSITION).add(PokerGameBeanMapper.ACTIVE_POSITION_TIME).add(PokerGameBeanMapper.BET));
			pokerPlayerDao.save(player, new StorageValueList(pokerPlayerDao.getEncoding()).add(PokerPlayerBeanMapper.AMOUNT).add(PokerPlayerBeanMapper.BET));

			logger.info("raise - game: " + game.getName() + " player: " + player.getName() + " amount: " + amount);

			completeTurn(game);

			pokerEventPoster.firePokerPlayerAmountChangedEvent(player);
		} catch (final StorageException e) {
			throw new PokerServiceException(e);
		} finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public void fold(final PokerGameIdentifier gameIdentifier, final PokerPlayerIdentifier playerIdentifier) throws PokerServiceException, ValidationException {
		final Duration duration = durationUtil.getDuration();
		try {
			logger.debug("fold - game: " + gameIdentifier + " player: " + playerIdentifier);
			if (!getActivePlayer(gameIdentifier).equals(playerIdentifier)) {
				throw new ValidationException(new ValidationResultImpl(new ValidationErrorSimple("not active player")));
			}

			final PokerGameBean game = pokerGameDao.load(gameIdentifier);
			final PokerPlayerBean player = pokerPlayerDao.load(playerIdentifier);
			game.getActivePlayers().remove(playerIdentifier);
			game.setActivePosition(game.getActivePosition() % (game.getActivePlayers().size()));
			game.setActivePositionTime(calendarUtil.now());

			pokerGameDao.save(game, new StorageValueList(pokerGameDao.getEncoding()).add(PokerGameBeanMapper.ACTIVE_POSITION).add(PokerGameBeanMapper.ACTIVE_POSITION_TIME).add(PokerGameBeanMapper.ACTIVE_PLAYERS));
			logger.info("fold - game: " + game.getName() + " player: " + player.getName());

			completeTurn(game);
		} catch (final StorageException e) {
			throw new PokerServiceException(e);
		} finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public Collection<PokerGame> getGames() throws PokerServiceException {
		final Duration duration = durationUtil.getDuration();
		try {
			logger.debug("getGames");
			final EntityIterator<PokerGameBean> i = pokerGameDao.getEntityIterator();
			final List<PokerGame> result = new ArrayList<PokerGame>();
			while (i.hasNext()) {
				result.add(i.next());
			}
			return result;
		} catch (final StorageException e) {
			throw new PokerServiceException(e);
		} catch (EntityIteratorException e) {
			throw new PokerServiceException(e);
		} finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	private Collection<PokerPlayer> getPlayerWithoutActiveGame() throws PokerServiceException {
		try {
			logger.debug("getPlayerWithoutActiveGame");
			final EntityIterator<PokerPlayerBean> i = pokerPlayerDao.getEntityIterator();
			final List<PokerPlayer> result = new ArrayList<PokerPlayer>();
			while (i.hasNext()) {
				final PokerPlayerBean player = i.next();
				if (!hasActiveGame(player)) {
					result.add(player);
				}
			}
			return result;
		} catch (final StorageException e) {
			throw new PokerServiceException(e);
		} catch (EntityIteratorException e) {
			throw new PokerServiceException(e);
		} finally {
		}
	}

	private boolean hasActiveGame(final PokerPlayerBean player) throws PokerServiceException, StorageException {
		final PokerGameIdentifier gameIdentifier = player.getGame();
		if (gameIdentifier != null) {
			final PokerGame game = pokerGameDao.load(gameIdentifier);
			if (game != null && Boolean.TRUE.equals(game.getRunning())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public Collection<PokerPlayer> getPlayers() throws PokerServiceException {
		final Duration duration = durationUtil.getDuration();
		try {
			logger.trace("getPlayers");
			final EntityIterator<PokerPlayerBean> i = pokerPlayerDao.getEntityIterator();
			final List<PokerPlayer> result = new ArrayList<PokerPlayer>();
			while (i.hasNext()) {
				result.add(i.next());
			}
			return result;
		} catch (final StorageException e) {
			throw new PokerServiceException(e);
		} catch (EntityIteratorException e) {
			throw new PokerServiceException(e);
		} finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public SessionIdentifier getPokerServerSessionIdentifier() throws PokerServiceException {
		try {
			return authenticationService.createSystemUser(SERVER_USER);
		} catch (AuthenticationServiceException e) {
			throw new PokerServiceException(e);
		}
	}

	@Override
	public UserIdentifier getPokerServerUserIdentifier() throws PokerServiceException {
		try {
			authenticationService.createSystemUser(SERVER_USER);
			return authenticationService.createUserIdentifier(SERVER_USER);
		} catch (AuthenticationServiceException e) {
			throw new PokerServiceException(e);
		}
	}

	@Override
	public PermissionIdentifier getPokerAdminPermissionIdentifier() throws PokerServiceException {
		try {
			return authorizationService.createPermissionIdentifier(PERMISSION_ADMIN);
		} catch (AuthorizationServiceException e) {
			throw new PokerServiceException(e);
		}
	}

	@Override
	public PermissionIdentifier getPokerPlayerPermissionIdentifier() throws PokerServiceException {
		try {
			return authorizationService.createPermissionIdentifier(PERMISSION_PLAYER);
		} catch (AuthorizationServiceException e) {
			throw new PokerServiceException(e);
		}
	}

	@Override
	public void deleteGame(final PokerGameIdentifier gameIdentifier) throws PokerServiceException, ValidationException {
		final Duration duration = durationUtil.getDuration();
		try {
			logger.debug("deleteGame - id: " + gameIdentifier);
			final PokerGameBean game = pokerGameDao.load(gameIdentifier);
			if (Boolean.TRUE.equals(game.getRunning())) {
				throw new ValidationException(new ValidationResultImpl(new ValidationErrorSimple("can't delete running game")));
			}
			for (final PokerPlayerIdentifier playerIdentifier : game.getPlayers()) {
				final PokerPlayerBean player = pokerPlayerDao.load(playerIdentifier);
				player.setGame(null);
				pokerPlayerDao.save(player, new StorageValueList(pokerPlayerDao.getEncoding()).add(PokerPlayerBeanMapper.GAME_ID));
			}
			pokerGameDao.delete(gameIdentifier);
		} catch (final StorageException e) {
			throw new PokerServiceException(e);
		} finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public void deletePlayer(
		final SessionIdentifier sessionIdentifier,
		final PokerPlayerIdentifier playerIdentifier
	) throws PokerServiceException, ValidationException, LoginRequiredException, PermissionDeniedException {
		final Duration duration = durationUtil.getDuration();
		try {
			logger.debug("deletePlayer - id: " + playerIdentifier);
			final PokerPlayerBean player = pokerPlayerDao.load(playerIdentifier);
			if (player.getGame() != null) {
				final PokerGameBean game = pokerGameDao.load(player.getGame());
				if (Boolean.TRUE.equals(game.getRunning())) {
					throw new ValidationException(new ValidationResultImpl(new ValidationErrorSimple("can't delete player with running game")));
				} else {
					leaveGame(player.getGame(), playerIdentifier);
				}
			}
			pokerPlayerDao.delete(playerIdentifier);
			pokerEventPoster.firePokerPlayerDeletedEvent(playerIdentifier);
		} catch (final StorageException e) {
			throw new PokerServiceException(e);
		} finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public void addAllAvailablePlayers(final PokerGameIdentifier pokerGameIdentifier) throws PokerServiceException, ValidationException {
		final Duration duration = durationUtil.getDuration();
		try {
			logger.debug("addAllAvailablePlayers to game: " + pokerGameIdentifier);
			final Collection<PokerPlayer> players = getPlayerWithoutActiveGame();
			for (final PokerPlayer player : players) {
				logger.debug("add player: " + player.getId());
				if (player.getGame() != null && !player.getGame().equals(pokerGameIdentifier)) {
					leaveGame(pokerGameIdentifier, player.getId());
					joinGame(pokerGameIdentifier, player.getId());
				} else if (player.getGame() == null) {
					joinGame(pokerGameIdentifier, player.getId());
				}
			}
		} finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public void resetEvent(final SessionIdentifier sessionIdentifier) throws PokerServiceException, LoginRequiredException, PermissionDeniedException {
		final Duration duration = durationUtil.getDuration();
		try {
			logger.debug("resetEvent");
			pokerEventReseter.reset(sessionIdentifier);
		} catch (StorageException e) {
			throw new PokerServiceException(e);
		} catch (EntityIteratorException e) {
			throw new PokerServiceException(e);
		} catch (ValidationException e) {
			throw new PokerServiceException(e);
		} catch (AuthorizationServiceException e) {
			throw new PokerServiceException(e);
		} catch (AnalyticsServiceException e) {
			throw new PokerServiceException(e);
		} catch (CronControllerException e) {
			throw new PokerServiceException(e);
		} catch (ConfigurationServiceException e) {
			throw new PokerServiceException(e);
		} finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public boolean isJsonApiEnabled() {
		return pokerCoreConfig.isJsonApiEnabled();
	}

	@Override
	public String getJsonApiDashboardToken() {
		return pokerCoreConfig.getJsonApiDashboardToken();
	}

	@Override
	public Collection<PokerGame> getGamesRunning() throws PokerServiceException {
		final Duration duration = durationUtil.getDuration();
		try {
			return getGames(true);
		} finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public Collection<PokerGame> getGamesNotRunning() throws PokerServiceException {
		final Duration duration = durationUtil.getDuration();
		try {
			return getGames(false);
		} finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	private Collection<PokerGame> getGames(final boolean running) throws PokerServiceException {
		final Duration duration = durationUtil.getDuration();
		try {
			logger.debug("getGames - running: " + running);
			final EntityIterator<PokerGameBean> i = pokerGameDao.getEntityIterator();
			final List<PokerGame> result = new ArrayList<PokerGame>();
			while (i.hasNext()) {
				final PokerGameBean game = i.next();
				if (Boolean.TRUE.equals(game.getRunning()) == running) {
					result.add(game);
				}
			}
			return result;
		} catch (final StorageException e) {
			throw new PokerServiceException(e);
		} catch (EntityIteratorException e) {
			throw new PokerServiceException(e);
		} finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public void stopGame(final PokerGameIdentifier gameIdentifier) throws PokerServiceException, ValidationException {
		final Duration duration = durationUtil.getDuration();
		try {
			final PokerGameBean game = pokerGameDao.load(gameIdentifier);
			if (!Boolean.TRUE.equals(game.getRunning())) {
				throw new ValidationException(new ValidationResultImpl(new ValidationErrorSimple("game not running")));
			}
			game.setRunning(false);
			game.setCards(new ArrayList<PokerCardIdentifier>());
			game.setBoardCards(new ArrayList<PokerCardIdentifier>());
			game.setActivePlayers(new ArrayList<PokerPlayerIdentifier>());
			game.setButtonPosition(null);
			game.setRound(null);
			game.setActivePosition(null);
			game.setActivePositionTime(null);
			game.setBet(null);

			final Collection<PokerPlayerBean> players = pokerPlayerDao.load(game.getPlayers());
			for (final PokerPlayerBean player : players) {
				player.setCards(new ArrayList<PokerCardIdentifier>());
				player.setAmount(player.getAmount() + player.getBet());
				player.setBet(0l);
				player.setScore(toLong(player.getScore()) + game.getScore() / players.size());

				final ValidationResult errors = validationExecutor.validate(player);
				if (errors.hasErrors()) {
					logger.warn(player.getClass().getSimpleName() + " " + errors.toString());
					throw new ValidationException(errors);
				}
			}
			game.setScore(0l);

			final ValidationResult errors = validationExecutor.validate(game);
			if (errors.hasErrors()) {
				logger.warn(game.getClass().getSimpleName() + " " + errors.toString());
				throw new ValidationException(errors);
			}

			for (final PokerPlayerBean player : players) {
				pokerPlayerDao.save(player, new StorageValueList(pokerPlayerDao.getEncoding()).add(PokerPlayerBeanMapper.CARDS).add(PokerPlayerBeanMapper.SCORE).add(PokerPlayerBeanMapper.BET).add(PokerPlayerBeanMapper.AMOUNT));
				pokerEventPoster.firePokerPlayerAmountChangedEvent(player);
			}
			pokerGameDao.save(
				game,
				new StorageValueList(pokerGameDao.getEncoding()).add(PokerGameBeanMapper.RUNNING).add(PokerGameBeanMapper.CARDS).add(PokerGameBeanMapper.SCORE).add(PokerGameBeanMapper.CARD_POSITION).add(PokerGameBeanMapper.BUTTON_POSITION).add(PokerGameBeanMapper.ROUND).add(PokerGameBeanMapper.ACTIVE_PLAYERS)
					.add(PokerGameBeanMapper.ACTIVE_POSITION).add(PokerGameBeanMapper.ACTIVE_POSITION_TIME).add(PokerGameBeanMapper.BOARD_CARDS).add(PokerGameBeanMapper.BET));

			logger.info("stop game: " + game.getName());
		} catch (final StorageException e) {
			throw new PokerServiceException(e);
		} finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public void expectPokerAdminPermission(final SessionIdentifier sessionIdentifier) throws PermissionDeniedException, LoginRequiredException, PokerServiceException {
		try {
			authorizationService.expectPermission(sessionIdentifier, authorizationService.createPermissionIdentifier(PERMISSION_ADMIN));
		} catch (final AuthorizationServiceException e) {
			throw new PokerServiceException(e);
		}
	}

	@Override
	public void expectPokerPlayerOrAdminPermission(final SessionIdentifier sessionIdentifier) throws PermissionDeniedException, LoginRequiredException, PokerServiceException {
		final Duration duration = durationUtil.getDuration();
		try {
			authorizationService.expectOneOfPermissions(sessionIdentifier, authorizationService.createPermissionIdentifier(PERMISSION_ADMIN),
				authorizationService.createPermissionIdentifier(PERMISSION_PLAYER));
		} catch (final AuthorizationServiceException e) {
			throw new PokerServiceException(e);
		} finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public void expectPokerPlayerPermission(final SessionIdentifier sessionIdentifier) throws PermissionDeniedException, LoginRequiredException, PokerServiceException {
		final Duration duration = durationUtil.getDuration();
		try {
			authorizationService.expectPermission(sessionIdentifier, authorizationService.createPermissionIdentifier(PERMISSION_PLAYER));
		} catch (final AuthorizationServiceException e) {
			throw new PokerServiceException(e);
		} finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public boolean hasPokerAdminPermission(final SessionIdentifier sessionIdentifier) throws LoginRequiredException, PokerServiceException {
		final Duration duration = durationUtil.getDuration();
		try {
			return authorizationService.hasPermission(sessionIdentifier, authorizationService.createPermissionIdentifier(PERMISSION_ADMIN));
		} catch (final AuthorizationServiceException e) {
			throw new PokerServiceException(e);
		} finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public boolean hasPokerPlayerOrAdminPermission(final SessionIdentifier sessionIdentifier) throws LoginRequiredException, PokerServiceException {
		final Duration duration = durationUtil.getDuration();
		try {
			return authorizationService.hasOneOfPermissions(sessionIdentifier, authorizationService.createPermissionIdentifier(PERMISSION_ADMIN),
				authorizationService.createPermissionIdentifier(PERMISSION_PLAYER));
		} catch (final AuthorizationServiceException e) {
			throw new PokerServiceException(e);
		} finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public boolean hasPokerPlayerPermission(final SessionIdentifier sessionIdentifier) throws LoginRequiredException, PokerServiceException {
		final Duration duration = durationUtil.getDuration();
		try {
			return authorizationService.hasPermission(sessionIdentifier, authorizationService.createPermissionIdentifier(PERMISSION_PLAYER));
		} catch (final AuthorizationServiceException e) {
			throw new PokerServiceException(e);
		} finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public void updateGame(final PokerGameDto pokerGameDto) throws PokerServiceException, ValidationException {
		final Duration duration = durationUtil.getDuration();
		try {
			logger.debug("updateGame - name: " + pokerGameDto.getName());

			final PokerGameBean bean = pokerGameDao.load(pokerGameDto.getId());
			bean.setName(pokerGameDto.getName());
			bean.setStartCredits(pokerGameDto.getStartCredits());
			bean.setBigBlind(pokerGameDto.getBigBlind());
			bean.setAutoJoinAndRestart(pokerGameDto.getAutoJoinAndRestart());
			bean.setAutoFoldTimeout(pokerGameDto.getAutoFoldTimeout());

			final ValidationResult errors = validationExecutor.validate(bean);
			if (errors.hasErrors()) {
				logger.warn(bean.getClass().getSimpleName() + " " + errors.toString());
				throw new ValidationException(errors);
			}

			pokerGameDao.save(bean, new StorageValueList(pokerGameDao.getEncoding()).add(PokerGameBeanMapper.NAME).add(PokerGameBeanMapper.START_CREDITS).add(PokerGameBeanMapper.AUTO_FOLD_TIMEOUT).add(PokerGameBeanMapper.BIG_BLIND).add(PokerGameBeanMapper.AUTO_JOIN_AND_RESTART));
		} catch (final StorageException e) {
			throw new PokerServiceException(e);
		} finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public void updatePlayer(final PokerPlayerDto pokerPlayerDto) throws PokerServiceException, ValidationException {
		final Duration duration = durationUtil.getDuration();
		try {
			logger.debug("updatePlayer - name: " + pokerPlayerDto.getName());

			final PokerPlayerBean player = pokerPlayerDao.load(pokerPlayerDto.getId());
			player.setName(pokerPlayerDto.getName());
			player.setAmount(pokerPlayerDto.getAmount());
			player.setOwners(pokerPlayerDto.getOwners());
			player.setScore(pokerPlayerDto.getScore());

			final ValidationResult errors = validationExecutor.validate(player);
			if (errors.hasErrors()) {
				logger.warn(player.getClass().getSimpleName() + " " + errors.toString());
				throw new ValidationException(errors);
			}

			pokerPlayerDao.save(player,
				new StorageValueList(pokerPlayerDao.getEncoding()).add(PokerPlayerBeanMapper.NAME).add(PokerPlayerBeanMapper.AMOUNT).add(PokerPlayerBeanMapper.OWNERS).add(PokerPlayerBeanMapper.SCORE));
			pokerEventPoster.firePokerPlayerAmountChangedEvent(player);
		} catch (final StorageException e) {
			throw new PokerServiceException(e);
		} finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}
}
