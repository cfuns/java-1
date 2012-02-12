package de.benjaminborbe.authentication.util;

import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.tools.Dao;

public interface SessionDao extends Dao<SessionBean, String> {

	SessionBean findBySessionId(SessionIdentifier sessionId) throws StorageException;

	SessionBean findOrCreateBySessionId(SessionIdentifier sessionId) throws StorageException;

}
