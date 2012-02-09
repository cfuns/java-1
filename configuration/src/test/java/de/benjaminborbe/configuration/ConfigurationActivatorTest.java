package de.benjaminborbe.configuration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.configuration.guice.ConfigurationModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import de.benjaminborbe.tools.osgi.ServiceInfo;
import de.benjaminborbe.tools.osgi.mock.ExtHttpServiceMock;
import de.benjaminborbe.tools.osgi.test.BundleActivatorTestUtil;

public class ConfigurationActivatorTest {

	@Test
	public void testInject() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new ConfigurationModulesMock());
		final ConfigurationActivator o = injector.getInstance(ConfigurationActivator.class);
		assertNotNull(o);
	}

	@Test
	public void testResources() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new ConfigurationModulesMock());
		final ConfigurationActivator o = new ConfigurationActivator() {

			@Override
			public Injector getInjector() {
				return injector;
			}

		};
		final BundleActivatorTestUtil bundleActivatorTestUtil = new BundleActivatorTestUtil();
		final ExtHttpServiceMock extHttpServiceMock = bundleActivatorTestUtil.startBundle(o);
		final List<String> paths = Arrays.asList();
		assertEquals(paths.size(), extHttpServiceMock.getRegisterResourceCallCounter());
		for (final String path : paths) {
			assertTrue("no resource for path " + path + " registered", extHttpServiceMock.hasResource(path));
		}
	}

	public void testServices() {
		final ConfigurationActivator configurationActivator = new ConfigurationActivator();
		final Collection<ServiceInfo> serviceInfos = configurationActivator.getServiceInfos();
		assertEquals(0, serviceInfos.size());
	}

}
