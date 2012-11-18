package de.benjaminborbe.gallery.api;

import java.util.Collection;

import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.PermissionDeniedException;

public interface GalleryServiceCollection {

	Collection<GalleryCollection> getCollectionsWithGroup(final SessionIdentifier sessionIdentifier, GalleryGroupIdentifier galleryGroupIdentifier) throws GalleryServiceException;

	Collection<GalleryCollection> getCollections(final SessionIdentifier sessionIdentifier) throws GalleryServiceException;

	void deleteCollection(final SessionIdentifier sessionIdentifier, GalleryCollectionIdentifier galleryCollectionIdentifier) throws GalleryServiceException;

	GalleryCollectionIdentifier createCollection(final SessionIdentifier sessionIdentifier, GalleryGroupIdentifier galleryGroupIdentifier, String collectionName, Long prio)
			throws GalleryServiceException, LoginRequiredException, PermissionDeniedException, ValidationException;

	Collection<GalleryCollectionIdentifier> getCollectionIdentifiers(final SessionIdentifier sessionIdentifier) throws GalleryServiceException;

	GalleryCollectionIdentifier createCollectionIdentifier(String id) throws GalleryServiceException;

	GalleryCollection getCollection(final SessionIdentifier sessionIdentifier, GalleryCollectionIdentifier galleryCollectionIdentifier) throws GalleryServiceException;

}
