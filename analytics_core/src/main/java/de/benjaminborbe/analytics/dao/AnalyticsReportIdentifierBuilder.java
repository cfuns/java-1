package de.benjaminborbe.analytics.dao;

import de.benjaminborbe.analytics.api.AnalyticsReportIdentifier;
import de.benjaminborbe.api.IdentifierBuilder;

public class AnalyticsReportIdentifierBuilder implements IdentifierBuilder<String, AnalyticsReportIdentifier> {

	@Override
	public AnalyticsReportIdentifier buildIdentifier(final String value) {
		return new AnalyticsReportIdentifier(value);
	}

}
