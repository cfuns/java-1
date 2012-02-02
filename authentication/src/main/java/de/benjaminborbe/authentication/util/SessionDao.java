package de.benjaminborbe.authentication.util;

import de.benjaminborbe.tools.dao.Dao;

public interface SessionDao extends Dao<SessionBean, String> {

	SessionBean findBySessionId(String sessionId);

	SessionBean findOrCreateBySessionId(String sessionId);

}
