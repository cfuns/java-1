package de.benjaminborbe.tools.mapper.stringobject;

import java.util.Calendar;

import de.benjaminborbe.tools.mapper.MapperCalendar;

public class StringObjectMapperCalendar<T> extends StringObjectMapperBase<T, Calendar> {

	private final MapperCalendar mapperCalendar;

	public StringObjectMapperCalendar(final String name, final MapperCalendar mapperCalendar) {
		super(name);
		this.mapperCalendar = mapperCalendar;
	}

	@Override
	public String toString(final Calendar value) {
		return mapperCalendar.map(value);
	}

	@Override
	public Calendar fromString(final String value) {
		return mapperCalendar.map(value);
	}

}
