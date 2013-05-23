package de.benjaminborbe.selenium.core.action;

import de.benjaminborbe.selenium.api.action.SeleniumActionConfigurationSleep;
import de.benjaminborbe.selenium.core.util.SeleniumCoreWebDriver;
import de.benjaminborbe.selenium.core.util.SeleniumExecutionProtocolImpl;

public class SeleniumActionSleep implements SeleniumAction<SeleniumActionConfigurationSleep> {

	@Override
	public Class<SeleniumActionConfigurationSleep> getType() {
		return SeleniumActionConfigurationSleep.class;
	}

	@Override
	public boolean execute(
		final SeleniumCoreWebDriver webDriver,
		final SeleniumExecutionProtocolImpl seleniumExecutionProtocol,
		final SeleniumActionConfigurationSleep seleniumActionConfiguration
	) {
		seleniumExecutionProtocol.addInfo(seleniumActionConfiguration.getMessage());
		try {
			Thread.sleep(seleniumActionConfiguration.getDuration());
		} catch (InterruptedException e) {

		}

		return true;
	}

}
