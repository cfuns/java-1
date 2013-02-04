package de.benjaminborbe.cron.api;

import java.util.Collection;
import java.util.List;

import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.PermissionDeniedException;

public interface CronService {

	List<CronExecutionInfo> getLatestExecutionInfos(int amount) throws CronServiceException, LoginRequiredException, PermissionDeniedException;

	Collection<CronIdentifier> getCronIdentifiers(final SessionIdentifier sessionIdentifier) throws CronServiceException, LoginRequiredException, PermissionDeniedException;

	void triggerCron(final SessionIdentifier sessionIdentifier, CronIdentifier cronIdentifier) throws CronServiceException, LoginRequiredException, PermissionDeniedException;

	CronIdentifier createCronIdentifier(String name);

}
