package de.benjaminborbe.note.dao;

import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.note.api.NoteIdentifier;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.tools.Dao;
import de.benjaminborbe.storage.tools.EntityIterator;

public interface NoteDao extends Dao<NoteBean, NoteIdentifier> {

	EntityIterator<NoteBean> getEntityIterator(UserIdentifier user) throws StorageException;

}
