package de.benjaminborbe.authentication.util;

import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.tools.dao.Dao;

public interface SessionDao extends Dao<SessionBean, String> {

	SessionBean findBySessionId(SessionIdentifier sessionId);

	SessionBean findOrCreateBySessionId(SessionIdentifier sessionId);

}
