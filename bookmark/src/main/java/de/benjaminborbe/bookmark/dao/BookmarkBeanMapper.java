package de.benjaminborbe.bookmark.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.bookmark.api.BookmarkIdentifier;
import de.benjaminborbe.tools.mapper.BaseMapper;
import de.benjaminborbe.tools.mapper.MapException;
import de.benjaminborbe.tools.util.ParseException;
import de.benjaminborbe.tools.util.ParseUtil;

@Singleton
public class BookmarkBeanMapper extends BaseMapper<BookmarkBean> {

	private static final String ID = "id";

	private static final String NAME = "name";

	private static final String URL = "url";

	private static final String DESCRIPTION = "description";

	private static final String FAVORITE = "favorite";

	private static final String OWNER = "owner";

	private static final String KEYWORDS = "keywords";

	private final ParseUtil parseUtil;

	@Inject
	public BookmarkBeanMapper(final Provider<BookmarkBean> provider, final ParseUtil parseUtil) {
		super(provider);
		this.parseUtil = parseUtil;
	}

	@Override
	public void map(final BookmarkBean object, final Map<String, String> data) {
		data.put(ID, toString(object.getId()));
		data.put(NAME, object.getName());
		data.put(URL, object.getUrl());
		data.put(DESCRIPTION, object.getDescription());
		data.put(FAVORITE, toString(object.isFavorite()));
		data.put(OWNER, object.getOwner() != null ? object.getOwner().getId() : null);
		data.put(KEYWORDS, toString(object.getKeywords()));
	}

	@Override
	public void map(final Map<String, String> data, final BookmarkBean object) throws MapException {
		object.setId(toBookmarkIdentifier(data.get(ID)));
		object.setName(data.get(NAME));
		object.setUrl(data.get(URL));
		object.setDescription(data.get(DESCRIPTION));
		object.setFavorite(toBoolean(data.get(FAVORITE)));
		object.setOwner(data.get(OWNER) != null ? new UserIdentifier(data.get(OWNER)) : null);
		object.setKeywords(toList(data.get(KEYWORDS)));
	}

	private List<String> toList(final String string) {
		if (string == null) {
			return new ArrayList<String>();
		}
		else {
			return Arrays.asList(string.split(","));
		}
	}

	private boolean toBoolean(final String string) throws MapException {
		try {
			return parseUtil.parseBoolean(string);
		}
		catch (final ParseException e) {
			throw new MapException("ParseException", e);
		}
	}

	private String toString(final List<String> keywords) {
		if (keywords.isEmpty()) {
			return null;
		}
		else {
			return StringUtils.join(keywords, ",");
		}
	}

	private String toString(final boolean bool) {
		return String.valueOf(bool);
	}

	private BookmarkIdentifier toBookmarkIdentifier(final String id) {
		return id != null ? new BookmarkIdentifier(id) : null;
	}

	private String toString(final BookmarkIdentifier id) {
		return id != null ? id.getId() : null;
	}
}
