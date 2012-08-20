package de.benjaminborbe.wiki.dao;

import java.util.Map;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.tools.mapper.BaseMapper;
import de.benjaminborbe.tools.mapper.MapException;
import de.benjaminborbe.wiki.api.WikiPageIdentifier;

@Singleton
public class WikiPageBeanMapper extends BaseMapper<WikiPageBean> {

	private static final String ID = "id";

	private static final String TITLE = "title";

	private static final String CONTENT = "content";

	@Inject
	public WikiPageBeanMapper(final Provider<WikiPageBean> provider) {
		super(provider);
	}

	@Override
	public void map(final WikiPageBean object, final Map<String, String> data) {
		data.put(ID, toString(object.getId()));
		data.put(TITLE, object.getTitle());
		data.put(CONTENT, object.getContent());
	}

	@Override
	public void map(final Map<String, String> data, final WikiPageBean object) throws MapException {
		object.setId(toWikiPageIdentifier(data.get(ID)));
		object.setTitle(data.get(TITLE));
		object.setContent(data.get(CONTENT));
	}

	private WikiPageIdentifier toWikiPageIdentifier(final String id) {
		return id != null ? new WikiPageIdentifier(id) : null;
	}

	private String toString(final WikiPageIdentifier id) {
		return id != null ? id.getId() : null;
	}
}
