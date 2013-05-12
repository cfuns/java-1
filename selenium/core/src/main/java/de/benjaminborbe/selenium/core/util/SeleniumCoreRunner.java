package de.benjaminborbe.selenium.core.util;

import de.benjaminborbe.selenium.api.SeleniumConfigurationIdentifier;

public class SeleniumCoreRunner implements Runnable {

	private final SeleniumCoreExecutor seleniumCoreExecutor;

	private final SeleniumConfigurationIdentifier seleniumConfigurationIdentifier;

	public SeleniumCoreRunner(final SeleniumCoreExecutor seleniumCoreExecutor, final SeleniumConfigurationIdentifier seleniumConfigurationIdentifier) {
		this.seleniumCoreExecutor = seleniumCoreExecutor;
		this.seleniumConfigurationIdentifier = seleniumConfigurationIdentifier;
	}

	@Override
	public void run() {
		seleniumCoreExecutor.execute(seleniumConfigurationIdentifier);
	}
}
