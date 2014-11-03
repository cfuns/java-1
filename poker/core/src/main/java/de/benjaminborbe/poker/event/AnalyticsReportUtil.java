package de.benjaminborbe.poker.event;

import de.benjaminborbe.analytics.api.AnalyticsReportAggregation;
import de.benjaminborbe.analytics.api.AnalyticsReportIdentifier;
import de.benjaminborbe.analytics.api.AnalyticsService;
import de.benjaminborbe.analytics.api.AnalyticsServiceException;
import de.benjaminborbe.poker.api.PokerPlayerIdentifier;

import javax.inject.Inject;

public class AnalyticsReportUtil {

	public static final AnalyticsReportAggregation AGGREGATION = AnalyticsReportAggregation.LATEST;

	private final AnalyticsService analyticsService;

	@Inject
	public AnalyticsReportUtil(final AnalyticsService analyticsService) {
		this.analyticsService = analyticsService;
	}

	public String createPlayerAmountReportName(final PokerPlayerIdentifier id) {
		return "PokerPlayerAmount-" + id.getId();
	}

	public String createPlayerScoreReportName(final PokerPlayerIdentifier id) {
		return "PokerPlayerScore-" + id.getId();
	}

	public AnalyticsReportIdentifier createPlayerAmountReportIdentifier(
		final PokerPlayerIdentifier playerIdentifier
	) throws AnalyticsServiceException {
		return analyticsService.createAnalyticsReportIdentifier(createPlayerAmountReportName(playerIdentifier), AGGREGATION);
	}

	public AnalyticsReportIdentifier createPlayerScoreReportIdentifier(
		final PokerPlayerIdentifier playerIdentifier
	) {
		return analyticsService.createAnalyticsReportIdentifier(createPlayerScoreReportName(playerIdentifier), AGGREGATION);
	}
}
