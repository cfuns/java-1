package de.benjaminborbe.websearch.api;

import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.PermissionDeniedException;

import java.net.URL;
import java.util.Collection;
import java.util.List;

public interface WebsearchServiceConfiguration {

	WebsearchConfigurationIdentifier createConfigurationIdentifier(String id) throws WebsearchServiceException;

	WebsearchConfiguration getConfiguration(
		SessionIdentifier sessionIdentifier,
		WebsearchConfigurationIdentifier websearchConfigurationIdentifier
	) throws WebsearchServiceException,
		LoginRequiredException, PermissionDeniedException;

	Collection<WebsearchConfiguration> getConfigurations(SessionIdentifier sessionIdentifier) throws WebsearchServiceException, LoginRequiredException, PermissionDeniedException;

	void deleteConfiguration(
		SessionIdentifier sessionIdentifier,
		WebsearchConfigurationIdentifier websearchConfigurationIdentifier
	) throws WebsearchServiceException,
		LoginRequiredException, PermissionDeniedException;

	WebsearchConfigurationIdentifier createConfiguration(
		SessionIdentifier sessionIdentifier,
		URL url,
		final List<String> excludes,
		int expire,
		long delay,
		boolean activated
	)
		throws WebsearchServiceException, LoginRequiredException, PermissionDeniedException, ValidationException;

	void updateConfiguration(
		SessionIdentifier sessionIdentifier, WebsearchConfigurationIdentifier websearchConfigurationIdentifier, URL url, final List<String> excludes,
		int expire, long delay, boolean activated
	) throws WebsearchServiceException, LoginRequiredException, PermissionDeniedException, ValidationException;

}
