package de.benjaminborbe.cron.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.authorization.api.AuthorizationServiceException;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.cron.api.CronExecutionInfo;
import de.benjaminborbe.cron.api.CronIdentifier;
import de.benjaminborbe.cron.api.CronJob;
import de.benjaminborbe.cron.api.CronService;
import de.benjaminborbe.cron.api.CronServiceException;
import de.benjaminborbe.cron.util.CronExecutionHistory;
import de.benjaminborbe.cron.util.CronJobRegistry;
import de.benjaminborbe.cron.util.CronMessageSender;
import de.benjaminborbe.message.api.MessageServiceException;
import de.benjaminborbe.tools.mapper.MapException;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Singleton
public class CronServiceImpl implements CronService {

	private final CronExecutionHistory cronExecutionHistory;

	private final Logger logger;

	private final AuthorizationService authorizationService;

	private final CronMessageSender cronMessageSender;

	private final CronJobRegistry cronJobRegistry;

	@Inject
	public CronServiceImpl(
		final Logger logger,
		final CronJobRegistry cronJobRegistry,
		final AuthorizationService authorizationService,
		final CronExecutionHistory cronExecutionHistory,
		final CronMessageSender cronMessageSender) {
		this.logger = logger;
		this.cronJobRegistry = cronJobRegistry;
		this.authorizationService = authorizationService;
		this.cronExecutionHistory = cronExecutionHistory;
		this.cronMessageSender = cronMessageSender;
	}

	@Override
	public List<CronExecutionInfo> getLatestExecutionInfos(final int amount) {
		logger.debug("getLatestExecutionInfos amount = " + amount);
		final List<CronExecutionInfo> result = cronExecutionHistory.getLatestElements(amount);
		logger.debug("getLatestExecutionInfos found " + result.size());
		return result;
	}

	@Override
	public Collection<CronIdentifier> getCronIdentifiers(final SessionIdentifier sessionIdentifier) throws CronServiceException, LoginRequiredException, PermissionDeniedException {
		try {
			authorizationService.expectAdminRole(sessionIdentifier);

			final List<CronIdentifier> result = new ArrayList<>();

			for (final CronJob cron : cronJobRegistry.getAll()) {
				result.add(createCronIdentifier(cron.getClass().getName()));
			}

			return result;
		} catch (final AuthorizationServiceException e) {
			throw new CronServiceException(e);
		}
	}

	@Override
	public CronIdentifier createCronIdentifier(final String name) {
		return name != null ? new CronIdentifier(name) : null;
	}

	@Override
	public void triggerCron(final SessionIdentifier sessionIdentifier, final CronIdentifier cronIdentifier) throws CronServiceException, LoginRequiredException,
		PermissionDeniedException {
		try {
			authorizationService.expectAdminRole(sessionIdentifier);
			cronMessageSender.send(cronIdentifier.getId());
		} catch (final AuthorizationServiceException | MessageServiceException | MapException e) {
			throw new CronServiceException(e);
		}
	}
}
