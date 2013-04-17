package de.benjaminborbe.slash;

import com.google.inject.Injector;
import de.benjaminborbe.slash.guice.SlashModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import de.benjaminborbe.tools.osgi.ServiceInfo;
import de.benjaminborbe.tools.osgi.test.BundleActivatorTestUtil;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class SlashActivatorIntegrationTest {

	@Test
	public void testInject() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new SlashModulesMock());
		final SlashActivator activator = injector.getInstance(SlashActivator.class);
		assertNotNull(activator);
	}

	@Test
	public void testServices() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new SlashModulesMock());
		final SlashActivator activator = new SlashActivator() {

			@Override
			public Injector getInjector() {
				return injector;
			}

		};

		final BundleActivatorTestUtil bundleActivatorTestUtil = new BundleActivatorTestUtil();
		bundleActivatorTestUtil.startBundle(activator);

		final Collection<ServiceInfo> serviceInfos = activator.getServiceInfos();
		final List<String> names = new ArrayList<>();
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
