package de.benjaminborbe.gallery.api;

import java.util.List;

import de.benjaminborbe.authentication.api.SessionIdentifier;

public interface GalleryServiceEntry {

	GalleryEntryIdentifier createEntryIdentifier(String id) throws GalleryServiceException;

	GalleryEntryIdentifier createEntry(final SessionIdentifier sessionIdentifier, GalleryCollectionIdentifier galleryCollectionIdentifier, String entryName, String imagePreviewName,
			byte[] imagePreviewContent, String imagePreviewContentType, String imageName, byte[] imageContent, String imageContentType) throws GalleryServiceException;

	List<GalleryEntryIdentifier> getEntryIdentifiers(final SessionIdentifier sessionIdentifier, GalleryCollectionIdentifier galleryCollectionIdentifier)
			throws GalleryServiceException;

	GalleryEntry getEntry(final SessionIdentifier sessionIdentifier, GalleryEntryIdentifier id) throws GalleryServiceException;

	void deleteEntry(final SessionIdentifier sessionIdentifier, GalleryEntryIdentifier id) throws GalleryServiceException;

}
