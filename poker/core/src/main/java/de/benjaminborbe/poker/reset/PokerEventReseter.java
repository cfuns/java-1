package de.benjaminborbe.poker.reset;

import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.authorization.api.AuthorizationServiceException;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.authorization.api.PermissionIdentifier;
import de.benjaminborbe.authorization.api.RoleIdentifier;
import de.benjaminborbe.poker.api.PokerService;
import de.benjaminborbe.poker.api.PokerServiceException;
import de.benjaminborbe.poker.game.PokerGameBean;
import de.benjaminborbe.poker.game.PokerGameDao;
import de.benjaminborbe.poker.gamecreator.PokerGameCreator;
import de.benjaminborbe.poker.player.PokerPlayerBean;
import de.benjaminborbe.poker.player.PokerPlayerDao;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.tools.EntityIterator;
import de.benjaminborbe.storage.tools.EntityIteratorException;
import org.slf4j.Logger;

import javax.inject.Inject;

public class PokerEventReseter {

	private final Logger logger;

	private final PokerService pokerService;

	private final PokerGameDao pokerGameDao;

	private final PokerPlayerDao pokerPlayerDao;

	private final PokerGameCreator pokerGameCreator;

	private final AuthorizationService authorizationService;

	@Inject
	public PokerEventReseter(
		final Logger logger,
		final PokerService pokerService,
		final PokerGameDao pokerGameDao,
		final PokerPlayerDao pokerPlayerDao,
		final PokerGameCreator pokerGameCreator,
		final AuthorizationService authorizationService
	) {
		this.logger = logger;
		this.pokerService = pokerService;
		this.pokerGameDao = pokerGameDao;
		this.pokerPlayerDao = pokerPlayerDao;
		this.pokerGameCreator = pokerGameCreator;
		this.authorizationService = authorizationService;
	}

	public void reset(final SessionIdentifier sessionIdentifier) throws StorageException, EntityIteratorException, ValidationException, LoginRequiredException, PermissionDeniedException, AuthorizationServiceException, PokerServiceException {
		logger.debug("reset poker event - started");
		deleteAllGames();
		deleteAllPlayer();
		createNewGame();
		allowAllLoggedInUserPlayPoker(sessionIdentifier);
		logger.debug("reset poker event - finished");
	}

	private void allowAllLoggedInUserPlayPoker(final SessionIdentifier sessionIdentifier) throws LoginRequiredException, PermissionDeniedException, AuthorizationServiceException, PokerServiceException {
		final PermissionIdentifier permissionIdentifier = pokerService.getPokerPlayerPermissionIdentifier();
		final RoleIdentifier roleIdentifier = authorizationService.getLoggedInRoleIdentifier();
		authorizationService.addPermissionRole(sessionIdentifier, permissionIdentifier, roleIdentifier);
	}

	private void createNewGame() throws ValidationException, StorageException {
		logger.debug("createNewGame");
		pokerGameCreator.createDefaultGame();
	}

	private void deleteAllPlayer() throws EntityIteratorException, StorageException {
		logger.debug("deleteAllPlayer");
		final EntityIterator<PokerPlayerBean> entityIterator = pokerPlayerDao.getEntityIterator();
		while (entityIterator.hasNext()) {
			pokerPlayerDao.delete(entityIterator.next());
		}
	}

	private void deleteAllGames() throws StorageException, EntityIteratorException {
		logger.debug("deleteAllGames");
		final EntityIterator<PokerGameBean> entityIterator = pokerGameDao.getEntityIterator();
		while (entityIterator.hasNext()) {
			pokerGameDao.delete(entityIterator.next());
		}
	}

}
