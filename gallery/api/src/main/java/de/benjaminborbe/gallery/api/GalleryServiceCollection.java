package de.benjaminborbe.gallery.api;

import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.PermissionDeniedException;

import java.util.Collection;

public interface GalleryServiceCollection {

	GalleryCollectionIdentifier getCollectionIdentifierByName(
		SessionIdentifier sessionIdentifier,
		String name
	) throws GalleryServiceException, LoginRequiredException,
		PermissionDeniedException;

	GalleryCollectionIdentifier getCollectionIdentifierByNameShared(String name) throws GalleryServiceException;

	Collection<GalleryCollection> getCollectionsWithGroup(
		final SessionIdentifier sessionIdentifier,
		GalleryGroupIdentifier galleryGroupIdentifier
	) throws GalleryServiceException,
		LoginRequiredException, PermissionDeniedException;

	Collection<GalleryCollection> getCollectionsWithGroupShared(GalleryGroupIdentifier galleryGroupIdentifier)
		throws GalleryServiceException;

	Collection<GalleryCollection> getCollections(final SessionIdentifier sessionIdentifier) throws GalleryServiceException, LoginRequiredException, PermissionDeniedException;

	Collection<GalleryCollection> getCollectionsShared() throws GalleryServiceException;

	void deleteCollection(
		final SessionIdentifier sessionIdentifier,
		GalleryCollectionIdentifier galleryCollectionIdentifier
	) throws GalleryServiceException, LoginRequiredException,
		PermissionDeniedException;

	GalleryCollectionIdentifier createCollection(
		final SessionIdentifier sessionIdentifier, GalleryGroupIdentifier galleryGroupIdentifier, String collectionName, Long prio,
		Boolean shared
	) throws GalleryServiceException, LoginRequiredException, PermissionDeniedException, ValidationException;

	void updateCollection(
		final SessionIdentifier sessionIdentifier, GalleryCollectionIdentifier galleryCollectionIdentifier, GalleryGroupIdentifier galleryGroupIdentifier,
		String collectionName, Long prio, Boolean shared
	) throws GalleryServiceException, LoginRequiredException, PermissionDeniedException, ValidationException;

	Collection<GalleryCollectionIdentifier> getCollectionIdentifiers(final SessionIdentifier sessionIdentifier) throws GalleryServiceException, LoginRequiredException,
		PermissionDeniedException;

	GalleryCollectionIdentifier createCollectionIdentifier(String id) throws GalleryServiceException;

	GalleryCollection getCollection(
		final SessionIdentifier sessionIdentifier,
		GalleryCollectionIdentifier galleryCollectionIdentifier
	) throws GalleryServiceException,
		LoginRequiredException, PermissionDeniedException;

	GalleryCollection getCollectionShared(GalleryCollectionIdentifier galleryCollectionIdentifier) throws GalleryServiceException;

}
