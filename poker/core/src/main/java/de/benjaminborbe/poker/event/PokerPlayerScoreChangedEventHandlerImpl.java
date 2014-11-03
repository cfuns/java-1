package de.benjaminborbe.poker.event;

import de.benjaminborbe.analytics.api.AnalyticsReportIdentifier;
import de.benjaminborbe.analytics.api.AnalyticsService;
import de.benjaminborbe.analytics.api.AnalyticsServiceException;
import de.benjaminborbe.poker.api.PokerPlayerIdentifier;
import org.ops4j.peaberry.ServiceUnavailableException;
import org.slf4j.Logger;

import javax.inject.Inject;

public class PokerPlayerScoreChangedEventHandlerImpl implements PokerPlayerScoreChangedEventHandler {

	private final Logger logger;

	private final AnalyticsService analyticsService;

	private final AnalyticsReportUtil analyticsReportUtil;

	@Inject
	public PokerPlayerScoreChangedEventHandlerImpl(final Logger logger, final AnalyticsService analyticsService, final AnalyticsReportUtil analyticsReportUtil) {
		this.logger = logger;
		this.analyticsService = analyticsService;
		this.analyticsReportUtil = analyticsReportUtil;
	}

	@Override
	public void onScoreChanged(final PokerPlayerIdentifier id, final Long score) {
		trackPlayerScore(id, score);
	}

	private void trackPlayerScore(final PokerPlayerIdentifier id, final Long score) {
		if (score != null) {
			try {
				final AnalyticsReportIdentifier analyticsReportIdentifier = analyticsReportUtil.createPlayerScoreReportIdentifier(id);
				analyticsService.addReportValue(analyticsReportIdentifier, score);
			} catch (final AnalyticsServiceException e) {
				logger.trace("trackPlayerScore failed", e);
			} catch (ServiceUnavailableException e) {
				logger.trace("trackPlayerScore failed", e);
			}
		}
	}
}
