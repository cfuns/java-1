package de.benjaminborbe.selenium.core.util;

import de.benjaminborbe.selenium.api.SeleniumConfiguration;

public class SeleniumCoreRunner implements Runnable {

	private final SeleniumCoreConfigurationExecutor seleniumCoreConfigurationExecutor;

	private final SeleniumConfiguration seleniumConfiguration;

	private final SeleniumExecutionProtocolImpl seleniumExecutionProtocol;

	public SeleniumCoreRunner(
		final SeleniumCoreConfigurationExecutor seleniumCoreConfigurationExecutor,
		final SeleniumConfiguration seleniumConfiguration,
		final SeleniumExecutionProtocolImpl seleniumExecutionProtocol
	) {
		this.seleniumCoreConfigurationExecutor = seleniumCoreConfigurationExecutor;
		this.seleniumConfiguration = seleniumConfiguration;
		this.seleniumExecutionProtocol = seleniumExecutionProtocol;
	}

	@Override
	public void run() {
		seleniumCoreConfigurationExecutor.execute(seleniumConfiguration, seleniumExecutionProtocol);
	}
}
