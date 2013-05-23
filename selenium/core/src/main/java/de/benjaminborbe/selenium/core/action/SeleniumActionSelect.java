package de.benjaminborbe.selenium.core.action;

import de.benjaminborbe.selenium.api.action.SeleniumActionConfigurationSelect;
import de.benjaminborbe.selenium.core.util.SeleniumCoreWebDriver;
import de.benjaminborbe.selenium.core.util.SeleniumExecutionProtocolImpl;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

public class SeleniumActionSelect implements SeleniumAction<SeleniumActionConfigurationSelect> {

	@Override
	public Class<SeleniumActionConfigurationSelect> getType() {
		return SeleniumActionConfigurationSelect.class;
	}

	@Override
	public boolean execute(
		final SeleniumCoreWebDriver webDriver,
		final SeleniumExecutionProtocolImpl seleniumExecutionProtocol,
		final SeleniumActionConfigurationSelect seleniumActionConfiguration
	) {

		final String xpathExpression = seleniumActionConfiguration.getXpath();
		final WebElement element = webDriver.findElement(By.xpath(xpathExpression));
		final Select select = new Select(element);
		select.selectByValue(seleniumActionConfiguration.getValue());
		seleniumExecutionProtocol.addInfo(seleniumActionConfiguration.getMessage());

		return true;
	}

}
