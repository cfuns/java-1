package de.benjaminborbe.analytics.gui.service;

import de.benjaminborbe.analytics.api.AnalyticsService;
import de.benjaminborbe.analytics.api.AnalyticsServiceException;
import de.benjaminborbe.analytics.gui.AnalyticsGuiConstants;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.navigation.api.NavigationEntry;

import javax.inject.Inject;

public class AnalyticsGuiNavigationEntry implements NavigationEntry {

	private final AnalyticsService analyticsService;

	@Inject
	public AnalyticsGuiNavigationEntry(final AnalyticsService analyticsService) {
		this.analyticsService = analyticsService;
	}

	@Override
	public String getTitle() {
		return "Analytics";
	}

	@Override
	public String getURL() {
		return "/" + AnalyticsGuiConstants.NAME;
	}

	@Override
	public boolean isVisible(final SessionIdentifier sessionIdentifier) {
		try {
			return analyticsService.hasAnalyticsViewOrAdminPermission(sessionIdentifier);
		} catch (final LoginRequiredException e) {
			return false;
		} catch (final AnalyticsServiceException e) {
			return false;
		}
	}

}
