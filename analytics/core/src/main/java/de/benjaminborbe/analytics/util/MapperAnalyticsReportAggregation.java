package de.benjaminborbe.analytics.util;

import de.benjaminborbe.analytics.api.AnalyticsReportAggregation;
import de.benjaminborbe.tools.mapper.MapperEnum;
import de.benjaminborbe.tools.util.ParseUtil;

import javax.inject.Inject;

public class MapperAnalyticsReportAggregation extends MapperEnum<AnalyticsReportAggregation> {

	@Inject
	public MapperAnalyticsReportAggregation(final ParseUtil parseUtil) {
		super(parseUtil);
	}

	@Override
	protected Class<AnalyticsReportAggregation> getEnumClass() {
		return AnalyticsReportAggregation.class;
	}

}
