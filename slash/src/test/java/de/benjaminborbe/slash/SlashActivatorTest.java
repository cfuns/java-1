package de.benjaminborbe.slash;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import org.easymock.EasyMock;
import org.junit.Test;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Filter;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceReference;
import com.google.inject.Injector;

import de.benjaminborbe.slash.guice.SlashModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import de.benjaminborbe.tools.osgi.mock.ExtHttpServiceMock;

public class SlashActivatorTest {

	@Test
	public void inject() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new SlashModulesMock());
		final SlashActivator o = injector.getInstance(SlashActivator.class);
		assertNotNull(o);
	}

	@Test
	public void testServletPaths() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new SlashModulesMock());
		final SlashActivator o = injector.getInstance(SlashActivator.class);
		assertNotNull(o);

		final long bundleId = 1337l;

		final Bundle bundle = EasyMock.createMock(Bundle.class);
		EasyMock.expect(bundle.getBundleId()).andReturn(bundleId);
		EasyMock.replay(bundle);

		final Filter filter = EasyMock.createMock(Filter.class);
		EasyMock.replay(filter);

		final ExtHttpServiceMock extBundle = new ExtHttpServiceMock();

		final ServiceReference serviceReference = EasyMock.createMock(ServiceReference.class);
		EasyMock.expect(serviceReference.getBundle()).andReturn(extBundle);
		EasyMock.replay(serviceReference);

		final BundleContext context = EasyMock.createMock(BundleContext.class);
		EasyMock.expect(context.getService(serviceReference)).andReturn(extBundle);
		EasyMock.expect(context.getBundle()).andReturn(bundle);
		EasyMock.expect(context.createFilter(EasyMock.anyObject(String.class))).andReturn(filter);
		context.addServiceListener(EasyMock.anyObject(ServiceListener.class), EasyMock.anyObject(String.class));
		final ServiceReference[] serviceReferences = new ServiceReference[] { serviceReference };
		EasyMock.expect(context.getServiceReferences("org.apache.felix.http.api.ExtHttpService", null)).andReturn(serviceReferences);
		EasyMock.replay(context);

		o.start(context);

		for (final String path : Arrays.asList("/", "/robots.txt")) {
			assertTrue("no servlet for path " + path + " registered", extBundle.hasServletPath(path));
		}
	}
}
