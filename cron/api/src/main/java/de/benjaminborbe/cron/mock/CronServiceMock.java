package de.benjaminborbe.cron.mock;

import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.cron.api.CronExecutionInfo;
import de.benjaminborbe.cron.api.CronIdentifier;
import de.benjaminborbe.cron.api.CronService;
import de.benjaminborbe.cron.api.CronServiceException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CronServiceMock implements CronService {

	@Override
	public List<CronExecutionInfo> getLatestExecutionInfos(final int amount) {
		return new ArrayList<>();
	}

	@Override
	public Collection<CronIdentifier> getCronIdentifiers(final SessionIdentifier sessionIdentifier) throws CronServiceException, LoginRequiredException, PermissionDeniedException {
		return null;
	}

	@Override
	public void triggerCron(final SessionIdentifier sessionIdentifier, final CronIdentifier cronIdentifier) throws CronServiceException, LoginRequiredException,
		PermissionDeniedException {
	}

	@Override
	public CronIdentifier createCronIdentifier(final String name) {
		return name != null ? new CronIdentifier(name) : null;
	}
}
