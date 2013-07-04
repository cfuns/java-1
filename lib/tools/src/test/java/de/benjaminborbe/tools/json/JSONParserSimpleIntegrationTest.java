package de.benjaminborbe.tools.json;

import com.google.inject.Injector;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import de.benjaminborbe.tools.guice.ToolModules;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class JSONParserSimpleIntegrationTest {

	@Test
	public void testSingleton() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new ToolModules());
		final JSONParser parser = injector.getInstance(JSONParser.class);
		assertNotNull(parser);
		assertEquals(JSONParserSimple.class, parser.getClass());
	}
}
