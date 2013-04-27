package de.benjaminborbe.note.dao;

import com.google.inject.Provider;
import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.note.api.NoteIdentifier;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.api.StorageService;
import de.benjaminborbe.storage.tools.DaoStorage;
import de.benjaminborbe.storage.tools.EntityIterator;
import de.benjaminborbe.storage.tools.StorageValueMap;
import de.benjaminborbe.tools.date.CalendarUtil;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class NoteDaoStorage extends DaoStorage<NoteBean, NoteIdentifier> implements NoteDao {

	private static final String COLUMN_FAMILY = "note";

	@Inject
	public NoteDaoStorage(
		final Logger logger,
		final StorageService storageService,
		final Provider<NoteBean> beanProvider,
		final NoteBeanMapper mapper,
		final NoteIdentifierBuilder identifierBuilder,
		final CalendarUtil calendarUtil
	) {
		super(logger, storageService, beanProvider, mapper, identifierBuilder, calendarUtil);
	}

	@Override
	protected String getColumnFamily() {
		return COLUMN_FAMILY;
	}

	@Override
	public EntityIterator<NoteBean> getEntityIterator(final UserIdentifier user) throws StorageException {
		return getEntityIterator(new StorageValueMap(getEncoding()).add(NoteBeanMapper.OWNER, user.getId()));
	}

}
