package de.benjaminborbe.lunch.api;

import java.util.Calendar;
import java.util.Collection;

import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.kiosk.api.KioskUser;

public interface LunchService {

	String PERMISSION_VIEW = "lunchView";

	String PERMISSION_VIEW_ARCHIVE = "lunchViewArchive";

	String PERMISSION_BOOKING = "lunchBooking";

	String PERMISSION_NOTIFICATION = "lunchNotification";

	Collection<Lunch> getLunchs(SessionIdentifier sessionIdentifier, String fullname) throws LunchServiceException, LoginRequiredException, PermissionDeniedException;

	Collection<Lunch> getLunchs(SessionIdentifier sessionIdentifier) throws LunchServiceException, LoginRequiredException, PermissionDeniedException;

	Collection<Lunch> getLunchsArchiv(SessionIdentifier sessionIdentifier, String fullname) throws LunchServiceException, LoginRequiredException, PermissionDeniedException;

	Collection<KioskUser> getSubscribeUser(SessionIdentifier sessionIdentifier, Calendar day) throws LunchServiceException, LoginRequiredException, PermissionDeniedException;

	Collection<KioskUser> getBookedUser(SessionIdentifier sessionIdentifier, Calendar day) throws LunchServiceException, LoginRequiredException, PermissionDeniedException;

	void book(SessionIdentifier sessionIdentifier, Calendar day, Collection<Long> users) throws LunchServiceException, LoginRequiredException, PermissionDeniedException;

	boolean isNotificationActivated(UserIdentifier userIdentifier) throws LunchServiceException;

	void activateNotification(UserIdentifier userIdentifier) throws LunchServiceException, ValidationException;

	void deactivateNotification(UserIdentifier userIdentifier) throws LunchServiceException, ValidationException;
}
