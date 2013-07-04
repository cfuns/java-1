package de.benjaminborbe.wiki.render;

import com.google.inject.Injector;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import de.benjaminborbe.wiki.api.WikiPageContentType;
import de.benjaminborbe.wiki.guice.WikiModulesMock;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class WikiConfluenceRendererIntegrationTest {

	@Test
	public void testInject() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new WikiModulesMock());
		final WikiRendererFactory wikiRendererFactory = injector.getInstance(WikiRendererFactory.class);
		assertNotNull(wikiRendererFactory);
		final WikiRenderer renderer = wikiRendererFactory.getRenderer(WikiPageContentType.CONFLUENCE);
		assertNotNull(renderer);
		assertEquals(WikiConfluenceRenderer.class, renderer.getClass());
	}

}
