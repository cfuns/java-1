package de.benjaminborbe.poker.test;

import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.configuration.api.ConfigurationIdentifier;
import de.benjaminborbe.configuration.api.ConfigurationService;
import de.benjaminborbe.configuration.api.ConfigurationServiceException;
import de.benjaminborbe.lib.servlet.mock.HttpServletRequestMock;
import de.benjaminborbe.lib.servlet.mock.HttpServletResponseMock;
import de.benjaminborbe.poker.api.PokerServiceException;
import de.benjaminborbe.tools.json.JSONObject;
import de.benjaminborbe.tools.json.JSONParseException;
import de.benjaminborbe.tools.json.JSONParser;
import de.benjaminborbe.tools.json.JSONParserSimple;
import org.apache.felix.http.api.ExtHttpService;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import javax.servlet.Servlet;

public class PokerApiDisabledIntegrationTest extends PokerIntegrationTest {

	public void testDisabled() throws PokerServiceException, ValidationException {
		/*
		 * Arrange
		 */
		try {
			final ConfigurationService configurationService = getConfigurationService();
			final ConfigurationIdentifier pokerJsonApiEnabledConfigurationIdentifier = configurationService.createConfigurationIdentifier("PokerJsonApiEnabled");
			configurationService.setConfigurationValue(pokerJsonApiEnabledConfigurationIdentifier, "false");
			final String configurationValue = configurationService.getConfigurationValue(pokerJsonApiEnabledConfigurationIdentifier);
			assertEquals("false", configurationValue);
		}
		catch (final ConfigurationServiceException e) {
			fail("unexpected exception: " + e);
		}

		final HttpServletRequestMock request = new HttpServletRequestMock();
		final HttpServletResponseMock response = new HttpServletResponseMock();
		final BundleContext bundleContext = getContext();
		assertNotNull(bundleContext);
		final ExtHttpServiceHelper extHttpService = new ExtHttpServiceHelper();
		final ServiceRegistration serviceRegistration = bundleContext.registerService(ExtHttpService.class.getName(), extHttpService, null);
		assertNotNull(serviceRegistration);
		final Servlet statusServlet = extHttpService.getServlet("/poker/game/status/json");
		assertNotNull(statusServlet);

		/*
		 * Act
		 */
		try {
			statusServlet.service(request, response);
			response.getWriter().flush();
		}
		catch (final Exception e) {
			fail("unexpected exception: " + e.getClass().getName());
		}

		/*
		 * Assert
		 */
		try {
			assertNotNull(response.getContent());
			assertTrue(response.getContent().length > 0);
			final JSONParser jsonParser = new JSONParserSimple();
			final Object object = jsonParser.parse(new String(response.getContent()));
			assertTrue(object instanceof JSONObject);
			final JSONObject jsonObject = (JSONObject) object;
			final Object errorObject = jsonObject.get("error");
			assertNotNull(errorObject);
			assertTrue(errorObject instanceof String);
			final String error = (String) errorObject;
			assertEquals("disabled", error);
		}
		catch (final JSONParseException e) {
			fail("unexpected exception: " + e);
		}
	}
}
