package de.benjaminborbe.poker.event;

import de.benjaminborbe.analytics.api.AnalyticsReportDto;
import de.benjaminborbe.analytics.api.AnalyticsService;
import de.benjaminborbe.analytics.api.AnalyticsServiceException;
import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.eventbus.api.EventbusService;
import de.benjaminborbe.poker.api.PokerPlayerIdentifier;
import de.benjaminborbe.poker.api.PokerService;
import de.benjaminborbe.poker.api.PokerServiceException;
import de.benjaminborbe.poker.player.PokerPlayerBean;
import org.slf4j.Logger;

import javax.inject.Inject;

public class PokerPlayerCreatedEventHandlerImpl implements PokerPlayerCreatedEventHandler {

	private final Logger logger;

	private final EventbusService eventbusService;

	private final AnalyticsService analyticsService;

	private final AnalyticsReportUtil analyticsReportUtil;

	private final PokerService pokerService;

	@Inject
	public PokerPlayerCreatedEventHandlerImpl(
		final Logger logger,
		final EventbusService eventbusService,
		final AnalyticsService analyticsService,
		final AnalyticsReportUtil analyticsReportUtil,
		final PokerService pokerService
	) {
		this.logger = logger;
		this.eventbusService = eventbusService;
		this.analyticsService = analyticsService;
		this.analyticsReportUtil = analyticsReportUtil;
		this.pokerService = pokerService;
	}

	@Override
	public void onPlayerCreated(final PokerPlayerBean playerBean) throws PokerServiceException, PermissionDeniedException, AnalyticsServiceException, LoginRequiredException, ValidationException {
		logger.debug("onPlayerCreated: {}", playerBean.getId());

		createPlayerScoreReport(pokerService.getPokerServerSessionIdentifier(), playerBean.getId(), playerBean.getName());
		createPlayerAmountReport(pokerService.getPokerServerSessionIdentifier(), playerBean.getId(), playerBean.getName());

		eventbusService.fireEvent(new PokerPlayerAmountChangedEvent(playerBean.getId(), playerBean.getAmount()));
		eventbusService.fireEvent(new PokerPlayerScoreChangedEvent(playerBean.getId(), playerBean.getScore()));
	}

	private void createPlayerScoreReport(
		final SessionIdentifier sessionIdentifier,
		final PokerPlayerIdentifier pokerPlayerIdentifier,
		final String playerName
	) throws PermissionDeniedException, AnalyticsServiceException, LoginRequiredException, ValidationException {
		final AnalyticsReportDto report = new AnalyticsReportDto();
		report.setAggregation(analyticsReportUtil.AGGREGATION);
		report.setName(analyticsReportUtil.createPlayerScoreReportName(pokerPlayerIdentifier));
		report.setDescription(playerName);
		analyticsService.createReport(sessionIdentifier, report);
	}

	private void createPlayerAmountReport(
		final SessionIdentifier sessionIdentifier,
		final PokerPlayerIdentifier pokerPlayerIdentifier,
		final String playerName
	) throws PermissionDeniedException, AnalyticsServiceException, LoginRequiredException, ValidationException {
		final AnalyticsReportDto report = new AnalyticsReportDto();
		report.setAggregation(analyticsReportUtil.AGGREGATION);
		report.setName(analyticsReportUtil.createPlayerAmountReportName(pokerPlayerIdentifier));
		report.setDescription(playerName);
		analyticsService.createReport(sessionIdentifier, report);
	}

}
