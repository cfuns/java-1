package de.benjaminborbe.selenium.core.util;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class SeleniumCoreWebDriver {

	private final WebDriver driver;

	private boolean closed = false;

	public SeleniumCoreWebDriver(final WebDriver driver) {
		this.driver = driver;
	}

	public WebDriver getDriver() {
		return driver;
	}

	public void close() {
		if (!closed) {
			closed = true;
			driver.close();
		}
	}

	public WebElement findElement(final By xpath) {
		return driver.findElement(xpath);
	}

	public List<WebElement> findElements(final By xpath) {
		return driver.findElements(xpath);
	}

	public String getTitle() {
		return driver.getTitle();
	}

	public String getCurrentUrl() {
		return driver.getCurrentUrl();
	}

	public String getPageSource() {
		return driver.getPageSource();
	}

	public String getWindowHandle() {
		return driver.getWindowHandle();
	}

	public Set<String> getWindowHandles() {
		return driver.getWindowHandles();
	}

	public void get(final String url) {
		driver.get(url);
	}

	public void maximize() {
		driver.manage().window().maximize();
	}

	// http://docs.seleniumhq.org/docs/04_webdriver_advanced.jsp
	public void implicitlyWait(final int l, final TimeUnit timeUnit) {
		driver.manage().timeouts().implicitlyWait(l, timeUnit);
	}

	public boolean isClosed() {
		return closed;
	}

	public boolean check() {
		try {
			driver.getTitle();
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
