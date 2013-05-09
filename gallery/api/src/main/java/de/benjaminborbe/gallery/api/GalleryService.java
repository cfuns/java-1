package de.benjaminborbe.gallery.api;

import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.PermissionDeniedException;

public interface GalleryService extends GalleryServiceGroup, GalleryServiceCollection, GalleryServiceEntry, GalleryServiceImage {

	void expectPermission(SessionIdentifier sessionIdentifier) throws PermissionDeniedException, LoginRequiredException, GalleryServiceException;

	boolean hasPermission(SessionIdentifier sessionIdentifier) throws GalleryServiceException;
}
