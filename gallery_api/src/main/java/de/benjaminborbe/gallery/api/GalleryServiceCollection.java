package de.benjaminborbe.gallery.api;

import java.util.Collection;

import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authentication.api.SuperAdminRequiredException;
import de.benjaminborbe.authorization.api.PermissionDeniedException;

public interface GalleryServiceCollection {

	GalleryCollectionIdentifier getCollectionIdentifierByName(SessionIdentifier sessionIdentifier, String name) throws GalleryServiceException, LoginRequiredException,
			SuperAdminRequiredException;

	GalleryCollectionIdentifier getCollectionIdentifierByNamePublic(SessionIdentifier sessionIdentifier, String name) throws GalleryServiceException;

	Collection<GalleryCollection> getCollectionsWithGroup(final SessionIdentifier sessionIdentifier, GalleryGroupIdentifier galleryGroupIdentifier) throws GalleryServiceException,
			LoginRequiredException, SuperAdminRequiredException;

	Collection<GalleryCollection> getCollectionsWithGroupPublic(final SessionIdentifier sessionIdentifier, GalleryGroupIdentifier galleryGroupIdentifier)
			throws GalleryServiceException;

	Collection<GalleryCollection> getCollections(final SessionIdentifier sessionIdentifier) throws GalleryServiceException, LoginRequiredException, SuperAdminRequiredException;

	Collection<GalleryCollection> getCollectionsPublic(final SessionIdentifier sessionIdentifier) throws GalleryServiceException;

	void deleteCollection(final SessionIdentifier sessionIdentifier, GalleryCollectionIdentifier galleryCollectionIdentifier) throws GalleryServiceException, LoginRequiredException,
			SuperAdminRequiredException;

	GalleryCollectionIdentifier createCollection(final SessionIdentifier sessionIdentifier, GalleryGroupIdentifier galleryGroupIdentifier, String collectionName, Long prio)
			throws GalleryServiceException, LoginRequiredException, PermissionDeniedException, ValidationException, SuperAdminRequiredException;

	Collection<GalleryCollectionIdentifier> getCollectionIdentifiers(final SessionIdentifier sessionIdentifier) throws GalleryServiceException, LoginRequiredException,
			SuperAdminRequiredException;

	GalleryCollectionIdentifier createCollectionIdentifier(String id) throws GalleryServiceException;

	GalleryCollection getCollection(final SessionIdentifier sessionIdentifier, GalleryCollectionIdentifier galleryCollectionIdentifier) throws GalleryServiceException,
			LoginRequiredException, SuperAdminRequiredException;

	GalleryCollection getCollectionPublic(final SessionIdentifier sessionIdentifier, GalleryCollectionIdentifier galleryCollectionIdentifier) throws GalleryServiceException;

}
