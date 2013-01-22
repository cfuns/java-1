package de.benjaminborbe.monitoring.gui.service;

import com.google.inject.Inject;

import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.monitoring.api.MonitoringService;
import de.benjaminborbe.monitoring.api.MonitoringServiceException;
import de.benjaminborbe.monitoring.gui.MonitoringGuiConstants;
import de.benjaminborbe.navigation.api.NavigationEntry;

public class MonitoringGuiNavigationEntry implements NavigationEntry {

	private final MonitoringService monitoringService;

	@Inject
	public MonitoringGuiNavigationEntry(final MonitoringService monitoringService) {
		this.monitoringService = monitoringService;
	}

	@Override
	public String getTitle() {
		return "Monitoring";
	}

	@Override
	public String getURL() {
		return "/" + MonitoringGuiConstants.NAME;
	}

	@Override
	public boolean isVisible(final SessionIdentifier sessionIdentifier) {
		try {
			return monitoringService.hasMonitoringViewOrAdminRole(sessionIdentifier);
		}
		catch (final LoginRequiredException e) {
			return false;
		}
		catch (final MonitoringServiceException e) {
			return false;
		}
	}

}
