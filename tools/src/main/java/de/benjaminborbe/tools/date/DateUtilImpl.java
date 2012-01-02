package de.benjaminborbe.tools.date;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class DateUtilImpl implements DateUtil {

	private static final SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");

	private static final SimpleDateFormat timeformat = new SimpleDateFormat("HH:mm:ss");

	private static final SimpleDateFormat datetimeformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@Inject
	public DateUtilImpl() {
	}

	@Override
	public String dateString(final Date date) {
		return dateformat.format(date);
	}

	@Override
	public String timeString(final Date date) {
		return timeformat.format(date);
	}

	@Override
	public String dateTimeString(final Date date) {
		return datetimeformat.format(date);
	}

}
