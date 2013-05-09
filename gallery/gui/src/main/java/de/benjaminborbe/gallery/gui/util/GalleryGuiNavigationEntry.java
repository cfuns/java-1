package de.benjaminborbe.gallery.gui.util;

import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.gallery.api.GalleryService;
import de.benjaminborbe.gallery.api.GalleryServiceException;
import de.benjaminborbe.gallery.gui.GalleryGuiConstants;
import de.benjaminborbe.navigation.api.NavigationEntry;

import javax.inject.Inject;

public class GalleryGuiNavigationEntry implements NavigationEntry {

	private final GalleryService galleryService;

	@Inject
	public GalleryGuiNavigationEntry(final GalleryService galleryService) {
		this.galleryService = galleryService;
	}

	@Override
	public String getTitle() {
		return "Gallery";
	}

	@Override
	public String getURL() {
		return "/" + GalleryGuiConstants.NAME;
	}

	@Override
	public boolean isVisible(final SessionIdentifier sessionIdentifier) {
		try {
			return galleryService.hasPermission(sessionIdentifier);
		} catch (final GalleryServiceException e) {
			return false;
		}
	}

}
