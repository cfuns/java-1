package de.benjaminborbe.authorization.gui;

import com.google.inject.Injector;
import de.benjaminborbe.authorization.gui.guice.AuthorizationGuiModulesMock;
import de.benjaminborbe.navigation.api.NavigationEntry;
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

public class AuthorizationGuiActivatorIntegrationTest {

	@Test
	public void testInject() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new AuthorizationGuiModulesMock());
		final AuthorizationGuiActivator activator = injector.getInstance(AuthorizationGuiActivator.class);
		assertNotNull(activator);
	}

	@Test
	public void testServlets() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new AuthorizationGuiModulesMock());
		final AuthorizationGuiActivator activator = new AuthorizationGuiActivator() {

			@Override
			public Injector getInjector() {
				return injector;
			}

		};
		final BundleActivatorTestUtil bundleActivatorTestUtil = new BundleActivatorTestUtil();
		final ExtHttpServiceMock extHttpServiceMock = bundleActivatorTestUtil.startBundle(activator);
		final List<String> paths = new ArrayList<String>();
		paths.add("/" + AuthorizationGuiConstants.NAME + AuthorizationGuiConstants.URL_ADD_ROLE);
		paths.add("/" + AuthorizationGuiConstants.NAME + AuthorizationGuiConstants.URL_PERMISSION_LIST);
		paths.add("/" + AuthorizationGuiConstants.NAME + AuthorizationGuiConstants.URL_PERMISSION_DENIED);
		paths.add("/" + AuthorizationGuiConstants.NAME + AuthorizationGuiConstants.URL_PERMISSION_DELETE);
		paths.add("/" + AuthorizationGuiConstants.NAME + AuthorizationGuiConstants.URL_PERMISSION_INFO);
		paths.add("/" + AuthorizationGuiConstants.NAME + AuthorizationGuiConstants.URL_ROLE_PERMISSION_ADD);
		paths.add("/" + AuthorizationGuiConstants.NAME + AuthorizationGuiConstants.URL_ROLE_CREATE);
		paths.add("/" + AuthorizationGuiConstants.NAME + AuthorizationGuiConstants.URL_ROLE_INFO);
		paths.add("/" + AuthorizationGuiConstants.NAME + AuthorizationGuiConstants.URL_ROLE_LIST);
		paths.add("/" + AuthorizationGuiConstants.NAME + AuthorizationGuiConstants.URL_ROLE_DELETE);
		paths.add("/" + AuthorizationGuiConstants.NAME + AuthorizationGuiConstants.URL_ROLE_PERMISSION_REMOVE);
		paths.add("/" + AuthorizationGuiConstants.NAME + AuthorizationGuiConstants.URL_SLASH);
		paths.add("/" + AuthorizationGuiConstants.NAME + AuthorizationGuiConstants.URL_USER_LIST);
		paths.add("/" + AuthorizationGuiConstants.NAME + AuthorizationGuiConstants.URL_USER_INFO);
		paths.add("/" + AuthorizationGuiConstants.NAME + AuthorizationGuiConstants.URL_USER_REMOVE_ROLE);
		assertEquals("servlet-count not match", paths.size(), extHttpServiceMock.getRegisterServletCallCounter());
		for (final String path : paths) {
			assertTrue("no servlet for path " + path + " registered", extHttpServiceMock.hasServletPath(path));
		}
	}

	@Test
	public void testFilters() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new AuthorizationGuiModulesMock());
		final AuthorizationGuiActivator activator = new AuthorizationGuiActivator() {

			@Override
			public Injector getInjector() {
				return injector;
			}

		};

		final BundleActivatorTestUtil bundleActivatorTestUtil = new BundleActivatorTestUtil();
		final ExtHttpServiceMock extHttpServiceMock = bundleActivatorTestUtil.startBundle(activator);
		final List<String> paths = Arrays.asList("/authorization.*");
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
		final Injector injector = GuiceInjectorBuilder.getInjector(new AuthorizationGuiModulesMock());
		final AuthorizationGuiActivator activator = new AuthorizationGuiActivator() {

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
		final Injector injector = GuiceInjectorBuilder.getInjector(new AuthorizationGuiModulesMock());
		final AuthorizationGuiActivator activator = new AuthorizationGuiActivator() {

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
