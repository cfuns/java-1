package de.benjaminborbe.wiki.util;

import de.benjaminborbe.tools.mapper.MapperEnum;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.wiki.api.WikiPageContentType;

import javax.inject.Inject;

public class MapperWikiPageContentType extends MapperEnum<WikiPageContentType> {

	@Inject
	public MapperWikiPageContentType(final ParseUtil parseUtil) {
		super(parseUtil);
	}

	@Override
	protected Class<WikiPageContentType> getEnumClass() {
		return WikiPageContentType.class;
	}

}
