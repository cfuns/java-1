package de.benjaminborbe.sample;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Collection;

import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.sample.guice.SampleModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import de.benjaminborbe.tools.osgi.ServiceInfo;
import de.benjaminborbe.tools.osgi.mock.ExtHttpServiceMock;
import de.benjaminborbe.tools.osgi.test.BundleActivatorTestUtil;

public class SampleActivatorTest {

	@Test
	public void testInject() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new SampleModulesMock());
		final SampleActivator o = injector.getInstance(SampleActivator.class);
		assertNotNull(o);
	}

	@Test
	public void testResources() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new SampleModulesMock());
		final SampleActivator o = new SampleActivator() {

			@Override
			public Injector getInjector() {
				return injector;
			}

		};
		final BundleActivatorTestUtil bundleActivatorTestUtil = new BundleActivatorTestUtil();
		final ExtHttpServiceMock extHttpServiceMock = bundleActivatorTestUtil.startBundle(o);
		assertEquals(0, extHttpServiceMock.getRegisterResourceCallCounter());
		// for (final String path : Arrays.asList("/search/css", "/search/js")) {
		// assertTrue("no resource for path " + path + " registered",
		// extHttpServiceMock.hasResource(path));
		// }
	}

	public void testServices() {
		final SampleActivator sampleActivator = new SampleActivator();
		final Collection<ServiceInfo> serviceInfos = sampleActivator.getServiceInfos();
		assertEquals(0, serviceInfos.size());
	}

}
