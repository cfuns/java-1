package de.benjaminborbe.monitoring.gui.service;

import java.util.Collection;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.monitoring.api.CheckResult;
import de.benjaminborbe.monitoring.api.MonitoringService;
import de.benjaminborbe.monitoring.api.MonitoringServiceException;
import de.benjaminborbe.tools.url.UrlUtil;

@Singleton
public class MonitoringGuiWidgetLiveImpl extends MonitoringGuiWidgetBase implements MonitoringGuiWidgetLive {

	private final MonitoringService monitoringService;

	@Inject
	public MonitoringGuiWidgetLiveImpl(final Logger logger, final MonitoringService monitoringService, final UrlUtil urlUtil, final AuthenticationService authenticationService) {
		super(logger, urlUtil, authenticationService);
		this.monitoringService = monitoringService;
	}

	@Override
	protected Collection<CheckResult> getResults(final SessionIdentifier sessionIdentifier) throws MonitoringServiceException, PermissionDeniedException {
		return monitoringService.checkRootNode(sessionIdentifier);
	}
}
