package de.benjaminborbe.bookmark.gui.service;

import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.authorization.api.AuthorizationServiceException;
import de.benjaminborbe.authorization.api.PermissionIdentifier;
import de.benjaminborbe.bookmark.api.BookmarkService;
import de.benjaminborbe.bookmark.gui.BookmarkGuiConstants;
import de.benjaminborbe.navigation.api.NavigationEntry;

import javax.inject.Inject;

public class BookmarkGuiNavigationEntry implements NavigationEntry {

	private final AuthorizationService authorizationService;

	@Inject
	public BookmarkGuiNavigationEntry(final AuthorizationService authorizationService) {
		this.authorizationService = authorizationService;
	}

	@Override
	public String getTitle() {
		return "Bookmark";
	}

	@Override
	public String getURL() {
		return "/" + BookmarkGuiConstants.NAME + BookmarkGuiConstants.URL_LIST;
	}

	@Override
	public boolean isVisible(final SessionIdentifier sessionIdentifier) {
		try {
			final PermissionIdentifier permissionIdentifier = authorizationService.createPermissionIdentifier(BookmarkService.PERMISSION);
			return authorizationService.hasPermission(sessionIdentifier, permissionIdentifier);
		} catch (final AuthorizationServiceException e) {
			return false;
		}
	}

}
