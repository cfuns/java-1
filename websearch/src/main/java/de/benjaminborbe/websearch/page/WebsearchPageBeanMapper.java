package de.benjaminborbe.websearch.page;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.tools.mapper.MapperCalendar;
import de.benjaminborbe.tools.mapper.mapobject.MapObjectMapperAdapter;
import de.benjaminborbe.tools.mapper.stringobject.StringObjectMapper;
import de.benjaminborbe.tools.mapper.stringobject.StringObjectMapperAdapter;
import de.benjaminborbe.websearch.api.WebsearchPageIdentifier;

@Singleton
public class WebsearchPageBeanMapper extends MapObjectMapperAdapter<WebsearchPageBean> {

	@Inject
	public WebsearchPageBeanMapper(final Provider<WebsearchPageBean> provider, final MapperCalendar mapperCalendar, final MapperWebsearchPageIdentifier mapperWebsearchPageIdentifier) {
		super(provider, buildMappings(mapperCalendar, mapperWebsearchPageIdentifier));
	}

	private static Collection<StringObjectMapper<WebsearchPageBean>> buildMappings(final MapperCalendar mapperCalendar,
			final MapperWebsearchPageIdentifier mapperWebsearchPageIdentifier) {
		final List<StringObjectMapper<WebsearchPageBean>> result = new ArrayList<StringObjectMapper<WebsearchPageBean>>();
		result.add(new StringObjectMapperAdapter<WebsearchPageBean, WebsearchPageIdentifier>("id", mapperWebsearchPageIdentifier));
		result.add(new StringObjectMapperAdapter<WebsearchPageBean, Calendar>("lastVisit", mapperCalendar));
		result.add(new StringObjectMapperAdapter<WebsearchPageBean, Calendar>("created", mapperCalendar));
		result.add(new StringObjectMapperAdapter<WebsearchPageBean, Calendar>("modified", mapperCalendar));
		return result;
	}
}

// extends MapObjectMapperBase<WebsearchPageBean> {
//
// private final DateUtil dateUtil;
//
// @Inject
// public WebsearchPageBeanMapper(final Provider<WebsearchPageBean> provider, final
// DateUtil dateUtil) {
// super(provider);
// this.dateUtil = dateUtil;
// }
//
// @Override
// public void map(final WebsearchPageBean object, final Map<String, String> data) {
// data.put("id", toString(object.getId()));
// data.put("lastvisit", toString(object.getLastVisit()));
// data.put("url", toString(object.getUrl()));
// }
//
// @Override
// public void map(final Map<String, String> data, final WebsearchPageBean object) throws
// MapException {
// // todo null safe machen
// object.setId(toPageIdentifier(data.get("id")));
// object.setLastVisit(toDate(data.get("lastvisit")));
// object.setUrl(toUrl(data.get("url")));
// }
//
// private WebsearchPageIdentifier toPageIdentifier(final String url) throws MapException
// {
// return new WebsearchPageIdentifier(toUrl(url));
// }
//
// private String toString(final Date date) {
// return date != null ? dateUtil.dateTimeString(date) : null;
// }
//
// private String toString(final WebsearchPageIdentifier pageIdentifier) {
// return pageIdentifier != null ? pageIdentifier.getId() : null;
// }
//
// private String toString(final URL url) {
// return url != null ? url.toExternalForm() : null;
// }
//
// private URL toUrl(final String url) throws MapException {
// try {
// return new URL(url);
// }
// catch (final MalformedURLException e) {
// throw new MapException("MalformedURLException - url: '" + url + "'", e);
// }
// }
//
// private Date toDate(final String datetime) throws MapException {
// try {
// return datetime != null ? dateUtil.parseDateTime(datetime) : null;
// }
// catch (final ParseException e) {
// throw new MapException("ParseException - datetime: " + datetime, e);
// }
// catch (final NumberFormatException e) {
// throw new MapException("NumberFormatException - datetime: " + datetime, e);
// }
// }
// }
