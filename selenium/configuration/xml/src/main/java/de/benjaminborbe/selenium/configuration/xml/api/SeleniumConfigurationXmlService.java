package de.benjaminborbe.selenium.configuration.xml.api;

import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.selenium.api.SeleniumConfigurationIdentifier;

import java.util.Collection;

public interface SeleniumConfigurationXmlService {

	Collection<SeleniumConfigurationIdentifier> list(SessionIdentifier sessionIdentifier) throws SeleniumConfigurationXmlServiceException, LoginRequiredException, PermissionDeniedException;

	SeleniumConfigurationIdentifier addXml(
		SessionIdentifier sessionIdentifier,
		String xml
	) throws SeleniumConfigurationXmlServiceException, LoginRequiredException, PermissionDeniedException;

	void deleteXml(
		SessionIdentifier sessionIdentifier,
		SeleniumConfigurationIdentifier seleniumConfiguration
	) throws SeleniumConfigurationXmlServiceException, LoginRequiredException, PermissionDeniedException;
}
