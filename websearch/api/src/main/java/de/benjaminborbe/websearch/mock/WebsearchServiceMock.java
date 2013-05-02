package de.benjaminborbe.websearch.mock;

import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.websearch.api.WebsearchConfiguration;
import de.benjaminborbe.websearch.api.WebsearchConfigurationIdentifier;
import de.benjaminborbe.websearch.api.WebsearchPage;
import de.benjaminborbe.websearch.api.WebsearchPageIdentifier;
import de.benjaminborbe.websearch.api.WebsearchService;
import de.benjaminborbe.websearch.api.WebsearchServiceException;

import java.net.URL;
import java.util.Collection;
import java.util.List;

public class WebsearchServiceMock implements WebsearchService {

	@Override
	public void saveImages(final SessionIdentifier sessionIdentifier) throws WebsearchServiceException, PermissionDeniedException {
	}

	@Override
	public void refreshSearchIndex(final SessionIdentifier sessionIdentifier) throws WebsearchServiceException, PermissionDeniedException {
	}

	@Override
	public void expirePage(
		final SessionIdentifier sessionIdentifier,
		final WebsearchPageIdentifier page
	) throws WebsearchServiceException, PermissionDeniedException {
	}

	@Override
	public void clearIndex(final SessionIdentifier sessionIdentifier) throws WebsearchServiceException, PermissionDeniedException {
	}

	@Override
	public WebsearchPage getPage(
		final SessionIdentifier sessionIdentifier, final WebsearchPageIdentifier websearchPageIdentifier
	) throws WebsearchServiceException, PermissionDeniedException {
		return null;
	}

	@Override
	public void refreshPage(
		final SessionIdentifier sessionIdentifier,
		final WebsearchPageIdentifier page
	) throws WebsearchServiceException, PermissionDeniedException {
	}

	@Override
	public WebsearchPageIdentifier createPageIdentifier(final URL id) throws WebsearchServiceException {
		return new WebsearchPageIdentifier(id.toExternalForm());
	}

	@Override
	public WebsearchPageIdentifier createPageIdentifier(final String id) throws WebsearchServiceException {
		return null;
	}

	@Override
	public WebsearchConfigurationIdentifier createConfigurationIdentifier(final String id) throws WebsearchServiceException {
		return null;
	}

	@Override
	public WebsearchConfiguration getConfiguration(
		final SessionIdentifier sessionIdentifier,
		final WebsearchConfigurationIdentifier websearchConfigurationIdentifier
	)
		throws WebsearchServiceException, LoginRequiredException, PermissionDeniedException {
		return null;
	}

	@Override
	public Collection<WebsearchConfiguration> getConfigurations(final SessionIdentifier sessionIdentifier) throws WebsearchServiceException, LoginRequiredException,
		PermissionDeniedException {
		return null;
	}

	@Override
	public void deleteConfiguration(final SessionIdentifier sessionIdentifier, final WebsearchConfigurationIdentifier websearchConfigurationIdentifier)
		throws WebsearchServiceException, LoginRequiredException, PermissionDeniedException {
	}

	@Override
	public Collection<WebsearchPage> getPages(final SessionIdentifier sessionIdentifier) throws WebsearchServiceException, PermissionDeniedException {
		return null;
	}

	@Override
	public void expireAllPages(final SessionIdentifier sessionIdentifier) throws WebsearchServiceException, PermissionDeniedException {
	}

	@Override
	public WebsearchConfigurationIdentifier createConfiguration(
		final SessionIdentifier sessionIdentifier, final URL url, final List<String> excludes, final int expire,
		final long delay, final boolean activated
	) throws WebsearchServiceException, LoginRequiredException, PermissionDeniedException, ValidationException {
		return null;
	}

	@Override
	public void updateConfiguration(
		final SessionIdentifier sessionIdentifier, final WebsearchConfigurationIdentifier websearchConfigurationIdentifier, final URL url,
		final List<String> excludes, final int expire, final long delay, final boolean activated
	) throws WebsearchServiceException, LoginRequiredException,
		PermissionDeniedException, ValidationException {
	}

}
