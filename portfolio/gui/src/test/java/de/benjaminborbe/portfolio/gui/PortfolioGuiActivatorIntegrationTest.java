package de.benjaminborbe.portfolio.gui;

import com.google.inject.Injector;
import de.benjaminborbe.portfolio.gui.guice.PortfolioGuiModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import de.benjaminborbe.tools.osgi.BaseGuiceFilter;
import de.benjaminborbe.tools.osgi.ServiceInfo;
import de.benjaminborbe.tools.osgi.mock.ExtHttpServiceMock;
import de.benjaminborbe.tools.osgi.test.BundleActivatorTestUtil;
import de.benjaminborbe.website.servlet.WebsiteServlet;
import org.junit.Test;

import javax.servlet.Filter;
import javax.servlet.Servlet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class PortfolioGuiActivatorIntegrationTest {

	@Test
	public void testInject() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new PortfolioGuiModulesMock());
		final PortfolioGuiActivator activator = injector.getInstance(PortfolioGuiActivator.class);
		assertNotNull(activator);
	}

	@Test
	public void testServlets() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new PortfolioGuiModulesMock());
		final PortfolioGuiActivator activator = new PortfolioGuiActivator() {

			@Override
			public Injector getInjector() {
				return injector;
			}

		};
		final BundleActivatorTestUtil bundleActivatorTestUtil = new BundleActivatorTestUtil();
		final ExtHttpServiceMock extHttpServiceMock = bundleActivatorTestUtil.startBundle(activator);
		final List<String> paths = new ArrayList<>();
		paths.add("/" + PortfolioGuiConstants.NAME + PortfolioGuiConstants.URL_GALLERY);
		paths.add("/" + PortfolioGuiConstants.NAME + PortfolioGuiConstants.URL_CONTACT);
		paths.add("/" + PortfolioGuiConstants.NAME + PortfolioGuiConstants.URL_IMAGE);
		paths.add("/" + PortfolioGuiConstants.NAME + PortfolioGuiConstants.URL_LINKS);
		assertEquals(paths.size(), extHttpServiceMock.getRegisterServletCallCounter());
		for (final String path : paths) {
			assertTrue("no servlet for path " + path + " registered", extHttpServiceMock.hasServletPath(path));
		}

		// all public and no admin
		for (final Servlet servlet : extHttpServiceMock.getServlets()) {
			assertTrue(servlet instanceof WebsiteServlet);
			final WebsiteServlet websiteServlet = (WebsiteServlet) servlet;
			assertFalse(servlet.getClass().getName(), websiteServlet.isAdminRequired());
			assertTrue(servlet.getClass().getName(), websiteServlet.isEnabled());
			assertFalse(servlet.getClass().getName(), websiteServlet.isLoginRequired());
		}
	}

	@Test
	public void testFilters() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new PortfolioGuiModulesMock());
		final PortfolioGuiActivator activator = new PortfolioGuiActivator() {

			@Override
			public Injector getInjector() {
				return injector;
			}
		};
		final BundleActivatorTestUtil bundleActivatorTestUtil = new BundleActivatorTestUtil();
		final ExtHttpServiceMock extHttpServiceMock = bundleActivatorTestUtil.startBundle(activator);
		final List<String> paths = new ArrayList<>();
		paths.add("/portfolio.*");
		paths.add("/portfolio.*");
		assertEquals(paths.size(), extHttpServiceMock.getRegisterFilterCallCounter());
		for (final String path : paths) {
			assertTrue("no filter for path " + path + " registered", extHttpServiceMock.hasFilterPath(path));
		}
		final BaseGuiceFilter guiceFilter = injector.getInstance(BaseGuiceFilter.class);
		for (final Filter filter : Arrays.asList(guiceFilter)) {
			assertTrue("no filter " + filter + " registered", extHttpServiceMock.hasFilter(filter));
		}
	}

	@Test
	public void testResources() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new PortfolioGuiModulesMock());
		final PortfolioGuiActivator activator = new PortfolioGuiActivator() {

			@Override
			public Injector getInjector() {
				return injector;
			}
		};
		final BundleActivatorTestUtil bundleActivatorTestUtil = new BundleActivatorTestUtil();
		final ExtHttpServiceMock extHttpServiceMock = bundleActivatorTestUtil.startBundle(activator);
		final List<String> paths = new ArrayList<>();
		paths.add("/" + PortfolioGuiConstants.NAME + "/images");
		paths.add("/" + PortfolioGuiConstants.NAME + "/css");
		paths.add("/" + PortfolioGuiConstants.NAME + "/js");
		assertEquals(paths.size(), extHttpServiceMock.getRegisterResourceCallCounter());
		for (final String path : paths) {
			assertTrue("no resource for path " + path + " registered", extHttpServiceMock.hasResource(path));
		}
	}

	@Test
	public void testServices() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new PortfolioGuiModulesMock());
		final PortfolioGuiActivator activator = new PortfolioGuiActivator() {

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
