package de.benjaminborbe.gallery.api;

import java.util.Collection;
import java.util.List;

import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.PermissionDeniedException;

public interface GalleryServiceEntry {

	GalleryEntryIdentifier createEntryIdentifier(String id) throws GalleryServiceException;

	GalleryEntryIdentifier createEntry(final SessionIdentifier sessionIdentifier, GalleryCollectionIdentifier galleryCollectionIdentifier, String entryName, Long priority,
			String imagePreviewName, byte[] imagePreviewContent, String imagePreviewContentType, String imageName, byte[] imageContent, String imageContentType)
			throws GalleryServiceException, LoginRequiredException, PermissionDeniedException, ValidationException;

	List<GalleryEntryIdentifier> getEntryIdentifiers(final SessionIdentifier sessionIdentifier, GalleryCollectionIdentifier galleryCollectionIdentifier)
			throws GalleryServiceException;

	GalleryEntry getEntry(final SessionIdentifier sessionIdentifier, GalleryEntryIdentifier id) throws GalleryServiceException;

	void deleteEntry(final SessionIdentifier sessionIdentifier, GalleryEntryIdentifier id) throws GalleryServiceException;

	Collection<GalleryEntry> getEntries(SessionIdentifier sessionIdentifier, GalleryCollectionIdentifier id) throws GalleryServiceException;

}
