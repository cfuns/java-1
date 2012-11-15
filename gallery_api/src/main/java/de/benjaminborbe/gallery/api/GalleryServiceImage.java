package de.benjaminborbe.gallery.api;

public interface GalleryServiceImage {

	GalleryImageIdentifier createImageIdentifier(String id) throws GalleryServiceException;

	GalleryImage getImage(GalleryImageIdentifier id) throws GalleryServiceException;

}
