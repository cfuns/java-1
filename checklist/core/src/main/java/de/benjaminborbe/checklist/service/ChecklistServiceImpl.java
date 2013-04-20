package de.benjaminborbe.checklist.service;

import javax.inject.Inject;
import javax.inject.Singleton;
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
import de.benjaminborbe.checklist.api.ChecklistEntry;
import de.benjaminborbe.checklist.api.ChecklistEntryIdentifier;
import de.benjaminborbe.checklist.api.ChecklistList;
import de.benjaminborbe.checklist.api.ChecklistListIdentifier;
import de.benjaminborbe.checklist.api.ChecklistService;
import de.benjaminborbe.checklist.api.ChecklistServiceException;
import de.benjaminborbe.checklist.dao.ChecklistEntryBean;
import de.benjaminborbe.checklist.dao.ChecklistEntryDao;
import de.benjaminborbe.checklist.dao.ChecklistListBean;
import de.benjaminborbe.checklist.dao.ChecklistListDao;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.tools.EntityIterator;
import de.benjaminborbe.storage.tools.EntityIteratorException;
import de.benjaminborbe.tools.util.Duration;
import de.benjaminborbe.tools.util.DurationUtil;
import de.benjaminborbe.tools.util.IdGeneratorUUID;
import de.benjaminborbe.tools.validation.ValidationExecutor;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Singleton
public class ChecklistServiceImpl implements ChecklistService {

	private static final long DURATION_WARN = 300;

	private final Logger logger;

	private final DurationUtil durationUtil;

	private final AuthenticationService authenticationService;

	private final ChecklistEntryDao checklistEntryDao;

	private final ChecklistListDao checklistListDao;

	private final AuthorizationService authorizationService;

	private final IdGeneratorUUID idGeneratorUUID;

	private final ValidationExecutor validationExecutor;

	@Inject
	public ChecklistServiceImpl(
		final Logger logger,
		final DurationUtil durationUtil,
		final AuthenticationService authenticationService,
		final AuthorizationService authorizationService,
		final ChecklistEntryDao checklistEntryDao,
		final ChecklistListDao checklistListDao,
		final IdGeneratorUUID idGeneratorUUID,
		final ValidationExecutor validationExecutor) {
		this.logger = logger;
		this.durationUtil = durationUtil;
		this.authenticationService = authenticationService;
		this.authorizationService = authorizationService;
		this.checklistEntryDao = checklistEntryDao;
		this.checklistListDao = checklistListDao;
		this.idGeneratorUUID = idGeneratorUUID;
		this.validationExecutor = validationExecutor;
	}

