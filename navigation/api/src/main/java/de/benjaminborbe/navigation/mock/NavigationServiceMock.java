package de.benjaminborbe.navigation.mock;

import de.benjaminborbe.navigation.api.NavigationEntry;
import de.benjaminborbe.navigation.api.NavigationService;

import java.util.Collection;
import java.util.HashSet;

public class NavigationServiceMock implements NavigationService {

	@Override
	public Collection<NavigationEntry> getNavigationEntries() {
		return new HashSet<>();
	}

}
