package de.benjaminborbe.gallery.api;

import java.util.Collection;
import java.util.List;

import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authentication.api.SuperAdminRequiredException;
import de.benjaminborbe.authorization.api.PermissionDeniedException;

public interface GalleryServiceEntry {

	GalleryEntryIdentifier createEntryIdentifier(String id) throws GalleryServiceException;

	GalleryEntryIdentifier createEntry(final SessionIdentifier sessionIdentifier, GalleryCollectionIdentifier galleryCollectionIdentifier, String entryName, Long priority,
			String imagePreviewName, byte[] imagePreviewContent, String imagePreviewContentType, String imageName, byte[] imageContent, String imageContentType, final Boolean shared)
			throws GalleryServiceException, LoginRequiredException, PermissionDeniedException, ValidationException, SuperAdminRequiredException;

	List<GalleryEntryIdentifier> getEntryIdentifiers(final SessionIdentifier sessionIdentifier, GalleryCollectionIdentifier galleryCollectionIdentifier)
			throws GalleryServiceException, LoginRequiredException, PermissionDeniedException, SuperAdminRequiredException;

	GalleryEntry getEntry(final SessionIdentifier sessionIdentifier, GalleryEntryIdentifier id) throws GalleryServiceException, LoginRequiredException, PermissionDeniedException,
			SuperAdminRequiredException;

	void deleteEntry(final SessionIdentifier sessionIdentifier, GalleryEntryIdentifier id) throws GalleryServiceException, LoginRequiredException, PermissionDeniedException,
			SuperAdminRequiredException;

	Collection<GalleryEntry> getEntries(SessionIdentifier sessionIdentifier, GalleryCollectionIdentifier id) throws GalleryServiceException, LoginRequiredException,
			PermissionDeniedException, SuperAdminRequiredException;

	Collection<GalleryEntry> getEntriesShared(SessionIdentifier sessionIdentifier, GalleryCollectionIdentifier id) throws GalleryServiceException;

	void updateEntry(SessionIdentifier sessionIdentifier, GalleryEntryIdentifier galleryEntryIdentifier, GalleryCollectionIdentifier galleryCollectionIdentifier, String entryName,
			Long priority, String imagePreviewName, String imageName, Boolean shared) throws GalleryServiceException, ValidationException, LoginRequiredException,
			SuperAdminRequiredException;

}
