package de.benjaminborbe.selenium.core.util;

import de.benjaminborbe.selenium.api.SeleniumConfigurationIdentifier;

public class SeleniumCoreRunner implements Runnable {

	private final SeleniumCoreExecutor seleniumCoreExecutor;

	private final SeleniumConfigurationIdentifier seleniumConfigurationIdentifier;

	private final SeleniumExecutionProtocolImpl seleniumExecutionProtocol;

	public SeleniumCoreRunner(
		final SeleniumCoreExecutor seleniumCoreExecutor,
		final SeleniumConfigurationIdentifier seleniumConfigurationIdentifier,
		final SeleniumExecutionProtocolImpl seleniumExecutionProtocol
	) {
		this.seleniumCoreExecutor = seleniumCoreExecutor;
		this.seleniumConfigurationIdentifier = seleniumConfigurationIdentifier;
		this.seleniumExecutionProtocol = seleniumExecutionProtocol;
	}

	@Override
	public void run() {
		seleniumCoreExecutor.execute(seleniumConfigurationIdentifier, seleniumExecutionProtocol);
	}
}
