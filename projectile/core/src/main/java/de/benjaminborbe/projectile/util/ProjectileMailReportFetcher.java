package de.benjaminborbe.projectile.util;

import de.benjaminborbe.projectile.api.ProjectileSlacktimeReportInterval;
import de.benjaminborbe.projectile.config.ProjectileConfig;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.DateUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.stream.StreamUtil;
import de.benjaminborbe.tools.synchronize.RunOnlyOnceATime;
import de.benjaminborbe.tools.util.ParseException;
import de.benjaminborbe.tools.util.ParseUtil;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Store;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class ProjectileMailReportFetcher {

	private final class FetchRunnable implements Runnable {

		@Override
		public void run() {
			Store store = null;
			try {

				final String host = projectileConfig.getPop3Hostname();
				final String user = projectileConfig.getPop3Login();
				final String password = projectileConfig.getPop3Password();

				final Session session = Session.getInstance(new Properties());
				store = session.getStore("pop3s");
				store.connect(host, user, password);
				logger.debug("open connection to " + host);

				final Folder fldr = store.getFolder("INBOX");

				fldr.open(Folder.READ_WRITE);

				final int count = fldr.getMessageCount();
				logger.debug(count + " total messages");

				final Map<String, Date> subjectSendDate = new HashMap<>();

				// Message numbers start at 1
				for (int i = 1; i <= count; i++) {
					logger.debug("process message " + i + " started");
					// Get a message by its sequence number
					final Message m = fldr.getMessage(i);
					final String subject = m.getSubject();
					logger.debug("subject: " + subject);
					final Date sendDate = m.getSentDate();
					logger.debug("sendDate: " + dateUtil.dateTimeString(sendDate));
					if (!subjectSendDate.containsKey(subject) || subjectSendDate.get(subject).before(sendDate)) {
						subjectSendDate.put(subject, sendDate);
					}

					final Object content = m.getContent();
					if (content instanceof Multipart) {
						final Multipart mp = (Multipart) content;
						final int count3 = mp.getCount();
						for (int j = 0; j < count3; j++) {
							final BodyPart b = mp.getBodyPart(j);
							final Object attachmentContent = b.getContent();
							if (attachmentContent instanceof InputStream) {
								logger.debug("Filename: " + b.getFileName());
								logger.debug("ContentType: " + b.getContentType());

								final InputStream inputStream = (InputStream) attachmentContent;
								final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
								streamUtil.copy(inputStream, outputStream);

								final Calendar date = filenameToCalendar(b.getFileName());
								final String csvContent = outputStream.toString("ISO-8859-1");
								final ProjectileSlacktimeReportInterval interval = buildInterval(subject);
								projectileCsvReportImporter.importCsvReport(date, csvContent, interval);
							}
						}
					}
					logger.debug("process message " + i + " started");
				}

				if (projectileConfig.getPop3Delete()) {
					for (int i = 1; i <= count; i++) {
						// Get a message by its sequence number
						final Message m = fldr.getMessage(i);
						final String subject = m.getSubject();
						if (m.getSentDate().before(subjectSendDate.get(subject))) {
							// mark mail to delete
							m.setFlag(Flags.Flag.DELETED, true);
							logger.debug("mark mail to delete: " + m.getSubject() + " " + dateUtil.dateTimeString(m.getSentDate()));
						} else {
							logger.debug("leave mail on server: " + m.getSubject() + " " + dateUtil.dateTimeString(m.getSentDate()));
						}
					}
				} else {
					logger.debug("leave mails on server");
				}

				// "true" actually deletes flagged messages from folder
				fldr.close(true);
				logger.debug("close connection to " + host);
			} catch (final ParseException | StorageException | IOException | MessagingException e) {
				logger.warn(e.getClass().getName(), e);
			} finally {
				if (store != null) {
					try {
						store.close();
					} catch (final MessagingException e) {
					}
				}
			}
		}

	}

	private final RunOnlyOnceATime runOnlyOnceATime;

	private final Logger logger;

	private final StreamUtil streamUtil;

	private final ProjectileCsvReportImporter projectileCsvReportImporter;

	private final ProjectileConfig projectileConfig;

	private final DateUtil dateUtil;

	private final ParseUtil parseUtil;

	private final CalendarUtil calendarUtil;

	private final TimeZoneUtil timeZoneUtil;

	@Inject
	public ProjectileMailReportFetcher(
		final Logger logger,
		final CalendarUtil calendarUtil,
		final TimeZoneUtil timeZoneUtil,
		final ParseUtil parseUtil,
		final DateUtil dateUtil,
		final ProjectileConfig projectileConfig,
		final RunOnlyOnceATime runOnlyOnceATime,
		final StreamUtil streamUtil,
		final ProjectileCsvReportImporter projectileCsvReportImporter
	) {
		this.logger = logger;
		this.calendarUtil = calendarUtil;
		this.timeZoneUtil = timeZoneUtil;
		this.parseUtil = parseUtil;
		this.dateUtil = dateUtil;
		this.projectileConfig = projectileConfig;
		this.runOnlyOnceATime = runOnlyOnceATime;
		this.streamUtil = streamUtil;
		this.projectileCsvReportImporter = projectileCsvReportImporter;
	}

	public boolean fetch() {
		logger.trace("fetch projectile-report - started");
		if (runOnlyOnceATime.run(new FetchRunnable())) {
			logger.trace("fetch projectile-report - finished");
			return true;
		} else {
			logger.trace("fetch projectile-report - skipped");
			return false;
		}
	}

	private ProjectileSlacktimeReportInterval buildInterval(final String subject) {
		final String lowerSubject = subject.toLowerCase();
		if (lowerSubject.contains("year") || lowerSubject.contains("jahr")) {
			return ProjectileSlacktimeReportInterval.YEAR;
		}
		if (lowerSubject.contains("month") || lowerSubject.contains("monat")) {
			return ProjectileSlacktimeReportInterval.MONTH;
		}
		if (lowerSubject.contains("week") || lowerSubject.contains("woche")) {
			return ProjectileSlacktimeReportInterval.WEEK;
		}
		throw new IllegalArgumentException("illegal subject");
	}

	protected Calendar filenameToCalendar(final String fileName) throws ParseException {
		try {
			final int pos = fileName.indexOf('_');
			final int year = parseUtil.parseInt(fileName.substring(pos + 1, pos + 5));
			final int month = parseUtil.parseInt(fileName.substring(pos + 5, pos + 7)) - 1;
			final int date = parseUtil.parseInt(fileName.substring(pos + 7, pos + 9));

			final int hour = parseUtil.parseInt(fileName.substring(pos + 9, pos + 11));
			final int min = parseUtil.parseInt(fileName.substring(pos + 11, pos + 13));
			final int sec = parseUtil.parseInt(fileName.substring(pos + 13, pos + 15));

			return calendarUtil.getCalendar(timeZoneUtil.getUTCTimeZone(), year, month, date, hour, min, sec);
		} catch (final Exception e) {
			throw new ParseException(e);
		}
	}
}
