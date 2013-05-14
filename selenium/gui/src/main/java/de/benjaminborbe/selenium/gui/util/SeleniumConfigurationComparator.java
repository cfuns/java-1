package de.benjaminborbe.selenium.gui.util;

import de.benjaminborbe.selenium.api.SeleniumConfiguration;
import de.benjaminborbe.tools.util.ComparatorBase;

public class SeleniumConfigurationComparator extends ComparatorBase<SeleniumConfiguration, String> {

	@Override
	public String getValue(final SeleniumConfiguration o) {
		return o.getName() != null ? o.getName().toLowerCase() : null;
	}
}
