package de.benjaminborbe.lunch.api;

import java.util.Calendar;
import java.util.Collection;

import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.PermissionDeniedException;

public interface LunchService {

	Collection<Lunch> getLunchs(SessionIdentifier sessionIdentifier, String fullname) throws LunchServiceException, LoginRequiredException, PermissionDeniedException;

	Collection<Lunch> getLunchs(SessionIdentifier sessionIdentifier) throws LunchServiceException, LoginRequiredException, PermissionDeniedException;

	Collection<Lunch> getLunchsArchiv(SessionIdentifier sessionIdentifier, String fullname) throws LunchServiceException, LoginRequiredException, PermissionDeniedException;

	Collection<String> getSubscribeUser(SessionIdentifier sessionIdentifier, Calendar day) throws LunchServiceException, LoginRequiredException, PermissionDeniedException;

	void book(SessionIdentifier sessionIdentifier, Calendar day, Collection<String> users) throws LunchServiceException, LoginRequiredException, PermissionDeniedException;

}
