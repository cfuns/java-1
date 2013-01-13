package de.benjaminborbe.analytics.gui.util;

import de.benjaminborbe.analytics.api.AnalyticsReport;
import de.benjaminborbe.tools.util.ComparatorBase;

public class AnalyticsReportComparator extends ComparatorBase<AnalyticsReport, String> {

	@Override
	public String getValue(final AnalyticsReport o) {
		return o.getName() != null ? o.getName().toLowerCase() : null;
	}

}
