package de.benjaminborbe.storage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import java.util.Collection;

import org.easymock.EasyMock;
import org.junit.Test;
import org.osgi.framework.BundleContext;

import com.google.inject.Injector;

import de.benjaminborbe.storage.guice.StorageModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.ServiceInfo;
import de.benjaminborbe.tools.osgi.mock.ExtHttpServiceMock;
import de.benjaminborbe.tools.osgi.test.BundleActivatorTestUtil;

public class StorageActivatorIntegrationTest {

	@Test
	public void testInject() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new StorageModulesMock());
		final StorageActivator o = injector.getInstance(StorageActivator.class);
		assertNotNull(o);
	}

	@Test
	public void testModules() {
		final StorageModulesMock modules = new StorageModulesMock();
		final StorageActivator storageActivator = new StorageActivator() {

			@Override
			protected Modules getModules(final BundleContext context) {
				return modules;
			}
		};

		assertEquals(modules, storageActivator.getModules(null));
	}

	@Test
	public void Services() throws Exception {
		final StorageActivator storageActivator = new StorageActivator() {

			@Override
			protected Modules getModules(final BundleContext context) {
				return new StorageModulesMock();
			}
		};
		final BundleContext context = EasyMock.createNiceMock(BundleContext.class);
		EasyMock.replay(context);
		storageActivator.start(context);
		final Collection<ServiceInfo> serviceInfos = storageActivator.getServiceInfos();
		assertEquals(1, serviceInfos.size());
	}

	@Test
	public void testResources() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new StorageModulesMock());
		final StorageActivator o = new StorageActivator() {

			@Override
			public Injector getInjector() {
				return injector;
			}

		};
		final BundleActivatorTestUtil bundleActivatorTestUtil = new BundleActivatorTestUtil();
		final ExtHttpServiceMock extHttpServiceMock = bundleActivatorTestUtil.startBundle(o);
		assertEquals(0, extHttpServiceMock.getRegisterResourceCallCounter());
		// for (final String path : Arrays.asList("/monitoring/css")) {
		// assertTrue("no resource for path " + path + " registered",
		// extHttpServiceMock.hasResource(path));
		// }
	}
}
