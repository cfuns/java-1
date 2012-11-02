package de.benjaminborbe.task.dao;

import java.util.Calendar;
import java.util.Map;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.task.api.TaskContextIdentifier;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.mapper.BaseMapper;
import de.benjaminborbe.tools.mapper.MapException;
import de.benjaminborbe.tools.util.ParseException;
import de.benjaminborbe.tools.util.ParseUtil;

@Singleton
public class TaskContextBeanMapper extends BaseMapper<TaskContextBean> {

	private static final String ID = "id";

	private static final String NAME = "name";

	private static final String OWNER_USERNAME = "owner";

	private static final String CREATED = "created";

	private static final String MODIFIED = "modified";

	private final CalendarUtil calendarUtil;

	private final ParseUtil parseUtil;

	@Inject
	public TaskContextBeanMapper(final Provider<TaskContextBean> provider, final CalendarUtil calendarUtil, final ParseUtil parseUtil) {
		super(provider);
		this.calendarUtil = calendarUtil;
		this.parseUtil = parseUtil;
	}

	@Override
	public void map(final TaskContextBean object, final Map<String, String> data) {
		data.put(ID, toString(object.getId()));
		data.put(NAME, object.getName());
		data.put(OWNER_USERNAME, object.getOwner() != null ? object.getOwner().getId() : null);
		data.put(MODIFIED, toString(object.getModified()));
		data.put(CREATED, toString(object.getCreated()));
	}

	@Override
	public void map(final Map<String, String> data, final TaskContextBean object) throws MapException {
		object.setId(toTaskContextIdentifier(data.get(ID)));
		object.setName(data.get(NAME));
		object.setOwner(data.get(OWNER_USERNAME) != null ? new UserIdentifier(data.get(OWNER_USERNAME)) : null);
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
	}

	private TaskContextIdentifier toTaskContextIdentifier(final String id) {
		return id != null ? new TaskContextIdentifier(id) : null;
	}

	private String toString(final TaskContextIdentifier id) {
		return id != null ? id.getId() : null;
	}

	private Calendar toCalendar(final String timestamp) throws ParseException {
		return timestamp != null ? calendarUtil.getCalendar(parseUtil.parseLong(timestamp)) : null;
	}

	private String toString(final Calendar calendar) {
		return calendar != null ? String.valueOf(calendarUtil.getTime(calendar)) : null;
	}

}
