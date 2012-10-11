package de.benjaminborbe.lunch.connector;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Collection;

import org.junit.Ignore;
import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.lunch.api.Lunch;
import de.benjaminborbe.lunch.guice.LunchModulesMock;
import de.benjaminborbe.tools.date.DateUtil;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class WikiConnectorIntegrationTest {

	private final String spaceKey = "MITTAG";

	private final String username = "bborbe";

	private final String password = "xxx";

	private final String fullname = "Benjamin Borbe";

	@Ignore
	@Test
	public void testname() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new LunchModulesMock());
		final DateUtil dateUtil = injector.getInstance(DateUtil.class);
		final WikiConnector c = injector.getInstance(WikiConnector.class);
		final Collection<Lunch> result = c.extractLunchs(spaceKey, username, password, fullname);
		assertNotNull(result);
		assertTrue(result.size() > 0);

		for (final Lunch l : result) {
			System.err.println(dateUtil.dateString(l.getDate()) + " - " + l.getName());
		}
	}
}
