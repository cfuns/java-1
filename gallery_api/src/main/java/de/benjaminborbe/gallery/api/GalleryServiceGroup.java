package de.benjaminborbe.gallery.api;

import java.util.Collection;

import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authentication.api.SuperAdminRequiredException;
import de.benjaminborbe.authorization.api.PermissionDeniedException;

public interface GalleryServiceGroup {

	GalleryGroupIdentifier getGroupByName(SessionIdentifier sessionIdentifier, String groupName) throws GalleryServiceException, LoginRequiredException, SuperAdminRequiredException;

	GalleryGroupIdentifier getGroupByNameShared(SessionIdentifier sessionIdentifier, String groupName) throws GalleryServiceException;

	void deleteGroup(final SessionIdentifier sessionIdentifier, GalleryGroupIdentifier galleryGroupIdentifier) throws GalleryServiceException, LoginRequiredException,
			SuperAdminRequiredException;

	GalleryGroupIdentifier createGroup(final SessionIdentifier sessionIdentifier, String groupName, Boolean shared) throws GalleryServiceException, LoginRequiredException,
			PermissionDeniedException, ValidationException, SuperAdminRequiredException;

	void updateGroup(final SessionIdentifier sessionIdentifier, GalleryGroupIdentifier galleryGroupIdentifier, String groupName, Boolean shared) throws GalleryServiceException,
			LoginRequiredException, PermissionDeniedException, ValidationException, SuperAdminRequiredException;

	Collection<GalleryGroupIdentifier> getGroupIdentifiers(final SessionIdentifier sessionIdentifier) throws GalleryServiceException, LoginRequiredException,
			SuperAdminRequiredException;

	Collection<GalleryGroup> getGroups(final SessionIdentifier sessionIdentifier) throws GalleryServiceException, LoginRequiredException, SuperAdminRequiredException;

	GalleryGroupIdentifier createGroupIdentifier(String id) throws GalleryServiceException;

	GalleryGroup getGroup(final SessionIdentifier sessionIdentifier, GalleryGroupIdentifier galleryGroupIdentifier) throws GalleryServiceException, LoginRequiredException,
			SuperAdminRequiredException;
}
