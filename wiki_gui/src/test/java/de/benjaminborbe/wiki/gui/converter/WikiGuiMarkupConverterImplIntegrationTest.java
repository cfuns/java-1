package de.benjaminborbe.wiki.gui.converter;

import static org.junit.Assert.*;
import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import de.benjaminborbe.wiki.gui.guice.WikiGuiModulesMock;

public class WikiGuiMarkupConverterImplIntegrationTest {

	@Test
	public void testInject() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new WikiGuiModulesMock());
		assertNotNull(injector.getInstance(WikiGuiMarkupConverter.class));
		assertEquals(WikiGuiMarkupConverterImpl.class, injector.getInstance(WikiGuiMarkupConverter.class).getClass());
	}

}
