package de.benjaminborbe.authentication.dao;

import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.tools.Dao;

public interface SessionDao extends Dao<SessionBean, SessionIdentifier> {

	SessionBean findOrCreate(SessionIdentifier sessionId) throws StorageException;

}
