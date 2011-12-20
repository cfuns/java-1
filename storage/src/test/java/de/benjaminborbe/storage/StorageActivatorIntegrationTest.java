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

public class StorageActivatorIntegrationTest {

	@Test
	public void Inject() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new StorageModulesMock());
		final StorageActivator o = injector.getInstance(StorageActivator.class);
		assertNotNull(o);
	}

	@Test
	public void Modules() {
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
		assertEquals(2, serviceInfos.size());
	}

}
