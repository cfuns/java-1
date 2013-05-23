package de.benjaminborbe.selenium.core.util;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class SeleniumLocalFirefoxWebDriverCreator {

    public WebDriver create() {
        return new FirefoxDriver();
    }
}
