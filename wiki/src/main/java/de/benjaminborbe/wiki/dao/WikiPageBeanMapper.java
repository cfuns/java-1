package de.benjaminborbe.wiki.dao;

import java.util.Map;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.tools.mapper.BaseMapper;
import de.benjaminborbe.tools.mapper.MapException;
import de.benjaminborbe.tools.util.ParseException;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.wiki.api.WikiPageContentType;
import de.benjaminborbe.wiki.api.WikiPageIdentifier;

@Singleton
public class WikiPageBeanMapper extends BaseMapper<WikiPageBean> {

	private static final String ID = "id";

	private static final String TITLE = "title";

	private static final String CONTENT = "content";

	private static final String CONTENT_TYPE = "content_type";

	private final ParseUtil parseUtil;

	@Inject
	public WikiPageBeanMapper(final Provider<WikiPageBean> provider, final ParseUtil parseUtil) {
		super(provider);
		this.parseUtil = parseUtil;
	}

	@Override
	public void map(final WikiPageBean object, final Map<String, String> data) {
		data.put(ID, toString(object.getId()));
		data.put(TITLE, object.getTitle());
		data.put(CONTENT, object.getContent());
		data.put(CONTENT_TYPE, toString(object.getContentType()));
	}

	@Override
	public void map(final Map<String, String> data, final WikiPageBean object) throws MapException {
		try {
			object.setId(toWikiPageIdentifier(data.get(ID)));
			object.setTitle(data.get(TITLE));
			object.setContent(data.get(CONTENT));
			object.setContentType(toWikiPageContentType(data.get(CONTENT_TYPE)));
		}
		catch (final ParseException e) {
			throw new MapException(e.getClass().getSimpleName(), e);
		}
	}

	private WikiPageContentType toWikiPageContentType(final String value) throws ParseException {
		return parseUtil.parseEnum(WikiPageContentType.class, value);
	}

	private WikiPageIdentifier toWikiPageIdentifier(final String id) {
		return id != null ? new WikiPageIdentifier(id) : null;
	}

	private String toString(final WikiPageIdentifier id) {
		return id != null ? id.getId() : null;
	}

	private String toString(final Enum<?> e) {
		return e != null ? e.name() : null;
	}

}
