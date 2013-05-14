package de.benjaminborbe.selenium.gui.util;

import de.benjaminborbe.selenium.api.SeleniumConfigurationIdentifier;
import de.benjaminborbe.tools.util.ComparatorBase;

public class SeleniumConfigurationIdentifierComparator extends ComparatorBase<SeleniumConfigurationIdentifier, String> {

	@Override
	public String getValue(final SeleniumConfigurationIdentifier o) {
		return o.getId() != null ? o.getId().toLowerCase() : null;
	}
}
