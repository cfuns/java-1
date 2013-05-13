package de.benjaminborbe.selenium.configuration.xml.api;

import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.selenium.api.SeleniumConfigurationIdentifier;

import java.util.Collection;

public class SeleniumConfigurationXmlServiceMock implements SeleniumConfigurationXmlService {

	@Override
	public Collection<SeleniumConfigurationIdentifier> list(final SessionIdentifier sessionIdentifier) throws SeleniumConfigurationXmlServiceException, LoginRequiredException, PermissionDeniedException {
		return null;
	}

	@Override
	public SeleniumConfigurationIdentifier addXml(
		final SessionIdentifier sessionIdentifier,
		final String xml
	) throws SeleniumConfigurationXmlServiceException, LoginRequiredException, PermissionDeniedException {
		return null;
	}

	@Override
	public void deleteXml(
		final SessionIdentifier sessionIdentifier, final SeleniumConfigurationIdentifier seleniumConfiguration
	) throws SeleniumConfigurationXmlServiceException, LoginRequiredException, PermissionDeniedException {
	}
}
