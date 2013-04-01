package de.benjaminborbe.gallery.api;

import de.benjaminborbe.authentication.api.LoginRequiredException;

public interface GalleryServiceImage {

	GalleryImageIdentifier createImageIdentifier(String id) throws GalleryServiceException;

	GalleryImage getImage(GalleryImageIdentifier id) throws GalleryServiceException, LoginRequiredException;

}
