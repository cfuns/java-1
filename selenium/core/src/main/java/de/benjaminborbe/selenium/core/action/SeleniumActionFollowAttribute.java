package de.benjaminborbe.selenium.core.action;

import de.benjaminborbe.selenium.api.action.SeleniumActionConfigurationFollowAttribute;
import de.benjaminborbe.selenium.core.util.SeleniumExecutionProtocolImpl;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class SeleniumActionFollowAttribute implements SeleniumAction<SeleniumActionConfigurationFollowAttribute> {

	@Override
	public Class<SeleniumActionConfigurationFollowAttribute> getType() {
		return SeleniumActionConfigurationFollowAttribute.class;
	}

	@Override
	public boolean execute(
		final WebDriver webDriver,
		final SeleniumExecutionProtocolImpl seleniumExecutionProtocol,
		final SeleniumActionConfigurationFollowAttribute seleniumActionConfiguration
	) {

		final String xpathExpression = seleniumActionConfiguration.getXpath();
		String src = webDriver.findElement(By.xpath(xpathExpression)).getAttribute(seleniumActionConfiguration.getAttribute());
		webDriver.get(src);
		seleniumExecutionProtocol.addInfo(seleniumActionConfiguration.getMessage());

		return true;
	}
}
