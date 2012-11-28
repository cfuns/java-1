package de.benjaminborbe.gallery.api;

import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;

public interface GalleryServiceImage {

	GalleryImageIdentifier createImageIdentifier(String id) throws GalleryServiceException;

	GalleryImage getImage(final SessionIdentifier sessionIdentifier, GalleryImageIdentifier id) throws GalleryServiceException, LoginRequiredException;

}
