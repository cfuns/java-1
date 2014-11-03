package de.benjaminborbe.poker.reset;

import de.benjaminborbe.analytics.api.AnalyticsService;
import de.benjaminborbe.analytics.api.AnalyticsServiceException;
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

	private final AnalyticsService analyticsService;

	@Inject
	public PokerEventReseter(
		final Logger logger,
		final PokerService pokerService,
		final PokerGameDao pokerGameDao,
		final PokerPlayerDao pokerPlayerDao,
		final PokerGameCreator pokerGameCreator,
		final AuthorizationService authorizationService, final AnalyticsService analyticsService
	) {
		this.logger = logger;
		this.pokerService = pokerService;
		this.pokerGameDao = pokerGameDao;
		this.pokerPlayerDao = pokerPlayerDao;
		this.pokerGameCreator = pokerGameCreator;
		this.authorizationService = authorizationService;
		this.analyticsService = analyticsService;
	}

	public void reset(final SessionIdentifier sessionIdentifier) throws StorageException, EntityIteratorException, ValidationException, LoginRequiredException, PermissionDeniedException, AuthorizationServiceException, PokerServiceException, AnalyticsServiceException {
		logger.debug("reset poker event - started");
		deleteAllGames();
		deleteAllPlayer();
		createNewGame();
		allowAllLoggedInUserPlayPoker(sessionIdentifier);
		allowPokerSystemUserAnalytics(sessionIdentifier);
		logger.debug("reset poker event - finished");
	}

	private void allowPokerSystemUserAnalytics(final SessionIdentifier sessionIdentifier) throws AuthorizationServiceException, PermissionDeniedException, ValidationException, AnalyticsServiceException, LoginRequiredException, PokerServiceException {
		logger.debug("allowPokerSystemUserAnalytics");
		final RoleIdentifier roleIdentifier = authorizationService.createRoleIdentifier(PokerService.SERVER_ROLE);
		authorizationService.createRole(sessionIdentifier, roleIdentifier);
		authorizationService.addPermissionRole(sessionIdentifier, analyticsService.getAnalyticsAdminPermissionIdentifier(), roleIdentifier);
		authorizationService.addUserRole(sessionIdentifier, pokerService.getPokerServerUserIdentifier(), roleIdentifier);
	}

	private void allowAllLoggedInUserPlayPoker(final SessionIdentifier sessionIdentifier) throws LoginRequiredException, PermissionDeniedException, AuthorizationServiceException, PokerServiceException {
		logger.debug("allowAllLoggedInUserPlayPoker");
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
