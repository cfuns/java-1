package de.benjaminborbe.projectile.gui;

import com.google.inject.Injector;
import de.benjaminborbe.navigation.api.NavigationEntry;
import de.benjaminborbe.projectile.gui.guice.ProjectileGuiModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import de.benjaminborbe.tools.osgi.BaseGuiceFilter;
import de.benjaminborbe.tools.osgi.ServiceInfo;
import de.benjaminborbe.tools.osgi.mock.ExtHttpServiceMock;
import de.benjaminborbe.tools.osgi.test.BundleActivatorTestUtil;
import org.junit.Test;

import javax.servlet.Filter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class ProjectileGuiActivatorIntegrationTest {

	@Test
	public void testInject() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new ProjectileGuiModulesMock());
		final ProjectileGuiActivator activator = injector.getInstance(ProjectileGuiActivator.class);
		assertNotNull(activator);
	}

	@Test
	public void testServlets() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new ProjectileGuiModulesMock());
		final ProjectileGuiActivator activator = new ProjectileGuiActivator() {

			@Override
			public Injector getInjector() {
				return injector;
			}

		};
		final BundleActivatorTestUtil bundleActivatorTestUtil = new BundleActivatorTestUtil();
		final ExtHttpServiceMock extHttpServiceMock = bundleActivatorTestUtil.startBundle(activator);
		final List<String> paths = new ArrayList<String>();
		paths.add("/" + ProjectileGuiConstants.NAME + ProjectileGuiConstants.URL_HOME);
		paths.add("/" + ProjectileGuiConstants.NAME + ProjectileGuiConstants.URL_REPORT_USER_CURRENT);
		paths.add("/" + ProjectileGuiConstants.NAME + ProjectileGuiConstants.URL_REPORT_USER_ALL);
		paths.add("/" + ProjectileGuiConstants.NAME + ProjectileGuiConstants.URL_REPORT_FETCH);
		paths.add("/" + ProjectileGuiConstants.NAME + ProjectileGuiConstants.URL_REPORT_IMPORT);
		paths.add("/" + ProjectileGuiConstants.NAME + ProjectileGuiConstants.URL_REPORT_JSON);
		paths.add("/" + ProjectileGuiConstants.NAME + ProjectileGuiConstants.URL_REPORT_TEAM_ALL);
		paths.add("/" + ProjectileGuiConstants.NAME + ProjectileGuiConstants.URL_REPORT_TEAM_CURRENT);
		paths.add("/" + ProjectileGuiConstants.NAME + ProjectileGuiConstants.URL_TEAM_CREATE);
		paths.add("/" + ProjectileGuiConstants.NAME + ProjectileGuiConstants.URL_TEAM_DELETE);
		paths.add("/" + ProjectileGuiConstants.NAME + ProjectileGuiConstants.URL_TEAM_LIST);
		paths.add("/" + ProjectileGuiConstants.NAME + ProjectileGuiConstants.URL_TEAM_UPDATE);
		paths.add("/" + ProjectileGuiConstants.NAME + ProjectileGuiConstants.URL_TEAM_VIEW);
		paths.add("/" + ProjectileGuiConstants.NAME + ProjectileGuiConstants.URL_TEAM_USER_REMOVE);
		assertEquals(paths.size(), extHttpServiceMock.getRegisterServletCallCounter());
		for (final String path : paths) {
			assertTrue("no servlet for path " + path + " registered", extHttpServiceMock.hasServletPath(path));
		}
	}

	@Test
	public void testFilters() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new ProjectileGuiModulesMock());
		final ProjectileGuiActivator activator = new ProjectileGuiActivator() {

			@Override
			public Injector getInjector() {
				return injector;
			}
		};
		final BundleActivatorTestUtil bundleActivatorTestUtil = new BundleActivatorTestUtil();
		final ExtHttpServiceMock extHttpServiceMock = bundleActivatorTestUtil.startBundle(activator);
		final List<String> paths = Arrays.asList("/projectile.*");
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
		final Injector injector = GuiceInjectorBuilder.getInjector(new ProjectileGuiModulesMock());
		final ProjectileGuiActivator activator = new ProjectileGuiActivator() {

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
		final Injector injector = GuiceInjectorBuilder.getInjector(new ProjectileGuiModulesMock());
		final ProjectileGuiActivator activator = new ProjectileGuiActivator() {

			@Override
			public Injector getInjector() {
				return injector;
			}
		};

		final BundleActivatorTestUtil bundleActivatorTestUtil = new BundleActivatorTestUtil();
		bundleActivatorTestUtil.startBundle(activator);

		final Collection<ServiceInfo> serviceInfos = activator.getServiceInfos();
		final List<String> names = new ArrayList<String>();
		names.add(NavigationEntry.class.getName());
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
