package de.benjaminborbe.selenium.mock;

import de.benjaminborbe.selenium.api.SeleniumService;

public class SeleniumServiceMock implements SeleniumService {

	public SeleniumServiceMock() {
	}

	@Override
	public long calc(final long value) {
		return 0;
	}
}
