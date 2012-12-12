package de.benjaminborbe.gallery.gui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.servlet.Filter;

import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.gallery.gui.guice.GalleryGuiModulesMock;
import de.benjaminborbe.navigation.api.NavigationEntry;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import de.benjaminborbe.tools.osgi.BaseGuiceFilter;
import de.benjaminborbe.tools.osgi.ServiceInfo;
import de.benjaminborbe.tools.osgi.mock.ExtHttpServiceMock;
import de.benjaminborbe.tools.osgi.test.BundleActivatorTestUtil;

public class GalleryGuiActivatorIntegrationTest {

	@Test
	public void testInject() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new GalleryGuiModulesMock());
		final GalleryGuiActivator activator = injector.getInstance(GalleryGuiActivator.class);
		assertNotNull(activator);
	}

	@Test
	public void testServlets() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new GalleryGuiModulesMock());
		final GalleryGuiActivator activator = new GalleryGuiActivator() {

			@Override
			public Injector getInjector() {
				return injector;
			}

		};
		final BundleActivatorTestUtil bundleActivatorTestUtil = new BundleActivatorTestUtil();
		final ExtHttpServiceMock extHttpServiceMock = bundleActivatorTestUtil.startBundle(activator);
		final List<String> paths = new ArrayList<String>();
		paths.add("/" + GalleryGuiConstants.NAME + GalleryGuiConstants.URL_GROUP_UPDATE);
		paths.add("/" + GalleryGuiConstants.NAME + GalleryGuiConstants.URL_GROUP_CREATE);
		paths.add("/" + GalleryGuiConstants.NAME + GalleryGuiConstants.URL_GROUP_DELETE);
		paths.add("/" + GalleryGuiConstants.NAME + GalleryGuiConstants.URL_GROUP_LIST);
		paths.add("/" + GalleryGuiConstants.NAME + GalleryGuiConstants.URL_COLLECTION_UPDATE);
		paths.add("/" + GalleryGuiConstants.NAME + GalleryGuiConstants.URL_COLLECTION_CREATE);
		paths.add("/" + GalleryGuiConstants.NAME + GalleryGuiConstants.URL_COLLECTION_DELETE);
		paths.add("/" + GalleryGuiConstants.NAME + GalleryGuiConstants.URL_COLLECTION_LIST);
		paths.add("/" + GalleryGuiConstants.NAME + GalleryGuiConstants.URL_ENTRY_UPDATE);
		paths.add("/" + GalleryGuiConstants.NAME + GalleryGuiConstants.URL_ENTRY_CREATE);
		paths.add("/" + GalleryGuiConstants.NAME + GalleryGuiConstants.URL_ENTRY_DELETE);
		paths.add("/" + GalleryGuiConstants.NAME + GalleryGuiConstants.URL_ENTRY_LIST);
		paths.add("/" + GalleryGuiConstants.NAME + GalleryGuiConstants.URL_IMAGE);
		paths.add("/" + GalleryGuiConstants.NAME + GalleryGuiConstants.URL_ENTRY_SWAP_PRIO);
		paths.add("/" + GalleryGuiConstants.NAME + GalleryGuiConstants.URL_ENTRY_SHARE);
		paths.add("/" + GalleryGuiConstants.NAME + GalleryGuiConstants.URL_ENTRY_UNSHARE);
		assertEquals(paths.size(), extHttpServiceMock.getRegisterServletCallCounter());
		for (final String path : paths) {
			assertTrue("no servlet for path " + path + " registered", extHttpServiceMock.hasServletPath(path));
		}
	}

	@Test
	public void testFilters() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new GalleryGuiModulesMock());
		final GalleryGuiActivator activator = new GalleryGuiActivator() {

			@Override
			public Injector getInjector() {
				return injector;
			}
		};
		final BundleActivatorTestUtil bundleActivatorTestUtil = new BundleActivatorTestUtil();
		final ExtHttpServiceMock extHttpServiceMock = bundleActivatorTestUtil.startBundle(activator);
		final List<String> paths = Arrays.asList("/gallery.*");
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
		final Injector injector = GuiceInjectorBuilder.getInjector(new GalleryGuiModulesMock());
		final GalleryGuiActivator activator = new GalleryGuiActivator() {

			@Override
			public Injector getInjector() {
				return injector;
			}
		};
		final BundleActivatorTestUtil bundleActivatorTestUtil = new BundleActivatorTestUtil();
		final ExtHttpServiceMock extHttpServiceMock = bundleActivatorTestUtil.startBundle(activator);
		final List<String> paths = new ArrayList<String>();
		paths.add("/" + GalleryGuiConstants.NAME + GalleryGuiConstants.URL_CSS);
		assertEquals(paths.size(), extHttpServiceMock.getRegisterResourceCallCounter());
		for (final String path : paths) {
			assertTrue("no resource for path " + path + " registered", extHttpServiceMock.hasResource(path));
		}
	}

	@Test
	public void testServices() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new GalleryGuiModulesMock());
		final GalleryGuiActivator activator = new GalleryGuiActivator() {

			@Override
			public Injector getInjector() {
				return injector;
			}
		};

		final BundleActivatorTestUtil bundleActivatorTestUtil = new BundleActivatorTestUtil();
		bundleActivatorTestUtil.startBundle(activator);

		final Collection<ServiceInfo> serviceInfos = activator.getServiceInfos();
		final List<String> names = Arrays.asList(NavigationEntry.class.getName());
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
