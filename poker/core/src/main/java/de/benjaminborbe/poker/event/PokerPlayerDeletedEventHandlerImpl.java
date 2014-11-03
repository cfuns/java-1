package de.benjaminborbe.poker.event;

import de.benjaminborbe.analytics.api.AnalyticsService;
import de.benjaminborbe.analytics.api.AnalyticsServiceException;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.poker.api.PokerPlayerIdentifier;
import de.benjaminborbe.poker.api.PokerService;
import de.benjaminborbe.poker.api.PokerServiceException;

import javax.inject.Inject;

public class PokerPlayerDeletedEventHandlerImpl implements PokerPlayerDeletedEventHandler {

	private final AnalyticsService analyticsService;

	private final AnalyticsReportUtil analyticsReportUtil;

	private final PokerService pokerService;

	@Inject
	public PokerPlayerDeletedEventHandlerImpl(
		final AnalyticsService analyticsService,
		final AnalyticsReportUtil analyticsReportUtil,
		final PokerService pokerService
	) {
		this.analyticsService = analyticsService;
		this.analyticsReportUtil = analyticsReportUtil;
		this.pokerService = pokerService;
	}

	@Override
	public void onPlayerDeleted(final PokerPlayerIdentifier playerIdentifier) throws AnalyticsServiceException, LoginRequiredException, PermissionDeniedException, PokerServiceException {
		final SessionIdentifier pokerServerSessionIdentifier = pokerService.getPokerServerSessionIdentifier();
		analyticsService.deleteReport(pokerServerSessionIdentifier, analyticsReportUtil.createPlayerScoreReportIdentifier(playerIdentifier));
		analyticsService.deleteReport(pokerServerSessionIdentifier, analyticsReportUtil.createPlayerAmountReportIdentifier(playerIdentifier));
	}
}
