package de.benjaminborbe.navigation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Collection;

import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.navigation.guice.NavigationModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import de.benjaminborbe.tools.osgi.ServiceInfo;

public class NavigationActivatorTest {

	@Test
	public void inject() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new NavigationModulesMock());
		final NavigationActivator o = injector.getInstance(NavigationActivator.class);
		assertNotNull(o);
	}

	public void testServices() {
		final NavigationActivator activator = new NavigationActivator();
		final Collection<ServiceInfo> serviceInfos = activator.getServiceInfos();
		assertEquals(1, serviceInfos.size());
	}
}
