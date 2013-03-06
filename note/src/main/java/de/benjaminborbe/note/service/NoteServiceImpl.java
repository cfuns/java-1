package de.benjaminborbe.note.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

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
import de.benjaminborbe.note.api.Note;
import de.benjaminborbe.note.api.NoteDto;
import de.benjaminborbe.note.api.NoteIdentifier;
import de.benjaminborbe.note.api.NoteService;
import de.benjaminborbe.note.api.NoteServiceException;
import de.benjaminborbe.note.dao.NoteBean;
import de.benjaminborbe.note.dao.NoteDao;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.tools.EntityIterator;
import de.benjaminborbe.storage.tools.EntityIteratorException;
import de.benjaminborbe.tools.util.IdGeneratorUUID;
import de.benjaminborbe.tools.validation.ValidationExecutor;

@Singleton
public class NoteServiceImpl implements NoteService {

	private final Logger logger;

	private final AuthenticationService authenticationService;

	private final AuthorizationService authorizationService;

	private final NoteDao noteDao;

	private final IdGeneratorUUID idGeneratorUUID;

	private final ValidationExecutor validationExecutor;

	@Inject
	public NoteServiceImpl(
			final Logger logger,
			final ValidationExecutor validationExecutor,
			final AuthenticationService authenticationService,
			final AuthorizationService authorizationService,
			final NoteDao noteDao,
			final IdGeneratorUUID idGeneratorUUID) {
		this.logger = logger;
		this.validationExecutor = validationExecutor;
		this.authenticationService = authenticationService;
		this.authorizationService = authorizationService;
		this.noteDao = noteDao;
		this.idGeneratorUUID = idGeneratorUUID;
	}

	@Override
	public NoteIdentifier createNoteIdentifier(final String id) {
		return id != null ? new NoteIdentifier(id) : null;
	}

	@Override
	public NoteIdentifier createNote(final SessionIdentifier sessionIdentifier, final NoteDto noteDto) throws PermissionDeniedException, LoginRequiredException,
			NoteServiceException, ValidationException {
		try {
			authorizationService.existsPermission(authorizationService.createPermissionIdentifier(PERMISSION));
			logger.debug("createNote");

			final NoteBean bean = noteDao.create();
			final NoteIdentifier id = new NoteIdentifier(idGeneratorUUID.nextId());
			bean.setId(id);
			bean.setOwner(authenticationService.getCurrentUser(sessionIdentifier));
			bean.setTitle(noteDto.getTitle());
			bean.setContent(noteDto.getContent());

			final ValidationResult errors = validationExecutor.validate(bean);
			if (errors.hasErrors()) {
				logger.warn(bean.getClass().getSimpleName() + " " + errors.toString());
				throw new ValidationException(errors);
			}

			noteDao.save(bean);
			return id;
		}
		catch (final AuthorizationServiceException | AuthenticationServiceException | StorageException e) {
			throw new NoteServiceException(e);
		}
	}

	@Override
	public void updateNote(final SessionIdentifier sessionIdentifier, final NoteDto noteDto) throws PermissionDeniedException, LoginRequiredException, NoteServiceException,
			ValidationException {
		try {
			authorizationService.existsPermission(authorizationService.createPermissionIdentifier(PERMISSION));
			logger.debug("updateNote");

			final NoteBean bean = noteDao.load(noteDto.getId());
			authorizationService.expectUser(sessionIdentifier, bean.getOwner());
			bean.setTitle(noteDto.getTitle());
			bean.setContent(noteDto.getContent());

			final ValidationResult errors = validationExecutor.validate(bean);
			if (errors.hasErrors()) {
				logger.warn(bean.getClass().getSimpleName() + " " + errors.toString());
				throw new ValidationException(errors);
			}

			noteDao.save(bean);
		}
		catch (final AuthorizationServiceException | StorageException e) {
			throw new NoteServiceException(e);
		}
	}

	@Override
	public void deleteNote(final SessionIdentifier sessionIdentifier, final NoteIdentifier noteIdentifier) throws PermissionDeniedException, LoginRequiredException,
			NoteServiceException {
		try {
			authorizationService.existsPermission(authorizationService.createPermissionIdentifier(PERMISSION));
			logger.debug("deleteNote");
			final NoteBean note = noteDao.load(noteIdentifier);
			authorizationService.expectUser(sessionIdentifier, note.getOwner());
			noteDao.delete(note);
		}
		catch (final AuthorizationServiceException | StorageException e) {
			throw new NoteServiceException(e);
		}
	}

	@Override
	public Collection<Note> getNodes(final SessionIdentifier sessionIdentifier) throws PermissionDeniedException, LoginRequiredException, NoteServiceException {
		try {
			authorizationService.existsPermission(authorizationService.createPermissionIdentifier(PERMISSION));
			logger.debug("getNodes");
			final UserIdentifier user = authenticationService.getCurrentUser(sessionIdentifier);
			final EntityIterator<NoteBean> i = noteDao.getEntityIterator(user);
			final List<Note> result = new ArrayList<Note>();
			while (i.hasNext()) {
				result.add(i.next());
			}
			return result;
		}
		catch (final AuthenticationServiceException | EntityIteratorException | AuthorizationServiceException | StorageException e) {
			throw new NoteServiceException(e);
		}
	}
}
