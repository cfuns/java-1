package de.benjaminborbe.bridge;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Properties;

import javax.servlet.ServletContext;

import junit.framework.TestCase;

import org.easymock.EasyMock;

public class ProvisionActivatorTest extends TestCase {

	public void testGetProperties() {
		final ServletContext servletContext = EasyMock.createMock(ServletContext.class);
		final InputStream inputStream = new ByteArrayInputStream("twentyfeet.bundle.1 = bla".getBytes());
		EasyMock.expect(servletContext.getResourceAsStream("/WEB-INF/activator.properties")).andReturn(inputStream);
		EasyMock.replay(servletContext);
		final ProvisionActivator pa = new ProvisionActivator(servletContext);
		final Properties props = pa.getProperties();
		assertTrue(props.containsKey("twentyfeet.bundle.1"));
	}
}
