package de.benjaminborbe.analytics.util;

import de.benjaminborbe.analytics.api.AnalyticsReportIdentifier;
import de.benjaminborbe.tools.mapper.Mapper;

public class MapperAnalyticsReportIdentifier implements Mapper<AnalyticsReportIdentifier> {

	@Override
	public String toString(final AnalyticsReportIdentifier value) {
		return value != null ? value.getId() : null;
	}

	@Override
	public AnalyticsReportIdentifier fromString(final String value) {
		return value != null ? new AnalyticsReportIdentifier(value) : null;
	}

}
