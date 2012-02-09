package de.benjaminborbe.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import de.benjaminborbe.tools.osgi.mock.ExtHttpServiceMock;
import de.benjaminborbe.tools.osgi.test.BundleActivatorTestUtil;
import de.benjaminborbe.translate.guice.TranslateModulesMock;

public class TranslateActivatorTest {

	@Test
	public void inject() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new TranslateModulesMock());
		final TranslateActivator o = injector.getInstance(TranslateActivator.class);
		assertNotNull(o);
	}

	@Test
	public void testResources() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new TranslateModulesMock());
		final TranslateActivator o = new TranslateActivator() {

			@Override
			public Injector getInjector() {
				return injector;
			}

		};
		final BundleActivatorTestUtil bundleActivatorTestTranslate = new BundleActivatorTestUtil();
		final ExtHttpServiceMock extHttpServiceMock = bundleActivatorTestTranslate.startBundle(o);
		final List<String> paths = Arrays.asList();
		assertEquals(paths.size(), extHttpServiceMock.getRegisterResourceCallCounter());
		for (final String path : paths) {
			assertTrue("no resource for path " + path + " registered", extHttpServiceMock.hasResource(path));
		}

	}
}
