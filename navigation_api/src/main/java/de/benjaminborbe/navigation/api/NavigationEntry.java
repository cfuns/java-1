package de.benjaminborbe.navigation.api;

import de.benjaminborbe.authentication.api.SessionIdentifier;

public interface NavigationEntry {

	String getTitle();

	String getURL();

	boolean isVisible(SessionIdentifier sessionIdentifier);
}
