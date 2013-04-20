package de.benjaminborbe.tools.date;

import javax.inject.Inject;
import javax.inject.Singleton;
import de.benjaminborbe.tools.util.ParseException;

import java.text.SimpleDateFormat;
import java.util.Date;

@Singleton
public class DateUtilImpl implements DateUtil {

	@Inject
	public DateUtilImpl() {
	}

	@Override
	public String dateString(final Date date) {
		final SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
		return dateformat.format(date);
	}

	@Override
	public String germanDateString(final Date date) {
		final SimpleDateFormat dateformat = new SimpleDateFormat("dd.MM.yyyy");
		return dateformat.format(date);
	}

	@Override
	public String timeString(final Date date) {
		final SimpleDateFormat timeformat = new SimpleDateFormat("HH:mm:ss");
		return timeformat.format(date);
	}

	@Override
	public String dateTimeString(final Date date) {
		final SimpleDateFormat datetimeformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return datetimeformat.format(date);
	}

	@Override
	public Date parseDateTime(final String datetime) throws ParseException {
		final SimpleDateFormat datetimeformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			return datetimeformat.parse(datetime);
		} catch (final NullPointerException | java.text.ParseException e) {
			throw new ParseException(e);
		}
	}

	@Override
	public Date parseDate(final String date) throws ParseException {
		final SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
		try {
			return dateformat.parse(date);
		} catch (final NullPointerException | java.text.ParseException e) {
			throw new ParseException(e);
		}
	}

	@Override
	public boolean isToday(final Date date) {
		final Date now = new Date();
		return dateString(date).equals(dateString(now));
	}

	@Override
	public Date today() {
		final Date now = new Date();
		try {
			return parseDate(dateString(now));
		} catch (final ParseException e) {
			return now;
		}
	}

}
