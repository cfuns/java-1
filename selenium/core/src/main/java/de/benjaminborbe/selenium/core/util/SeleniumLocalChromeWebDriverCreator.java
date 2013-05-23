package de.benjaminborbe.selenium.core.util;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

public class SeleniumLocalChromeWebDriverCreator {

    public WebDriver create() {
        // final String chromepath = "/Applications/Google Chrome.app/Contents/MacOS/Google Chrome";
        // System.setProperty("webdriver.chrome.driver", chromepath);
        // capabilities.setCapability("chrome.switches", Arrays.asList("--start-maximized","--no-sandbox","--allow-running-insecure-content"));
        // capabilities.setCapability("chrome.binary", chromepath);

        final String chromepath = "/opt/chromedriver/chromedriver";
        System.setProperty("webdriver.chrome.driver", chromepath);

        final DesiredCapabilities capabilities = DesiredCapabilities.chrome();
        return new ChromeDriver(capabilities);
    }
}
