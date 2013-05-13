package de.benjaminborbe.selenium.configuration.xml.api;

import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.selenium.api.SeleniumConfigurationIdentifier;

import java.util.Collection;

public interface SeleniumConfigurationXmlService {

	Collection<SeleniumConfigurationIdentifier> list(SessionIdentifier sessionIdentifier)
		throws SeleniumConfigurationXmlServiceException, LoginRequiredException, PermissionDeniedException;

	SeleniumConfigurationIdentifier addXml(
		final SessionIdentifier sessionIdentifier,
		final String xml
	) throws SeleniumConfigurationXmlServiceException, LoginRequiredException, PermissionDeniedException;

	void deleteXml(
		final SessionIdentifier sessionIdentifier,
		final SeleniumConfigurationIdentifier id
	) throws SeleniumConfigurationXmlServiceException, LoginRequiredException, PermissionDeniedException;

	SeleniumConfigurationXml getXml(
		SessionIdentifier sessionIdentifier,
		SeleniumConfigurationIdentifier id
	) throws SeleniumConfigurationXmlServiceException, LoginRequiredException, PermissionDeniedException;
}


