package de.benjaminborbe.tools.json;

import static org.junit.Assert.*;
import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import de.benjaminborbe.tools.guice.ToolModules;

public class JSONParserSimpleIntegrationTest {

	@Test
	public void testSingleton() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new ToolModules());
		final JSONParser parser = injector.getInstance(JSONParser.class);
		assertNotNull(parser);
		assertEquals(JSONParserSimple.class, parser.getClass());
	}
}
