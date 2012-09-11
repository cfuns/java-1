package de.benjaminborbe.blog.post;

import java.util.Calendar;
import java.util.Map;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.blog.api.BlogPostIdentifier;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.mapper.BaseMapper;
import de.benjaminborbe.tools.mapper.MapException;
import de.benjaminborbe.tools.util.ParseException;
import de.benjaminborbe.tools.util.ParseUtil;

@Singleton
public class BlogPostBeanMapper extends BaseMapper<BlogPostBean> {

	private static final String ID = "id";

	private static final String TITLE = "title";

	private static final String CONTENT = "content";

	private static final String CREATED = "created";

	private static final String MODIFIED = "modified";

	private static final String AUTHOR_USERNAME = "author";

	private final CalendarUtil calendarUtil;

	private final ParseUtil parseUtil;

	@Inject
	public BlogPostBeanMapper(final Provider<BlogPostBean> provider, final ParseUtil parseUtil, final CalendarUtil calendarUtil) {
		super(provider);
		this.parseUtil = parseUtil;
		this.calendarUtil = calendarUtil;
	}

	@Override
	public void map(final BlogPostBean object, final Map<String, String> data) {
		data.put(ID, toString(object.getId()));
		data.put(TITLE, object.getTitle());
		data.put(CONTENT, object.getContent());
		data.put(CREATED, toString(object.getCreated()));
		data.put(MODIFIED, toString(object.getModified()));
		data.put(AUTHOR_USERNAME, toString(object.getCreator()));
	}

	@Override
	public void map(final Map<String, String> data, final BlogPostBean object) throws MapException {
		object.setId(toBlogPostIdentifier(data.get(ID)));
		object.setTitle(data.get(TITLE));
		object.setContent(data.get(CONTENT));
		try {
			object.setCreated(toCalendar(data.get(CREATED)));
		}
		catch (final ParseException e) {
			throw new MapException("map created failed", e);
		}
		try {
			object.setModified(toCalendar(data.get(MODIFIED)));
		}
		catch (final ParseException e) {
			throw new MapException("map modified failed", e);
		}
		object.setCreator(toUserIdentifier(data.get(AUTHOR_USERNAME)));
	}

	private Calendar toCalendar(final String timestamp) throws ParseException {
		return timestamp != null ? calendarUtil.getCalendar(parseUtil.parseLong(timestamp)) : null;
	}

	private BlogPostIdentifier toBlogPostIdentifier(final String id) {
		return id != null ? new BlogPostIdentifier(id) : null;
	}

	private UserIdentifier toUserIdentifier(final String id) {
		return id != null ? new UserIdentifier(id) : null;
	}

	private String toString(final BlogPostIdentifier id) {
		return id != null ? id.getId() : null;
	}

	private String toString(final Calendar calendar) {
		return calendar != null ? String.valueOf(calendarUtil.getTime(calendar)) : null;
	}

	private String toString(final UserIdentifier creator) {
		return creator != null ? creator.getId() : null;
	}

}
