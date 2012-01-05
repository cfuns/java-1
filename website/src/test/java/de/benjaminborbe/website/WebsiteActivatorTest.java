package de.benjaminborbe.website;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.website.WebsiteActivator;
import de.benjaminborbe.website.guice.WebsiteModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class WebsiteActivatorTest {

	@Test
	public void Inject() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new WebsiteModulesMock());
		final WebsiteActivator o = injector.getInstance(WebsiteActivator.class);
		assertNotNull(o);
	}

}
