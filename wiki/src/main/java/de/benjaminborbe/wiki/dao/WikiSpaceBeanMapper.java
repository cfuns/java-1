package de.benjaminborbe.wiki.dao;

import java.util.Map;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.tools.mapper.MapException;
import de.benjaminborbe.tools.mapper.mapobject.MapObjectMapperBase;
import de.benjaminborbe.wiki.api.WikiSpaceIdentifier;

@Singleton
public class WikiSpaceBeanMapper extends MapObjectMapperBase<WikiSpaceBean> {

	private static final String ID = "id";

	private static final String NAME = "name";

	@Inject
	public WikiSpaceBeanMapper(final Provider<WikiSpaceBean> provider) {
		super(provider);
	}

	@Override
	public void map(final WikiSpaceBean object, final Map<String, String> data) {
		data.put(ID, toString(object.getId()));
		data.put(NAME, object.getName());
	}

	@Override
	public void map(final Map<String, String> data, final WikiSpaceBean object) throws MapException {
		object.setId(toWikiSpaceIdentifier(data.get(ID)));
		object.setName(data.get(NAME));
	}

	private WikiSpaceIdentifier toWikiSpaceIdentifier(final String id) {
		return id != null ? new WikiSpaceIdentifier(id) : null;
	}

	private String toString(final WikiSpaceIdentifier id) {
		return id != null ? id.getId() : null;
	}
}
