package de.benjaminborbe.poker.client;

import com.google.inject.Injector;
import de.benjaminborbe.poker.client.guice.PokerClientModules;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class PokerClientIntegrationTest {

	@Test
	public void testInject() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new PokerClientModules());
		final PokerClient pokerClient = injector.getInstance(PokerClient.class);
		assertThat(pokerClient.getClass().getName(), is(PokerClient.class.getName()));
	}
}
