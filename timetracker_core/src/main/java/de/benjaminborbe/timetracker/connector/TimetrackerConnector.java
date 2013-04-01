package de.benjaminborbe.timetracker.connector;

import java.util.Date;

public interface TimetrackerConnector {

	String getSessionId(String username, String password) throws TimetrackerConnectorException;

	boolean isCompleted(String sessionId, Date date) throws TimetrackerConnectorException;
}
