package de.benjaminborbe.microblog;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.microblog.MicroblogActivator;
import de.benjaminborbe.microblog.guice.MicroblogModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import de.benjaminborbe.tools.osgi.mock.ExtHttpServiceMock;
import de.benjaminborbe.tools.osgi.test.BundleActivatorTestUtil;

public class MicroblogActivatorTest {

	@Test
	public void inject() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new MicroblogModulesMock());
		final MicroblogActivator o = injector.getInstance(MicroblogActivator.class);
		assertNotNull(o);
	}

	@Test
	public void testResources() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new MicroblogModulesMock());
		final MicroblogActivator o = new MicroblogActivator() {

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
}
