package de.benjaminborbe.lunch.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.lunch.guice.LunchModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import de.benjaminborbe.tools.util.ResourceUtil;

public class LunchParseUtilIntegrationTest {

	@Test
	public void testInject() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new LunchModulesMock());
		assertNotNull(injector.getInstance(LunchParseUtil.class));
	}

	@Test
	public void testExtractLunchNameViaFile() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new LunchModulesMock());
		final LunchParseUtil lunchParseUtil = injector.getInstance(LunchParseUtil.class);
		final ResourceUtil resourceUtil = injector.getInstance(ResourceUtil.class);

		{
			final String filename = "page_v1.html";
			final String htmlContent = resourceUtil.getResourceContentAsString(filename);
			assertEquals("Spiegeleier mit Spinat und Salzkartoffeln dazu Salat und Ketchup", lunchParseUtil.extractLunchName(htmlContent));
		}

		{
			final String filename = "page_v2.html";
			final String htmlContent = resourceUtil.getResourceContentAsString(filename);
			assertEquals("Hausgemachte Gemüselasagne (vegetarisch) mit buntem Beilagensalat", lunchParseUtil.extractLunchName(htmlContent));
		}

	}
}
