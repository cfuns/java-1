package de.benjaminborbe.selenium.core.util;

import de.benjaminborbe.selenium.api.SeleniumConfiguration;

public class SeleniumCoreRunner implements Runnable {

	private final SeleniumCoreExecutor seleniumCoreExecutor;

	private final SeleniumConfiguration seleniumConfiguration;

	private final SeleniumExecutionProtocolImpl seleniumExecutionProtocol;

	public SeleniumCoreRunner(
		final SeleniumCoreExecutor seleniumCoreExecutor,
		final SeleniumConfiguration seleniumConfiguration,
		final SeleniumExecutionProtocolImpl seleniumExecutionProtocol
	) {
		this.seleniumCoreExecutor = seleniumCoreExecutor;
		this.seleniumConfiguration = seleniumConfiguration;
		this.seleniumExecutionProtocol = seleniumExecutionProtocol;
	}

	@Override
	public void run() {
		seleniumCoreExecutor.execute(seleniumConfiguration, seleniumExecutionProtocol);
	}
}
