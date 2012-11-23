package de.benjaminborbe.confluence.gui.util;

import de.benjaminborbe.confluence.api.ConfluenceInstance;
import de.benjaminborbe.tools.util.ComparatorBase;

public class ConfluenceInstanceComparator extends ComparatorBase<ConfluenceInstance, String> {

	@Override
	public String getValue(final ConfluenceInstance o) {
		return o.getUrl();
	}

}
