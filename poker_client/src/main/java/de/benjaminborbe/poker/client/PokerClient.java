package de.benjaminborbe.poker.client;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Injector;

import de.benjaminborbe.poker.client.guice.PokerClientModules;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class PokerClient {

	private final Logger logger;

	@Inject
	public PokerClient(final Logger logger) {
		this.logger = logger;
	}

	public static void main(final String[] args) {

		final Injector injector = GuiceInjectorBuilder.getInjector(new PokerClientModules());
		final PokerClient client = injector.getInstance(PokerClient.class);
		client.run();
	}

	private void run() {
		logger.debug("run");
	}
}