	@Override
	public void delete(final SessionIdentifier sessionIdentifier, final ChecklistListIdentifier id) throws ChecklistServiceException, PermissionDeniedException,
		LoginRequiredException {
		final Duration duration = durationUtil.getDuration();
		try {
			final PermissionIdentifier permissionIdentifier = authorizationService.createPermissionIdentifier(ChecklistService.PERMISSION);
			authorizationService.existsPermission(permissionIdentifier);

			logger.debug("delete");

			final ChecklistListBean bean = checklistListDao.load(id);
			if (bean == null) {
				logger.debug("already deleted");
				return;
			}
			authorizationService.expectUser(sessionIdentifier, bean.getOwner());

			final EntityIterator<ChecklistEntryBean> i = checklistEntryDao.getEntityIteratorForListAndUser(id, authenticationService.getCurrentUser(sessionIdentifier));
			while (i.hasNext()) {
				checklistEntryDao.delete(i.next());
			}

			checklistListDao.delete(bean);
		} catch (final AuthenticationServiceException | EntityIteratorException | AuthorizationServiceException | StorageException e) {
			throw new ChecklistServiceException(e);
		} finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public ChecklistList read(final SessionIdentifier sessionIdentifier, final ChecklistListIdentifier id) throws ChecklistServiceException, PermissionDeniedException,
		LoginRequiredException {
		final Duration duration = durationUtil.getDuration();
		try {
			final PermissionIdentifier permissionIdentifier = authorizationService.createPermissionIdentifier(ChecklistService.PERMISSION);
			authorizationService.existsPermission(permissionIdentifier);

			logger.debug("delete");

			final ChecklistListBean bean = checklistListDao.load(id);
			if (bean == null) {
				logger.info("list not found with id " + id);
				return null;
			}
			authorizationService.expectUser(sessionIdentifier, bean.getOwner());

			return bean;
		} catch (final AuthorizationServiceException | StorageException e) {
			throw new ChecklistServiceException(e);
		} finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public ChecklistListIdentifier create(final SessionIdentifier sessionIdentifier, final ChecklistList checklistList) throws ChecklistServiceException, PermissionDeniedException,
		ValidationException, LoginRequiredException {
		final Duration duration = durationUtil.getDuration();
		try {
			final PermissionIdentifier permissionIdentifier = authorizationService.createPermissionIdentifier(ChecklistService.PERMISSION);
			authorizationService.existsPermission(permissionIdentifier);

			logger.debug("create");

			final UserIdentifier userIdentifier = authenticationService.getCurrentUser(sessionIdentifier);

			final ChecklistListIdentifier checklistListIdentifier = new ChecklistListIdentifier(idGeneratorUUID.nextId());
			final ChecklistListBean bean = checklistListDao.create();
			bean.setId(checklistListIdentifier);
			bean.setName(checklistList.getName());
			bean.setOwner(userIdentifier);

			final ValidationResult errors = validationExecutor.validate(bean);
			if (errors.hasErrors()) {
				logger.warn("List " + errors.toString());
				throw new ValidationException(errors);
			}
			checklistListDao.save(bean);

			return checklistListIdentifier;
		} catch (final AuthenticationServiceException | StorageException | AuthorizationServiceException e) {
			throw new ChecklistServiceException(e);
		} finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public void update(final SessionIdentifier sessionIdentifier, final ChecklistList checklistList) throws ChecklistServiceException, PermissionDeniedException,
		ValidationException, LoginRequiredException {
		final Duration duration = durationUtil.getDuration();
		try {
			final PermissionIdentifier permissionIdentifier = authorizationService.createPermissionIdentifier(ChecklistService.PERMISSION);
			authorizationService.existsPermission(permissionIdentifier);

			logger.debug("update");

			final ChecklistListBean bean = checklistListDao.load(checklistList.getId());
			authorizationService.expectUser(sessionIdentifier, bean.getOwner());

			bean.setName(checklistList.getName());

			final ValidationResult errors = validationExecutor.validate(bean);
			if (errors.hasErrors()) {
				logger.warn("List " + errors.toString());
				throw new ValidationException(errors);
			}
			checklistListDao.save(bean);
		} catch (final AuthorizationServiceException | StorageException e) {
			throw new ChecklistServiceException(e);
		} finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public void delete(final SessionIdentifier sessionIdentifier, final ChecklistEntryIdentifier id) throws ChecklistServiceException, PermissionDeniedException,
		LoginRequiredException {
		final Duration duration = durationUtil.getDuration();
		try {
			final PermissionIdentifier permissionIdentifier = authorizationService.createPermissionIdentifier(ChecklistService.PERMISSION);
			authorizationService.existsPermission(permissionIdentifier);

			logger.debug("delete");

			final ChecklistEntryBean bean = checklistEntryDao.load(id);
			if (bean == null) {
				logger.debug("already deleted");
				return;
			}
			authorizationService.expectUser(sessionIdentifier, bean.getOwner());

			checklistEntryDao.delete(bean);
		} catch (final AuthorizationServiceException | StorageException e) {
			throw new ChecklistServiceException(e);
		} finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public ChecklistEntry read(final SessionIdentifier sessionIdentifier, final ChecklistEntryIdentifier id) throws ChecklistServiceException, PermissionDeniedException,
		LoginRequiredException {
		final Duration duration = durationUtil.getDuration();
		try {
			final PermissionIdentifier permissionIdentifier = authorizationService.createPermissionIdentifier(ChecklistService.PERMISSION);
			authorizationService.existsPermission(permissionIdentifier);

			logger.debug("delete");

			final ChecklistEntryBean bean = checklistEntryDao.load(id);
			if (bean == null) {
				logger.info("entry not found with id " + id);
				return null;
			}
			authorizationService.expectUser(sessionIdentifier, bean.getOwner());

			return bean;
		} catch (final StorageException | AuthorizationServiceException e) {
			throw new ChecklistServiceException(e);
		} finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public ChecklistEntryIdentifier create(final SessionIdentifier sessionIdentifier, final ChecklistEntry checklistEntry) throws ChecklistServiceException,
		PermissionDeniedException, ValidationException, LoginRequiredException {
		final Duration duration = durationUtil.getDuration();
		try {
			final PermissionIdentifier permissionIdentifier = authorizationService.createPermissionIdentifier(ChecklistService.PERMISSION);
			authorizationService.existsPermission(permissionIdentifier);

			logger.debug("create");

			final UserIdentifier userIdentifier = authenticationService.getCurrentUser(sessionIdentifier);
			final ChecklistListBean list = checklistListDao.load(checklistEntry.getListId());
			authorizationService.expectUser(sessionIdentifier, list.getOwner());

			final ChecklistEntryIdentifier checklistEntryIdentifier = new ChecklistEntryIdentifier(idGeneratorUUID.nextId());
			final ChecklistEntryBean bean = checklistEntryDao.create();
			bean.setId(checklistEntryIdentifier);
			bean.setName(checklistEntry.getName());
			bean.setOwner(userIdentifier);
			bean.setListId(list.getId());
			bean.setCompleted(false);

			final ValidationResult errors = validationExecutor.validate(bean);
			if (errors.hasErrors()) {
				logger.warn("Entry " + errors.toString());
				throw new ValidationException(errors);
			}
			checklistEntryDao.save(bean);

			return checklistEntryIdentifier;
		} catch (final AuthenticationServiceException | AuthorizationServiceException | StorageException e) {
			throw new ChecklistServiceException(e);
		} finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public void update(final SessionIdentifier sessionIdentifier, final ChecklistEntry checklistEntry) throws ChecklistServiceException, PermissionDeniedException,
		ValidationException, LoginRequiredException {
		final Duration duration = durationUtil.getDuration();
		try {
			final PermissionIdentifier permissionIdentifier = authorizationService.createPermissionIdentifier(ChecklistService.PERMISSION);
			authorizationService.existsPermission(permissionIdentifier);

			logger.debug("update");

			final ChecklistEntryBean bean = checklistEntryDao.load(checklistEntry.getId());
			authorizationService.expectUser(sessionIdentifier, bean.getOwner());

			bean.setName(checklistEntry.getName());

			final ValidationResult errors = validationExecutor.validate(bean);
			if (errors.hasErrors()) {
				logger.warn("Entry " + errors.toString());
				throw new ValidationException(errors);
			}
			checklistEntryDao.save(bean);
		} catch (final StorageException | AuthorizationServiceException e) {
			throw new ChecklistServiceException(e);
		} finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public Collection<ChecklistList> getLists(final SessionIdentifier sessionIdentifier) throws ChecklistServiceException, PermissionDeniedException, LoginRequiredException {
		final Duration duration = durationUtil.getDuration();
		try {
			final PermissionIdentifier permissionIdentifier = authorizationService.createPermissionIdentifier(ChecklistService.PERMISSION);
			authorizationService.existsPermission(permissionIdentifier);

			logger.debug("getLists");
			final List<ChecklistList> result = new ArrayList<>();
			final EntityIterator<ChecklistListBean> i = checklistListDao.getEntityIteratorForUser(authenticationService.getCurrentUser(sessionIdentifier));
			while (i.hasNext()) {
				result.add(i.next());
			}
			return result;
		} catch (final AuthenticationServiceException | StorageException | AuthorizationServiceException | EntityIteratorException e) {
			throw new ChecklistServiceException(e);
		} finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public Collection<ChecklistEntry> getEntries(final SessionIdentifier sessionIdentifier, final ChecklistListIdentifier checklistListIdentifier) throws ChecklistServiceException,
		PermissionDeniedException, LoginRequiredException {
		final Duration duration = durationUtil.getDuration();
		try {
			final PermissionIdentifier permissionIdentifier = authorizationService.createPermissionIdentifier(ChecklistService.PERMISSION);
			authorizationService.existsPermission(permissionIdentifier);

			logger.debug("getEntries");
			final List<ChecklistEntry> result = new ArrayList<>();
			final EntityIterator<ChecklistEntryBean> i = checklistEntryDao.getEntityIteratorForListAndUser(checklistListIdentifier,
				authenticationService.getCurrentUser(sessionIdentifier));
			while (i.hasNext()) {
				result.add(i.next());
			}
			return result;
		} catch (final StorageException | AuthorizationServiceException | AuthenticationServiceException | EntityIteratorException e) {
			throw new ChecklistServiceException(e);
		} finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public void uncomplete(final SessionIdentifier sessionIdentifier, final ChecklistEntryIdentifier identifier) throws ChecklistServiceException, PermissionDeniedException,
		LoginRequiredException, ValidationException {
		final Duration duration = durationUtil.getDuration();
		try {
			final PermissionIdentifier permissionIdentifier = authorizationService.createPermissionIdentifier(ChecklistService.PERMISSION);
			authorizationService.existsPermission(permissionIdentifier);

			logger.debug("update");

			final ChecklistEntryBean bean = checklistEntryDao.load(identifier);
			authorizationService.expectUser(sessionIdentifier, bean.getOwner());

			bean.setCompleted(false);

			final ValidationResult errors = validationExecutor.validate(bean);
			if (errors.hasErrors()) {
				logger.warn("Entry " + errors.toString());
				throw new ValidationException(errors);
			}
			checklistEntryDao.save(bean);
		} catch (final StorageException | AuthorizationServiceException e) {
			throw new ChecklistServiceException(e);
		} finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	@Override
	public void complete(final SessionIdentifier sessionIdentifier, final ChecklistEntryIdentifier identifier) throws ChecklistServiceException, PermissionDeniedException,
		LoginRequiredException, ValidationException {
		final Duration duration = durationUtil.getDuration();
		try {
			final PermissionIdentifier permissionIdentifier = authorizationService.createPermissionIdentifier(ChecklistService.PERMISSION);
			authorizationService.existsPermission(permissionIdentifier);

			logger.debug("update");

			final ChecklistEntryBean bean = checklistEntryDao.load(identifier);
			authorizationService.expectUser(sessionIdentifier, bean.getOwner());

			bean.setCompleted(true);

			final ValidationResult errors = validationExecutor.validate(bean);
			if (errors.hasErrors()) {
				logger.warn("Entry " + errors.toString());
				throw new ValidationException(errors);
			}
			checklistEntryDao.save(bean);
		} catch (final StorageException | AuthorizationServiceException e) {
			throw new ChecklistServiceException(e);
		} finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}
}
