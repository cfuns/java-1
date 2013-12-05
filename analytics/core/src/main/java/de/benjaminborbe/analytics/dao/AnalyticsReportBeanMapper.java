package de.benjaminborbe.analytics.dao;

import com.google.inject.Provider;
import de.benjaminborbe.analytics.api.AnalyticsReportAggregation;
import de.benjaminborbe.analytics.api.AnalyticsReportIdentifier;
import de.benjaminborbe.analytics.util.MapperAnalyticsReportAggregation;
import de.benjaminborbe.analytics.util.MapperAnalyticsReportIdentifier;
import de.benjaminborbe.tools.mapper.MapperCalendar;
import de.benjaminborbe.tools.mapper.MapperString;
import de.benjaminborbe.tools.mapper.mapobject.MapObjectMapperAdapter;
import de.benjaminborbe.tools.mapper.stringobject.StringObjectMapper;
import de.benjaminborbe.tools.mapper.stringobject.StringObjectMapperAdapter;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

@Singleton
public class AnalyticsReportBeanMapper extends MapObjectMapperAdapter<AnalyticsReportBean> {

	public static final String NAME = "name";

	public static final String DESCRIPTION = "description";

	public static final String AGGREGATION = "aggregation";

	public static final String CREATED = "created";

	public static final String MODIFIED = "modified";

	public static final String ID = "id";

	@Inject
	public AnalyticsReportBeanMapper(
		final Provider<AnalyticsReportBean> provider,
		final MapperAnalyticsReportIdentifier mapperListIdentifier,
		final MapperString mapperString,
		final MapperCalendar mapperCalendar,
		final MapperAnalyticsReportAggregation mapperAnalyticsReportAggregation
	) {
		super(provider, buildMappings(mapperListIdentifier, mapperString, mapperCalendar, mapperAnalyticsReportAggregation));
	}

	private static Collection<StringObjectMapper<AnalyticsReportBean>> buildMappings(
		final MapperAnalyticsReportIdentifier mapperListIdentifier, final MapperString mapperString,
		final MapperCalendar mapperCalendar, final MapperAnalyticsReportAggregation mapperAnalyticsReportAggregation
	) {
		final List<StringObjectMapper<AnalyticsReportBean>> result = new ArrayList<StringObjectMapper<AnalyticsReportBean>>();
		result.add(new StringObjectMapperAdapter<AnalyticsReportBean, AnalyticsReportIdentifier>(ID, mapperListIdentifier));
		result.add(new StringObjectMapperAdapter<AnalyticsReportBean, String>(NAME, mapperString));
		result.add(new StringObjectMapperAdapter<AnalyticsReportBean, String>(DESCRIPTION, mapperString));
		result.add(new StringObjectMapperAdapter<AnalyticsReportBean, AnalyticsReportAggregation>(AGGREGATION, mapperAnalyticsReportAggregation));
		result.add(new StringObjectMapperAdapter<AnalyticsReportBean, Calendar>(CREATED, mapperCalendar));
		result.add(new StringObjectMapperAdapter<AnalyticsReportBean, Calendar>(MODIFIED, mapperCalendar));
		return result;
	}
}
