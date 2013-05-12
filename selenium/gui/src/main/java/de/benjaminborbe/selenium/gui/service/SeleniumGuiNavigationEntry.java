package de.benjaminborbe.selenium.gui.service;

import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.navigation.api.NavigationEntry;
import de.benjaminborbe.selenium.api.SeleniumService;
import de.benjaminborbe.selenium.api.SeleniumServiceException;
import de.benjaminborbe.selenium.gui.SeleniumGuiConstants;

import javax.inject.Inject;

public class SeleniumGuiNavigationEntry implements NavigationEntry {

	private final SeleniumService seleniumService;

	@Inject
	public SeleniumGuiNavigationEntry(final SeleniumService seleniumService) {
		this.seleniumService = seleniumService;
	}

	@Override
	public String getTitle() {
		return "Selenium";
	}

	@Override
	public String getURL() {
		return "/" + SeleniumGuiConstants.NAME;
	}

	@Override
	public boolean isVisible(final SessionIdentifier sessionIdentifier) {
		try {
			return seleniumService.hasPermission(sessionIdentifier);
		} catch (final SeleniumServiceException e) {
			return false;
		}
	}

}
