package de.benjaminborbe.poker.event;

import de.benjaminborbe.analytics.api.AnalyticsReportIdentifier;
import de.benjaminborbe.analytics.api.AnalyticsService;
import de.benjaminborbe.analytics.api.AnalyticsServiceException;
import de.benjaminborbe.poker.api.PokerPlayerIdentifier;
import org.ops4j.peaberry.ServiceUnavailableException;
import org.slf4j.Logger;

import javax.inject.Inject;

public class PokerPlayerAmountChangedEventHandlerImpl implements PokerPlayerAmountChangedEventHandler {

	private final Logger logger;

	private final AnalyticsService analyticsService;

	private final AnalyticsReportUtil analyticsReportUtil;

	@Inject
	public PokerPlayerAmountChangedEventHandlerImpl(final Logger logger, final AnalyticsService analyticsService, final AnalyticsReportUtil analyticsReportUtil) {
		this.logger = logger;
		this.analyticsService = analyticsService;
		this.analyticsReportUtil = analyticsReportUtil;
	}

	@Override
	public void onAmountChanged(final PokerPlayerIdentifier pokerPlayerIdentifier, final Long amount) {
		trackPlayerAmount(pokerPlayerIdentifier, amount);
	}

	private void trackPlayerAmount(final PokerPlayerIdentifier id, final Long amount) {
		if (amount != null) {
			try {
				final AnalyticsReportIdentifier analyticsReportIdentifier = analyticsReportUtil.createPlayerAmountReportIdentifier(id);
				analyticsService.addReportValue(analyticsReportIdentifier, amount);
			} catch (final AnalyticsServiceException e) {
				logger.trace("trackPlayerAmount failed", e);
			} catch (ServiceUnavailableException e) {
				logger.trace("trackPlayerAmount failed", e);
			}
		}
	}
}
