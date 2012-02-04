package de.benjaminborbe.authentication.gui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import javax.servlet.Filter;

import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.authentication.gui.AuthenticationGuiActivator;
import de.benjaminborbe.authentication.gui.guice.AuthenticationGuiModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import de.benjaminborbe.tools.osgi.BaseGuiceFilter;
import de.benjaminborbe.tools.osgi.mock.ExtHttpServiceMock;
import de.benjaminborbe.tools.osgi.test.BundleActivatorTestUtil;

public class AuthenticationGuiActivatorTest {

	@Test
	public void testInject() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new AuthenticationGuiModulesMock());
		final AuthenticationGuiActivator o = injector.getInstance(AuthenticationGuiActivator.class);
		assertNotNull(o);
	}

	@Test
	public void testServlets() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new AuthenticationGuiModulesMock());
		final AuthenticationGuiActivator o = new AuthenticationGuiActivator() {

			@Override
			public Injector getInjector() {
				return injector;
			}

		};
		final BundleActivatorTestUtil bundleActivatorTestUtil = new BundleActivatorTestUtil();
		final ExtHttpServiceMock extHttpServiceMock = bundleActivatorTestUtil.startBundle(o);
		assertEquals("servlet count doesn't match", 4, extHttpServiceMock.getRegisterServletCallCounter());
		for (final String path : Arrays.asList("/authentication", "/authentication/login", "/authentication/logout", "/authentication/status")) {
			assertTrue("no servlet for path " + path + " registered", extHttpServiceMock.hasServletPath(path));
		}
	}

	@Test
	public void testFilters() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new AuthenticationGuiModulesMock());
		final AuthenticationGuiActivator o = new AuthenticationGuiActivator() {

			@Override
			public Injector getInjector() {
				return injector;
			}

		};

		final BundleActivatorTestUtil bundleActivatorTestUtil = new BundleActivatorTestUtil();
		final ExtHttpServiceMock extHttpServiceMock = bundleActivatorTestUtil.startBundle(o);
		assertEquals(1, extHttpServiceMock.getRegisterFilterCallCounter());

		for (final String path : Arrays.asList("/authentication.*")) {
			assertTrue("no filter for path " + path + " registered", extHttpServiceMock.hasFilterPath(path));
		}

		final BaseGuiceFilter guiceFilter = injector.getInstance(BaseGuiceFilter.class);
		for (final Filter filter : Arrays.asList(guiceFilter)) {
			assertTrue("no filter " + filter + " registered", extHttpServiceMock.hasFilter(filter));
		}
	}

	@Test
	public void testResources() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new AuthenticationGuiModulesMock());
		final AuthenticationGuiActivator o = new AuthenticationGuiActivator() {

			@Override
			public Injector getInjector() {
				return injector;
			}

		};
		final BundleActivatorTestUtil bundleActivatorTestUtil = new BundleActivatorTestUtil();
		final ExtHttpServiceMock extHttpServiceMock = bundleActivatorTestUtil.startBundle(o);
		assertEquals(0, extHttpServiceMock.getRegisterResourceCallCounter());
		// for (final String path : Arrays.asList("/", "/robots.txt")) {
		// assertTrue("no servlet for path " + path + " registered",
		// extHttpServiceMock.hasServletPath(path));
		// }
	}

}
