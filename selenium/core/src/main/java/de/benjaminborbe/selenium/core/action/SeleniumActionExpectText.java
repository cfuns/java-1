package de.benjaminborbe.selenium.core.action;

import de.benjaminborbe.selenium.api.action.SeleniumActionConfigurationExpectText;
import de.benjaminborbe.selenium.core.util.SeleniumCoreWebDriver;
import de.benjaminborbe.selenium.core.util.SeleniumExecutionProtocolImpl;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class SeleniumActionExpectText implements SeleniumAction<SeleniumActionConfigurationExpectText> {

	@Override
	public Class<SeleniumActionConfigurationExpectText> getType() {
		return SeleniumActionConfigurationExpectText.class;
	}

	@Override
	public boolean execute(
		final SeleniumCoreWebDriver webDriver,
		final SeleniumExecutionProtocolImpl seleniumExecutionProtocol,
		final SeleniumActionConfigurationExpectText seleniumActionConfiguration
	) {

		final String xpathExpression = seleniumActionConfiguration.getXpath();
		final WebElement element = webDriver.findElement(By.xpath(xpathExpression));
		if (seleniumActionConfiguration.getText().equals(element.getText())) {
			seleniumExecutionProtocol.addInfo(seleniumActionConfiguration.getMessage() + " found");
			return true;
		} else {
			seleniumExecutionProtocol.addError(seleniumActionConfiguration.getMessage() + " not found");
			return false;
		}
	}
}
