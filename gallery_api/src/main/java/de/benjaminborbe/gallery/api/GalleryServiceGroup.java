package de.benjaminborbe.gallery.api;

import java.util.Collection;

import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.PermissionDeniedException;

public interface GalleryServiceGroup {

	GalleryGroupIdentifier getGroupByName(SessionIdentifier sessionIdentifier, String groupName) throws GalleryServiceException;

	void deleteGroup(final SessionIdentifier sessionIdentifier, GalleryGroupIdentifier galleryGroupIdentifier) throws GalleryServiceException;

	GalleryGroupIdentifier createGroup(final SessionIdentifier sessionIdentifier, String groupName) throws GalleryServiceException, LoginRequiredException,
			PermissionDeniedException, ValidationException;

	Collection<GalleryGroupIdentifier> getGroupIdentifiers(final SessionIdentifier sessionIdentifier) throws GalleryServiceException;

	Collection<GalleryGroup> getGroups(final SessionIdentifier sessionIdentifier) throws GalleryServiceException;

	GalleryGroupIdentifier createGroupIdentifier(String id) throws GalleryServiceException;

	GalleryGroup getGroup(final SessionIdentifier sessionIdentifier, GalleryGroupIdentifier galleryGroupIdentifier) throws GalleryServiceException;
}
