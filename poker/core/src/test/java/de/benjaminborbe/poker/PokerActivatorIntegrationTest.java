package de.benjaminborbe.poker;

import com.google.inject.Injector;
import de.benjaminborbe.configuration.api.ConfigurationDescription;
import de.benjaminborbe.cron.api.CronJob;
import de.benjaminborbe.eventbus.api.EventHandler;
import de.benjaminborbe.poker.api.PokerService;
import de.benjaminborbe.poker.guice.PokerModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import de.benjaminborbe.tools.osgi.ServiceInfo;
import de.benjaminborbe.tools.osgi.mock.ExtHttpServiceMock;
import de.benjaminborbe.tools.osgi.test.BundleActivatorTestUtil;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class PokerActivatorIntegrationTest {

	@Test
	public void testInject() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new PokerModulesMock());
		final PokerCoreActivator activator = injector.getInstance(PokerCoreActivator.class);
		assertNotNull(activator);
	}

	@Test
	public void testResources() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new PokerModulesMock());
		final PokerCoreActivator activator = new PokerCoreActivator() {

			@Override
			public Injector getInjector() {
				return injector;
			}

		};
		final BundleActivatorTestUtil bundleActivatorTestUtil = new BundleActivatorTestUtil();
		final ExtHttpServiceMock extHttpServiceMock = bundleActivatorTestUtil.startBundle(activator);
		final List<String> paths = new ArrayList<String>();
		assertEquals(paths.size(), extHttpServiceMock.getRegisterResourceCallCounter());
		for (final String path : paths) {
			assertTrue("no resource for path " + path + " registered", extHttpServiceMock.hasResource(path));
		}
	}

	@Test
	public void testServices() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new PokerModulesMock());
		final PokerCoreActivator activator = new PokerCoreActivator() {

			@Override
			public Injector getInjector() {
				return injector;
			}

		};

		final BundleActivatorTestUtil bundleActivatorTestUtil = new BundleActivatorTestUtil();
		bundleActivatorTestUtil.startBundle(activator);

		final Collection<ServiceInfo> serviceInfos = activator.getServiceInfos();
		final List<String> names = new ArrayList<String>();
		names.add(PokerService.class.getName());
		names.add(CronJob.class.getName());
		names.add(EventHandler.class.getName());
		names.add(EventHandler.class.getName());
		names.add(EventHandler.class.getName());
		names.add(EventHandler.class.getName());
		names.add(ConfigurationDescription.class.getName());
		names.add(ConfigurationDescription.class.getName());
		names.add(ConfigurationDescription.class.getName());
		names.add(ConfigurationDescription.class.getName());
		names.add(ConfigurationDescription.class.getName());
		names.add(ConfigurationDescription.class.getName());
		names.add(ConfigurationDescription.class.getName());
		names.add(ConfigurationDescription.class.getName());
		names.add(ConfigurationDescription.class.getName());
		assertEquals(names.size(), serviceInfos.size());
		for (final String name : names) {
			boolean match = false;
			for (final ServiceInfo serviceInfo : serviceInfos) {
				if (name.equals(serviceInfo.getName())) {
					match = true;
				}
			}
			assertTrue("no service with name: " + name + " found", match);
		}
	}
}
