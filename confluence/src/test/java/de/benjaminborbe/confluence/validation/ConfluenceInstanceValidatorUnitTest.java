package de.benjaminborbe.confluence.validation;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.apache.xmlrpc.XmlRpcException;
import org.easymock.EasyMock;
import org.junit.Test;
import org.slf4j.Logger;

import de.benjaminborbe.confluence.connector.ConfluenceConnector;
import de.benjaminborbe.confluence.dao.ConfluenceInstanceBean;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.tools.url.UrlUtilImpl;
import de.benjaminborbe.tools.validation.ValidationConstraintValidator;

public class ConfluenceInstanceValidatorUnitTest {

	@Test
	public void testValidateInactive() throws Exception {
		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);

		final UrlUtil urlUtil = new UrlUtilImpl();

		final ValidationConstraintValidator validationConstraintValidator = new ValidationConstraintValidator();

		final ConfluenceConnector confluenceConnector = EasyMock.createMock(ConfluenceConnector.class);
		EasyMock.replay(confluenceConnector);

		final ConfluenceInstanceValidator va = new ConfluenceInstanceValidator(logger, urlUtil, validationConstraintValidator, confluenceConnector);
		final ConfluenceInstanceBean bean = new ConfluenceInstanceBean();
		bean.setActivated(false);
		bean.setExpire(1);
		bean.setDelay(0l);
		bean.setUrl("http://confluence.de");
		bean.setUsername("username");
		bean.setPassword("password");
		assertThat(va.validate(bean).size(), is(0));
	}

	@Test
	public void testValidateActive() throws Exception {
		final String confluenceBaseUrl = "http://confluence.de";
		final String username = "username";
		final String password = "password";
		final String token = "sessionToken";

		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);

		final UrlUtil urlUtil = new UrlUtilImpl();

		final ValidationConstraintValidator validationConstraintValidator = new ValidationConstraintValidator();

		final ConfluenceConnector confluenceConnector = EasyMock.createMock(ConfluenceConnector.class);
		EasyMock.expect(confluenceConnector.login(confluenceBaseUrl, username, password)).andThrow(new XmlRpcException("login failed"));
		EasyMock.expect(confluenceConnector.login(confluenceBaseUrl, username, password)).andReturn(token);
		EasyMock.replay(confluenceConnector);

		final ConfluenceInstanceValidator va = new ConfluenceInstanceValidator(logger, urlUtil, validationConstraintValidator, confluenceConnector);
		final ConfluenceInstanceBean bean = new ConfluenceInstanceBean();
		bean.setActivated(true);
		bean.setExpire(1);
		bean.setDelay(0l);
		bean.setUrl(confluenceBaseUrl);
		bean.setUsername(username);
		bean.setPassword(password);
		assertThat(va.validate(bean).size(), is(1));
		assertThat(va.validate(bean).size(), is(0));
	}
}
