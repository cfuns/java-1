package de.benjaminborbe.dashboard.service;

import de.benjaminborbe.navigation.api.NavigationEntry;

public class DashboardNavigationEntry implements NavigationEntry {

	public DashboardNavigationEntry() {
	}

	@Override
	public String getTitle() {
		return "Dashboard";
	}

	@Override
	public String getURL() {
		return "/bb/dashboard";
	}
}
