package de.benjaminborbe.selenium.configuration.xml.dao;

import com.google.inject.Provider;
import de.benjaminborbe.selenium.api.SeleniumConfigurationIdentifier;
import de.benjaminborbe.selenium.configuration.xml.util.MapperSeleniumConfigurationIdentifier;
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
public class SeleniumConfigurationXmlBeanMapper extends MapObjectMapperAdapter<SeleniumConfigurationXmlBean> {

	@Inject
	public SeleniumConfigurationXmlBeanMapper(
		final Provider<SeleniumConfigurationXmlBean> provider,
		final MapperSeleniumConfigurationIdentifier mapperSeleniumConfigurationXmlIdentifier,
		final MapperCalendar mapperCalendar,
		final MapperString mapperString
	) {
		super(provider, buildMappings(mapperSeleniumConfigurationXmlIdentifier, mapperCalendar, mapperString));
	}

	private static Collection<StringObjectMapper<SeleniumConfigurationXmlBean>> buildMappings(
		final MapperSeleniumConfigurationIdentifier mapperSeleniumConfigurationXmlIdentifier, final MapperCalendar mapperCalendar, final MapperString mapperString
	) {
		final List<StringObjectMapper<SeleniumConfigurationXmlBean>> result = new ArrayList<>();
		result.add(new StringObjectMapperAdapter<SeleniumConfigurationXmlBean, SeleniumConfigurationIdentifier>("id", mapperSeleniumConfigurationXmlIdentifier));
		result.add(new StringObjectMapperAdapter<SeleniumConfigurationXmlBean, String>("xml", mapperString));
		result.add(new StringObjectMapperAdapter<SeleniumConfigurationXmlBean, Calendar>("created", mapperCalendar));
		result.add(new StringObjectMapperAdapter<SeleniumConfigurationXmlBean, Calendar>("modified", mapperCalendar));
		return result;
	}
}
