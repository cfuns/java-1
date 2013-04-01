package de.benjaminborbe.navigation.mock;

import java.util.Collection;
import java.util.HashSet;

import de.benjaminborbe.navigation.api.NavigationEntry;
import de.benjaminborbe.navigation.api.NavigationService;

public class NavigationServiceMock implements NavigationService {

	@Override
	public Collection<NavigationEntry> getNavigationEntries() {
		return new HashSet<NavigationEntry>();
	}

}
