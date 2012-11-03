package de.benjaminborbe.task.dao;

import java.util.Calendar;
import java.util.Map;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.task.api.TaskIdentifier;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.mapper.BaseMapper;
import de.benjaminborbe.tools.mapper.MapException;
import de.benjaminborbe.tools.util.ParseException;
import de.benjaminborbe.tools.util.ParseUtil;

@Singleton
public class TaskBeanMapper extends BaseMapper<TaskBean> {

	public static final String ID = "id";

	public static final String NAME = "name";

	public static final String COMPLETED = "completed";

	public static final String DESCRIPTION = "description";

	public static final String OWNER = "owner";

	public static final String CREATED = "created";

	public static final String MODIFIED = "modified";

	public static final String START = "start";

	public static final String DUE = "due";

	public static final String DURATION = "duration";

	private final CalendarUtil calendarUtil;

	private final ParseUtil parseUtil;

	@Inject
	public TaskBeanMapper(final Provider<TaskBean> provider, final CalendarUtil calendarUtil, final ParseUtil parseUtil) {
		super(provider);
		this.calendarUtil = calendarUtil;
		this.parseUtil = parseUtil;
	}

	@Override
	public void map(final TaskBean object, final Map<String, String> data) {
		data.put(ID, toString(object.getId()));
		data.put(NAME, object.getName());
		data.put(DESCRIPTION, object.getDescription());
		data.put(OWNER, object.getOwner() != null ? object.getOwner().getId() : null);
		data.put(COMPLETED, toString(object.isCompleted()));
		data.put(MODIFIED, toString(object.getModified()));
		data.put(CREATED, toString(object.getCreated()));
		data.put(START, toString(object.getStart()));
		data.put(DUE, toString(object.getDue()));
		data.put(DURATION, toString(object.getDuration()));
	}

	@Override
	public void map(final Map<String, String> data, final TaskBean object) throws MapException {
		object.setId(toTaskIdentifier(data.get(ID)));
		object.setName(data.get(NAME));
		object.setDescription(data.get(DESCRIPTION));
		object.setOwner(data.get(OWNER) != null ? new UserIdentifier(data.get(OWNER)) : null);
		try {
			object.setCompleted(toBoolean(data.get(COMPLETED)));
		}
		catch (final ParseException e) {
			object.setCompleted(null);
		}
		try {
			object.setCreated(toCalendar(data.get(CREATED)));
		}
		catch (final ParseException e) {
			object.setCreated(null);
		}
		try {
			object.setModified(toCalendar(data.get(MODIFIED)));
		}
		catch (final ParseException e) {
			object.setModified(null);
		}
		try {
			object.setStart(toCalendar(data.get(START)));
		}
		catch (final ParseException e) {
			object.setStart(null);
		}
		try {
			object.setDue(toCalendar(data.get(DUE)));
		}
		catch (final ParseException e) {
			object.setDue(null);
		}
		try {
			object.setDuration(toLong(data.get(DURATION)));
		}
		catch (final ParseException e) {
			object.setDuration(null);
		}
	}

	private long toLong(final String number) throws ParseException {
		return parseUtil.parseLong(number);
	}

	private boolean toBoolean(final String string) throws ParseException {
		return parseUtil.parseBoolean(string);
	}

	private TaskIdentifier toTaskIdentifier(final String id) {
		return id != null ? new TaskIdentifier(id) : null;
	}

	private String toString(final TaskIdentifier id) {
		return id != null ? id.getId() : null;
	}

	private Calendar toCalendar(final String timestamp) throws ParseException {
		return timestamp != null ? calendarUtil.getCalendar(parseUtil.parseLong(timestamp)) : null;
	}

	private String toString(final Calendar calendar) {
		return calendar != null ? String.valueOf(calendarUtil.getTime(calendar)) : null;
	}

	private String toString(final Boolean completed) {
		return String.valueOf(completed);
	}

	private String toString(final Long d) {
		return String.valueOf(d);
	}

}
