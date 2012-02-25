package de.benjaminborbe.websearch.page;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.Map;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.tools.date.DateUtil;
import de.benjaminborbe.tools.mapper.BaseMapper;
import de.benjaminborbe.tools.mapper.MapException;
import de.benjaminborbe.tools.util.ParseException;
import de.benjaminborbe.websearch.api.PageIdentifier;

@Singleton
public class PageBeanMapper extends BaseMapper<PageBean> {

	private final DateUtil dateUtil;

	@Inject
	public PageBeanMapper(final Provider<PageBean> provider, final DateUtil dateUtil) {
		super(provider);
		this.dateUtil = dateUtil;
	}

	@Override
	public void map(final PageBean object, final Map<String, String> data) {
		data.put("id", toString(object.getId()));
		data.put("lastvisit", toString(object.getLastVisit()));
		data.put("url", toString(object.getUrl()));
	}

	@Override
	public void map(final Map<String, String> data, final PageBean object) throws MapException {
		// todo null safe machen
		object.setId(toPageIdentifier(data.get("id")));
		object.setLastVisit(toDate(data.get("lastvisit")));
		object.setUrl(toUrl(data.get("url")));
	}

	private PageIdentifier toPageIdentifier(final String url) throws MapException {
		return new PageIdentifier(toUrl(url));
	}

	private String toString(final Date date) {
		return date != null ? dateUtil.dateTimeString(date) : null;
	}

	private String toString(final PageIdentifier pageIdentifier) {
		return pageIdentifier != null ? pageIdentifier.getId() : null;
	}

	private String toString(final URL url) {
		return url != null ? url.toExternalForm() : null;
	}

	private URL toUrl(final String url) throws MapException {
		try {
			return new URL(url);
		}
		catch (final MalformedURLException e) {
			throw new MapException("MalformedURLException - url: " + url, e);
		}
	}

	private Date toDate(final String datetime) throws MapException {
		try {
			return datetime != null ? dateUtil.parseDateTime(datetime) : null;
		}
		catch (final ParseException e) {
			throw new MapException("ParseException - datetime: " + datetime, e);
		}
		catch (final NumberFormatException e) {
			throw new MapException("NumberFormatException - datetime: " + datetime, e);
		}
	}
}
