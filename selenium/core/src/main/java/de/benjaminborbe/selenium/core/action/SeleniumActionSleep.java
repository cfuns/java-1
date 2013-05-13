package de.benjaminborbe.selenium.core.action;

import de.benjaminborbe.selenium.api.action.SeleniumActionConfigurationSleep;
import de.benjaminborbe.selenium.core.util.SeleniumExecutionProtocolImpl;
import org.openqa.selenium.WebDriver;

public class SeleniumActionSleep implements SeleniumAction<SeleniumActionConfigurationSleep> {

	@Override
	public Class<SeleniumActionConfigurationSleep> getType() {
		return SeleniumActionConfigurationSleep.class;
	}

	@Override
	public boolean execute(
		final WebDriver webDriver, final SeleniumExecutionProtocolImpl seleniumExecutionProtocol, final SeleniumActionConfigurationSleep seleniumActionConfiguration
	) {
		seleniumExecutionProtocol.addInfo(seleniumActionConfiguration.getMessage());
		try {
			Thread.sleep(seleniumActionConfiguration.getDuration());
		} catch (InterruptedException e) {

		}

		return true;
	}

}
