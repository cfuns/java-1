package de.benjaminborbe.lunch.api;

import java.util.Calendar;
import java.util.Collection;

import de.benjaminborbe.authentication.api.SessionIdentifier;

public interface LunchService {

	Collection<Lunch> getLunchs(SessionIdentifier sessionIdentifier, String fullname) throws LunchServiceException;

	Collection<Lunch> getLunchs(SessionIdentifier sessionIdentifier) throws LunchServiceException;

	Collection<Lunch> getLunchsArchiv(SessionIdentifier sessionIdentifier, String fullname) throws LunchServiceException;

	Collection<String> getSubscribeUser(SessionIdentifier sessionIdentifier, Calendar day) throws LunchServiceException;

}
